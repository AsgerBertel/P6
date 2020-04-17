package Lattice;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Node {
    public LinkedHashMap<Dimension, Level> getDimensions() {
        return dimensions;
    }

    private Node materializedUpperNode = null;

    public int getActualCost() {
        return actualCost;
    }

    public Node getMaterializedUpperNode() {
        return materializedUpperNode;
    }

    public void setMaterializedUpperNode(Node materializedUpperNode) {
        this.materializedUpperNode = materializedUpperNode;
    }

    LinkedHashMap<Dimension, Level> dimensions = new LinkedHashMap<>();
    private ArrayList<Node> parents = new ArrayList<>();
    private ArrayList<Node> children = new ArrayList<>();
    private int ownCost;

    public void setActualCost(int actualCost) {
        this.actualCost = actualCost;
    }

    private int actualCost;

    public static void main(String[] args) {
        /*
        Dimension d1 = new Dimension(new String[]{"P","C","None"}, new int []{});
        Dimension d2 = new Dimension(new String[]{"A","B","None"}, new int[]{});
        Node n = new Node(new Object[][]{
                {d1,"C"},
                {d2,"A"}
        });

         */
    }

    public Node() {
    }

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




    public int getOwnCost() {
        return ownCost;
    }



}
