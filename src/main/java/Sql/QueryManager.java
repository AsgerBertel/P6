package Sql;

public class QueryManager {
    public static String connectionString = "jdbc:sqlserver://127.0.0.1;databaseName=WareHousedb;integratedSecurity=true";



    public static String insertIntoWareHouse(String wareHouseMapName, String width, String length) {
        return "INSERT INTO WareHouse VALUES ('" + wareHouseMapName + "', '" + width + "', '" + length + "')";
    }
}
