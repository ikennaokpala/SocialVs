package schoolprojectnov2010.model

import net.liftweb.actor._
import dispatch._
import json._
import json.JsHttp._
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

case object AuthURL
case class OAuthResponse(verifier: String)
case class Tweets(screenName: String)
case class Mentions(screenName: String)
case class TwitterUserInfoStream(id: BigDecimal, name: String, screenName: String,
                                 description: String, text: String, statuses_count: BigDecimal,
                                 friends_count: BigDecimal, followers_count: BigDecimal,
                                 listed_count: BigDecimal, favourites_count: BigDecimal,
                                 url: String, location: String, profile_image_url: String)

class TwitterActor extends LiftActor {
    val http = new Http
    val topic = ""
    val topicString= "search.json?key=n6aahpgj7geqespvdvsbuk7u&topic="+topic
    val req = :/("api.klout.com")/"1"/"topics"/topicString
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

                val twt = {

                    val twt1 = for{
                        twtJsonList <- http(Status(screenName).timeline)
                        kjson <- http(req ># {'users ! list}) map {'score ! obj}
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

                    } yield new TwitterUserInfoStream(id, name, screenName,
                            description, text, statuses_count, friends_count,
                            followers_count, listed_count, favourites_count,
                            url, location, profile_image_url)

                    twt1


                }
                //        println(twt.asInstanceOf[List[TwitterUserInfoStream]])
                reply(twt)
            } catch {

                case _ => reply(Nil)
            }

        case Mentions(screenName) =>

            val req = Twitter.host

        case _ => println("unkown message")

    }

}




