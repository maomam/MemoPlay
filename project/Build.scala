import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "MemoSpielPlay"
  val appVersion      = "1.0"

  val appDependencies = Seq(
    "org.scala-lang" % "scala-swing" % "2.10.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
