import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.GreedyAlgorithm.GreedyAlgorithm;
import Lattice.GreedyAlgorithm.GreedyPopularityAlgorithm;
import Lattice.Level;
import Lattice.Node;
import Sql.ConnectionManager;
import Sql.QueryManager;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LatticeGraphTests {
    Dimension d1,d2,d3,d4;
    Level d1prod,d1cat,d1none;
    Level d2loc,d2dis,d2county,d2cit,d2country,d2none;
    Level d3day,d3month,d3year,d3none;
    Level d4opinion,d4none;
    @Before
    public void init(){
        d1prod = new Level("toptopic", new BigInteger(String.valueOf(70)));
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
        d1 = new Dimension(new Level[]{d1prod,d1none});
        d2 = new Dimension(new Level[]{d2loc,d2dis,d2county,d2cit,d2country,d2none});
        d3 = new Dimension(new Level[]{d3day,d3month,d3year,d3none});
        d4 = new Dimension(new Level[]{d4opinion,d4none});
    }


    @Test
    public void visitAllNodesTest(){

        //Create the root node
        Node root = new Node(new Object[][]{
                {d1,d1prod},
                {d2,d2loc},
                {d3,d3day},
                {d4,d4opinion}
        });
        GraphManager gm = new GraphManager(root);
        gm.nodes.put(root,root);
        gm.generateTree(root);
        assertEquals(96, gm.nodes.size());
        //add all nodes to a <Node,Boolean> Map
        LinkedHashMap<Node,Boolean> visitedMap = new LinkedHashMap<>();
        for(Node n : gm.nodes.keySet()){
            visitedMap.put(n,false);
        }
        //Do a BFS
        BFS(root,visitedMap);
        //Check that all values are true now
        for(Node n : visitedMap.keySet()){
            assertTrue(visitedMap.get(n));
        }
    }
    private void BFS(Node root, LinkedHashMap<Node,Boolean> visited){
        visited.put(root,true);
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);
        while(queue.size() != 0){
            Node s = queue.poll();
            Iterator<Node> it = s.getChildren().iterator();
            while(it.hasNext()){
                Node i = it.next();
                if(!visited.get(i)){
                    visited.put(i,true);
                    queue.add(i);
                }
            }
        }

    }
    @Test
    public void correctNumberOfDifferingDimensionsFromParentTest(){
        //Create the root node
        Node root = new Node(new Object[][]{
                {d1,d1prod},
                {d2,d2loc},
                {d3,d3day},
                {d4,d4opinion}
        });

       GraphManager gm = new GraphManager(root);

        gm.nodes.put(root,root);
        gm.generateTree(root);

        int counter = 0;
        ArrayList<Level> parentLevels = new ArrayList<>();
        ArrayList<Level> childLevels = new ArrayList<>();

        for(Node n: gm.nodes.keySet()){
            parentLevels = getLevelsInNode(n);
            for(Node child: n.getChildren()){
                childLevels = getLevelsInNode(child);
                for(int i = 0; i < parentLevels.size();i++){
                    if(parentLevels.get(i) != childLevels.get(i)){
                        counter++;
                    }
                }
                assertEquals(1, counter);
                counter = 0;
            }
        }
    }
    public ArrayList<Level> getLevelsInNode(Node n){
        ArrayList<Level> nodeDimensions = new ArrayList<>();
        for(Dimension d: n.getDimensions().keySet()){
            nodeDimensions.add(n.getDimensions().get(d));
        }
        return nodeDimensions;
    }
 /*
    @Test
    public void getBenefittest(){
        GraphManager gm = new GraphManager();
        //Create the root node
        Node root = new Node(new Object[][]{
                {d1,d1cat},
                {d2,d2cit},
                {d3,d3month},
                {d4,d4opinion}
        });
        gm.nodes.put(root,root);
        gm.generateTree(root);

        root.getViewSize();
        GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(gm.nodes.keySet(), root);
        greedyAlgorithm.initializeGraph();
        greedyAlgorithm.updateActualCost(root);
        LinkedHashMap<Node,Boolean> visitedMap = new LinkedHashMap<>();
        for(Node n : gm.nodes.keySet()){
            visitedMap.put(n,false);
        }
        BFS(root, visitedMap);

        //Calculate root node cost
        BigInteger totalCost = d1cat.getRows().multiply(d2cit.getRows().multiply(d3month.getRows().multiply(d4opinion.getRows())));
        assertEquals(totalCost, root.getActualCost());
    }
    */
    @Test
    public void identicalBenefitTreeTest() throws SQLException {
        //Given two greedy algos (pop and base) we check that given no popularity, they give identical results
        //Generate the lattice graph
        Node root = new Node(new Object[][]{
                {d1,d1prod},
                {d2,d2loc},
                {d3,d3day},
                {d4,d4opinion}
        });
        GraphManager gm = new GraphManager(root);
        gm.generateTree(root);
        GreedyAlgorithm ga = new GreedyAlgorithm(gm);
        GreedyPopularityAlgorithm gpa = new GreedyPopularityAlgorithm(gm);

        LinkedHashMap<Node,Double> gaBnf,gpaBnf;
        ga.initializeGraph();
        gaBnf = ga.benefitValueTree;
        gpa.initializeGraph();
        gpaBnf = gpa.benefitValueTree;
        assertEquals(gpaBnf,gaBnf);

        LinkedHashSet<Node> gaSet = ga.materializeNodes(5);
        //empty popularity table
        ConnectionManager.updateSql(QueryManager.deleteContentsTablePopularity);
        LinkedHashSet<Node> gpaSet = gpa.materializeNodes(5);
        assertEquals(gaSet,gpaSet);
        //tree is now generated so we run the base greedy algorithm and save the results
    }

}




