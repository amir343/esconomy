package com.jayway.esconomy.ui

import java.util.Date
import com.jayway.esconomy.domain.Item
import com.vaadin.ui.Window.Notification
import com.vaadin.ui.AbstractSelect.Filtering
import com.jayway.esconomy.dao.{Queries, Commands}
import collection.JavaConversions._
import com.vaadin.ui._
import com.vaadin.event.ShortcutAction.KeyCode
import org.vaadin.autoreplacefield.DoubleField


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
trait ExpenseForm {

  val nameLbl = new Label("Item Name")
  val priceLbl = new Label("Price")
  val dateLbl = new Label("Date")
  val categoryLbl = new Label("Category")
  val nameTxt = new TextField()
  val priceTxt = new DoubleField()
  val dateInput = new PopupDateField()
  val categoryCombo = new ComboBox()

  categoryCombo setFilteringMode Filtering.FILTERINGMODE_STARTSWITH
  categoryCombo setImmediate true
  categoryCombo setNullSelectionAllowed false

  def construct(btn:Button) = {
    val queries = new Queries
    queries.getAllCategories match {
      case Left(x) =>
      case Right(x) => x.foreach { c => categoryCombo.addItem(c.category) }
    }
    val gridLayout = new GridLayout(2,5)
    gridLayout setSpacing true
    gridLayout setHeight "100%"
    gridLayout setWidth "100%"

    dateInput.setResolution(DateField.RESOLUTION_DAY)
    dateInput.setValue(new Date())

    val components = List(nameLbl, nameTxt, priceLbl, priceTxt, dateLbl, dateInput, categoryLbl, categoryCombo, btn)
    components foreach { gridLayout.addComponent(_) }
    nameTxt focus()
    gridLayout
  }

  def getComponents:GridLayout

}

class AddExpenseForm(val expenseView:AddExpenseView) extends ExpenseForm with Button.ClickListener {

  val addBtn = new Button("Add")
  addBtn setClickShortcut KeyCode.ENTER
  addBtn addListener this

  def getComponents():GridLayout = {
    construct(addBtn)
  }

  def buttonClick(event:Button#ClickEvent) = {
    val commands = new Commands()
    commands.saveItem(Item(
      itemName = nameTxt.getValue.asInstanceOf[String],
      price = priceTxt.getValue,
      date = dateInput.getValue.asInstanceOf[Date],
      category = categoryCombo.getValue.toString))

    addBtn.getWindow.showNotification("Notification", "Item '" + nameTxt.getValue + "' is added", Notification.TYPE_TRAY_NOTIFICATION)
    expenseView decideTheView()
    nameTxt.setValue("")
    categoryCombo.setValue("")
    priceTxt.setValue("")
  }

}

class EditExpenseForm(val expenseView:AddExpenseView, val item:Item, val window:Window) extends ExpenseForm with Button.ClickListener {

  val editBtn = new Button("Edit")
  editBtn setClickShortcut KeyCode.ENTER
  editBtn addListener this

  nameTxt.setValue(item.itemName)
  priceTxt.setValue(item.price.toString)
  dateInput.setValue(item.date)

  def getComponents():GridLayout = {
    val grid = construct(editBtn)
    categoryCombo.setValue(item.category)
    grid
  }

  def buttonClick(event:Button#ClickEvent) = {
    val commands = new Commands()
    commands.saveItem(Item(
      item.id,
      nameTxt.getValue.asInstanceOf[String],
      priceTxt.getValue,
      dateInput.getValue.asInstanceOf[Date],
      categoryCombo.getValue.toString))

    expenseView decideTheView()
    (window.getParent).removeWindow(window)
  }

}