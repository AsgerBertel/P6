package Sql;

import atlas.Atlas;
import org.json4s.JsonAST;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    ResultSet resultSet;
    ArrayList<TweetElement> listOfFactTableElements;

    public static void main(String[] args) {
        Main main = new Main();

        try {
            //main.insertIntoProduct();
            //  main.insertIntoDates();
            //  main.multithreadedInsertIntoLocation();
            main.insertIntoFactTable();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoFactTable() throws SQLException {
        ResultSet resultSets;

        resultSets = ConnectionManager.selectSQL(QueryManager.selectAllFromTweet);
        while (resultSets.next()) {
            TweetElement tweetElement = new TweetElement(resultSets.getString(1), resultSets.getDouble(2), resultSets.getDouble(3), resultSets.getString(4), resultSets.getString(5));

            listOfFactTableElements.add(tweetElement);
        }


    }
    private int getProductID(String product) throws SQLException{

        resultSet = ConnectionManager.selectSQL(QueryManager.selectProductIDFromProduct(product));
        return resultSet.getInt(1);
    }
    private int getOpinionID(String opinion) throws SQLException{

        resultSet = ConnectionManager.selectSQL(QueryManager.selectOpinionIDFromOpinion(opinion));
        return resultSet.getInt(1);
    }

    private int getLocationID(double lat, double longi) throws SQLException{

        resultSet = ConnectionManager.selectSQL(QueryManager.selectLocationIDFromCoordinates(lat,longi));
        return resultSet.getInt(1);
    }

    private int getDateID(String product) throws SQLException{

     //   resultSet = ConnectionManager.selectSQL(QueryManager.selectProductsFromTweet(product));
        return resultSet.getInt(1);
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

