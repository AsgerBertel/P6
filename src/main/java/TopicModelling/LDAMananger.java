package TopicModelling;

import com.github.chen0040.lda.Lda;
import com.github.chen0040.lda.LdaResult;

import java.util.ArrayList;

public class LDAMananger {
    ArrayList<String> tweets = new ArrayList<>();


    public LdaResult calculateTopics(int topics, int vocabSize){
        Lda lda = new Lda();
        lda.setTopicCount(topics);
        lda.setMaxVocabularySize(vocabSize);
        return lda.fit(tweets);
    }
    public void extractTweetText(ArrayList<TopicModelTweet> topicModelTweets){
        for(TopicModelTweet t : topicModelTweets){
            tweets.add(t.modified_text);
        }
    }
}
