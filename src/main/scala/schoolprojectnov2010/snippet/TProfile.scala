package schoolprojectnov2010.snippet

import net.liftweb.http.S
import schoolprojectnov2010.model.TwitterUserInfoStream
import net.liftweb.util._
import Helpers._
import scala.xml.{NodeSeq, Elem}


class TProfile extends ApplicationUser {
    def render(xhtml: NodeSeq): NodeSeq =
        if (user.authorized == true) {
            val tprofile = (
                    for{
                        screenName <- S.param("screenName")
                    } yield
                        userStream(screenName)
                    ) getOrElse noTweets
            if (tprofile.isInstanceOf[Elem]) {
                noTweets
            } else {

                val TwtInfoList = tprofile.asInstanceOf[List[TwitterUserInfoStream]]
                val TwtUserInfo = TwtInfoList(0)
                val text = TwtInfoList map (n => <li>
                    {n.text}
                </li>)


                def doBind(xhtml: NodeSeq) =
                    bind("p", xhtml,
                        "name" -> TwtUserInfo.name,
                        "description" -> TwtUserInfo.description,
                        "screen_name" -> TwtUserInfo.screenName,
                        "screen_name_anchor" -> <a>@
                            {TwtUserInfo.screenName}
                        </a>,
                        "url" -> TwtUserInfo.url,
                        "favourites_count" -> TwtUserInfo.favourites_count.toString,
                        "listed_count" -> TwtUserInfo.listed_count.toString,
                        "text" -> text,
                        "location" -> TwtUserInfo.location,
                        "statuses_count" -> TwtUserInfo.statuses_count.toString,
                        "followers_count" -> TwtUserInfo.followers_count.toString,
                        "friends_count" -> TwtUserInfo.friends_count.toString,
                        "profile_picture" -> <img src={TwtUserInfo.profile_image_url.toString} width=' ' height=' '/>

    )

    doBind(xhtml)
}

} else {
notAuthorised
}

def userStream (screen_name: String) = {
val userInfoList = user.tweetsForName (screen_name)
userInfoList
}


def noTweets: NodeSeq = {
<div>
no tweets here
</div>
}

def notAuthorised: NodeSeq = {
<center> <h2>
You need to login to used this application
</h2> </center>
}


}
