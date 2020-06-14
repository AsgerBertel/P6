package PerformanceTester;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;


public class PerformanceTestQueryGenerator {

    //this class generates an arraylist of queries
    final static int FULL_RANDOM_CHANCE = 40;

    final static ArrayList<String> yeet = new ArrayList<>(Arrays.asList(
            "nonecoordinatenonenone", "nonecoordinatenonenone", "nonecoordinatenonenone",
            "toptopicnonenoneopinion", "toptopicnonenoneopinion", "toptopicnonenoneopinion"
    ));
    final static ArrayList<String> yeet2 = new ArrayList<>(Arrays.asList(
            "toptopiccountymonthopinion", "toptopiccountymonthopinion", "toptopiccountymonthopinion",
            "toptopiccountydayopinion", "toptopiccountydayopinion", "toptopiccountydayopinion"
    ));
    final static ArrayList<String> yeet3 = new ArrayList<>(Arrays.asList(
            "toptopicnonenonenone", "toptopicnonenonenone", "toptopicnonenonenone",
            "toptopiccoordinatedayopinion", "toptopiccoordinatedayopinion", "toptopiccoordinatedayopinion"
    ));
    final static ArrayList<String> yeet4 = new ArrayList<>(Arrays.asList(
            "nonecountydayopinion", "nonecountydayopinion", "nonecountydayopinion",
            "toptopiccitynonenone", "toptopiccitynonenone", "toptopiccitynonenone"
    ));
    final static ArrayList<String> yeet5 = new ArrayList<>(Arrays.asList(
            "nonenonedayopinion", "nonenonedayopinion", "nonenonedayopinion",
            "nonenonemonthopinion", "nonenonemonthopinion", "nonenonemonthopinion"
    ));
    final static ArrayList<String> yeet6 = new ArrayList<>(Arrays.asList(
            "nonenoneyearopinion", "nonenoneyearopinion", "nonenoneyearopinion",
            "toptopicdistrictnonenone", "toptopicdistrictnonenone", "toptopicdistrictnonenone"
    ));
    final static ArrayList<String> yeet7 = new ArrayList<>(Arrays.asList(
            "nonedistrictmonthnone", "nonedistrictmonthnone", "nonedistrictmonthnone",
            "toptopiccountynonenone", "toptopiccountynonenone", "toptopiccountynonenone"
    ));
    final static ArrayList<String> yeet8 = new ArrayList<>(Arrays.asList(
            "toptopicnonenoneopinion", "toptopicnonenoneopinion", "toptopicnonenoneopinion",
            "nonedistrictdaynone", "nonedistrictdaynone", "nonedistrictdaynone"
    ));
    final static ArrayList<String> yeet9 = new ArrayList<>(Arrays.asList(
            "nonedistrictnoneopinion", "nonedistrictnoneopinion", "nonedistrictnoneopinion",
            "nonecityyearopinion", "nonecityyearopinion", "nonecityyearopinion"
    ));
    final static ArrayList<String> yeet10 = new ArrayList<>(Arrays.asList(
            "toptopiccoordinatenonenone", "toptopiccoordinatenonenone", "toptopiccoordinatenonenone",
            "toptopiccountyyearopinion", "toptopiccountyyearopinion", "toptopiccountyyearopinion"
    ));


    private static ArrayList<String> randomYeeteronis(Random random){
        ArrayList<String> queries = new ArrayList<>();
        ArrayList<ArrayList<String>> yeetlist = new ArrayList<>(Arrays.asList(yeet, yeet2, yeet3, yeet4, yeet5,yeet6,yeet7,yeet8,yeet9,yeet10));
        for(int i = 0; i < 15; i++){
            queries.addAll(yeetlist.get(random.nextInt(yeetlist.size())));
        }
        return queries;
    }
    public static LinkedHashMap<Integer, ArrayList<String>> neoGeneralChanges(Random random, int days){
        LinkedHashMap<Integer,ArrayList<String>> queries = new LinkedHashMap<>();
        for(int i = 0; i < days; i++){
            queries.put(i,randomYeeteronis(random));
        }
        return queries;
    }

    final static ArrayList<String> taskOneViews = new ArrayList<>(Arrays.asList(
            "toptopicdistrictmonthopinion",
            "toptopiccitynoneopinion", "toptopiccitynoneopinion", "toptopiccitynoneopinion", "toptopiccitynoneopinion",
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
    final static ArrayList<String> dailyTOP = new ArrayList<>(Arrays.asList(
            "toptopiccountydayopinion","toptopiccountydayopinion","toptopiccountydayopinion",
            "toptopicdistrictdayopinion","toptopicdistrictdayopinion","toptopicdistrictdayopinion"
    ));
    final static ArrayList<String> monthlyTOP = new ArrayList<>(Arrays.asList(
            "toptopiccountymonthopinion","toptopiccountymonthopinion","toptopiccountymonthopinion",
            "toptopicdistrictmonthopinion","toptopicdistrictmonthopinion","toptopicdistrictmonthopinion"
    ));
    final static ArrayList<String> dailyMentions = new ArrayList<>(Arrays.asList(
            "nonecountydaynone","nonecountydaynone","nonecountydaynone",
            "nonedistrictdaynone","nonedistrictdaynone","nonedistrictdaynone"
    ));
    final static ArrayList<String> monthlyMentions = new ArrayList<>(Arrays.asList(
            "nonecountymonthnone","nonecountymonthnone","nonecountymonthnone",
            "nonedistrictmonthnone","nonedistrictmonthnone","nonedistrictmonthnone"
    ));

    final static ArrayList<String> suddenpt2 = new ArrayList<>(Arrays.asList(
            "toptopicdistrictyearopinion","toptopicdistrictyearopinion","toptopicdistrictyearopinion",
            "toptopiccountyyearopinion","toptopiccountyyearopinion","toptopiccountyyearopinion"
    ));

    final static ArrayList<String> finalday1 = new ArrayList<>(Arrays.asList(
            "toptopicdistrictdayopinion","toptopicdistrictdayopinion","toptopicdistrictdayopinion",
            "toptopiccountydayopinion","toptopiccountydayopinion","toptopiccountydayopinion",
            "toptopiccountymonthopinion","toptopiccountymonthopinion","toptopiccountymonthopinion"
    ));
    final static ArrayList<String> finalday2 = new ArrayList<>(Arrays.asList(
            "toptopicdistrictmonthopinion","toptopicdistrictmonthopinion","toptopicdistrictmonthopinion",
            "toptopiccountymonthopinion","toptopiccountymonthopinion","toptopiccountymonthopinion"
    ));
    final static ArrayList<String> finalday3 = new ArrayList<>(Arrays.asList(
            "nonedistrictdayopinion","nonedistrictdayopinion","nonedistrictdayopinion",
            "nonecountydayopinion","nonecountydayopinion","nonecountydayopinion",
            "toptopicdistrictmonthopinion","toptopicdistrictmonthopinion","toptopicdistrictmonthopinion"
    ));

    public static LinkedHashMap<Integer, ArrayList<String>> finaltesteronies(){
        LinkedHashMap<Integer,ArrayList<String>> querymap = new LinkedHashMap<>();
        for(int i = 0; i < 30; i++){
            querymap.put(i,finalTesteroniQueries(i));
        }
        return querymap;
    }

    public static ArrayList<String> finalTesteroniQueries(int day){
        ArrayList<String> queries = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            if(day < 5){
                queries.addAll(finalday1);
            }
            else if(day < 10){
                queries.addAll(finalday2);
            }
            else if(day < 15){
                queries.addAll(finalday3);
            }
            else if(day < 20){
                queries.addAll(finalday1);
            }
            else if(day < 25){
                queries.addAll(finalday2);
            }
            else queries.addAll(finalday3);
        }
        return queries;
    }

    public static ArrayList<String> suddenpt1(){
        ArrayList<String> queries = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            queries.addAll(taskThreeViews);
        }
        return queries;
    }
    public static ArrayList<String> suddenpt2List(){
        ArrayList<String> queries = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            queries.addAll(suddenpt2);
        }
        return queries;
    }
    public static LinkedHashMap<Integer, ArrayList<String>> suddenDeath(){
        LinkedHashMap<Integer,ArrayList<String>> querymap = new LinkedHashMap<>();
        for(int i = 0; i < 30; i++){
            if(i < 15 ) querymap.put(i,suddenpt1());
            else querymap.put(i, suddenpt2List());
        }
        return querymap;
    }
    public static ArrayList<String> daily(){
        ArrayList<String> queries = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            if(i < 7) queries.addAll(dailyTOP);
            else queries.addAll(dailyMentions);
        }
        return queries;
    }
    public static ArrayList<String> monthly(){
        ArrayList<String> queries = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            if(i < 7) queries.addAll(monthlyTOP);
            else queries.addAll(monthlyMentions);
        }
        return queries;
    }

    public static LinkedHashMap<Integer, ArrayList<String>> normPattern(){
        LinkedHashMap<Integer,ArrayList<String>> queries = new LinkedHashMap<>();
        for(int i = 0; i < 30; i++){
            if(i%7 == 0){
                ArrayList<String> temp = new ArrayList<>();
                temp.addAll(daily());
                temp.addAll(monthly());
                queries.put(i,temp);
            } else queries.put(i,daily());
        }
        return queries;
    }


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
