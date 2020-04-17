package Lattice.GreedyAlgorithm;

import Lattice.GraphManager;
import Lattice.Node;

import java.util.*;

public class GreedyAlgorithm {
    //When we find our own benefit calculation model, this should probably be a double value
    LinkedHashMap<Node, Double> benefitValueTree = new LinkedHashMap<>();
    LinkedHashSet<Node> materializedNodes = new LinkedHashSet<>();
    Set<Node> nodes;
    Node rootNode;

    public GreedyAlgorithm(Set<Node> nodes, Node rootNode){
        this.nodes = nodes;
        this.rootNode = rootNode;
    }

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
        double bestNodeVal = 0;
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
        for(Node child: BFS_GetSubGraph(nodes, n)){
            if(child.getActualCost() > n.getActualCost()){
                child.setActualCost(n.getOwnCost());
                child.setMaterializedUpperNode(n);

            }
        }
    }
    private void initializeGraph(){
        calculateInitialValue(rootNode, nodes);
        updateCurrentBenefit();
    }

    public void updateCurrentBenefit(){
        for(Node n: nodes){
            benefitValueTree.put(n,getBenefit(nodes, n));
        }
    }

    public double getBenefit(Set<Node> nodes, Node currentNode) {
        int benefit = 0;
        for(Node n: BFS_GetSubGraph(nodes, currentNode)){
            if(!(n.getActualCost() < currentNode.getOwnCost())) {
                benefit = benefit + n.getActualCost() - currentNode.getOwnCost();
            }
        }
        return benefit * currentNode.getScale();
    }

    private void calculateInitialValue(Node topNode, Set<Node> keyset){
            int rootNodeCost = topNode.calculateOwnCost();
            for(Node n: keyset){
                n.calculateOwnCost();
                n.setActualCost(rootNodeCost);
                n.setMaterializedUpperNode(topNode);
        }

    }


    public ArrayList<Node> BFS_GetSubGraph(Set<Node> nodes, Node subTreeRootNode) {
        ArrayList<Node> subGraphNodes = new ArrayList<>();
        LinkedHashMap<Node, Boolean> visited = new LinkedHashMap<>();
        for(Node n: nodes){
            visited.put(n, false);
        }
        visited.put(subTreeRootNode, true);
        subGraphNodes.add(subTreeRootNode);
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(subTreeRootNode);
        while (queue.size() != 0) {
            Node s = queue.poll();
            Iterator<Node> it = s.getChildren().iterator();
            while (it.hasNext()) {
                Node i = it.next();
                if (!visited.get(i)) {
                    visited.put(i, true);
                    queue.add(i);
                    subGraphNodes.add(i);
                }
            }
        }
        return subGraphNodes;

    }


}
