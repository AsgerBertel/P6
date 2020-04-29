package PerformanceTester;

import GUI.Other.PopularityManager;
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
import scala.collection.parallel.ParIterableLike;

public class PerformanceTester {

    final static int RANDOM_SEED = 1234;
    Random random = new Random(RANDOM_SEED);
    private GraphManager gm;
    private GreedyAlgorithm ga;
    private GreedyPopularityAlgorithm gpa;

    public void runAllPerformanceTests(GraphManager gm) throws IOException, SQLException {
        //Runs all tests for all queries and stores them in an xls file
        this.gm = gm;
        this.ga = new GreedyAlgorithm(this.gm);
        this.gpa = new GreedyPopularityAlgorithm(this.gm);

        LinkedHashMap<Integer, ArrayList<QueryString>> dayQueriesMap = new LinkedHashMap<>();
        for(int i = 0; i < 2; i++){
            dayQueriesMap.put(i,PerformanceTestQueryGenerator.weightedRandomQueries(getAllViewNamesExceptForNNNN(this.gm.nodes.keySet()),20,this.random));
        }

        //Run base and write results
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet baseDataSheet = workbook.createSheet("baseDATA");
        HSSFSheet popularityDataSheet = workbook.createSheet("popularityDATA");
        SummaryStats baseSum = new SummaryStats("BASE");
        SummaryStats popSum = new SummaryStats("POPULARITY");
        //run base greedy
        //delet everything
        ConnectionManager.updateSql(QueryManager.dropSchemaPublic);
        runBase(dayQueriesMap,baseDataSheet,baseSum);
        //delete all views and empty the popularity table
        ConnectionManager.updateSql(QueryManager.dropSchemaPublic);
        ConnectionManager.updateSql(QueryManager.deleteContentsTablePopularity);
        //Run popularity
        runPopularity(dayQueriesMap,popularityDataSheet,popSum);
        //Before every day, run greedy and generate views
        //before first run, drop the contents of the popularity table manually?
        writeToExcel(workbook,baseSum,popSum);

    }
    private void writeToExcel(HSSFWorkbook workbook, SummaryStats base, SummaryStats pop) throws IOException {
        File results = new File("C:/Users/Mads/Desktop/PerformanceTest/Results.xls");
        HSSFSheet baseSummarySheet = workbook.createSheet("baseSUM");
        HSSFSheet popularitySummarySheet = workbook.createSheet("popularitySUM");
        populateSumSheet(base,baseSummarySheet);
        populateSumSheet(pop,popularitySummarySheet);
        FileOutputStream os = new FileOutputStream(results);
        workbook.write(os);
    }

    private void populateSumSheet(SummaryStats stats, HSSFSheet sheet){
        int rownum = 0;
        LinkedHashMap<String,String> summary = stats.getStatSummary();
        for(String s : summary.keySet()){
            Row row = sheet.createRow(++rownum);
            Cell cell1, cell2;
            cell1 = row.createCell(1);
            cell2 = row.createCell(2);
            cell1.setCellValue(s);
            cell2.setCellValue(summary.get(s));
        }
    }

    private void runBase(LinkedHashMap<Integer, ArrayList<QueryString>> dayQueriesMap, HSSFSheet data,SummaryStats baseSum) throws SQLException {
        //materialize view and add time
        baseSum.setTimeSpentMaterializing(runGreedyAlgorithmAndGenerateViews(this.ga,this.gm));
        for(int i : dayQueriesMap.keySet()){
            double avg = runPerformanceTest(dayQueriesMap.get(i),data, i, baseSum);
            baseSum.updateValues(dayQueriesMap.get(i).size(),i+1,avg);
        }
    }
    private void runPopularity(LinkedHashMap<Integer, ArrayList<QueryString>> dayQueriesMap, HSSFSheet data,SummaryStats popSum) throws SQLException {
        for(int i : dayQueriesMap.keySet()){
            popSum.setTimeSpentMaterializing(runGreedyAlgorithmAndGenerateViews(this.gpa,this.gm));
            double avg = runPerformanceTest(dayQueriesMap.get(i),data, i, popSum);
            popSum.updateValues(dayQueriesMap.get(i).size(),i+1,avg);
            ConnectionManager.updateSql(QueryManager.updateCurrentDayInPopularity);
        }
    }

    private double runGreedyAlgorithmAndGenerateViews(GreedyAlgorithm ga, GraphManager gm) throws SQLException {
        ViewGenerator vg = new ViewGenerator();
        ga.materializeNodes(5);
        return vg.generateViews(gm.nodes,gm.getTopNode());
    }
    private ArrayList<String> getAllViewNamesExceptForNNNN(Set<Node> nodes){
        ArrayList<String> viewNames = new ArrayList<>();
        for(Node n : nodes){
            if(!NodeQueryUtils.getNodeViewName(n).equals("nonenonenonenone")){
                viewNames.add(NodeQueryUtils.getNodeViewName(n));
            }
        }
        return viewNames;
    }

    private double runPerformanceTest(ArrayList<QueryString> queries, HSSFSheet sheet, int day, SummaryStats stats){
        double totalTime = 0;
        PopularityManager pm = new PopularityManager();
        for(QueryString s : queries){
            try {
                //update view popularity
                pm.updatePopularityValue(s.getViewname());
                //run query 3 times and get average, to minimize variation. todo set to 5 when doing actual test?
                double totalTimeSpentOnQuery = 0;
                for(int i = 0; i < 3; i++){
                    ResultSet rs = ConnectionManager.selectSQL(s.toString());
                    rs.next();
                    JSONArray jsonArray = new JSONArray(rs.getString(1));
                    totalTimeSpentOnQuery+= jsonArray.getJSONObject(0).getDouble("Execution Time");
                }
                double timeSpent = totalTimeSpentOnQuery/3;
                totalTime+=timeSpent;
                Row row = sheet.createRow(sheet.getLastRowNum()+1);
                Cell query = row.createCell(0);
                query.setCellValue(s.getViewname());
                Cell timeSpentOnQuery = row.createCell(1);
                timeSpentOnQuery.setCellValue(timeSpent);
                Cell dayCell = row.createCell(2);
                dayCell.setCellValue(day+1);
                stats.setLongestQueryViewAndTime(s.getViewname(),timeSpent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return totalTime/queries.size();
    }
}
