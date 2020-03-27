
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import TopicModelling.Sentiment;
import TopicModelling.TopicModelTweet;
import TopicModelling.TweetLoader;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalyzer {

    public void findSentiment(ArrayList<TopicModelTweet> tweets) {
                Properties props = new Properties();
                props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
                StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
                for (TopicModelTweet tweet : tweets) {

                    int mainSentiment = 0;
                    String line = tweet.getModified_text();
                    if (line != null && line.length() > 0) {
                        int longest = 0;
                        Annotation annotation = pipeline.process(line);
                        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                            String partText = sentence.toString();
                            if (partText.length() > longest) {
                                mainSentiment = sentiment;
                                longest = partText.length();
                            }
                        }
                    }
                    //add sentiment to tweet
                    //CoreNLP returns 0 or 1 for negative, 2 for neutral and 3 or 4 for positive sentiment
                    if(mainSentiment < 2 && mainSentiment >= 0){
                        tweet.setSentiment(Sentiment.NEGATIVE);
                    } else if(mainSentiment > 2 && mainSentiment <= 4){
                        tweet.setSentiment(Sentiment.POSITIVE);
                    }
                    if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
                        tweet.setSentiment(Sentiment.NEUTRAL);
                    }
                    System.out.println(mainSentiment);
                }
    }
        //add sentiment to tweet


    private String toCss(int sentiment) {
        switch (sentiment) {
            case 0:
                return "alert alert-danger";
            case 1:
                return "alert alert-danger";
            case 2:
                return "alert alert-warning";
            case 3:
                return "alert alert-success";
            case 4:
                return "alert alert-success";
            default:
                return "";
        }
    }

    public static void main(String[] args) throws IOException {
        TweetLoader tweetLoader = new TweetLoader();
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        ArrayList<TopicModelTweet> tweets = tweetLoader.getTweetsFromFile("D:\\Programming\\P6\\assets\\CleanedData\\2.txt");
        ArrayList<TopicModelTweet> tweetsTest = new ArrayList<>();
        TopicModelTweet tweet1 = new TopicModelTweet("1", "i do not like mcDonalds", Sentiment.NEUTRAL);
        TopicModelTweet tweet2 = new TopicModelTweet("2", "i hate McDonalds", Sentiment.NEUTRAL);

        tweetsTest.add(tweet1);
        tweetsTest.add(tweet2);
        sentimentAnalyzer.findSentiment(tweetsTest);
        for (TopicModelTweet t: tweetsTest) {
            System.out.println(t.getSentiment());
        }
    }
}