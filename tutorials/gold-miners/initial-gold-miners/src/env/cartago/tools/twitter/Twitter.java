package cartago.tools.twitter;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import cartago.*;

/**
 * Naive Twitter artifact.
 *
 * To post a tweet:
 *
 * tweet(Msg)
 *
 * The observable property tw_user_status(S: String) stores the last tweet posted
 *
 *
 * To refresh the current time line:
 *
 * refreshCurrentTimeline()
 *
 * By refreshing the current time line, the current time line is made observable
 * in terms of observable properties:
 *
 * time_line_size(N: Integer) - size of the timeline
 * tl_tweets(Id: Long, WhoScreenName: String, WhoFullName: String, What: String) - a tweet in the time line, identified by Id, posted by Who, containing the What msg.
 */
public class Twitter extends Artifact {

    private TwitterFactory factory;
    private twitter4j.Twitter twitter;

    /**
     * Instantiate a twitter artifact configured with a consumer
     * key/secret and an access token/secret got for accessing
     * a Twitter account-
     */
    void  init(String consumerKey, String consumerSecret, String accessToken, String accessSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
             .setOAuthConsumerKey(consumerKey)
               .setOAuthConsumerSecret(consumerSecret)
               .setOAuthAccessToken(accessToken)
               .setOAuthAccessTokenSecret(accessSecret);

        factory = new TwitterFactory(cb.build());
        twitter = factory.getInstance();

        try {
            List<Status> statuses = twitter.getHomeTimeline();
            defineObsProperty("tw_user_status",statuses.get(0).getText());
        } catch (Exception ex){
            ex.printStackTrace();
            defineObsProperty("tw_user_status","");
        }
    }

    /**
     * To post a tweet.
     *
     * @param tweet
     */
    @OPERATION void tweet(String tweet) {
        try {
            Status stat = twitter.updateStatus(tweet);
            ObsProperty prop = getObsProperty("tw_user_status");
            prop.updateValue(stat.getText());
        } catch (Exception ex){
            ex.printStackTrace();
            failed("updateStatus failed.");
        }
    }

    /**
     * To refresh the time line.
     */
    @OPERATION void refreshCurrentTimeline() {
        try {
            List<Status> statuses = twitter.getHomeTimeline();
            ObsProperty prop = getObsProperty("time_line_size");
            if (prop != null){
                int nt = prop.intValue();
                for (int i = 0; i < nt; i++){
                    removeObsProperty("tl_tweets");
                }
                prop.updateValue(statuses.size());
            } else {
                defineObsProperty("time_line_size",statuses.size());
            }
            for (Status s: statuses){
                defineObsProperty("tl_tweets",s.getId(),s.getUser().getScreenName(), s.getUser().getName(),s.getText());
            }
        } catch (Exception ex){
            ex.printStackTrace();
            defineObsProperty("tw_user_status","");
        }
    }

    /**
     * Do a retweet
     *
     * @param tweetId the id of the tweet to be retweeted
     */
    @OPERATION void retweet(long tweetId){
        try {
            twitter.retweetStatus(tweetId);
        } catch (Exception ex){
            failed("retweet failed. "+ex.toString());
        }
    }

    /**
     * To post a tweet direct to a specific user
     *
     * @param targetUser
     * @param msg
     */
    @OPERATION void tweetTo(String targetUser, String msg){
        try {
            DirectMessage message = twitter.sendDirectMessage(targetUser, msg);
        } catch (Exception ex){
            failed("send direct msg to "+msg+" failed: "+ex.toString());
        }

    }

}

