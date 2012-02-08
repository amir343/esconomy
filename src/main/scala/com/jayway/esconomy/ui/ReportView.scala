package com.jayway.esconomy.ui

import collection.JavaConversions._
import com.jayway.esconomy.util.Utils._
import com.vaadin.data.Property
import wrapped.{PanelW, ComboBoxW, HorizontalLayoutW, VerticalLayoutW}
import com.vaadin.ui.CheckBox
import com.jayway.esconomy.dao.Queries
import java.util.{LinkedHashSet, Calendar}
import com.invient.vaadin.charts.{InvientCharts, InvientChartsConfig}
import com.invient.vaadin.charts.InvientCharts.{DecimalPoint, XYSeries, SeriesType}
import java.text.DecimalFormat
import com.invient.vaadin.charts.InvientChartsConfig.GeneralChartConfig.Margin
import com.invient.vaadin.charts.InvientChartsConfig._
import com.invient.vaadin.charts.InvientChartsConfig.AxisBase.AxisTitle
import com.invient.vaadin.charts.Color.RGB
import com.vaadin.data.Property.{ValueChangeListener, ValueChangeEvent}
import com.vaadin.ui.Window.Notification


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

  val queries = new Queries

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
    mainLayout <~ List(periodLayout, chartLayout)
    mainPanel <~ mainLayout
    mainPanel
  }

  def constructPeriodLayout() = {
    yearCombo <~ getYears
    yearCombo.setValue(cal.get(Calendar.YEAR).toString)
    yearCombo addListener this

    monthCombo addListener this
    monthCombo <~ months
    monthCombo.setValue(months.apply(cal.get(Calendar.MONTH)))

    showYearlyChkBox.setImmediate(true)
    showYearlyChkBox.addListener(new ValueChangeListener {
      def valueChange(event: ValueChangeEvent) {
        showYearlyChkBox.getValue.asInstanceOf[Boolean] match {
          case false => {
            monthCombo.setEnabled(true)
            getItemsGroupedByCategories()
          }
          case true  => {
            monthCombo.setEnabled(false)
            getYearlyItemsGroupedByCategories()
          }
        }
      }
    })
    
    periodLayout <~ List(yearCombo, monthCombo, showYearlyChkBox)

  }

  def valueChange(event:ValueChangeEvent) {
    if ( yearCombo.getValue != null && monthCombo.getValue != null ) {
      showYearlyChkBox.getValue.asInstanceOf[Boolean] match {
        case true => getYearlyItemsGroupedByCategories()
        case false => getItemsGroupedByCategories()
      }
    } else {
      println("One of the combo was null!")
    }
  }

  def getYearlyItemsGroupedByCategories() {
    queries.getYearlyItemsGroupedByCategoriesIn(yearCombo.getValue.toString.toInt) match {
      case Left(x) => mainLayout.getWindow.showNotification("Error happened: " + x, Notification.TYPE_ERROR_MESSAGE)
      case Right(x) => updateChart(x)
    }
  }

  def getItemsGroupedByCategories() {
    queries.getItemsGroupedByCategoriesIn(yearCombo.getValue.toString.toInt, months.indexOf(monthCombo.getValue.toString).toString.toInt) match {
      case Left(x) => mainLayout.getWindow.showNotification("Error happened: " + x, Notification.TYPE_ERROR_MESSAGE)
      case Right(x) => updateChart(x)
    }
  }

  def updateChart(list:List[(String, Double)]) = {
    val totalExpense = list.foldLeft(0.0)( (r,c) => r + c._2)
    val barChart = getBarChart(list)
    val chart = getPieChart (list.map { x => (x._1, if (x._2 != 0.0) df.format(100 * x._2/totalExpense).toDouble else x._2)})
    chartLayout.removeAllComponents()
    chartLayout <~ List(chart,barChart)
  }

  def getPieChart(list:List[(String, Double)]) = {
    val chartConfig = new InvientChartsConfig()
    chartConfig.getGeneralChartConfig.setType(SeriesType.PIE)

    showYearlyChkBox.getValue.asInstanceOf[Boolean] match {
      case false => chartConfig.getTitle.setText("Expenses in " + monthCombo.getValue + ", " + yearCombo.getValue)
      case true => chartConfig.getTitle.setText("Expenses in " + yearCombo.getValue)
    }

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
  
  def getBarChart(list:List[(String, Double)]) = {
    val chartConfig = new InvientChartsConfig()
    chartConfig.getGeneralChartConfig.setType(SeriesType.COLUMN)
    chartConfig.getGeneralChartConfig.setMargin(new Margin())
    chartConfig.getGeneralChartConfig.getMargin.setTop(50)
    chartConfig.getGeneralChartConfig.getMargin.setRight(50)
    chartConfig.getGeneralChartConfig.getMargin.setBottom(100)
    chartConfig.getGeneralChartConfig.getMargin.setLeft(80)

    showYearlyChkBox.getValue.asInstanceOf[Boolean] match {
      case false => chartConfig.getTitle.setText("Expenses in " + monthCombo.getValue + ", " + yearCombo.getValue)
      case true => chartConfig.getTitle.setText("Expenses in " + yearCombo.getValue)
    }

    val categories = list.map { _._1 }
    val xAxis = new CategoryAxis()
    xAxis.setCategories(categories)
    xAxis.setLabel(new XAxisDataLabel())
    xAxis.getLabel.setRotation(-45)
    xAxis.getLabel.setAlign(HorzAlign.RIGHT)
    xAxis.getLabel.setStyle("{ font: 'normal 13px Verdana, sans-serif' }")
    val xAxesSet = new LinkedHashSet[InvientChartsConfig.XAxis]()
    xAxesSet.add(xAxis)
    chartConfig.setXAxes(xAxesSet)

    val yAxis = new NumberYAxis()
    yAxis.setMin(0.0)
    yAxis.setTitle(new AxisTitle("Expense (SEK)"))
    val yAxesSet = new LinkedHashSet[InvientChartsConfig.YAxis]()
    yAxesSet.add(yAxis);
    chartConfig.setYAxes(yAxesSet);

    chartConfig.setLegend(new Legend(false));

    chartConfig.getTooltip.setFormatterJsFunc("function() { return this.y +' SEK'; }")

    val chart = new InvientCharts(chartConfig)

    val colCfg = new ColumnConfig()
    colCfg.setDataLabel(new DataLabel())
    colCfg.getDataLabel.setRotation(-90)
    colCfg.getDataLabel.setAlign(HorzAlign.RIGHT)
    colCfg.getDataLabel.setX(-3)
    colCfg.getDataLabel.setY(10)
    colCfg.getDataLabel.setColor(new RGB(255, 255, 255))
    colCfg.getDataLabel.setFormatterJsFunc("function() {" + " return this.y; " + "}")
    colCfg.getDataLabel.setStyle(" { font: 'normal 9px Verdana, sans-serif' } ")
    val seriesData = new XYSeries("", colCfg)
    val expenses = new LinkedHashSet[InvientCharts.DecimalPoint]()
    list.foreach { t => expenses.add(new DecimalPoint(seriesData, t._2))}
    seriesData.setSeriesPoints(expenses);

    chart.addSeries(seriesData);
    chart    
  }



}
