package Lattice;


import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Node {

    private double scale = 1;

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    //These values are set during the greedy algorithm
    private Node materializedUpperNode = null;
    private Node immediateParentNode = null;
    LinkedHashMap<Dimension, Level> dimensions = new LinkedHashMap<>();
    private ArrayList<Node> parents = new ArrayList<>();
    private ArrayList<Node> children = new ArrayList<>();
    private BigInteger ownCost;

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

    public BigInteger getActualCost() {
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

    public void setActualCost(BigInteger actualCost) {
        this.actualCost = actualCost;
    }

    public ArrayList<Node> getParents() {
        return parents;
    }

    private BigInteger actualCost;


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

    public BigInteger calculateOwnCost() {
        BigInteger rowVal = BigInteger.valueOf(0);
        for (Dimension d : this.dimensions.keySet()) {
            if (rowVal.intValue() == 0) {
                rowVal = d.getLevel(d.getLevelIndex(this.dimensions.get(d))).getRows();

            } else {
                rowVal = rowVal.multiply(d.getLevel(d.getLevelIndex(this.dimensions.get(d))).getRows());
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




    public BigInteger getOwnCost() {
        return ownCost;
    }



}
