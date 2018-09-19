
# sbt-play-cross-compilation

 [ ![Download](https://api.bintray.com/packages/hmrc/releases/sbt-play-cross-compilation/images/download.svg) ](https://bintray.com/hmrc/releases/sbt-play-cross-compilation/_latestVersion)

This is a tiny sbt library to be used in projects requiring cross Play version compilation.

In order to add Play cross compilation capabilities to your project following steps needs to taken:
* Add this library as an sbt plugin to your project's `plugins.sbt`:
```scala
addSbtPlugin("uk.gov.hmrc" % "sbt-play-cross-compilation" % "<LATEST_VERSION>")
```
* Create an object under the `project` folder extending `AbstractPlayCrossCompilation`
```scala
object PlayCrossCompilation extends AbstractPlayCrossCompilation(defaultPlayVersion = Play25)
```
* Reference your `PlayCrossCompilation`'s `playCrossCompilationSettings` property to your project's sbt settings
```scala
import PlayCrossCompilation.playCrossCompilationSettings

...

settings(playCrossCompilationSettings)
```
* Mark Play version dependent libraries with a relevant Play version
```scala
import PlayCrossCompilation._

  val test = DependenciesSeq(
    "org.pegdown"            % "pegdown"             % "1.6.0" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test crossPlay Play25,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test crossPlay Play26
  )
```
Note: the `crossPlay` keyword becomes available in the context of `DependenciesSeq` only.

* Create Play version specific source folders if needed. Following folders are recognizable:

`src/main/play-25`, `src/main/play-26` for sources

`src/main/play-25/resources` and`src/main/play-26/resources` for resources

`test/main/play-25` and`test/main/play-26` for tests


The default Play version is defined by the argument passed to the `AbstractPlayCrossCompilation(defaultPlayVersion = Play25)`. This default value can be changed by setting `PLAY_VERSION` environment variable to either `2.5` or `2.6` value.

The common `scala` folders in both `main` and `test` folders are still honoured and should contain non-Play version specific files. 

#### SBT
In order to run `sbt` commands against certain version of Play, the `PLAY_VERSION` environment variable has to be set prior to an sbt command.
Example:
```
export PLAY_VERSION=2.6
sbt clean test
```

#### IDE
The easiest way to switch between different versions of Play in an IDE, is to change value of the `defaultPlayVersion` defined in the object extending `AbstractPlayCrossCompilation`.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
