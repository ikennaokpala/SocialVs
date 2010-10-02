import sbt._

class SchoolProjectNov2010(info: ProjectInfo) extends DefaultWebProject(info) with stax.StaxPlugin {

    // stax
    override def staxApplicationId = "socialvs"

    override def staxUsername = "ikennaokpala"
    //    override def staxPassword = ""

    // Lift
    val snapshots = ScalaToolsSnapshots
    // Libraries
    val liftDep = "net.liftweb" %% "lift-mapper" % "2.1" % "compile"
    val jetty6Dep = "org.mortbay.jetty" % "jetty" % "6.1.14" % "test"
    val servletDep = "javax.servlet" % "servlet-api" % "2.5" % "provided"
    val junitDep = "junit" % "junit" % "3.8.1" % "test"



    val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"
    val nexusRepo = "nexus" at "https://nexus.griddynamics.net/nexus/content/groups/public"
    //  val jbossRepo = "jboss" at "http://repository.jboss.org/maven2/"
    // databinder dispatch
    val dispatch = "net.databinder" %% "dispatch-twitter" % "0.7.5"
    //  val jsonDep = "net.sf.json-lib" % "json-lib" % "2.2.3"

    val scalaToolsRepo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"

    // override val jettyPort = 9999
}
