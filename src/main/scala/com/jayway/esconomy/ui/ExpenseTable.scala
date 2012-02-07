package com.jayway.esconomy.ui

import com.vaadin.data.util.IndexedContainer
import com.jayway.esconomy.domain.Item
import com.vaadin.event.Action
import com.vaadin.event.Action.Handler
import com.jayway.esconomy.dao.{Commands, Queries}
import java.util.Date
import collection.JavaConversions._
import com.vaadin.ui.{Tree, Panel, Window, Table}
import com.vaadin.ui.Window.Notification
import com.jayway.esconomy.service.ComputeService
import wrapped.ComboBoxW
import com.vaadin.data.{Property, Item => VaadinItem}
import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}


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
class ExpenseTable(addExpenseView:AddExpenseView, tree:Tree) extends Table {

  val editAction = new Action("Edit")
  val removeAction = new Action("Remove")
  val self = this

  val queries = new Queries
  
  this setPageLength 10
  this setWidth "100%"
  this setSelectable true
  this setFooterVisible true
  this setImmediate true

  val dataSource:IndexedContainer = new IndexedContainer()

  val categories = queries.getAllCategories.right.get.map { x => x.category }

  List( ("Id", classOf[String]),
        ("Item name", classOf[String]),
        ("Category", classOf[ComboBoxW]),
        ("Date", classOf[Date]),
        ("Price", classOf[String]) )
    .foreach { x => dataSource.addContainerProperty(x._1, x._2, "")  }

  getAllItems()

  this addActionHandler { new Handler {
    def getActions(target: AnyRef, sender: AnyRef): Array[Action] = Array(editAction, removeAction)

    def handleAction(action: Action, sender: AnyRef, target: AnyRef) {
      action match {
        case `editAction`   => editTableItem(target)
        case `removeAction` => removeTableItem(target)
      }
    }
  }}

  def getAllItems() = {
      self removeAllItems()
      dataSource removeAllItems()
      var totalSum = 0.0

      queries.getAllItems match {
        case Left(x) => tree.getWindow.showNotification("Error", x, Notification.TYPE_ERROR_MESSAGE)
        case Right(x) => {
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
  
  def getAllItemsIn(year:String, month:String) = {
      self removeAllItems()
      dataSource removeAllItems()
      var totalSum = 0.0

      queries.getAllItemsIn(year.toInt, month.toInt) match {
        case Left(x) =>  tree.getWindow.showNotification("Error", x, Notification.TYPE_ERROR_MESSAGE)
        case Right(x) => {
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
    item.getItemProperty("Date").setValue(record.date)
    item.getItemProperty("Price").setValue(record.price)
  }

  def editTableItem(target:AnyRef) {
    val editWindow = new Window("Edit Item")
    editWindow setWidth "380px"
    editWindow setHeight "350px"
    editWindow setResizable false
    editWindow setModal true
    val item = this getItem target
    val row = extractFromTable(item)
    val editExpenseForm = new EditExpenseForm(addExpenseView, row, editWindow)
    val verticalLayout = editWindow.getContent
    val panel = new Panel("Edit")
    panel.addComponent(editExpenseForm.getComponents())
    verticalLayout addComponent panel
    getWindow.addWindow(editWindow)
    editWindow.center()
  }

  def removeTableItem(target:AnyRef) = {
    val item = this.getItem(target)
    val row = extractFromTable(item)
    val commands = new Commands
    commands.deleteItem(row)

    getAllItems()
  }

  def extractFromTable(item:VaadinItem):Item = {
    val row = Item(
      item.getItemProperty("Id").getValue.toString,
      item.getItemProperty("Item name").getValue.toString,
      item.getItemProperty("Price").getValue.toString.toDouble,
      item.getItemProperty("Date").getValue.asInstanceOf[Date],
      item.getItemProperty("Category").getValue.toString
    )
    row
  }

}