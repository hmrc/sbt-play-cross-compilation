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

import sbt.ModuleID
import uk.gov.hmrc.PlayCrossCompilation._

object Dependencies {

  def apply(
    common: Seq[ModuleID]    = Seq.empty,
    play25: Seq[ModuleID]    = Seq.empty,
    play26: Seq[ModuleID]    = Seq.empty,
    playVersion: PlayVersion = PlayCrossCompilation.playVersion
  ): Seq[ModuleID] =
    common ++ (playVersion match {
      case Play25 => play25
      case Play26 => play26
    })
}
