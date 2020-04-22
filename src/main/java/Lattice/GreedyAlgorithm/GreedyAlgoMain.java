package Lattice.GreedyAlgorithm;

import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.Level;
import Lattice.Node;
import OLAP.NodeQueryUtils;
import OLAP.ViewGeneration.ViewQueryManager;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;
import Sql.QueryManager;

import java.math.BigInteger;
import java.sql.ResultSet;

public class GreedyAlgoMain {
    public static void main(String[] args) {
        Dimension d1, d2, d3, d4;
        Level d1prod, d1cat, d1none;
        Level d2loc, d2dis, d2county, d2cit, d2country, d2none;
        Level d3day, d3month, d3year, d3none;
        Level d4opinion, d4none;
        d1prod = new Level("toptopic", new BigInteger(String.valueOf(70)));
        d1cat = new Level("subtopic", new BigInteger(String.valueOf(1401)));
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
        d1 = new Dimension(new Level[]{d1prod, d1cat, d1none});
        d2 = new Dimension(new Level[]{d2loc, d2dis, d2county, d2cit, d2country, d2none});
        d3 = new Dimension(new Level[]{d3day, d3month, d3year, d3none});
        d4 = new Dimension(new Level[]{d4opinion, d4none});
        GraphManager gm = new GraphManager();
        Node root = new Node(new Object[][]{
                {d1,d1prod},
                {d2,d2loc},
                {d3,d3day},
                {d4,d4opinion}
        });
        //Generate tree and nodes
        ViewQueryManager vqm = new ViewQueryManager(root);
        gm.generateTree(root);
        for(Node n: gm.nodes.keySet()){
            n.setMaterializedUpperNode(root);
        }
        ViewGenerator.generateOnlyVirtualViews(gm.nodes,vqm);
        //Set the size for all nodes
        //Set uppermatirialised node as root for all nodes

        for(Node n: gm.nodes.keySet()){
            try{
                ResultSet rs = ConnectionManager.selectSQL(QueryManager.getViewSize(NodeQueryUtils.getNodeViewName(n)));
                rs.next();
                System.out.println(NodeQueryUtils.getNodeViewName(n) + " : " + rs.getInt(1) + " rows in view");
                n.setViewSize(BigInteger.valueOf(rs.getInt(1)));
            }catch (Exception e){
                System.out.println(e.getMessage());
                n.setViewSize(BigInteger.valueOf(1));
            }
        }
    }
}
