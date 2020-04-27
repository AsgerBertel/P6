package PerformanceTester;


import java.util.ArrayList;
import java.util.Random;


public class PerformanceTestQueryGenerator {

    //this class generates an arraylist of queries

    public static ArrayList<String> fullRandomQueries(ArrayList<String> possibleViews, int amount, Random r){
        ArrayList<String> queries = new ArrayList<>();
        while(queries.size() < amount){
            //populate array
            //select random view from possibleViews
            String view = possibleViews.get(r.nextInt(possibleViews.size()));
            //construct query
            String query = selectQuery(view);
            queries.add(query);
        }
        return queries;
    }
    private static String selectQuery(String view){
        return "EXPLAIN ANALYSE SELECT * FROM " + view;
        //todo add possibility for a where clause
    }
}
