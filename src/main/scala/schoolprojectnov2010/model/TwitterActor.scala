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
    val consumerKey = "ZbjvoQtqD56WzVGsJYYqzw" // OAuth application key, top-secret
    val consumerSecret = "plgoNIGZOic9keO4BEHZ3aHRdRC6N0JJMGJPOpg"
    val consumer = Consumer(consumerKey, consumerSecret)
}

object KloutCredentials {
    val key = "n6aahpgj7geqespvdvsbuk7u" // Klout Application key
    val req = :/("api.klout.com") / "1"
}

case object AuthURL
case class OAuthResponse(verifier: String)
case class Tweets(screenName: String)
case class Mentions(screenName: String)
case class InfluencersSearch(searchInput: String)
case class KloutUser(user_name: String, score: BigDecimal, picture: String, topic: String)
case class TwitterUserVO(id: BigDecimal, name: String, screenName: String,
                         description: String, text: String, statuses_count: BigDecimal,
                         friends_count: BigDecimal, followers_count: BigDecimal,
                         listed_count: BigDecimal, favourites_count: BigDecimal,
                         url: String, location: String, profile_image_url: String,
                         score: BigDecimal, true_reach: BigDecimal,
                         amplification_score: BigDecimal, network_score: BigDecimal)

class TwitterActor extends LiftActor {
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
                val reqScore = KloutCredentials.req / "users" / "show.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName)
                val reqInfluencer = KloutCredentials.req / "soi" / "influenced_by.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName)
                val reqInfluencerOf = KloutCredentials.req / "soi" / "influenced_of.json" <<? Map("key" -> KloutCredentials.key, "users" -> screenName)
                val twt = {
                    val kjson = http(reqScore ># {'users ! list}) map {'score ! obj}
                    val score = ('kscore ! num)(kjson(0))
                    val true_reach = ('true_reach ! num)(kjson(0))
                    val amplification_score = ('amplification_score ! num)(kjson(0))
                    val network_score = ('network_score ! num)(kjson(0))
                    /*val influenced_by = {
                        for{

                        }

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
                        url = ExtUser.url(user)
                        profile_image_url = ExtUser.profile_image_url(user)
                        name = ExtUser.name(user)
                        location = ExtUser.location(user)
                        description = ExtUser.description(user)

                    } yield TwitterUserVO(id, name, screenName,
                            description, text, statuses_count, friends_count,
                            followers_count, listed_count, favourites_count,
                            url, location, profile_image_url, score, true_reach,
                            amplification_score, network_score)

                    twt1


                }
                //        println(twt.asInstanceOf[List[TwitterUserVO]])
                reply(twt)
            } catch {

                case _ => reply(Nil)
            }

        case Mentions(screenName) =>

            val req = Twitter.host

        case InfluencersSearch(searchInput) =>

            try {
                /* val req = KloutCredentials.req / "topics" / "verify.json" <<? Map("key" -> KloutCredentials.key, "topic" -> searchInput)
            val verified = http(req as_str)*/

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




