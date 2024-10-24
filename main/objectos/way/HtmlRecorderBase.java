/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.util.Objects;

sealed class HtmlRecorderBase permits HtmlRecorderAttributes {

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

  final Html.Instruction.OfAttribute attribute0(Html.AttributeName name) {
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

  public final Html.Instruction.OfElement element(Html.ElementName name, Html.Instruction... contents) {
    Check.notNull(name, "name == null");

    elementBegin(name);

    for (int i = 0; i < contents.length; i++) {
      Html.Instruction inst;
      inst = Check.notNull(contents[i], "contents[", i, "] == null");

      elementValue(inst);
    }

    elementEnd();

    return Html.ELEMENT;
  }

  public final Html.Instruction.OfElement element(Html.ElementName name, String text) {
    Check.notNull(name, "name == null");
    Check.notNull(text, "text == null");

    textImpl(text);

    elementBegin(name);
    elementValue(Html.ELEMENT);
    elementEnd();

    return Html.ELEMENT;
  }

  final void elementBegin(Html.ElementName name) {
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

    else if (value instanceof Html.AttributeObject ext) {
      Html.AttributeName name;
      name = ext.name();

      int nameIndex;
      nameIndex = name.index();

      if (nameIndex < 0) {
        throw new UnsupportedOperationException("Custom attribute name");
      }

      int valueIndex;
      valueIndex = externalValue(ext.value());

      auxAdd(
          HtmlByteProto.ATTRIBUTE_EXT1,

          // name
          HtmlBytes.encodeInt0(nameIndex),

          // value
          HtmlBytes.encodeInt0(valueIndex),
          HtmlBytes.encodeInt1(valueIndex)
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
        case HtmlByteProto.TEXT -> {
          mainAdd(mark, aux[index++], aux[index++]);
        }

        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          mainAdd(mark, aux[index++], aux[index++], aux[index++]);
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
                   HtmlByteProto.TEXT,
                   HtmlByteProto.TESTABLE -> {
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

  final void testableImpl(String name) {
    int object;
    object = objectAdd(name);

    mainAdd(
        HtmlByteProto.TESTABLE,

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
    aux = Util.growIfNecessary(aux, auxIndex + 0);
    aux[auxIndex++] = b0;
  }

  final void auxAdd(byte b0, byte b1) {
    aux = Util.growIfNecessary(aux, auxIndex + 1);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
  }

  final void auxAdd(byte b0, byte b1, byte b2) {
    aux = Util.growIfNecessary(aux, auxIndex + 2);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3) {
    aux = Util.growIfNecessary(aux, auxIndex + 3);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    aux = Util.growIfNecessary(aux, auxIndex + 4);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5) {
    aux = Util.growIfNecessary(aux, auxIndex + 5);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6) {
    aux = Util.growIfNecessary(aux, auxIndex + 6);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
    aux = Util.growIfNecessary(aux, auxIndex + 7);
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
    main = Util.growIfNecessary(main, mainIndex + 4);

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
    main = Util.growIfNecessary(main, mainIndex + 3);

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
        case HtmlByteProto.ATTRIBUTE_EXT1 -> {
          byte idx0;
          idx0 = main[index++];

          byte idx1;
          idx1 = main[index++];

          byte idx2;
          idx2 = main[index++];

          mainAdd(proto, idx0, idx1, idx2);
        }

        case HtmlByteProto.AMBIGUOUS1,
             HtmlByteProto.ATTRIBUTE0,
             HtmlByteProto.ATTRIBUTE1,
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
          main = Util.growIfNecessary(main, mainIndex + 3);

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
    main = Util.growIfNecessary(main, mainIndex + 3);

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
    main = Util.growIfNecessary(main, mainIndex + 3);

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
    main = Util.growIfNecessary(main, mainIndex + 3);

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
    main = Util.growIfNecessary(main, mainIndex + 4);

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
    main = Util.growIfNecessary(main, mainIndex + 0);
    main[mainIndex++] = b0;
  }

  private void mainAdd(byte b0, byte b1, byte b2) {
    main = Util.growIfNecessary(main, mainIndex + 2);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3) {
    main = Util.growIfNecessary(main, mainIndex + 3);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4) {
    main = Util.growIfNecessary(main, mainIndex + 4);
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

    objectArray = Util.growIfNecessary(objectArray, objectIndex);

    objectArray[index] = value;

    return index;
  }

}

/**
 * Provides methods for rendering HTML attributes.
 */
sealed class HtmlRecorderAttributes extends HtmlRecorderBase {

  HtmlRecorderAttributes() {}

  /**
   * Generates the {@code accesskey} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute accesskey(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ACCESSKEY, value);
  }

  /**
   * Generates the {@code action} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute action(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ACTION, value);
  }

  /**
   * Generates the {@code align} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute align(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALIGN, value);
  }

  /**
   * Generates the {@code alignment-baseline} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute alignmentBaseline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALIGNMENT_BASELINE, value);
  }

  /**
   * Generates the {@code alt} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute alt(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALT, value);
  }

  /**
   * Generates the {@code aria-hidden} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute ariaHidden(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ARIA_HIDDEN, value);
  }

  /**
   * Generates the {@code aria-label} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute ariaLabel(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ARIA_LABEL, value);
  }

  /**
   * Generates the {@code async} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute async() {
    return attribute0(HtmlAttributeName.ASYNC);
  }

  /**
   * Generates the {@code autocomplete} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute autocomplete(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.AUTOCOMPLETE, value);
  }

  /**
   * Generates the {@code autofocus} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute autofocus() {
    return attribute0(HtmlAttributeName.AUTOFOCUS);
  }

  /**
   * Generates the {@code baseline-shift} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute baselineShift(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.BASELINE_SHIFT, value);
  }

  /**
   * Generates the {@code border} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute border(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.BORDER, value);
  }

  /**
   * Generates the {@code cellpadding} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cellpadding(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CELLPADDING, value);
  }

  /**
   * Generates the {@code cellspacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cellspacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CELLSPACING, value);
  }

  /**
   * Generates the {@code charset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute charset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CHARSET, value);
  }

  /**
   * Generates the {@code cite} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cite(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CITE, value);
  }

  /**
   * Generates the {@code class} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute className(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CLASS, value);
  }

  /**
   * Generates the {@code clip-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute clipRule(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CLIP_RULE, value);
  }

  /**
   * Generates the {@code color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute color(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR, value);
  }

  /**
   * Generates the {@code color-interpolation} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute colorInterpolation(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR_INTERPOLATION, value);
  }

  /**
   * Generates the {@code color-interpolation-filters} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute colorInterpolationFilters(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR_INTERPOLATION_FILTERS, value);
  }

  /**
   * Generates the {@code cols} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cols(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLS, value);
  }

  /**
   * Generates the {@code content} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute content(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CONTENT, value);
  }

  /**
   * Generates the {@code contenteditable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute contenteditable(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CONTENTEDITABLE, value);
  }

  /**
   * Generates the {@code crossorigin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute crossorigin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CROSSORIGIN, value);
  }

  /**
   * Generates the {@code cursor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cursor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CURSOR, value);
  }

  /**
   * Generates the {@code d} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute d(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.D, value);
  }

  /**
   * Generates the {@code defer} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute defer() {
    return attribute0(HtmlAttributeName.DEFER);
  }

  /**
   * Generates the {@code dir} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute dir(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIR, value);
  }

  /**
   * Generates the {@code direction} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute direction(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIRECTION, value);
  }

  /**
   * Generates the {@code dirname} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute dirname(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIRNAME, value);
  }

  /**
   * Generates the {@code disabled} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute disabled() {
    return attribute0(HtmlAttributeName.DISABLED);
  }

  /**
   * Generates the {@code display} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute display(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DISPLAY, value);
  }

  /**
   * Generates the {@code dominant-baseline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute dominantBaseline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DOMINANT_BASELINE, value);
  }

  /**
   * Generates the {@code draggable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute draggable(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DRAGGABLE, value);
  }

  /**
   * Generates the {@code enctype} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute enctype(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ENCTYPE, value);
  }

  /**
   * Generates the {@code fill} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fill(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL, value);
  }

  /**
   * Generates the {@code fill-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fillOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL_OPACITY, value);
  }

  /**
   * Generates the {@code fill-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fillRule(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL_RULE, value);
  }

  /**
   * Generates the {@code filter} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute filter(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILTER, value);
  }

  /**
   * Generates the {@code flood-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute floodColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FLOOD_COLOR, value);
  }

  /**
   * Generates the {@code flood-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute floodOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FLOOD_OPACITY, value);
  }

  /**
   * Generates the {@code font-family} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontFamily(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_FAMILY, value);
  }

  /**
   * Generates the {@code font-size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontSize(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_SIZE, value);
  }

  /**
   * Generates the {@code font-size-adjust} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontSizeAdjust(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_SIZE_ADJUST, value);
  }

  /**
   * Generates the {@code font-stretch} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontStretch(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_STRETCH, value);
  }

  /**
   * Generates the {@code font-style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontStyle(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_STYLE, value);
  }

  /**
   * Generates the {@code font-variant} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontVariant(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_VARIANT, value);
  }

  /**
   * Generates the {@code font-weight} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontWeight(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_WEIGHT, value);
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute forAttr(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FOR, value);
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute forElement(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FOR, value);
  }

  /**
   * Generates the {@code glyph-orientation-horizontal} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute glyphOrientationHorizontal(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.GLYPH_ORIENTATION_HORIZONTAL, value);
  }

  /**
   * Generates the {@code glyph-orientation-vertical} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute glyphOrientationVertical(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.GLYPH_ORIENTATION_VERTICAL, value);
  }

  /**
   * Generates the {@code height} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute height(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HEIGHT, value);
  }

  /**
   * Generates the {@code hidden} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute hidden() {
    return attribute0(HtmlAttributeName.HIDDEN);
  }

  /**
   * Generates the {@code href} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute href(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HREF, value);
  }

  /**
   * Generates the {@code http-equiv} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute httpEquiv(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HTTP_EQUIV, value);
  }

  /**
   * Generates the {@code id} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute id(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ID, value);
  }

  /**
   * Generates the {@code image-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute imageRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.IMAGE_RENDERING, value);
  }

  /**
   * Generates the {@code integrity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute integrity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.INTEGRITY, value);
  }

  /**
   * Generates the {@code lang} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute lang(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LANG, value);
  }

  /**
   * Generates the {@code letter-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute letterSpacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LETTER_SPACING, value);
  }

  /**
   * Generates the {@code lighting-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute lightingColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LIGHTING_COLOR, value);
  }

  /**
   * Generates the {@code marker-end} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute markerEnd(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_END, value);
  }

  /**
   * Generates the {@code marker-mid} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute markerMid(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_MID, value);
  }

  /**
   * Generates the {@code marker-start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute markerStart(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_START, value);
  }

  /**
   * Generates the {@code mask} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute mask(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MASK, value);
  }

  /**
   * Generates the {@code mask-type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute maskType(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MASK_TYPE, value);
  }

  /**
   * Generates the {@code maxlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute maxlength(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MAXLENGTH, value);
  }

  /**
   * Generates the {@code media} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute media(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MEDIA, value);
  }

  /**
   * Generates the {@code method} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute method(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.METHOD, value);
  }

  /**
   * Generates the {@code minlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute minlength(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MINLENGTH, value);
  }

  /**
   * Generates the {@code multiple} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute multiple() {
    return attribute0(HtmlAttributeName.MULTIPLE);
  }

  /**
   * Generates the {@code name} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute name(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.NAME, value);
  }

  /**
   * Generates the {@code nomodule} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute nomodule() {
    return attribute0(HtmlAttributeName.NOMODULE);
  }

  /**
   * Generates the {@code onafterprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onafterprint(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONAFTERPRINT, value);
  }

  /**
   * Generates the {@code onbeforeprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onbeforeprint(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONBEFOREPRINT, value);
  }

  /**
   * Generates the {@code onbeforeunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onbeforeunload(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONBEFOREUNLOAD, value);
  }

  /**
   * Generates the {@code onclick} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onclick(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONCLICK, value);
  }

  /**
   * Generates the {@code onhashchange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onhashchange(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONHASHCHANGE, value);
  }

  /**
   * Generates the {@code onlanguagechange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onlanguagechange(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONLANGUAGECHANGE, value);
  }

  /**
   * Generates the {@code onmessage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onmessage(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONMESSAGE, value);
  }

  /**
   * Generates the {@code onoffline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onoffline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONOFFLINE, value);
  }

  /**
   * Generates the {@code ononline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute ononline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONONLINE, value);
  }

  /**
   * Generates the {@code onpagehide} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onpagehide(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPAGEHIDE, value);
  }

  /**
   * Generates the {@code onpageshow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onpageshow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPAGESHOW, value);
  }

  /**
   * Generates the {@code onpopstate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onpopstate(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPOPSTATE, value);
  }

  /**
   * Generates the {@code onrejectionhandled} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onrejectionhandled(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONREJECTIONHANDLED, value);
  }

  /**
   * Generates the {@code onstorage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onstorage(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONSTORAGE, value);
  }

  /**
   * Generates the {@code onsubmit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onsubmit(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONSUBMIT, value);
  }

  /**
   * Generates the {@code onunhandledrejection} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onunhandledrejection(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONUNHANDLEDREJECTION, value);
  }

  /**
   * Generates the {@code onunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onunload(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONUNLOAD, value);
  }

  /**
   * Generates the {@code opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute opacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.OPACITY, value);
  }

  /**
   * Generates the {@code open} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute open() {
    return attribute0(HtmlAttributeName.OPEN);
  }

  /**
   * Generates the {@code overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute overflow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.OVERFLOW, value);
  }

  /**
   * Generates the {@code paint-order} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute paintOrder(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PAINT_ORDER, value);
  }

  /**
   * Generates the {@code placeholder} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute placeholder(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PLACEHOLDER, value);
  }

  /**
   * Generates the {@code pointer-events} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute pointerEvents(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.POINTER_EVENTS, value);
  }

  /**
   * Generates the {@code property} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute property(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PROPERTY, value);
  }

  /**
   * Generates the {@code readonly} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute readonly() {
    return attribute0(HtmlAttributeName.READONLY);
  }

  /**
   * Generates the {@code referrerpolicy} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute referrerpolicy(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REFERRERPOLICY, value);
  }

  /**
   * Generates the {@code rel} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute rel(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REL, value);
  }

  /**
   * Generates the {@code required} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute required() {
    return attribute0(HtmlAttributeName.REQUIRED);
  }

  /**
   * Generates the {@code rev} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute rev(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REV, value);
  }

  /**
   * Generates the {@code reversed} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute reversed() {
    return attribute0(HtmlAttributeName.REVERSED);
  }

  /**
   * Generates the {@code role} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute role(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ROLE, value);
  }

  /**
   * Generates the {@code rows} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute rows(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ROWS, value);
  }

  /**
   * Generates the {@code selected} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute selected() {
    return attribute0(HtmlAttributeName.SELECTED);
  }

  /**
   * Generates the {@code shape-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute shapeRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SHAPE_RENDERING, value);
  }

  /**
   * Generates the {@code size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute size(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SIZE, value);
  }

  /**
   * Generates the {@code sizes} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute sizes(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SIZES, value);
  }

  /**
   * Generates the {@code spellcheck} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute spellcheck(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SPELLCHECK, value);
  }

  /**
   * Generates the {@code src} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute src(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SRC, value);
  }

  /**
   * Generates the {@code srcset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute srcset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SRCSET, value);
  }

  /**
   * Generates the {@code start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute start(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.START, value);
  }

  /**
   * Generates the {@code stop-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute stopColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STOP_COLOR, value);
  }

  /**
   * Generates the {@code stop-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute stopOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STOP_OPACITY, value);
  }

  /**
   * Generates the {@code stroke} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute stroke(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE, value);
  }

  /**
   * Generates the {@code stroke-dasharray} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeDasharray(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_DASHARRAY, value);
  }

  /**
   * Generates the {@code stroke-dashoffset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeDashoffset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_DASHOFFSET, value);
  }

  /**
   * Generates the {@code stroke-linecap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeLinecap(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_LINECAP, value);
  }

  /**
   * Generates the {@code stroke-linejoin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeLinejoin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_LINEJOIN, value);
  }

  /**
   * Generates the {@code stroke-miterlimit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeMiterlimit(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_MITERLIMIT, value);
  }

  /**
   * Generates the {@code stroke-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_OPACITY, value);
  }

  /**
   * Generates the {@code stroke-width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeWidth(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_WIDTH, value);
  }

  /**
   * Generates the {@code style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute inlineStyle(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STYLE, value);
  }

  /**
   * Generates the {@code tabindex} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute tabindex(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TABINDEX, value);
  }

  /**
   * Generates the {@code target} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute target(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TARGET, value);
  }

  /**
   * Generates the {@code text-anchor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute textAnchor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_ANCHOR, value);
  }

  /**
   * Generates the {@code text-decoration} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute textDecoration(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_DECORATION, value);
  }

  /**
   * Generates the {@code text-overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute textOverflow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_OVERFLOW, value);
  }

  /**
   * Generates the {@code text-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute textRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_RENDERING, value);
  }

  /**
   * Generates the {@code transform} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute transform(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSFORM, value);
  }

  /**
   * Generates the {@code transform-origin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute transformOrigin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSFORM_ORIGIN, value);
  }

  /**
   * Generates the {@code translate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute translate(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSLATE, value);
  }

  /**
   * Generates the {@code type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute type(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TYPE, value);
  }

  /**
   * Generates the {@code unicode-bidi} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute unicodeBidi(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.UNICODE_BIDI, value);
  }

  /**
   * Generates the {@code value} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute value(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VALUE, value);
  }

  /**
   * Generates the {@code vector-effect} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute vectorEffect(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VECTOR_EFFECT, value);
  }

  /**
   * Generates the {@code viewBox} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute viewBox(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VIEWBOX, value);
  }

  /**
   * Generates the {@code visibility} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute visibility(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VISIBILITY, value);
  }

  /**
   * Generates the {@code white-space} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute whiteSpace(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WHITE_SPACE, value);
  }

  /**
   * Generates the {@code width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute width(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WIDTH, value);
  }

  /**
   * Generates the {@code word-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute wordSpacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WORD_SPACING, value);
  }

  /**
   * Generates the {@code wrap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute wrap(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WRAP, value);
  }

  /**
   * Generates the {@code writing-mode} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute writingMode(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WRITING_MODE, value);
  }

  /**
   * Generates the {@code xmlns} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute xmlns(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.XMLNS, value);
  }

}

/**
 * Provides methods for rendering HTML elements.
 */
sealed class HtmlRecorderElements extends HtmlRecorderAttributes permits HtmlRecorder {

  HtmlRecorderElements() {}

  /**
   * Generates the {@code a} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement a(Html.Instruction... contents) {
    return element(HtmlElementName.A, contents);
  }

  /**
   * Generates the {@code a} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement a(String text) {
    return element(HtmlElementName.A, text);
  }

  /**
   * Generates the {@code abbr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
    return element(HtmlElementName.ABBR, contents);
  }

  /**
   * Generates the {@code abbr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement abbr(String text) {
    return element(HtmlElementName.ABBR, text);
  }

  /**
   * Generates the {@code article} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement article(Html.Instruction... contents) {
    return element(HtmlElementName.ARTICLE, contents);
  }

  /**
   * Generates the {@code article} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement article(String text) {
    return element(HtmlElementName.ARTICLE, text);
  }

  /**
   * Generates the {@code b} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement b(Html.Instruction... contents) {
    return element(HtmlElementName.B, contents);
  }

  /**
   * Generates the {@code b} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement b(String text) {
    return element(HtmlElementName.B, text);
  }

  /**
   * Generates the {@code blockquote} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
    return element(HtmlElementName.BLOCKQUOTE, contents);
  }

  /**
   * Generates the {@code blockquote} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement blockquote(String text) {
    return element(HtmlElementName.BLOCKQUOTE, text);
  }

  /**
   * Generates the {@code body} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement body(Html.Instruction... contents) {
    return element(HtmlElementName.BODY, contents);
  }

  /**
   * Generates the {@code body} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement body(String text) {
    return element(HtmlElementName.BODY, text);
  }

  /**
   * Generates the {@code br} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.BR, contents);
  }

  /**
   * Generates the {@code button} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement button(Html.Instruction... contents) {
    return element(HtmlElementName.BUTTON, contents);
  }

  /**
   * Generates the {@code button} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement button(String text) {
    return element(HtmlElementName.BUTTON, text);
  }

  /**
   * Generates the {@code clipPath} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
    return element(HtmlElementName.CLIPPATH, contents);
  }

  /**
   * Generates the {@code clipPath} attribute or element with the specified
   * text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.Instruction.OfElement clipPath(String text) {
    ambiguous(HtmlAmbiguous.CLIPPATH, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code code} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement code(Html.Instruction... contents) {
    return element(HtmlElementName.CODE, contents);
  }

  /**
   * Generates the {@code code} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement code(String text) {
    return element(HtmlElementName.CODE, text);
  }

  /**
   * Generates the {@code dd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dd(Html.Instruction... contents) {
    return element(HtmlElementName.DD, contents);
  }

  /**
   * Generates the {@code dd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dd(String text) {
    return element(HtmlElementName.DD, text);
  }

  /**
   * Generates the {@code defs} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement defs(Html.Instruction... contents) {
    return element(HtmlElementName.DEFS, contents);
  }

  /**
   * Generates the {@code defs} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement defs(String text) {
    return element(HtmlElementName.DEFS, text);
  }

  /**
   * Generates the {@code details} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement details(Html.Instruction... contents) {
    return element(HtmlElementName.DETAILS, contents);
  }

  /**
   * Generates the {@code details} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement details(String text) {
    return element(HtmlElementName.DETAILS, text);
  }

  /**
   * Generates the {@code div} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement div(Html.Instruction... contents) {
    return element(HtmlElementName.DIV, contents);
  }

  /**
   * Generates the {@code div} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement div(String text) {
    return element(HtmlElementName.DIV, text);
  }

  /**
   * Generates the {@code dl} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dl(Html.Instruction... contents) {
    return element(HtmlElementName.DL, contents);
  }

  /**
   * Generates the {@code dl} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dl(String text) {
    return element(HtmlElementName.DL, text);
  }

  /**
   * Generates the {@code dt} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dt(Html.Instruction... contents) {
    return element(HtmlElementName.DT, contents);
  }

  /**
   * Generates the {@code dt} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dt(String text) {
    return element(HtmlElementName.DT, text);
  }

  /**
   * Generates the {@code em} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement em(Html.Instruction... contents) {
    return element(HtmlElementName.EM, contents);
  }

  /**
   * Generates the {@code em} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement em(String text) {
    return element(HtmlElementName.EM, text);
  }

  /**
   * Generates the {@code fieldset} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
    return element(HtmlElementName.FIELDSET, contents);
  }

  /**
   * Generates the {@code fieldset} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement fieldset(String text) {
    return element(HtmlElementName.FIELDSET, text);
  }

  /**
   * Generates the {@code figure} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement figure(Html.Instruction... contents) {
    return element(HtmlElementName.FIGURE, contents);
  }

  /**
   * Generates the {@code figure} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement figure(String text) {
    return element(HtmlElementName.FIGURE, text);
  }

  /**
   * Generates the {@code footer} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement footer(Html.Instruction... contents) {
    return element(HtmlElementName.FOOTER, contents);
  }

  /**
   * Generates the {@code footer} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement footer(String text) {
    return element(HtmlElementName.FOOTER, text);
  }

  /**
   * Generates the {@code form} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement form(Html.Instruction... contents) {
    return element(HtmlElementName.FORM, contents);
  }

  /**
   * Generates the {@code form} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.Instruction.OfElement form(String text) {
    ambiguous(HtmlAmbiguous.FORM, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code g} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement g(Html.Instruction... contents) {
    return element(HtmlElementName.G, contents);
  }

  /**
   * Generates the {@code g} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement g(String text) {
    return element(HtmlElementName.G, text);
  }

  /**
   * Generates the {@code h1} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h1(Html.Instruction... contents) {
    return element(HtmlElementName.H1, contents);
  }

  /**
   * Generates the {@code h1} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h1(String text) {
    return element(HtmlElementName.H1, text);
  }

  /**
   * Generates the {@code h2} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h2(Html.Instruction... contents) {
    return element(HtmlElementName.H2, contents);
  }

  /**
   * Generates the {@code h2} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h2(String text) {
    return element(HtmlElementName.H2, text);
  }

  /**
   * Generates the {@code h3} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h3(Html.Instruction... contents) {
    return element(HtmlElementName.H3, contents);
  }

  /**
   * Generates the {@code h3} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h3(String text) {
    return element(HtmlElementName.H3, text);
  }

  /**
   * Generates the {@code h4} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h4(Html.Instruction... contents) {
    return element(HtmlElementName.H4, contents);
  }

  /**
   * Generates the {@code h4} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h4(String text) {
    return element(HtmlElementName.H4, text);
  }

  /**
   * Generates the {@code h5} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h5(Html.Instruction... contents) {
    return element(HtmlElementName.H5, contents);
  }

  /**
   * Generates the {@code h5} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h5(String text) {
    return element(HtmlElementName.H5, text);
  }

  /**
   * Generates the {@code h6} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h6(Html.Instruction... contents) {
    return element(HtmlElementName.H6, contents);
  }

  /**
   * Generates the {@code h6} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h6(String text) {
    return element(HtmlElementName.H6, text);
  }

  /**
   * Generates the {@code head} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement head(Html.Instruction... contents) {
    return element(HtmlElementName.HEAD, contents);
  }

  /**
   * Generates the {@code head} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement head(String text) {
    return element(HtmlElementName.HEAD, text);
  }

  /**
   * Generates the {@code header} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement header(Html.Instruction... contents) {
    return element(HtmlElementName.HEADER, contents);
  }

  /**
   * Generates the {@code header} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement header(String text) {
    return element(HtmlElementName.HEADER, text);
  }

  /**
   * Generates the {@code hgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
    return element(HtmlElementName.HGROUP, contents);
  }

  /**
   * Generates the {@code hgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement hgroup(String text) {
    return element(HtmlElementName.HGROUP, text);
  }

  /**
   * Generates the {@code hr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.HR, contents);
  }

  /**
   * Generates the {@code html} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement html(Html.Instruction... contents) {
    return element(HtmlElementName.HTML, contents);
  }

  /**
   * Generates the {@code html} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement html(String text) {
    return element(HtmlElementName.HTML, text);
  }

  /**
   * Generates the {@code img} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.IMG, contents);
  }

  /**
   * Generates the {@code input} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.INPUT, contents);
  }

  /**
   * Generates the {@code kbd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
    return element(HtmlElementName.KBD, contents);
  }

  /**
   * Generates the {@code kbd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement kbd(String text) {
    return element(HtmlElementName.KBD, text);
  }

  /**
   * Generates the {@code label} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement label(Html.Instruction... contents) {
    return element(HtmlElementName.LABEL, contents);
  }

  /**
   * Generates the {@code label} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.Instruction.OfElement label(String text) {
    ambiguous(HtmlAmbiguous.LABEL, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code legend} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement legend(Html.Instruction... contents) {
    return element(HtmlElementName.LEGEND, contents);
  }

  /**
   * Generates the {@code legend} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement legend(String text) {
    return element(HtmlElementName.LEGEND, text);
  }

  /**
   * Generates the {@code li} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement li(Html.Instruction... contents) {
    return element(HtmlElementName.LI, contents);
  }

  /**
   * Generates the {@code li} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement li(String text) {
    return element(HtmlElementName.LI, text);
  }

  /**
   * Generates the {@code link} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.LINK, contents);
  }

  /**
   * Generates the {@code main} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement main(Html.Instruction... contents) {
    return element(HtmlElementName.MAIN, contents);
  }

  /**
   * Generates the {@code main} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement main(String text) {
    return element(HtmlElementName.MAIN, text);
  }

  /**
   * Generates the {@code menu} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement menu(Html.Instruction... contents) {
    return element(HtmlElementName.MENU, contents);
  }

  /**
   * Generates the {@code menu} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement menu(String text) {
    return element(HtmlElementName.MENU, text);
  }

  /**
   * Generates the {@code meta} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.META, contents);
  }

  /**
   * Generates the {@code nav} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement nav(Html.Instruction... contents) {
    return element(HtmlElementName.NAV, contents);
  }

  /**
   * Generates the {@code nav} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement nav(String text) {
    return element(HtmlElementName.NAV, text);
  }

  /**
   * Generates the {@code ol} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement ol(Html.Instruction... contents) {
    return element(HtmlElementName.OL, contents);
  }

  /**
   * Generates the {@code ol} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement ol(String text) {
    return element(HtmlElementName.OL, text);
  }

  /**
   * Generates the {@code optgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
    return element(HtmlElementName.OPTGROUP, contents);
  }

  /**
   * Generates the {@code optgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement optgroup(String text) {
    return element(HtmlElementName.OPTGROUP, text);
  }

  /**
   * Generates the {@code option} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement option(Html.Instruction... contents) {
    return element(HtmlElementName.OPTION, contents);
  }

  /**
   * Generates the {@code option} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement option(String text) {
    return element(HtmlElementName.OPTION, text);
  }

  /**
   * Generates the {@code p} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement p(Html.Instruction... contents) {
    return element(HtmlElementName.P, contents);
  }

  /**
   * Generates the {@code p} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement p(String text) {
    return element(HtmlElementName.P, text);
  }

  /**
   * Generates the {@code path} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement path(Html.Instruction... contents) {
    return element(HtmlElementName.PATH, contents);
  }

  /**
   * Generates the {@code path} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement path(String text) {
    return element(HtmlElementName.PATH, text);
  }

  /**
   * Generates the {@code pre} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement pre(Html.Instruction... contents) {
    return element(HtmlElementName.PRE, contents);
  }

  /**
   * Generates the {@code pre} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement pre(String text) {
    return element(HtmlElementName.PRE, text);
  }

  /**
   * Generates the {@code progress} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement progress(Html.Instruction... contents) {
    return element(HtmlElementName.PROGRESS, contents);
  }

  /**
   * Generates the {@code progress} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement progress(String text) {
    return element(HtmlElementName.PROGRESS, text);
  }

  /**
   * Generates the {@code samp} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement samp(Html.Instruction... contents) {
    return element(HtmlElementName.SAMP, contents);
  }

  /**
   * Generates the {@code samp} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement samp(String text) {
    return element(HtmlElementName.SAMP, text);
  }

  /**
   * Generates the {@code script} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement script(Html.Instruction... contents) {
    return element(HtmlElementName.SCRIPT, contents);
  }

  /**
   * Generates the {@code script} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement script(String text) {
    return element(HtmlElementName.SCRIPT, text);
  }

  /**
   * Generates the {@code section} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement section(Html.Instruction... contents) {
    return element(HtmlElementName.SECTION, contents);
  }

  /**
   * Generates the {@code section} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement section(String text) {
    return element(HtmlElementName.SECTION, text);
  }

  /**
   * Generates the {@code select} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement select(Html.Instruction... contents) {
    return element(HtmlElementName.SELECT, contents);
  }

  /**
   * Generates the {@code select} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement select(String text) {
    return element(HtmlElementName.SELECT, text);
  }

  /**
   * Generates the {@code small} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement small(Html.Instruction... contents) {
    return element(HtmlElementName.SMALL, contents);
  }

  /**
   * Generates the {@code small} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement small(String text) {
    return element(HtmlElementName.SMALL, text);
  }

  /**
   * Generates the {@code span} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement span(Html.Instruction... contents) {
    return element(HtmlElementName.SPAN, contents);
  }

  /**
   * Generates the {@code span} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement span(String text) {
    return element(HtmlElementName.SPAN, text);
  }

  /**
   * Generates the {@code strong} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement strong(Html.Instruction... contents) {
    return element(HtmlElementName.STRONG, contents);
  }

  /**
   * Generates the {@code strong} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement strong(String text) {
    return element(HtmlElementName.STRONG, text);
  }

  /**
   * Generates the {@code style} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement style(Html.Instruction... contents) {
    return element(HtmlElementName.STYLE, contents);
  }

  /**
   * Generates the {@code style} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement style(String text) {
    return element(HtmlElementName.STYLE, text);
  }

  /**
   * Generates the {@code sub} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement sub(Html.Instruction... contents) {
    return element(HtmlElementName.SUB, contents);
  }

  /**
   * Generates the {@code sub} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement sub(String text) {
    return element(HtmlElementName.SUB, text);
  }

  /**
   * Generates the {@code summary} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement summary(Html.Instruction... contents) {
    return element(HtmlElementName.SUMMARY, contents);
  }

  /**
   * Generates the {@code summary} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement summary(String text) {
    return element(HtmlElementName.SUMMARY, text);
  }

  /**
   * Generates the {@code sup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement sup(Html.Instruction... contents) {
    return element(HtmlElementName.SUP, contents);
  }

  /**
   * Generates the {@code sup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement sup(String text) {
    return element(HtmlElementName.SUP, text);
  }

  /**
   * Generates the {@code svg} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement svg(Html.Instruction... contents) {
    return element(HtmlElementName.SVG, contents);
  }

  /**
   * Generates the {@code svg} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement svg(String text) {
    return element(HtmlElementName.SVG, text);
  }

  /**
   * Generates the {@code table} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement table(Html.Instruction... contents) {
    return element(HtmlElementName.TABLE, contents);
  }

  /**
   * Generates the {@code table} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement table(String text) {
    return element(HtmlElementName.TABLE, text);
  }

  /**
   * Generates the {@code tbody} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
    return element(HtmlElementName.TBODY, contents);
  }

  /**
   * Generates the {@code tbody} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement tbody(String text) {
    return element(HtmlElementName.TBODY, text);
  }

  /**
   * Generates the {@code td} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement td(Html.Instruction... contents) {
    return element(HtmlElementName.TD, contents);
  }

  /**
   * Generates the {@code td} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement td(String text) {
    return element(HtmlElementName.TD, text);
  }

  /**
   * Generates the {@code template} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement template(Html.Instruction... contents) {
    return element(HtmlElementName.TEMPLATE, contents);
  }

  /**
   * Generates the {@code template} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement template(String text) {
    return element(HtmlElementName.TEMPLATE, text);
  }

  /**
   * Generates the {@code textarea} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
    return element(HtmlElementName.TEXTAREA, contents);
  }

  /**
   * Generates the {@code textarea} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement textarea(String text) {
    return element(HtmlElementName.TEXTAREA, text);
  }

  /**
   * Generates the {@code th} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement th(Html.Instruction... contents) {
    return element(HtmlElementName.TH, contents);
  }

  /**
   * Generates the {@code th} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement th(String text) {
    return element(HtmlElementName.TH, text);
  }

  /**
   * Generates the {@code thead} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement thead(Html.Instruction... contents) {
    return element(HtmlElementName.THEAD, contents);
  }

  /**
   * Generates the {@code thead} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement thead(String text) {
    return element(HtmlElementName.THEAD, text);
  }

  /**
   * Generates the {@code title} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement title(Html.Instruction... contents) {
    return element(HtmlElementName.TITLE, contents);
  }

  /**
   * Generates the {@code title} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.Instruction.OfElement title(String text) {
    ambiguous(HtmlAmbiguous.TITLE, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code tr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement tr(Html.Instruction... contents) {
    return element(HtmlElementName.TR, contents);
  }

  /**
   * Generates the {@code tr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement tr(String text) {
    return element(HtmlElementName.TR, text);
  }

  /**
   * Generates the {@code ul} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement ul(Html.Instruction... contents) {
    return element(HtmlElementName.UL, contents);
  }

  /**
   * Generates the {@code ul} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement ul(String text) {
    return element(HtmlElementName.UL, text);
  }

}