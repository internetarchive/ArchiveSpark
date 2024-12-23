/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2024 Helge Holzmann (Internet Archive) <helge@archive.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.archive.webservices.archivespark.implicits

import org.apache.spark.rdd.RDD
import org.archive.webservices.sparkling.util.RddUtil
import org.archive.webservices.archivespark.util.JsonConvertible
import org.archive.webservices.archivespark.util.JsonConvertible

import scala.reflect.ClassTag

class JsonConvertibleRDD[Record <: JsonConvertible : ClassTag](rdd: RDD[Record]) {
  def toJson: RDD[Map[String, Any]] = rdd.map(r => r.toJson)

  def toJsonStrings: RDD[String] = rdd.map(r => r.toJsonString)
  def toJsonStrings(pretty: Boolean = true): RDD[String] = rdd.map(r => r.toJsonString(pretty))

  def saveAsJson(path: String): Unit = RddUtil.saveAsTextFile(toJsonStrings(pretty = false), path)

  def peekJson: String = rdd.peek.toJsonString
  def peekJson(index: Int): String = rdd.peek(index).toJsonString
}
