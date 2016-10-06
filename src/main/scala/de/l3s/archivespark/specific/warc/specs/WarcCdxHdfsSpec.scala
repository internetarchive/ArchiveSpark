/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Helge Holzmann (L3S) and Vinay Goel (Internet Archive)
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

package de.l3s.archivespark.specific.warc.specs

import de.l3s.archivespark.specific.warc.{CdxRecord, WarcRecord}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.util.Try

class WarcCdxHdfsSpec private (@transient cdx: RDD[CdxRecord], warcPath: String) extends WarcHdfsSpecBase[CdxRecord] {
  val warcPathMap = filePathMap(warcPath)

  override def load(sc: SparkContext, minPartitions: Int): RDD[CdxRecord] = cdx

  override def parse(cdx: CdxRecord): Option[WarcRecord] = {
    Try{cdx.additionalFields(1)}.toOption.flatMap(warcPathMap.dir.get).flatMap(dir => parse(cdx, dir))
  }
}

object WarcCdxHdfsSpec {
  def apply(cdx: RDD[CdxRecord], warcPath: String) = new WarcCdxHdfsSpec(cdx, warcPath)
}