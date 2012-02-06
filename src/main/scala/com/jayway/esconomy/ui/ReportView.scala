package com.jayway.esconomy.ui

import com.jayway.esconomy.util.Utils._
import java.util.Calendar
import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import wrapped.{PanelW, ComboBoxW, HorizontalLayoutW, VerticalLayoutW}
import com.vaadin.ui.CheckBox
import com.jayway.esconomy.dao.Queries


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

case class ReportView(dashboard:Main) extends Property.ValueChangeListener {

  val mainPanel = new PanelW(caption = "Reports", width = "100%")
  val mainLayout = new VerticalLayoutW()
  val periodLayout = new HorizontalLayoutW(width = "100%")
  val yearCombo = new ComboBoxW(caption = "Year")
  val monthCombo = new ComboBoxW(caption = "Month")
  val showYearlyChkBox = new CheckBox("Show yearly", false)

  def getComponents = {
    constructPeriodLayout()
    mainLayout addComponent periodLayout
    mainPanel addComponent mainLayout
    mainPanel
  }

  def constructPeriodLayout() = {
    getYears.foreach(yearCombo.addItem(_))
    yearCombo.setValue(cal.get(Calendar.YEAR).toString)
    yearCombo addListener this

    monthCombo addListener this
    months.foreach(monthCombo.addItem _)
    monthCombo.setValue(months.apply(cal.get(Calendar.MONTH)))

    List(yearCombo, monthCombo, showYearlyChkBox).foreach { periodLayout addComponent _ }

  }

  def valueChange(event:ValueChangeEvent) {
    if ( yearCombo.getValue != null && monthCombo.getValue != null ) {
      val queries = new Queries
      queries.getItemsGroupedByCategoriesIn(yearCombo.getValue.toString.toInt, months.indexOf(monthCombo.getValue.toString).toString.toInt)
    } else {
      println("One of the combo was null!")
    }
  }



}
