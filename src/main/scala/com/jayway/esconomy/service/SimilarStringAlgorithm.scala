package com.jayway.esconomy.service

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

object SimilarStringAlgorithm {

  def similarity(s1:String, s2:String):Double = {
    val slided_s1 = s1.toUpperCase.sliding(2).toList
    val slided_s2 = s2.toUpperCase.sliding(2).toList
    2.0 * slided_s1.intersect(slided_s2).size / (slided_s1.size + slided_s2.size)
  }


}
