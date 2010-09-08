package schoolprojectnov2010.model

class Tweeter {

  var screenName: String = ""
  var fullName: String = ""
  var profileImageURL: String = ""

  var tweetString: String = "tweet"
  var followers: List[Tweeter] = Nil
  var mentions: List[Tweet] = Nil
  var retweetsOfMe: List[Tweet] = Nil

  def profileURL = "http://twitter.com/" + screenName

}
