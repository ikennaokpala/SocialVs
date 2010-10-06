package schoolprojectnov2010.snippet

import xml.NodeSeq
import net.liftweb._
import util.Helpers._
import schoolprojectnov2010.model.KloutUser

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Oct 5, 2010
 * Time: 12:39:54 AM
 * To change this template use File | Settings | File Templates.
 */

class SearchSnippet {
    def render(xhtml: NodeSeq): NodeSeq = {
        val influencerList = Influencer.is.asInstanceOf[List[KloutUser]]
        val influencerDetails = influencerList map {
            x => <li>
                <a href={"/" + x.user_name} class="badge">
                    <div class="thumb">
                            <img
                            src={x.picture}/>
                    </div>
                    <div class="username">
                        {x.user_name}
                    </div>
                    <div class="score">
                            <img src="/classpath/images/klout_mini.png"/>{x.score}
                    </div>
                </a>
            </li>
        }
        bind("sn", xhtml,
            "influencerDetails" -> <span>
                {influencerDetails}
            </span>)
    }

}