package schoolprojectnov2010.snippet

import net.liftweb.http._

import schoolprojectnov2010.model.TwitterUser

object UserSession extends SessionVar[Option[TwitterUser]](None)

class ApplicationUser {
    var user: TwitterUser = UserSession.is.getOrElse(createUserForSession)

    // hold instance of TwitterUser in Session
    def createUserForSession: TwitterUser = {
        val usr = new TwitterUser
        UserSession set Some(usr)
        usr
    }

}
