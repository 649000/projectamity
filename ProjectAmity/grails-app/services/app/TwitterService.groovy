package app

//import winterwell.jtwitter.Twitter
import twitter4j.*



class TwitterService {

    static transactional = true

    def serviceMethod() {

    }

    def String updateStatus(String s) {

//        String toReturn = "";
//        println("String Length: " + s.length())
//        if(s.length() < 140 || s.length() ==140)
//        {
//            try {
//
//                //                Twitter twitter = new Twitter("projectamity","rainchang");
//                //                twitter.updateStatus(s)
//                Twitter twitter = new TwitterFactory().getInstance("projectamity","rainchang");
//                generateRequestToken(twitter)
//                twitter.updateStatus(s);
//                toReturn = "Success"
//            }
//            catch (Exception e)
//            {
//                println(e)
//            }
//        }
//        else
//        {
//            //http://www.twitlonger.com/api not available
//            toReturn = "String is more than 140 characters. Unable to post"
//        }
//        return toReturn
    }
    def generateRequestToken(twitter,callbackUrl) {
        def consumerKey = ConfigurationHolder.config.twitter.oauth.consumer_key
        def consumerSecret = ConfigurationHolder.config.twitter.oauth.consumer_secret
        twitter.setOAuthConsumer(consumerKey, consumerSecret)
        def requestToken = twitter.getOAuthRequestToken(callbackUrl)
        return requestToken
    }
}