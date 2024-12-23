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

package org.archive.webservices.archivespark.model

import org.archive.webservices.archivespark.model.pointers.{DependentFieldPointer, FieldPointer}

abstract class BoundEnrichFunc[Root <: EnrichRoot, Source, DefaultValue] (bound: DependentFieldPointer[Root, Source]) extends EnrichFunc[Root, Source, DefaultValue] {
  def source: DependentFieldPointer[Root, Source] = bound

  override def on[DependencyRoot <: EnrichRoot, S <: Source](dependency: FieldPointer[DependencyRoot, S]): EnrichFunc[DependencyRoot, Source, DefaultValue] = {
    val self = this
    val boundOn = new DependentFieldPointer[DependencyRoot, Source](bound.func.asInstanceOf[EnrichFunc[Root, Source, _]].on(dependency), bound.fieldName)
    new BoundEnrichFunc[DependencyRoot, Source, DefaultValue](boundOn) {
      override def fields: Seq[String] = self.fields
      override def defaultField: String = self.defaultField
      override def isTransparent: Boolean = self.isTransparent
      override def derive(source: TypedEnrichable[Source], derivatives: Derivatives): Unit = self.derive(source, derivatives)
      override def enrichPartition[R <: EnrichRoot](partition: Iterator[R], func: EnrichFunc[R, _, _]): Iterator[R] = self.enrichPartition(partition, func)
      override def initPartition(partition: Iterator[EnrichRoot]): Iterator[EnrichRoot] = self.initPartition(partition)
      override def cleanup(): Unit = self.cleanup()
    }
  }
}