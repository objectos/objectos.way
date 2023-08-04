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
import objectos.css.tmpl.Api;
import objectos.css.tmpl.Api.FilterFunction;
import objectos.css.util.ClassSelector;
import objectos.css.util.CustomProperty;
import objectos.util.ByteArrays;
import objectos.util.ObjectArrays;

class Compiler01 extends CssTemplateApi {

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
  public final void colorHex(String value) {
    int index;
    index = objectAdd(value);

    mainAdd(
      ByteProto.COLOR_HEX,

      Bytes.two0(index),
      Bytes.two1(index)
    );
  }

  @Override
  public final void compilationBegin() {
    aux = new byte[128];

    main = new byte[256];
  }

  @Override
  public final void compilationEnd() {
  }

  @Override
  public final void customPropertyBegin(CustomProperty<?> property) {
    declarationBeginCommon();

    // we store the property name
    String name;
    name = property.cssName;

    int nameIndex;
    nameIndex = objectAdd(name);

    mainAdd(
      ByteProto.PROPERTY_CUSTOM,

      // name
      Bytes.two0(nameIndex),
      Bytes.two1(nameIndex)
    );
  }

  @Override
  public final void customPropertyEnd() {
    declarationEnd();
  }

  @Override
  public final void declarationBegin(Property name) {
    declarationBeginCommon();

    mainAdd(
      ByteProto.PROPERTY_STANDARD,

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

    loop: while (index < indexMax) {
      byte mark;
      mark = aux[index++];

      switch (mark) {
        case ByteProto.COMMA -> mainAdd(mark);

        case ByteProto.DECLARATION -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case ByteProto.DECLARATION -> {
                // keep the start index handy
                int startIndex;
                startIndex = contents;

                // mark this declaration
                main[contents++] = ByteProto.MARKED;

                // decode the length
                byte len0;
                len0 = main[contents++];

                byte len1;
                len1 = main[contents++];

                int length;
                length = Bytes.decodeFixedLength(len0, len1);

                // point to next element
                contents += length;

                // ensure main can hold least 3 elements
                // 0   - ByteProto
                // 1-2 - variable length
                main = ByteArrays.growIfNecessary(main, mainIndex + 2);

                main[mainIndex++] = proto;

                length = mainIndex - startIndex;

                mainIndex = Bytes.varInt(main, mainIndex, length);

                continue loop;
              }

              case ByteProto.MARKED -> {
                contents++;

                // decode the length
                byte len0;
                len0 = main[contents++];

                byte len1;
                len1 = main[contents++];

                int length;
                length = Bytes.decodeFixedLength(len0, len1);

                // point to next element
                contents += length;
              }

              case ByteProto.MARKED3 -> contents += 3;

              case ByteProto.MARKED5 -> contents += 5;

              case ByteProto.MARKED6 -> contents += 6;

              case ByteProto.MARKED9 -> contents += 9;

              case ByteProto.MARKED10 -> contents += 10;

              default -> {
                throw new UnsupportedOperationException(
                  "Implement me :: proto=" + proto
                );
              }
            }
          }
        }

        case ByteProto.INTERNAL -> {
          // keep startIndex handy
          int startIndex;
          startIndex = contents;

          // decode the element's length
          byte lengthByte;
          lengthByte = aux[index++];

          int length;
          length = Bytes.toInt(lengthByte, 0);

          // point to next element
          contents += length;

          // keep the old proto handy
          byte proto;
          proto = main[startIndex];

          // mark this element
          main[startIndex] = ByteProto.markedOf(length);

          // ensure main can hold at least 3 elements
          // 0   - ByteProto
          // 1-2 - variable length
          main = ByteArrays.growIfNecessary(main, mainIndex + 2);

          // byte proto
          main[mainIndex++] = proto;

          // variable length
          length = mainIndex - startIndex;

          mainIndex = Bytes.varInt(main, mainIndex, length);
        }

        case ByteProto.RAW,
             ByteProto.STANDARD_NAME -> {
          mainAdd(mark, aux[index++], aux[index++]);
        }

        case ByteProto.ZERO -> {
          mainAdd(mark);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: mark=" + mark
        );
      }
    }

    // ensure main can hold 4 more elements
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    // mark the end
    main[mainIndex++] = ByteProto.DECLARATION_END;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - mainContents - 1;

    mainIndex = Bytes.varIntR(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = ByteProto.DECLARATION;

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
  public final void filterFunction(FilterFunction func) {
    // @ ByteProto
    mainContents--;

    byte proto;
    proto = main[mainContents--];

    switch (proto) {
      case ByteProto.DECLARATION -> {
        byte len0;
        len0 = main[mainContents--];

        int length;
        length = len0;

        if (length < 0) {
          byte len1;
          len1 = main[mainContents--];

          length = Bytes.toVarInt(len0, len1);
        }

        mainContents -= length;
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: proto=" + proto
      );
    }

    auxAdd(proto);
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
  public final void literalDouble(double value) {
    long bits;
    bits = Double.doubleToLongBits(value);

    mainAdd(
      ByteProto.LITERAL_DOUBLE,

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
  public final void literalInt(int value) {
    mainAdd(
      ByteProto.LITERAL_INT,

      Bytes.int0(value),
      Bytes.int1(value),
      Bytes.int2(value),
      Bytes.int3(value)
    );
  }

  @Override
  public final void literalString(String value) {
    int index;
    index = objectAdd(value);

    mainAdd(
      ByteProto.LITERAL_STRING,

      Bytes.two0(index),
      Bytes.two1(index)
    );
  }

  @Override
  public final void mediaRuleBegin() {
    commonBegin(ByteProto.MEDIA_RULE);
  }

  @Override
  public final void mediaRuleElement(Api.MediaRuleElement element) {
    commonElement(element);
  }

  @Override
  public final void mediaRuleEnd() {
    commonEnd(ByteProto.MEDIA_RULE, ByteProto.MEDIA_RULE_END);
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
  public final void propertyHash(Api.StyleDeclaration value) {
    // @ ByteProto
    mainContents--;

    byte proto;
    proto = main[mainContents--];

    switch (proto) {
      case ByteProto.DECLARATION -> {
        byte len0;
        len0 = main[mainContents--];

        int length;
        length = len0;

        if (length < 0) {
          byte len1;
          len1 = main[mainContents--];

          length = Bytes.toVarInt(len0, len1);
        }

        mainContents -= length;
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: proto=" + proto
      );
    }

    auxAdd(proto);
  }

  @Override
  public final void propertyValue(Api.PropertyValue value) {
    if (value instanceof StandardName name) {
      // value is a keyword like color: currentcolor; or display: block;
      // store the enum ordinal
      auxAdd(
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

      auxAdd(ByteProto.INTERNAL, (byte) length);
    }

    else if (value instanceof InternalZero) {
      auxAdd(ByteProto.ZERO);
    }

    else if (value instanceof InternalColor color) {
      String raw;
      raw = color.raw;

      int rawIndex;
      rawIndex = objectAdd(raw);

      auxAdd(
        ByteProto.RAW,
        Bytes.two0(rawIndex),
        Bytes.two1(rawIndex)
      );
    }

    else if (value instanceof InternalLength length) {
      String raw;
      raw = length.raw;

      int rawIndex;
      rawIndex = objectAdd(raw);

      auxAdd(
        ByteProto.RAW,
        Bytes.two0(rawIndex),
        Bytes.two1(rawIndex)
      );
    }

    else if (value instanceof InternalPercentage percentage) {
      String raw;
      raw = percentage.raw;

      int rawIndex;
      rawIndex = objectAdd(raw);

      auxAdd(
        ByteProto.RAW,
        Bytes.two0(rawIndex),
        Bytes.two1(rawIndex)
      );
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + value.getClass()
      );
    }
  }

  @Override
  public final void propertyValueComma() {
    auxAdd(ByteProto.COMMA);
  }

  @Override
  public final void selectorAttribute(String name) {
    int nameIndex;
    nameIndex = objectAdd(name);

    mainAdd(
      ByteProto.SELECTOR_ATTR,

      Bytes.two0(nameIndex),
      Bytes.two1(nameIndex),

      ByteProto.INTERNAL4
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

      ByteProto.INTERNAL7
    );
  }

  @Override
  public final void styleRuleBegin() {
    byte proto;
    proto = ByteProto.STYLE_RULE;

    commonBegin(proto);
  }

  @Override
  public final void styleRuleElement(Api.StyleRuleElement element) {
    commonElement(element);
  }

  @Override
  public final void styleRuleEnd() {
    byte trailerProto;
    trailerProto = ByteProto.STYLE_RULE;

    byte endProto;
    endProto = ByteProto.STYLE_RULE_END;

    commonEnd(trailerProto, endProto);
  }

  @Override
  public final void url(String value) {
    int index;
    index = objectAdd(value);

    mainAdd(
      ByteProto.URL,

      Bytes.two0(index),
      Bytes.two1(index)
    );
  }

  @Override
  public final void varFunction(CustomProperty<?> variable) {
    String name;
    name = variable.cssName;

    int nameIndex;
    nameIndex = objectAdd(name);

    mainAdd(
      ByteProto.VAR0,

      Bytes.two0(nameIndex),
      Bytes.two1(nameIndex)
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

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
      byte b8) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 8);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
    aux[auxIndex++] = b7;
    aux[auxIndex++] = b8;
  }

  final void auxAdd(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7,
      byte b8, byte b9) {
    aux = ByteArrays.growIfNecessary(aux, auxIndex + 9);
    aux[auxIndex++] = b0;
    aux[auxIndex++] = b1;
    aux[auxIndex++] = b2;
    aux[auxIndex++] = b3;
    aux[auxIndex++] = b4;
    aux[auxIndex++] = b5;
    aux[auxIndex++] = b6;
    aux[auxIndex++] = b7;
    aux[auxIndex++] = b8;
    aux[auxIndex++] = b9;
  }

  private void auxVarInt(int value) {
    auxIndex = Bytes.varInt(aux, auxIndex, value);
  }

  private void commonBegin(byte proto) {
    // we mark the start of our aux list
    auxStart = auxIndex;

    // we mark:
    // 1) the start of the contents of the current declaration
    // 2) the start of our main list
    mainContents = mainStart = mainIndex;

    mainAdd(
      proto,

      // length takes 2 bytes
      ByteProto.NULL,
      ByteProto.NULL
    );
  }

  private void commonElement(Object element) {
    if (element instanceof StandardName name) {
      // element is a selector name
      // store the enum ordinal
      auxAdd(
        ByteProto.STANDARD_NAME,
        Bytes.name0(name),
        Bytes.name1(name)
      );
    }

    else if (element instanceof ClassSelector selector) {
      String string;
      string = selector.toString();

      int index;
      index = objectAdd(string);

      auxAdd(
        ByteProto.SELECTOR_CLASS,
        Bytes.two0(index),
        Bytes.two1(index)
      );
    }

    else if (element instanceof StandardPseudoClassSelector selector) {
      auxAdd(
        ByteProto.SELECTOR_PSEUDO_CLASS,
        (byte) selector.ordinal()
      );
    }

    else if (element instanceof StandardPseudoElementSelector selector) {
      auxAdd(
        ByteProto.SELECTOR_PSEUDO_ELEMENT,
        (byte) selector.ordinal()
      );
    }

    else if (element instanceof StandardTypeSelector selector) {
      auxAdd(ByteProto.SELECTOR_TYPE);
      auxVarInt(selector.ordinal());
    }

    else if (element instanceof Combinator combinator) {
      auxAdd(
        ByteProto.SELECTOR_COMBINATOR,
        (byte) combinator.ordinal()
      );
    }

    else if (element == InternalInstruction.INSTANCE) {
      // @ ByteProto
      mainContents--;

      byte proto;
      proto = main[mainContents--];

      switch (proto) {
        case ByteProto.DECLARATION,
             ByteProto.STYLE_RULE -> {
          byte len0;
          len0 = main[mainContents--];

          int length;
          length = len0;

          if (length < 0) {
            byte len1;
            len1 = main[mainContents--];

            length = Bytes.toVarInt(len0, len1);
          }

          mainContents -= length;
        }

        case ByteProto.INTERNAL4 -> mainContents -= 4 - 2;

        case ByteProto.INTERNAL7 -> mainContents -= 7 - 2;

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }

      auxAdd(ByteProto.INTERNAL);
    }

    else if (element instanceof MediaType type) {
      int ordinal;
      ordinal = type.ordinal();

      auxAdd(
        ByteProto.MEDIA_TYPE,
        Bytes.int0(ordinal)
      );
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + element.getClass()
      );
    }
  }

  private void commonEnd(byte trailerProto, byte endProto) {
    // we will iterate over the marked elements
    int index;
    index = auxStart;

    int indexMax;
    indexMax = auxIndex;

    int contents;
    contents = mainContents;

    loop: while (index < indexMax) {
      byte marker;
      marker = aux[index++];

      switch (marker) {
        case ByteProto.INTERNAL -> {
          while (true) {
            byte proto;
            proto = main[contents];

            switch (proto) {
              case ByteProto.DECLARATION,
                   ByteProto.STYLE_RULE -> {
                // keep the start index handy
                int startIndex;
                startIndex = contents;

                // mark this declaration
                main[contents++] = ByteProto.MARKED;

                // decode the length
                byte len0;
                len0 = main[contents++];

                byte len1;
                len1 = main[contents++];

                int length;
                length = Bytes.decodeFixedLength(len0, len1);

                // point to next element
                contents += length;

                // ensure main can hold least 3 elements
                // 0   - ByteProto
                // 1-2 - variable length
                main = ByteArrays.growIfNecessary(main, mainIndex + 2);

                main[mainIndex++] = proto;

                length = mainIndex - startIndex;

                mainIndex = Bytes.varInt(main, mainIndex, length);

                continue loop;
              }

              case ByteProto.MARKED -> {
                contents++;

                // decode the length
                byte len0;
                len0 = main[contents++];

                byte len1;
                len1 = main[contents++];

                int length;
                length = Bytes.decodeFixedLength(len0, len1);

                // point to next element
                contents += length;
              }

              case ByteProto.MARKED3 -> contents += 3;

              case ByteProto.MARKED5 -> contents += 5;

              case ByteProto.MARKED6 -> contents += 6;

              case ByteProto.MARKED9 -> contents += 9;

              case ByteProto.MARKED10 -> contents += 10;

              case ByteProto.SELECTOR_ATTR -> {
                main[contents++] = ByteProto.MARKED4;

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
                // keep the start index handy
                int startIndex;
                startIndex = contents;

                // mark this element
                main[contents] = ByteProto.MARKED7;

                // point to next
                contents += 7;

                // ensure main can hold least 3 elements
                // 0   - ByteProto
                // 1-2 - variable length
                main = ByteArrays.growIfNecessary(main, mainIndex + 2);

                main[mainIndex++] = proto;

                int length;
                length = mainIndex - startIndex;

                mainIndex = Bytes.varInt(main, mainIndex, length);

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

        case ByteProto.MEDIA_TYPE,
             ByteProto.SELECTOR_COMBINATOR,
             ByteProto.SELECTOR_PSEUDO_CLASS,
             ByteProto.SELECTOR_PSEUDO_ELEMENT -> {
          byte ordinal;
          ordinal = aux[index++];

          mainAdd(marker, ordinal);
        }

        case ByteProto.SELECTOR_CLASS,
             ByteProto.STANDARD_NAME -> {
          mainAdd(marker, aux[index++], aux[index++]);
        }

        case ByteProto.SELECTOR_TYPE -> {
          byte b0;
          b0 = aux[index++];

          if (b0 >= 0) {
            mainAdd(marker, b0);
          } else {
            mainAdd(marker, b0, aux[index++]);
          }
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }

    // ensure main can hold 4 more elements
    // 0   - ByteProto.STYLE_RULE_END
    // 1-2 - variable length
    // 2-3 - ByteProto.STYLE_RULE
    main = ByteArrays.growIfNecessary(main, mainIndex + 3);

    // mark the end
    main[mainIndex++] = endProto;

    // store the distance to the contents (yes, reversed)
    int length;
    length = mainIndex - mainContents - 1;

    mainIndex = Bytes.varIntR(main, mainIndex, length);

    // trailer proto
    main[mainIndex++] = trailerProto;

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

  private void declarationBeginCommon() {
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
      ByteProto.NULL
    );
  }

  private void mainAdd(byte b0) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 0);
    main[mainIndex++] = b0;
  }

  private void mainAdd(byte b0, byte b1) {
    main = ByteArrays.growIfNecessary(main, mainIndex + 1);
    main[mainIndex++] = b0;
    main[mainIndex++] = b1;
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