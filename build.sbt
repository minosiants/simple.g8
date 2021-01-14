import org.scalafmt.sbt.ScalaFmtPlugin.autoImport._

lazy val commonSettings = Seq(
  name := "simple.g8",
  organization := "com.minosiants",
  scalaVersion := "2.11.8",
  scalafmtConfig := Some(baseDirectory.in(ThisBuild).value / ".scalafmt.conf")
)
 
scriptedLaunchOpts ++= sys.process.javaVmArguments.filter(
  a => Seq("-Xmx", "-Xms", "-XX", "-Dsbt.log.noformat").exists(a.startsWith)
)

lazy val `simple-g8` = project.in(file(".")).settings(commonSettings).settings(reformatOnCompileSettings)
