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

package uk.gov.hmrc

import org.scalamock.scalatest.MockFactory
import org.scalatest.WordSpec
import org.scalatest.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import uk.gov.hmrc.PlayCrossCompilation._

class PlayCrossCompilationSpec extends WordSpec with MockFactory with TableDrivenPropertyChecks {

  "playVersion" should {

    "return Play25 if no there is no PLAY_VERSION environment variable present" in new Setup {
      envPropertyFinder
        .expects("PLAY_VERSION")
        .returning(None)

      playCrossCompilation.playVersion shouldBe Play25
    }

    "return Play25 if PLAY_VERSION environment variable is set to '2.5'" in new Setup {
      envPropertyFinder
        .expects("PLAY_VERSION")
        .returning(Some("2.5"))

      playCrossCompilation.playVersion shouldBe Play25
    }

    "return Play26 if PLAY_VERSION environment variable is set to '2.6'" in new Setup {
      envPropertyFinder
        .expects("PLAY_VERSION")
        .returning(Some("2.6"))

      playCrossCompilation.playVersion shouldBe Play26
    }

    "throw an exception if PLAY_VERSION environment variable is set to value is neither '2.5' nor '2.6'" in new Setup {
      envPropertyFinder
        .expects("PLAY_VERSION")
        .returning(Some("x"))

      intercept[Exception] {
        playCrossCompilation.playVersion
      }.getMessage shouldBe "Play version 'x' not supported"
    }
  }

  val scenarios = Table(
    ("envProperty", "version", "expectedVersion"),
    ("2.5", "2.3.0", "2.3.0-play-25"),
    ("2.6", "2.3.0", "2.3.0-play-26"),
    ("2.5", "2.3.0-SNAPSHOT", "2.3.0-play-25-SNAPSHOT"),
    ("2.6", "2.3.0-SNAPSHOT", "2.3.0-play-26-SNAPSHOT")
  )

  "updateVersion" should {

    forAll(scenarios) { (envProperty, version, expectedVersion) =>
      s"return $expectedVersion if environment property is $envProperty and version is $version" in new Setup {
        envPropertyFinder
          .expects("PLAY_VERSION")
          .returning(Some(envProperty))

        playCrossCompilation.updateVersion(version) shouldBe expectedVersion
      }
    }
  }

  private trait Setup {
    val envPropertyFinder    = mockFunction[String, Option[String]]
    val playCrossCompilation = new PlayCrossCompilation(envPropertyFinder) {}
  }
}
