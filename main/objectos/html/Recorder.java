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
package objectos.html;

import objectos.lang.object.Check;
import objectos.util.array.ByteArrays;
import objectos.util.array.ObjectArrays;

class Recorder {

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
    mainAdd(ByteProto.DOCTYPE);
  }

  final void compilationEnd() {
    // TODO remove...
  }

  final void ambiguous(Ambiguous name, String value) {
    int ordinal;
    ordinal = name.ordinal();

    int object;
    object = objectAdd(value);

    mainAdd(
        ByteProto.AMBIGUOUS1,

        // name
        Bytes.encodeInt0(ordinal),

        // value
        Bytes.encodeInt0(object),
        Bytes.encodeInt1(object),

        ByteProto.INTERNAL5
    );
  }

  final Api.Attribute attribute0(AttributeName name) {
    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    mainAdd(
        ByteProto.ATTRIBUTE0,

        // name
        Bytes.encodeInt0(index),

        ByteProto.INTERNAL3
    );

    return Api.ATTRIBUTE;
  }

  final Api.Attribute attribute0(AttributeName name, String value) {
    int index;
    index = name.index();

    if (index < 0) {
      throw new UnsupportedOperationException("Custom attribute name");
    }

    int object;
    object = objectAdd(value);

    mainAdd(
        ByteProto.ATTRIBUTE1,

        // name
        Bytes.encodeInt0(index),

        // value
        Bytes.encodeInt0(object),
        Bytes.encodeInt1(object),

        ByteProto.INTERNAL5
    );

    return Api.ATTRIBUTE;
  }

  final void element(StandardElementName name, Api.Instruction[] contents) {
    elementBegin(name);

    for (int i = 0; i < contents.length; i++) {
      Api.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      elementValue(inst);
    }

    elementEnd();
  }

  final void element(StandardElementName name, String text) {
    textImpl(text);

    elementBegin(name);
    elementValue(Api.ELEMENT);
    elementEnd();
  }

  final void elementBegin(StandardElementName name) {
    commonBegin();

    mainAdd(
        ByteProto.ELEMENT,

        // length takes 2 bytes
        ByteProto.NULL,
        ByteProto.NULL,

        ByteProto.STANDARD_NAME,

        Bytes.encodeName(name)
    );
  }

  final void elementValue(Api.Instruction value) {
    if (value == Api.ATTRIBUTE ||
        value == Api.ELEMENT ||
        value == Api.FRAGMENT) {
      // @ ByteProto
      mainContents--;

      byte proto;
      proto = main[mainContents--];

      switch (proto) {
        case ByteProto.INTERNAL -> {
          int endIndex;
          endIndex = mainContents;

          byte maybeNeg;

          do {
            maybeNeg = main[mainContents--];
          } while (maybeNeg < 0);

          int length;
          length = Bytes.decodeCommonEnd(main, mainContents, endIndex);

          mainContents -= length;
        }

        case ByteProto.INTERNAL3 -> mainContents -= 3 - 2;

        case ByteProto.INTERNAL4 -> mainContents -= 4 - 2;

        case ByteProto.INTERNAL5 -> mainContents -= 5 - 2;

        default -> throw new UnsupportedOperationException(
            "Implement me :: proto=" + proto
        );
      }

      auxAdd(ByteProto.INTERNAL);
    }

    else if (value instanceof Api.ExternalAttribute.Id ext) {
      int index;
      index = externalValue(ext.id());

      auxAdd(
          ByteProto.ATTRIBUTE_ID,

          Bytes.encodeInt0(index),
          Bytes.encodeInt1(index)
      );
    }

    else if (value instanceof Api.ExternalAttribute.StyleClass ext) {
      int index;
      index = externalValue(ext.className());

      auxAdd(
          ByteProto.ATTRIBUTE_CLASS,

          Bytes.encodeInt0(index),
          Bytes.encodeInt1(index)
      );
    }

    else if (value == Api.NOOP) {
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
        case ByteProto.ATTRIBUTE_CLASS,
             ByteProto.ATTRIBUTE_ID,
             ByteProto.TEXT -> {
          mainAdd(mark, aux[index++], aux[index++]);
        }

        case ByteProto.INTERNAL -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case ByteProto.ATTRIBUTE0 -> {
                contents = encodeInternal3(contents, proto);

                continue loop;
              }

              case ByteProto.AMBIGUOUS1,
                   ByteProto.ATTRIBUTE1 -> {
                contents = encodeInternal5(contents, proto);

                continue loop;
              }

              case ByteProto.ELEMENT -> {
                contents = encodeElement(contents, proto);

                continue loop;
              }

              case ByteProto.FLATTEN -> {
                contents = encodeFlatten(contents);

                continue loop;
              }

              case ByteProto.FRAGMENT -> {
                contents = encodeFragment(contents);

                continue loop;
              }

              case ByteProto.LENGTH2 -> contents = encodeLength2(contents);

              case ByteProto.LENGTH3 -> contents = encodeLength3(contents);

              case ByteProto.MARKED3 -> contents += 3;

              case ByteProto.MARKED4 -> contents += 4;

              case ByteProto.MARKED5 -> contents += 5;

              case ByteProto.RAW,
                   ByteProto.TEXT -> {
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
        ByteProto.FLATTEN,

        // length takes 2 bytes
        ByteProto.NULL,
        ByteProto.NULL
    );
  }

  final void rawImpl(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
        ByteProto.RAW,

        // value
        Bytes.encodeInt0(object),
        Bytes.encodeInt1(object),

        ByteProto.INTERNAL4
    );
  }

  final void textImpl(String value) {
    int object;
    object = objectAdd(value);

    mainAdd(
        ByteProto.TEXT,

        // value
        Bytes.encodeInt0(object),
        Bytes.encodeInt1(object),

        ByteProto.INTERNAL4
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
    main[mainIndex++] = ByteProto.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - contentsIndex - 1;

    mainIndex = Bytes.encodeCommonEnd(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = ByteProto.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // we skip the first byte proto
    main[startIndex + 1] = Bytes.encodeInt0(length);
    main[startIndex + 2] = Bytes.encodeInt1(length);
  }

  private int encodeElement(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents++] = ByteProto.LENGTH2;

    // decode the length
    byte len0;
    len0 = main[contents++];

    byte len1;
    len1 = main[contents++];

    // point to next element
    int offset;
    offset = Bytes.decodeInt(len0, len1);

    // ensure main can hold least 4 elements
    // 0   - ByteProto
    // 1-3 - variable length
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    main[mainIndex++] = proto;

    int length;
    length = mainIndex - startIndex;

    mainIndex = Bytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeFlatten(int contents) {
    int index;
    index = contents;

    // mark this fragment
    main[index++] = ByteProto.LENGTH2;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    // point to next element
    int offset;
    offset = Bytes.decodeInt(len0, len1);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case ByteProto.ATTRIBUTE_CLASS,
             ByteProto.ATTRIBUTE_ID -> {
          byte idx0;
          idx0 = main[index++];

          byte idx1;
          idx1 = main[index++];

          mainAdd(proto, idx0, idx1);
        }

        case ByteProto.ATTRIBUTE1,
             ByteProto.ELEMENT,
             ByteProto.TEXT -> {
          int elementIndex;
          elementIndex = index;

          do {
            len0 = main[index++];
          } while (len0 < 0);

          int len;
          len = Bytes.decodeOffset(main, elementIndex, index);

          elementIndex -= len;

          // ensure main can hold least 4 elements
          // 0   - ByteProto
          // 1-3 - variable length
          main = ByteArrays.growIfNecessary(main, mainIndex + 3);

          main[mainIndex++] = proto;

          int length;
          length = mainIndex - elementIndex;

          mainIndex = Bytes.encodeOffset(main, mainIndex, length);
        }

        case ByteProto.END -> {
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
    main[index++] = ByteProto.LENGTH3;

    // decode the length
    byte len0;
    len0 = main[index++];

    byte len1;
    len1 = main[index++];

    byte len2;
    len2 = main[index++];

    // point to next element
    int offset;
    offset = Bytes.decodeLength3(len0, len1, len2);

    int maxIndex;
    maxIndex = index + offset;

    loop: while (index < maxIndex) {
      byte proto;
      proto = main[index];

      switch (proto) {
        case ByteProto.AMBIGUOUS1 -> index = encodeInternal5(index, proto);

        case ByteProto.ATTRIBUTE0 -> index = encodeInternal3(index, proto);

        case ByteProto.ATTRIBUTE1 -> index = encodeInternal5(index, proto);

        case ByteProto.ELEMENT -> index = encodeElement(index, proto);

        case ByteProto.END -> {
          break loop;
        }

        case ByteProto.FRAGMENT -> index = encodeFragment(index);

        case ByteProto.LENGTH2 -> index = encodeLength2(index);

        case ByteProto.LENGTH3 -> index = encodeLength3(index);

        case ByteProto.MARKED3 -> index += 3;

        case ByteProto.MARKED4 -> index += 4;

        case ByteProto.MARKED5 -> index += 5;

        case ByteProto.RAW,
             ByteProto.TEXT -> index = encodeInternal4(index, proto);

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
    main[contents] = ByteProto.MARKED3;

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

    mainIndex = Bytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal4(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = ByteProto.MARKED4;

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

    mainIndex = Bytes.encodeOffset(main, mainIndex, length);

    return contents + offset;
  }

  private int encodeInternal5(int contents, byte proto) {
    // keep the start index handy
    int startIndex;
    startIndex = contents;

    // mark this element
    main[contents] = ByteProto.MARKED5;

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

    mainIndex = Bytes.encodeOffset(main, mainIndex, length);

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
    length = Bytes.decodeInt(len0, len1);

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
    length = Bytes.decodeLength3(len0, len1, len2);

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
        ByteProto.FRAGMENT,

        // length takes 3 bytes
        ByteProto.NULL,
        ByteProto.NULL,
        ByteProto.NULL
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
    main[mainIndex++] = ByteProto.END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - startIndex - 1;

    mainIndex = Bytes.encodeCommonEnd(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = ByteProto.INTERNAL;

    // set the end index of the declaration
    length = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1 + len2
    length -= 4;

    // we skip the first byte proto
    Bytes.encodeLength3(main, startIndex + 1, length);
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