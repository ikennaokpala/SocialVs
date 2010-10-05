package schoolprojectnov2010.snippet

import xml.NodeSeq
import net.liftweb._
import util.Helpers._
import http._

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Oct 5, 2010
 * Time: 12:39:54 AM
 * To change this template use File | Settings | File Templates.
 */

class SearchSnippet {
    def render(xhtml: NodeSeq): NodeSeq =
//        if (searchObj.is.isEmpty)
//            S.redirectTo("/")
//        else
            bind("sn", xhtml,
                "searchtest" -> Influencer.is.toString)


}