package com.jayway.esconomy.ui

import com.vaadin.data.util.IndexedContainer
import com.jayway.esconomy.domain.Item
import com.vaadin.event.Action
import com.vaadin.event.Action.Handler
import collection.JavaConversions._
import com.vaadin.ui.Window.Notification
import com.vaadin.data.{Item => VaadinItem}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import wrapped.{WindowW, VerticalLayoutW, PanelW, ComboBoxW}
import com.jayway.esconomy.util.Utils._
import scalaz.{Failure, Success}
import java.lang.{Double => JDouble }
import com.vaadin.ui.{Component, Tree, Table}
import com.jayway.esconomy.dao.{Commands, QueryChannelImpl}

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
class ExpenseTable(selectedCategory:String, view:View, component:Component) extends Table {

  val queries = new QueryChannelImpl

  val editAction = new Action("Edit")
  val removeAction = new Action("Remove")

  val self = this

  this setPageLength 20
  this setWidth "100%"
  this setSelectable true
  this setFooterVisible true
  this setImmediate true

  val dataSource:IndexedContainer = new IndexedContainer()

  val categories = queries.allCategories match {
    case Success(x) => x map ( _.category )
    case Failure(x) => List()
  }

  List( ("Id", classOf[String]),
        ("Item name", classOf[String]),
        ("Category", classOf[ComboBoxW]),
        ("Date", classOf[String]),
        ("Price", classOf[JDouble]) )
    .foreach { x => dataSource.addContainerProperty(x._1, x._2, null)  }

  allItems()

  this addActionHandler { new Handler {
    def getActions(target: AnyRef, sender: AnyRef): Array[Action] = Array(editAction, removeAction)

    def handleAction(action: Action, sender: AnyRef, target: AnyRef) {
      action match {
        case `editAction`   => editTableItem(target)
        case `removeAction` => removeTableItem(target)
      }
    }
  }}

  def allItems() {
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

    self setColumnFooter ("Item name", "Total expenses")
    self setColumnFooter ("Price", totalSum + " SEK")
    self setContainerDataSource dataSource
    self setVisibleColumns Array[AnyRef]("Item name", "Category", "Date", "Price")
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

    self setColumnFooter ("Item name", "Total expenses")
    self setColumnFooter ("Price", totalSum + " SEK")
    self setContainerDataSource dataSource
    self setVisibleColumns Array[AnyRef]("Item name", "Category", "Date", "Price")
  }
  
  def addToContainer(record:Item, dataSource:IndexedContainer) = {
    val itemId = dataSource.addItem()
    val item = dataSource.getItem(itemId)
    val cats = new ComboBoxW()
    categories.foreach { cats.addItem(_) }
    cats.setValue(record.category)
    cats.addListener(new ValueChangeListener {
      def valueChange(event: ValueChangeEvent) {
        val commands = new Commands
        commands.saveItem(Item(record.id, record.itemName, record.price, record.date, cats.getValue.toString))
      }
    })
    item.getItemProperty("Id").setValue(record.id)
    item.getItemProperty("Item name").setValue(record.itemName)
    item.getItemProperty("Category").setValue(cats)
    item.getItemProperty("Date").setValue(formatDate(record.date))
    item.getItemProperty("Price").setValue(record.price.toDouble)
  }

  def editTableItem(target:AnyRef) {
    val editWindow = new WindowW(caption = "Edit Item", height = "320px", width = "380px")
    val item = this getItem target
    val row = extractFromTable(item)
    val editExpenseForm = new EditExpenseForm(view, row, editWindow)
    val verticalLayout = new  VerticalLayoutW
    editWindow.addComponent(verticalLayout)
    val panel = new PanelW("Edit")
    panel <~ editExpenseForm.components
    verticalLayout <~ panel
    getWindow.addWindow(editWindow)
    editWindow.center()
  }

  def removeTableItem(target:AnyRef) = {
    val item = this.getItem(target)
    val row = extractFromTable(item)
    val commands = new Commands
    commands.deleteItem(row)
    dataSource removeItem target
  }

  def extractFromTable(item:VaadinItem):Item = {
    val row = Item(
      item.getItemProperty("Id").getValue.toString,
      item.getItemProperty("Item name").getValue.toString,
      item.getItemProperty("Price").getValue.toString.toDouble,
      getDate(item.getItemProperty("Date").getValue.toString),
      item.getItemProperty("Category").getValue.toString
    )
    row
  }

}