package com.jayway.esconomy.ui

import java.util.Date
import com.jayway.esconomy.domain.Item
import com.vaadin.ui.Window.Notification
import com.vaadin.ui.AbstractSelect.Filtering
import com.jayway.esconomy.dao.{QueryChannelImpl, Commands}
import collection.JavaConversions._
import com.vaadin.ui._
import com.vaadin.event.ShortcutAction.KeyCode
import org.vaadin.autoreplacefield.DoubleField
import scalaz.{Success, Failure}
import wrapped.{ButtonW, GridLayoutW}


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
    val queries = new QueryChannelImpl
    queries.allCategories match {
      case Failure(x) =>
      case Success(x) => x.foreach { c => categoryCombo.addItem(c.category) }
    }
    val gridLayout = new GridLayoutW(columns = 2, rows = 5, height = "100%", width = "100%")

    dateInput.setResolution(DateField.RESOLUTION_DAY)
    priceTxt.setWidth("200px")
    nameTxt.setWidth("200px")
    dateInput.setWidth("200px")
    categoryCombo.setWidth("200px")

    val components = List(nameLbl, nameTxt, priceLbl, priceTxt, dateLbl, dateInput, categoryLbl, categoryCombo, btn)
    components foreach { com:AbstractComponent => com.setImmediate(true) }
    gridLayout <~ components
    nameTxt focus()
    gridLayout
  }

  def components:GridLayout

}

class AddExpenseForm(val expenseView:AddExpenseView) extends ExpenseForm with Button.ClickListener {

  val addBtn = new ButtonW("Add")
  addBtn setClickShortcut KeyCode.ENTER
  addBtn addListener this

  def components:GridLayout = {
    dateInput.setValue(new Date())
    construct(addBtn)
  }

  def buttonClick(event:Button#ClickEvent) = {
    if (nameTxt.getValue != null && priceTxt.getValue != null && dateInput.getValue != null && categoryCombo.getValue != null) {
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
    } else {
      addBtn.getWindow.showNotification("You haven't entered all the fields yet", Notification.TYPE_HUMANIZED_MESSAGE)
    }

  }

}

class EditExpenseForm(val view:View, val item:Item, val window:Window) extends ExpenseForm with Button.ClickListener {

  val editBtn = new ButtonW("Edit")
  editBtn setClickShortcut KeyCode.ENTER
  editBtn addListener this

  nameTxt.setValue(item.itemName)
  priceTxt.setValue(item.price.toString)
  dateInput.setValue(item.date)

  def components:GridLayout = {
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

    view decideTheView()
    (window.getParent).removeWindow(window)
  }

}