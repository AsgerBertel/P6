package TopicModelling;

import java.io.*;
import java.util.ArrayList;

public class TweetLoader {

    public ArrayList<TopicModelTweet> getTweetsFromFile(String path) throws IOException {
        ArrayList<TopicModelTweet> tweets = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while((line = br.readLine())!=null){
            String[] lines = line.split("\\|");
            try{
                tweets.add(new TopicModelTweet(lines[0],cleanTweet(lines[1]), null));
            }catch (IndexOutOfBoundsException e){
                System.out.println(e + " :: " + line);
            }
            if(tweets.size() > 100){
                break;
            }
        }
        return tweets;
    }
    private String cleanTweet(String text){
        //remove urls
        if(text.contains("https")) {
            text = text.replaceAll("https:\\S*"," ");
        }
        //remove Retweet letters RT + @UsrName
        if(text.contains("RT @")){
            text = text.replaceAll("RT @\\S*", "");
        }
        return text;
    }
}
