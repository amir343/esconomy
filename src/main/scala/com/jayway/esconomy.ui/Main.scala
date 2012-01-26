package com.jayway.esconomy.ui

import com.vaadin.Application
import com.vaadin.terminal.Sizeable
import com.vaadin.ui._

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

  def init() {

    setMainWindow(new Window("Esconomy - Little Economy Application"))
    getMainWindow.setContent(getMainLayout)
    setTheme("runo")
  }

  def getMainLayout = {
    val mainLayout = new VerticalSplitPanel
    mainLayout setHeight "100%"
    mainLayout setWidth "100%"
    mainLayout.setSplitPosition(100, Sizeable.UNITS_PIXELS)

    mainLayout addComponent getTopLayout
    mainLayout addComponent getBottomLayout
    mainLayout
  }

  def getTopLayout = {
    val title = new Label("Welcome to Esconomy !")
    title
  }

  def getBottomLayout = {
    val bottomLayout = new HorizontalSplitPanel
    bottomLayout setHeight "100%"
    bottomLayout setWidth "100%"
    bottomLayout.setSplitPosition(10)
    
    bottomLayout addComponent getNavigationMenu
    
    bottomLayout
  }
  
  def getNavigationMenu = {
    val nav = new VerticalLayout
    nav setSpacing true
    
    val tree = new Tree
    val myEconomy = MyEconomy()
    val addExp = AddExpense()
    val reports = Reports()
    val settings = Settings()
    val addCategory = AddCategory()
    val items = List(myEconomy, addExp, reports, settings, addCategory)
    
    items foreach { tree addItem _ }
    items foreach { x => tree.setItemCaption(x, x.title)}
    items.filter( ! _.hasChildren ).foreach( tree.setChildrenAllowed(_, false))

    tree.setParent(addExp, myEconomy)
    tree.setParent(addCategory, settings)

    items.filter( _.hasChildren ).foreach( tree.expandItemsRecursively(_) )
    
    nav addComponent tree
    nav
  }


}


