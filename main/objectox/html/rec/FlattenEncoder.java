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

import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;

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

    // decode the length
    final byte len0;
    len0 = main.get(index++);

    final byte len1;
    len1 = main.get(index++);

    // point to next element
    final int offset;
    offset = HtmlBytes.decodeInt(len0, len1);

    final int maxIndex;
    maxIndex = index + offset;

    while (index < maxIndex) {
      final byte proto;
      proto = main.get(index++);

      if (proto == HtmlByteProto.END) {
        break;
      }

      switch (proto) {
        case HtmlByteProto.ATTRIBUTE_EXT0 -> {
          byte idx0;
          idx0 = main.get(index++);

          main.add(proto, idx0);
        }

        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          byte idx0;
          idx0 = main.get(index++);

          byte idx1;
          idx1 = main.get(index++);

          byte idx2;
          idx2 = main.get(index++);

          main.add(proto, idx0, idx1, idx2);
        }

        case HtmlByteProto.AMBIGUOUS1,
             HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1,
             HtmlByteProto.ELEMENT,
             HtmlByteProto.TEXT,
             HtmlByteProto.RAW -> {
          int elementIndex;
          elementIndex = index;

          int aux;

          do {
            aux = main.get(index++);
          } while (aux < 0);

          int len;
          len = HtmlBytes.decodeOffset(main, elementIndex, index);

          elementIndex -= len;

          main.add(proto);

          final int length;
          length = main.size() - elementIndex;

          HtmlBytes.encodeOffset(main, length);
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
