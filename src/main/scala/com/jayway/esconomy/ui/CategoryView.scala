package com.jayway.esconomy.ui

import com.vaadin.ui._
import com.jayway.esconomy.dao.Commands
import com.jayway.esconomy.domain.Category
import com.vaadin.ui.Window.Notification
import com.vaadin.event.ShortcutAction.KeyCode
import wrapped.{VerticalLayoutW, HorizontalLayoutW}


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

case class CategoryView(dashboard:Main) extends Button.ClickListener {

  val label = new Label("Category")
  val categories = new TextField()
  val addBtn = new Button("Add")
  addBtn addListener this
  addBtn setClickShortcut KeyCode.ENTER
  val categoryTable = new CategoryTable(dashboard.tree)

  def getComponents = {
    val panel = new Panel("Categories")
    val verticalLayout = new VerticalLayoutW ( width = "100%")
    verticalLayout <~ List(getAddCategoryLayout, categoryTable)
    panel.addComponent(verticalLayout)
    panel
  }
  
  def getAddCategoryLayout = {
    val hori = new HorizontalLayoutW {}
    hori <~ List(label, categories, addBtn)
    categories focus()
    hori
  }

  def buttonClick(event:Button#ClickEvent) = {
    val commands = new Commands
    commands.saveCategory(Category(category = categories.getValue.toString  ))
    addBtn.getWindow.showNotification("Notification", "Category '" + categories.getValue + "' is added", Notification.TYPE_TRAY_NOTIFICATION)
    categoryTable.getAllItems()
    categories.setValue("")

  }

}