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
package objectox.html.play;

import objectox.html.IntSize;
import objectox.html.rec.HtmlBytes;

final class NextElement {

  private final HtmlSource source;

  private final int startIndex;

  private final IntSize nameSize;

  NextElement(HtmlSource source, int startIndex, IntSize nameSize) {
    this.source = source;

    this.startIndex = startIndex;

    this.nameSize = nameSize;
  }

  public final State compute(Frame frame) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final int endIndex() {
    final int valuesIndex;
    valuesIndex = nameSize.skip(startIndex + 1);

    final byte valuesByte;
    valuesByte = source.getByte(valuesIndex);

    return switch (valuesByte) {
      case HtmlBytes.VALUES0 -> throw new UnsupportedOperationException("Implement me");

      case HtmlBytes.VALUES8 -> IntSize.BIT8;

      case HtmlBytes.VALUES16 -> IntSize.BIT16;

      case HtmlBytes.VALUES24 -> IntSize.BIT24;

      default -> throw new UnsupportedOperationException(
          "Implement me :: " + valuesByte
      );
    };

    final int length;
    length = source.getInt(index, valueSize);

    index = valueSize.skip(index);

    index += length * valueSize.skip(0);

    index = nameSize.skip(index + 1);

    return index;
  }

}
