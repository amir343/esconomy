package com.jayway.esconomy.service

import org.apache.commons.io.FileUtils
import collection.JavaConverters._
import java.io.File
import scalaz._
import Scalaz._
import com.jayway.esconomy.util.Utils.ItemTuple

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

case class ParseImportedFile(file:File) {

  val parsedItems:Validation[String, List[ItemTuple]] =
    try {
      FileUtils.readLines(file, "ISO8859_1").asScala.toList.foldLeft(List[ItemTuple]()) {
      (r, c) =>
        val tokens = c.split("\t")
        r ::: List((tokens.apply(0), tokens.apply(1), tokens.apply(2).replace("\"", "").replace(",", "")))
      }.success
    } catch { case e:Exception => "Could not parse the file: %s".format(e.getLocalizedMessage).fail }
  
  def items = parsedItems

}
