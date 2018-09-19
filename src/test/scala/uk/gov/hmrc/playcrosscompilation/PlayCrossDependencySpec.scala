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

package uk.gov.hmrc.playcrosscompilation

import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatest.prop.TableDrivenPropertyChecks
import sbt._
import uk.gov.hmrc.playcrosscompilation.data._

class PlayCrossDependencySpec extends WordSpec with TableDrivenPropertyChecks {

  import PlayCrossDependency.Implicits._

  "dependencyOf" should {

    forAll(playVersions) { playVersion =>
      s"convert the moduleId the method is invoked on and add the $playVersion playVersion" in {
        val dependency = "groupId" % "artifactName" % "2.3.4" dependencyOf playVersion

        dependency shouldBe PlayCrossDependency("groupId" % "artifactName" % "2.3.4", playVersion)
      }
    }
  }
}
