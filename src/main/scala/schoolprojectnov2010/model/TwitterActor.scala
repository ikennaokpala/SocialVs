package schoolprojectnov2010.model

import net.liftweb.actor._

import dispatch._
import oauth.{Consumer, Token}
import twitter._
import net.liftweb.http.S
import net.liftweb.http.provider.HTTPCookie

object TwitterCredentials {

  // OAuth application key, top-secret                                         
  val consumerKey = "Y3Qr7NRIl9J8bbwBAJrsdw"
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

  def messageHandler = {

    case AuthURL =>

      try {

        // request token for the application, as opposed to the user
        // consider using Future
        // this could throw a 503
        val tok = http(Auth.request_token(TwitterCredentials.consumer,
          "http://localhost:8080"))
        request_token = Some(tok)
        println("This it the request token ::: here  " + request_token)

        // generate the url the user needs to go to, to grant us access
        //authorize_url(tok).to_uri
        val url = Auth.authenticate_url(tok).to_uri.toString

        reply(Some(url))

      } catch {

        case ex => reply((None, None))
      }

    case OAuthResponse(vrfr) =>

      println("THIS HAS BEEN CALLED B4 ENTERING TO THE TRY BLOCK")
      try {
        val accessToken = http(Auth.access_token(TwitterCredentials.consumer,
          request_token.get, vrfr))
        println("This it the acces token ::: here  " + accessToken)

//        S.addCookie(HTTPCookie("display","true"))

        reply(Some(accessToken._3))
      } catch {
        case ex => reply((None, None))
      }

    case Tweets(screenName) =>

      try {

        val twts = http(Status(screenName).timeline)
        reply(twts flatMap (_ toString))

      } catch {

        case _ => reply(Nil)
      }

    case Mentions(screenName) =>

      val req = Twitter.host

    case _ => println("unkown message")

  }

}
