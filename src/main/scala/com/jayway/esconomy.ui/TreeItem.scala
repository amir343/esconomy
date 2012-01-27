package com.jayway.esconomy.ui

import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import com.vaadin.ui.Tree

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

sealed abstract class TreeItem {
  val title:String
  def hasChildren:Boolean = false
}

case class MyEconomy() extends TreeItem {
  override val title = "My economy"
  override def hasChildren = true
}

case class AddExpense() extends TreeItem {
  override val title = "Add an expense"
}

case class Reports() extends TreeItem {
  override val title = "Reports"
  override def hasChildren = true
}

case class Settings() extends TreeItem {
  override val title = "Settings"
  override def hasChildren = true
}

case class AddCategory() extends TreeItem {
  override val title = "Add a category"
}

case class MenuItemValueChangeListener(dashboard:Main) extends Property.ValueChangeListener {
  def valueChange(event:ValueChangeEvent) {
    event.getProperty.getValue.asInstanceOf[TreeItem] match {
      case AddExpense() => dashboard switchToAddExpenseView
      case _ => println("No")
    }
  }
}