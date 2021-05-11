lazy val project = Project("sbt-play-cross-compilation", file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    majorVersion := 2,
    isPublicArtefact := true,
    scalaVersion := "2.12.12",
    libraryDependencies ++= Seq(
      "org.scalatest"  %% "scalatest"  % "3.0.8"   % Test,
      "org.scalamock"  %% "scalamock"  % "4.1.0"   % Test,
      "org.pegdown"    %  "pegdown"    % "1.6.0"   % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.3"  % Test
    )
  )
