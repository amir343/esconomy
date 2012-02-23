package com.jayway.esconomy.service

import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification
import org.junit.runner.RunWith

/**
 * Copyright 2012 Amir Moulavi (amir.moulavi@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * @author Amir Moulavi
 */

@RunWith(classOf[JUnitRunner])
class SimilarStringAlgorithmSpec extends Specification {

  "[healed] and [healed] " should {
    "have similarity of 1.0" in {
      SimilarStringAlgorithm.similarity("healed", "healed") mustEqual 1.0
    }
  }

  "[healed] and [sealed] " should {
    "have similarity of 0.8" in {
      SimilarStringAlgorithm.similarity("healed", "sealed") mustEqual 0.8
    }
  }

  "[france] and [french] " should {
    "have similarity of 0.4" in {
      SimilarStringAlgorithm.similarity("france", "french") mustEqual 0.4
    }
  }

  "[Structural Assessment: The Role of Large and Full-Scale Testing] and [Web Database Applications] " should {
    "have similarity of 0.13953488372093023" in {
      SimilarStringAlgorithm.similarity("Structural Assessment: The Role of Large and Full-Scale Testing", "Web Database Applications") mustEqual 0.13953488372093023
    }
  }

  "[Web Database Applications with PHP & MySQL] and [Web Database Applications] " should {
    "have similarity of 0.7384615384615385" in {
      SimilarStringAlgorithm.similarity("Web Database Applications with PHP & MySQL", "Web Database Applications") mustEqual 0.7384615384615385
    }
  }

}
