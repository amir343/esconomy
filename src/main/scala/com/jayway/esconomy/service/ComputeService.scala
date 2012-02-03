package com.jayway.esconomy.service

import java.util.concurrent.Executors

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

object ComputeService {

  // TODO shutdown pool when the application closes

  private[this] val threadNum = 10
  private[this] val executor = Executors.newFixedThreadPool(threadNum)

  def run(computation: => Unit) = {
    val runnable = new Runnable {
      def run() {
        computation
      }
    }
    executor execute runnable
  }

}
