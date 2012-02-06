package com.jayway.esconomy.service

import com.vaadin.terminal.gwt.server.ApplicationServlet
import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.ui.Window;
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

class EsconomyAppServlet extends ApplicationServlet {

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  protected override def writeAjaxPageHtmlVaadinScripts(window:Window,
                                               themeName:String,
                                               application:Application,
                                               page:BufferedWriter,
                                               appUrl:String,
                                               themeUri:String,
                                               appId:String,
                                               request:HttpServletRequest) {
    page.write("<script type=\"text/javascript\">\n");
    page.write("//<![CDATA[\n");
    page.write("document.write(\"<script language='javascript' src='./VAADIN/jquery/jquery-1.4.4.min.js'><\\/script>\");\n");
    page.write("document.write(\"<script language='javascript' src='./VAADIN/js/highcharts.js'><\\/script>\");\n");
    page.write("document.write(\"<script language='javascript' src='./VAADIN/js/modules/exporting.js'><\\/script>\");\n");
    page.write("//]]>\n</script>\n");
    super.writeAjaxPageHtmlVaadinScripts(window, themeName, application,
      page, appUrl, themeUri, appId, request);
  }

}
