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

final class Encoder {

  private final ByteArray main;

  private final FlattenEncoder flattenEncoder;

  private final ElementEncoder elementEncoder;

  Encoder(ByteArray main, FlattenEncoder flattenEncoder, ElementEncoder elementEncoder) {
    this.main = main;

    this.flattenEncoder = flattenEncoder;

    this.elementEncoder = elementEncoder;
  }

  public final int encode(final int startIndex) {
    int index;
    index = startIndex;

    byte proto;

    while (true) {
      byte maybe;
      maybe = main.get(index);

      switch (maybe) {
        case HtmlByteProto.LENGTH2 -> { index = encodeLength2(index); continue; }

        case HtmlByteProto.LENGTH3 -> { index = encodeLength3(index); continue; }

        case HtmlByteProto.MARKED3 -> { index += 3; continue; }

        case HtmlByteProto.MARKED4 -> { index += 4; continue; }

        case HtmlByteProto.MARKED5 -> { index += 5; continue; }

        case HtmlByteProto.MARKED6 -> { index += 6; continue; }
      }

      proto = maybe;

      break;
    }

    return switch (proto) {
      case HtmlByteProto.ATTRIBUTE0 -> encodeInternal(index, proto, 3, HtmlByteProto.MARKED3);

      case HtmlByteProto.CUSTOM_ATTR0,
           HtmlByteProto.RAW,
           HtmlByteProto.TEXT -> encodeInternal(index, proto, 4, HtmlByteProto.MARKED4);

      case HtmlByteProto.AMBIGUOUS1,
           HtmlByteProto.ATTRIBUTE1 -> encodeInternal(index, proto, 5, HtmlByteProto.MARKED5);

      case HtmlByteProto.CUSTOM_ATTR1 -> encodeInternal(index, proto, 6, HtmlByteProto.MARKED6);

      case HtmlByteProto.ELEMENT -> elementEncoder.encode(index);

      case HtmlByteProto.FLATTEN -> flattenEncoder.encode(index);

      case HtmlByteProto.FRAGMENT -> encodeFragment(index);

      default -> {
        throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }
    };
  }

  private int encodeFragment(final int startIndex) {
    int index;
    index = startIndex;

    // mark this fragment
    main.set(index++, HtmlByteProto.LENGTH3);

    // decode the length
    final byte len0;
    len0 = main.get(index++);

    final byte len1;
    len1 = main.get(index++);

    final byte len2;
    len2 = main.get(index++);

    // point to next element
    final int offset;
    offset = HtmlBytes.decodeLength3(len0, len1, len2);

    final int maxIndex;
    maxIndex = index + offset;

    while (index < maxIndex) {
      final byte proto;
      proto = main.get(index);

      if (proto == HtmlByteProto.END) {
        break;
      }

      index = encode(index);
    }

    return maxIndex;
  }

  private int encodeInternal(final int startIndex, byte proto, int offset, byte marked) {
    main.set(startIndex, marked);

    main.add(proto);

    final int length;
    length = main.size() - startIndex;

    main.addVarIntLE(length);

    return startIndex + offset;
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
