import sbt._

class SchoolProjectNov2010(info: ProjectInfo) extends DefaultWebProject(info) with stax.StaxPlugin {
  val liftVersion = "2.1"
  // stax
  override def staxApplicationId = "socialvs"

  override def staxUsername = "ikennaokpala"
  //    override def staxPassword = ""

  // Lift
  val snapshots = ScalaToolsSnapshots
  // Libraries
  val liftWebkit = "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default" withSources ()
  val liftTestkit = "net.liftweb" %% "lift-testkit" % liftVersion % "compile->default"
  val liftWidgets = "net.liftweb" %% "lift-widgets" % liftVersion % "compile->default"
  val liftDep = "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default"
  val specs = "org.scala-tools.testing" % "specs" % "1.6.1" % "test->default"
  val jetty6Dep = "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default"
  val servletDep = "javax.servlet" % "servlet-api" % "2.5" % "provided"
  val junitDep = "junit" % "junit" % "4.5" % "test->default"
  // databinder dispatch
  val dispatch = "net.databinder" %% "dispatch-twitter" % "0.7.5"
  // Repositories ( local and remote
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
  val scalatoolsSnapshot = "Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/"
  val scalatoolsRelease = "Scala Tools Snapshot" at "http://scala-tools.org/repo-releases/"

  val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"
  val nexusRepo = "nexus" at "https://nexus.griddynamics.net/nexus/content/groups/public"
  //  val jbossRepo = "jboss" at "http://repository.jboss.org/maven2/"
  val scalaToolsRepo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"

  // override val jettyPort = 9999
}
