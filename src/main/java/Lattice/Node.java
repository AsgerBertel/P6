package Lattice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Node {
    LinkedHashMap<Dimension, String> dimensions = new LinkedHashMap<>();
    private ArrayList<Node> parents = new ArrayList<>();
    private ArrayList<Node> children = new ArrayList<>();

    public static void main(String[] args) {
        Dimension d1 = new Dimension(new String[]{"P","C","None"});
        Dimension d2 = new Dimension(new String[]{"A","B","None"});
        Node n = new Node(new Object[][]{
                {d1,"C"},
                {d2,"A"}
        });
    }

    public Node() {
    }

    public Node(Object[][] mapArray){
        Map<Dimension,String> temp = Stream.of(mapArray).collect(
                Collectors.toMap(data -> (Dimension) data[0], data -> (String) data[1]));
        this.dimensions.putAll(temp);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void addChildren(ArrayList<Node> children) {
        if(children.isEmpty()){
            int i = 0;
        }

        for(Node n : children){
            if(n.getCombinedDimensionVal()-this.getCombinedDimensionVal() < 2 && !this.children.contains(n)){
                this.children.add(n);
                n.parents.add(this);
            }
        }
    }
    private int getCombinedDimensionVal(){
        int val = 0;
        for(Dimension d : this.dimensions.keySet()){
            val = val + d.getLevel(this.dimensions.get(d));
        }
        return val;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        Node compareNode = (Node) obj;
        for(Dimension d : compareNode.dimensions.keySet()){
            if (!containsStringDimension(compareNode.dimensions.get(d),d))
                return false;
        }
        return true;
    }

    private boolean containsStringDimension(String cmp_s, Dimension cmp_d){
        for(Dimension d : this.dimensions.keySet()){
            if(this.dimensions.get(d).equals(cmp_s) && d.equals(cmp_d)){
                return true;
            }
        }
        return false;
    }

    public void addDimension(String value, Dimension key){
        this.dimensions.put(key,value);
    }
}
