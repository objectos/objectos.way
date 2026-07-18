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
package objectox.html.rec;

import objectox.html.HtmlByteProto;

final class FlattenEncoder {

  private final ByteArray main;

  FlattenEncoder(ByteArray main) {
    this.main = main;
  }

  public final int encode(final int startIndex) {
    int index;
    index = startIndex;

    // mark this fragment
    main.set(index++, HtmlByteProto.LENGTH2);

    // point to next element
    final int offset;
    offset = main.getInt16(index++);

    index++;

    final int maxIndex;
    maxIndex = index + offset;

    while (index < maxIndex) {
      final byte proto;
      proto = main.get(index);

      if (proto == HtmlByteProto.END) {
        break;
      }

      switch (proto) {
        case HtmlByteProto.ATTRIBUTE_EXT0 -> index = main.addBytes(main, index, 2);

        case HtmlByteProto.ATTRIBUTE_EXT1 -> index = main.addBytes(main, index, 4);

        case HtmlByteProto.AMBIGUOUS1,
             HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1,
             HtmlByteProto.ELEMENT,
             HtmlByteProto.TEXT,
             HtmlByteProto.RAW -> {
          index++;

          final int endIndex;
          endIndex = main.varIntLEEndIndex(index);

          final int len;
          len = main.getVarIntLE(index, endIndex);

          final int elementIndex;
          elementIndex = index - len;

          main.add(proto);

          final int length;
          length = main.size() - elementIndex;

          main.addVarIntLE(length);

          index = endIndex;
        }

        default -> {
          throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
          );
        }
      }
    }

    return maxIndex;
  }

}
