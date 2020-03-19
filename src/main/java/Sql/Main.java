package Sql;



import atlas.Atlas;
import org.apache.avro.generic.GenericData;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    ResultSet resultSet;

    public static void main(String[] args) {
        Main main = new Main();

        try {
            //main.insertIntoProduct();
            // main.insertIntoDates();
            main.multithreadedInsertIntoLocation();

        } catch (SQLException | InterruptedException e) {
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
    private void multithreadedInsertIntoLocation() throws SQLException, InterruptedException {
        Atlas atlas = new Atlas();
        resultSet = ConnectionManager.selectSQL(QueryManager.selectCoordinatesFromTweet);
        System.out.println("connected/loaded");
        SynchronizedLocationManager syncSet = new SynchronizedLocationManager(resultSet);

        ArrayList<InsertThread> threads = new ArrayList<>();
        for(int i =0; i < 5; i++){
            InsertThread t1 = new InsertThread(syncSet, atlas);
            threads.add(t1);
            t1.start();
        }
        for(InsertThread it : threads){
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
class SynchronizedLocationManager {
    final Object lock = new Object();
    ResultSet resultSet;

    public SynchronizedLocationManager(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    public DatabaseLocation syncedGetNext(Atlas atlas) throws SQLException {
        synchronized (lock){
                if(resultSet.next()){
                    double latitude = this.resultSet.getDouble(1);
                    double longitude = this.resultSet.getDouble(2);
                    return new DatabaseLocation(atlas.find(latitude,longitude),latitude,longitude);
                }
                else{
                    throw new LastRowReachedException("Last row of results set has been reached");
                }
        }
    }
}
