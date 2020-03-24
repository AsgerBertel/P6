package Sql;

import atlas.Atlas;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    ResultSet resultSet;

    public static void main(String[] args) {
        Main main = new Main();

        try {
            //main.insertIntoProduct();
            //  main.insertIntoDates();
            //  main.multithreadedInsertIntoLocation();
            // main.insertIntoFactTable();
            main.test();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void test() throws SQLException {
        String product = "fish";
        double lat;
        double longitude;
        String opinion;
        String date;
        System.out.println(product);
        resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromTweet);
        while (resultSet.next()) {

            System.out.println(resultSet.getInt(1));
        }

    }

    private void insertIntoFactTable() throws SQLException {
        String product;
        double lat;
        double longitude;
        String opinion;
        String date;

        resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromTweet);

        product = resultSet.getString(2);
        lat = resultSet.getDouble(3);
        longitude = resultSet.getDouble(4);
        opinion = resultSet.getString(5);
        date = resultSet.getString(6);
        System.out.println(product);


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

    private void multithreadedInsertIntoLocation() throws SQLException, InterruptedException {
        Atlas atlas = new Atlas();
        resultSet = ConnectionManager.selectSQL(QueryManager.selectCoordinatesFromTweet);
        System.out.println("connected/loaded");
        SynchronizedLocationManager syncSet = new SynchronizedLocationManager(resultSet);

        ArrayList<InsertThread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            InsertThread t1 = new InsertThread(syncSet, atlas);
            threads.add(t1);
            t1.start();
        }
        for (InsertThread it : threads) {
            it.join();
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

