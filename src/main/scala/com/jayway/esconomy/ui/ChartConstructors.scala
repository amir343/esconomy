package com.jayway.esconomy.ui

import collection.JavaConversions._
import com.invient.vaadin.charts.{InvientChartsConfig, InvientCharts}
import com.invient.vaadin.charts.InvientChartsConfig.GeneralChartConfig.Margin
import java.util.LinkedHashSet
import com.invient.vaadin.charts.InvientChartsConfig.AxisBase.AxisTitle
import com.invient.vaadin.charts.InvientChartsConfig._
import com.invient.vaadin.charts.Color.RGB
import com.invient.vaadin.charts.InvientCharts.{DecimalPoint, XYSeries, SeriesType}
import wrapped.ComboBoxW
import com.vaadin.ui.CheckBox

/**
 * Copyright 2012 Amir Moulavi (amir.moulavi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Amir Moulavi
 */

class ChartConstructors(allCategoriesCombo:ComboBoxW, monthCombo:ComboBoxW, yearCombo:ComboBoxW, showYearlyChkBox:CheckBox) {

  def groupedCategoryBarChart(list:List[(String, Double)]):InvientCharts = {
    val chartConfig = new InvientChartsConfig()
    chartConfig.getGeneralChartConfig.setType(SeriesType.COLUMN)
    chartConfig.getGeneralChartConfig.setMargin(new Margin())
    chartConfig.getGeneralChartConfig.getMargin.setTop(50)
    chartConfig.getGeneralChartConfig.getMargin.setRight(50)
    chartConfig.getGeneralChartConfig.getMargin.setBottom(100)
    chartConfig.getGeneralChartConfig.getMargin.setLeft(80)
    chartConfig.getTitle.setText("Expenses for category '" + allCategoriesCombo.getValue.asInstanceOf[String] + "'")

    val categories = formatDates(list.map(_._1))
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
    yAxesSet.add(yAxis)
    chartConfig.setYAxes(yAxesSet)

    chartConfig.setLegend(new Legend(false))

    chartConfig.getTooltip.setFormatterJsFunc("function() { return this.y +' SEK'; }")

    val chart = new InvientCharts(chartConfig)
    chart.setWidth("100%")

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
    seriesData.setSeriesPoints(expenses)

    chart.addSeries(seriesData)
    chart
  }

  def allCategoriesBarChart(list:List[(String, Double)]):InvientCharts = {
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
    yAxesSet.add(yAxis)
    chartConfig.setYAxes(yAxesSet)

    chartConfig.setLegend(new Legend(false))

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
    seriesData.setSeriesPoints(expenses)

    chart.addSeries(seriesData)
    chart
  }

  def formatDates(list:List[String]):List[String] = {
    def format(key:String):String = {
      val tokens = key.split("-")
      val month = tokens(1) match {
        case "00" => "Jan"
        case "01" => "Feb"
        case "02" => "Mar"
        case "03" => "Apr"
        case "04" => "May"
        case "05" => "Jun"
        case "06" => "Jul"
        case "07" => "Aug"
        case "08" => "Sep"
        case "09" => "Oct"
        case "10" => "Nov"
        case "11" => "Dec"
        case _    => "WTF!"
      }
      tokens(0) + " " + month
    }
    list.map(format(_))
  }

}
