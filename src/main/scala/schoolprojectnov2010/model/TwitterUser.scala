package schoolprojectnov2010.model

class TwitterUser extends Tweeter {

  val twitterActor = new TwitterActor
  var authorized: Boolean = false

  def twitterAuthURL = 
    twitterActor !? AuthURL 

  def twitterVerifyAuth(verifier: String) = 
    twitterActor !? OAuthResponse(verifier)

  def tweetsForName(screenName: String) =
    twitterActor !? Tweets(screenName)
}
