package OLAP;

import Lattice.Dimension;
import Lattice.Level;
import Lattice.Node;



public class NodeQueryUtils {

    public static void main(String[] args) {
        Dimension d1 = new Dimension(new Level[]{
                new Level("Coordinate",0),
                new Level("District",0),
                new Level("None",0)
        });
        Dimension d2 = new Dimension(new Level[]{
                new Level("TopTopic",0),
                new Level("SubTopic", 0)
        });
        Dimension d3 = new Dimension(new Level[]{
                new Level("Day",0),
                new Level("Month",0)
        });

        Node root = new Node(new Object[][]{
                {d1, new Level("Coordinate",0)},
                {d2, new Level("TopTopic",0)},
                {d3, new Level("Day",0)}
        });
        Node intermediate = new Node(new Object[][]{
                {d1, new Level("District",0)},
                {d2, new Level("TopTopic",0)},
                {d3, new Level("Day",0)}
        });
        Node child = new Node(new Object[][]{
                {d1, new Level("None",0)},
                {d2, new Level("TopTopic",0)},
                {d3, new Level("Day",0)}
        });
        child.setMaterializedUpperNode(root);
        child.setImmediateParentNode(intermediate);
        intermediate.setImmediateParentNode(root);
        intermediate.setMaterializedUpperNode(root);
        Dimension d = getDescendantDimension(child);
        System.out.println(frequencyView(child));

    }

    public static String nodeToQuery(Node node, Measure measure){
        switch (measure) {
            case FREQUENCY:
                return frequencyView(node);
            default: return "Y I K E S";
        }
    }

    private static String createViewQuery(Node node){
        return "CREATE VIEW " +
                getNodeViewName(node) +
                " AS ";
    }
    private static boolean containsNoneDimension(Node node){
        for(Level l : node.getDimensions().values()){
            if(l.getName().equals("None")){
                return true;
            }
        }
        return false;
    }
    private static String frequencyView(Node node){
        if(containsNoneDimension(node)){
            return frequencyNoneView(node);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(createViewQuery(node));
        sb.append("\n");
        sb.append(selectQuery());
        sb.append(", ");
        sb.append("count(");
        //todo add count append here
        sb.append(")").append("\n");
        sb.append(fromQuery(node));
        return sb.toString();
    }

    private static String frequencyNoneView(Node node) {
        //Select all dimension except for the one with None

        return "";
    }

    private static Dimension getDescendantDimension(Node child){
        //This function compares the child node to its immediate parent and returns the dimension
        //where the child has a descendant dimension from the parent
        //It returns an array
        for(Dimension parentDimension : child.getImmediateParentNode().getDimensions().keySet()){
            for(Dimension childDimension : child.getDimensions().keySet()){
                if(parentDimension.getDescendant(child.getImmediateParentNode().getDimensions().get(parentDimension))
                        .equals(child.getDimensions().get(childDimension))){
                    return parentDimension;
                }
            }
        }
        throw new RuntimeException("No descendant dimension found");
    }

    private static String selectQuery(){
        return "SELECT *";
    }
    /**
     * In this method we get a construct a from query, where we inner join on our immediate parent view.
     * This way we should never have to "nest" inner joins
     * */
    private static String fromQuery(Node node){
        StringBuilder sb = new StringBuilder();
        Dimension diff_dimension = getDescendantDimension(node);
        sb.append("FROM ");
        sb.append(getNodeViewName(node.getImmediateParentNode()));
        sb.append(" INNER JOIN ");
        sb.append(node.getDimensions().get(diff_dimension).getName());
        sb.append(" ON ");
        sb.append(node.getDimensions().get(diff_dimension).getName()).append(".id");
        sb.append(" = ").append(getNodeViewName(node.getImmediateParentNode())).append(".").append(node.getDimensions().get(diff_dimension).getName()).append("id");
        return sb.toString();
    }
    private static String getNodeViewName(Node node){
        StringBuilder sb = new StringBuilder();
        for(Dimension d: node.getDimensions().keySet()){
            sb.append(node.getDimensions().get(d).getName());
        }
        return sb.toString();
    }

    private static String whereQuery(Node node){
        return "";
    }
}
