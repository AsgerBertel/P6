package SentimentAnalysis_CoreNLP;

public class TweetWithSentiment {

    private String line;
    private String cssClass;

    public TweetWithSentiment() {
    }

    public TweetWithSentiment(String line, String cssClass) {
        super();
        this.line = line;
        this.cssClass = cssClass;
    }

    public String getLine() {
        return line;
    }

    public String getCssClass() {
        return cssClass;
    }

    @Override
    public String toString() {
        return "SentimentAnalysis_CoreNLP.TweetWithSentiment [line=" + line + ", cssClass=" + cssClass + "]";
    }

}