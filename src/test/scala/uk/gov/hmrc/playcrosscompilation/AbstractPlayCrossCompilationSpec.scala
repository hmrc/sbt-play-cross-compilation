/*
 * Copyright 2021 HM Revenue & Customs
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

import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatest.prop.PropertyChecks
import uk.gov.hmrc.playcrosscompilation.DependenciesGenerators.{moduleIds, nonEmptyListOf}
import uk.gov.hmrc.playcrosscompilation.PlayVersion.{Play25, Play26}

class AbstractPlayCrossCompilationSpec extends WordSpec with MockFactory with PropertyChecks {

  "playVersion" should {

    forAll(playVersions) { defaultPlayVersion =>
      s"return $defaultPlayVersion if it's set as defaultPlayVersion and no PLAY_VERSION environment variable present" in new Setup {
        envPropertyFinder
          .expects("PLAY_VERSION")
          .returning(None)

        playCrossCompilation(defaultPlayVersion).playVersion shouldBe defaultPlayVersion
      }
    }

    "return Play25 regardless of defaultPlayVersion if PLAY_VERSION environment variable is set to '2.5'" in new Setup {
      envPropertyFinder
        .expects("PLAY_VERSION")
        .repeat(playVersions.size)
        .returning(Some("2.5"))

      forAll(playVersions) { defaultPlayVersion =>
        playCrossCompilation(defaultPlayVersion).playVersion shouldBe Play25
      }
    }

    "return Play26 regardless of defaultPlayVersion if PLAY_VERSION environment variable is set to '2.6'" in new Setup {
      envPropertyFinder
        .expects("PLAY_VERSION")
        .repeat(playVersions.size)
        .returning(Some("2.6"))

      forAll(playVersions) { defaultPlayVersion =>
        playCrossCompilation(defaultPlayVersion).playVersion shouldBe Play26
      }
    }

    "throw an exception if PLAY_VERSION environment variable is set to neither '2.5' nor '2.6'" in new Setup {
      envPropertyFinder
        .expects("PLAY_VERSION")
        .repeat(playVersions.size)
        .returning(Some("x"))

      forAll(playVersions) { defaultPlayVersion =>
        intercept[Exception] {
          playCrossCompilation(defaultPlayVersion).playVersion
        }.getMessage shouldBe "Play version 'x' not supported"
      }
    }
  }

  "updateVersion" should {

    val scenarios = Table(
      ("envProperty", "version", "expectedVersion"),
      ("2.5", "2.3.0", "2.3.0-play-25"),
      ("2.6", "2.3.0", "2.3.0-play-26"),
      ("2.8", "2.3.0", "2.3.0-play-28"),
      ("2.5", "2.3.0-SNAPSHOT", "2.3.0-play-25-SNAPSHOT"),
      ("2.6", "2.3.0-SNAPSHOT", "2.3.0-play-26-SNAPSHOT"),
      ("2.8", "2.3.0-SNAPSHOT", "2.3.0-play-28-SNAPSHOT")
    )

    forAll(scenarios) { (envProperty, version, expectedVersion) =>
      s"return $expectedVersion if environment variable is set to $envProperty and version is $version" in new Setup {
        envPropertyFinder
          .expects("PLAY_VERSION")
          .repeat(playVersions.size)
          .returning(Some(envProperty))

        forAll(playVersions) { defaultPlayVersion =>
          playCrossCompilation(defaultPlayVersion).updateVersion(version) shouldBe expectedVersion
        }
      }
    }
  }

  "dependencies" should {

    "return a list of shared dependencies if only these are provided" in new Setup {
      `no PLAY_VERSION set`

      forAll(nonEmptyListOf(moduleIds, withMax = 5)) { shared =>
        playCrossCompilation(Play26).dependencies(shared = shared) should contain theSameElementsAs shared
      }
    }

    "return a list of shared and play25 dependencies if playVersion is Play25" in new Setup {
      `no PLAY_VERSION set`

      forAll(
        nonEmptyListOf(moduleIds, withMax = 5),
        nonEmptyListOf(moduleIds, withMax = 5),
        nonEmptyListOf(moduleIds, withMax = 5)) { (shared, play25, play26) =>
        playCrossCompilation(Play25).dependencies(
          shared = shared,
          play25 = play25,
          play26 = play26
        ) should contain theSameElementsAs shared ++ play25
      }
    }

    "return a list of shared and play26 dependencies if playVersion is Play26" in new Setup {
      `no PLAY_VERSION set`

      forAll(
        nonEmptyListOf(moduleIds, withMax = 5),
        nonEmptyListOf(moduleIds, withMax = 5),
        nonEmptyListOf(moduleIds, withMax = 5)) { (shared, play25, play26) =>
        playCrossCompilation(Play26).dependencies(
          shared = shared,
          play25 = play25,
          play26 = play26
        ) should contain theSameElementsAs shared ++ play26
      }
    }
  }

  private lazy val playVersions = Table(
    "PlayVersion",
    Play25,
    Play26
  )

  private trait Setup {
    val envPropertyFinder = mockFunction[String, Option[String]]

    def playCrossCompilation(defaultPlayVersion: PlayVersion) =
      new AbstractPlayCrossCompilation(defaultPlayVersion, envPropertyFinder) {}

    lazy val `no PLAY_VERSION set` =
      envPropertyFinder
        .expects("PLAY_VERSION")
        .anyNumberOfTimes()
        .returning(None)
  }
}
