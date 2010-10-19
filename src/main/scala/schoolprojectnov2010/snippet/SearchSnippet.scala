package schoolprojectnov2010.snippet

import scala._
import math._

import net.liftweb.widgets.flot._
import net.liftweb.common._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js._

import JE._

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
  val width = 500
  val height = 225

  def render(xhtml: NodeSeq): NodeSeq = {
    Influencer.is match {
      case x :: rest =>
        val influencerList = Influencer.is.asInstanceOf[List[KloutUser]]
        //                val sortedInfluencerList = stableSort(influencerList, (x:KloutUser, y:KloutUser) => round(x.score.toInt) < round(y.score.toInt))
        val sortedInfluencerList = influencerList sortWith (_.score > _.score) //dropRight 45
       /* val data = sortedInfluencerList map {_.score}
        val bar_labels = sortedInfluencerList map {_.user_name}
        val googleGraph = "http://chart.apis.google.com/chart?" + List(
          "chxt=x,y",
          //"chbh=52",
          "chxl=0:|" + bar_labels.mkString("|"),
          "chs=%dx%d".format(width, height),                    val data = sortedInfluencerList map {_.score}
        val bar_labels = sortedInfluencerList map {_.user_name}
        val googleGraph = "http://chart.apis.google.com/chart?" + List(
          "chxt=x,y",
          //"chbh=52",
          "chxl=0:|" + bar_labels.mkString("|"),
          "chs=%dx%d".format(width, height),
          "cht=bhg",
          "chco=A2C180",
          "chd=t:" + data.mkString(",")).mkString("&")
          "cht=bhg",
          "chco=A2C180",
          "chd=t:" + data.mkString(",")).mkString("&")*/
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

          "graph" -> "") // <img src={googleGraph.toString} width='1024' height='225' alt="graph"/>)

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

  //
  //  def googleUrl = "http://chart.apis.google.com/chart?" + List(
  //    "chxt=x,y",
  //    "chxl=0:|" + bar_labels.mkString("|"),
  //    "chs=%dx%d".format(width, height),
  //    "cht=bvg",
  //    "chco=A2C180",
  //    "chd=t:" + data.mkString(",")).mkString("&")

  //  def google(xhtml: NodeSeq) = <img src={googleUrl} width={width} height={height} alt="graph"/>



}
