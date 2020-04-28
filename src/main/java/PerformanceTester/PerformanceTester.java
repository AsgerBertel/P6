package PerformanceTester;

import Lattice.GraphManager;
import Lattice.GreedyAlgorithm.GreedyAlgorithm;
import Lattice.GreedyAlgorithm.GreedyPopularityAlgorithm;
import Lattice.Node;
import OLAP.NodeQueryUtils;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;


import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import Sql.QueryManager;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
public class PerformanceTester {

    final static int RANDOM_SEED = 1234;
    Random random = new Random(RANDOM_SEED);

    public void runAllPerformanceTests(GraphManager gm) throws IOException, SQLException {
        ViewGenerator vg = new ViewGenerator();
        //Runs all tests for all queries and stores them in an xls file
        LinkedHashMap<Integer, ArrayList<String>> dayQueriesMap = new LinkedHashMap<>();
        for(int i = 0; i < 2; i++){
            dayQueriesMap.put(i,PerformanceTestQueryGenerator.fullRandomQueries(getAllViewNamesExceptForNNNN(gm.nodes.keySet()),10,random));
        }
        //Declare both greedy algorithms
        GreedyAlgorithm ga = new GreedyAlgorithm(gm.nodes.keySet(),gm.getTopNode());
        GreedyPopularityAlgorithm gpa = new GreedyPopularityAlgorithm(gm.nodes.keySet(), gm.getTopNode());


        //Create excel sheet and workbook
        File results = new File("C:/Users/Mads/Desktop/PerformanceTest/Results.xls");

        //Run base and write results
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet baseSheet = workbook.createSheet("base");
        HSSFSheet popularitySheet = workbook.createSheet("popularity");
        //run base greedy
        runGreedyAlgorithmAndGenerateViews(ga,gm);
        for(int i : dayQueriesMap.keySet()){
            System.out.println("yeet");
            runPerformanceTest(dayQueriesMap.get(i),baseSheet);
        }
        //Run popularity
        //Before every day, run greedy and generate views
        //before first run, drop the contents of the popularity table manually?
        for(int i : dayQueriesMap.keySet()){
            //run greedy
            runGreedyAlgorithmAndGenerateViews(gpa,gm);
            runPerformanceTest(dayQueriesMap.get(i),popularitySheet);
            ConnectionManager.updateSql(QueryManager.updateCurrentDayInPopularity);
        }
        FileOutputStream os = new FileOutputStream(results);
        workbook.write(os);
    }
    private void runGreedyAlgorithmAndGenerateViews(GreedyAlgorithm ga, GraphManager gm) throws SQLException {
        ViewGenerator vg = new ViewGenerator();
        ga.materializeNodes(6);
        vg.generateViews(gm.nodes,gm.getTopNode());
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
        for(String s : queries){
            try {
                ResultSet rs = ConnectionManager.selectSQL(s);
                rs.next();
                JSONArray jsonArray = new JSONArray(rs.getString(1));
                double timeSpent = jsonArray.getJSONObject(1).getDouble("Execution Time");
                Row row = sheet.createRow(sheet.getLastRowNum()+1);
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
