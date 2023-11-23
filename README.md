[DEPRECATED]

* This plugin will not support Play version 2.9 and above. As of now, the pattern for cross-compilation projects is the one showcased in the [platops-example-library](https://github.com/hmrc/platops-example-library), which uses standard sbt multi-modules. *

# sbt-play-cross-compilation

![](https://img.shields.io/github/v/release/hmrc/sbt-play-cross-compilation)

This is a tiny sbt library to be used in projects requiring cross Play version compilation. The main goal is to use a single source code repository for different versions of Play.

## Migration

### Version 2.0.0
- Supports Play __2.6__, __2.7__ and __2.8__. (Play 2.5 support was dropped.)
- Built for sbt __1.x__. (Sbt 0.13 was dropped.)

### Version 1.0.0
`playDir` no longer reuses the `play-26` source folders for Play 27, every version of play expects it's own directory. You can override this for the older behaviour - see below for details.

## Usage

In order to add Play cross compilation capabilities to your project following steps needs to taken:
* Add this library as an sbt plugin to your project's `plugins.sbt`:
```scala
addSbtPlugin("uk.gov.hmrc" % "sbt-play-cross-compilation" % "<LATEST_VERSION>")
```
* Create an object under the `project` folder extending `AbstractPlayCrossCompilation`
```scala
import uk.gov.hmrc.playcrosscompilation.{AbstractPlayCrossCompilation, PlayVersion}

object PlayCrossCompilation extends AbstractPlayCrossCompilation(defaultPlayVersion = PlayVersion.Play28)
```

The version of play set as part of `PlayCrossCompilation` will be used by sbt and your IDE by default. Allowed values are `PlayVersion.Play26`, `PlayVersion.Play27` and `PlayVersion.Play28`.

* Set `PlayCrossCompilation.playCrossCompilationSettings` in your build.sbt
```scala
settings(PlayCrossCompilation.playCrossCompilationSettings)
```

This must come after other settings since it updates previous settings.


* Configure different dependencies for different versions of Play
```scala
  val test: Seq[ModuleID] = PlayCrossCompilation.dependencies(
    shared = Seq("com.vladsch.flexmark"   %  "flexmark-all"       % "0.35.10" % Test),
    play27 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3"   % Test),
    play28 = Seq("org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0"   % Test)
  )
```
* Create Play version specific source folders if needed. Following folders are recognizable:

`src/main/play-27`, `src/main/play-28` for sources

`src/main/play-27/resources` and`src/main/play-28/resources` for resources

`test/main/play-27` and`test/main/play-28` for tests

The common `scala` folders in both `main` and `test` folders are still honoured and should contain non-Play version specific files.

Some versions of Play are backward compatible. You can override `playDir` defined in `AbstractPlayCrossCompilation` to reuse source folders, e.g. to reuse "play-27" sources for Play 2.8.
```scala
  override lazy val playDir = playVersion match {
    case Play26 => "play-26"
    case Play27 => "play-27"
    case Play28 => "play-27"
  }
```

#### SBT
In order to run `sbt` commands against certain version of Play, the `PLAY_VERSION` environment variable has to be set prior to an sbt command. Allowed values for `PLAY_VERSION` are `2.6`, `2.7`  and `2.8`
Example:
```
export PLAY_VERSION=2.8
sbt clean test
```

If no `PLAY_VERSION` is exported, default Play version from `PlayCrossCompilation` will be used.

#### IDE
The easiest way to switch between different versions of Play in an IDE, is to change the value of `defaultPlayVersion` defined in the `PlayCrossCompilation`.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
