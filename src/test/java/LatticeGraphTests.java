import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.Level;
import Lattice.Node;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

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
        d1prod = new Level("TopTopics", new BigInteger(String.valueOf(70)));
        d1none = new Level("None", new BigInteger(String.valueOf(1)));
        d2loc = new Level("Coordinate", new BigInteger(String.valueOf(3861358)));
        d2dis = new Level("District", new BigInteger(String.valueOf(143)));
        d2county = new Level("County", new BigInteger(String.valueOf(15)));
        d2cit = new Level("City", new BigInteger(String.valueOf(2)));
        d2country = new Level("Country", new BigInteger(String.valueOf(1)));
        d2none = new Level("None", new BigInteger(String.valueOf(1)));
        d3day = new Level("Day", new BigInteger(String.valueOf(19)));
        d3month = new Level("Month", new BigInteger(String.valueOf(3)));
        d3year = new Level("Year", new BigInteger(String.valueOf(1)));
        d3none = new Level("None", new BigInteger(String.valueOf(1)));
        d4opinion = new Level("Opinion", new BigInteger(String.valueOf(3)));
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
        //todo implement
    }

}
