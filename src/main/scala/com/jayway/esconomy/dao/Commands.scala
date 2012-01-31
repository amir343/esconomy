package com.jayway.esconomy.dao

import com.jayway.esconomy.db.MongoOps._
import com.jayway.esconomy.domain.{Category, Item}
import org.springframework.data.mongodb.core.query.{Update, Query, Criteria}


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
class Commands {

  def saveItem(item:Item) {
    mongoOperations.save(item, itemCollection)
  }

  def deleteItem(item:Item) {
    mongoOperations.remove(item, itemCollection)
  }

  def saveCategory(category:Category) {
    mongoOperations.save(category, categoryCollection)
  }

  def updateCategory(category:Category) {
    val oldCategory = mongoOperations.findOne(new Query(Criteria.where("_id").is(category.id)), classOf[Category], categoryCollection)
    mongoOperations.save(category, categoryCollection)
    mongoOperations.updateMulti(new Query(Criteria.where("category").is(oldCategory.category) ), new Update().set("category", category.category), itemCollection)
  }

  def deleteCategory(category:Category) {
    mongoOperations.remove(category, categoryCollection)
    mongoOperations.updateMulti(new Query(Criteria.where("category").is(category.category) ), new Update().set("category", "UNKNOWN CATEGORY"), itemCollection)
  }

}