package TweetCleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;

public class JsonCleaner {
    String line;

    public ArrayList<Object> cleanTweets(String path) {
        ArrayList usableTweets = new ArrayList();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            int i = 0;
            JSONObject json = null;

            while((this.line = bufferedReader.readLine()) != null) {
                try {
                    json = new JSONObject(this.line);
                } catch (Exception var7) {
                    System.out.println(var7);
                }

                if (!line.contains("lang\":\"en")){
                    continue;
                }
                if(line.contains("extended_tweet")) {
                    this.handleRetweet(json);
                }
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return usableTweets;
    }

    public void handleRetweet(JSONObject json) {
        try {
            String text = (String)json.getJSONObject("retweeted_status").getJSONObject("extended_tweet").get("full_text");
            System.out.println(text);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void parseTwitterObject(JSONObject tweet) {
    }
}
