package PerformanceTester;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;


public class PerformanceTestQueryGenerator {

    //this class generates an arraylist of queries
    final static int FULL_RANDOM_CHANCE = 40;
    final static ArrayList<String> taskOneViews = new ArrayList<>(Arrays.asList(
            "toptopicdistrictmonthopinion",
            //"toptopiccitynoneopinion", "toptopiccitynoneopinion", "toptopiccitynoneopinion", "toptopiccitynoneopinion",
            "toptopiccountynoneopinion", "toptopiccountynoneopinion", "toptopiccountynoneopinion",
            "toptopicdistrictnoneopinion", "toptopicdistrictnoneopinion", "toptopicdistrictnoneopinion"
    ));
    final static ArrayList<String> taskTwoViews = new ArrayList<>(Arrays.asList(
            "toptopiccountymonthopinion", "toptopiccountymonthopinion", "toptopiccountymonthopinion",
            "toptopiccountydayopinion", "toptopiccountydayopinion", "toptopiccountydayopinion"
    ));
    final static ArrayList<String> taskThreeViews = new ArrayList<>(Arrays.asList(
            "toptopiccountymonthopinion", "toptopiccountymonthopinion", "toptopiccountymonthopinion",
            "toptopiccountydayopinion", "toptopiccountydayopinion", "toptopiccountydayopinion"
    ));
    final static ArrayList<String> taskFourViews = new ArrayList<>(Arrays.asList(
            "toptopicnoneyearopinion", "toptopicnoneyearopinion", "toptopicnoneyearopinion",
            "toptopicnonemonthopinion","toptopicnonemonthopinion","toptopicnonemonthopinion","toptopicnonemonthopinion",
            "toptopicnonemonthopinion","toptopicnonemonthopinion","toptopicnonemonthopinion","toptopicnonemonthopinion"
    ));
    final static ArrayList<String> taskFiveViews = new ArrayList<>(Arrays.asList(
            "toptopiccountynoneopinion","toptopiccountynoneopinion","toptopiccountynoneopinion","toptopiccountynoneopinion"
    ));

    public static ArrayList<String> fullRandomQueries(ArrayList<String> possibleViews, int amount, Random r){
        ArrayList<String> queries = new ArrayList<>();
        while(queries.size() < amount){
            //populate array
            //select random view from possibleViews
            String view = possibleViews.get(r.nextInt(possibleViews.size()));
            //construct query
            queries.add(view);
        }
        return queries;
    }

    public static ArrayList<String> weightedRandomQueries(ArrayList<String> possibleViews, int amount, Random r){
        ArrayList<String> queries = new ArrayList<>();
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
    private static String selectQuery(String view){
        return view;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> sameMonth(){
        LinkedHashMap<Integer,ArrayList<String>> queries = new LinkedHashMap<>();
        for(int i = 0; i < 30; i++){
            queries.put(i,getDayOneQueries());
        }
        return queries;
    }
    public static LinkedHashMap<Integer, ArrayList<String>> suddenChanges(){
        LinkedHashMap<Integer,ArrayList<String>> queries = new LinkedHashMap<>();
        for(int i = 0; i < 30; i++){
            if(i < 15)
                queries.put(i,getDayOneQueries());
            else
                queries.put(i,getDayTwoQueries());
        }
        return queries;
    }
    public static LinkedHashMap<Integer,ArrayList<String>> generalChangesScenario(){
        LinkedHashMap<Integer,ArrayList<String>> queries = new LinkedHashMap<>();
        for(int i = 0; i < 30; i++){
            if(i < 10){
                queries.put(i,getDayOneQueries());
            }
            else if(i < 20){
                queries.put(i,getDayTwoQueries());
            }
            else if(i < 25){
                queries.put(i,getDayThreeQueries());
            }
            else{
                queries.put(i,getDayFourQueries());
            }
        }
        return queries;
    }

    private static ArrayList<String> getDayOneQueries(){
        ArrayList<String> views = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            views.addAll(taskOneViews);
        }
        for(int i = 0; i < 6; i++){
            views.addAll(taskThreeViews);
        }
        return views;
    }

    private static ArrayList<String> getDayTwoQueries(){
        ArrayList<String> views = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            views.addAll(taskTwoViews);
        }
        for(int i = 0; i < 5; i++){
            views.addAll(taskFiveViews);
        }
        for(int i = 0; i < 6; i++){
            views.addAll(taskFourViews);
        }
        return views;
    }

    private static ArrayList<String> getDayThreeQueries(){
        ArrayList<String> views = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            views.addAll(taskOneViews);
        }
        for(int i = 0; i < 7; i++){
            views.addAll(taskTwoViews);
        }
        for(int i = 0; i < 4; i++){
            views.addAll(taskFiveViews);
        }
        return views;
    }

    private static ArrayList<String> getDayFourQueries(){
        ArrayList<String> views = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            views.addAll(taskFourViews);
        }
        for(int i = 0; i < 10; i++){
            views.addAll(taskThreeViews);
        }
        return views;
    }

}
