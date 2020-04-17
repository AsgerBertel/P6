package Sql;

import atlas.Atlas;
import org.apache.spark.sql.catalyst.plans.logical.Except;
import org.json4s.DateFormat;
import org.json4s.JsonAST;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.*;
//FileGeneratorClass kald function når den er done
public class Main {
    ResultSet resultSet;
    ArrayList<TweetElement> listOfFactTableElements = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        FactTableIdGenerator factTableIdGenerator = new FactTableIdGenerator();
        Set<Date> dateSet;

        try {

            dateSet = main.getDatesFromTweets("assets/CleanedData/tweetsWithTopicAndSentimentAndCoordinatesMetaFile.txt");
            for(Date d: dateSet){
                System.out.println();
                //ConnectionManager.updateSql(QueryManager.insertIntoDate(d.getDay(), d.getMonth(), d.getYear()));
            }


            System.out.println(dateSet.size());
            //main.insertIntoDates(dateSet);
            //factTableIdGenerator.generateFactTableElement();
            //main.insertIntoProduct();
            //  main.insertIntoDates();
            //  main.multithreadedInsertIntoLocation();
            //  main.insertIntoFactTable();
            //main.insertIntoFactTable();


        } catch (IOException e) {
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
            if (productID != 0 || opinionID != 0 || dateID != 0 || locationID != 0)
                ConnectionManager.updateSql(QueryManager.insertFactTable(productID, opinionID, dateID, locationID));

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


    private void insertIntoDates(Set<Date> dateSet) throws SQLException {
        ArrayList<Integer> alreadyExistsList = new ArrayList<>();
        for(Date d: dateSet){
            if(alreadyExistsList.contains(d.getYear()))
            ConnectionManager.updateSql(QueryManager.insertIntoYear(d.getYear()));
            alreadyExistsList.add(d.getYear());
        }

        //ConnectionManager.updateSql(QueryManager.insertIntoDate())

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

    private Set<Date> getDatesFromTweets(String file) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String tweet;
        String[] tweetArray = null;
        HashSet<Date> uniqueDates = new HashSet<>();
        int i = 0;
        while( (tweet = bf.readLine())!= null){
            try{
                tweetArray = tweet.split("\\|");
            } catch(Exception e){

            }
            int month = 0;
            try {

                if(!tweetArray[2].contains("+0000")){
                    continue;
                }

                String[] datearray = tweetArray[2].split("\\s");
                Date date = new Date(Integer.parseInt(datearray[5]),findMonthInDate(datearray[1]), Integer.parseInt(datearray[2]));

                resultSet = ConnectionManager.selectSQL(QueryManager.selectAllDayIDFromDay(date.getDay(), date.getMonth(), date.getYear()));

                int dateID = 0;
                while(resultSet.next()){
                    dateID = resultSet.getInt(1);
                }
                PrintWriter writer = new PrintWriter("assets/CleanedData/test.txt");
                writer.write(tweetArray[1] + "|" + dateID + "|" + tweetArray[3].trim() + "|" + tweetArray[4] + "\n");

                uniqueDates.add(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uniqueDates;
    }

    private int findMonthInDate(String month) throws Exception {
        switch(month){
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;
            case "Apr":
                return 4;
            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sep":
                return 9;
            case "Okt":
                return 10;
            case "Nov":
                return 11;
            case "Dec":
                return 12;
            default:
                throw new Exception("Month is not a month:" + month);
        }

    }
}

