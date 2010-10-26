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
  private lazy val influencerList = Influencer.is.asInstanceOf[List[KloutUser]]
  private lazy val sortedInfluencerList = influencerList sortWith (_.score > _.score) //dropRight 45
  private lazy val topic = sortedInfluencerList(0).topic + "."
  private lazy val data = influencerList map {_.score}
  private lazy val bar_labels = influencerList map {_.user_name}

  private lazy val title = <b>According to
    <a href="http://www.klout.com">Klout</a>
    the following are the top 50 influencers on topic:</b>

  def render(xhtml: NodeSeq): NodeSeq = {
    Influencer.is match {
      case x :: rest =>
        val influencerDetails = sortedInfluencerList map {
          x => <li>
            <a href={"/" + x.user_name.trim} class="badge">
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
          "title" -> title,
          "graphTitle" -> "Graphical representation of the top influencers on topic: ",
          "topic" -> topic,
          "instruction" -> "To view volume and/or Influence rating.login with your twitter account.",
          "influencerDetails" ->
                  <ul>
                    {influencerDetails}
                  </ul>,
          "graphArea" -> <div id="graph_area" style="width:920px;height:400px;"></div>,
          "graph" -> flot _,
          "errorMsg" -> "")

      case _ => bind("sn", xhtml,
        "title" -> "",
        "graphTitle" -> "",
        "topic" -> "",
        "instruction" -> "",
        "influencerDetails" -> "",
        "graphArea" ->"",
        "graph" -> "",
        "errorMsg" -> <h1>Oops ! Error >>> Topic Not Found...</h1>)

    }

  }

  def flot(xhtml: NodeSeq) = {
    // One FlotSerie for each bar
    val data_to_plot = for ((y, x) <- data zipWithIndex) yield new FlotSerie() {
      override val data: List[(Double, Double)] = (x.toDouble, y.toDouble) :: Nil
      override val label = Full(bar_labels(x))
    }

    val options: FlotOptions = new FlotOptions() {
      override val series = Full(Map(
        "stack" -> JsTrue,
        "bars" -> JsObj("show" -> true, "barWidth" -> 1.0)))

      override val xaxis = Full(new FlotAxisOptions() {
        override def min = Some(0d)

        override def max = Some(data.length * 1d)
        //        override val mode = Full("time")
      })
      override val legend = Full(new FlotLegendOptions() {
        override val container = Full("legend_area")
      })

    }

    Flot.render("graph_area", data_to_plot, options, Flot.script(xhtml))
  }


}
