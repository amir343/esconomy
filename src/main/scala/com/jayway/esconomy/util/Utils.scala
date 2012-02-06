package com.jayway.esconomy.util

import java.util.Calendar

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

object Utils {

  val cal = Calendar.getInstance()
  val months = List("January", "Feburary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

  def getYears = {
    (1900 to cal.get(Calendar.YEAR)).map { _.toString }.reverse
  }

}
