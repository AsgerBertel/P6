package TopicModelling;

import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.lda.Doc;
import com.github.chen0040.lda.Lda;
import com.github.chen0040.lda.LdaResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LDAMananger {
    ArrayList<String> tweets = new ArrayList<>();
    int curr_doc=0;
    LdaResult tempLdaResult;

    public LdaResult calculateTopics(int topics, int vocabSize){
        Lda lda = new Lda();
        lda.setTopicCount(topics);
        lda.setMaxVocabularySize(vocabSize);
        tempLdaResult = lda.fit(tweets);
        return tempLdaResult;
    }
    public void extractTweetText(ArrayList<TopicModelTweet> topicModelTweets){
        for(TopicModelTweet t : topicModelTweets){
            tweets.add(t.topic_text);
        }
    }
    public void assignTopics(ArrayList<TopicModelTweet> topicModelTweets, LdaResult results, HashMap<Integer, ArrayList<String>> descriptors){
        //for all tweets, find the document in results and determine what topic(s) it was assigned to.
        //Given the number of that topic, take the top three descriptors and add to the list in TopicModelTweet.

        for(TopicModelTweet t : topicModelTweets){
            //find corresponding document
            Doc doc = getCorrespondingDocument(t,results.documents());
            if(doc == null)
                continue;
            //loop over top 3 topics assigned to tweet
            for(TupleTwo<Integer,Double> tuple : doc.topTopics(3)){
                int added = 0;
                //if probability that topic is correct is less than 50% then skip
                if(tuple._2() < 0.5 && !doc.topTopics(3).get(0)._1().equals(tuple._1())){
                    continue;
                }
                //add the topics that "should" describe the content of the tweet
                for(String s : descriptors.get(tuple._1())){
                    //only add top3
                    if(added < 3){
                        t.addTopic(s);
                        added++;
                    }else break;
                }
            }
        }
    }
    private Doc getCorrespondingDocument(TopicModelTweet tweet, List<Doc> docs){
        if(docs.get(curr_doc).getContent().equals(tweet.topic_text)){
            Doc d = docs.get(curr_doc);
            curr_doc++;
            return d;
        }
        System.out.println("Missing document for: " + tweet.topic_text + " ||| " + tweet.original_text);
        return null;
    }
}
