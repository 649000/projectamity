package app

import winterwell.jtwitter.Twitter



class TwitterService {

    static transactional = true

    def serviceMethod() {

    }

    def String updateStatus(String s) {

        String toReturn = "";
        // TwitterFactory twit = new TwitterFactory()
        // def twitter = twit.getInstance("projectamity", "rainchang")
        println("String Length: " + s.length())
        if(s.length() < 140 || s.length() ==140)
        {
            try {

                Twitter twitter = new Twitter("projectamity","rainchang");
                twitter.updateStatus(s)
                toReturn = "Success"
            }
            catch (Exception e)
            {
                println(e)
            }
        }
        else
        {
            //http://www.twitlonger.com/api not available
            toReturn = "String is more than 140 characters. Unable to post"
        }
        return toReturn
    }
}