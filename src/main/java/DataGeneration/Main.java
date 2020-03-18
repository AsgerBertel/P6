package DataGeneration;

import java.io.*;
import java.text.DecimalFormat;

public class Main {
    static int n = 100000000;
    private static DecimalFormat df2 = new DecimalFormat("#.#");
    public static void main(String[] args) {
         double p= 0;

        for (int i = 0; i < n; i++) {
            if (i % (n / 1000) == 0) {
                System.out.print("\r " + df2.format(p) + "%");
                p = p + 0.1;
                if (p >= 99.9)
                    System.out.print("\r " + 100 + "%");
            }
            try {
                File file = new File("C:/Users/madsf/Desktop/Sentence word lists/GeneratedData/tweets.txt");
                FileWriter fr = new FileWriter(file, true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(generateRandomTweet().toString());
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
     /*   try {
            System.out.println( LineCounter.lineCount("C:/Users/madsf/Desktop/Sentence word lists/GeneratedData/tweets.txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public static Tweet generateRandomTweet() throws IOException {
        Tweet tweet;
        int x = (int) (Math.random() * ((100 - 1) + 1)) + 1;
        if (x < 15 && x > 1) {
            //staten island
            tweet = new Tweet(DataGeneration.generateProduct(), DataGeneration.generateLat(40.639261, 40.588129), DataGeneration.generateLong(74.155931, 74.072087), DataGeneration.GenerateOpinion(), DataGeneration.generateDate());

        } else if (x < 30 && x > 15) {
            //Brooklyn
            tweet = new Tweet(DataGeneration.generateProduct(), DataGeneration.generateLat(40.744731, 40.600113), DataGeneration.generateLong(74.003468, 73.879982), DataGeneration.GenerateOpinion(), DataGeneration.generateDate());

        } else if (x < 45 && x > 30) {
            //Manhattan
            tweet = new Tweet(DataGeneration.generateProduct(), DataGeneration.generateLat(40.814722, 40.736384), DataGeneration.generateLong(74.008487, 73.960494), DataGeneration.GenerateOpinion(), DataGeneration.generateDate());

        } else if (x < 60 && x > 45) {
            tweet = new Tweet(DataGeneration.generateProduct(), DataGeneration.generateLat(40.734631, 40.689349), DataGeneration.generateLong(73.846514, 73.738024), DataGeneration.GenerateOpinion(), DataGeneration.generateDate());

        } else {
            tweet = new Tweet(DataGeneration.generateProduct(), DataGeneration.generateLat(40.915568, 40.495992), DataGeneration.generateLong(74.257159, 73.699215), DataGeneration.GenerateOpinion(), DataGeneration.generateDate());

        }
        return tweet;
    }
}
