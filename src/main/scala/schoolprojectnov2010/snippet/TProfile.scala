package schoolprojectnov2010.snippet

import _root_.net.liftweb._
import http.S
import util._
import Helpers._
import scala.math._
import scala.xml.NodeSeq
import schoolprojectnov2010.model.TwitterUserVO

class TProfile extends ApplicationUser {
  val screenName = S.param("screenName")
  val width = 300
  val height = 225

  def render(xhtml: NodeSeq): NodeSeq =
    user.authorized match {
      case true => val tprofile = (for{
        screenName <- S.param("screenName")
      } yield userStream(screenName)) getOrElse noTweets
      tprofile match {
        case x :: rest => val TwtInfoList = tprofile.asInstanceOf[List[TwitterUserVO]]
        //                println("THE TWITTER INFORMATION FOR"+screenName+"  IS :"+TwtInfoList)
        val TwtUserInfo = TwtInfoList(0)
        val text = TwtInfoList map (n => <li>
          {n.text}
        </li>)
        val influencerOfDetails = <li>
          <em>Oops !! This information is not available at the moment..</em>
        </li>
        val influencedByDetails = {
          TwtUserInfo.influencedBy match {
            case x :: rest => TwtUserInfo.influencedBy map {
              x => // println("This is the influencedByDetails " + x)
                <li>
                    <img src="/classpath/images/klout_mini.png"/>{round(x._2.doubleValue)}<a href={"/" + x._1}>
                  {"| @" + x._1}
                </a>

                </li>
            }
            case _ => <li>
              <em>Oops !! This information is not available at the moment..</em>
            </li>
          }
        }
        bind("p", xhtml,
          "error" -> "",
          "name" -> TwtUserInfo.name.toString,
          "description" -> TwtUserInfo.description,
          "screen_name" -> TwtUserInfo.screenName,
          "screen_name_anchor" -> <a>@
            {TwtUserInfo.screenName}
          </a>,
          "influencedBy" -> influencedByDetails,
          "influencerOf" -> influencerOfDetails,
          "favourites_count" -> TwtUserInfo.favourites_count.toString,
          "listed_count" -> TwtUserInfo.listed_count.toString,
          "text" -> text,
          "location" -> TwtUserInfo.location.toString,
          "statuses_count" -> TwtUserInfo.statuses_count.toString,
          "followers_count" -> TwtUserInfo.followers_count.toString,
          "profile_picture" -> <img src={TwtUserInfo.profile_image_url.toString} width=' ' height=' '/>,
          "friends_count" -> TwtUserInfo.friends_count.toString,
          "score" -> round(TwtUserInfo.score.doubleValue).toString,
          "percentile" -> TwtUserInfo.percentile.toString,
          "kclass" -> TwtUserInfo.kclass,
          "klout_description" -> TwtUserInfo.klout_description,
          "truereach" -> round(TwtUserInfo.true_reach.doubleValue).toString,
          "amplification" -> round(TwtUserInfo.amplification_score.doubleValue).toString,
          "network" -> round(TwtUserInfo.network_score.doubleValue).toString)

        case _ => val tprofileTuple = tprofile.asInstanceOf[Tuple2[Option[String], String]]
        bind("p", xhtml,
          "error" -> (tprofileTuple._2 + " is not a valid twitter user !!"),
          "name" -> "",
          "description" -> "",
          "screen_name" -> "",
          "screen_name_anchor" -> "",
          "influencedBy" -> "",
          "influencerOf" -> "",
          "favourites_count" -> "",
          "listed_count" -> "",
          "text" -> "",
          "location" -> "",
          "statuses_count" -> "",
          "followers_count" -> "",
          "profile_picture" -> "",
          "friends_count" -> "",
          "score" -> "",
          "percentile" -> "",
          "truereach" -> "",
          "amplification" -> "",
          "network" -> "")
      }
      case false => notAuthorised;
    }

  def userStream(screen_name: String) = {
    val userInfoList = user.tweetsForName(screen_name)
    userInfoList
  }

  def noTweets: NodeSeq = <div>
    no tweets here
  </div>

  def notAuthorised: NodeSeq = <center>
    <h2>
      You need to login to used this application
    </h2>
  </center>

  def googleChartURL(data: List[Int], bar_labels: List[String]) = "http://chart.apis.google.com/chart?" + List(
    "chxt=x,y",
    "chxl=0:|" + bar_labels.mkString("|"),
    "chs=%dx%d".format(width, height),
    "cht=bvg",
    "chco=A2C180",
    "chd=t:" + data.mkString(",")).mkString("&")

  //   def google(xhtml: NodeSeq) = <img src="" width={width} height={height} alt="graph"/>


}
