package com.jayway.esconomy.ui

import com.vaadin.Application
import com.vaadin.terminal.{ThemeResource, Sizeable}
import com.vaadin.ui._
import wrapped.{HorizontalLayoutW, VerticalLayoutW}

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

class Main extends Application {

  val items = List(MyEconomy(), AddExpense(), Reports(), AddCategory(), Import())
  val centerLayout = new VerticalLayoutW( width = "100%", height = "100%")
  val cssLayout = new CssLayout

  def init() {
    setMainWindow(new Window("Esconomy - Your Little Economy Application"))
    getMainWindow.setContent(mainLayout)
    setTheme("esconomy")
  }

  def mainLayout = {
    val mainLayout = new VerticalSplitPanel
    mainLayout setHeight "100%"
    mainLayout setWidth "100%"
    mainLayout setSplitPosition (145, Sizeable.UNITS_PIXELS)
    mainLayout setMargin true
    mainLayout setLocked true

    mainLayout addComponent topLayout
    mainLayout addComponent bottomLayout
    mainLayout
  }

  def topLayout = {
    val topLayout = new HorizontalLayoutW
    val logo = new Embedded(null, new ThemeResource("img/logo.png"))
    val title = new Label("<h1>Welcome to Esconomy!</h1>")
    title setContentMode Label.CONTENT_XHTML
    topLayout addComponent logo
    topLayout addComponent title
    topLayout
  }

  def bottomLayout = {
    val bottomLayout = new HorizontalSplitPanel
    bottomLayout setHeight "100%"
    bottomLayout setWidth "100%"
    bottomLayout setSplitPosition 10
    bottomLayout setMargin true
    
    bottomLayout addComponent navigationMenu
    bottomLayout addComponent getCenterLayout
    
    bottomLayout
  }

  def getCenterLayout = {
    centerLayout
  }
  
  def navigationMenu = {
    val nav = new VerticalLayoutW(margin = false)

    cssLayout.setSizeFull()
    cssLayout.setStyleName("sidebar-menu")

    items foreach { i =>
      i.hasChildren match {
        case true  => cssLayout.addComponent(new Label(i.title))
        case false => {
          val item = new NativeButton(i.title)
          item.setData(i)
          item.addListener(MenuItemClickListener(this))
          cssLayout.addComponent(item)
        }
      }
    }
    
    nav addComponent cssLayout
    nav
  }
  
  def switchToAddExpenseView() {
    getCenterLayout.removeAllComponents()
    getCenterLayout <~ AddExpenseView(this).components
  }

  def switchToAddCategoryView() {
    getCenterLayout.removeAllComponents()
    getCenterLayout <~ CategoryView(this).components
  }

  def switchToReportView() {
    getCenterLayout.removeAllComponents()
    getCenterLayout <~ ReportView(this).components
  }

  def switchToImportView() {
    getCenterLayout.removeAllComponents()
    getCenterLayout <~ ImportView(this).getComponents
  }


}


