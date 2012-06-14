package com.jayway.esconomy.ui

import com.vaadin.data.Property.{ValueChangeEvent, ValueChangeListener}
import scalaz.{Failure, Success}
import wrapped.{PanelW, ComboBoxW, VerticalLayoutW}
import com.jayway.esconomy.dao.QueryChannelImpl

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
class CategoryFocusedTabView(chartConstructors:ChartConstructors) {

  val queries = new QueryChannelImpl

  lazy val cats:List[String] = queries.allCategories match {
    case Success(x) => x map ( _.category )
    case Failure(x) => List()
  }

  val categoryFocusedMainLayout = new VerticalLayoutW()
  val allCategoriesCombo = new ComboBoxW(caption = "Categories")
  allCategoriesCombo <~ cats
  val comboCategoryPanel = new PanelW(caption = "Categories", width = "100%")
  val categoryFocusedChartLayout = new VerticalLayoutW(height = "100%", width = "100%")


  def getComponents = categoryFocusedMainLayout

  def constructCategoryFocusedLayout() {
    allCategoriesCombo.addListener(new ValueChangeListener {
      def valueChange(event: ValueChangeEvent) {
        queries.groupedPriceForItemsInCategory(allCategoriesCombo.getValue.asInstanceOf[String]) match {
          case Success(result)  =>
            categoryFocusedChartLayout.removeAllComponents()
            categoryFocusedChartLayout <~ chartConstructors.groupedCategoryBarChart(result.toList)
          case Failure(message) => println(message)
        }
      }
    })
    comboCategoryPanel <~ allCategoriesCombo
    categoryFocusedMainLayout <~ comboCategoryPanel <~ categoryFocusedChartLayout
  }


}
