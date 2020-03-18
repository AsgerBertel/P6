package DataGeneration;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class DataGeneration {

    private final static String NEG = "Negative", POS = "Positive", NEU = "Neutral";

    public static String GenerateOpinion() {

        int x = (int) (Math.random() * ((3 - 1) + 1)) + 1;
        if (x == 1)
            return NEG;
        else if (x == 2)
            return POS;
        else
            return NEU;
    }

    public static double generateLat(double max_Lat, double min_Lat) {

        return  (Math.random() * ((max_Lat - min_Lat))) + min_Lat;
    }

    public static double generateLong(double max_Long, double min_Long) {
        return  (Math.random() * ((max_Long - min_Long))) + min_Long;
    }

    public static String generateProduct() throws IOException {
        int i = (int) (Math.random() * ((41 - 1))) + 1;
        return Files.readAllLines(Paths.get("C:/Users/madsf/Desktop/Sentence word lists/1/Products.txt")).get(i);
    }

    public static String generateDate() {
        int day;
        int month = (int) (Math.random() * ((12 - 1))) + 1;
        int year = (int) (Math.random() * ((2020 - 2018)+1)) + 2018;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 9 || month == 11 || month == 12)
            day = (int) (Math.random() * ((31 - 1))) + 1;
        else if (month == 2)
            day = (int) (Math.random() * ((28 - 1))) + 1;
        else {
            day = (int) (Math.random() * ((30 - 1))) + 1;
        }
        return day + "/" + month + "/" + year;
    }

}
