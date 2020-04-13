package Sql;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class TopicInsert {

    public static void main(String[] args) throws IOException {
        HashSet<String> usedStrings = new HashSet<>();
        File f = new File("C:/Users/Mads/Desktop/CleanedData/TopicMetaFile.txt");
        LinkedHashMap<Integer, String[]> file_topics = new LinkedHashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        //populate topics map
        while((line = br.readLine())!= null){
            String[] splitLine = line.split("\\|");
            file_topics.put(Integer.valueOf(splitLine[0]),splitLine[1].split(","));
        }
        //Select topic identifiers
        LinkedHashMap<Integer, String> toptopics = new LinkedHashMap<>();
        for(int i : file_topics.keySet()){
            for(String s : file_topics.get(i)){
                if(!usedStrings.contains(s.trim())){
                    toptopics.put(i,s.trim());
                    usedStrings.add(s.trim());
                    break;
                }
            }
        }
        //Insert all toptopics
        for(int i : toptopics.keySet()){
            ConnectionManager.updateSql(QueryManager.insertIntoTopTopics(i,toptopics.get(i)));
        }
        //Insert all subtopics
        for(int i : file_topics.keySet()){
            for(String s : file_topics.get(i)){
                ConnectionManager.updateSql(QueryManager.insertIntoSubTopics(s.trim(),i));
            }
        }
    }
}
