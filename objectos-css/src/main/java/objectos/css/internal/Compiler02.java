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
import objectos.util.ObjectArrays;

final class Compiler02 extends Compiler01 {

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

    // holds the indentation level
    mainStart = 0;

    // we will iterate over the main list looking for unmarked elements
    int indexMax;
    indexMax = mainIndex;

    mainIndex = 0;

    while (mainIndex < indexMax) {
      byte proto;
      proto = nextProto();

      int length;
      length = switch (proto) {
        case ByteProto.MARKED -> Bytes.decodeFixedLength(nextProto(), nextProto());

        case ByteProto.MARKED4 -> 4 - 1;

        case ByteProto.MARKED5 -> 5 - 1;

        case ByteProto.MARKED6 -> 6 - 1;

        case ByteProto.MARKED7 -> 7 - 1;

        case ByteProto.MARKED9 -> 9 - 1;

        case ByteProto.MARKED10 -> 10 - 1;

        case ByteProto.STYLE_RULE -> {
          int thisLength;
          thisLength = Bytes.decodeFixedLength(nextProto(), nextProto());

          styleRule();

          yield thisLength;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      };

      mainIndex += length;
    }
  }

  private void declaration(int index) {
    int valueCount = 0;

    loop: while (index < main.length) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case ByteProto.DECLARATION -> {
          // skip distance to end
          index += 2;
        }

        case ByteProto.DECLARATION_END -> {
          auxAdd(ByteCode.SEMICOLON);

          break loop;
        }

        case ByteProto.JAVA_DOUBLE -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(ByteCode.JAVA_DOUBLE);

          int length = 8;

          System.arraycopy(main, index, aux, auxIndex, length);

          auxIndex += length;
          index += length;
        }

        case ByteProto.JAVA_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(
            ByteCode.JAVA_INT,

            main[index++],
            main[index++],
            main[index++],
            main[index++]
          );
        }

        case ByteProto.JAVA_STRING -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(
            ByteCode.JAVA_STRING,

            main[index++],
            main[index++]
          );
        }

        case ByteProto.LENGTH_DOUBLE -> {
          valueCount = spaceIfNecessary(valueCount);

          int baseIndex;
          baseIndex = index;

          index = decodeLength(index);

          int contents;
          contents = baseIndex - auxStart;

          // skip ByteProto
          contents++;

          auxAdd(
            ByteCode.LENGTH_DOUBLE,

            // long pt1
            main[contents++],
            main[contents++],
            main[contents++],
            main[contents++],

            // long pt2
            main[contents++],
            main[contents++],
            main[contents++],
            main[contents++],

            // unit
            main[contents++]
          );
        }

        case ByteProto.LENGTH_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          int baseIndex;
          baseIndex = index;

          index = decodeLength(index);

          int contents;
          contents = baseIndex - auxStart;

          // skip ByteProto
          contents++;

          auxAdd(
            ByteCode.LENGTH_INT,

            // int
            main[contents++],
            main[contents++],
            main[contents++],
            main[contents++],

            // unit
            main[contents++]
          );
        }

        case ByteProto.MARKED -> {
          // skip end length
          index += 2;

          auxAdd(ByteCode.PROPERTY_NAME, main[index++], main[index++]);

          auxAdd(ByteCode.SPACE_OPTIONAL);
        }

        case ByteProto.PERCENTAGE_DOUBLE -> {
          valueCount = spaceIfNecessary(valueCount);

          int baseIndex;
          baseIndex = index;

          index = decodeLength(index);

          int contents;
          contents = baseIndex - auxStart;

          // skip ByteProto
          contents++;

          auxAdd(
            ByteCode.PERCENTAGE_DOUBLE,

            // long pt1
            main[contents++],
            main[contents++],
            main[contents++],
            main[contents++],

            // long pt2
            main[contents++],
            main[contents++],
            main[contents++],
            main[contents++]
          );
        }

        case ByteProto.PERCENTAGE_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          int baseIndex;
          baseIndex = index;

          index = decodeLength(index);

          int contents;
          contents = baseIndex - auxStart;

          // skip ByteProto
          contents++;

          auxAdd(
            ByteCode.PERCENTAGE_INT,

            // int
            main[contents++],
            main[contents++],
            main[contents++],
            main[contents++]
          );
        }

        case ByteProto.STANDARD_NAME -> {
          valueCount = spaceIfNecessary(valueCount);

          auxAdd(ByteCode.KEYWORD, main[index++], main[index++]);
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

      auxStart = Bytes.decodeVariableLength(len0, len1);
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

  private byte nextProto() {
    return main[mainIndex++];
  }

  private Object[] objects() {
    if (objectArray == null) {
      return ObjectArrays.empty();
    }

    return Arrays.copyOf(objectArray, objectIndex);
  }

  private int selectorComma(int selectorCount) {
    if (selectorCount == 0) {
      indentationWrite();
    }

    else {
      auxAdd(ByteCode.COMMA);
    }

    selectorCount++;

    return selectorCount;
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

  private void styleRule() {
    int declarationCount = 0,
        selectorCount = 0;

    loop: while (mainIndex < main.length) {
      byte proto;
      proto = nextProto();

      switch (proto) {
        case ByteProto.DECLARATION -> {
          if (declarationCount == 0) {
            auxAdd(ByteCode.BLOCK_START);

            indentationInc();
          }

          declarationCount++;

          indentationWrite();

          int index;
          index = mainIndex;

          byte len0;
          len0 = nextProto();

          int length;
          length = len0;

          if (length < 0) {
            byte len1;
            len1 = nextProto();

            length = Bytes.decodeVariableLength(len0, len1);
          }

          int elemIndex;
          elemIndex = index - length;

          declaration(elemIndex);
        }

        case ByteProto.SELECTOR_ATTR -> {
          selectorCount = selectorComma(selectorCount);

          auxAdd(
            ByteCode.SELECTOR_ATTR,

            // nameIndex0
            nextProto(),

            // nameIndex1
            nextProto()
          );
        }

        case ByteProto.SELECTOR_ATTR_VALUE -> {
          selectorCount = selectorComma(selectorCount);

          int baseIndex;
          baseIndex = mainIndex;

          mainIndex = decodeLength(mainIndex);

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

        case ByteProto.STANDARD_NAME -> {
          selectorCount = selectorComma(selectorCount);

          auxAdd(ByteCode.SELECTOR, nextProto(), nextProto());
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