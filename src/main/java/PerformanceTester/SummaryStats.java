package PerformanceTester;

import java.util.LinkedHashMap;

public class SummaryStats {
    LinkedHashMap<Integer, Double> avgQuerySizeDayMap = new LinkedHashMap<>();
    String greedyAlgType, LargestQueryView;
    long largestQuerySize = 0, timeSpentMaterializing = 0;
    int numOfQueriesExecuted = 0;

    public SummaryStats(String greedyAlgType) {
        this.greedyAlgType = greedyAlgType;
    }

    public void setTimeSpentMaterializing(double d){
        this.timeSpentMaterializing+=d;
    }

    private void addAvgQueryTimeDay(int day, double avgQueryTime) throws RuntimeException {
        if(this.avgQuerySizeDayMap.containsKey(day)){
            throw new RuntimeException("Already has that day");
        }
        this.avgQuerySizeDayMap.put(day,avgQueryTime);
    }
    public void setLongestQueryViewAndTime(String view, long size){
        if(size > this.largestQuerySize){
            this.LargestQueryView = view;
            this.largestQuerySize = size;
        }
    }
    private double getAvgQueryTime(){
        double totalTime = 0;
        for(double d : avgQuerySizeDayMap.values()){
            totalTime += d;
        }
        return totalTime/ avgQuerySizeDayMap.values().size();
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
        statSummaryMap.put("LargestQuerySize",String.valueOf(this.largestQuerySize));
        statSummaryMap.put("LargestQueryView",String.valueOf(this.LargestQueryView));
        //Avg query time per day
        for(int i : this.avgQuerySizeDayMap.keySet()){
            statSummaryMap.put("AvgQueryTimeDay" + i,String.valueOf(this.avgQuerySizeDayMap.get(i)));
        }
        return statSummaryMap;

    }


}
