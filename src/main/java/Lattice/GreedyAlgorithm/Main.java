package Lattice.GreedyAlgorithm;

import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.Level;
import Lattice.Node;
import scala.collection.mutable.LinkedHashSet;

import java.util.ArrayList;

public class Main {
    GraphManager graphManager = new GraphManager();

    public static void main(String[] args) {
        Main main = new Main();
        main.greedyAlgBaseTest();
        GreedyPopularityAlgorithm greedyAlgorithm = new GreedyPopularityAlgorithm(main.graphManager.nodes.keySet(), main.graphManager.getTopNode());
        greedyAlgorithm.materializeNodes(3);
        int i = 0;
    }

    public void greedyAlgBaseTest(){
        /*
        Level d1Category = new Level("Category", 40);
        Level d2Country = new Level("Country", 2);
        Level d3Year = new Level("Year", 3);
        Level d4Prod = new Level("Prod", 1000);

        Level[] leveld1 = new Level[]{d4Prod,d1Category , new Level("None", 1)};
        Level[] leveld2 = new Level[]{new Level("Location", 10000), new Level("District", 600), new Level("County", 40), new Level("City", 20), d2Country, new Level("None", 1)};
        Level[] leveld3 = new Level[]{new Level("Day", 10000), new Level("Month", 36), d3Year, new Level("None", 1)};
        Level[] leveld4 = new Level[]{new Level("Opinion", 1000), new Level("None", 1)};
        Dimension d1 = new Dimension(leveld1);
        Dimension d2 = new Dimension(leveld2);
        Dimension d3 = new Dimension(leveld3);
        Dimension d4 = new Dimension(leveld4);
        Node topNode = new Node(new Object[][]{
                {d1, d4Prod},
                {d2, d2Country},
                {d3, d3Year}
        });
        graphManager.setTopNode(topNode);


        graphManager.nodes.put(topNode,topNode);
        graphManager.generateTree(topNode);*/
    }


}
