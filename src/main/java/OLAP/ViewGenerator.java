package OLAP;

import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.GreedyAlgorithm.GreedyAlgorithm;
import Lattice.Level;
import Lattice.Node;
import OLAP.ViewGeneration.ViewQueryManager;
import Sql.ConnectionManager;
import Sql.QueryManager;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class ViewGenerator {
    public static void generateOnlyVirtualViews(LinkedHashMap<Node,Node> nodes, Node root){
        ConnectionManager.updateSql(QueryManager.dropSchemaPublic);
        ViewQueryManager vqm = new ViewQueryManager(root);
        boolean isNNNN = false;
        for(Node n : nodes.keySet()){
            //IF NONE NONE NONE NONE skip
            for(Level l : n.getDimensions().values()){
                if(!l.getName().equals("None")){
                    isNNNN = false;
                    break;
                }
                isNNNN = true;
            }
            if(isNNNN)
                continue;
            //if(!n.isMaterialised()){
                ConnectionManager.updateSql(vqm.createVirtViewFromFactTable(n));
            //}
        }
    }

    private static void deleteViews(LinkedHashMap<Node,Node> nodes){
        //Get resultset of all existing virtual views and material views.
        ArrayList<String> materialisedNodeNames = new ArrayList<>();
        //put all mat node names into a list
        for(Node n: nodes.keySet()){
            if(n.isMaterialised()){
                materialisedNodeNames.add(NodeQueryUtils.getNodeViewName(n));
            }
        }
        try {
            ResultSet virtViews = ConnectionManager.selectSQL(QueryManager.selectAllVirtualViewNames);
            while(virtViews.next()){
                //delete every view
                ConnectionManager.updateSql(QueryManager.dropVirtualView(virtViews.getString(1)));
            }
            ResultSet matViews = ConnectionManager.selectSQL(QueryManager.selectAllVirtualViewNames);
            while(matViews.next()){
                //Only delete views that are no longer going to be materialised
                if(!materialisedNodeNames.contains(matViews.getString(1))){
                    ConnectionManager.updateSql(QueryManager.dropMaterializedView(matViews.getString(1)));
                }
            }
        } catch (SQLException e) {
            System.exit(420);
        }

    }
    public static void generateViews(LinkedHashMap<Node,Node> nodes, Node root){
        deleteViews(nodes);
        //We should only drop views that
        ViewQueryManager vqm = new ViewQueryManager(root);
        boolean isNNNN = false;
        for(Node n : nodes.keySet()){
            if(n.isMaterialised()){ ;
                ConnectionManager.updateSql(vqm.createView(n));
            }
        }
        int i = 0;
        for(Node n : nodes.keySet()){
            //IF NONE NONE NONE NONE skip
            for(Level l : n.getDimensions().values()){
                if(!l.getName().equals("None")){
                    isNNNN = false;
                    break;
                }
                isNNNN = true;
            }
            if(isNNNN)
                continue;
            if(!n.isMaterialised()){
                System.out.println("THIS: "+NodeQueryUtils.getNodeViewName(n));
                System.out.println("UPPER: " + NodeQueryUtils.getNodeViewName(n.getMaterializedUpperNode()));
                ConnectionManager.updateSql(vqm.createView(n));
            }
    }
    }
    public static void main(String[] args) throws SQLException {
        Dimension d1, d2, d3, d4;
        Level d1topic, d1subtopic, d1none;
        Level d2loc, d2dis, d2county, d2cit, d2country, d2none;
        Level d3day, d3month, d3year, d3none;
        Level d4opinion, d4none;
        d1topic = new Level("toptopic", new BigInteger(String.valueOf(70)));
        //d1subtopic = new Level("subtopic", new BigInteger(String.valueOf(1401)));
        d1none = new Level("None", new BigInteger(String.valueOf(1)));
        d2loc = new Level("coordinate", new BigInteger(String.valueOf(3861358)));
        d2dis = new Level("district", new BigInteger(String.valueOf(143)));
        d2county = new Level("county", new BigInteger(String.valueOf(15)));
        d2cit = new Level("city", new BigInteger(String.valueOf(2)));
        d2country = new Level("country", new BigInteger(String.valueOf(1)));
        d2none = new Level("None", new BigInteger(String.valueOf(1)));
        d3day = new Level("day", new BigInteger(String.valueOf(19)));
        d3month = new Level("month", new BigInteger(String.valueOf(3)));
        d3year = new Level("year", new BigInteger(String.valueOf(1)));
        d3none = new Level("None", new BigInteger(String.valueOf(1)));
        d4opinion = new Level("opinion", new BigInteger(String.valueOf(3)));
        d4none = new Level("None", new BigInteger(String.valueOf(1)));
        d1 = new Dimension(new Level[]{d1topic, d1none});
        d2 = new Dimension(new Level[]{d2loc, d2dis, d2county, d2cit, d2country, d2none});
        d3 = new Dimension(new Level[]{d3day, d3month, d3year, d3none});
        d4 = new Dimension(new Level[]{d4opinion, d4none});

        Node root = new Node(new Object[][]{
                {d1,d1topic},
                {d2,d2loc},
                {d3,d3day},
                {d4,d4opinion}
        });
        GraphManager gm = new GraphManager(root);
        gm.generateTree(root);
        GreedyAlgorithm ga = new GreedyAlgorithm(gm.nodes.keySet(),root);
        HashSet<Node> nodies = ga.materializeNodes(4);
        for(Node n : gm.nodes.keySet()){
            if(nodies.contains(n))
                n.setMaterialised(true);
        }
        System.out.println("Materialised Nodes");
        generateViews(gm.nodes,root);
    }
}
