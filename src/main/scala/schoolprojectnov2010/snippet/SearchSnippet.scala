package schoolprojectnov2010.snippet

import scala._
import math._

import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js._
import net.liftweb.widgets.flot._
import net.liftweb.common._
import JE._

import schoolprojectnov2010.model.KloutUserVO
import xml.NodeSeq


/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Oct 5, 2010
 * Time: 12:39:54 AM
 * To change this template use File | Settings | File Templates.
 */


class SearchSnippet {     // class   SearchSnippet
  private lazy val influencerList = Influencer.is.asInstanceOf[List[KloutUserVO]]// casting List of Any to List of KloutUserVO lazily evaluated
  private lazy val sortedInfluencerList = influencerList sortWith (_.score > _.score) //Sorting the list of Klout Users lazily evaluated
  private lazy val topic = sortedInfluencerList(0).topic + "." // retriving topic from Klout user object lazily evaluated
  private lazy val data = influencerList map {_.score}  // creating new List of klout user score lazily evaluated
  private lazy val bar_labels = influencerList map {_.user_name}  // creating new List of klout user name lazily evaluated
    //  title lazily evaluated
  private lazy val title = <b>According to
    <a href="http://www.klout.com">Klout</a>
    the following are the top {sortedInfluencerList.length} influencers on topic:</b>

  def render(xhtml: NodeSeq): NodeSeq = {  // default page load method 
    Influencer.is match {
      case x :: rest =>
        val influencerDetails = sortedInfluencerList map { // creating new list of topic influencer with thier detail embedded in XML
          x => <li>
            <a href={"/" + x.user_name.trim /*triming username for any white spaces*/} class="badge">
              <div class="thumb">
                  <img
                  src={x.picture/*twitter picture*/}/>
              </div>
              <div class="username">
                {x.user_name /*twitter user name*/}
              </div>
              <div class="score">
                  <img src="/classpath/images/klout_mini.png"/>{round(x.score.doubleValue)/* Rounding to the nearest whole number*/}
              </div>
            </a>
          </li>
        }
        
        bind("sn", xhtml, // bind helper 
          "title" -> title,  // title
          "graphTitle" -> "Graphical representation of the top influencers on topic: ", // message string
          "topic" -> topic, // topic
          "instruction" -> "To view Influence report.login with your twitter account.", // message string
          "influencerDetails" ->
                  <ul>
                    {influencerDetails}
                  </ul>,
          "graphArea" -> <div id="graph_area" style="width:920px;height:400px;"></div>,
          "graph" -> flot _,//  calling flot graph method
          "errorMsg" -> "") // error message string

      case _ => bind("sn", xhtml,  // bind helper
        "title" -> "", // returning Empty string
        "graphTitle" -> "", // returning Empty string
        "topic" -> "", // returning Empty string
        "instruction" -> "", // returning Empty string
        "influencerDetails" -> "",// returning Empty string
        "graphArea" ->"",    // returning Empty string
        "graph" -> "",   // returning Empty string
        "errorMsg" -> <h1>Oops ! Error >>> Topic Not Found...</h1>)   // error message string

    }

  }

  def flot(xhtml: NodeSeq) = { // flot graphing  metho definition

    val data_to_plot = for ((y, x) <- data zipWithIndex) yield new FlotSerie() {   // One FlotSerie for each bar zipped with list index
      override val data: List[(Double, Double)] = (x.toDouble, y.toDouble) :: Nil  // creating new list for graph data
      override val label = Full(bar_labels(x)) // Bar label 
    }

    val options: FlotOptions = new FlotOptions() {  // Options for rendering graph
      override val series = Full(Map("bars" -> JsObj("show" -> true, "barWidth" -> 1.0))) // bar graph selected to be rendered

      override val xaxis = Full(new FlotAxisOptions() { // x axis flot options
        override def min = Some(0d) //  minimum value

        override def max = Some(data.length * 1d)  // maximum value
        //        override val mode = Full("time")
      })
      override val legend = Full(new FlotLegendOptions() {// legend flot options
        override val container = Full("legend_area")    // kengend container flot options
      })

    }

    Flot.render("graph_area", data_to_plot, options, Flot.script(xhtml)) // rendering the flot graph 
  }


}
