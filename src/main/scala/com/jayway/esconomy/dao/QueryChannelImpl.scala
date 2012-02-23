package com.jayway.esconomy.dao

import com.jayway.esconomy.domain.{Category, Item}
import scalaz._
import collection.mutable

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
class QueryChannelImpl extends QueryChannel {

  val exec = new QueryExecutionImpl

  override def allItems:Validation[String, List[Item]] = {
    try { Success(exec.findAll.sortWith( (i1, i2) => i1.date.compareTo(i2.date) > 0)) }
    catch { case e => Failure(e.getMessage) }
  }

  override def allItemsInCategory(category:String):Validation[String, List[Item]] = {
    try { Success(exec.findAllInCategory(category).sortWith( (i1, i2) => i1.date.compareTo(i2.date) > 0)) }
    catch { case e => Failure(e.getMessage) }
  }

  override def allItemsIn(year:Int, month:Int):Validation[String, List[Item]] = {
    try { Success(exec.allItemsIn(year, month).sortWith( (i1, i2) => i1.date.compareTo(i2.date) > 0)) }
    catch { case e => Failure(e.getMessage)}
  }

  override def allCategories:Validation[String, List[Category]] = {
    try { Success(exec.allCategories) }
    catch { case e => Failure(e.getMessage)}
  }

  override def itemsGroupedByCategoriesIn(year:Int, month:Int, currentCategories:mutable.ListBuffer[String]):Validation[String, List[(String, Double)]] = {
    try { Success(exec.itemsGroupedByCategoriesIn(year, month, currentCategories)) }
    catch { case e => Failure(e.getMessage)}
  }

  override def yearlyItemsGroupedByCategoriesIn(year:Int, currentCategories:mutable.ListBuffer[String]):Validation[String, List[(String, Double)]] = {
    try { Success(exec.yearlyItemsGroupedByCategoriesIn(year, currentCategories)) }
    catch { case e => Failure(e.getMessage)}
  }

}