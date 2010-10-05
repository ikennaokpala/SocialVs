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
import http.{S, SHtml, SessionVar}

object Influencer extends SessionVar[Any]()

class SharedSnippet extends ApplicationUser {
    var search = ""

    def render(xhtml: NodeSeq): NodeSeq = {
        bind("ss", xhtml,
            "search" -> SHtml.text(search, search = _),
            "submit" -> SHtml.submit(S.?("Search"), () => doSearch(search), "id" -> "btn"))
    }

    def smallSearchBox(xhtml: NodeSeq): NodeSeq = {

        bind("ss", xhtml,
            "search" -> SHtml.text(search, search = _, "id" -> "smallbox"),
            "submit" -> SHtml.submit(S.?("Go"), () => S.redirectTo("/" + search), "id" -> "smallboxbtn"))

    }

    def doSearch(search: String) =
        S.redirectTo("/search/index")
    Influencer set (user.topicInfluencersSeach(search))


}