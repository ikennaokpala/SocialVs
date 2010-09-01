package rootsocial.snippet

import net.liftweb.http._

import rootsocial.model.TwitterUser

object UserSession extends SessionVar[Option[TwitterUser]](None)

class ApplicationUser {
  var user: TwitterUser =
  UserSession.is.getOrElse(createUserForSession)

  // hold instance of TwitterUser in Session
  def createUserForSession: TwitterUser = {

    val usr = new TwitterUser
    UserSession set Some(usr)
    usr
  }

}
