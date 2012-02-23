package com.jayway.esconomy.ui

import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Upload._
import java.io.{FileOutputStream, File, OutputStream}
import java.util.UUID
import wrapped._
import collection.JavaConversions._
import com.jayway.esconomy.dao.{Commands, QueryChannelImpl}
import com.jayway.esconomy.domain.Item
import com.vaadin.ui.Window.Notification
import com.jayway.esconomy.util.Utils._
import com.vaadin.event.Action
import com.vaadin.event.Action.Handler
import scalaz.{Success, Failure}
import com.vaadin.ui.{Label, Table, Button, Upload}
import com.jayway.esconomy.util.Utils.ItemTuple
import com.jayway.esconomy.service.{CategoryGuessService, ParseImportedFile}

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

case class ImportView(dashboard:Main) extends Receiver {

  val uploadLayout = new VerticalLayoutW()
  val tableLayout = new VerticalLayoutW()
  val mainLayout = new VerticalLayoutW()
  val mainPanel = new PanelW(caption = "Import", width = "100%", height = "100%")
  val label = new Label("Please upload your expense tab delimited file contaning {date, item name, price}")

  val queries = new QueryChannelImpl
  lazy val categories = queries.allCategories match {
    case Failure(x)  => {
      upload.getWindow.showNotification("Error happened: " + x, Notification.TYPE_ERROR_MESSAGE)
      List()
    }
    case Success(x) => x.map ( _.category )
  }
  var file:File = _
  val upload = new Upload(null, this)
  val cancelBtn = new Button("Cancel")
  val saveBtn = new Button("Save")
  val progressIndicator = new ProgressIndicatorW(visible = false)
  val removeAction = new Action("Remove")

  def getComponents = {
    constructUploadLayout()
    mainLayout <~ List(uploadLayout, tableLayout)
    mainPanel <~ mainLayout
    mainPanel
  }

  def constructUploadLayout()  {
    upload.setButtonCaption("Upload file")
    upload.setImmediate(true)

    cancelBtn.addListener( new ClickListener {
      def buttonClick(event: Button#ClickEvent) {
        upload.interruptUpload()
      }
    })
    
    upload.addListener(new StartedListener {
      def uploadStarted(event: StartedEvent) {
        progressIndicator.setVisible(true)
        upload.setVisible(false)
        progressIndicator.setValue(0f)
        progressIndicator.setPollingInterval(500)
      }
    })

    upload.addListener(new ProgressListener {
      def updateProgress(readBytes: Long, contentLength: Long) {
        progressIndicator.setValue(readBytes.asInstanceOf[Float] / contentLength.asInstanceOf[Float]);
      }
    })

    upload.addListener(new FinishedListener {
      def uploadFinished(event: FinishedEvent) {
        progressIndicator.setVisible(false)
        upload.setVisible(true)
        ParseImportedFile(file).items match {
          case Success(x) => updateTable(x)
          case Failure(x) => upload.getWindow.showNotification(x, Notification.TYPE_ERROR_MESSAGE)
        }
      }
    })

    uploadLayout <~ List(label, upload, progressIndicator)
  }

  override def receiveUpload(fileName:String, mimeType:String):OutputStream = {
    file = new File("/tmp/" + fileName)
    val fos = new FileOutputStream(file)
    fos
  }

  def updateTable(list:List[ItemTuple]) {
    val table = new Table()
    table.setWidth("100%")
    table.addContainerProperty("Date", classOf[String],  null)
    table.addContainerProperty("ItemName", classOf[String], null)
    table.addContainerProperty("Price", classOf[String],  null)
    table.addContainerProperty("Category", classOf[ComboBoxW], null)
    table.setSelectable(true)

    table addActionHandler { new Handler {
      def getActions(target: AnyRef, sender: AnyRef): Array[Action] = Array(removeAction)

      def handleAction(action: Action, sender: AnyRef, target: AnyRef) {
        action match {
          case `removeAction` => table.removeItem(target)
        }
      }
    }}

    var itemIds = List[String]()

    val estimatedList = CategoryGuessService().estimateForItems(list)

    estimatedList.foreach { x =>
      val cats = new ComboBoxW()
      categories.foreach { i => cats.addItem(i) }
      val itemId = UUID.randomUUID().toString
      itemIds = itemIds ::: List(itemId)
      x._2 match {
        case null =>
        case category    => cats.setValue(category)
      }
      table.addItem(Array(x._1._1, x._1._2, x._1._3, cats), itemId)
    }
    
    saveBtn.addListener(new ClickListener {
      def buttonClick(event: Button#ClickEvent) {
        val commands = new Commands
        itemIds.foreach { id =>
          val item = table.getItem(id)
          val price = item.getItemProperty("Price").getValue.asInstanceOf[String].toDouble
          if ( price < 0.0 ) {
            val date = getDate(item.getItemProperty("Date").getValue.asInstanceOf[String])
            val itName = item.getItemProperty("ItemName").getValue.asInstanceOf[String]
            val category = item.getItemProperty("Category").getValue.asInstanceOf[ComboBoxW].getValue match {
              case null => UNKNOWN_CATEGORY
              case a:String => a
            }
            commands.saveItem(Item(id, itName, -1*price, date, category))
          }
        }
        upload.getWindow.showNotification("Items saved!", Notification.TYPE_HUMANIZED_MESSAGE)
        tableLayout removeAllComponents()
      }
    })
    
    tableLayout <~ List(table, saveBtn)
  }

 
}
