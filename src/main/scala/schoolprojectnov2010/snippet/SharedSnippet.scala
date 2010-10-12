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

object Influencer extends SessionVar[Any]()
object searchObj extends RequestVar[String]("")

class SharedSnippet extends ApplicationUser {
    var search = ""
    import SHtml._
    import JsCmds._
    import JE._
    import JsCmds.Alert
    def render(xhtml: NodeSeq): NodeSeq = {

        bind("ss", xhtml,
            "search" -> text(searchObj.is, searchObj(_), "id" -> "search"),
            "submit" -> submit(S.?("Search"), () => doSearch(searchObj.is), "id" -> "btn", "onclick" ->
                    JsIf(JsEq(ValById("search"), ""), Alert("You are expected to provide a topic or keyword") & JsReturn(false))))
    }

    def smallSearchBox(xhtml: NodeSeq): NodeSeq = {

        bind("ss", xhtml,
            "search" -> text(search, search = _, "id" -> "smallbox"),
            "submit" -> submit(S.?("Go"), () => S.redirectTo("/" + search), "id" -> "smallboxbtn", "onclick" ->
                    JsIf(JsEq(ValById("smallbox"), ""),
                        Alert("You are expected to provide a twitter user name") & JsReturn(false))))
    }

    def doSearch(searchInput: String) = {
        val influencerList = user.topicInfluencersSearch(searchInput)
        Influencer set (influencerList)
        S.redirectTo("/search/index")

    }

}