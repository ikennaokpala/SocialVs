package schoolprojectnov2010.model

class Tweet {

  var id: String = ""
  var text: String = ""
  var sourceName: String = ""
  var sourceURL: String = ""
  var tweeter: Tweeter = new Tweeter
//  var createdAt: 

  def tweetURL = tweeter.profileURL + "/status/" + id

  def hashTags:List[String] = 
    text.split(" ").toList filter (word => word.startsWith("#"))

  def embeddedUrls:List[String] = 
    text.split(" ").toList filter (word => word.startsWith("http://"))

}
