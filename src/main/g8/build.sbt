import sbt.Keys.{ ivyScala, _ }
import org.scalafmt.sbt.ScalaFmtPlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport.ReleaseStep
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import sbtrelease.ReleasePlugin.autoImport._

lazy val commonScalacOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

lazy val commonSettings = Seq(
  organization := "com.minosiants",
  scalaVersion := "2.12.1",
  scalacOptions ++= commonScalacOptions,
  fork in test := true,
  parallelExecution in Test := false,
  scalacOptions in (Compile, doc) := (scalacOptions in (Compile, doc)).value.filter(_ != "-Xfatal-warnings"),
  scalafmtConfig := Some(baseDirectory.in(ThisBuild).value / ".scalafmt.conf"),
  ivyScala := ivyScala.value.map(_.copy(overrideScalaVersion = sbtPlugin.value)),
  logLevel in assembly := Level.Error
)

lazy val commonJvmSettings = Seq(
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
)

lazy val releaseProcessSettings = Seq(
  releaseIgnoreUntrackedFiles := true,
  releaseProcess := Seq[ReleaseStep](checkSnapshotDependencies,
                                     inquireVersions,
                                     runClean,
                                     runTest,
                                     setReleaseVersion,
                                     commitReleaseVersion,
                                     tagRelease,
                                     publishArtifacts,
                                     setNextVersion,
                                     commitNextVersion,
                                     pushChanges)
)

val buildInfoSettings = (infoPackage: String, traits: Seq[String]) =>
  Seq(
    buildInfoOptions += BuildInfoOption.Traits(traits.mkString(",")),
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoKeys := Seq[BuildInfoKey](
      organization,
      name,
      version,
      scalaVersion,
      sbtVersion,
      scalacOptions,
      licenses
    ),
    buildInfoPackage := infoPackage
)

lazy val settings = releaseProcessSettings //++ buildInfoSettings("com.minosiants", Seq(""))

lazy val catsVersion       = "0.9.0"
lazy val circeVersion      = "0.7.0"
lazy val monocleVersion    = "1.4.0-M2"
lazy val scalatestVersion  = "3.0.0"
lazy val scalacheckVersion = "1.13.4"

def cats(version: String = catsVersion) = Seq(
  "org.typelevel" %% "cats" % version
)

def circe(version: String = circeVersion) = Seq(
  "io.circe" %% "circe-core"    % version,
  "io.circe" %% "circe-generic" % version,
  "io.circe" %% "circe-parser"  % version,
  "io.circe" %% "circe-java8"   % version
)

def monocle(version: String = monocleVersion) = Seq(
  "com.github.julien-truffaut" %% "monocle-core"  % version,
  "com.github.julien-truffaut" %% "monocle-macro" % version,
  "com.github.julien-truffaut" %% "monocle-law"   % version % "test"
)
def testing(
    scalatest: String = scalatestVersion,
    scalacheck: String = scalacheckVersion
) = Seq(
  "org.scalatest"  %% "scalatest"  % scalatest  % "test",
  "org.scalacheck" %% "scalacheck" % scalacheck % "test"
)

lazy val dependencies = Seq(
    ) ++ cats() ++ circe() ++ monocle() ++ testing()

lazy val `$name;format=" normalize" $` = project
  .in(file("."))
  .settings(name := "$name;format=" normalize" $")
  .settings(commonSettings)
  .settings(commonJvmSettings)
  .settings(settings)
  .settings(reformatOnCompileSettings)
  .settings(libraryDependencies ++= dependencies)
//.enablePlugins(BuildInfoPlugin)
