package schoolprojectnov2010.snippet

class TProfile extends ApplicationUser {

  import scala.xml.{NodeSeq}
  import net.liftweb.http.{S}

  def render(xhtml:NodeSeq): NodeSeq = (for {
    screenName <- S.param("screenName")
  } yield <div>{user.tweetsForName(screenName)}</div>) getOrElse noTweets

  def noTweets: NodeSeq =
    <div> no tweets here </div>


}
