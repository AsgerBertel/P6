package PerformanceTester;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SummaryStats {
    LinkedHashMap<Integer, Double> avgQueryTimeDayMap = new LinkedHashMap<>();
    String greedyAlgType, longestQueryView;
    double longestQueryTime = 0, timeSpentMaterializing = 0;
    int numOfQueriesExecuted = 0;

    public SummaryStats(String greedyAlgType) {
        this.greedyAlgType = greedyAlgType;
    }

    public void setTimeSpentMaterializing(double d){
        this.timeSpentMaterializing+=d;
    }

    private void addAvgQueryTimeDay(int day, double avgQueryTime) throws RuntimeException {
        if(this.avgQueryTimeDayMap.containsKey(day)){
            throw new RuntimeException("Already has that day");
        }
        this.avgQueryTimeDayMap.put(day,avgQueryTime);
    }
    public void setLongestQueryViewAndTime(String view, double time){
        if(time > this.longestQueryTime){
            this.longestQueryView = view;
            this.longestQueryTime = time;
        }
    }
    private double getAvgQueryTime(){
        double totalTime = 0;
        for(double d : avgQueryTimeDayMap.values()){
            totalTime += d;
        }
        return totalTime/avgQueryTimeDayMap.values().size();
    }
    private void setNumOfQueriesExecuted(int num){
        this.numOfQueriesExecuted+=num;
    }

    public void updateValues(int numQueries, int day, double dayAvg){
        this.setNumOfQueriesExecuted(numQueries);
        this.addAvgQueryTimeDay(day,dayAvg);
    }

    public LinkedHashMap<String,String> getStatSummary(){
        LinkedHashMap<String,String> statSummaryMap = new LinkedHashMap<>();
        //Alg type
        statSummaryMap.put("GreedyAlgType",this.greedyAlgType);
        //time spent materializing
        statSummaryMap.put("TimeSpentMaterializing", String.valueOf(this.timeSpentMaterializing));
        //Number of queries executed
        statSummaryMap.put("NumberOfQueriesExecuted",String.valueOf(this.numOfQueriesExecuted));
        //Avg query time total
        statSummaryMap.put("AvgQueryTimeTotal",String.valueOf(this.getAvgQueryTime()));
        //Longest query time + view
        statSummaryMap.put("LongestQueryTime",String.valueOf(this.longestQueryTime));
        statSummaryMap.put("LongestQueryView",String.valueOf(this.longestQueryView));
        //Avg query time per day
        for(int i : this.avgQueryTimeDayMap.keySet()){
            statSummaryMap.put("AvgQueryTimeDay" + i,String.valueOf(this.avgQueryTimeDayMap.get(i)));
        }
        return statSummaryMap;

    }


}
