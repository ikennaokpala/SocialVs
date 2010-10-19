package schoolprojectnov2010.snippet

import scala._
import math._

import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js._
import net.liftweb.widgets.flot._
import net.liftweb.common._
import JE._

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
        val sortedInfluencerList = influencerList sortWith (_.score > _.score) //dropRight 45
        val topic = sortedInfluencerList(0).topic
        val influencerDetails = sortedInfluencerList map {
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
                  </span>,

          "graph" -> flot _) 

      case _ => bind("sn", xhtml,
        "topicTitle1" -> "",
        "topicTitle2" -> "",
        "topicTitle3" -> "",
        "topic" -> <h1>Oops ! Error >>> Topic Not Found...</h1>,
        "instruction" -> "",

        "influencerDetails" -> "",
        "graph" -> "")

    }

  }

  def flot(xhtml: NodeSeq) = {
    val influencerList = Influencer.is.asInstanceOf[List[KloutUser]]
    val sortedInfluencerList = influencerList sortWith (_.score > _.score) //dropRight 45
    val data = sortedInfluencerList map {_.score}
    val bar_labels = sortedInfluencerList map {_.user_name}

    // One FlotSerie for each bar
    val data_to_plot = for ((y, x) <- data zipWithIndex) yield new FlotSerie() {
      override val data: List[(Double, Double)] = (x.toDouble, y.toDouble) :: Nil
      override val label = Full(bar_labels(x))
    }

    val options: FlotOptions = new FlotOptions() {
      override val series = Full(Map("bars" -> JsObj("show" -> true, "barWidth" -> 1.0)))

      /*override val xaxis = Full(new FlotAxisOptions() {
        override def min = Some(0d)

        override def max = Some(data.length * 1d)
      })*/
    }

    Flot.render("graph_area", data_to_plot, options, Flot.script(xhtml))
  }


}
