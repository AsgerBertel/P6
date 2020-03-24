package Sql;


public class QueryManager {
    public static String connectionString = "jdbc:postgresql://62.107.118.55:5432/cubefrequency?user=postgres&password=password";
    public static String selectDateFromTweet = "SELECT date FROM cube.tweets ";
    public static String selectCoordinatesFromTweet = "SELECT lat,long FROM cube.tweets WHERE cube.tweets.tweetid > 10000000";
    public static String selectAllFromTweet = "SELECT product,lat,long,opinion,date FROM cube.tweets Where tweetid < 4000000";
    public static String selectProductIDFromProduct = "SELECT lat,long FROM cube.tweets ";

    public static String insertIntoDate(int day, int month, int year) {
        return "INSERT INTO cube.datedimenson (year,month,day)  VALUES ('" + year + "', '" + month + "', '" + day + "')";
    }

    public static String selectProductIDFromProduct(String product) {
        return "SELECT productid FROM cube.product WHERE product.product = '" + product + "'";
    }

    public static String selectOpinionIDFromOpinion(String opinion) {
        return "SELECT opinionid FROM cube.opinion WHERE opinion.opinion = '" + opinion + "'";
    }

    public static String selectLocationIDFromCoordinates(double lat, double longi) {
        return "SELECT coordinate.locationid FROM cube.coordinate WHERE lat = " + lat + "' AND long =" + longi + "";
    }


    public static String insertIntoLocation(String district, String county, String City, String country, double lat, double longtitude) {
        return "INSERT INTO cube.locationdimension (district,county,City,country,lat,long)  VALUES ( '" + district + "', '" + county + "','" + City + "','" + country + "','" + lat + "','" + longtitude + "')";
    }

    public static String insertIntoProduct(String product, String category) {
        return "INSERT INTO cube.productdimension (category,product)  VALUES ( '" + product + "','" + category + "')";
    }

}
