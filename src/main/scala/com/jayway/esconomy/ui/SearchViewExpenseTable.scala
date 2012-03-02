package com.jayway.esconomy.ui

import com.vaadin.ui.Component
import com.vaadin.ui.Window.Notification
import scalaz.{Success, Failure}

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

class SearchViewExpenseTable(view:View, component:Component)
  extends ExpenseTable(view, component) {

  override def allItems(filter:String) {
    self removeAllItems()
    dataSource removeAllItems()

    queries.find(filter) match {
      case Failure(x) => component.getWindow.showNotification("Error", x, Notification.TYPE_ERROR_MESSAGE)
      case Success(x) => x.foreach ( addToContainer(_, dataSource) )
    }

  }

}
