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

  def getAllItems = {
    try { Right(exec.findAll()) }
    catch { case e => Left(e.getMessage) }
  }
  
  def getAllItemsIn(year:Int, month:Int) = {
    try { Right(exec.getAllItemsIn(year, month)) }
    catch { case e => Left(e.getMessage)}
  }

  def getAllCategories = {
    try { Right(exec.getAllCategories) }
    catch { case e => Left(e.getMessage)}
  }
  
  def getItemsGroupedByCategoriesIn(year:Int, month:Int) = {
    try { Right(exec.getItemsGroupedByCategoriesIn(year, month)) }
    catch { case e => Left(e.getMessage)}
  }

  def getYearlyItemsGroupedByCategoriesIn(year:Int) = {
    try { Right(exec.getYearlyItemsGroupedByCategoriesIn(year)) }
    catch { case e => Left(e.getMessage)}
  }

}