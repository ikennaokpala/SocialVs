package schoolprojectnov2010.snippet

import scala._
import math._

import net.liftweb.util._
import Helpers._

import schoolprojectnov2010.model.KloutUser
import xml.NodeSeq

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Oct 5, 2010
 * Time: 12:39:54 AM
 * To change this template use File | Settings | File Templates.
 */

class SearchSnippet {
    def render(xhtml: NodeSeq): NodeSeq = {
        Influencer.is match {
            case x :: rest =>
                val influencerList = Influencer.is.asInstanceOf[List[KloutUser]]
                //                val sortedInfluencerList = stableSort(influencerList, (x:KloutUser, y:KloutUser) => round(x.score.toInt) < round(y.score.toInt))
                val sortedInfluencerList = influencerList sortWith (_.score < _.score)
                val topic = sortedInfluencerList(0).topic
                val influencerDetails = sortedInfluencerList.reverse map {
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
                                    <img src="/classpath/images/klout_mini.png"/>{round(x.score.doubleValue)}
                            </div>
                        </a>
                    </li>
                }
                bind("sn", xhtml,
                    "topicTitle1" -> "According to ",
                    "topicTitle2" -> <a href="http :// www.klout.com">Klout</a>,
                    "topicTitle3" -> "the following are the top 50 influencers on topic:",
                    "topic" -> topic,
                    "instruction" -> "To view volume and/or Influence rating.login with your twitter account.",
                    "influencerDetails" ->
                            <span>
                                {influencerDetails}
                            </span>)
            case _ => bind("sn", xhtml,
                "topicTitle1" -> "",
                "topicTitle2" -> "",
                "topicTitle3" -> "",
                "topic" -> <h1>Oops !!! Error >>> Not Found...</h1>,
                "instruction" -> "",

                "influencerDetails" -> "")

        }
    }
}