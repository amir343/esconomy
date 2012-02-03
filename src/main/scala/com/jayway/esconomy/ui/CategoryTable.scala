package com.jayway.esconomy.ui

import com.vaadin.event.Action
import com.vaadin.data.util.IndexedContainer
import com.vaadin.event.Action.Handler
import com.vaadin.data.{Item => VaadinItem }
import collection.JavaConversions._
import com.jayway.esconomy.domain.Category
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui._
import com.jayway.esconomy.dao.{Commands, Queries}
import com.vaadin.ui.Window.Notification
import com.vaadin.event.ShortcutAction.KeyCode
import com.jayway.esconomy.service.ComputeService

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

class CategoryTable(tree:Tree) extends Table {

  val editAction = new Action("Edit")
  val removeAction = new Action("Remove")

  val queries = new Queries

  this setPageLength 10
  this setWidth "50%"
  this setSelectable true
  this setFooterVisible true
  this setImmediate true

  val dataSource:IndexedContainer = new IndexedContainer()

  List(("Id", classOf[String]),
       ("Category", classOf[String]))
    .foreach { x => dataSource.addContainerProperty(x._1, x._2, "")  }

  getAllItems()

  this addActionHandler { new Handler {
    def getActions(target: AnyRef, sender: AnyRef): Array[Action] = Array(editAction, removeAction)

    def handleAction(action: Action, sender: AnyRef, target: AnyRef) {
      action match {
        case `editAction` => editTableItem(target)
        case `removeAction` => removeTableItem(target)
      }
    }
  }}

  def getAllItems() = {
    this removeAllItems()
    dataSource removeAllItems()

    queries.getAllCategories match {
      case Left(x) => tree.getWindow.showNotification("Error", x.toString, Notification.TYPE_ERROR_MESSAGE)
      case Right(x) => { x.foreach { i => addToContainer(i, dataSource)} }
    }

    this setContainerDataSource dataSource
    this setVisibleColumns Array[AnyRef]("Category")
  }

  def removeTableItem(target:AnyRef) {
    val item = this.getItem(target)
    val row = extractFromTable(item)
    val commands = new Commands
    commands.deleteCategory(row)

    getAllItems()
  }  

  def editTableItem(target:AnyRef) {
    val editWindow = new Window("Edit Item")
    editWindow setWidth "270px"
    editWindow setHeight "200px"
    editWindow setResizable false
    editWindow setModal true
    val item = this getItem target
    val row = extractFromTable(item)
    val verticalLayout = editWindow.getContent
    verticalLayout.addComponent(getEditWindowComponents(editWindow, row))
    getWindow.addWindow(editWindow)
    editWindow.center()
  }

  def extractFromTable(item:VaadinItem):Category = {
    val row = Category(
      item.getItemProperty("Id").getValue.toString,
      item.getItemProperty("Category").getValue.toString
    )
    row
  }

  def addToContainer(record:Category, dataSource:IndexedContainer) = {
    val itemId = dataSource.addItem()
    val item = dataSource.getItem(itemId)
    item.getItemProperty("Id").setValue(record.id)
    item.getItemProperty("Category").setValue(record.category)
  }

  def getEditWindowComponents(window:Window, category:Category) = {
    val categoryTxt = new TextField("Category")
    categoryTxt.setValue(category.category)
    val updateBtn = new Button("Update")
    updateBtn setClickShortcut KeyCode.ENTER
    val vertical = new VerticalLayout
    vertical.setSpacing(true)
    vertical.setMargin(true)
    List(categoryTxt, updateBtn).foreach( vertical.addComponent(_))
    updateBtn.addListener(new ClickListener {
      def buttonClick(event: Button#ClickEvent) {
        val commands = new Commands
        commands.updateCategory(Category(category.id, categoryTxt.getValue.toString))
        getAllItems()
        getWindow.removeWindow(window)
      }
    })
    categoryTxt focus()
    vertical
  }



}