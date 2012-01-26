package com.jayway.esconomy

import com.vaadin.Application
import com.vaadin.ui.{Label, VerticalLayout, Window}

/**
 * Created by IntelliJ IDEA.
 * User: amir
 * Date: 1/26/12
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */

class Main extends Application {

  def init():Unit = {

    setMainWindow(new Window("Esconomy - Little Economy Application"))

    val mainLayout = new VerticalLayout

    val header = new Label("Welcone to this little application!")

    mainLayout.addComponent(header)
    
    getMainWindow.setContent(mainLayout)

    setTheme("runo")
  }
}