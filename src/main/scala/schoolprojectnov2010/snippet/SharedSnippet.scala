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


class SharedSnippet {
  var search = ""

  def render(xhtml: NodeSeq): NodeSeq = {

  bind("in", xhtml,
      "search" -> SHtml.text(search, search = _),
      "submit" -> SHtml.submit(S.?("Get Report"), () => {}, "id" -> "btn"))
  }


}