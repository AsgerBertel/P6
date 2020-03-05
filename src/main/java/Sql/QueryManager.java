package Sql;

public class QueryManager {
    public static String connectionString = "jdbc:postgresql://localhost:5432/cubefrequency?user=postgres&password=Password";
    public static String selectDateFromTweet = "SELECT date FROM cube.tweets ";


    public static String insertIntoWareHouse(int day, int month, int year) {
        return "INSERT INTO cube.datedimenson (year,month,day)  VALUES ('" + year + "', '" + month + "', '" + day + "')";
    }//INSERT INTO cube.datedimenson (year,month,day)  VALUES (2020, 10, 30)

}
