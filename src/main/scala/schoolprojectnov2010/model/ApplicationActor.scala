package schoolprojectnov2010.model

import net.liftweb.actor._
import dispatch._
import json._
import JsHttp._
import oauth.{Consumer, Token}
import twitter._
import net.liftweb.http.S


trait ExtUserProps extends UserProps with Js {
  val id = 'id ! num
  val listed_count = 'listed_count ! num
  val favourites_count = 'favourites_count ! num
  val url = 'url ! str
  val friends_count = 'friends_count ! num
  val profile_image_url = 'profile_image_url ! str
  val name = 'name ! str
  val description = 'description ! str
  val location = 'location ! str
  val statuses_count = 'statuses_count ! num
}
object ExtUser extends ExtUserProps with Js

object TwitterCredentials {
  lazy val consumerKey = "ZbjvoQtqD56WzVGsJYYqzw" // OAuth application key, top-secret
  lazy val consumerSecret = "plgoNIGZOic9keO4BEHZ3aHRdRC6N0JJMGJPOpg"
  lazy val consumer = Consumer(consumerKey, consumerSecret)
}

object KloutCredentials {
  lazy val key = "n6aahpgj7geqespvdvsbuk7u" // Klout Application key
  lazy val req = :/("api.klout.com") / "1"
}
object TwitterCounterCredentials{
  lazy val key = "77e1b81ea83c1c5d0c5d894fd3d5d66e"
  lazy val req = :/("api.twittercounter.com")
}

case object AuthURL
case class OAuthResponse(verifier: String)
case class Tweets(screenName: String)
case class InfluencersSearch(searchInput: String)
case class KloutUser(user_name: String, score: BigDecimal, picture: String, topic: String)
case class TwitterUserVO(id: BigDecimal, name: String, screenName: String,
                         description: String, text: String, statuses_count: BigDecimal,
                         friends_count: BigDecimal, followers_count: BigDecimal,
                         listed_count: BigDecimal, favourites_count: BigDecimal,
                         location: String, profile_image_url: String,
                         score: BigDecimal, true_reach: BigDecimal, kclass: String,
                        klout_description: String, amplification_score: BigDecimal, network_score: BigDecimal,
                         influencedBy: List[(String, BigDecimal)])

class ApplicationActor extends LiftActor {
  val http = new Http
  var request_token: Option[Token] = None
  var access_token: Option[Token] = None
  val httpHost = S.hostAndPath

  def messageHandler = { // Lift Actors message handler
    case AuthURL =>
      try {
        val tok = http(Auth.request_token(TwitterCredentials.consumer, httpHost)) // request token for the application, as opposed to the user consider using Future this could throw a 503

        request_token = Some(tok)
        val url = Auth.authenticate_url(tok).to_uri.toString // generate the url the user needs to go to, to grant us access authorize_url(tok).to_uri
        reply(Some(url))

      } catch {
        case ex => reply((None, None))
      }

    case OAuthResponse(vrfr) =>

      val accessToken = http(Auth.access_token(TwitterCredentials.consumer, // this  could throw a NoSuchElementException
        request_token.get, vrfr))

      reply(Some(accessToken._3))

    case Tweets(screenName) =>

      try {
        lazy val reqScore = KloutCredentials.req / "users" / "show.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName)
        lazy val reqInfluencedBy = KloutCredentials.req / "soi" / "influenced_by.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName)
        lazy val reqInfluencerOf = KloutCredentials.req / "soi" / "influenced_of.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName)
        val twt = {
          val kjson = http(reqScore ># {'users ! list}) map {'score ! obj}
          val score = ('kscore ! num)(kjson(0))
          val true_reach = ('true_reach ! num)(kjson(0))
          val amplification_score = ('amplification_score ! num)(kjson(0))
          val network_score = ('network_score ! num)(kjson(0))
          val kclass = ('kclass ! str)(kjson(0))
          val klout_description = ('description ! str)(kjson(0))
           lazy val influencedBy = {
             try{
             val influencer = for{
              influencerList <- http(reqInfluencedBy ># {'users ! list}) flatten {'influencers ! list}
              screen_name = ('twitter_screen_name ! str)(influencerList)
              kscore = ('kscore ! num)(influencerList)
            } yield (screen_name, kscore)
           //  println("I am looking for you "+influencer)
            influencer
             }catch{
               case _ => List()
             }
          }

          /*val influencerOf  = {
            val influencer = for{
              influencerList <- http(reqInfluencerOf ># {'users ! list}) flatten {'influencers ! list}
              screen_name = ('influencer ! str)(influencerList)
              kscore = ('kscore ! num)(influencerList)
            } yield (screen_name, kscore) 
            influencer
          }*/

          val twt1 = for{

            twtJsonList <- http(Status(screenName).timeline)
            Status.user.screen_name(screen_name) = twtJsonList
            Status.text(text) = twtJsonList
            Status.user.followers_count(followers_count) = twtJsonList
            Status.user(user) = twtJsonList
            id = ExtUser.id(user)
            statuses_count = ExtUser.statuses_count(user)
            friends_count = ExtUser.friends_count(user)
            listed_count = ExtUser.listed_count(user)
            favourites_count = ExtUser.favourites_count(user)
            profile_image_url = ExtUser.profile_image_url(user)
            name = ExtUser.name(user)
            location = ExtUser.location(user)
            description = ExtUser.description(user)



          } yield TwitterUserVO(id, name, screenName,
              description, text, statuses_count, friends_count,
              followers_count, listed_count, favourites_count,
              location, profile_image_url, score, true_reach, kclass, klout_description,
              amplification_score, network_score, influencedBy)
          println("this is my id: "+twt1(0).id)
          twt1

        }
        reply(twt)
      } catch {

        case ex => println(ex); reply((None, screenName))
      }

    case InfluencersSearch(searchInput) =>

      try {

        val searchresult = {
          val req = KloutCredentials.req / "topics" / "search.json" <<? Map("key" -> KloutCredentials.key, "topic" -> searchInput)
          val kuser = for{
            json <- http(req ># {
              'users ! list

            })
            user_name = ('user_name ! str)(json)
            score = ('skore ! num)(json)
            picture = ('pic_url ! str)(json)
          } yield KloutUser(user_name, score, picture, searchInput)
          kuser
        }

        reply(searchresult)
      } catch {
        case ex => reply((None, None))
      }

    case _ => println("unkown message")

  }

}


//                 println("THE TWITTER INFORMATION FOR "+screenName+"  FROM APPLICATION IS : "+twt1)
    

