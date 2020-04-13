package OLAP;

import Lattice.Dimension;
import Lattice.Level;
import Lattice.Node;

import java.util.Set;

public class QueryUtils {

    public static void main(String[] args) {
        Dimension d1 = new Dimension(new Level[]{
                new Level("A",0),
                new Level("B",0),
                new Level("None",0)
        });
        Dimension d2 = new Dimension(new Level[]{
                new Level("C",0),
                new Level("D", 0)
        });
        Dimension d3 = new Dimension(new Level[]{
                new Level("E",0),
                new Level("F",0)
        });

        Node root = new Node(new Object[][]{
                {d1, new Level("A",0)},
                {d2, new Level("C",0)},
                {d3, new Level("E",0)}
        });
        Node intermediate = new Node(new Object[][]{
                {d1, new Level("B",0)},
                {d2, new Level("C",0)},
                {d3, new Level("E",0)}
        });
        Node child = new Node(new Object[][]{
                {d1, new Level("None",0)},
                {d2, new Level("C",0)},
                {d3, new Level("E",0)}
        });
        child.setMaterializedUpperNode(root);
        System.out.println(frequencyQuery(child));
    }

    public static String nodeToQuery(Node node, Measure measure){
        switch (measure) {
            case FREQUENCY:
                return frequencyQuery(node);
            default: return "Y I K E S";
        }
    }

    private static String frequencyQuery(Node node){
        StringBuilder sb = new StringBuilder();
        sb.append(selectQuery());
        sb.append(", ");
        sb.append("count(");
        //todo add count append here
        sb.append(")");
        return sb.toString();
    }

    private static String selectQuery(){
        return "SELECT *";
    }
    /**
     * In this method we get a construct a from query, where we inner join on our immediate parent view.
     * This way we should never have to "nest" inner joins
     * todo In the greedy algorithm, we assign the correct immediate parent view.
     * */
    private static String fromQuery(Node node){
        StringBuilder sb = new StringBuilder();
        sb.append("FROM ");
        //Find all dimensions where we differ from
        return "";

    }

    private static String whereQuery(Node node){
        return "";
    }
}
