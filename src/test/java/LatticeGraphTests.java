import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.Node;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertTrue;

public class LatticeGraphTests {

    @Test
    public void visitAllNodesTest(){
        GraphManager gm = new GraphManager();
        Dimension d1 = new Dimension(new String[]{"Prod","Category","None"});
        Dimension d2 = new Dimension(new String[]{"Location", "District","County","City","Country","None"});
        Dimension d3 = new Dimension(new String[]{"Day","Month","Year","None"});
        Dimension d4 = new Dimension(new String[]{"Opinion","None"});
        //Create the root node
        Node root = new Node(new Object[][]{
                {d1,"Prod"},
                {d2,"Location"},
                {d3, "Day"},
                {d4, "Opinion"}
        });
        gm.nodes.put(root,root);
        gm.generateTree(root);
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

}
