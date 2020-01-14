/*
 * Copyright 2020 HM Revenue & Customs
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

import sbt._

object DependenciesGenerators {
  import org.scalacheck.Gen
  import org.scalacheck.Gen._

  private val revisions: Gen[String] = for {
    major <- choose(0, 50)
    minor <- choose(0, 50)
    patch <- choose(0, 100)
  } yield s"$major.$minor.$patch"

  val moduleIds: Gen[ModuleID] = for {
    groupIdLength  <- choose(3, 10)
    groupId        <- Gen.listOfN(groupIdLength, Gen.alphaChar).map(_.mkString)
    artifactLength <- choose(3, 10)
    artifact       <- Gen.listOfN(artifactLength, Gen.alphaChar).map(_.mkString)
    revision       <- revisions
  } yield ModuleID(groupId, artifact, revision)

  def nonEmptyListOf[A](generator: Gen[A], withMax: Int): Gen[Seq[A]] =
    for {
      itemsNumber <- Gen.choose(1, withMax)
      items       <- Gen.listOfN(itemsNumber, generator)
    } yield items
}
