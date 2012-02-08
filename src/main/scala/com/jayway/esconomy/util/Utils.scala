package com.jayway.esconomy.util

import java.text.SimpleDateFormat
import java.util.{Date, Calendar}

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

  val df = new SimpleDateFormat("yyyy-MM-dd")
  val cal = Calendar.getInstance()
  val months = List("January", "Feburary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
  val UNKNOWN_CATEGORY = "Unkown category"

  def getYears:List[String] = {
    (1900 to cal.get(Calendar.YEAR)).map { _.toString }.reverse.toList
  }

  def formatDate(date:Date):String = {
    df.format(date)
  }

  def getDate(date:String):Date = {
    df.parse(date)
  }

}
