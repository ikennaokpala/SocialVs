import sbt._

class SchoolProjectNov2010(info: ProjectInfo) extends DefaultWebProject(info){

  // Lift
  val snapshots = ScalaToolsSnapshots
  val liftDep = "net.liftweb" %% "lift-mapper" % "2.1-SNAPSHOT" % "compile"
  val jetty6Dep = "org.mortbay.jetty" % "jetty" % "6.1.14" % "test"
  val servletDep = "javax.servlet" % "servlet-api" % "2.5" % "provided"
  val junitDep = "junit" % "junit" % "3.8.1" % "test"

  val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"
  val nexusRepo = "nexus" at "https://nexus.griddynamics.net/nexus/content/groups/public"

  // databinder dispatch
  val scalaToolsRepo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
  val dispatch = "net.databinder" %% "dispatch-twitter" % "0.7.5"

  // override val jettyPort = 9999
}
