/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectox.html.stream;

import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class ElementFrame {

  final ElementKind elementKind;

  final ElementName elementName;

  private int valueIndex;

  private final int valueLength;

  private final IntSize valueSize;

  ElementFrame(ElementKind elementKind, ElementName elementName, int valueLength, IntSize valueSize) {
    this(elementKind, elementName, 0, valueLength, valueSize);
  }

  ElementFrame(ElementKind elementKind, ElementName elementName, int valueIndex, int valueLength, IntSize valueSize) {
    this.elementKind = elementKind;

    this.elementName = elementName;

    this.valueIndex = valueIndex;

    this.valueLength = valueLength;

    this.valueSize = valueSize;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ElementFrame that
        && elementKind.equals(that.elementKind)
        && elementName.equals(that.elementName)
        && valueIndex == that.valueIndex
        && valueLength == that.valueLength
        && valueSize.equals(that.valueSize);
  }

  public final boolean hasValue() {
    return valueIndex < valueLength;
  }

  public final int nextValue(Ctx ctx) {
    valueIndex += 1;

    return ctx.nextInt(valueSize);
  }

  public final int peekValue(Ctx ctx) {
    return ctx.peekInt(valueSize);
  }

  @Override
  public final String toString() {
    return "ElementFrame(%s, %d, %d, %s)".formatted(elementName, valueIndex, valueLength, valueSize);
  }

}
