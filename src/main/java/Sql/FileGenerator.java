package Sql;

import uk.ac.wlv.sentistrength.SentiStrength;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class FileGenerator {

    private ResultSet resultSet;

    public void createFileWithLocationId() throws SQLException, IOException {

        resultSet = ConnectionManager.selectSQL(QueryManager.selectCoordinatesidFromCoordinates);
        System.out.println("starting loading list");
        File sentimenttext = new File("C:/Users/madsf/Desktop/tweet-editing/locationid.txt");
        FileWriter fr = new FileWriter(sentimenttext, true);
        BufferedWriter writer = new BufferedWriter(fr);
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
            writer.write(resultSet.getInt(1) + "\n");

        }
        writer.close();
    }

    public void mergeFiles() throws FileNotFoundException, IOException {
        PrintWriter pw = new PrintWriter(new File("C:/Users/madsf/Desktop/tweet-editing/tweetsWithTopicAndSentimentAndCoordinatesMetaFile.txt"));
        // BufferedReader object for file1.txt
        BufferedReader br1 = new BufferedReader(new FileReader("C:/Users/madsf/Desktop/tweet-editing/tweetsWithTopicAndSentimentMetaFile.txt"));
        BufferedReader br2 = new BufferedReader(new FileReader("C:/Users/madsf/Desktop/tweet-editing/locationid.txt"));
        BufferedReader br4 = new BufferedReader(new FileReader("C:/Users/madsf/Desktop/tweet-editing/locationid.txt"));
        BufferedReader br3 = new BufferedReader(new FileReader("C:/Users/madsf/Desktop/tweet-editing/locationid.txt"));


        String output = "";
        try (Scanner sc1 = new Scanner((new File("C:/Users/madsf/Desktop/tweet-editing/tweetsWithTopicAndSentimentMetaFile.txt")));
             Scanner sc2 = new Scanner((new File("C:/Users/madsf/Desktop/tweet-editing/locationid.txt")))) {
            System.out.println("write to file");
            String firstLine = "";
            String secondLine = "";
            int i = 0;
            while (i < 4935709 ) {
                firstLine = br1.readLine();
                secondLine = br2.readLine();
                if (secondLine ==null){
                    br2 = br4;
                    output = firstLine + " |" + secondLine;
                    output += "\n";

                } else{
                    output = firstLine + " |" + secondLine;
                    output += "\n";
                }
               // System.out.println(output);
                pw.write(output);
                i++;
            }

        }

    }

}
