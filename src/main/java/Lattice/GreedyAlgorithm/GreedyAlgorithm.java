package Lattice.GreedyAlgorithm;

import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.Node;
import nu.xom.Nodes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class GreedyAlgorithm {
    //When we find our own benefit calculation model, this should probably be a double value
    LinkedHashMap<Node, Integer> benefitValueTree = new LinkedHashMap<>();
    HashSet<Node> materializedNodes = new HashSet<>();
    GraphManager gm= new GraphManager();

    public HashSet<Node> materializeNodes(int amountOfNodesToMaterialize){
        for(int i = 0; i < amountOfNodesToMaterialize; i++){
            selectHighestBenefit();
        }
        return materializedNodes;
    }

    public void selectHighestBenefit(){
        calcBenefitValue();
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

    public void updateActualCost(Node n){
        n.setActualCost(n.calculateOwnCost());
        for(Node child: n.BFS_GetSubgraph(gm.nodes)){
            if(child.getActualCost() > n.getActualCost()){
                child.setActualCost(n.getOwnCost());
                child.setMaterializedUpperNode(n);
            }
        }
    }
    public void calcBenefitValue(){
        gm.greedyAlgBaseTest();
        calculateInitialValue(gm.getTopNode(), gm.nodes.keySet());

        for(Node n: gm.nodes.keySet()){
            benefitValueTree.put(n,n.getBenefit(gm.nodes));
        }

    }

    public void calculateInitialValue(Node topNode, Set<Node> keyset){
            int rootNodeCost = topNode.calculateOwnCost();
            for(Node n: keyset){
                n.calculateOwnCost();
                n.setActualCost(rootNodeCost);
                n.setMaterializedUpperNode(topNode);
        }

    }


}
