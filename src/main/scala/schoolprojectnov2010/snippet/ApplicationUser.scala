package schoolprojectnov2010.snippet

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Sep 12, 2010
 * Time: 1:40:34 AM
 * To change this template use File | Settings | File Templates.
 */

import net.liftweb.http._

import schoolprojectnov2010.model.AppUser

object UserSession extends SessionVar[Option[AppUser]](None) //UserSession session object

class ApplicationUser {    // Application user class definition
    var user: AppUser = UserSession.is.getOrElse(createUserForSession)  // if UserSession is not set get new UserSessionfrom the createUserForSession

    // hold instance of AppUser in Session
    def createUserForSession: AppUser = { // method definition
        val usr = new AppUser // new Instance of AppUser
        UserSession set Some(usr) // set new Instance of AppUser in session scope
        usr     // new Instance of AppUser in session scope
    }

}
