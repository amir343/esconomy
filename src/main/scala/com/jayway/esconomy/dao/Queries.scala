package com.jayway.esconomy.dao

import com.jayway.esconomy.domain.{Category, Item}
import scalaz._


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

  val exec = new QueryExecution

  def allItems:Validation[String, List[Item]] = {
    try { Success(exec.findAll.sortWith( (i1, i2) => i1.date.compareTo(i2.date) > 0)) }
    catch { case e => Failure(e.getMessage) }
  }
  
  def allItemsIn(year:Int, month:Int):Validation[String, List[Item]] = {
    try { Success(exec.allItemsIn(year, month).sortWith( (i1, i2) => i1.date.compareTo(i2.date) > 0)) }
    catch { case e => Failure(e.getMessage)}
  }

  def allCategories:Validation[String, List[Category]] = {
    try { Success(exec.allCategories) }
    catch { case e => Failure(e.getMessage)}
  }
  
  def itemsGroupedByCategoriesIn(year:Int, month:Int):Validation[String, List[(String, Double)]] = {
    try { Success(exec.itemsGroupedByCategoriesIn(year, month)) }
    catch { case e => Failure(e.getMessage)}
  }

  def yearlyItemsGroupedByCategoriesIn(year:Int):Validation[String, List[(String, Double)]] = {
    try { Success(exec.yearlyItemsGroupedByCategoriesIn(year)) }
    catch { case e => Failure(e.getMessage)}
  }

}