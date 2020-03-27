package TopicModelling;

import com.github.chen0040.lda.LdaResult;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

public class TopicLabelCalculator {
    LdaResult result;
    TweetLoader tweetLoader;
    private HashMap<Integer, HashMap<String,Integer>> topicLabels = new HashMap<>();

    public TopicLabelCalculator(LdaResult result, TweetLoader tweetLoader) {
        this.result = result;
        this.tweetLoader = tweetLoader;
    }
    public HashMap<Integer, ArrayList<String>> getOrderedTopicDescriptors(){
        HashMap<Integer, ArrayList<String>> orderedDescriptors = new HashMap<>();
        Comparator<Entry<String,Integer>> valueComparator = (o1, o2) -> {
            int v1 = o1.getValue();
            int v2 = o2.getValue();
            return Integer.compare(v2,v1);
        };

        for(int i = 0; i < topicLabels.keySet().size(); i++){
            ArrayList<Entry<String, Integer>> entries = new ArrayList<>(topicLabels.get(i).entrySet());
            Collections.sort(entries,valueComparator);
            ArrayList<String> sortedList = new ArrayList<>();
            for(Entry<String,Integer> e : entries){
                sortedList.add(e.getKey());
            }
            orderedDescriptors.put(i,sortedList);
        }
        return orderedDescriptors;
    }

    public HashMap<Integer, HashMap<String, Integer>> simpleCalcTopicLabelScore(){
        for(int index = 0; index < result.topicCount(); index++){
            findLabelValsForTopic(result.topicSummary(index),index);
        }
        return topicLabels;
    }
    private void findLabelValsForTopic(String topicSum, int index){
        //Map that will hold val
        HashMap<String,Integer> labelVals = new HashMap<>();
        //Split topics separated with spaces
        String[] topics = topicSum.trim().split("\\s+");
        populateLabelVals(labelVals,topics);
        for(TopicModelTweet t : tweetLoader.getTweets()){
            getScoreForLine(t.modified_text, topics, labelVals);
        }
        topicLabels.put(index,labelVals);
    }
    private void populateLabelVals(HashMap<String,Integer> labelVals, String[] topics){
        for(String t : topics){
            labelVals.put(t,0);
        }
    }
    private void getScoreForLine(String text, String[] topics, HashMap<String,Integer> labelVals){
        for(String s : labelVals.keySet()){
            if(!text.contains(s))
                continue;
            for(String topic : topics){
                if(text.contains(topic) && !topic.equals(s)){
                    labelVals.put(s,labelVals.get(s)+1);
                }
            }
        }
    }
}