package TopicModelling;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class TweetLoader {
    private ArrayList<TopicModelTweet> tweets = new ArrayList<>();

    public ArrayList<TopicModelTweet> getTweetsFromFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while((line = br.readLine())!=null){
            String[] lines = line.split("\\|");
            try{
                tweets.add(new TopicModelTweet(lines[0],cleanTweet(lines[1]),line));
            }catch (IndexOutOfBoundsException e){
                System.out.println(e + " :: " + line);
            }
            /*if(tweets.size() > 10000){
                break;
            }*/
        }
        return tweets;
    }
    public ArrayList<TopicModelTweet> getTweets(){
        return tweets;
    }
    private String cleanTweet(String text){
        //remove urls
        if(text.contains("https"))
            text = text.replaceAll("https:\\S*"," ");
        //remove Retweet letters RT + @UsrName
        if(text.contains("RT @"))
            text = text.replaceAll("RT @\\S*", "");
        //remove symbols
        if(text.contains("&amp") || text.contains("&gt"))
            text=text.replaceAll("&\\S*","");
        if(text.contains("."))
            text=text.replaceAll("\\.","");
        //remove useless words
        text = removeApostropheWords(text);
        text = removeStopWords(text);
        text = removeSymbols(text);
        return text.trim();
    }

    private String removeSymbols(String text) {
        Matcher matcher = SymbolAndWordRemovePatterns.symbolsPattern.matcher(text);
        text = matcher.replaceAll(" ");
        return text;
    }

    private String removeApostropheWords(String text) {
        Matcher matcher = SymbolAndWordRemovePatterns.apostropheWordPattern.matcher(text);
        text = matcher.replaceAll("");
        return text;
    }

    private String removeStopWords(String text){
        Matcher matcher = SymbolAndWordRemovePatterns.stopWordPattern.matcher(text);
        text = matcher.replaceAll("");
        return text;
    }
}
