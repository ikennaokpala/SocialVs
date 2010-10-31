package schoolprojectnov2010.model

class TwitterUser extends Tweeter {
    val twitterActor = new ApplicationActor
    var authorized: Boolean = false

    def twitterAuthURL =
        twitterActor !? AuthURL

    def twitterVerifyAuth(verifier: String) =
        twitterActor !? OAuthResponse(verifier)

    def tweetsForName(screenName: String) =
        twitterActor !? TwitteruserInfo(screenName)

    def topicInfluencersSearch(search: String) =
        twitterActor !? InfluencersSearch(search)
}
