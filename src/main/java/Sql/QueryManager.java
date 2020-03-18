package Sql;

public class QueryManager {
    public static String connectionString = "jdbc:postgresql://62.107.118.55:5432/cubefrequency?user=postgres&password=password";
    public static String selectDateFromTweet = "SELECT date FROM cube.tweets ";
        public static String selectCoordinatesFromTweet = "SELECT lat,long FROM cube.tweets ";
    public static String selectProductsFromTweet = "SELECT lat,long FROM cube.tweets ";


    public static String insertIntoDate(int day, int month, int year) {
        return "INSERT INTO cube.datedimenson (year,month,day)  VALUES ('" + year + "', '" + month + "', '" + day + "')";
    }

    public static String insertIntoLocation(String district, String county, String City, String country, double lat, double longtitude ) {
        return "INSERT INTO cube.locationdimension (district,county,City,country,lat,long)  VALUES ( '" + district + "', '" + county + "','" + City + "','" + country + "','" + lat + "','" + longtitude + "')";
    }

    public static String insertIntoProduct(String product, String category ) {
        return "INSERT INTO cube.productdimension (category,product)  VALUES ( '"+ product + "','" + category + "')";
    }

}
