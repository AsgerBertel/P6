package Lattice;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Node {
    //These values are set during the greedy algorithm
    private Node materializedUpperNode = null;
    private Node immediateParentNode = null;
    LinkedHashMap<Dimension, Level> dimensions = new LinkedHashMap<>();
    private ArrayList<Node> parents = new ArrayList<>();
    private ArrayList<Node> children = new ArrayList<>();
    private int ownCost;

    private boolean isMaterialised = false;

    public LinkedHashMap<Dimension, Level> getDimensions() {
        return dimensions;
    }
    public boolean isMaterialised() {
        return isMaterialised;
    }

    public void setMaterialised(boolean materialised) {
        isMaterialised = materialised;
    }

    public int getActualCost() {
        return actualCost;
    }

    public Node getMaterializedUpperNode() {
        return materializedUpperNode;
    }

    public Node getImmediateParentNode() {
        return immediateParentNode;
    }

    public void setImmediateParentNode(Node immediateParentNode) {
        this.immediateParentNode = immediateParentNode;
    }

    public void setMaterializedUpperNode(Node materializedUpperNode) {
        this.materializedUpperNode = materializedUpperNode;
    }

    public void setActualCost(int actualCost) {
        this.actualCost = actualCost;
    }

    public ArrayList<Node> getParents() {
        return parents;
    }

    private int actualCost;


    public Node() {}

    public Node(Object[][] mapArray){
        Map<Dimension, Level> temp = Stream.of(mapArray).collect(
                Collectors.toMap(data -> (Dimension) data[0], data -> (Level) data[1]));
        this.dimensions.putAll(temp);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void addChildren(ArrayList<Node> children) {
        if (children.isEmpty()) {
            int i = 0;
        }

        for (Node n : children) {
            if (n.getCombinedDimensionVal() - this.getCombinedDimensionVal() < 2 && !this.children.contains(n)) {
                this.children.add(n);
                n.parents.add(this);
            }
        }
    }

    private int getCombinedDimensionVal() {
        int val = 0;
        for (Dimension d : this.dimensions.keySet()) {
            val = val + d.getLevelIndex(this.dimensions.get(d));
        }
        return val;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public int calculateOwnCost() {
        int rowVal = 0;
        for (Dimension d : this.dimensions.keySet()) {
            if (rowVal == 0) {
                rowVal = d.getLevel(d.getLevelIndex(this.dimensions.get(d))).getRows();

            } else {
                rowVal = rowVal * d.getLevel(d.getLevelIndex(this.dimensions.get(d))).getRows();
            }
        }
        this.ownCost = rowVal;
        return rowVal;
    }

    @Override
    public boolean equals(Object obj) {
        Node compareNode = (Node) obj;
        for (Dimension d : compareNode.dimensions.keySet()) {
            if (!containsLevelDimension(compareNode.dimensions.get(d), d))
                return false;
        }
        return true;
    }

    private boolean containsLevelDimension(Level cmp_l, Dimension cmp_d) {
        for (Dimension d : this.dimensions.keySet()) {
            if (this.dimensions.get(d).equals(cmp_l) && d.equals(cmp_d)) {
                return true;
            }
        }
        return false;
    }

    public void addDimension(Level value, Dimension key) {
        this.dimensions.put(key, value);
    }


    public int getBenefit(HashMap<Node, Node> nodes) {
        int benefit = 0;
        for(Node n: BFS_GetSubGraph(nodes)){
            if(!(n.actualCost < this.ownCost)) {
                benefit = benefit + n.actualCost - this.ownCost;
            }
        }
        return benefit;
    }

    public int getOwnCost() {
        return ownCost;
    }

    public ArrayList<Node> BFS_GetSubGraph(HashMap<Node, Node> nodes) {
        ArrayList<Node> subGraphNodes = new ArrayList<>();
        LinkedHashMap<Node, Boolean> visited = new LinkedHashMap<>();
        for(Node n: nodes.keySet()){
            visited.put(n, false);
        }
        visited.put(this, true);
        subGraphNodes.add(this);
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(this);
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
