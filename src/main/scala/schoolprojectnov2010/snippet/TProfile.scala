package schoolprojectnov2010.snippet

/**
 * Created by IntelliJ IDEA.
 * User: ikennaokpala
 * Date: Sep 23, 2010
 * Time: 5:23:45 PM
 * To change this template use File | Settings | File Templates.
 */


import _root_.net.liftweb._
import http.S
import util._
import Helpers._
import scala.math._
import scala.xml.NodeSeq
import schoolprojectnov2010.model.TwitterUserVO

class TProfile extends ApplicationUser { // TProfile class definition
  val screenName = S.param("screenName") // retrieving twitter screen name from URL
  val width = 300 // graph width
  val height = 225 // graph height

  def render(xhtml: NodeSeq): NodeSeq = // render method definition
    user.authorized match {
      case true => val tprofile = (for{
        screenName <- S.param("screenName") // retrieving twitter screen name from URL
      } yield userStream(screenName)) getOrElse noTweets // yield making call to userStream for a particular twitter data if it returns null render the notweets method 
      tprofile match {
        case x :: rest => val TwtInfoList = tprofile.asInstanceOf[List[TwitterUserVO]] // casting List of Any to List of TwitterUserVO
        //                println("THE TWITTER INFORMATION FOR"+screenName+"  IS :"+TwtInfoList)
        val TwtUserInfo = TwtInfoList(0) // retrieving the first object index in list of TwitterUserVO
        val text = TwtInfoList map (n => <li>
          {n.text}
        </li>) // creating a new list of the twitter user tweets
        val influencerOfDetails = <li>
          <em>Oops !! This information is not available at the moment..</em>
        </li> // errors messagge for influencerOfDetails not conclusive yet
        val influencedByDetails = {
          TwtUserInfo.influencedBy match {
            case x :: rest => TwtUserInfo.influencedBy map { // creating a new list of the twitter user influenced By
              x => // println("This is the influencedByDetails " + x)
                <li>
                    <img src="/classpath/images/klout_mini.png"/>{round(x._2.doubleValue)}<a href={"/" + x._1}>
                  {"| @" + x._1}
                </a>

                </li>
            }
            case _ => <li>
              <em>Oops !! This information is not available at the moment..</em>
            </li> // errors messagge for influenced By not conclusive yet
          }
        }
        bind("p", xhtml, // bind helper returns a NodeSeq
          "error" -> "", // error mesage
          "name" -> TwtUserInfo.name.toString, // twitter user name
          "description" -> TwtUserInfo.description, // twitter user bio description
          "screen_name" -> TwtUserInfo.screenName, // twitter user screen name
          "screen_name_anchor" -> <a>@
            {TwtUserInfo.screenName}
          </a>, // twitter user screen name with anchor
          "influencedBy" -> influencedByDetails, // list of influenced .by details
          "influencerOf" -> influencerOfDetails, // list of influenced .of details
          "favourites_count" -> TwtUserInfo.favourites_count.toString, // twitter user favoritte count
          "listed_count" -> TwtUserInfo.listed_count.toString, // twitter user's listed count
          "text" -> text, // twitter user tweets
          "location" -> TwtUserInfo.location.toString, // twitter user's location details
          "statuses_count" -> TwtUserInfo.statuses_count.toString, // twitter user's status count
          "followers_count" -> TwtUserInfo.followers_count.toString, // twitter user's follwer count
          "profile_picture" -> <img src={TwtUserInfo.profile_image_url.toString} width=' ' height=' '/>, // twitter user's image
          "friends_count" -> TwtUserInfo.friends_count.toString, // twitter user's friends count
          "score" -> round(TwtUserInfo.score.doubleValue).toString, // twitter user's klout score
          "percentile" -> TwtUserInfo.percentile.toString, // twitter user's percentile score
          "kclass" -> TwtUserInfo.kclass, // twitter user's kclass score
          "klout_description" -> TwtUserInfo.klout_description, // twitter user's kclass description
          "truereach" -> round(TwtUserInfo.true_reach.doubleValue).toString, // twitter user's true reach
          "amplification" -> round(TwtUserInfo.amplification_score.doubleValue).toString, // twitter user's amplification score
          "network" -> round(TwtUserInfo.network_score.doubleValue).toString, // twitter user's networki score
          "scoregraph" -> <img src={googleVizGroovyChartURL(round(TwtUserInfo.score.doubleValue), TwtUserInfo.screenName)} width="300" height="150" alt="graph"/>, // Klout Score bar graph
          "truereachgraph" -> <img src={googleVizBarChartURL(List(round(TwtUserInfo.true_reach.doubleValue)), List(TwtUserInfo.screenName))} width={width.toString} height={height.toString} alt="graph"/>, // Klout true reach bar graph
          "ampgraph" -> <img src={googleVizBarChartURL(List(round(TwtUserInfo.amplification_score.doubleValue)), List(TwtUserInfo.screenName))} width={width.toString} height={height.toString} alt="graph"/>, // Klout amplification score bar graph
          "networkgraph" -> <img src={googleVizBarChartURL(List(round(TwtUserInfo.network_score.doubleValue)), List(TwtUserInfo.screenName))} width={width.toString} height={height.toString} alt="graph"/>, // Klout network score bar graph
          "qrcodegraph" -> <img src={googleVizQrCodeChartURL(TwtUserInfo.screenName, round(TwtUserInfo.score.doubleValue))} alt="graph"/>) // Klout qrcode score bar graph

        case _ => val tprofileTuple = tprofile.asInstanceOf[Tuple2[Option[String], String]] // casting List of Any toTuple2 of Option of string and string
        bind("p", xhtml, // bind helper returns xml node sequence
          "error" -> (tprofileTuple._2 + " is not a valid twitter user !!"), // returns twitter user screen name appedded to a message string 
          "name" -> "", // returns an Empty String
          "description" -> "", // returns an Empty String
          "screen_name" -> "", // returns an Empty String
          "screen_name_anchor" -> "", // returns an Empty String
          "influencedBy" -> "", // returns an Empty String
          "influencerOf" -> "", // returns an Empty String
          "favourites_count" -> "", // returns an Empty String
          "listed_count" -> "", // returns an Empty String
          "text" -> "", // returns an Empty String
          "location" -> "", // returns an Empty String
          "statuses_count" -> "", // returns an Empty String
          "followers_count" -> "", // returns an Empty String
          "profile_picture" -> "", // returns an Empty String
          "friends_count" -> "", // returns an Empty String
          "score" -> "", // returns an Empty String
          "percentile" -> "", // returns an Empty String
          "truereach" -> "", // returns an Empty String
          "amplification" -> "", // returns an Empty String
          "network" -> "", // returns an Empty String
          "scoregraph" -> "", // returns an Empty String
          "truereachgraph" -> "", // returns an Empty String
          "ampgraph" -> "", // returns an Empty String
          "networkgraph" -> "", // returns an Empty String
          "qrcodegraph" -> "") // returns an Empty String

      }
      case false => notAuthorised // returns Node Sequence of div with You need to login 
    }

  def userStream(screen_name: String) = { // userStream method definition
    val userInfoList = user.userDataForName(screen_name) // method call to userDataForName which returns List of Any
    userInfoList
  }
  // returns Node Sequence of div with Notweets
  def noTweets: NodeSeq = <div>
    no tweets here
  </div>

  // returns Node Sequence of div with You need to login 
  def notAuthorised: NodeSeq = <center>
    <h2>
      You need to login to used this application
    </h2>
  </center>

  def googleVizGroovyChartURL(data: Double, bar_label: String) = "http://chart.apis.google.com/chart?" + List(
    "chtt=Influence+Meter+for+@"+ bar_label,
    "chts=000000,10",
    "chxt=x,y",
    "chxl=0:|Low|High",
    "chxt=y",
    "chs=300x150",
//    "chma=10px,10px,10px,10px|0,0",
    "cht=gm",
    "chd=t:" + data,
    //    "chdl=" + bar_label,
    "chl=Influence").mkString("&") // returns Google vizualisation google o meter

  def googleVizBarChartURL(data: List[Double], bar_label: List[String]) = "http://chart.apis.google.com/chart?" + List(
    //    "chxr=0,0," + data.max + 10,
    "chxt=x,y",
    "chbh=41,0,240",
    "chxl=0:|" + bar_label.mkString("|"),
    "chs=%dx%d".format(width, height),
    //    "chds=0," + data.max + 10,
    "cht=bvg",
    "chco=A2C180",
    "chd=t:" + data.mkString(",")).mkString("&") // returns Google vizualisation chart bar graph
  //"chtt=%d+%d+%d".format(src, gtype, bar_label(0))).mkString("&")

  def googleVizQrCodeChartURL(user: String, kscore: Double) = "http://chart.apis.google.com/chart?" + List(
    "chf=a,s,000000",
    "chs=100x100",
    "cht=qr",
    "chld=|0",
    "chl=Twitter Username: @%s\n+Klout Score: %d\n".format(user, kscore.toInt)).mkString("&") // returns Google vizualisation chart QR Code bar code image
  //"chl=Twitter Username: @%d+Klout Score: %d+TrueReach: %d+Amplification: %d+ Network:%d").format(user, kscore, truereach, amp, net)).mkString("&")
}
