package schoolprojectnov2010.model

import net.liftweb.actor._
import dispatch._
import json._
import JsHttp._
import oauth.{Consumer, Token}
import twitter._

object TwitterCredentials {
        val consumerKey = "Y3Qr7NRIl9J8bbwBAJrsdw" // OAuth application key, top-secret
        val consumerSecret = "SAqnlO6Fj8VDI6MBAUcjQ8L2kXDloMAG3iDmgwnOs"
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

        def messageHandler = { // Lift Actors message handler
                case AuthURL =>
                        try {
                                val tok = http(Auth.request_token(TwitterCredentials.consumer, // request token for the application, as opposed to the user consider using Future this could throw a 503
                                        "http://localhost:8080"))
                                request_token = Some(tok)
                                println("This it the request token ::: here  " + request_token)
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

                                val statuses: List[dispatch.json.JsObject] = http(Status(screenName).timeline)
                                var twt: List[Any] = List()
                                val profile_image_url = 'profile_image_url ? str
                                statuses foreach {
                                        js =>
                                                val Status.user.screen_name(screen_name) = js
                                                val Status.text(text) = js
                                                val twt1 = <p>
                                                        {"%-15s%s" format (screen_name, Status.rebracket(text))}
                                                </p>
                                                twt = twt ++ twt1
                                                twt
                                }
                                reply(twt)
                        } catch {

                                case _ => reply(Nil)
                        }

                case Mentions(screenName) =>

                        val req = Twitter.host

                case _ => println("unkown message")

        }

}
