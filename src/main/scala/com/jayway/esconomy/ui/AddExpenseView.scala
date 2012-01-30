package com.jayway.esconomy.ui

import com.vaadin.ui._
import java.util.{Calendar, Date}
import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent

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

case class AddExpenseView(dashboard:Main) extends Property.ValueChangeListener {

  val addExpensePanel = new Panel("Add an expense")
  val currentExpenseTable = new ExpenseTable
  val currentExpensesPanel = new Panel("Current expenses")

  val months = List("January", "Feburary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
  val yearCombo = new ComboBox()
  val monthCombo = new ComboBox()

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
    val addExpenseForm = new AddExpenseForm(currentExpenseTable)
    addExpensePanel addComponent (addExpenseForm.getComponents())
  }

  def constructCurrentExpensesPanel = {
    currentExpensesPanel addComponent periodConfiguration
    currentExpensesPanel addComponent currentExpenseTable
  }
  
  def periodConfiguration = {
    val cal = Calendar.getInstance()
    val hori = new HorizontalLayout
    hori setSpacing true
    hori setMargin true

    val label = new Label("Period")
    (1900 to cal.get(Calendar.YEAR)).map { _.toString }.reverse.foreach(yearCombo.addItem(_))
    yearCombo.setValue(cal.get(Calendar.YEAR).toString)
    yearCombo addListener this

    monthCombo addListener this
    months.foreach(monthCombo.addItem _)

    monthCombo.setValue(months.apply(cal.get(Calendar.MONTH)))

    hori.addComponent(label)
    hori.addComponent(yearCombo)
    hori.addComponent(monthCombo)

    hori
  }

  def valueChange(event:ValueChangeEvent) {
    if ( yearCombo.getValue != null && monthCombo.getValue != null ) {
      currentExpenseTable.getAllItemsIn(yearCombo.getValue.toString, months.indexOf(monthCombo.getValue.toString).toString)
    }
  }


  
}