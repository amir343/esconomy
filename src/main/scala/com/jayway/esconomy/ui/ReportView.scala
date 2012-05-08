package com.jayway.esconomy.ui

import collection.JavaConversions._
import com.jayway.esconomy.util.Utils._
import com.vaadin.data.Property
import java.util.{LinkedHashSet, Calendar}
import com.invient.vaadin.charts.{InvientCharts, InvientChartsConfig}
import java.text.DecimalFormat
import com.invient.vaadin.charts.InvientChartsConfig.GeneralChartConfig.Margin
import com.invient.vaadin.charts.InvientChartsConfig._
import com.invient.vaadin.charts.InvientChartsConfig.AxisBase.AxisTitle
import com.invient.vaadin.charts.Color.RGB
import com.vaadin.data.Property.{ValueChangeListener, ValueChangeEvent}
import com.vaadin.ui.Window.Notification
import scalaz.{Success, Failure}
import collection.mutable
import com.invient.vaadin.charts.InvientCharts._
import com.vaadin.ui.{Button, CheckBox}
import com.vaadin.ui.Button.ClickListener
import com.jayway.esconomy.dao.{QueryChannelImpl}
import wrapped._


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

case class ReportView(dashboard:Main) extends View with Property.ValueChangeListener {

  lazy val queries = new QueryChannelImpl
  val self = this

  lazy val cats:List[String] = queries.allCategories match {
    case Success(x) => x map ( _.category )
    case Failure(x) => List()
  }

  val df = new DecimalFormat("##.##");
  val mainTab = new TabSheetW()

  /* All Categories tab */
  val checkBoxCategoryPanel = new PanelW(caption = "Categories and Period Configurations", width = "100%")
  val allCategoriesMainLayout = new VerticalLayoutW()
  val expenseTableLayout = new VerticalLayoutW()
  val periodLayout = new HorizontalLayoutW(width = "100%")
  val allCategoriesChartLayout = new HorizontalLayoutW(width = "100%")
  val categoryLayout = new HorizontalLayoutW(width = "100%")
  checkBoxCategoryPanel <~ categoryLayout <~ periodLayout
  val yearCombo = new ComboBoxW(caption = "Year")
  val monthCombo = new ComboBoxW(caption = "Month")
  val showYearlyChkBox = new CheckBox("Show yearly", false)
  val showItemsInCategoryBtn = new ButtonW()
  var selectedPieChartCategory:String = _
  val currentCategories:mutable.ListBuffer[String] = mutable.ListBuffer[String]()

  /* Category focused report */
  val categoryFocusedMainLayout = new VerticalLayoutW()
  val allCategoriesCombo = new ComboBoxW(caption = "Categories")
  allCategoriesCombo <~ cats
  val comboCategoryPanel = new PanelW(caption = "Categories", width = "100%")
  val categoryFocusedChartLayout = new VerticalLayoutW(height = "100%", width = "100%")

  showItemsInCategoryBtn.addListener(new ClickListener {
    def buttonClick(event: Button#ClickEvent) {
      val expenseTable = new AddExpenseExpenseTable(selectedPieChartCategory, self, showItemsInCategoryBtn)
      expenseTableLayout removeAllComponents()
      expenseTableLayout <~ expenseTable
    }
  })

  def components = {
    constructAllCategoryLayout()
    constructAllPeriodLayout()
    constructCategoryFocusedLayout()
    allCategoriesMainLayout.setSizeFull()
    allCategoriesMainLayout <~ checkBoxCategoryPanel <~ allCategoriesChartLayout <~ expenseTableLayout
    mainTab.addTab(allCategoriesMainLayout, "All Categories Monthly & Yearly Report")
    mainTab.addTab(categoryFocusedMainLayout, "Category focused Report")
    mainTab
  }

  def constructCategoryFocusedLayout() {
    allCategoriesCombo.addListener(new ValueChangeListener {
      def valueChange(event: ValueChangeEvent) {
        queries.groupedPriceForItemsInCategory(allCategoriesCombo.getValue.asInstanceOf[String]) match {
          case Success(result)  =>
            categoryFocusedChartLayout.removeAllComponents()
            categoryFocusedChartLayout <~ groupedCategoryBarChart(result.toList)
          case Failure(message) => println(message)
        }
      }
    })
    comboCategoryPanel <~ allCategoriesCombo
    categoryFocusedMainLayout <~ comboCategoryPanel <~ categoryFocusedChartLayout
  }

  def constructAllCategoryLayout() {
    cats foreach ( currentCategories += _ )
    categoryLayout <~ cats.map { c =>
      val check = new CheckBoxW(caption = c, immediate = true, selected = true)
      check.addListener(new ValueChangeListener {
        def valueChange(event: ValueChangeEvent) {
          expenseTableLayout removeAllComponents()
          check.getValue.asInstanceOf[Boolean] match {
            case true  => currentCategories += check.getCaption ; updateView()
            case false => currentCategories -= check.getCaption ; updateView()
          }
        }
      })
      check
    }
  }

  def constructAllPeriodLayout() {
    yearCombo <~ years
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
            itemsGroupedByCategories()
          }
          case true  => {
            monthCombo.setEnabled(false)
            yearlyItemsGroupedByCategories()
          }
        }
      }
    })
    
    periodLayout <~ yearCombo <~ monthCombo <~ showYearlyChkBox

  }

  def updateView() {
    valueChange(new ValueChangeEvent {
      def getProperty: Property = null
    })
  }

  def valueChange(event:ValueChangeEvent) {
    if ( yearCombo.getValue != null && monthCombo.getValue != null ) {
      showYearlyChkBox.getValue.asInstanceOf[Boolean] match {
        case true => yearlyItemsGroupedByCategories()
        case false => itemsGroupedByCategories()
      }
    } else {
      println("One of the combo was null!")
    }
  }

  def yearlyItemsGroupedByCategories() {
    queries.yearlyItemsGroupedByCategoriesIn(yearCombo.getValue.toString.toInt, currentCategories) match {
      case Failure(x) => allCategoriesMainLayout.getWindow.showNotification("Error happened: " + x, Notification.TYPE_ERROR_MESSAGE)
      case Success(x) => updateChart(x)
    }
  }

  def itemsGroupedByCategories() {
    queries.itemsGroupedByCategoriesIn(yearCombo.getValue.toString.toInt, months.indexOf(monthCombo.getValue.toString).toString.toInt, currentCategories) match {
      case Failure(x) => allCategoriesMainLayout.getWindow.showNotification("Error happened: " + x, Notification.TYPE_ERROR_MESSAGE)
      case Success(x) => updateChart(x)
    }
  }

  def updateChart(list:List[(String, Double)]) {
    val totalExpense = list.foldLeft(0.0)( (r,c) => r + c._2)
    val bChart = allCategoriesBarChart(list)
    val chart = pieChart (list.map { x => (x._1, if (x._2 != 0.0) df.format(100 * x._2/totalExpense).toDouble else x._2)})
    allCategoriesChartLayout.removeAllComponents()
    allCategoriesChartLayout <~ chart <~ bChart
  }

  def pieChart(list:List[(String, Double)]):VerticalLayoutW = {
    val verticalLayout = new VerticalLayoutW()
    showItemsInCategoryBtn.setVisible(false)

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
    pie.getDataLabel.setEnabled(true)
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

    chart.addListener(new PointSelectListener {
      def pointSelected(p1: InvientCharts#PointSelectEvent) {
        selectedPieChartCategory = p1.getPoint.getName
        showItemsInCategoryBtn.setCaption("Show '" + p1.getPoint.getName + "' Items")
        showItemsInCategoryBtn.setVisible(true)
      }
    })
    verticalLayout <~ chart <~ showItemsInCategoryBtn
    verticalLayout
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
    yAxesSet.add(yAxis);
    chartConfig.setYAxes(yAxesSet);

    chartConfig.setLegend(new Legend(false));

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
    seriesData.setSeriesPoints(expenses);

    chart.addSeries(seriesData);
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
        case "11" => "Dev"
        case _    => "WTF!"
      }
      tokens(0) + " " + month
    }
    list.map(format(_))
  }

  override def decideTheView() {
    expenseTableLayout removeAllComponents()
    expenseTableLayout <~ new AddExpenseExpenseTable(selectedPieChartCategory, this, showItemsInCategoryBtn)
    updateView()
  }

}
