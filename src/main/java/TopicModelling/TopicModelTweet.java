package TopicModelling;

import java.util.ArrayList;

public class TopicModelTweet {
    String id, modified_text, original_text;
    Sentiment sentiment;

    ArrayList<String> topics = new ArrayList<>();

    public TopicModelTweet(String id, String modified_text, Sentiment sentiment) {
        this.id = id;
        this.modified_text = modified_text;
        this.sentiment = sentiment;
    }

    public TopicModelTweet(String id, String modified_text, String original_text) {
        this.id = id;
        this.modified_text = modified_text;
        this.original_text = original_text;
    }

    public String getId() {
        return id;
    }


    public String getModified_text() {
        return modified_text;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }
}
