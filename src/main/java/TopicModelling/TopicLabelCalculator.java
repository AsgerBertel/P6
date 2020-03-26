package TopicModelling;

import com.github.chen0040.lda.LdaResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TopicLabelCalculator {
    LdaResult result;
    TweetLoader tweetLoader;
    private HashMap<Integer, HashMap<String,Integer>> topicLabels = new HashMap<>();

    public TopicLabelCalculator(LdaResult result, TweetLoader tweetLoader) {
        this.result = result;
        this.tweetLoader = tweetLoader;
    }

    public HashMap<Integer, HashMap<String, Integer>> simpleCalcTopicLabels(){
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