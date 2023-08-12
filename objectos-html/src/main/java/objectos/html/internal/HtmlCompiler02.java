/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import java.util.Arrays;
import objectos.util.ObjectArrays;

final class HtmlCompiler02 extends HtmlCompiler01 {

  @Override
  public final CompiledMarkup compile() {
    return new CompiledMarkup(
      Arrays.copyOf(aux, auxIndex), objects()
    );
  }

  @Override
  public final void optimize() {
    // we will use the aux list to store our byte code
    auxIndex = 0;

    // holds decoded length
    auxStart = 0;

    // jmp auxiliary
    mainContents = 0;

    // holds the indentation level
    mainStart = 0;

    // we will iterate over the main list looking for unmarked elements
    int index;
    index = 0;

    while (index < mainIndex) {
      byte proto;
      proto = main[index++];

      int length;
      length = switch (proto) {
        case ByteProto2.ELEMENT -> {
          int thisLength;
          thisLength = Bytes.decodeInt(main[index++], main[index++]);

          element(index);

          yield thisLength;
        }

        case ByteProto2.MARKED5 -> 5 - 1;

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      };

      index += length;
    }

    if (index > 0) {
      auxAdd(ByteCode.NL);
    }
  }

  private int decodeLength(int index) {
    byte len0;
    len0 = main[index++];

    auxStart = len0;

    if (auxStart < 0) {
      byte len1;
      len1 = main[index++];

      auxStart = Bytes.decodeVarInt(len0, len1);
    }

    return index;
  }

  private void element(int index) {
    // first proto should point to the element name
    byte proto;
    proto = main[index++];

    // we'll keep the name value for the end tag (if necessary)
    byte name;

    switch (proto) {
      case ByteProto2.STANDARD_NAME -> {
        name = main[index++];

        auxAdd(ByteCode.START_TAG, name);
      }

      default -> throw new IllegalArgumentException(
        "Malformed element. Expected name but found=" + proto
      );
    }

    // we'll iterate over the attributes (if any)

    // we keep the index handy
    int contents;
    contents = index;

    int attr;
    attr = Integer.MIN_VALUE;

    loop: while (index < mainIndex) {
      proto = main[index++];

      switch (proto) {
        case ByteProto2.ATTRIBUTE1 -> {
          index = jmp(index);

          // handle attr name

          byte ordinalByte;
          ordinalByte = main[mainContents++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          if (attr == Integer.MIN_VALUE) {
            // this is the first attribute
            auxAdd(ByteCode.SPACE, ByteCode.ATTR_NAME, ordinalByte, ByteCode.ATTR_VALUE_START);
          }

          else if (attr != ordinal) {
            // this is a new attribute
            auxAdd(ByteCode.ATTR_VALUE_END,
              ByteCode.SPACE, ByteCode.ATTR_NAME, ordinalByte, ByteCode.ATTR_VALUE_START);
          }

          attr = ordinal;

          // handle attr value

          byte int0;
          int0 = main[mainContents++];

          byte int1;
          int1 = main[mainContents++];

          auxAdd(ByteCode.ATTR_VALUE, int0, int1);
        }

        case ByteProto2.END -> {
          if (attr != Integer.MIN_VALUE) {
            auxAdd(ByteCode.ATTR_VALUE_END);
          }

          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    // let's close the start tag

    auxAdd(ByteCode.GT);

    // we'll iterate over the children (if any)

    // we count the children so we'll know if this element was empty or not
    int children;
    children = 0;

    // start from the beginning
    index = contents;

    loop: while (index < mainIndex) {
      proto = main[index++];

      switch (proto) {
        case ByteProto2.ATTRIBUTE1 -> index = skipVarInt(index);

        case ByteProto2.END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    if (children == 0) {
      // empty element

      auxAdd(ByteCode.EMPTY_ELEMENT, (byte) mainStart);
    }

    // write end tag

    auxAdd(ByteCode.END_TAG, name);
  }

  private int jmp(int index) {
    int baseIndex;
    baseIndex = index;

    index = decodeLength(index);

    mainContents = baseIndex - auxStart;

    // skip ByteProto
    mainContents++;

    return index;
  }

  private Object[] objects() {
    if (objectArray == null) {
      return ObjectArrays.empty();
    }

    return Arrays.copyOf(objectArray, objectIndex);
  }

  private int skipVarInt(int index) {
    byte len0;
    len0 = main[index++];

    if (len0 < 0) {
      index++;
    }

    return index;
  }

}