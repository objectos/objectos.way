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

final class ElementValueEncoder {

  private final ByteArray aux;

  private final int auxStart;

  private final ByteArray main;

  private final int mainContents;

  ElementValueEncoder(ByteArray aux, int auxStart, ByteArray main, int mainContents) {
    this.aux = aux;

    this.auxStart = auxStart;

    this.main = main;

    this.mainContents = mainContents;
  }

  public final void encode() {
    // we iterate over each value added via elementValue(Instruction)
    int index;
    index = auxStart;

    int indexMax;
    indexMax = aux.size();

    int contents;
    contents = mainContents;

    loop: while (index < indexMax) {
      final byte mark;
      mark = aux.get(index++);

      switch (mark) {
        case HtmlByteProto.TEXT -> {
          main.add(mark, aux.get(index++), aux.get(index++));
        }

        case HtmlByteProto.ATTRIBUTE_EXT0 -> {
          main.add(mark, aux.get(index++));
        }

        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          main.add(mark, aux.get(index++), aux.get(index++), aux.get(index++));
        }

        case HtmlByteProto.INTERNAL -> {
          while (true) {
            byte proto;
            proto = main.get(contents);

            switch (proto) {
              case HtmlByteProto.ATTRIBUTE0 -> {
                contents = encodeInternal3(contents, proto);

                continue loop;
              }

              case HtmlByteProto.CUSTOM_ATTR0 -> {
                contents = encodeInternal4(contents, proto);

                continue loop;
              }

              case HtmlByteProto.AMBIGUOUS1,
                   HtmlByteProto.ATTRIBUTE1 -> {
                contents = encodeInternal5(contents, proto);

                continue loop;
              }

              case HtmlByteProto.CUSTOM_ATTR1 -> {
                contents = encodeInternal6(contents, proto);

                continue loop;
              }

              case HtmlByteProto.ELEMENT -> {
                contents = encodeElement(contents, proto);

                continue loop;
              }

              case HtmlByteProto.FLATTEN -> {
                contents = encodeFlatten(contents);

                continue loop;
              }

              case HtmlByteProto.FRAGMENT -> {
                contents = encodeFragment(contents);

                continue loop;
              }

              case HtmlByteProto.LENGTH2 -> contents = encodeLength2(contents);

              case HtmlByteProto.LENGTH3 -> contents = encodeLength3(contents);

              case HtmlByteProto.MARKED3 -> contents += 3;

              case HtmlByteProto.MARKED4 -> contents += 4;

              case HtmlByteProto.MARKED5 -> contents += 5;

              case HtmlByteProto.MARKED6 -> contents += 6;

              case HtmlByteProto.RAW,
                   HtmlByteProto.TEXT -> {
                contents = encodeInternal4(contents, proto);

                continue loop;
              }

              default -> {
                throw new UnsupportedOperationException(
                    "Implement me :: proto=" + proto
                );
              }
            }
          }
        }

        default -> throw new UnsupportedOperationException(
            "Implement me :: mark=" + mark
        );
      }
    }
  }

  private int encodeElement(int contents, byte proto) {
    throw new UnsupportedOperationException("Implement me");
  }

  private int encodeFlatten(int contents) {
    throw new UnsupportedOperationException("Implement me");
  }

  private int encodeFragment(int contents) {
    throw new UnsupportedOperationException("Implement me");
  }

  private int encodeInternal3(int contents, byte proto) {
    return encodeInternal(contents, proto, 3, HtmlByteProto.MARKED3);
  }

  private int encodeInternal4(int contents, byte proto) {
    return encodeInternal(contents, proto, 4, HtmlByteProto.MARKED4);
  }

  private int encodeInternal5(int contents, byte proto) {
    return encodeInternal(contents, proto, 5, HtmlByteProto.MARKED5);
  }

  private int encodeInternal6(int contents, byte proto) {
    return encodeInternal(contents, proto, 6, HtmlByteProto.MARKED6);
  }

  private int encodeInternal(int contents, byte proto, int offset, byte marked) {
    // keep the start index handy
    final int startIndex;
    startIndex = contents;

    // mark this element
    main.set(contents, marked);

    main.add(proto);

    final int length;
    length = main.size() - startIndex;

    HtmlBytes.encodeOffset(main, length);

    return contents + offset;
  }

  private int encodeLength2(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main.get(contents++);

    byte len1;
    len1 = main.get(contents++);

    int length;
    length = HtmlBytes.decodeInt(len0, len1);

    // point to next element
    return contents + length;
  }

  private int encodeLength3(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main.get(contents++);

    byte len1;
    len1 = main.get(contents++);

    byte len2;
    len2 = main.get(contents++);

    int length;
    length = HtmlBytes.decodeLength3(len0, len1, len2);

    // point to next element
    return contents + length;
  }

}
