package TopicModelling;


import SentimentAnalysis_CoreNLP.SentimentAnalyzer;

import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.lda.Doc;
import com.github.chen0040.lda.LdaResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) throws IOException {
        TweetLoader tl = new TweetLoader();
        //tl.getTweetsFromFile("C:/Users/mk96/Desktop/CleanedData/2.txt");
        tl.getTweetsFromFile("C:/Users/mk96/Desktop/CleanedData/1.txt");

        //sentiment(tl.getTweets());
        topicModel(tl.getTweets(),tl);
        //TopicAmountCalculator topicAmountCalculator = new TopicAmountCalculator();
        //topicAmountCalculator.jaccardAverage(10, orderedDescriptors);

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
    private static void sentiment(ArrayList<TopicModelTweet> tweets){
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.findSentiment(tweets);
    }
    private static void topicModel(ArrayList<TopicModelTweet> tweets,TweetLoader tl){
        LDAMananger ldaMananger = new LDAMananger();
        ldaMananger.extractTweetText(tweets);
        LdaResult result = ldaMananger.calculateTopics(80,30000);

        TopicLabelCalculator topicCalc = new TopicLabelCalculator(result,tl);
        topicCalc.simpleCalcTopicLabelScore();
        HashMap<Integer, ArrayList<String>> orderedDescriptors = topicCalc.getOrderedTopicDescriptors();
        ldaMananger.assignTopics(tweets,result,orderedDescriptors);
        int i =0;
        double avg = 0;
        /*for(Doc d : result.documents()){
            for(TupleTwo<Integer,Double> tt : d.topTopics(1)){
                if(tt._2() < 0.6){
                    //System.out.println(d.getContent() + " ::: " + tt._2());
                    avg = avg + tt._2();
                    i++;
                }
            }
            //System.out.println(t.getTopics());
        }
        System.out.println(i + " out of " + tweets.size());
        System.out.println("avg: " + avg/i);*/
        for(ArrayList<String> ls : orderedDescriptors.values()){
            System.out.println("Words to describe this category: ");
            for(int j =0; j < 3; j++){
                System.out.print(ls.get(j) + ", ");
            }
            System.out.println("\n----------------------------------------------------------------");
        }
    }
    public static ArrayList<String> getTweets(ArrayList<TopicModelTweet> tweets){
        ArrayList<String> text = new ArrayList();
        for(TopicModelTweet t : tweets){
            text.add(t.topic_text);
        }
        return text;
    }
}
