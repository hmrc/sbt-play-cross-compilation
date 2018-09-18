
# sbt-play-cross-compilation

 [ ![Download](https://api.bintray.com/packages/hmrc/releases/sbt-play-cross-compilation/images/download.svg) ](https://bintray.com/hmrc/releases/sbt-play-cross-compilation/_latestVersion)

This is a tiny library to be used in build.sbt when cross Play compilation is required.

After adding `PlayCrossCompilation()` settings to your `build.sbt` file new set of folders can be used to add play version specific sources and files. The folders are:
* `src/main/play-25`, `src/main/play-26` for sources
* `src/main/play-25/resources` and`src/main/play-26/resources` for resources  
* `test/main/play-25` and`test/main/play-26` for tests

By default Play 2.5 branch is used. In order to switch between them, `PLAY_VERSION` environment variable has to be set to either `2.5` or `2.6`.

The common `scala` folders in both `main` and `test` are still honoured and should source code that is compatible with both versions of play. 

Example:
```
export PLAY_VERSION=2.6
```

In regards to Intellij opening a project in desired Play version, `launchctl setenv PLAY_VERSION 2.6` (on MACOS) has to be executed before starting the IDE. Standard bash `export` command seems not to be honoured by Intellij.

Usage in `build.sbt`:

```scala
import sbt.PlayCrossCompilation._

...

project.settings(
  PlayCrossCompilation(),
  libraryDependencies ++= compileDependencies ++ testDependencies,
)
  
...
    
val compileDependencies = {

    val play25Dependencies = Seq(
      "com.typesafe.play" %% "play-json"            % "2.5.12"
    )

    val play26Dependencies = Seq(
      "com.typesafe.play" %% "play-json"            % "2.6.8"
    )

    Seq(
      "play-version-independent" %% "artifact" % "XXX"
    ) ++ (
      if (playVersion == Play25) play25Dependencies else play26Dependencies
    )
}

val testDependencies = Seq(
    "org.scalatest"          %% "scalatest"          % "3.0.3"             % "test",
    "org.mockito"            % "mockito-all"         % "1.9.5"             % "test",
    "org.pegdown"            %  "pegdown"            % "1.6.0"              % "test"
)

```


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
