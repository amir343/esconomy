package com.jayway.esconomy.dao

import com.jayway.esconomy.db.MongoOps._
import collection.JavaConversions._
import java.util.Calendar
import com.jayway.esconomy.domain.{Category, Item}
import org.springframework.data.mongodb.core.query.{Criteria, Query}


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

class QueryExecution {

  def findAll() = {
    mongoOperations.findAll(classOf[Item], itemCollection)
  }

  def getAllItemsIn(year:Int, month:Int) = {
    val cal = Calendar.getInstance()
    mongoOperations.findAll(classOf[Item], itemCollection).filter { x =>
      cal.setTime(x.date)
      if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) true else false
    }
  }

  def getAllCategories = {
    mongoOperations.findAll(classOf[Category], categoryCollection)
  }

  def getItemsInCategory(category:String) = {
    mongoOperations.find(new Query(Criteria.where("category").is(category)),classOf[Item], itemCollection)
  }

  def getItemsGroupedByCategoriesIn(year:Int, month:Int) = {
    val cal = Calendar.getInstance()

    getAllCategories.foldLeft(List[(Category, List[Item])]()) { (r, coll) =>
      val it:List[Item] = getItemsInCategory(coll.category).filter { item =>
        cal.setTime(item.date)
        if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) true else false
      }.toList
      r ::: List((coll, it))
    }.map { tuples =>
      (tuples._1.category, sum(tuples._2))
    }
  }

  def getYearlyItemsGroupedByCategoriesIn(year:Int) = {
    val cal = Calendar.getInstance()

    getAllCategories.foldLeft(List[(Category, List[Item])]()) { (r, coll) =>
      val it:List[Item] = getItemsInCategory(coll.category).filter { item =>
        cal.setTime(item.date)
        if (cal.get(Calendar.YEAR) == year) true else false
      }.toList
      r ::: List((coll, it))
    }.map { tuples =>
      (tuples._1.category, sum(tuples._2))
    }
  }

  private def sum(items:List[Item]):Double = {
    items.foldLeft(0.0) { (r,c) => r + c.price }
  }


}
