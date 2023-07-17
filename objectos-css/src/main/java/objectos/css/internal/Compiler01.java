/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.css.AttributeOperator;
import objectos.css.om.PropertyValue;
import objectos.css.tmpl.StyleRuleElement;
import objectos.util.ByteArrays;
import objectos.util.ObjectArrays;

class Compiler01 extends CssTemplateApi {

  private static final byte MARK_INTERNAL = -1;
  private static final byte MARK_VALUE1 = -2;
  private static final byte MARK_VALUE3 = -4;

  byte[] aux;

  int auxIndex;

  int auxStart;

  byte[] main;

  int mainContents;

  int mainIndex;

  int mainStart;

  Object[] objectArray;

  int objectIndex;

  @Override
  public final void compilationBegin() {
    aux = new byte[128];

    main = new byte[256];
  }

  @Override
  public final void compilationEnd() {
    // we will iterate over the unmarked elements in the main list
    int index;
    index = mainIndex;

    // @ last ByteProto
    index--;

    while (index > 0) {
      byte proto;
      proto = main[index--];

      switch (proto) {
        case ByteProto.STYLE_RULE -> {
          // decode the distance to the start
          byte len0;
          len0 = main[index--];

          int length;
          length = Bytes.toInt(len0, 0);

          byte len1;
          len1 = main[index--];

          length |= Bytes.toInt(len1, 8);

          int indexStart;
          indexStart = index - length;

          // store indices so they can be retrieved by iterating over aux list from end to start
          auxAdd(
            Bytes.idx2(indexStart),
            Bytes.idx1(indexStart),
            Bytes.idx0(indexStart),

            proto
          );

          // decode the distance to the contents
          len0 = main[index--];

          length = Bytes.toInt(len0, 0);

          len1 = main[index--];

          length |= Bytes.toInt(len1, 8);

          // new root @ this elements' contents index
          index -= length;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    int rootStart = mainIndex;

    mainAdd(ByteProto.ROOT);

    while (auxIndex > 0) {
      mainAdd(aux[--auxIndex]);
    }

    mainAdd(
      ByteProto.ROOT_END,
      Bytes.idx0(rootStart),
      Bytes.idx1(rootStart),
      Bytes.idx2(rootStart)
    );
  }

  @Override
  public final void declarationBegin(Property name) {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;

    mainAdd(
      ByteProto.DECLARATION,

      // length takes 2 bytes
      ByteProto.NULL,
      ByteProto.NULL,

      // name
      Bytes.prop0(name),
      Bytes.prop1(name)
    );
  }

  @Override
  public final void declarationEnd() {
    // we iterate over each value added via declarationValue(PropertyValue)
    int index;
    index = auxStart;

    int indexMax;
    indexMax = auxIndex;

    int contents;
    contents = mainContents;

    while (index < indexMax) {
      byte mark;
      mark = aux[index++];

      switch (mark) {
        case MARK_INTERNAL -> {
          byte lengthByte;
          lengthByte = aux[index++];

          int length;
          length = lengthByte & 0xFF;

          byte proto;
          proto = main[contents];

          main[contents] = ByteProto.markedOf(length);

          mainAdd(
            proto,

            Bytes.idx0(contents),
            Bytes.idx1(contents),
            Bytes.idx2(contents)
          );

          contents += length;
        }

        case MARK_VALUE1 -> {
          mainAdd(aux[index++]);
        }

        case MARK_VALUE3 -> {
          mainAdd(aux[index++], aux[index++], aux[index++]);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: mark=" + mark
        );
      }
    }

    int length;
    length = mainIndex - mainContents;

    mainAdd(
      ByteProto.DECLARATION_END,

      // length: yes, backwards
      Bytes.len1(length),
      Bytes.len0(length),

      ByteProto.DECLARATION
    );

    // set the end index of the declaration
    length = mainIndex - mainStart;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // we skip the first byte proto
    main[mainStart + 1] = Bytes.len0(length);
    main[mainStart + 2] = Bytes.len1(length);

    // we clear the aux list
    auxIndex = auxStart;
  }

  @Override
  public final void javaDouble(double value) {
    long bits;
    bits = Double.doubleToLongBits(value);

    mainAdd(
      ByteProto.JAVA_DOUBLE,

      Bytes.long0(bits),
      Bytes.long1(bits),
      Bytes.long2(bits),
      Bytes.long3(bits),
      Bytes.long4(bits),
      Bytes.long5(bits),
      Bytes.long6(bits),
      Bytes.long7(bits)
    );
  }

  @Override
  public final void javaInt(int value) {
    mainAdd(
      ByteProto.JAVA_INT,

      Bytes.int0(value),
      Bytes.int1(value),
      Bytes.int2(value),
      Bytes.int3(value)
    );
  }

  @Override
  public final void javaString(String value) {
    int index;
    index = objectAdd(value);

    mainAdd(
      ByteProto.JAVA_STRING,

      Bytes.two0(index),
      Bytes.two1(index)
    );
  }

  @Override
  public final void length(double value, LengthUnit unit) {
    long bits;
    bits = Double.doubleToLongBits(value);

    int unitOrdinal;
    unitOrdinal = unit.ordinal();

    mainAdd(
      ByteProto.LENGTH_DOUBLE,

      Bytes.long0(bits),
      Bytes.long1(bits),
      Bytes.long2(bits),
      Bytes.long3(bits),
      Bytes.long4(bits),
      Bytes.long5(bits),
      Bytes.long6(bits),
      Bytes.long7(bits),
      (byte) unitOrdinal
    );
  }

  @Override
  public final void length(int value, LengthUnit unit) {
    int unitOrdinal;
    unitOrdinal = unit.ordinal();

    mainAdd(
      ByteProto.LENGTH_INT,

      Bytes.int0(value),
      Bytes.int1(value),
      Bytes.int2(value),
      Bytes.int3(value),
      (byte) unitOrdinal
    );
  }

  @Override
  public final void percentage(double value) {
    long bits;
    bits = Double.doubleToLongBits(value);

    mainAdd(
      ByteProto.PERCENTAGE_DOUBLE,

      Bytes.long0(bits),
      Bytes.long1(bits),
      Bytes.long2(bits),
      Bytes.long3(bits),
      Bytes.long4(bits),
      Bytes.long5(bits),
      Bytes.long6(bits),
      Bytes.long7(bits)
    );
  }

  @Override
  public final void percentage(int value) {
    mainAdd(
      ByteProto.PERCENTAGE_INT,

      Bytes.int0(value),
      Bytes.int1(value),
      Bytes.int2(value),
      Bytes.int3(value)
    );
  }

  @Override
  public final void propertyValue(PropertyValue value) {
    if (value instanceof StandardName name) {
      // value is a keyword like color: currentcolor; or display: block;
      // store the enum ordinal
      auxAdd(
        MARK_VALUE3,
        ByteProto.STANDARD_NAME,
        Bytes.name0(name),
        Bytes.name1(name)
      );
    }

    else if (value instanceof InternalInstruction internal) {
      int length;
      length = internal.length;

      if (length < 0) {
        throw new UnsupportedOperationException(
          "Implement me :: internal=" + internal.name()
        );
      }

      mainContents -= length;

      if (length > 255) {
        throw new AssertionError(
          "Length is too large. Cannot store it in 1 byte."
        );
      }

      auxAdd(MARK_INTERNAL, (byte) length);
    }

    else if (value instanceof InternalZero) {
      auxAdd(MARK_VALUE1, ByteProto.ZERO);
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + value.getClass()
      );
    }
  }

  @Override
  public final void selectorAttribute(String name) {
    int nameIndex;
    nameIndex = objectAdd(name);

    mainAdd(
      ByteProto.SELECTOR_ATTR,

      Bytes.two0(nameIndex),
      Bytes.two1(nameIndex),

      ByteProto.INTERNAL3
    );
  }

  @Override
  public final void selectorAttribute(String name, AttributeOperator operator, String value) {
    int nameIndex;
    nameIndex = objectAdd(name);

    InternalAttributeOperator operatorActual;
    operatorActual = (InternalAttributeOperator) operator;

    int operatorOrdinal;
    operatorOrdinal = operatorActual.ordinal();

    int valueIndex;
    valueIndex = objectAdd(value);

    mainAdd(
      ByteProto.SELECTOR_ATTR_VALUE,

      Bytes.two0(nameIndex),
      Bytes.two1(nameIndex),

      (byte) operatorOrdinal,

      Bytes.two0(valueIndex),
      Bytes.two1(valueIndex),

      ByteProto.INTERNAL6
    );
  }

  @Override
  public final void styleRuleBegin() {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;

    mainAdd(
      ByteProto.STYLE_RULE,

      // length takes 2 bytes
      ByteProto.NULL,
      ByteProto.NULL
    );
  }

  @Override
  public final void styleRuleElement(StyleRuleElement element) {
    if (element instanceof StandardName name) {
      // element is a selector name
      // store the enum ordinal
      auxAdd(
        MARK_VALUE3,
        ByteProto.STANDARD_NAME,
        Bytes.name0(name),
        Bytes.name1(name)
      );
    }

    else if (element == InternalInstruction.INSTANCE) {
      // @ ByteProto
      mainContents--;

      byte proto;
      proto = main[mainContents--];

      switch (proto) {
        case ByteProto.DECLARATION -> {
          byte len0;
          len0 = main[mainContents--];

          int length;
          length = Bytes.toInt(len0, 0);

          byte len1;
          len1 = main[mainContents--];

          length |= Bytes.toInt(len1, 8);

          mainContents -= length;
        }

        case ByteProto.INTERNAL3 -> mainContents -= 2;

        case ByteProto.INTERNAL6 -> mainContents -= 5;

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }

      auxAdd(MARK_INTERNAL);
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + element.getClass()
      );
    }
  }

  @Override
  public final void styleRuleEnd() {
    // we will iterate over the marked elements
    int index;
    index = auxStart;

    int indexMax;
    indexMax = auxIndex;

    int contents;
    contents = mainContents;

    loop: while (index < indexMax) {
      int marker = aux[index++];

      switch (marker) {
        case MARK_INTERNAL -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case ByteProto.DECLARATION -> {
                mainAdd(
                  proto,

                  Bytes.idx0(contents),
                  Bytes.idx1(contents),
                  Bytes.idx2(contents)
                );

                main[contents++] = ByteProto.MARKED;

                byte len0;
                len0 = main[contents++];

                int length;
                length = Bytes.toInt(len0, 0);

                byte len1;
                len1 = main[contents++];

                length |= Bytes.toInt(len1, 8);

                contents += length;

                continue loop;
              }

              case ByteProto.MARKED5 -> contents += 5;

              case ByteProto.MARKED6 -> contents += 6;

              case ByteProto.MARKED9 -> contents += 9;

              case ByteProto.MARKED10 -> contents += 10;

              case ByteProto.SELECTOR_ATTR -> {
                main[contents++] = ByteProto.MARKED3;

                mainAdd(
                  proto,

                  // nameIndex0
                  main[contents++],

                  // nameIndex1
                  main[contents++]
                );

                // ByteProto.INTERNAL3
                contents++;

                continue loop;
              }

              case ByteProto.SELECTOR_ATTR_VALUE -> {
                main[contents] = ByteProto.MARKED6;

                mainAdd(
                  proto,

                  Bytes.idx0(contents),
                  Bytes.idx1(contents),
                  Bytes.idx2(contents)
                );

                contents += 7;

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

        case MARK_VALUE3 -> {
          mainAdd(aux[index++], aux[index++], aux[index++]);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }

    // ensure main can hold 6 more elements
    main = ByteArrays.growIfNecessary(main, mainIndex + 5);

    // mark the end
    main[mainIndex++] = ByteProto.STYLE_RULE_END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - mainContents - 1;

    main[mainIndex++] = Bytes.len1(length);

    main[mainIndex++] = Bytes.len0(length);

    // store the distance to the start (yes, reversed)
    length = mainIndex - mainStart - 1;

    main[mainIndex++] = Bytes.len1(length);

    main[mainIndex++] = Bytes.len0(length);

    // trailer proto
    main[mainIndex++] = ByteProto.STYLE_RULE;

    // set the end index of the declaration
    length = mainIndex - mainStart;

    // skip ByteProto.FOO + len0 + len1
    length -= 3;

    // write after the first byte proto
    main[mainStart + 1] = Bytes.len0(length);

    main[mainStart + 2] = Bytes.len1(length);

    // we clear the aux list
    auxIndex = auxStart;
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

  final int mainIndex(int offset) {
    int idx0;
    idx0 = Bytes.toInt(main[offset + 0], 0);

    int idx1;
    idx1 = Bytes.toInt(main[offset + 1], 8);

    int idx2;
    idx2 = Bytes.toInt(main[offset + 2], 16);

    return idx2 | idx1 | idx0;
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

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 5);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
    main[mainIndex++] = b5;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 6);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
    main[mainIndex++] = b5;
    main[mainIndex++] = b6;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
      byte b8) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 8);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
    main[mainIndex++] = b5;
    main[mainIndex++] = b6;
    main[mainIndex++] = b7;
    main[mainIndex++] = b8;
  }

  private void mainAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
      byte b8, byte b9) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 9);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
    main[mainIndex++] = b2;
    main[mainIndex++] = b3;
    main[mainIndex++] = b4;
    main[mainIndex++] = b5;
    main[mainIndex++] = b6;
    main[mainIndex++] = b7;
    main[mainIndex++] = b8;
    main[mainIndex++] = b9;
  }

  private int objectAdd(Object value) {
    if (objectArray == null) {
      objectArray = new Object[10];
    }

    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);

    int index;
    index = objectIndex++;

    objectArray[index] = value;

    return index;
  }

}