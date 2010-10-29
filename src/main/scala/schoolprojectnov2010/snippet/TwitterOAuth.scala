package schoolprojectnov2010.snippet

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Sep 8, 2010
 * Time: 12:14:01 PM
 * To change this template use File | Settings | File Templates.
 */


import net.liftweb.http._

class TwitterOAuth extends ApplicationUser {
  import scala.xml.NodeSeq   // library import into local class scope
  import SHtml._
  import js._
  import JsCmds.{Noop, RedirectTo}

  def render(xhtml: NodeSeq) = { //defualt page load method 
  user.authorized match {
      case true => userLoggedIn  // if user is already logged in, dont show OAuth button
      case false =>

        (for{ // for comprehension
          tkn <- S.param("oauth_token")   // retrieves the oauth token for the current authentication session
          vrfr <- S.param("oauth_verifier") // retrieves the oauth verifier for the current authentication session
        } yield useTokens(tkn, vrfr)) getOrElse <li class="btnimg">
          {a(() => twitterAuthURL,  // if user isnt logged in, create an anchor tag around a the twitter sign in imagge button which will do an AJAX call and invoke the OAuth URL request and Authentication function
              <img src="/classpath/images/twitter/twitter_button_3_lo.gif" alt="Twitter OAuth Button"/>)}
        </li>
    }
  }


  def twitterAuthURL: JsCmd = {  //  generate URL for Twitter Auth requests

    user.appAuthURL match {

      case (Some(url: String)) =>
        //        println("I HAVE BEEN CALLED: " + url)
        RedirectTo(url) //  generate javascript command redirect URL for Twitter Auth requests


      case _ =>

        Noop // return nothing or no url
    }
  }


  def useTokens(token: String, verifier: String): NodeSeq = {

    user.appVerifyAuth(verifier) match {

      case Some(username: String) =>     // if URL has tokens, then complete OAuth process

        user.authorized = true
        user.screenName = username
        //        S.redirectTo("/" + username)
        S.redirectTo("/")  // redirect to home page
        userLoggedIn   // return node sequence for logged in user

      case _ => <b>something aint right</b> // return error notification node sequence 
    }
  }
 // return node sequence for logged in user
  def userLoggedIn = <span>
    <li>
      <a href={"/" + user.screenName}>
        {user.screenName}
      </a>
    </li>
    <li>
      <a href="/logout" class="last">Log Out</a>
    </li>
  </span>
}
