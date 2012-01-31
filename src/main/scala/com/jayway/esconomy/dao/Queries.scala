package com.jayway.esconomy.dao

import com.jayway.esconomy.db.MongoOps
import com.jayway.esconomy.domain.Item
import collection.JavaConversions._
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
class Queries {

  def getAllItems = {
    try { Right(MongoOps.mongoOperations.findAll(classOf[Item], "items")) }
    catch { case e => Left("Error happened: " + e.getMessage) }
  }
  
  def getAllItemsIn(year:Int, month:Int) = {
    val cal = Calendar.getInstance()
    try {
      Right(MongoOps.mongoOperations.findAll(classOf[Item], "items").filter { x =>
        cal.setTime(x.date)
        if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) true else false
      })
    } catch { case e => Left("Error happened: " + e.getMessage)}
    
  }

}