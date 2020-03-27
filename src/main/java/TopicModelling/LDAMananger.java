package TopicModelling;

import com.github.chen0040.lda.Lda;
import com.github.chen0040.lda.LdaResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LDAMananger {
    ArrayList<String> tweets = new ArrayList<>();
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
            tweets.add(t.modified_text);
        }
    }


}
