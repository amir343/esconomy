package com.jayway.esconomy.db

import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import com.mongodb.Mongo
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.context.annotation.{AnnotationConfigApplicationContext, Configuration}
import org.springframework.context.ApplicationContext

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
@Configuration
class MongoConfig extends AbstractMongoConfiguration {

  def getDatabaseName:String = "mydb"
  def mongo:Mongo = new Mongo("localhost")

}

object MongoOps {
  val ctx: ApplicationContext = new AnnotationConfigApplicationContext(classOf[MongoConfig])
  val mongoOperations: MongoOperations = ctx.getBean("mongoTemplate").asInstanceOf[MongoOperations]
  val categoryCollection = "categories"
  val itemCollection = "items"
}