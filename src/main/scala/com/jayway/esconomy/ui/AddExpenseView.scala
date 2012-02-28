package com.jayway.esconomy.ui

import java.util.Calendar
import com.vaadin.data.Property
import com.vaadin.ui._
import com.vaadin.data.Property.{ValueChangeListener, ValueChangeEvent}
import wrapped.{ComboBoxW, PanelW, HorizontalLayoutW, VerticalLayoutW}
import com.jayway.esconomy.util.Utils._

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

case class AddExpenseView(dashboard:Main) extends View with Property.ValueChangeListener {

  val addExpensePanel = new PanelW(caption = "Add an expense", width = "50%", height = "60%")
  val currentExpenseTable = new ExpenseTable(null, this, dashboard.cssLayout)
  val currentExpensesPanel = new PanelW(caption = "Current expenses")

  val label = new Label("Period: ")
  val yearCombo = new ComboBoxW(caption = "Year")
  val monthCombo = new ComboBoxW(caption = "Month")
  val showAllChkBox = new CheckBox("Show all items", false)

  def components = {
    val verticalLayout = new VerticalLayoutW
    constructAddExpensePanel()
    constructCurrentExpensesPanel()
    verticalLayout <~ List(addExpensePanel, currentExpensesPanel)
    verticalLayout
  }
  
  def constructAddExpensePanel() {
    val addExpenseForm = new AddExpenseForm(this)
    addExpensePanel addComponent (addExpenseForm.components)
  }

  def constructCurrentExpensesPanel() {
    currentExpensesPanel addComponent periodConfiguration
    currentExpensesPanel addComponent currentExpenseTable
  }
  
  def periodConfiguration = {
    val cal = Calendar.getInstance()
    val hori = new HorizontalLayoutW

    yearCombo <~ years
    yearCombo.setValue(cal.get(Calendar.YEAR).toString)
    yearCombo addListener this

    monthCombo addListener this
    monthCombo <~ months
    monthCombo.setValue(months.apply(cal.get(Calendar.MONTH)))

    showAllChkBox.setImmediate(true)
    showAllChkBox.addListener(new ValueChangeListener {
      def valueChange(event: ValueChangeEvent) {
        showAllChkBox.getValue.asInstanceOf[Boolean] match {
          case true  => disablePeriodConfigs(); currentExpenseTable.allItems()
          case false => enablePeriodConfig(); currentExpenseTable.allItemsIn(yearCombo.getValue.toString, months.indexOf(monthCombo.getValue.toString).toString)
        }
      }
    })

    hori <~ List(label, yearCombo, monthCombo, showAllChkBox)
    hori
  }

  def disablePeriodConfigs() = {
    label.setEnabled(false)
    yearCombo.setEnabled(false)
    monthCombo.setEnabled(false)
  }

  def enablePeriodConfig() = {
    label.setEnabled(true)
    yearCombo.setEnabled(true)
    monthCombo.setEnabled(true)
  }

  def valueChange(event:ValueChangeEvent) {
    if ( yearCombo.getValue != null && monthCombo.getValue != null ) {
      currentExpenseTable.allItemsIn(yearCombo.getValue.toString, months.indexOf(monthCombo.getValue.toString).toString)
    } else {
      println("One of the combo was null!")
    }
  }

  override def decideTheView() {
    showAllChkBox.getValue.asInstanceOf[Boolean] match {
      case true  => currentExpenseTable.allItems()
      case false => currentExpenseTable.allItemsIn(yearCombo.getValue.toString, months.indexOf(monthCombo.getValue.toString).toString)
    }
  }


  
}