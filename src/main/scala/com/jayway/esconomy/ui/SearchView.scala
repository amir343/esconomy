package com.jayway.esconomy.ui

import com.vaadin.ui.{Button, TextField}
import com.vaadin.ui.Button.ClickListener
import wrapped.{ButtonW, HorizontalLayoutW, VerticalLayoutW}
import com.vaadin.event.ShortcutAction.KeyCode


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

case class SearchView(dashboard:Main) extends View {

  val mainLayout = new VerticalLayoutW()
  val searchLayout = new VerticalLayoutW()
  val searchTxt = new TextField("Keyword")
  searchTxt focus()
  val searchBtn = new ButtonW("Search")
  searchBtn setClickShortcut KeyCode.ENTER
  val expenseTable = new SearchViewExpenseTable(this, dashboard.cssLayout)

  searchBtn.addListener(new ClickListener {
    def buttonClick(event: Button#ClickEvent) {
      expenseTable.allItems(searchTxt.getValue.asInstanceOf[String])
    }
  })

  def getComponents = {
    constructSearchLayout()
    mainLayout <~ searchLayout <~ expenseTable
    mainLayout
  }

  def constructSearchLayout() {
    searchLayout <~ searchTxt <~ searchBtn
  }

  override def decideTheView() {

  }

}
