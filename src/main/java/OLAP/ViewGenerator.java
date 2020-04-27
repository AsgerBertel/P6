package OLAP;

import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.GreedyAlgorithm.GreedyAlgorithm;
import Lattice.GreedyAlgorithm.GreedyAlgorithmType;
import Lattice.GreedyAlgorithm.GreedyPopularityAlgorithm;
import Lattice.Level;
import Lattice.Node;
import OLAP.ViewGeneration.ViewQueryManager;
import Sql.ConnectionManager;
import Sql.QueryManager;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ViewGenerator {
    GreedyAlgorithm ga;
    GraphManager gm;
    Node root;

    public void generateOnlyVirtualViews(LinkedHashMap<Node,Node> nodes, Node root){
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

    private ArrayList<String> getMaterialisedViewNames() throws SQLException {
        ResultSet matViews =  ConnectionManager.selectSQL(QueryManager.selectAllMaterializedlViewNames);
        ArrayList<String> matViewNames = new ArrayList<>();
        while(matViews.next()){
            matViewNames.add(matViews.getString(1));
        }
        return matViewNames;

    }

    private void deleteViews(LinkedHashMap<Node,Node> nodes){
        //Get resultset of all existing virtual views and material views.
        ArrayList<String> materialisedNodeNames = new ArrayList<>();
        //put all mat node names into a list
        for(Node n: nodes.keySet()){
            if(n.isMaterialised()){
                materialisedNodeNames.add(NodeQueryUtils.getNodeViewName(n).toLowerCase());
            }
        }
        try {
            ResultSet virtViews = ConnectionManager.selectSQL(QueryManager.selectAllVirtualViewNames);
            while(virtViews.next()){
                //delete every view
                ConnectionManager.updateSql(QueryManager.dropVirtualView(virtViews.getString(1)));
            }
            ArrayList<String> matViews = getMaterialisedViewNames();
           for(String s : matViews){
               if(!materialisedNodeNames.contains(s)){
                   ConnectionManager.updateSql(QueryManager.dropMaterializedView(s));
               }
           }
        } catch (SQLException e) {
            System.exit(420);
        }

    }
    public void generateViews(LinkedHashMap<Node,Node> nodes, Node root) throws SQLException {
        deleteViews(nodes);
        //We should only drop views that
        ViewQueryManager vqm = new ViewQueryManager(root);
        boolean isNNNN = false;
        ArrayList<String> matViews = getMaterialisedViewNames();
        for(Node n : nodes.keySet()){
            if(matViews.contains(NodeQueryUtils.getNodeViewName(n).toLowerCase())){
                continue;
            }
            if(n.isMaterialised()){
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
                ConnectionManager.updateSql(vqm.createView(n));
            }
        }
        System.out.println("finished creating views");
    }

    public void init(GreedyAlgorithmType type){
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

        this.root = new Node(new Object[][]{
                {d1,d1topic},
                {d2,d2loc},
                {d3,d3day},
                {d4,d4opinion}
        });
        this.gm = new GraphManager(root);
        gm.generateTree(root);
        if(type.equals(GreedyAlgorithmType.BASE)){
            this.ga = new GreedyAlgorithm(gm.nodes.keySet(),root);
        }else{
            this.ga = new GreedyPopularityAlgorithm(gm.nodes.keySet(),root);
        }

    }
    public void updateViews() throws SQLException {
        ga.materializeNodes(4);
        generateViews(gm.nodes,root);
    }
}
