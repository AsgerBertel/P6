package TopicModelling;

import java.util.ArrayList;

public class TopicModelTweet {
    String id, text;
    ArrayList<String> topics = new ArrayList<>();

    public TopicModelTweet(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
