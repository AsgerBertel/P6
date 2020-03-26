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
    ArrayList<TweetElement> listOfFactTableElements = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();


       try {
            //main.insertIntoProduct();
            //  main.insertIntoDates();
            //  main.multithreadedInsertIntoLocation();
            //  main.insertIntoFactTable();
            main.insertIntoFactTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoFactTable() throws SQLException {
        ResultSet resultSets;

        resultSets = ConnectionManager.selectSQL(QueryManager.selectAllFromTweet);
        System.out.println("starting loading list");
        while (resultSets.next()) {
            TweetElement tweetElement = new TweetElement(resultSets.getString(1), resultSets.getDouble(2), resultSets.getDouble(3), resultSets.getString(4), resultSets.getString(5));
            listOfFactTableElements.add(tweetElement);

        }
        System.out.println(listOfFactTableElements.size());
        System.out.println("done loading data");
        System.out.println("inserting data");
        for (TweetElement tweetelement : listOfFactTableElements) {

            String[] arrOfStr = tweetelement.getDate().split("/", 4);
            arrOfStr[0] = arrOfStr[0].trim();
         //   System.out.println(tweetelement.getProduct());
                int productID = getProductID(tweetelement.getProduct().trim());
                int opinionID = getOpinionID(tweetelement.getOpinion().trim());
                int dateID = getDateID(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));
                int locationID = getLocationID(tweetelement.getLat(), tweetelement.getLongitude());
            if(productID != 0 || opinionID != 0 || dateID != 0 || locationID != 0)
            ConnectionManager.updateSql(QueryManager.insertFactTable(productID,opinionID,dateID , locationID));

        }

    }

    private int getProductID(String product) throws SQLException {
        ResultSet resultSet;
        resultSet = ConnectionManager.selectSQL(QueryManager.selectProductIDFromProduct(product));

        while (resultSet.next())
            return resultSet.getInt(1);
        return 0;

    }

    private int getOpinionID(String opinion) throws SQLException {
        ResultSet resultSet;
        resultSet = ConnectionManager.selectSQL(QueryManager.selectOpinionIDFromOpinion(opinion));
        while (resultSet.next())
            return resultSet.getInt(1);
        return 0;
    }

    private int getLocationID(double lat, double longi) throws SQLException {
        ResultSet resultSet;
        resultSet = ConnectionManager.selectSQL(QueryManager.selectLocationIDFromCoordinates(lat, longi));
        while (resultSet.next())
            return resultSet.getInt(1);
        return 0;
    }

    private int getDateID(int day, int month, int year) throws SQLException {
        ResultSet resultSet;
        resultSet = ConnectionManager.selectSQL(QueryManager.selectDayIDFromDay(day, month, year));
        while (resultSet.next())
            return resultSet.getInt(1);
        return 0;
    }


    private void insertIntoDates() throws SQLException {
      /*  resultSet = ConnectionManager.selectSQL(QueryManager.selectDateFromTweet);
        while (resultSet.next()) {
            String result = resultSet.getString(1);
            String[] arrOfStr = result.split("/");
            ConnectionManager.updateSql(QueryManager.insertIntoDate(Integer.parseInt(arrOfStr[0].trim()), Integer.parseInt(arrOfStr[1].trim()), Integer.parseInt(arrOfStr[2].trim())));
        }*/

        for (int month = 1; month <= 36; month++) {
            for (int day = 1; day <= 31; day++) {
                ConnectionManager.updateSql(QueryManager.insertIntoDay(day, month));
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

