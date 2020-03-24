package Sql;

public class TweetElement {
    private String product;
    private double lat;
    private double longitude;
    private String opinion;
    private String date;

    public TweetElement(String product, double lat, double longitude, String opinion, String date) {
        this.product = product;
        this.lat = lat;
        this.longitude = longitude;
        this.opinion = opinion;
        this.date = date;
    }
}
