package PerformanceTester;

import Lattice.Dimension;
import Lattice.GraphManager;
import Lattice.Level;
import Lattice.Node;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        for(int i = 7; i < 11; i++){
            System.out.println("\nRUN WITH " + i + " MAT VIEWS \n");
            run(i);
        }

    }

    public static void run(int i) throws IOException, SQLException {
        Dimension d1, d2, d3, d4;
        Level d1prod, d1cat, d1none;
        Level d2loc, d2dis, d2county, d2cit, d2country, d2none;
        Level d3day, d3month, d3year, d3none;
        Level d4opinion, d4none;
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
        d1 = new Dimension(new Level[]{d1prod, d1none});
        d2 = new Dimension(new Level[]{d2loc, d2dis, d2county, d2cit, d2country, d2none});
        d3 = new Dimension(new Level[]{d3day, d3month, d3year, d3none});
        d4 = new Dimension(new Level[]{d4opinion, d4none});
        Node root = new Node(new Object[][]{
                {d1,d1prod},
                {d2,d2loc},
                {d3,d3day},
                {d4,d4opinion}
        });
        GraphManager gm = new GraphManager(root);
        //Generate tree and nodes
        gm.generateTree(root);
        PerformanceTester pt = new PerformanceTester(i);
        pt.runAllPerformanceTests(gm);
    }
}
