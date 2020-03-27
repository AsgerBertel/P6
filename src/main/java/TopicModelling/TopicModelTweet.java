package TopicModelling;

import java.util.ArrayList;

public class TopicModelTweet {
    String id, topic_text, original_text, sentiment_text;
    Sentiment sentiment;
    private ArrayList<String> topics = new ArrayList<>();
    public TopicModelTweet(String id, String topic_text, Sentiment sentiment) {
        this.id = id;
        this.topic_text = topic_text;
        this.sentiment = sentiment;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void addTopic(String s){
        topics.add(s);
    }

    public TopicModelTweet(String id, String topic_text, String original_text) {
        this.id = id;
        this.topic_text = topic_text;
        this.original_text = original_text;
    }

    public String getId() {
        return id;
    }


    public String getTopic_text() {
        return topic_text;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }
}
