package com.jayway.esconomy.ui.wrapped

import com.vaadin.ui.{ComboBox, Panel, HorizontalLayout, VerticalLayout}
import com.vaadin.ui.AbstractSelect.Filtering
import com.vaadin.ui.AbstractOrderedLayout._


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
  setImmediate(immediate)
  setMargin(margin)
  setSpacing(spacing)
  if (width != null) setWidth(width)
  if (height!= null) setHeight(height)
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
}

class PanelW(caption:String = "",
             immediate:Boolean = true,
             height:String = "100%",
             width:String = "100%" ) extends Panel {
  setCaption(caption)
  setImmediate(immediate)
  if (width != null) setWidth(width)
  if (height!= null) setHeight(height)
}

class ComboBoxW(immediate:Boolean = true,
                filteringMode:Int = Filtering.FILTERINGMODE_STARTSWITH,
                nullSelectionAllowed:Boolean = false) extends ComboBox {
  setFilteringMode(filteringMode)
  setImmediate(immediate)
  setNullSelectionAllowed(nullSelectionAllowed)
}
