package com.jayway.esconomy.ui

import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Upload._
import java.io.{FileOutputStream, File, OutputStream}
import com.jayway.esconomy.service.ParseImportedFile
import com.vaadin.ui.{Table, Button, Upload}
import java.util.UUID
import wrapped._
import collection.JavaConversions._
import com.jayway.esconomy.dao.{Commands, Queries}
import java.text.SimpleDateFormat
import com.jayway.esconomy.domain.Item
import com.vaadin.ui.Window.Notification
import com.jayway.esconomy.util.Utils._


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

  val uploadLayout = new HorizontalLayoutW()
  val tableLayout = new VerticalLayoutW()
  val mainLayout = new VerticalLayoutW()
  val mainPanel = new PanelW(caption = "Import", width = "100%", height = "100%")

  val queries = new Queries
  val categories = queries.allCategories.right.get.map { x => x.category }
  var file:File = _
  val upload = new Upload(null, this)
  val cancelBtn = new Button("Cancel")
  val saveBtn = new Button("Save")
  val progressIndicator = new ProgressIndicatorW(visible = false)

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
        updateTable(ParseImportedFile(file).items)
      }
    })

    uploadLayout <~ List(upload, progressIndicator)
  }

  override def receiveUpload(fileName:String, mimeType:String):OutputStream = {
    file = new File("/tmp/" + fileName)
    val fos = new FileOutputStream(file)
    fos
  }

  def updateTable(list:List[(String, String, String)]) {
    val table = new Table()
    table.setWidth("100%")
    table.addContainerProperty("Date", classOf[String],  null)
    table.addContainerProperty("ItemName", classOf[String], null)
    table.addContainerProperty("Price", classOf[String],  null)
    table.addContainerProperty("Category", classOf[ComboBoxW], null)

    var itemIds = List[String]()

    list.foreach { x =>
      val cats = new ComboBoxW()
      categories.foreach { i => cats.addItem(i) }
      val itemId = UUID.randomUUID().toString
      itemIds = itemIds ::: List(itemId)
      table.addItem(Array(x._1, x._2, x._3, cats), itemId)
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
