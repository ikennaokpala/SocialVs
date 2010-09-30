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
import http.{S, SHtml}
import schoolprojectnov2010.model.Tweeter



class SharedSnippet {
    var search = ""

    def render(xhtml: NodeSeq): NodeSeq = {

        bind("ss", xhtml,
            "search" -> SHtml.text(search, search = _),
            "submit" -> SHtml.submit(S.?("Search"), () => {}, "id" -> "btn"))
    }

    def smallSearchBox(xhtml: NodeSeq): NodeSeq = {

        bind("ss", xhtml,
            "search" -> SHtml.text(search, search = _, "id" -> "smallbox"),
            "submit" -> SHtml.submit(S.?("Go"), () => {}, "id" -> "smallboxbtn"))

    }

}