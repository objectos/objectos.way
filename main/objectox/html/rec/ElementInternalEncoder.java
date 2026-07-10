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

final class ElementInternalEncoder {

  private final ByteArray main;

  ElementInternalEncoder(ByteArray main) {
    this.main = main;
  }

  public final int encode(final int startIndex) {
    int index;
    index = startIndex;

    while (true) {
      final byte proto;
      proto = main.get(index);

      switch (proto) {
        case HtmlByteProto.ATTRIBUTE0 -> {
          return encodeInternal(index, proto, 3, HtmlByteProto.MARKED3);
        }

        case HtmlByteProto.CUSTOM_ATTR0,
             HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> {
          return encodeInternal(index, proto, 4, HtmlByteProto.MARKED4);
        }

        case HtmlByteProto.AMBIGUOUS1,
             HtmlByteProto.ATTRIBUTE1 -> {
          return encodeInternal(index, proto, 5, HtmlByteProto.MARKED5);
        }

        case HtmlByteProto.CUSTOM_ATTR1 -> {
          return encodeInternal(index, proto, 6, HtmlByteProto.MARKED6);
        }

        default -> {
          throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
          );
        }
      }
    }
  }

  private int encodeInternal(final int startIndex, byte proto, int offset, byte marked) {
    main.set(startIndex, marked);

    main.add(proto);

    final int length;
    length = main.size() - startIndex;

    HtmlBytes.encodeOffset(main, length);

    return startIndex + offset;
  }

}
