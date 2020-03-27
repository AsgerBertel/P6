package TopicModelling;

import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.lda.Doc;
import com.github.chen0040.lda.LdaResult;

import java.util.*;

public class TopicAmountCalculator {
    LDAMananger ldaMananger = new LDAMananger();


    public void jaccardAverage(int topicAmount, HashMap<Integer, ArrayList<String>> topicMap){
        double jaccardIndexSum = 0, jaccardAverage = 0, d = 0;

        for(int topicIndex = 0; topicIndex < topicAmount-1; topicIndex++){
            List<String> topicGroup1, topicGroup2;
            topicGroup1 = topicMap.get(topicIndex);
            topicGroup2 = topicMap.get(topicIndex+1);
            d = (double) topicMap.get(topicIndex).size();
            jaccardIndexSum += jaccardIndex(topicGroup1, topicGroup2);
            jaccardAverage = (1/d)*jaccardIndexSum;
            System.out.println(jaccardAverage);

        }
        System.out.println(jaccardAverage);
    }

    private double jaccardIndex(List<String> set1, List<String> set2){
        double set1Size = set1.size();
        double set2Size = set2.size();

        //create new set to store intersection of set 1 and 2
        List<String> intersectSet = set1;
        intersectSet.retainAll(set2);

        int intersectSize = intersectSet.size();

        return intersectSize / (set1Size + set2Size - intersectSize);
    }
}
