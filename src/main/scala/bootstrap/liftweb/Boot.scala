package bootstrap.liftweb

import _root_.net.liftweb.sitemap._
import Loc._
import net.liftweb.http._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    // where to search snippet
    LiftRules.addToPackages("schoolprojectnov2010")

    // Build SiteMap
    val entries =
    Menu(Loc("index", List("index"),
      "Home")) ::
            Menu(Loc("search", List("search", "index"),
              "Search", Hidden)) ::
            Menu(Loc("profile", List("profile", "index"),
              "Profile", Hidden)) ::
            Menu(Loc("logout", List("logout", "logout"), "logout", Hidden)) ::
            Menu(Loc("error", List("error"),
              "Error", Hidden)) ::
            Menu(Loc("404", List("404"),
              "404", Hidden)) ::
            Nil

    // setting the sitemap
    LiftRules.setSiteMap(SiteMap(entries: _*))

    //Logout and Redirecto home page(/)
    LiftRules.dispatch.append {
      case Req("logout" :: Nil, _, GetRequest) => S.request.foreach(_.request.session.terminate);
      S.redirectTo("/")
      //            case Req("search" :: "index" :: Nil, _, GetRequest) if (searchObj.is.isEmpty) => S.redirectTo("/")

    }

    // rewrite rules for urls like rs.com/twitter-screen-name
    LiftRules.statefulRewrite.append {
      case RewriteRequest(ParsePath(screenName :: Nil, _, _, _), _, _)
        if isScreenNameValid(screenName) =>
        RewriteResponse("profile" :: "index" :: Nil, Map("screenName" -> screenName))

    }

    import net.liftweb.widgets.flot._
    Flot.init
    // Defining allowed resource folders located in the resources source folder
    ResourceServer.allow {
      //      case "jquery.js" :: Nil => true
      case "css" :: _ => true
      case "script" :: _ => true
      case "images" :: _ => true
      case "docs" :: _ => true
      case "js" :: _ => true
      case "flot" :: "jquery.flot.stack.js" :: Nil => true

    }

    //page error handling
    LiftRules.exceptionHandler.prepend {
      case (mode, request, ex) => RedirectResponse("/error")
    }
    // Redirect on no URL
    LiftRules.uriNotFound.prepend {
      case (req, _) =>
        NotFoundAsTemplate(ParsePath("404" :: Nil, "", true, false))
    }
  }
  // find out how to check if screen names exist in Twitter
  def isScreenNameValid(twitterScreenName: String): Boolean = {
    SiteMap.findAndTestLoc(twitterScreenName).isEmpty

  }

}


