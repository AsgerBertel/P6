package TweetCleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DataGeneration.DataGeneration;
import org.json.JSONObject;

public class JsonCleaner {
    DataGeneration datagenerator = new DataGeneration();
    String line;
    double p =0;
    DecimalFormat df2 = new DecimalFormat("#.#");
    public ArrayList<JsonTweet> cleanAllFilesInDirectory(String pathToDirectory){
        ArrayList<JsonTweet> usableTweets = new ArrayList<>();
        File folder = new File(pathToDirectory);
        List<File> files = Arrays.asList(folder.listFiles());
        int i=0;
        for (File f : files) {
            if(i % (files.size()/1000) == 0){
                System.out.print("\r " + df2.format(p) + "%");
                p = p + 0.1;
                if (p >= 99.9)
                    System.out.print("\r " + 100 + "%");
            }
            i++;
            usableTweets.addAll(cleanTweets(f.getPath()));
        }
        return usableTweets;
    }
    private ArrayList<JsonTweet> cleanTweets(String path) {
        ArrayList<JsonTweet> usableTweets = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            JSONObject json = null;

            while ((this.line = bufferedReader.readLine()) != null) {
                String text = "";
                try {
                    json = new JSONObject(this.line);
                    if(!(json.get("lang").equals("en")))
                        continue;
                    if (line.contains("extended_tweet")) {
                        if (handleRetweet(json)) {
                            if(!(json.getJSONObject("retweeted_status").get("lang").equals("en")))
                                continue;
                            text = (String) json.getJSONObject("retweeted_status").getJSONObject("extended_tweet").get("full_text");
                        } else {
                            continue;
                        }
                    } else {
                        text = (String) json.get("text");
                    }
                    double[] latlong = genLatLong();
                    JsonTweet jsonTweet = new JsonTweet(
                            String.valueOf(json.get("id")),
                            text,
                            (String) json.get("created_at"),
                            latlong[0], latlong[1],
                            null
                    );
                    usableTweets.add(jsonTweet);
                } catch (Exception e) {
                    //System.out.println(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usableTweets;
    }

    public double[] genLatLong() {
        double[] longLatArray = new double[]{DataGeneration.generateLat(20, 100), DataGeneration.generateLong(20, 100)};
        // TODO: 06/03/2020  Make it take bundary for future use
        return longLatArray;
    }

    public boolean handleRetweet(JSONObject json) {
        try {
            String text = (String) json.getJSONObject("retweeted_status").getJSONObject("extended_tweet").get("full_text");
            return true;
        } catch (Exception e) {
            //System.out.println(e);
        }
        return false;
    }

    public static void parseTwitterObject(JSONObject tweet) {
    }
}
