package schoolprojectnov2010{
package snippet{


import net.liftweb.http._
class TwitterOAuth extends ApplicationUser {
  import scala.xml.NodeSeq
  import SHtml._
  import js._
  import JsCmds.{Noop, RedirectTo}

    def render(xhtml: NodeSeq) = {
    // if user is already logged in, dont show OAuth button
       if (user.authorized) {
          userLoggedIn
        } else {
      // if user isnt logged in, then check for OAuth params on URL
      (for{
        tkn <- S.param("oauth_token")
        vrfr <- S.param("oauth_verifier")
      } yield useTokens(tkn, vrfr)) getOrElse span(xhtml, twitterAuthURL)
    }
  }

  // try to generate URL for Twitter Auth requests
  def twitterAuthURL: JsCmd = {

    user.twitterAuthURL match {

      case (Some(url: String)) =>

        RedirectTo(url)

      case _ =>

        Noop
    }
  }

  // if URL has tokens, then complete OAuth process
  def useTokens(token: String, verifier: String): NodeSeq = {
    println("USETOKENS  HAS BEEN CALLED ")

    user.twitterVerifyAuth(verifier) match {

      case Some(username: String) =>

        user.authorized = true
        user.screenName = username
        S.redirectTo("/"+username)
        userLoggedIn

      case _ =>     <b>something aint right</b>
    }
  }

  def userLoggedIn = <span><li >  <a href={user.screenName} >
    {user.screenName}  </a></li>  <li >  <a href="logout" class="last" >Log Out</a> </li></span>
}
}
}
