package Lattice.GreedyAlgorithm;

import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.Level;
import Lattice.Node;
import OLAP.NodeQueryUtils;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;
import Sql.QueryManager;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GreedyAlgorithm {
    //When we find our own benefit calculation model, this should probably be a double value
    public LinkedHashMap<Node, Double> benefitValueTree = new LinkedHashMap<>();
    LinkedHashSet<Node> materializedNodes = new LinkedHashSet<>();
    GraphManager gm;
    Node rootNode;

    public GreedyAlgorithm(GraphManager gm){
        this.gm = gm;
        this.rootNode = gm.getTopNode();
    }

    public void resetNodes(){
        //resets all nodes
        benefitValueTree = new LinkedHashMap<>();
        materializedNodes = new LinkedHashSet<>();
        for(Node n : this.gm.nodes.keySet()){
            n.setMaterialised(false);
            n.setMaterializedUpperNode(null);
            n.setScale(1);
        }
    }

    public LinkedHashSet<Node> materializeNodes(int amountOfNodesToMaterialize) throws SQLException {
        initializeGraph();
        for(int i = 0; i < amountOfNodesToMaterialize; i++){
            //printBenefitTree();
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
        bestNode.setMaterialised(true);
        updateActualCost(bestNode);

    }

    private void updateActualCost(Node n){
        n.setActualCost(n.getViewSize());
        for(Node child: BFS_GetSubGraph(this.gm.nodes.keySet(), n)){
            if(child.getActualCost().compareTo(n.getActualCost()) > 0){
                child.setActualCost(n.getViewSize());
                child.setMaterializedUpperNode(n);

            }
        }
    }
    public void initializeGraph() throws SQLException {
        //reset in case we've already calculated once
        resetNodes();
        calculateInitialValue(rootNode, this.gm.nodes.keySet());
        updateCurrentBenefit();
    }

    public void updateCurrentBenefit(){
        for(Node n: this.gm.nodes.keySet()){
            benefitValueTree.put(n,getBenefit(this.gm.nodes.keySet(),n));
        }
    }

    public double getBenefit(Set<Node> nodes, Node currentNode) {
        BigInteger benefit = BigInteger.valueOf(0);
        for(Node n: BFS_GetSubGraph(nodes, currentNode)){
            if(!(n.getActualCost().compareTo(currentNode.getViewSize()) < 0 )) {
                benefit = benefit.add(n.getActualCost().subtract(currentNode.getViewSize()));
            }
        }

        return benefit.doubleValue() * currentNode.getScale();
    }

    private void calculateInitialValue(Node topNode, Set<Node> keyset) throws SQLException {
        ResultSet rs = ConnectionManager.selectSQL(QueryManager.selectAllFromViewsize);
        HashMap<String,Long> viewNameSizeMap = new HashMap<>();
        while(rs.next()){
            viewNameSizeMap.put(rs.getString(1).toLowerCase(),rs.getLong(2));
        }
        BigInteger rootNodeCost = BigInteger.valueOf(viewNameSizeMap.get(NodeQueryUtils.getNodeViewName(topNode)));
            for(Node n: keyset){
                n.setViewSize(new BigInteger(String.valueOf(viewNameSizeMap.get(NodeQueryUtils.getNodeViewName(n)))));
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

    private void printBenefitTree(){
        System.out.println("Benefit:");
        for(Node n: benefitValueTree.keySet()){
            System.out.println(NodeQueryUtils.getNodeViewName(n) + " : " + benefitValueTree.get(n)+ " scale:"+n.getScale());
        }
    }
}
