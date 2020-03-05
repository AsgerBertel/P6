import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class main {
    public static void main(String[] args) {
        /*
        ArrayList<CoreLabel> tokenStream = new ArrayList<>();

        tokenStream = tokenizer("Mads Bau is a beautiful human being. He deserves to die.");
        System.out.println(tokenStream);
        Sentence sent = new Sentence("Mads ");
        nerTag(sent);



         */
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.findSentiment("Mads Bau loves McDonalds");
    }


    public static ArrayList<CoreLabel> tokenizer(String tweet){
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
        for(CoreSentence sentence: allSentences){
            allSentenceTokens.addAll(sentence.tokens());
        }
        List<CoreLabel> firstSentenceTokens = exampleDocument.sentences().get(0).tokens();
        // this for loop will print out all of the tokens and the character offset info
        for (CoreLabel token : firstSentenceTokens) {
            System.out.println(token.word() + "\t" + token.beginPosition() + "\t" + token.endPosition());
        }
        return allSentenceTokens;
    }
    public static List<String> nerTag(Sentence tweetText){
        List<String> nerTags = tweetText.nerTags();
        String FirstPOSTag = tweetText.posTag(0);
        for(String nerTag: nerTags){
            System.out.println(nerTag + "\t");
        }
        return nerTags;
    }

}
