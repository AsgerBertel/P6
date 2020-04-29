package OLAP.ViewGeneration;

import Lattice.Dimension;
import Lattice.Level;
import Lattice.Node;
import OLAP.NodeQueryUtils;

import java.util.LinkedHashMap;

public class ViewQueryManager {
    final static String CUBE_PREFIX = "cubefrequency.cube.";
    final static String PUBLIC_PREFIX = "cubefrequency.public.";
    private static Node root;

    public ViewQueryManager(Node root) {
        ViewQueryManager.root = root;
    }


    public static String createView(Node n){
        if(n.isMaterialised()){
            return createMatView(n);
        }
        else if(n.getMaterializedUpperNode().getParents().isEmpty()){
            return createVirtViewFromFactTable(n);
        }else return createVirtViewFromMatNode(n);
    }

    private static String fromUpperNode(Node child){
        Node upperNode = child.getMaterializedUpperNode();
        if(upperNode.equals(root)){
            return CUBE_PREFIX + "facttable f \n";
        }
        return PUBLIC_PREFIX + NodeQueryUtils.getNodeViewName(upperNode) + " f \n";

    }

    public static String createVirtViewFromFactTable(Node n) {
        String query = "CREATE VIEW " + NodeQueryUtils.getNodeViewName(n)
                + " AS SELECT " + selectQuery(n) + "\n"
                + "FROM " + fromUpperNode(n) + innerJoinQuery(n)
                + "GROUP BY " + groupByQuery(n);
        return query;
    }
    private static String createVirtViewFromMatNode(Node n) {
        String query = "CREATE VIEW " + NodeQueryUtils.getNodeViewName(n)
                + " AS SELECT " + selectQuery(n) + "\n"
                + "FROM " + fromUpperNode(n) + innerJoinQuery(n)
                + "GROUP BY " + groupByQuery(n);
        return query;
    }

    private static String groupByQuery(Node n){
        StringBuilder sb = new StringBuilder();
        for(Dimension d : n.getDimensions().keySet()){
            Level l = n.getDimensions().get(d);
            if(l.getName().equals("None"))
                continue;
            if(l.getName().equals("coordinate")){
                sb.append(CUBE_PREFIX + "coordinate.lat,");
                sb.append(CUBE_PREFIX + "coordinate.long,");
                sb.append(CUBE_PREFIX).append(l.getName()).append(".").append(l.getName()).append("id").append(",");
            }else{
                sb.append(CUBE_PREFIX).append(l.getName()).append(".").append(l.getName()).append(",");
                sb.append(CUBE_PREFIX).append(l.getName()).append(".").append(l.getName()).append("id").append(",");
            }
            try{
                Level descendantLevel = d.getDescendant(l);
                if(descendantLevel.getName().equals("None")){
                    continue;
                }
                sb.append(CUBE_PREFIX).append(l.getName()).append(".").append(descendantLevel.getName()).append("id").append(",");
            }catch (Exception e ){
            }
        }
        String s = sb.toString();
        if(s.substring(s.length()-1).equals(",")){
            s = s.substring(0,s.length()-1);
        }
        if(n.isMaterialised()){
            if(!containsTopicDimension(n.getDimensions())){
                s = s + ", f.toptopicid";
            }
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

    private static Level setInitialCurrLevel(Node n, Dimension currDimension){
        /*if(n.isMaterialised()){
            return currDimension.getLevel(0);
        }
        else*/ return n.getMaterializedUpperNode().getDimensions().get(currDimension);
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
            Level curr_level = setInitialCurrLevel(n,d);
            //Fact table inner join INNER JOIN cube.product ON f.productid = product.productid
            sb.append("INNER JOIN ").append(CUBE_PREFIX).
                    append(curr_level.getName()).
                    append(" ON ").
                    append("f.").append(curr_level.getName()).append("id = ").append(CUBE_PREFIX).
                    append(curr_level.getName()).append(".").append(curr_level.getName()).append("id\n");
            //Push curr_level down
            curr_level = d.getDescendant(curr_level);
            try{
                while(!curr_level.getName().equals("None") && !curr_level.equals(d.getDescendant(dimension_level))){
                    sb.append("INNER JOIN ").append(CUBE_PREFIX).
                            append(curr_level.getName()).
                            append(" ON ").
                            append(CUBE_PREFIX).append(d.getAscendant(curr_level).getName()).append(".").
                            append(curr_level.getName()).append("id = ").append(CUBE_PREFIX).
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
                + " AS SELECT " + selectQuery(n) + "\n"
                + "FROM " + fromUpperNode(n) + innerJoinQuery(n)
                + "GROUP BY " + groupByQuery(n);
        return query;
    }

    private static String selectQuery(Node n){
        StringBuilder sb = new StringBuilder();
        for(Dimension d : n.getDimensions().keySet()){
            Level l = n.getDimensions().get(d);
            if(l.getName().equals("None"))
                continue;
            if(l.getName().equals("coordinate")){
                sb.append(CUBE_PREFIX + "coordinate.lat,");
                sb.append(CUBE_PREFIX + "coordinate.long,");
                sb.append(CUBE_PREFIX).append(l.getName()).append(".").append(l.getName()).append("id").append(",");

            } else{
                sb.append(CUBE_PREFIX).append(l.getName()).append(".").append(l.getName()).append(",");
                sb.append(CUBE_PREFIX).append(l.getName()).append(".").append(l.getName()).append("id").append(",");
            }
            //add descendant id
            try{
                Level descendantLevel = d.getDescendant(l);
                if(descendantLevel.getName().equals("None")){
                    continue;
                }
                sb.append(CUBE_PREFIX).append(l.getName()).append(".").append(descendantLevel.getName()).append("id").append(",");
            }catch (Exception e ){
            }

        }
        //Select count/sum
        sb.append(aggregateFunction(n));
        if(n.isMaterialised()){
            //If topic dimension is None
            if(!containsTopicDimension(n.getDimensions())){
                sb.append(", f.toptopicid");
            }
        }
        return sb.toString();
    }

    private static String aggregateFunction(Node n) {
        if(!n.getMaterializedUpperNode().equals(root) && !n.isMaterialised()){
            return "sum(f.count)";
        }else{
            for(Level l : n.getDimensions().values()){
                if(l.getName().equals("toptopic")){
                    return"count(cubefrequency.cube.toptopic.toptopic)";
                } else if(l.getName().equals("subtopic")){
                    return"count(cubefrequency.cube.subtopic.subtopic)";
                }
            }
        }
        //If we dont have top topic or sub topic, it means we have None. In that case return toptopic count
        return"count(cubefrequency.cube.toptopic.toptopic)";
    }

}
