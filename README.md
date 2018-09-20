
# sbt-play-cross-compilation

 [ ![Download](https://api.bintray.com/packages/hmrc/releases/sbt-play-cross-compilation/images/download.svg) ](https://bintray.com/hmrc/releases/sbt-play-cross-compilation/_latestVersion)

This is a tiny sbt library to be used in projects requiring cross Play version compilation. The main goal is to use a single source code repository for different versions of Play.

In order to add Play cross compilation capabilities to your project following steps needs to taken:
* Add this library as an sbt plugin to your project's `plugins.sbt`:
```scala
addSbtPlugin("uk.gov.hmrc" % "sbt-play-cross-compilation" % "<LATEST_VERSION>")
```
* Create an object under the `project` folder extending `AbstractPlayCrossCompilation`
```scala
import uk.gov.hmrc.playcrosscompilation.AbstractPlayCrossCompilation
import uk.gov.hmrc.playcrosscompilation.PlayVersion.Play25

object PlayCrossCompilation extends AbstractPlayCrossCompilation(defaultPlayVersion = Play25)
```

The version of play set as part of `PlayCrossCompilation` will be used by sbt and your IDE by default. Allowed values are `PlayVersion.Play25` and `PlayVersion.Play26`.

* Set `PlayCrossCompilation.playCrossCompilationSettings` in your build.sbt
```scala
settings(PlayCrossCompilation.playCrossCompilationSettings)
```
* Configure different dependencies for different versions of Play
```scala
  val test: Seq[ModuleID] = PlayCrossCompilation.dependencies(
    shared = Seq("org.pegdown"            % "pegdown"             % "1.6.0" % Test),
    play25 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test),
    play26 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test)
  )
```
* Create Play version specific source folders if needed. Following folders are recognizable:

`src/main/play-25`, `src/main/play-26` for sources

`src/main/play-25/resources` and`src/main/play-26/resources` for resources

`test/main/play-25` and`test/main/play-26` for tests

The common `scala` folders in both `main` and `test` folders are still honoured and should contain non-Play version specific files. 

#### SBT
In order to run `sbt` commands against certain version of Play, the `PLAY_VERSION` environment variable has to be set prior to an sbt command. Allowed values for `PLAY_VERSION` are `2.5` and `2.6`
Example:
```
export PLAY_VERSION=2.6
sbt clean test
```

If no `PLAY_VERSION` is exported, default Play version from `PlayCrossCompilation` will be used.

#### IDE
The easiest way to switch between different versions of Play in an IDE, is to change the value of `defaultPlayVersion` defined in the `PlayCrossCompilation`.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
