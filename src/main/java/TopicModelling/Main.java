package TopicModelling;


import SentimentAnalysis_CoreNLP.SentimentAnalyzer;

import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.lda.Doc;
import com.github.chen0040.lda.LdaResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        TweetLoader tl = new TweetLoader();
        tl.getTweetsFromFile("C:/Users/Mads/Desktop/CleanedData/2.txt");
        tl.getTweetsFromFile("C:/Users/Mads/Desktop/CleanedData/1.txt");

        //sentiment(tl.getTweets());
        topicModel(tl.getTweets(),tl);
        //sentiment(tl.getTweets());
        writeToFile("C:/Users/Mads/Desktop/CleanedData/topicSenti.txt",tl.getTweets());
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
    private static void sentiment(ArrayList<TopicModelTweet> tweets) throws InterruptedException {
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.findSentiment(tweets);
    }
    private static void topicModel(ArrayList<TopicModelTweet> tweets,TweetLoader tl){
        LDAMananger ldaMananger = new LDAMananger();
        ldaMananger.extractTweetText(tweets);
        LdaResult result = ldaMananger.calculateTopics(50,50000);
        TopicLabelCalculator topicCalc = new TopicLabelCalculator(result,tl);
        topicCalc.simpleCalcTopicLabelScore();
        HashMap<Integer, ArrayList<String>> orderedDescriptors = topicCalc.getOrderedTopicDescriptors();
        ldaMananger.assignTopics(tweets,result,orderedDescriptors);
        //remove any tweets that for some reason, might not have topics assigned to them.
        tweets.removeIf(topicModelTweet -> topicModelTweet.getTopics().isEmpty());
    }
    private static void writeToFile(String filePath, ArrayList<TopicModelTweet> tweets) throws IOException {
        File topicSentiTweet = new File(filePath);
        BufferedWriter bfw = new BufferedWriter(new FileWriter(topicSentiTweet));
        for(TopicModelTweet t: tweets){
            bfw.write(t.toString());
            bfw.newLine();
        }
        bfw.close();
    }
}