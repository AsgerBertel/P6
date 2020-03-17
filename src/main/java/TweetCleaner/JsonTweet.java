package TweetCleaner;

public class JsonTweet {
    String id, text, date, sentiment;
    double longitude, latitude;

    public JsonTweet(String id, String text, String date, double latitude, double longitude, String sentiment) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sentiment = sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getSentiment() {
        return sentiment;
    }

    public String getText() {
        return text;
    }
}
