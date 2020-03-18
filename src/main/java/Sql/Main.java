package Sql;

import atlas.Atlas;
import atlas.City;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
    private void multithreadedInsertIntoLocation() throws SQLException {
        InsertThread t1 = new InsertThread(true);
        InsertThread t2 = new InsertThread(false);
        t1.start();
        t2.start();
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
