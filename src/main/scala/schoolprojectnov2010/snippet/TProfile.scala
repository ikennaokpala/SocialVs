package schoolprojectnov2010.snippet

import net.liftweb.http.S

class TProfile extends ApplicationUser {
    import scala.xml.NodeSeq

    def render(xhtml: NodeSeq): NodeSeq = (for{
        screenName <- S.param("screenName")
    } yield <div>
            {user.tweetsForName(screenName)}
        </div>) getOrElse noTweets

    def noTweets: NodeSeq =
        <div>no tweets here</div>


}
