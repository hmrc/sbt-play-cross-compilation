import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

val pluginName = "sbt-play-cross-compilation"

lazy val project = Project(pluginName, file("."))
  .enablePlugins(SbtPlugin, SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    majorVersion := 1,
    makePublicallyAvailableOnBintray := true,
    crossSbtVersions := List("0.13.18", "1.3.4"),
    scalaVersion := "2.12.12",
    libraryDependencies ++= Seq(
      "org.scalatest"  %% "scalatest"  % "3.0.8"   % Test,
      "org.scalamock"  %% "scalamock"  % "4.1.0"   % Test,
      "org.pegdown"    %  "pegdown"    % "1.6.0"   % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.3"  % Test
    )
  )
