package SentimentAnalysis_CoreNLP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import TopicModelling.Sentiment;
import TopicModelling.TopicModelTweet;
import TopicModelling.TweetLoader;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalyzer {

    public void findSentiment(ArrayList<TopicModelTweet> tweets) {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, parse, sentiment");
        props.put("threads", "4");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        HashMap<String, TopicModelTweet> map = new HashMap<>();
        List<Annotation> annotations = new ArrayList<>();
        for (TopicModelTweet tweet : tweets) {
            Annotation a = new Annotation(tweet.getOriginal_text());
            annotations.add(a);
            map.put(a.toString(), tweet);
        }
        pipeline.annotate(annotations);
        for (Annotation a : annotations) {
            int mainSentiment = 0;
            int longest = 0;
            for (CoreMap cm : a.get(CoreAnnotations.SentencesAnnotation.class)) {
                int sentiment = RNNCoreAnnotations.getPredictedClass(cm.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class));
                String partText = cm.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
            //add sentiment to tweet
            //CoreNLP returns 0 or 1 for negative, 2 for neutral and 3 or 4 for positive sentiment
            if (mainSentiment < 2 && mainSentiment >= 0) {
                map.get(a.toString()).setSentiment(Sentiment.NEGATIVE);
            } else if (mainSentiment > 2 && mainSentiment <= 4) {
                map.get(a.toString()).setSentiment(Sentiment.POSITIVE);
            }
            if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
                map.get(a.toString()).setSentiment(Sentiment.NEUTRAL);
            }
        }
    }


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

    /* Might not be useful*/
    public static ArrayList<CoreLabel> tokenizer(String tweet) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(tweet);
        CoreDocument exampleDocument = new CoreDocument(tweet);
        // annotate document
        pipeline.annotate(exampleDocument);
        // access tokens from a CoreDocument
        // a token is represented by a CoreLabel
        ArrayList<CoreLabel> allSentenceTokens = new ArrayList<>();
        List<CoreSentence> allSentences = exampleDocument.sentences();
        for (CoreSentence sentence : allSentences) {
            allSentenceTokens.addAll(sentence.tokens());
        }
        List<CoreLabel> firstSentenceTokens = exampleDocument.sentences().get(0).tokens();
        // this for loop will print out all of the tokens and the character offset info
        for (CoreLabel token : firstSentenceTokens) {
            System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
        }
        return allSentenceTokens;
    }

    public static List<String> nerTag(Sentence tweetText) {
        List<String> nerTags = tweetText.nerTags();
        String FirstPOSTag = tweetText.posTag(0);
        for (String nerTag : nerTags) {
            System.out.println(nerTag + "\t");
        }
        return nerTags;
    }

}
