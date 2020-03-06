package TweetCleaner;

public class Tweet {
    String id, text, date;
    double longitude, latitude;

    public Tweet(String id, String text, String date, double latitude, double longitude) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
