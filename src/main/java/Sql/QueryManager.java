package Sql;


public class QueryManager {
    public static String connectionString = "jdbc:postgresql://62.107.118.55:5432/cubefrequency?user=postgres&password=password";
    public static String selectDateFromTweet = "SELECT date FROM cube.tweets ";
    public static String selectCoordinatesFromTweet = "SELECT lat,long FROM cube.tweets WHERE cube.tweets.tweetid > 10000000";
    public static String selectAllFromTweet = "SELECT product,lat,long,opinion,date FROM cube.tweets Where tweetid > 162928 AND tweetid < 2000000";
    public static String selectProductIDFromProduct = "SELECT lat,long FROM cube.tweets ";
    public static String selectCoordinatesidFromCoordinates = "SELECT locationid FROM cubefrequency.cube.coordinate";

    public static String insertIntoDate(int day, int month, int year) {
        return "INSERT INTO cubefrequency.cube.datedimension (year,month,day)  VALUES ('" + year + "', '" + month + "', '" + day + "')";
    }

    public static String insertIntoYear(int year){
        return "INSERT INTO cubefrequency.cube.year (year) VALUES ("+year+")";
    }
    public static String insertIntoMonth(int month, int year){
        return "INSERT INTO cubefrequency.cube.month (month, yearid) VALUES("+month+", "+year+")";
    }

    public static String insertIntoDay(int day, int month) {
        return "INSERT INTO cube.day (monthid,day)  VALUES (" + month + ", " + day + ")";
    }

    public static String selectProductIDFromProduct(String product) {
        return "SELECT productid FROM cube.product WHERE product.product = '" + product + "'";
    }

    public static String selectOpinionIDFromOpinion(String opinion) {
        return "SELECT opinionid FROM cube.opinion WHERE opinion.opinion = '" + opinion + "'";
    }

    public static String selectLocationIDFromCoordinates(double lat, double longi) {    
        return "SELECT coordinate.locationid FROM cube.coordinate WHERE lat = " + lat + " AND long =" + longi + "";
    }

    public static String selectDayIDFromDay(int day, int month, int year) {
        return "SELECT dayid FROM cube.day d INNER JOIN cube.month ON d.monthid = month.monthid INNER JOIN cube.year ON month.yearid = year.yearid WHERE day = " + day + " AND month.month = " + month + " AND year.year =" + year + "";
    }

    /**/
    public static String selectAllProductIDFromProduct() {
        return "SELECT productid FROM cube.product";
    }

    public static String selectAllOpinionIDFromOpinion() {
        return "SELECT opinionid FROM cube.opinion";
    }

    public static String selecAlltLocationIDFromCoordinates() {
        return "SELECT coordinate.locationid FROM cube.coordinate";
    }

    public static String selectAllDayIDFromDay(int day, int month, int year) {
        return "SELECT dayid FROM cube.day d INNER JOIN cube.month ON d.monthid = month.monthid INNER JOIN cube.year ON month.yearid = year.yearid WHERE day = " + day + " AND month.month = " + month + " AND year.year= " + year + "";
    }

    public static String insertFactTable(int productid, int opinionid, int dateid, int coordinateid) {
        return "INSERT INTO cube.facttable ( productid, opinionid, dateid, coordinateid)\n" +
                "VALUES( " + productid + ", " + opinionid + "," + dateid + "," + coordinateid + ")";
    }

    public static String insertIntoLocation(String district, String county, String City, String country, double lat, double longtitude) {
        return "INSERT INTO cube.locationdimension (district,county,City,country,lat,long)  VALUES ( '" + district + "', '" + county + "','" + City + "','" + country + "','" + lat + "','" + longtitude + "')";
    }

    public static String insertIntoProduct(String product, String category) {
        return "INSERT INTO cube.productdimension (category,product)  VALUES ( '" + product + "','" + category + "')";
    }
    public static String insertIntoTopTopics(int id, String topic){
        return "INSERT INTO cube.toptopics (toptopicid, topic) VALUES (" + id + "," + "'" + topic + "')";
    }

    public static String insertIntoSubTopics(String topic, int toptopicid){
        return "INSERT INTO cube.subtopics (subtopic, toptopicid) VALUES ('" + topic + "'," + toptopicid + ")";
    }
}
