package com.jayway.esconomy.dao

import com.jayway.esconomy.domain.{Category, Item}
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

trait QueryExecution {

  def findAll:List[Item]

  def findAllInCategory(category:String):List[Item]

  def allItemsIn(year:Int, month:Int):List[Item]

  def allCategories:List[Category]

  def itemsInCategory(category:String):List[Item]

  def itemsGroupedByCategoriesIn(year:Int, month:Int, currentCategories:mutable.ListBuffer[String]):List[(String, Double)]

  def yearlyItemsGroupedByCategoriesIn(year:Int, currentCategories:mutable.ListBuffer[String]):List[(String, Double)]

  def groupedPriceForItemsInCategory(category:String):List[(String, Double)]

  def find(keyword:String):List[Item]

}
