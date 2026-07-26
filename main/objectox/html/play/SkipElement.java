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

final class SkipElement {

  private final HtmlSource source;

  private final int startIndex;

  private final IntSize nameSize;

  SkipElement(HtmlSource source, int startIndex, IntSize nameSize) {
    this.source = source;

    this.startIndex = startIndex;

    this.nameSize = nameSize;
  }

  public final int compute() {
    final int nameIndex;
    nameIndex = startIndex + 1;

    final int valuesIndex;
    valuesIndex = nameSize.skip(nameIndex);

    final byte valuesByte;
    valuesByte = source.getByte(valuesIndex);

    return switch (valuesByte) {
      case HtmlBytes.VALUES8 -> compute0(valuesIndex, IntSize.BIT8);

      case HtmlBytes.VALUES16 -> compute0(valuesIndex, IntSize.BIT16);

      case HtmlBytes.VALUES24 -> compute0(valuesIndex, IntSize.BIT24);

      default -> throw new UnsupportedOperationException(
          "Implement me :: " + valuesByte
      );
    };
  }

  private int compute0(int valuesIndex, IntSize valuesSize) {
    final int lengthIndex;
    lengthIndex = valuesIndex + 1;

    final int length;
    length = source.getInt(lengthIndex, valuesSize);

    final int offset;
    offset = length * valuesSize.bytes();

    final int firstIndex;
    firstIndex = valuesSize.skip(lengthIndex);

    final int afterValuesIndex;
    afterValuesIndex = firstIndex + offset;

    final byte endTag;
    endTag = source.getByte(afterValuesIndex);

    return switch (endTag) {
      case HtmlBytes.VOIDELEM -> afterValuesIndex;

      case HtmlBytes.ENDTAG8 -> afterValuesIndex + 1 + 1;

      default -> throw new UnsupportedOperationException(
          "Implement me :: " + endTag
      );
    };
  }

}
