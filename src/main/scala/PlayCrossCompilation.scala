/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sbt

import Keys._

object PlayCrossCompilation {

  sealed trait PlayVersion
  case object Play25 extends PlayVersion
  case object Play26 extends PlayVersion

  val playVersion: PlayVersion =
    sys.env.get("PLAY_VERSION") match {
      case Some("2.5")   => Play25
      case Some("2.6")   => Play26
      case None          => Play25 // default to Play 2.5 for local development
      case Some(sthElse) => throw new Exception(s"Play version '$sthElse' not supported")
    }

  private val releaseSuffix, playDir =
    if (playVersion == Play25) "play-25" else "play-26"

  private def updateVersion(v: String): String =
    if (v.endsWith("-SNAPSHOT")) {
      v.stripSuffix("-SNAPSHOT") + "-" + releaseSuffix + "-SNAPSHOT"
    } else {
      v + "-" + releaseSuffix
    }

  def apply() = Seq(
    version ~= { v =>
      updateVersion(v)
    },
    unmanagedSourceDirectories in Compile += {
      (sourceDirectory in Compile).value / playDir
    },
    unmanagedSourceDirectories in Test += {
      (sourceDirectory in Test).value / playDir
    },
    unmanagedResourceDirectories in Compile += {
      (sourceDirectory in Compile).value / playDir / "resources"
    },
    unmanagedResourceDirectories in Test += {
      (sourceDirectory in Test).value / playDir / "resources"
    }
  )

}
