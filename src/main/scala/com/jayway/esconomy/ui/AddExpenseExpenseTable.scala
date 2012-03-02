package com.jayway.esconomy.ui

import collection.JavaConversions._
import com.vaadin.ui.Window.Notification
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import scalaz.{Failure, Success}
import com.vaadin.ui.Component

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
class AddExpenseExpenseTable(selectedCategory:String, view:View, component:Component)
  extends ExpenseTable(view, component) {

  self setColumnFooter ("Item name", "Total expenses")

  allItems(selectedCategory)

  override def allItems(filter:String) {
    self removeAllItems()
    dataSource removeAllItems()
    var totalSum = 0.0

    selectedCategory match {
      case null => queries.allItems match {
                      case Failure(x) => component.getWindow.showNotification("Error", x, Notification.TYPE_ERROR_MESSAGE)
                      case Success(x) => {
                        x.foreach { i =>
                          addToContainer(i, dataSource)
                          totalSum += i.price
                        }
                      }
                   }
      case _    => queries.allItemsInCategory(selectedCategory) match {
                      case Failure(x) => component.getWindow.showNotification("Error", x, Notification.TYPE_ERROR_MESSAGE)
                      case Success(x) => {
                        x.foreach { i =>
                          addToContainer(i, dataSource)
                          totalSum += i.price
                        }
                      }
                    }
    }

    self setColumnFooter ("Price", totalSum + " SEK")
  }
  
  def allItemsIn(year:String, month:String) {
    self removeAllItems()
    dataSource removeAllItems()
    var totalSum = 0.0

    queries.allItemsIn(year.toInt, month.toInt) match {
      case Failure(x) =>  component.getWindow.showNotification("Error", x, Notification.TYPE_ERROR_MESSAGE)
      case Success(x) => {
        x.foreach { i =>
          addToContainer(i, dataSource)
          totalSum += i.price
        }
      }
    }

    self setColumnFooter ("Price", totalSum + " SEK")
  }
  
}