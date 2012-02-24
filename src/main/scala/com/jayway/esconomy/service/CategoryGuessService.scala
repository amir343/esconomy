package com.jayway.esconomy.service

import com.jayway.esconomy.dao.QueryChannelImpl
import scalaz.{Failure, Success}
import com.jayway.esconomy.util.Utils.ItemTuple
import com.jayway.esconomy.domain.Item

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

case class CategoryGuessService() {
  
  val query = new QueryChannelImpl
  
  lazy val items:List[Item] = query.allItems match {
    case Success(x) => x
    case Failure(x) => List()
  }
  
  def estimateForItems(list:List[ItemTuple]):List[(ItemTuple, String)] = {
    list map ( findTheClosest )
  }

  def findTheClosest(item:ItemTuple):(ItemTuple, String) = {
    var maxCategoryName:String = null
    var maxSimilarity:Double = 0.0
    
    items.foreach { i =>
      val s = SimilarStringAlgorithm.similarity(item._2, i.itemName)
      if (s > maxSimilarity) {
        maxSimilarity = s
        maxCategoryName = i.category
      }
    }
    
    (item, maxCategoryName)
  }

}
