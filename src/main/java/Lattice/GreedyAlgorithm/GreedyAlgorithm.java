package Lattice.GreedyAlgorithm;

import Lattice.GraphManager;
import Lattice.Node;

import java.util.*;

public class GreedyAlgorithm {
    //When we find our own benefit calculation model, this should probably be a double value
    LinkedHashMap<Node, Integer> benefitValueTree = new LinkedHashMap<>();
    LinkedHashSet<Node> materializedNodes = new LinkedHashSet<>();
    GraphManager gm= new GraphManager();

    public HashSet<Node> materializeNodes(int amountOfNodesToMaterialize){
        initializeGraph();
        for(int i = 0; i < amountOfNodesToMaterialize; i++){
            selectHighestBenefit();
            updateCurrentBenefit();
        }
        return materializedNodes;
    }

    private void selectHighestBenefit(){
        Node bestNode = null;
        int bestNodeVal = 0;
        for(Node n: benefitValueTree.keySet()){
            if((bestNode == null || benefitValueTree.get(n) > bestNodeVal) && !materializedNodes.contains(n)){
                bestNode = n;
                bestNodeVal = benefitValueTree.get(n);
            }
        }
        if (bestNode != null) {
            bestNode.setMaterialised(true);
            materializedNodes.add(bestNode);
            updateActualCost(bestNode);
        } else throw new RuntimeException("No best node found - Greedy algo bugged?");
        //todo better exception desc/custom exception
    }

    private void updateActualCost(Node n){
        n.setActualCost(n.calculateOwnCost());
        ArrayList<Node> subgraphNodes = n.BFS_GetSubGraph(gm.nodes);
        for(Node child: subgraphNodes){
            if(child.getActualCost() > n.getActualCost()){
                child.setActualCost(n.getOwnCost());
                child.setMaterializedUpperNode(n);
            }
        }
        updateImmediateParent(n,subgraphNodes);
    }
    private void updateImmediateParent(Node materialisedNode, ArrayList<Node> subgraphNodes){
        for(Node n : subgraphNodes){
            if(n.getActualCost() < materialisedNode.getActualCost()){
                continue;
            }
            if(n.equals(materialisedNode) || n.getParents().contains(materialisedNode)){
                n.setImmediateParentNode(materialisedNode);
            }else{
                for(Node parent : n.getParents()){
                    if(parent.getActualCost() == materialisedNode.getOwnCost()){
                        n.setImmediateParentNode(parent);
                        break;
                    }
                }
                //If there is no immediateParentNode throw an exception
                if(n.getImmediateParentNode() == null){
                    throw new RuntimeException("No immediate parent node has been set");
                    //todo custom exception/better desc
                }
            }
        }
    }
    private void initializeGraph(){
        gm.greedyAlgBaseTest();
        calculateInitialValue(gm.getTopNode(), gm.nodes.keySet());
        updateCurrentBenefit();
    }

    private void updateCurrentBenefit(){
        for(Node n: gm.nodes.keySet()){
            benefitValueTree.put(n,n.getBenefit(gm.nodes));
        }
    }
    private void calculateInitialValue(Node topNode, Set<Node> keyset){
            int rootNodeCost = topNode.calculateOwnCost();
            for(Node n: keyset){
                n.calculateOwnCost();
                n.setActualCost(rootNodeCost);
                n.setMaterializedUpperNode(topNode);
                n.setImmediateParentNode(topNode);
        }

    }


}
