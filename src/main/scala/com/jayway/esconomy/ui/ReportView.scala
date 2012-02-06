package com.jayway.esconomy.ui

import com.jayway.esconomy.util.Utils._
import com.vaadin.data.Property
import com.vaadin.data.Property.ValueChangeEvent
import wrapped.{PanelW, ComboBoxW, HorizontalLayoutW, VerticalLayoutW}
import com.vaadin.ui.CheckBox
import com.jayway.esconomy.dao.Queries
import java.util.{LinkedHashSet, Calendar}
import com.invient.vaadin.charts.{InvientCharts, InvientChartsConfig}
import com.invient.vaadin.charts.InvientCharts.{DecimalPoint, XYSeries, SeriesType}
import com.invient.vaadin.charts.InvientChartsConfig.{PointConfig, PieDataLabel, PieConfig}
import java.text.DecimalFormat


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
  val df = new DecimalFormat("##.##");
  val mainPanel = new PanelW(caption = "Reports", width = "100%")
  val mainLayout = new VerticalLayoutW()
  val periodLayout = new HorizontalLayoutW(width = "100%")
  val chartLayout = new HorizontalLayoutW(width = "100%")
  val yearCombo = new ComboBoxW(caption = "Year")
  val monthCombo = new ComboBoxW(caption = "Month")
  val showYearlyChkBox = new CheckBox("Show yearly", false)
  

  def getComponents = {
    constructPeriodLayout()
    mainLayout.setSizeFull()
    mainLayout addComponent periodLayout
    mainLayout addComponent chartLayout
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
      queries.getItemsGroupedByCategoriesIn(yearCombo.getValue.toString.toInt, months.indexOf(monthCombo.getValue.toString).toString.toInt) match {
        case Left(x) =>
        case Right(x) => updateChart(x) 
      }
    } else {
      println("One of the combo was null!")
    }
  }
  
  def updateChart(list:List[(String, Double)]) = {
    val totalExpense = list.foldLeft(0.0)( (r,c) => r + c._2)
    val chart = getChart (list.map { x => (x._1, df.format(100 * x._2/totalExpense).toDouble)})
    chartLayout.removeAllComponents()
    chartLayout.addComponent(chart)
  }

  def getChart(list:List[(String, Double)]) = {
    val chartConfig = new InvientChartsConfig()
    chartConfig.getGeneralChartConfig.setType(SeriesType.PIE)

    chartConfig.getTitle.setText("Expenses in " + monthCombo.getValue + ", " + yearCombo.getValue)

    chartConfig.getTooltip.setFormatterJsFunc("function() {"
        + " return '<b>'+ this.point.name +'</b>: '+ this.y +' %'; "
        + "}")

    val pie = new PieConfig()
    pie.setAllowPointSelect(true)
    pie.setCursor("pointer")
    pie.setDataLabel(new PieDataLabel(false))
    pie.setShowInLegend(true)
    chartConfig.addSeriesConfig(pie)

    val chart = new InvientCharts(chartConfig)

    val series = new XYSeries("Category")
    val points = new LinkedHashSet[DecimalPoint]()
    list.foreach { i => points.add(new DecimalPoint(series, i._1, i._2))}

    series.setSeriesPoints(points)
    chart.addSeries(series)
    chart.setStyleName("v-chart-min-width")
    chart.setHeight("410px")
    chart.setWidth("400px")
    chart.setImmediate(true)
    chart
  }



}
