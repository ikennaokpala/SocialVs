package schoolprojectnov2010.snippet

import _root_.scala.xml.NodeSeq
import _root_.java.util.{Date}

case class myModel(mydate: Date)

class HelloWorldSnippet {
val today = myModel(new Date)

  def render(in: NodeSeq): NodeSeq = <span> { today.mydate.toString} </span>
}
 