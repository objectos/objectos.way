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

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import objectos.util.ObjectArrays;

final class Compiler02 extends Compiler01 {

  private static final Pattern FONT_FAMILY = Pattern.compile("-?[a-zA-Z_][a-zA-Z0-9_-]*");

  @Override
  public CompiledStyleSheet compile() {
    return new CompiledStyleSheet(
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

    int ruleCount;
    ruleCount = 0;

    while (index < mainIndex) {
      byte proto;
      proto = main[index++];

      int length;
      length = switch (proto) {
        case ByteProto.MARKED -> Bytes.decodeFixedLength(main[index++], main[index++]);

        case ByteProto.MARKED3 -> 3 - 1;

        case ByteProto.MARKED4 -> 4 - 1;

        case ByteProto.MARKED5 -> 5 - 1;

        case ByteProto.MARKED6 -> 6 - 1;

        case ByteProto.MARKED7 -> 7 - 1;

        case ByteProto.MARKED9 -> 9 - 1;

        case ByteProto.MARKED10 -> 10 - 1;

        case ByteProto.MEDIA_RULE -> {
          ruleCount = nextRuleIf(ruleCount);

          int thisLength;
          thisLength = Bytes.decodeFixedLength(main[index++], main[index++]);

          mediaRule(index);

          yield thisLength;
        }

        case ByteProto.STYLE_RULE -> {
          ruleCount = nextRuleIf(ruleCount);

          int thisLength;
          thisLength = Bytes.decodeFixedLength(main[index++], main[index++]);

          styleRule(index);

          yield thisLength;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      };

      index += length;
    }
  }

  private void declaration(int index) {
    Property property;
    property = null;

    int valueCount;
    valueCount = 0;

    loop: while (index < mainIndex) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case ByteProto.COLOR_HEX -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(ByteCode.COLOR_HEX, main[mainContents++], main[mainContents++]);
        }

        case ByteProto.COMMA -> {
          auxAdd(ByteCode.COMMA);

          valueCount = 0;
        }

        case ByteProto.DECLARATION -> {
          // keep index handy
          int thisIndex;
          thisIndex = index;

          // decode length
          byte len0;
          len0 = main[index++];

          int length;
          length = len0;

          if (length < 0) {
            byte len1;
            len1 = main[index++];

            length = Bytes.toVarInt(len0, len1);
          }

          // compute declaration index
          int elemIndex;
          elemIndex = thisIndex - length;

          // skip ByteProto
          elemIndex += 1;

          // skip end length
          elemIndex += 2;

          byte propertyKind;
          propertyKind = main[elemIndex++];

          byte b0;
          b0 = main[elemIndex++];

          byte b1;
          b1 = main[elemIndex++];

          if (property == Property.FILTER ||
              property == Property._WEBKIT_FILTER) {

            byte propertyByteCode;
            propertyByteCode = switch (propertyKind) {
              case ByteProto.PROPERTY_STANDARD -> ByteCode.PROPERTY_STANDARD;

              default -> throw new UnsupportedOperationException(
                "Implement me :: propertyKind=" + propertyKind
              );
            };

            auxAdd(propertyByteCode, b0, b1, ByteCode.PARENS_OPEN);

            declaration(elemIndex);

            auxAdd(ByteCode.PARENS_CLOSE);
          }

          else {
            declaration(elemIndex);
          }
        }

        case ByteProto.DECLARATION_END -> {
          break loop;
        }

        case ByteProto.JAVA_DOUBLE -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(
            ByteCode.LITERAL_DOUBLE,

            main[index++],
            main[index++],
            main[index++],
            main[index++],
            main[index++],
            main[index++],
            main[index++],
            main[index++]
          );
        }

        case ByteProto.JAVA_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(
            ByteCode.LITERAL_INT,

            main[index++],
            main[index++],
            main[index++],
            main[index++]
          );
        }

        case ByteProto.JAVA_STRING -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(
            ByteCode.LITERAL_STRING,

            main[index++],
            main[index++]
          );
        }

        case ByteProto.LENGTH_DOUBLE -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(
            ByteCode.LENGTH_DOUBLE,

            // long pt1
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],

            // long pt2
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],

            // unit
            main[mainContents++]
          );
        }

        case ByteProto.LENGTH_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(
            ByteCode.LENGTH_INT,

            // int
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],

            // unit
            main[mainContents++]
          );
        }

        case ByteProto.LITERAL_DOUBLE -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(
            ByteCode.LITERAL_DOUBLE,

            // long pt1
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],

            // long pt2
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++]
          );
        }

        case ByteProto.LITERAL_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(
            ByteCode.LITERAL_INT,

            // int
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++]
          );
        }

        case ByteProto.LITERAL_STRING -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          byte byteCode;
          byteCode = ByteCode.LITERAL_STRING;

          byte b0;
          b0 = main[mainContents++];

          byte b1;
          b1 = main[mainContents++];

          if (property == Property.FONT_FAMILY) {
            int objectIndex;
            objectIndex = Bytes.decodeIndex2(b0, b1);

            Object object;
            object = objectArray[objectIndex];

            String s;
            s = (String) object;

            Matcher matcher;
            matcher = FONT_FAMILY.matcher(s);

            if (matcher.matches()) {
              byteCode = ByteCode.STRING_QUOTES_OPTIONAL;
            }
          }

          auxAdd(byteCode, b0, b1);
        }

        case ByteProto.MARKED -> {
          // skip end length
          index += 2;

          byte propertyKind;
          propertyKind = main[index++];

          byte b0;
          b0 = main[index++];

          byte b1;
          b1 = main[index++];

          switch (propertyKind) {
            case ByteProto.PROPERTY_CUSTOM -> {
              auxAdd(ByteCode.PROPERTY_CUSTOM, b0, b1);
            }

            case ByteProto.PROPERTY_STANDARD -> {
              property = Bytes.property(b0, b1);

              auxAdd(ByteCode.PROPERTY_STANDARD, b0, b1);
            }

            default -> throw new UnsupportedOperationException(
              "Implement me :: propertyKind=" + propertyKind
            );
          }

          auxAdd(ByteCode.COLON, ByteCode.SPACE_OPTIONAL);
        }

        case ByteProto.PERCENTAGE_DOUBLE -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(
            ByteCode.PERCENTAGE_DOUBLE,

            // long pt1
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],

            // long pt2
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++]
          );
        }

        case ByteProto.PERCENTAGE_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(
            ByteCode.PERCENTAGE_INT,

            // int
            main[mainContents++],
            main[mainContents++],
            main[mainContents++],
            main[mainContents++]
          );
        }

        case ByteProto.RAW -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(ByteCode.RAW, main[index++], main[index++]);
        }

        case ByteProto.STANDARD_NAME -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(ByteCode.KEYWORD, main[index++], main[index++]);
        }

        case ByteProto.URL -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(ByteCode.URL, main[mainContents++], main[mainContents++]);
        }

        case ByteProto.VAR0 -> {
          valueCount = spaceIfNecessary(valueCount);

          index = jmp(index);

          auxAdd(ByteCode.VAR, main[mainContents++], main[mainContents++], ByteCode.PARENS_CLOSE);
        }

        case ByteProto.ZERO -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(ByteCode.ZERO);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

  private int decodeLength(int index) {
    byte len0;
    len0 = main[index++];

    auxStart = len0;

    if (auxStart < 0) {
      byte len1;
      len1 = main[index++];

      auxStart = Bytes.toVarInt(len0, len1);
    }

    return index;
  }

  private void indentationDec() {
    mainStart--;
  }

  private void indentationInc() {
    mainStart++;
  }

  private void indentationWrite() {
    if (mainStart == 0) {
      return;
    }

    auxAdd(ByteCode.TAB, (byte) mainStart);
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

  private void mediaRule(int index) {
    auxAdd(
      ByteCode.AT_MEDIA,
      ByteCode.SPACE
    );

    int ruleCount;
    ruleCount = 0;

    loop: while (index < main.length) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case ByteProto.DECLARATION -> {
          auxAdd(ByteCode.PARENS_OPEN);

          // keep index handy
          int thisIndex;
          thisIndex = index;

          // decode length
          byte len0;
          len0 = main[index++];

          int length;
          length = len0;

          if (length < 0) {
            byte len1;
            len1 = main[index++];

            length = Bytes.toVarInt(len0, len1);
          }

          // compute declaration index
          int elemIndex;
          elemIndex = thisIndex - length;

          declaration(elemIndex);

          auxAdd(ByteCode.PARENS_CLOSE);
        }

        case ByteProto.MEDIA_RULE_END -> {
          break loop;
        }

        case ByteProto.MEDIA_TYPE -> {
          auxAdd(ByteCode.MEDIA_QUERY, main[index++]);
        }

        case ByteProto.STYLE_RULE -> {
          if (ruleCount == 0) {
            auxAdd(ByteCode.BLOCK_START);

            indentationInc();
          }

          ruleCount++;

          // keep index handy
          int thisIndex;
          thisIndex = index;

          // decode length
          byte len0;
          len0 = main[index++];

          int length;
          length = len0;

          if (length < 0) {
            byte len1;
            len1 = main[index++];

            length = Bytes.toVarInt(len0, len1);
          }

          // compute rule index
          int elemIndex;
          elemIndex = thisIndex - length;

          // skip ByteProto + length to the end
          elemIndex += 3;

          styleRule(elemIndex);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    if (ruleCount == 0) {
      auxAdd(ByteCode.BLOCK_EMPTY);
    }

    else {
      semicolonOptional();
      indentationDec();
      indentationWrite();
      auxAdd(ByteCode.BLOCK_END);
    }
  }

  private int nextRuleIf(int ruleCount) {
    if (ruleCount > 0) {
      auxAdd(ByteCode.NEXT_RULE);
    }

    ruleCount++;

    return ruleCount;
  }

  private Object[] objects() {
    if (objectArray == null) {
      return ObjectArrays.empty();
    }

    return Arrays.copyOf(objectArray, objectIndex);
  }

  private void semicolonOptional() {
    if (auxIndex <= 0) {
      return;
    }

    int lastIndex;
    lastIndex = auxIndex - 1;

    byte last;
    last = aux[lastIndex];

    if (last != ByteCode.SEMICOLON) {
      return;
    }

    aux[lastIndex] = ByteCode.SEMICOLON_OPTIONAL;
  }

  private int spaceIfNecessary(int valueCount) {
    if (valueCount > 0) {
      auxAdd(ByteCode.SPACE);
    }

    valueCount++;

    return valueCount;
  }

  private int selectorComma(int selectorCount) {
    if (selectorCount == 0) {
      indentationWrite();
    }

    selectorCount++;

    return selectorCount;
  }

  private void styleRule(int index) {
    int declarationCount = 0,
        selectorCount = 0;

    loop: while (index < main.length) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case ByteProto.DECLARATION -> {
          if (declarationCount == 0) {
            auxAdd(ByteCode.BLOCK_START);

            indentationInc();
          }

          declarationCount++;

          indentationWrite();

          // keep index handy
          int thisIndex;
          thisIndex = index;

          // decode length
          byte len0;
          len0 = main[index++];

          int length;
          length = len0;

          if (length < 0) {
            byte len1;
            len1 = main[index++];

            length = Bytes.toVarInt(len0, len1);
          }

          // compute declaration index
          int elemIndex;
          elemIndex = thisIndex - length;

          declaration(elemIndex);

          auxAdd(ByteCode.SEMICOLON);
        }

        case ByteProto.SELECTOR_ATTR -> {
          selectorCount = selectorComma(selectorCount);

          auxAdd(
            ByteCode.SELECTOR_ATTR,

            // nameIndex0
            main[index++],

            // nameIndex1
            main[index++]
          );
        }

        case ByteProto.SELECTOR_ATTR_VALUE -> {
          selectorCount = selectorComma(selectorCount);

          int baseIndex;
          baseIndex = index;

          index = decodeLength(index);

          int elemIndex;
          elemIndex = baseIndex - auxStart;

          // skip ByteProto.SELECTOR_ATTR_VALUE
          elemIndex++;

          auxAdd(
            ByteCode.SELECTOR_ATTR_VALUE,

            // nameIndex
            main[elemIndex++],
            main[elemIndex++],

            // operator
            main[elemIndex++],

            // valueIndex
            main[elemIndex++],
            main[elemIndex++]
          );
        }

        case ByteProto.SELECTOR_CLASS -> {
          selectorCount = selectorComma(selectorCount);

          auxAdd(ByteCode.SELECTOR_CLASS, main[index++], main[index++]);
        }

        case ByteProto.SELECTOR_COMBINATOR -> {
          selectorCount = selectorComma(selectorCount);

          auxAdd(ByteCode.SELECTOR_COMBINATOR, main[index++]);
        }

        case ByteProto.SELECTOR_PSEUDO_CLASS -> {
          selectorCount = selectorComma(selectorCount);

          auxAdd(ByteCode.SELECTOR_PSEUDO_CLASS, main[index++]);
        }

        case ByteProto.SELECTOR_PSEUDO_ELEMENT -> {
          selectorCount = selectorComma(selectorCount);

          auxAdd(ByteCode.SELECTOR_PSEUDO_ELEMENT, main[index++]);
        }

        case ByteProto.SELECTOR_TYPE -> {
          selectorCount = selectorComma(selectorCount);

          byte b0;
          b0 = main[index++];

          if (b0 >= 0) {
            auxAdd(ByteCode.SELECTOR_TYPE, b0);
          } else {
            auxAdd(ByteCode.SELECTOR_TYPE, b0, main[index++]);
          }
        }

        case ByteProto.STANDARD_NAME -> {
          selectorCount = selectorComma(selectorCount);

          auxAdd(ByteCode.SELECTOR, main[index++], main[index++]);
        }

        case ByteProto.STYLE_RULE_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    if (declarationCount == 0) {
      auxAdd(ByteCode.BLOCK_EMPTY);
    }

    else {
      semicolonOptional();
      indentationDec();
      indentationWrite();
      auxAdd(ByteCode.BLOCK_END);
    }
  }

}