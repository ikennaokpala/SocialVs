package schoolprojectnov2010.model

import net.liftweb.actor._
import dispatch._
import json._
//import JsonHttp._
import oauth.{Consumer, Token}
import twitter._
import net.liftweb.http.S

trait ExtUserProps extends UserProps with Js {
    val friends_count = 'friends_count ! num
    val profile_image_url = 'profile_image_url ! str
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

                /*val statuses: List[dispatch.json.JsObject] = http(Status(screenName).timeline)
                var twt: List[Any] = List()

                statuses foreach {
                    js =>
                        val Status.user.screen_name(screen_name) = js
                        val Status.text(text) = js
                        val Status.user.followers_count(followers_count) = js
                        val profileURLExtractor = new Obj('user) with MyUserProps
                        //	val MyUserProps.profile_image_url(profile_image_url) = js
                        //	println(profile_image_url)
                        val twt1 = <p>
                            {"%-15s%-15s%s" format (screen_name, followers_count, Status.rebracket(text))}
                        </p>

                        twt = twt ++ twt1
                        twt
                }*/
                val twt = for{
                    item <- http(Status(screenName).timeline)
                    Status.user(user) = item
                    //                    ExtUser.screen_name(screen_name) = item
                    friend_count = ExtUser.friends_count(user)
                    profile_image_url = ExtUser.profile_image_url(user)
                } yield <img src={profile_image_url}/>
                //                yield <p>{"%-15s%-15s%s" format (screen_name, msg, friend_count)}</p>

                println(twt)


                reply(twt)
            } catch {

                case _ => reply(Nil)
            }

        case Mentions(screenName) =>

            val req = Twitter.host

        case _ => println("unkown message")

    }

}




