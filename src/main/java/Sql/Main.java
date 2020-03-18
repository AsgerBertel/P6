package Sql;

import atlas.Atlas;
import atlas.City;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    ResultSet resultSet;

    public static void main(String[] args) {
        Main main = new Main();

        try {
            //main.insertIntoProduct();
            // main.insertIntoDates();
            main.insertIntoLocation();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void insertIntoDates() throws SQLException {
      /*  resultSet = ConnectionManager.selectSQL(QueryManager.selectDateFromTweet);
        while (resultSet.next()) {
            String result = resultSet.getString(1);
            String[] arrOfStr = result.split("/");
            ConnectionManager.updateSql(QueryManager.insertIntoDate(Integer.parseInt(arrOfStr[0].trim()), Integer.parseInt(arrOfStr[1].trim()), Integer.parseInt(arrOfStr[2].trim())));
        }*/


        for (int year = 2018; year <= 2020; year++) {
            for (int month = 1; month <= 12; month++) {
                for (int day = 1; day <= 31; day++) {
                    ConnectionManager.updateSql(QueryManager.insertIntoDate(day, month, year));

                }
            }
        }
    }

    private void insertIntoLocation() throws SQLException {
        boolean test = true;
        resultSet = ConnectionManager.selectSQL(QueryManager.selectCoordinatesFromTweet);
        while (resultSet.next()) {
            Atlas atlas = new Atlas();
            City city = atlas.find(resultSet.getDouble(1), resultSet.getDouble(2));

            ConnectionManager.updateSql(QueryManager.insertIntoLocation(city.name, city.admin2, city.admin1, city.countryCode, resultSet.getDouble(1), resultSet.getDouble(2)));
            if (test == true) {
                System.out.printf("first row");
                test = false;
            }
        }

    }

    private void insertIntoProduct() throws SQLException, FileNotFoundException {
        File file =
                new File("C:/Users/madsf/Desktop/Sentence word lists/Map/ProductCategory.txt");
        Scanner sc = new Scanner(file);

        String[] values;
        while (sc.hasNextLine()) {
            // String[] arrSplit = strMain.split(", ");
            values = sc.nextLine().split(",");
            ConnectionManager.updateSql(QueryManager.insertIntoProduct(values[1], values[0]));
        }

    }


}
