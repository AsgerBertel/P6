package OLAP.ViewGeneration;

import Lattice.Dimension;
import Lattice.Level;
import Lattice.Node;
import OLAP.NodeQueryUtils;

import java.util.LinkedHashMap;

public class ViewQueryManager {

    final static String prefix = "cubefrequency.cube.";

    public static void main(String[] args) {
        Dimension d1 = new Dimension(new Level[]{
                new Level("coordinate",0),
                new Level("district",0),
                new Level("None",0)
        });
        Dimension d2 = new Dimension(new Level[]{
                new Level("toptopic",0),
                new Level("subtopic", 0),
                new Level("None",0)
        });
        Dimension d3 = new Dimension(new Level[]{
                new Level("day",0),
                new Level("month",0),
                new Level("None",0)
        });

        Node root = new Node(new Object[][]{
                {d1, new Level("coordinate",0)},
                {d2, new Level("toptopic",0)},
                {d3, new Level("day",0)}
        });
        Node intermediate = new Node(new Object[][]{
                {d1, new Level("coordinate",0)},
                {d2, new Level("subtopic",0)},
                {d3, new Level("day",0)}
        });
        Node child2 = new Node(new Object[][]{
                {d1, new Level("district",0)},
                {d2, new Level("subtopic",0)},
                {d3, new Level("day",0)}
        });
        Node child = new Node(new Object[][]{
                {d1, new Level("coordinate",0)},
                {d2, new Level("None",0)},
                {d3, new Level("day",0)}
        });
        child2.setMaterializedUpperNode(root);
        child2.setImmediateParentNode(intermediate);
        child.setMaterializedUpperNode(root);
        child.setImmediateParentNode(intermediate);
        intermediate.setImmediateParentNode(root);
        intermediate.setMaterializedUpperNode(root);
        System.out.println(createView(child2));
    }

    public static String createView(Node n){
        if(n.isMaterialised()){
            return createMatView(n);
        }
        else if(n.getMaterializedUpperNode().getParents().isEmpty()){
            return createVirtViewFromFactTable(n);
        }else return createVirtViewFromMatNode(n);
    }

    private static String createVirtViewFromFactTable(Node n) {
        String query = "CREATE VIEW " + NodeQueryUtils.getNodeViewName(n)
                + " AS SELECT " + selectQuery(n.getDimensions()) + "\n"
                + "FROM " + prefix + "facttable f \n" + innerJoinQuery(n)
                + "GROUP BY " + aggregateQuery(n);
        return query;
    }
    private static String createVirtViewFromMatNode(Node n) {
        String query = "CREATE VIEW " + NodeQueryUtils.getNodeViewName(n)
                + " AS SELECT " + selectQuery(n.getDimensions()) + "\n"
                + "FROM " + prefix + innerJoinQuery(n)
                + "GROUP BY " + aggregateQuery(n);
        return query;
    }

    private static String aggregateQuery(Node n){
        StringBuilder sb = new StringBuilder();
        for(Dimension d : n.getDimensions().keySet()){
            Level l = n.getDimensions().get(d);
            if(l.getName().equals("None"))
                continue;
            if(l.getName().equals("coordinate")){
                sb.append(prefix + "coordinate.lat,");
                sb.append(prefix + "coordinate.long,");
            }else{
                sb.append(prefix).append(l.getName()).append(".").append(l.getName()).append(",");
                sb.append(prefix).append(l.getName()).append(".").append(l.getName()).append("id").append(",");
            }
        }
        String s = sb.toString();
        if(s.substring(s.length()-1).equals(",")){
            s = s.substring(0,s.length()-1);
        }
        return s;
    }

    private static boolean containsTopicDimension(LinkedHashMap<Dimension,Level> nodeDimensions){
        for(Dimension d : nodeDimensions.keySet()){
            for(Level l : d.getHierarchy()){
                if(l.getName().equals("toptopic")){
                    return !nodeDimensions.get(d).getName().equals("None");
                }
            }
        }
        throw new RuntimeException("No topic dimension found");
    }

    private static String innerJoinQuery(Node n) {
        StringBuilder sb = new StringBuilder();
        if(!containsTopicDimension(n.getDimensions())){
            sb.append("INNER JOIN cubefrequency.cube.toptopic ON f.toptopicid = cubefrequency.cube.toptopic.toptopicid \n");
        }
        for(Dimension d : n.getDimensions().keySet()){
            Level dimension_level = n.getDimensions().get(d);
            if(dimension_level.getName().equals("subtopic")){
                sb.append(
                        "INNER JOIN cubefrequency.cube.toptopic ON f.toptopicid = cubefrequency.cube.toptopic.toptopicid\n" +
                        "INNER JOIN cubefrequency.cube.subtopic ON cubefrequency.cube.subtopic.toptopicid = cubefrequency.cube.toptopic.toptopicid\n");
                continue;
            }
            if(dimension_level.getName().equals("None"))
                continue;
            //set curr_level as top level
            Level curr_level = d.getLevel(0);
            //Fact table inner join INNER JOIN cube.product ON f.productid = product.productid
            sb.append("INNER JOIN ").append(prefix).
                    append(curr_level.getName()).
                    append(" ON ").
                    append("f.").append(curr_level.getName()).append("id = ").append(prefix).
                    append(curr_level.getName()).append(".").append(curr_level.getName()).append("id\n");
            //Push curr_level down
            curr_level = d.getDescendant(curr_level);
            try{
                while(!curr_level.getName().equals("None") && !curr_level.equals(d.getDescendant(dimension_level))){
                    sb.append("INNER JOIN ").append(prefix).
                            append(curr_level.getName()).
                            append(" ON ").
                            append(prefix).append(d.getAscendant(curr_level).getName()).append(".").
                            append(curr_level.getName()).append("id = ").append(prefix).
                            append(curr_level.getName()).append(".").append(curr_level.getName()).append("id\n");

                    curr_level = d.getDescendant(curr_level);
                }
            }catch (Exception e){
                //todo update with custom exception
            }

        }
        return sb.toString();
    }

    private static String createMatView(Node n) {
        String query = "CREATE MATERIALIZED VIEW " + NodeQueryUtils.getNodeViewName(n)
                + "AS SELECT " + selectQuery(n.getDimensions())
                + "FROM ";
        return query;
    }

    private static String selectQuery(LinkedHashMap<Dimension, Level> dimensions){
        StringBuilder sb = new StringBuilder();
        for(Dimension d : dimensions.keySet()){
            Level l = dimensions.get(d);
            if(l.getName().equals("None"))
                continue;
            if(l.getName().equals("coordinate")){
                sb.append(prefix + "coordinate.lat,");
                sb.append(prefix + "coordinate.long,");
            } else{
                sb.append(prefix).append(l.getName()).append(".").append(l.getName()).append(",");
                sb.append(prefix).append(l.getName()).append(".").append(l.getName()).append("id").append(",");
            }
        }
        sb.append("count(cubefrequency.cube.toptopic.toptopic)");
        return sb.toString();
    }
}
