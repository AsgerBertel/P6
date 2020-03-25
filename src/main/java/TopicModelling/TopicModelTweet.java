package TopicModelling;

import java.util.ArrayList;

public class TopicModelTweet {
    String id, text, sentiment;

    ArrayList<String> topics = new ArrayList<>();

    public TopicModelTweet(String id, String text, String sentiment) {
        this.id = id;
        this.text = text;
        this.sentiment = sentiment;
    }

    public String getId() {
        return id;
    }


    public String getText() {
        return text;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getSentiment() {
        return sentiment;
    }
}
