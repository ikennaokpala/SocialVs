package schoolprojectnov2010.snippet

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Sep 8, 2010
 * Time: 1:47:55 PM
 * To change this template use File | Settings | File Templates.
 */

import _root_.net.liftweb.util._
import Helpers._
import xml.NodeSeq
import net.liftweb._
import http.js._
import http._

object Influencer extends SessionVar[Any]()  // influencer singleton object
object searchObj extends RequestVar[String]("") // search singletion object

class SharedSnippet extends ApplicationUser { // SharedSnippet extends ApplicationUser Class
    var search = "" // search variable
    import SHtml._    // Library imports into local class scope
    import JsCmds._
    import JE._
    import JsCmds.Alert
    def render(xhtml: NodeSeq): NodeSeq = {    // render method definition

        bind("ss", xhtml,          // bind helper
            "search" -> text(searchObj.is, searchObj(_), "id" -> "search"), // search text field
            "submit" -> submit(S.?("Search"), () => doSearch(searchObj.is), "id" -> "btn", "onclick" ->
                    JsIf(JsEq(ValById("search"), ""), Alert("You are expected to provide a topic or keyword") & JsReturn(false)))) // submit button with javascript validation
    }

    def twitterUserSearchBox(xhtml: NodeSeq): NodeSeq = {   // render method definition
        bind("ss", xhtml,
            "search" -> text(search, search = _, "id" -> "smallbox"), // search text field
            "submit" -> submit(S.?("Go"), () => S.redirectTo("/" + search), "id" -> "smallboxbtn", "onclick" ->
                    JsIf(JsEq(ValById("smallbox"), ""),
                        Alert("You are expected to provide a twitter user name") & JsReturn(false))))   // submit button with javascript validation
    }

    def doSearch(searchInput: String) = {  // topic search operation  method
        val influencerList = user.topicInfluencersSearch(searchInput) // method call to topicInfluencersSearch which returns List of Any
        Influencer set (influencerList)   // setting the Influencer session object with InfluencerList
        S.redirectTo("/search/index")  // redirecting to search index page which automatically invokes the render method for Search snippet

    }

}