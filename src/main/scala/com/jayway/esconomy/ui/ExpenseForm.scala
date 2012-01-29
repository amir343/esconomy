package com.jayway.esconomy.ui

import com.vaadin.ui._
import java.util.Date
import com.jayway.esconomy.dao.Commands
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
trait ExpenseForm {

  val nameLbl = new Label("Item Name")
  val priceLbl = new Label("Price")
  val dateLbl = new Label("Date")
  val categoryLbl = new Label("Category")
  val nameTxt = new TextField()
  val priceTxt = new TextField()
  val dateInput = new PopupDateField()
  val categoryTxt = new TextField()

  def construct(btn:Button) = {
    val gridLayout = new GridLayout(2,5)
    gridLayout setSpacing true
    gridLayout setHeight "100%"
    gridLayout setWidth "100%"

    dateInput.setResolution(DateField.RESOLUTION_DAY)
    dateInput.setValue(new Date())

    val components = List(nameLbl, nameTxt, priceLbl, priceTxt, dateLbl, dateInput, categoryLbl, categoryTxt, btn)
    components foreach { gridLayout.addComponent(_) }
    gridLayout
  }

  def getComponents:GridLayout

}

class AddExpenseForm(currentExpenseTable:ExpenseTable) extends ExpenseForm with Button.ClickListener {

  val addBtn = new Button("Add")
  addBtn addListener this

  def getComponents():GridLayout = {
    construct(addBtn)
  }

  def buttonClick(event:Button#ClickEvent) = {
    val commands = new Commands()
    commands.saveItem(Item(
      itemName = nameTxt.getValue.asInstanceOf[String],
      price = priceTxt.getValue.asInstanceOf[String].toDouble,
      date = dateInput.getValue.asInstanceOf[Date],
      category = categoryTxt.getValue.toString))

    currentExpenseTable getAllItems()
  }

}

class EditExpenseForm(val currentExpenseTable:ExpenseTable, val item:Item, val window:Window) extends ExpenseForm with Button.ClickListener {

  val editBtn = new Button("Edit")
  editBtn addListener this

  nameTxt.setValue(item.itemName)
  priceTxt.setValue(item.price.toString)
  dateInput.setValue(item.date)
  categoryTxt.setValue(item.category)

  def getComponents():GridLayout = {
    construct(editBtn)
  }

  def buttonClick(event:Button#ClickEvent) = {
    val commands = new Commands()
    commands.saveItem(Item(
      item.id,
      nameTxt.getValue.asInstanceOf[String],
      priceTxt.getValue.asInstanceOf[String].toDouble,
      dateInput.getValue.asInstanceOf[Date],
      categoryTxt.getValue.toString))

    currentExpenseTable getAllItems()
    (window.getParent).removeWindow(window)
  }

}