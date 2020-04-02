package TopicModelling;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;

public class TweetLoader {
    private ArrayList<TopicModelTweet> tweets = new ArrayList<>();

    public ArrayList<TopicModelTweet> getTweetsFromFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while((line = br.readLine())!=null){
            String[] lines = line.split("\\|");
            try{
                String cleaned_tweet = cleanTweet(lines[1]);
                if(cleaned_tweet.isEmpty() || cleaned_tweet.isBlank() || cleaned_tweet.split("\\s+").length == 1){
                    continue;
                }
                tweets.add(new TopicModelTweet(lines[0],cleaned_tweet,line));
            }catch (IndexOutOfBoundsException e){
                System.out.println(e + " :: " + line);
            }
            if(tweets.size() > 500000){
                break;
            }
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
        text = removeApostropheWords(text);
        text = removeStopWords(text);
        text = removeDigitsAndSymbols(text);
        text = removeSingleCharacters(text);
        text = removeDuplicateWords(text);
        return text.trim().replaceAll(" +", " ").toLowerCase();
    }

    private String removeDuplicateWords(String text) {
        LinkedHashSet<String> words = new LinkedHashSet<String>(Arrays.asList(text.split("\\s+")));
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(String s : words){
            if(index > 0)
                sb.append(" ");
            sb.append(s);
            index++;
        }
        return sb.toString();
    }

    private String removeSingleCharacters(String text) {
        text = text.replaceAll("\\b[a-zA-Z]\\b","");
        return text;
    }

    private String removeDigitsAndSymbols(String text) {
        text = text.replaceAll("[^a-zA-Z\\s]","");
        text = text.replaceAll("(?:^|\\W)(th|nd|st)(?:$|\\W)","");
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
