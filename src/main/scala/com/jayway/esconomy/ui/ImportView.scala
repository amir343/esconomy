package com.jayway.esconomy.ui

import wrapped.{ProgressIndicatorW, PanelW, HorizontalLayoutW, VerticalLayoutW}
import com.vaadin.ui.{Button, Upload}
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Upload._
import java.io.{FileOutputStream, File, OutputStream}


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

  val upload = new Upload(null, this)
  val cancelBtn = new Button("Cancel")
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
      }
    })

    uploadLayout <~ List(upload, progressIndicator)
  }

  override def receiveUpload(fileName:String, mimeType:String):OutputStream = {
    val file = new File("/tmp/" + fileName)
    val fos = new FileOutputStream(file)
    fos
  }

  


}
