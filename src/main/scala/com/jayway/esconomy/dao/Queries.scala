package com.jayway.esconomy.dao

import com.jayway.esconomy.db.MongoOps._
import collection.JavaConversions._
import java.util.Calendar
import com.jayway.esconomy.domain.{Category, Item}


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

  def allItems = {
    try { Right(exec.findAll()) }
    catch { case e => Left(e.getMessage) }
  }
  
  def allItemsIn(year:Int, month:Int) = {
    try { Right(exec.allItemsIn(year, month)) }
    catch { case e => Left(e.getMessage)}
  }

  def allCategories = {
    try { Right(exec.allCategories) }
    catch { case e => Left(e.getMessage)}
  }
  
  def itemsGroupedByCategoriesIn(year:Int, month:Int) = {
    try { Right(exec.itemsGroupedByCategoriesIn(year, month)) }
    catch { case e => Left(e.getMessage)}
  }

  def yearlyItemsGroupedByCategoriesIn(year:Int) = {
    try { Right(exec.yearlyItemsGroupedByCategoriesIn(year)) }
    catch { case e => Left(e.getMessage)}
  }

}