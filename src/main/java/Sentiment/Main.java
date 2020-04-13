package Sentiment;


import uk.ac.wlv.sentistrength.SentiStrength;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Main main = new Main();
        try {
            main.createAnalysis();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void createAnalysis() throws FileNotFoundException, IOException {
        String opinion;
        String sshint[] = {"sentidata", "C:/Programering/Java_Programmer/P6/assets/TextFiles/", "explain"};
        SentiStrength sentiStrength = new SentiStrength();

        sentiStrength.initialise(sshint);


        File file = new File("C:/Users/madsf/Downloads/tweetsWithTopicMetaFile/topicSenti.txt");    //creates a new file instance
        Scanner sc = new Scanner(file);
        int i = 0;
        double p = 0;
        int n = 4935668;
        DecimalFormat df2 = new DecimalFormat("#.#");

        while (sc.hasNextLine()) {
            if (i % (n / 1000) == 0) {
                System.out.print("\r " + df2.format(p) + "%");
                p = p + 0.1;
                if (p >= 99.9)
                    System.out.print("\r " + 100 + "%");
            }
            i++;
            String line = sc.nextLine();
            String savedLine = line;
            String regex = "\\Q|";
            String str[] = line.split(regex);

            String test = sentiStrength.computeSentimentScores(str[0]);
            String stri[] = test.split("=", 3);
            try {
                int result = Integer.parseInt(stri[1].substring(1, 3).trim());
                if (result == 1)
                    opinion = "positive";
                else if (result == -1)
                    opinion = "negative";
                else
                    opinion = "neutral";
            } catch (NumberFormatException e) {
                opinion = "neutral";
            } catch (IndexOutOfBoundsException e) {
                opinion = "neutral";
            }
            File sentimenttext = new File("C:/Users/madsf/Downloads/tweetsWithTopicMetaFile/tweetsWithTopicAndSentimentMetaFile.txt");
            FileWriter fr = new FileWriter(sentimenttext, true);
            BufferedWriter writer = new BufferedWriter(fr);

            writer.write(savedLine + "|" + opinion + "\n");
            writer.close();
            //   System.out.println(savedLine+ "|" + opinion);
            // System.out.println(str[0] + " | " + opinion);
        }


    }
}
