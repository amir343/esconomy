package com.jayway.esconomy.ui

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

case class CategoryView(dashboard:Main) {

  val label = new Label("Category")
  val categories = new TextField()
  val addBtn = new Button("Add")

  def getComponents() = {
    val panel = new Panel("Categories")
    val verticalLayout = new VerticalLayout
    verticalLayout setWidth "100%"
    verticalLayout setImmediate true
    verticalLayout setSpacing true
    verticalLayout setMargin true
    panel setImmediate true
    verticalLayout.addComponent(getAddCategoryLayout())
    panel.addComponent(verticalLayout)
    panel
  }
  
  def getAddCategoryLayout() = {
    val hori = new HorizontalLayout
    hori.setImmediate(true)
    hori.setMargin(true)
    hori.setSpacing(true)
    List(label, categories, addBtn).foreach { hori.addComponent(_) }
    hori
  }

}