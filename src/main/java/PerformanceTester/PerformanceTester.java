package PerformanceTester;

import Lattice.GraphManager;
import Lattice.GreedyAlgorithm.GreedyAlgorithm;
import Lattice.Node;
import OLAP.NodeQueryUtils;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;


import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class PerformanceTester {

    final static int RANDOM_SEED = 1234;
    Random random = new Random(RANDOM_SEED);

    public void runAllPerformanceTests(GraphManager gm) throws IOException, SQLException {
        ViewGenerator vg = new ViewGenerator();
        //Runs all tests for all queries and stores them in an xls file
        LinkedHashMap<Integer, ArrayList<String>> dayQueriesMap = new LinkedHashMap<>();
        ViewGenerator vg = new ViewGenerator();
        for(int i = 0; i < 2; i++){
            dayQueriesMap.put(i,PerformanceTestQueryGenerator.fullRandomQueries(getAllViewNamesExceptForNNNN(gm.nodes.keySet()),10,random));
        }
        //Generate the views which the greedy algorithm proposes
        GreedyAlgorithm ga = new GreedyAlgorithm(gm.nodes.keySet(),gm.getTopNode());
        ga.materializeNodes(6);
        vg.generateViews(gm.nodes,gm.getTopNode());
        //Create excel sheet and workbook
        File testOutput = new File("C:/Users/Mads/Desktop/PerformanceTest/results.xls");
        HSSFWorkbook workbook = new HSSFWorkbook();
        for(int i : dayQueriesMap.keySet()){
            System.out.println("yeet");
            runPerformanceTest(dayQueriesMap.get(i),workbook.createSheet());
        }
        FileOutputStream os = new FileOutputStream(testOutput);
        workbook.write(os);
    }
    private ArrayList<String> getAllViewNamesExceptForNNNN(Set<Node> nodes){
        ArrayList<String> viewNames = new ArrayList<>();
        for(Node n : nodes){
            if(!NodeQueryUtils.getNodeViewName(n).equals("NoneNoneNoneNone")){
                viewNames.add(NodeQueryUtils.getNodeViewName(n));
            }
        }
        return viewNames;
    }

    private void runPerformanceTest(ArrayList<String> queries, HSSFSheet sheet){
        int rownum = 0;
        for(String s : queries){
            long startTime = System.currentTimeMillis();
            try {
                ResultSet rs = ConnectionManager.selectSQL(s);
                long timeSpent = System.currentTimeMillis() - startTime;
                Row row = sheet.createRow(++rownum);
                Cell query = row.createCell(0);
                query.setCellValue(s);
                Cell timeSpentOnQuery = row.createCell(1);
                timeSpentOnQuery.setCellValue(timeSpent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
