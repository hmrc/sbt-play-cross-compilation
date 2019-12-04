import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

val pluginName = "sbt-play-cross-compilation"

lazy val project = Project(pluginName, file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    sbtPlugin := true,
    majorVersion := 0,
    makePublicallyAvailableOnBintray := true
  )
  .settings(
    targetJvm := "jvm-1.7",
    scalaVersion := "2.10.7",
    crossSbtVersions := List("0.13.18", "1.3.4"),
    libraryDependencies ++= Seq(
      "org.scalatest"  %% "scalatest"  % "3.0.5"   % Test,
      "org.scalamock"  %% "scalamock"  % "4.1.0"   % Test,
      "org.pegdown"    %  "pegdown"    % "1.6.0"   % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.0"  % Test
    )
  ).settings(
    scalaCompilerBridgeSource := {
      val sv = appConfiguration.value.provider.id.version
      ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
    }
  )
