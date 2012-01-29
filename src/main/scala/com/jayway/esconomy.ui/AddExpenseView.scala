package com.jayway.esconomy.ui

import com.vaadin.ui._
import com.jayway.esconomy.domain.Item
import java.util.Date
import com.jayway.esconomy.dao.Commands

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

case class AddExpenseView(dashboard:Main) extends Button.ClickListener {

  val addExpensePanel = new Panel("Add an expense")
  val currentExpenseTable = new ExpenseTable
  val currentExpensesPanel = new Panel("Current expenses")
  val nameLbl = new Label("Item Name")
  val priceLbl = new Label("Price")
  val dateLbl = new Label("Date")
  val categoryLbl = new Label("Category")
  val nameTxt = new TextField()
  val priceTxt = new TextField()
  val dateInput = new PopupDateField()
  val categoryTxt = new TextField()
  val addBtn = new Button("Add")
  
  addBtn.addListener(this)

  def getComponents = {
    val verticalLayout = new VerticalLayout
    constructAddExpensePanel
    constructCurrentExpensesPanel
    addExpensePanel setWidth "50%"
    currentExpensesPanel setWidth "100%"
    verticalLayout addComponent addExpensePanel
    verticalLayout addComponent currentExpensesPanel
    verticalLayout setSpacing true
    verticalLayout setMargin true
    verticalLayout
  }
  
  def constructAddExpensePanel = {
    val gridLayout = new GridLayout(2,5)
    gridLayout setSpacing true
    gridLayout setHeight "100%"
    gridLayout setWidth "100%"

    dateInput.setResolution(DateField.RESOLUTION_DAY)
    dateInput.setValue(new Date())

    val components = List(nameLbl, nameTxt, priceLbl, priceTxt, dateLbl, dateInput, categoryLbl, categoryTxt, addBtn)
    components foreach { gridLayout.addComponent(_) }
    
    addExpensePanel addComponent gridLayout
  }

  def constructCurrentExpensesPanel = {
    currentExpensesPanel addComponent currentExpenseTable
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