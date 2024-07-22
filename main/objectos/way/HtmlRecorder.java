/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.way;

import objectos.lang.object.Check;
import objectos.util.array.ByteArrays;
import objectos.util.array.ObjectArrays;

class HtmlRecorder {

  byte[] aux = new byte[128];

  int auxIndex;

  int auxStart;

  byte[] main = new byte[256];

  int mainContents;

  int mainIndex;

  int mainStart;

  Object[] objectArray;

  int objectIndex;

  final void compilationBegin() {
    auxIndex = auxStart = 0;

    mainContents = mainIndex = mainStart = 0;

    objectIndex = 0;
  }

  /**
   * Generates the {@code <!DOCTYPE html>} doctype.
   */
  public final void doctype() {
    mainAdd(HtmlByteProto.DOCTYPE);
  }

  final void compilationEnd() {
    // TODO remove...
  }

  final void ambiguous(HtmlAmbiguous name, String value) {
    int ordinal;
    ordinal = name.ordinal();

    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.AMBIGUOUS1,

        // name
        HtmlBytes.encodeInt0(ordinal),

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL5
    );
  }

  final Html.AttributeInstruction attribute0(Html.AttributeName name) {
    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    mainAdd(
        HtmlByteProto.ATTRIBUTE0,

        // name
        HtmlBytes.encodeInt0(index),

        HtmlByteProto.INTERNAL3
    );

    return Html.ATTRIBUTE;
  }

  final Html.AttributeOrNoOp attribute0(Html.AttributeName name, Object value) {
    return attribute1(name, value, HtmlByteProto.ATTRIBUTE1);
  }

  private Html.AttributeOrNoOp attribute1(Html.AttributeName name, Object value, byte proto) {
    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    int object;
    object = objectAdd(value);

    mainAdd(
        proto,

        // name
        HtmlBytes.encodeInt0(index),

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL5
    );

    return Html.ATTRIBUTE;
  }

  final void element(HtmlElementName name, Html.Instruction[] contents) {
    elementBegin(name);

    for (int i = 0; i < contents.length; i++) {
      Html.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      elementValue(inst);
    }

    elementEnd();
  }

  final void element(HtmlElementName name, String text) {
    textImpl(text);

    elementBegin(name);
    elementValue(Html.ELEMENT);
    elementEnd();
  }

  final void elementBegin(HtmlElementName name) {
    commonBegin();

    mainAdd(
        HtmlByteProto.ELEMENT,

        // length takes 2 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,

        HtmlByteProto.STANDARD_NAME,

        HtmlBytes.encodeName(name)
    );
  }

  final void elementValue(Html.Instruction value) {
    if (value == Html.ATTRIBUTE ||
        value == Html.ELEMENT ||
        value == Html.FRAGMENT) {
      // @ ByteProto
      mainContents--;

      byte proto;
      proto = main[mainContents--];

      switch (proto) {
        case HtmlByteProto.INTERNAL -> {
          int endIndex;
          endIndex = mainContents;

          byte maybeNeg;

          do {
            maybeNeg = main[mainContents--];
          } while (maybeNeg < 0);

          int length;
          length = HtmlBytes.decodeCommonEnd(main, mainContents, endIndex);

          mainContents -= length;
        }

        case HtmlByteProto.INTERNAL3 -> mainContents -= 3 - 2;

        case HtmlByteProto.INTERNAL4 -> mainContents -= 4 - 2;

        case HtmlByteProto.INTERNAL5 -> mainContents -= 5 - 2;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }

      auxAdd(HtmlByteProto.INTERNAL);
    }

    else if (value instanceof Html.Id ext) {
      int index;
      index = externalValue(ext.value());

      auxAdd(
          HtmlByteProto.ATTRIBUTE_ID,

          HtmlBytes.encodeInt0(index),
          HtmlBytes.encodeInt1(index)
      );
    }

    else if (value instanceof Html.ClassName ext) {
      int index;
      index = externalValue(ext.value());

      auxAdd(
          HtmlByteProto.ATTRIBUTE_CLASS,

          HtmlBytes.encodeInt0(index),
          HtmlBytes.encodeInt1(index)
      );
    }

    else if (value == Html.NOOP) {
      // no-op
    }

    else {
      throw new UnsupportedOperationException(
          "Implement me :: type=" + value.getClass()
      );
    }
  }

  final void elementEnd() {
    // we iterate over each value added via elementValue(Instruction)
    int index;
    index = auxStart;

    int indexMax;
    indexMax = auxIndex;

    int contents;
    contents = mainContents;

    loop: while (index < indexMax) {
      byte mark;
      mark = aux[index++];

      switch (mark) {
        case HtmlByteProto.ATTRIBUTE_CLASS,
             HtmlByteProto.ATTRIBUTE_ID,
             HtmlByteProto.TEXT -> {
          mainAdd(mark, aux[index++], aux[index++]);
        }

        case HtmlByteProto.INTERNAL -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case HtmlByteProto.ATTRIBUTE0 -> {
                contents = encodeInternal3(contents, proto);

                continue loop;
              }

              case HtmlByteProto.AMBIGUOUS1,
                   HtmlByteProto.ATTRIBUTE1 -> {
                contents = encodeInternal5(contents, proto);

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

    commonEnd(mainContents, mainStart);

    // we clear the aux list
    auxIndex = auxStart;
  }

  final void flattenBegin() {
    commonBegin();

    mainAdd(
        HtmlByteProto.FLATTEN,

        // length takes 2 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL
    );
  }

  final void rawImpl(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.RAW,

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL4
    );
  }

  final void textImpl(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
        HtmlByteProto.TEXT,

        // value
        HtmlBytes.encodeInt0(object),
        HtmlBytes.encodeInt1(object),

        HtmlByteProto.INTERNAL4
    );
  }

  final void auxAdd(byte b0) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 0);
    aux[auxIndex++] = b0;
  }

  final void auxAdd(byte b0, byte b1) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 1);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
  }

  final void auxAdd(byte b0, byte b1, byte b2) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 2);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 3);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 4);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 5);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 6);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 7);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
    aux[auxIndex++] = b7;
  }

  private void commonBegin() {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;
  }

  private void commonEnd(int contentsIndex, int startIndex) {
    // ensure main can hold 5 more elements
    // - ByteProto.END
    // - length
    // - length
    // - length
    // - ByteProto.INTERNAL
    main = ByteArrays.growIfNecessary(main, mainIndex + 4);

    // mark the end
    main[mainIndex++] = HtmlByteProto.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - contentsIndex - 1;

    mainIndex = HtmlBytes.encodeCommonEnd(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = HtmlByteProto.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // we skip the first byte proto
    main[startIndex + 1] = HtmlBytes.encodeInt0(length);
    main[startIndex + 2] = HtmlBytes.encodeInt1(length);
  }

  private int encodeElement(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents++] = HtmlByteProto.LENGTH2;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeInt(len0, len1);

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeFlatten(int contents) {
    int index;
    index = contents;

    // mark this fragment
    main[index++] = HtmlByteProto.LENGTH2;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeInt(len0, len1);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case HtmlByteProto.ATTRIBUTE_CLASS,
             HtmlByteProto.ATTRIBUTE_ID -> {
          byte idx0;
          idx0 = main[index++];

          byte idx1;
          idx1 = main[index++];

          mainAdd(proto, idx0, idx1);
        }

        case HtmlByteProto.ATTRIBUTE1,
             HtmlByteProto.ELEMENT,
             HtmlByteProto.TEXT,
             HtmlByteProto.RAW -> {
          int elementIndex;
          elementIndex = index;

          do {
            len0 = main[index++];
          } while (len0 < 0);

          int len;
          len = HtmlBytes.decodeOffset(main, elementIndex, index);

          elementIndex -= len;

          // ensure main can hold least 4 elements
          // 0   - ByteProto
          // 1-3 - variable length
          main = ByteArrays.growIfNecessary(main, mainIndex + 3);

          main[mainIndex++] = proto;

          int length;
          length = mainIndex - elementIndex;

          mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);
        }

        case HtmlByteProto.END -> {
          break loop;
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

  private int encodeFragment(int contents) {
    int index;
    index = contents;

    // mark this fragment
    main[index++] = HtmlByteProto.LENGTH3;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    byte len2;
    len2 = main[index++];

    // point to next element
    int offset;
    offset = HtmlBytes.decodeLength3(len0, len1, len2);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index];

      switch (proto) {
        case HtmlByteProto.AMBIGUOUS1 -> index = encodeInternal5(index, proto);

        case HtmlByteProto.ATTRIBUTE0 -> index = encodeInternal3(index, proto);

        case HtmlByteProto.ATTRIBUTE1 -> index = encodeInternal5(index, proto);

        case HtmlByteProto.ELEMENT -> index = encodeElement(index, proto);

        case HtmlByteProto.END -> {
          break loop;
        }

        case HtmlByteProto.FRAGMENT -> index = encodeFragment(index);

        case HtmlByteProto.LENGTH2 -> index = encodeLength2(index);

        case HtmlByteProto.LENGTH3 -> index = encodeLength3(index);

        case HtmlByteProto.MARKED3 -> index += 3;

        case HtmlByteProto.MARKED4 -> index += 4;

        case HtmlByteProto.MARKED5 -> index += 5;

        case HtmlByteProto.RAW,
             HtmlByteProto.TEXT -> index = encodeInternal4(index, proto);

        default -> {
          throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
          );
        }
      }
    }

    return maxIndex;
  }

  private int encodeInternal3(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = HtmlByteProto.MARKED3;

    // point to next
    int offset;
    offset = 3;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal4(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = HtmlByteProto.MARKED4;

    // point to next
    int offset;
    offset = 4;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal5(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = HtmlByteProto.MARKED5;

    // point to next
    int offset;
    offset = 5;

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = HtmlBytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeLength2(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    int length;
    length = HtmlBytes.decodeInt(len0, len1);

    // point to next element
    return contents + length;
  }

  private int encodeLength3(int contents) {
    contents++;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    byte len2;
    len2 = main[contents++];

    int length;
    length = HtmlBytes.decodeLength3(len0, len1, len2);

    // point to next element
    return contents + length;
  }

  private int externalValue(String value) {
    String result;
    result = value;

    if (value == null) {
      result = "null";
    }

    return objectAdd(result);
  }

  final int fragmentBegin() {
    // we mark:
    // 1) the start of the contents of the current declaration
    int startIndex;
    startIndex = mainIndex;

    mainAdd(
        HtmlByteProto.FRAGMENT,

        // length takes 3 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,
        HtmlByteProto.NULL
    );

    return startIndex;
  }

  final void fragmentEnd(int startIndex) {
    // ensure main can hold 5 more elements
    // - ByteProto.END
    // - length
    // - length
    // - length
    // - ByteProto.INTERNAL
    main = ByteArrays.growIfNecessary(main, mainIndex + 4);

    // mark the end
    main[mainIndex++] = HtmlByteProto.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - startIndex - 1;

    mainIndex = HtmlBytes.encodeCommonEnd(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = HtmlByteProto.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1 + len2
    length -= 4;

    // we skip the first byte proto
    HtmlBytes.encodeLength3(main, startIndex + 1, length);
  }

  private void mainAdd(byte b0) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 0);
    main[mainIndex++] = b0;
  }

  private void mainAdd(byte b0, byte b1, byte b2) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 2);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 4);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
  }

  private int objectAdd(Object value) {
    int index;
    index = objectIndex++;

    if (objectArray == null) {
      objectArray = new Object[10];
    }

    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    objectArray[index] = value;

    return index;
  }

}