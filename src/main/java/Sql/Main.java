package Sql;

import atlas.Atlas;
import atlas.City;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    ResultSet resultSet;

    public static void main(String[] args) {
        Main main = new Main();
        Atlas atlas = new Atlas();
        City city= atlas.find(40.5644630645709, -74.0703918547116);
        System.out.println(city);
       /* try {
            main.insertDates();

        } catch (SQLException e) {
            e.printStackTrace();
        }*/


    }

    private void insertDates() throws SQLException {
        resultSet = ConnectionManager.selectSQL(QueryManager.selectDateFromTweet);
        while (resultSet.next()) {
            String result = resultSet.getString(1);
            String[] arrOfStr = result.split("/");
              ConnectionManager.updateSql(QueryManager.insertIntoWareHouse(Integer.parseInt(arrOfStr[0].trim()), Integer.parseInt(arrOfStr[1].trim()), Integer.parseInt(arrOfStr[2].trim())));
        }
    }
}
