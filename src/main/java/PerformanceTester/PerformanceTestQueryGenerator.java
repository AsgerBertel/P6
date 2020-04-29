package PerformanceTester;


import java.util.ArrayList;
import java.util.Random;


public class PerformanceTestQueryGenerator {

    //this class generates an arraylist of queries
    final static int FULL_RANDOM_CHANCE = 40;

    public static ArrayList<QueryString> fullRandomQueries(ArrayList<String> possibleViews, int amount, Random r){
        ArrayList<QueryString> queries = new ArrayList<>();
        while(queries.size() < amount){
            //populate array
            //select random view from possibleViews
            String view = possibleViews.get(r.nextInt(possibleViews.size()));
            //construct query
            QueryString query = selectQuery(view);
            queries.add(query);
        }
        return queries;
    }

    public static ArrayList<QueryString> weightedRandomQueries(ArrayList<String> possibleViews, int amount, Random r){
        ArrayList<QueryString> queries = new ArrayList<>();
        ArrayList<String> whiteListQueries = new ArrayList<>();
        // add white list queries - White listed are: anything that contains toptopic + month/day + district/county
        for(String s : possibleViews){
            if(isWhitelistQuery(s))
                whiteListQueries.add(s);
        }
        while(queries.size() < amount){
            String view;
            int val = r.nextInt(100);
            if(val > FULL_RANDOM_CHANCE)
                view = possibleViews.get(r.nextInt(possibleViews.size()));
            else
                view = whiteListQueries.get(r.nextInt(whiteListQueries.size()));
            queries.add(selectQuery(view));
        }
        return queries;
    }
    private static boolean isWhitelistQuery(String s){
        return s.contains("toptopic") && (s.contains("month") || s.contains("day")) && (s.contains("district") || s.contains("county"));
    }
    private static QueryString selectQuery(String view){
        return new QueryString("EXPLAIN (FORMAT JSON, ANALYSE) SELECT * FROM ", view);
    }
}
