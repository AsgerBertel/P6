

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import TweetCleaner.JsonTweet;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.json.JSONObject;

public class SentimentAnalyzer {

    public void findSentiment(ArrayList<JsonTweet> tweets) {
                Properties props = new Properties();
                props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
                StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
                for (JsonTweet tweet : tweets) {

                    int mainSentiment = 0;
                    String line = tweet.getText();
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
                        tweet.setSentiment("Negative");
                    } else if(mainSentiment > 2 && mainSentiment <= 4){
                        tweet.setSentiment("Positive");
                    }
                    if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
                        tweet.setSentiment("Neutral");
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

    public static void main(String[] args) {
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        ArrayList<JsonTweet> tweets = new ArrayList<>();
        JsonTweet tweet1 = new JsonTweet("1", "Mads loves mcDonalds", "2020", 20.0, 10.0, null);
        JsonTweet tweet2 = new JsonTweet("2", "Mads hates mcDonalds", "2020", 20.0, 10.0, null);
        JsonTweet tweet3 = new JsonTweet("3", "Mads do not like mcDonalds", "2020", 20.0, 10.0, null);
        JsonTweet tweet4 = new JsonTweet("4", "Mads thinks mcDonalds is distasteful. The food industry is filled with nasty ", "2020", 20.0, 10.0, null);
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);
        tweets.add(tweet4);
        sentimentAnalyzer.findSentiment(tweets);
        for (JsonTweet t:tweets) {
            System.out.println(t.getSentiment());
        }
        System.out.println();
    }
}