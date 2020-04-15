package Sql;

import DataGeneration.DataGeneration;
import DataGeneration.Tweet;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FactTableIdGenerator {
    private ResultSet resultSet;
    private ArrayList<Integer> listOfProductID = new ArrayList<>();
    private ArrayList<Integer> listOfOpinionID = new ArrayList<>();
    private ArrayList<Integer> listOfLocationID = new ArrayList<>();
    private ArrayList<Integer> listOfDateID = new ArrayList<>();
    FactTable factTable;
    private int n = 6000000;
    private static DecimalFormat df2 = new DecimalFormat("#.#");

    public void generateFactTableElement() throws SQLException {
        //getDateID();
        getLocationID();
        getProductID();
        getOpinionID();

        double p = 0;
        for (int i = 0; i < n; i++) {
            if (i % (n / 1000) == 0) {
                System.out.print("\r " + df2.format(p) + "%");
                p = p + 0.1;
                if (p >= 99.9)
                    System.out.print("\r " + 100 + "%");
            }
            printToFile();
        }

    }

    public void printToFile() {
        try {
            File file = new File("C:/Users/madsf/OneDrive/Skrivebord/FactTable Generation/factTable.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(generateRandomFactTableElement().toString());
            br.append('\n');
            br.close();
            fr.close();
            // System.out.println(tweet.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private FactTable generateRandomFactTableElement() {

        factTable = new FactTable(randomGenerator(listOfDateID), randomGenerator(listOfLocationID), randomGenerator(listOfOpinionID), randomGenerator(listOfProductID));

        return factTable;

    }

    private int randomGenerator(ArrayList<Integer> listOFID) {
        int x = (int) (Math.random() * (((listOFID.size() - 1) - 1) + 1)) + 1;
        return listOFID.get(x);
    }

    private void getProductID() throws SQLException {
        ResultSet resultSet;
        resultSet = ConnectionManager.selectSQL(QueryManager.selectAllProductIDFromProduct());

        while (resultSet.next()) {
            listOfProductID.add(resultSet.getInt(1));
        }

    }

    private void getOpinionID() throws SQLException {
        ResultSet resultSet;
        resultSet = ConnectionManager.selectSQL(QueryManager.selectAllOpinionIDFromOpinion());
        while (resultSet.next())
            listOfOpinionID.add(resultSet.getInt(1));

    }

    private void getLocationID() throws SQLException {
        ResultSet resultSet;
        resultSet = ConnectionManager.selectSQL(QueryManager.selecAlltLocationIDFromCoordinates());
        while (resultSet.next())
            listOfLocationID.add(resultSet.getInt(1));

    }

    /*private void getDateID() throws SQLException {
        ResultSet resultSet;
        resultSet = ConnectionManager.selectSQL(QueryManager.selectAllDayIDFromDay());
        while (resultSet.next())
            listOfDateID.add(resultSet.getInt(1));
    }*/


}
