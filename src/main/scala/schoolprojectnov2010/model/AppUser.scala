package schoolprojectnov2010.model

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Sep 12, 2010
 * Time: 2:10:18 AM
 * To change this template use File | Settings | File Templates.
 */


class AppUser extends Tweeter { // class AppUser
    val appActor = new ApplicationActor // new instance of ApplicationActor class
    var authorized: Boolean = false //  authorized set tpo false by default

    def appAuthURL =  // appAuthURL method making calls to  appActor
        appActor !? AuthURL // sending message  and receiving  reponse to / from AuthURL case class in message handler

    def appVerifyAuth(verifier: String) =     // appVerifyAuth method making calls to  appActor
        appActor !? OAuthResponse(verifier)  // sending message  and receiving  reponse to / from OAuthResponse case class in message handler

    def userDataForName(screenName: String) =   // userDataForName method making calls to  appActor
        appActor !? TwitteruserInfo(screenName)  // sending message  and receiving  reponse to / from TwitteruserInfo case class in message handler

    def topicInfluencersSearch(search: String) = // topicInfluencersSearch method making calls to  appActor
        appActor !? InfluencersSearch(search)   // sending message  and receiving  reponse to / from InfluencersSearch case class in message handler
}
