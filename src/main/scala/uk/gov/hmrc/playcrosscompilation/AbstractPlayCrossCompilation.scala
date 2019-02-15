/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.playcrosscompilation

import sbt.Keys._
import sbt._
import uk.gov.hmrc.playcrosscompilation.PlayVersion.{Play25, Play26}

sealed trait PlayVersion
object PlayVersion {
  case object Play25 extends PlayVersion
  case object Play26 extends PlayVersion
}

abstract class AbstractPlayCrossCompilation(
  defaultPlayVersion: PlayVersion,
  findEnvProperty: String => Option[String] = sys.env.get
) {

  lazy val playVersion: PlayVersion =
    findEnvProperty("PLAY_VERSION") match {
      case Some("2.5")   => Play25
      case Some("2.6")   => Play26
      case None          => defaultPlayVersion
      case Some(sthElse) => throw new Exception(s"Play version '$sthElse' not supported")
    }

  def dependencies(
    play25: Seq[ModuleID] = Nil,
    play26: Seq[ModuleID] = Nil,
    shared: Seq[ModuleID] = Nil): Seq[ModuleID] =
    shared ++ (
      playVersion match {
        case Play25 => play25
        case Play26 => play26
      }
    )

  def playCrossScalaBuilds(scalaVersions: Seq[String]) = playVersion match {
    case Play25 => scalaVersions.filter(version => version.startsWith("2.11"))
    case _ => scalaVersions
  }

  lazy val playCrossCompilationSettings = Seq(
    version ~= updateVersion,
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
    },
    crossScalaVersions ~= playCorssScalaBuilds
  )

  private lazy val releaseSuffix, playDir =
    if (playVersion == Play25) "play-25" else "play-26"

  private[hmrc] def updateVersion(v: String): String =
    if (v.endsWith("-SNAPSHOT")) {
      v.stripSuffix("-SNAPSHOT") + "-" + releaseSuffix + "-SNAPSHOT"
    } else {
      v + "-" + releaseSuffix
    }
}
