package Lattice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Node {
    Map<String, Dimension> dimensions;
    ArrayList<Node> children;

    public static void main(String[] args) {
        Dimension d1 = new Dimension(new String[]{"P","C","None"});
        Dimension d2 = new Dimension(new String[]{"A","B","None"});
        Node n = new Node(new Object[][]{
                {"C",d1},
                {"A",d2}
        });
        int i =0;
    }

    public Node(Object[][] mapArray){
        this.dimensions = Stream.of(mapArray).collect(
                Collectors.toMap(data -> (String) data[0], data -> (Dimension) data[1]));
    }
}
