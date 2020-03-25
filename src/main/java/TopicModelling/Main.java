package TopicModelling;


import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.lda.Doc;
import com.github.chen0040.lda.Lda;
import com.github.chen0040.lda.LdaResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        TweetLoader tl = new TweetLoader();
        ArrayList<TopicModelTweet> tweets = tl.getTweetsFromFile("C:/Users/Mads/Desktop/CleanedData/2.txt");
        Lda lda = new Lda();
        lda.setTopicCount(7);
        lda.setMaxVocabularySize(2000);
        LdaResult res = lda.fit(getTweets(tweets));
        System.out.println("Topics: " + res.topicCount());

        for(int topicIndex = 0; topicIndex < res.topicCount(); ++topicIndex){
            String topicSum = res.topicSummary(topicIndex);
            List<TupleTwo<String,Integer>> topKeyWords = res.topKeyWords(topicIndex,10);
            List<TupleTwo<Doc,Double>> topStrings = res.topDocuments(topicIndex,5);
            System.out.println("Topic " + (topicIndex+1) + ": " + topicSum);
            /*for(TupleTwo<String, Integer> entry : topKeyWords){
                String keyword = entry._1();
                int score = entry._2();
                System.out.println("Keyword: " + keyword + "(" + score + ")");
            }
            for(TupleTwo<Doc, Double> entry : topStrings){
                double score = entry._2();
                int docIndex = entry._1().getDocIndex();
                String docContent = entry._1().getContent();
                System.out.println("Doc (" + docIndex + ", " + score + ")): " + docContent);
            }
             */
        }
    }
    private static ArrayList<String> getTweets(ArrayList<TopicModelTweet> tweets){
        ArrayList<String> text = new ArrayList();
        for(TopicModelTweet t : tweets){
            text.add(t.text);
        }
        return text;
    }
}
