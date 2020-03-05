package DataGeneration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LineCounter {
    public static int lineCount(String address) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(address));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }
}
