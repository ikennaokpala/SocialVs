package schoolprojectnov2010.model

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Sep 8, 2010
 * Time: 1:21:51 PM
 * To change this template use File | Settings | File Templates.
 */


import net.liftweb.actor._
import dispatch._
import json._
import JsHttp._
import oauth.{Consumer, Token}
import twitter._
import net.liftweb.http.S


trait ExtUserProps extends UserProps with Js {   // ExtUserProps extending  UserProps and  Js ofrom the databinder dispatch library
  val id = 'id ! num // SymOp extractor for twitter user id `
  val listed_count = 'listed_count ! num // SymOp extractor for twitter user list count
  val favourites_count = 'favourites_count ! num // SymOp extractor for twitter user favourites count
  val url = 'url ! str // SymOp extractor for twitter user url ( notes this may throw a runtime extractor  exception)
  val friends_count = 'friends_count ! num   // SymOp extractor for twitter user friends count
  val profile_image_url = 'profile_image_url ! str  // SymOp extractor for twitter user image url
  val name = 'name ! str // SymOp extractor for twitter user screen name
  val description = 'description ! str // SymOp extractor for twitter user bio description
  val location = 'location ! str // SymOp extractor for twitter user location
  val statuses_count = 'statuses_count ! num  // SymOp extractor for twitter user statuses count
}
object ExtUser extends ExtUserProps with Js  //  ExtUser inherits SymOp extractor for twitter user from ExtUserProps and Js

object TwitterCredentials {  // twitter api credentials singleton object
  lazy val consumerKey = "ZbjvoQtqD56WzVGsJYYqzw" // OAuth application consumer key, top-secret lazily evaluated
  lazy val consumerSecret = "plgoNIGZOic9keO4BEHZ3aHRdRC6N0JJMGJPOpg" // OAuth application consumer Secret lazily evaluated
  lazy val consumer = Consumer(consumerKey, consumerSecret)  // composing Oauth  application consumer key, top-secret lazily evaluated
}

object KloutCredentials { // klout api credentials singleton object
  lazy val key = "n6aahpgj7geqespvdvsbuk7u" // Klout Application key
  lazy val req = :/("api.klout.com") / "1"   // Klout request uri
}
object TwitterCounterCredentials { // TwitterCounter api credentials singleton object
  lazy val key = "77e1b81ea83c1c5d0c5d894fd3d5d66e"
  lazy val req = :/("api.twittercounter.com")
}

case object AuthURL  // case object to obtain OAuth-thentication URL
case class OAuthResponse(verifier: String) // OAuth-thentication  Responce case class
case class TwitteruserInfo(screenName: String) // twitter users information
case class InfluencersSearch(searchInput: String) // topic or key word search case class
case class KloutUserVO(user_name: String, score: BigDecimal, picture: String, topic: String) // Klout User Data Transfer Object  
case class TwitterUserVO(id: BigDecimal, name: String, screenName: String,
                         description: String, text: String, statuses_count: BigDecimal,
                         friends_count: BigDecimal, followers_count: BigDecimal,
                         listed_count: BigDecimal, favourites_count: BigDecimal,
                         location: String, profile_image_url: String,
                         score: BigDecimal, true_reach: BigDecimal, kclass: String,
                         klout_description: String, amplification_score: BigDecimal, network_score: BigDecimal,
                         influencedBy: List[(String, BigDecimal)], percentile: BigDecimal) // Twitter and  Klout user Data Transfer Object

class ApplicationActor extends LiftActor { // Aplication actor inherit capabilities from Lift'sActor library
  private lazy val http = new Http   //  databinder dispatch Http instance  lazily evaluated
  private var request_token: Option[Token] = None // request token Option type
  private var access_token: Option[Token] = None  // access token Option type
  private val httpHost = S.hostAndPath // this get the current Host url 

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

    case TwitteruserInfo(screenName) =>

      try {
        lazy val reqScore = KloutCredentials.req / "users" / "show.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName) // Klout User request end point Url
        lazy val reqInfluencedBy = KloutCredentials.req / "soi" / "influenced_by.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName)  // Klout User influencedBy end point Url
        lazy val reqInfluencerOf = KloutCredentials.req / "soi" / "influenced_of.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName) // Klout User influencedOf end point Url
        lazy val reqTwitterUserId = User(screenName).show  // Twitter user  end point Url and Json parser handler 
        val twitterUserDTO = {    // twitter data transfer object
          val kjson = http(reqScore ># {'users ! list}) map {'score ! obj}  // handling the reqScore as JSON,  taking what i want
          val score = ('kscore ! num)(kjson(0)) // Extracting Klout score with the SymOp kscore
          val true_reach = ('true_reach ! num)(kjson(0)) // Extracting truereach with the SymOp true_reach
          val amplification_score = ('amplification_score ! num)(kjson(0)) // Extracting amplification score with the SymOp amplification_score
          val network_score = ('network_score ! num)(kjson(0))  // Extracting network score with the SymOp network_score
          val kclass = ('kclass ! str)(kjson(0)) // Extracting klout class  with the SymOp kclass
          val klout_description = ('description ! str)(kjson(0)) // Extracting klout class description with the SymOp description
          val percentile = ('percentile ! num)(kjson(0)) // Extracting klout user percentile  with the SymOp percentile
        /*  val twitterUser = http(reqTwitterUserId)  // handling the reqTwitterUserId as JSON,  taking what i want
          val twitterUserId = ('id ! num)(twitterUserObj) // Extracting twitterUserId  with the SymOp kscore
          lazy val reqTc = TwitterCounterCredentials.req <<? Map("apikey" -> TwitterCounterCredentials.key, "twitter_id" -> twitterUserId)// Twitter Counter User request end point Url
          val followersPerDate = http(reqTc ># {'followersperdate ! obj}) // handling the reqTc as JSON,  taking followersperdate as an obj
          val g = followersPerDate*/
          lazy val influencedBy = {  //  influencedBy variable  lazily evaluated
            try {     // exception
              val influencer = for{ // for conmprehension
                influencerList <- http(reqInfluencedBy ># {'users ! list}) flatten {'influencers ! list}  // handling the reqTwitterUserId as JSON,  taking what i want
                screen_name = ('twitter_screen_name ! str)(influencerList) // Extracting screen_name  with the SymOp twitter_screen_name
                kscore = ('kscore ! num)(influencerList) // Extracting screen_name  with the SymOp kscore
              } yield (screen_name, kscore) // yield creates a new list of tuple2 string and BigDecimal
              //  println("I am looking for you "+influencer)
              influencer
            } catch { // exception trap
              case _ => List() // return an empty list
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

          val twitterUserObj = for{// for comprehension
            twtJsonList <- http(Status(screenName).timeline)  // handling the Status reponse as JSON,  taking what i want
            Status.user.screen_name(screen_name) = twtJsonList  // handling the Status reponse as JSON,  taking what i want
            Status.text(text) = twtJsonList      // handling the Status reponse as JSON,  taking what i want
            Status.user.followers_count(followers_count) = twtJsonList  // handling the Status reponse as JSON,  taking what i want
            Status.user(user) = twtJsonList     // handling the Status reponse as JSON,  taking what i want
            id = ExtUser.id(user)        // handling the Status reponse as JSON,  taking what i want
            statuses_count = ExtUser.statuses_count(user)    // handling the Status reponse as JSON,  taking what i want
            friends_count = ExtUser.friends_count(user) // handling the Status reponse as JSON,  taking what i want
            listed_count = ExtUser.listed_count(user)   // handling the Status reponse as JSON,  taking what i want
            favourites_count = ExtUser.favourites_count(user)  // handling the Status reponse as JSON,  taking what i want
            profile_image_url = ExtUser.profile_image_url(user)  // handling the Status reponse as JSON,  taking what i want
            name = ExtUser.name(user)         // handling the Status reponse as JSON,  taking what i want
            location = ExtUser.location(user)   // handling the Status reponse as JSON,  taking what i want
            description = ExtUser.description(user)  // handling the Status reponse as JSON,  taking what i want
          } yield TwitterUserVO(id, name, screenName,
              description, text, statuses_count, friends_count,
              followers_count, listed_count, favourites_count,
              location, profile_image_url, score, true_reach, kclass, klout_description,
              amplification_score, network_score, influencedBy, percentile)
          println("Current user\'s id: " + twitterUserObj(0).id)  // yield creates a new list of TwitterUserVO
          twitterUserObj

        }
        reply(twitterUserDTO) // replying back to caller the twitterUserDTO
      } catch {

        case ex => println(ex); reply((None, screenName))  // replying back to caller tuple2 of Option and String
      }

    case InfluencersSearch(searchInput) =>

      try {

        val searchresult = {   //  searchresult variable 
          val req = KloutCredentials.req / "topics" / "search.json" <<? Map("key" -> KloutCredentials.key, "topic" -> searchInput) // Klout Topic request end point Url
          val kuser = for{   // for comprehension
            json <- http(req ># {
              'users ! list

            })   // processing topic request
            user_name = ('user_name ! str)(json)  // Extracting twitter user name via  Klout with the SymOp user_name
            score = ('skore ! num)(json) // Extracting Klout user score with the SymOp skore
            picture = ('pic_url ! str)(json) // Extracting twitter image via  Klout with the SymOp pic_url
          } yield KloutUserVO(user_name, score, picture, searchInput) // yield creates a new list of KloutUserVO
          kuser
        }

        reply(searchresult)  // replying back to caller the searchresult
      } catch {
        case ex => reply((None, None)) // replying back to caller tuple2 of Option and Option
      }

    case _ => println("unkown message")  // printing to console some unknown message

  }

}


//                 println("THE TWITTER INFORMATION FOR "+screenName+"  FROM APPLICATION IS : "+twt1)
    

