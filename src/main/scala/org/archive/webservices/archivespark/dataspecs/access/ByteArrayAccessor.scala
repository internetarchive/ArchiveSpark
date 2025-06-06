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

package org.archive.webservices.archivespark.dataspecs.access

import java.io.InputStream
import java.util.zip.GZIPInputStream

import org.archive.webservices.sparkling.io.ByteArray

class ByteArrayAccessor(bytes: ByteArray, gz: Boolean = false) extends CloseableDataAccessor[InputStream] {
  def this(bytes: Array[Byte], gz: Boolean) = this({
    val array = new ByteArray()
    array.append(bytes)
    array
  }, gz)

  def this(bytes: Array[Byte]) = this(bytes, false)

  override def get: Option[InputStream] = {
    var stream: InputStream = null
    try {
      stream = bytes.toInputStream
      stream = if (gz) new GZIPInputStream(stream) else stream
      Some(stream)
    } catch {
      case e: Exception =>
        e.printStackTrace()
        if (stream != null) stream.close()
        None
    }
  }
}