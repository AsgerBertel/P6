package Lattice.GreedyAlgorithm;

import Lattice.GraphManager;
import Lattice.Node;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

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
        materializedNodes.add(bestNode);
        updateActualCost(bestNode);

    }

    private void updateActualCost(Node n){
        n.setActualCost(n.calculateOwnCost());
        for(Node child: n.BFS_GetSubGraph(gm.nodes)){
            if(child.getActualCost() > n.getActualCost()){
                child.setActualCost(n.getOwnCost());
                child.setMaterializedUpperNode(n);

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
        }

    }


}
