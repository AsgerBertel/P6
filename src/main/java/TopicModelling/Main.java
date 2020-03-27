package TopicModelling;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws IOException {
        LDAMananger ldaMananger = new LDAMananger();
        TweetLoader tl = new TweetLoader();
        ArrayList<TopicModelTweet> tweets = tl.getTweetsFromFile("C:/Users/Mads/Desktop/CleanedData/2.txt");
        ldaMananger.extractTweetText(tweets);
        ldaMananger.calculateTopics(10,20000);
        TopicLabelCalculator topicCalc = new TopicLabelCalculator(ldaMananger.result,tl);
        topicCalc.simpleCalcTopicLabelScore();
        HashMap<Integer, ArrayList<String>> orderedDescriptors = topicCalc.getOrderedTopicDescriptors();
        int x = 0;
        /*for(int topicIndex = 0; topicIndex < res.topicCount(); ++topicIndex){
            String topicSum = res.topicSummary(topicIndex);
            //List<TupleTwo<String,Integer>> topKeyWords = res.topKeyWords(topicIndex,10);
            //List<TupleTwo<Doc,Double>> topStrings = res.topDocuments(topicIndex,5);
            System.out.println("Topic " + (topicIndex+1) + ": " + topicSum);
            for(TupleTwo<String, Integer> entry : topKeyWords){
                String keyword = entry._1();
                int score = entry._2();
                System.out.println("Keyword: " + keyword + "(" + score + ")");
            }
            for(TupleTwo<Doc, Double> entry : topStrings){
                double score = entry._2();
                int docIndex = entry._1().getDocIndex();
                String docContent = entry._1().getContent();
                System.out.println("Doc (" + docIndex + ", " + score + ")): " + docContent);
            }}*/
    }
    public static ArrayList<String> getTweets(ArrayList<TopicModelTweet> tweets){
        ArrayList<String> text = new ArrayList();
        for(TopicModelTweet t : tweets){
            text.add(t.modified_text);
        }
        return text;
    }
}
