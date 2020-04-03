package TopicModelling;

import java.util.ArrayList;

public class TopicModelTweet {
    String id, topic_text, original_text;
    Sentiment sentiment;
    String date;
    private ArrayList<String> topics = new ArrayList<>();

    public ArrayList<String> getTopics() {
        return topics;
    }

    public String getOriginal_text() {
        return original_text;
    }

    public void addTopic(String s){
        topics.add(s);
    }

    public TopicModelTweet(String id, String topic_text, String original_text, String date) {
        this.id = id;
        this.topic_text = topic_text;
        this.original_text = original_text;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getTopicsString(){
        StringBuilder sb = new StringBuilder();
        for(int i =0; i < topics.size(); i++){
            sb.append(topics.get(i));
            if(i != topics.size()-1){
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    @Override
    public String toString() {
        return
                original_text + "|"
                + getTopicsString() + "|"
                + getSentiment() + "|"
                + date;
    }
}
