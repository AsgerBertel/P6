package DataGeneration;

public class Tweet {
    private String product;
    private double latitude;
    private double longitude;
    private String opinion;
    private String date;

    public Tweet(String product, double latitude, double longitude, String opinion, String date) {
        this.product = product;
        this.latitude = latitude;
        this.longitude = longitude;
        this.opinion = opinion;
        this.date = date;
    }

    @Override
    public String toString() {
        return
                product +
                        ", " + latitude +
                        ", " + longitude +
                        ", " + opinion +
                        ", " + date +
                        ';';
    }
}
