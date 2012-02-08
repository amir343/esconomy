package com.jayway.esconomy.ui.wrapped

import com.vaadin.ui.AbstractOrderedLayout._
import com.vaadin.ui._
import com.vaadin.ui.Panel._
import com.vaadin.ui.AbstractSelect._


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

class VerticalLayoutW(immediate:Boolean = true,
                      height:String = null,
                      width:String = null,
                      spacing:Boolean = true,
                      margin:Boolean = true) extends VerticalLayout {
  type T = VerticalLayoutW
  setImmediate(immediate)
  setMargin(margin)
  setSpacing(spacing)
  if (width != null) setWidth(width)
  if (height!= null) setHeight(height)

  def <~(list:List[Component]) = list foreach { addComponent(_) }
  def <~(c:Component) = addComponent(c)

}

class HorizontalLayoutW(immediate:Boolean = true,
                        height:String = null,
                        width:String = null,
                        spacing:Boolean = true,
                        margin:Boolean = true) extends HorizontalLayout {
  setImmediate(immediate)
  setMargin(margin)
  setSpacing(spacing)
  if (width != null) setWidth(width)
  if (height!= null) setHeight(height)

  def <~(list:List[Component]) = list foreach { addComponent(_) }
  def <~(c:Component) = addComponent(c)

}

class PanelW(caption:String = "",
             immediate:Boolean = true,
             height:String = "100%",
             width:String = "100%" ) extends Panel {
  setCaption(caption)
  setImmediate(immediate)
  if (width != null) setWidth(width)
  if (height!= null) setHeight(height)

  def <~(list:List[Component]) = list foreach { addComponent(_) }
  def <~(c:Component) = addComponent(c)

}

class ComboBoxW(caption:String = null,
                immediate:Boolean = true,
                filteringMode:Int = Filtering.FILTERINGMODE_STARTSWITH,
                nullSelectionAllowed:Boolean = false) extends ComboBox {
  if (caption != null) setCaption(caption)
  setFilteringMode(filteringMode)
  setImmediate(immediate)
  setNullSelectionAllowed(nullSelectionAllowed)

  def <~(list:List[Any]) = list foreach { addItem(_) }
  def <~(item:Any) = addItem(item)

}

class WindowW(caption:String = null,
              width:String = null,
              height:String = null,
              modal:Boolean = true,
              resizable:Boolean = false) extends Window {
  setCaption(caption)
  if (height != null) setHeight(height)
  if (width != null) setWidth(width)
  setResizable(resizable)
  setModal(modal)
}

class ProgressIndicatorW(immediate:Boolean = true,
                         visible:Boolean = true) extends ProgressIndicator {
  setImmediate(immediate)
  setVisible(visible)
}

class GridLayoutW(columns:Int,
                  rows:Int,
                  immediate:Boolean = true,
                  spacing:Boolean = true,
                  height:String = null,
                  width:String = null
                  ) extends GridLayout(columns, rows) {
  setSpacing(spacing)
  setImmediate(immediate)
  if (height != null) setHeight(height)
  if (width != null) setWidth(width)
  def <~(list:List[Component]) = list foreach { addComponent(_) }
  def <~(component:Component) = addComponent(component)
}
