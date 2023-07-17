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

  private Object[] objects() {
    if (objectArray == null) {
      return ObjectArrays.empty();
    }

    return Arrays.copyOf(objectArray, objectIndex);
  }

  @Override
  public final void optimize() {
    // we will use the aux list to store our byte code
    auxIndex = 0;

    // holds the indentation level
    mainStart = 0;

    // we will iterate over the main list looking for unmarked elements
    int index;
    index = 0;

    int indexMax;
    indexMax = mainIndex;

    while (index < indexMax) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case ByteProto.MARKED -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          int length;
          length = Bytes.decodeFixedLength(len0, len1);

          index += length;
        }

        case ByteProto.MARKED4 -> index += 4 - 1;

        case ByteProto.MARKED5 -> index += 5 - 1;

        case ByteProto.MARKED6 -> index += 6 - 1;

        case ByteProto.MARKED7 -> index += 7 - 1;

        case ByteProto.MARKED9 -> index += 9 - 1;

        case ByteProto.MARKED10 -> index += 10 - 1;

        case ByteProto.STYLE_RULE -> {
          byte len0;
          len0 = main[index++];

          byte len1;
          len1 = main[index++];

          styleRule(index);

          int length;
          length = Bytes.decodeFixedLength(len0, len1);

          index += length;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

  private void declaration(int index) {
    int valueCount = 0;

    loop: while (index < main.length) {
      byte proto = main[index++];

      switch (proto) {
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

          int thisIndex = mainIndex(index) + 1;

          index += 3;

          auxAdd(ByteCode.LENGTH_DOUBLE);

          int length = 9;

          System.arraycopy(main, thisIndex, aux, auxIndex, length);

          auxIndex += length;
        }

        case ByteProto.LENGTH_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          int thisIndex = mainIndex(index) + 1;

          index += 3;

          auxAdd(ByteCode.LENGTH_INT);

          int length = 5;

          System.arraycopy(main, thisIndex, aux, auxIndex, length);

          auxIndex += length;
        }

        case ByteProto.MARKED -> {
          // skip end length
          index += 2;

          auxAdd(ByteCode.PROPERTY_NAME, main[index++], main[index++]);

          auxAdd(ByteCode.SPACE_OPTIONAL);
        }

        case ByteProto.PERCENTAGE_DOUBLE -> {
          valueCount = spaceIfNecessary(valueCount);

          int thisIndex = mainIndex(index) + 1;

          index += 3;

          auxAdd(ByteCode.PERCENTAGE_DOUBLE);

          int length = 8;

          System.arraycopy(main, thisIndex, aux, auxIndex, length);

          auxIndex += length;
        }

        case ByteProto.PERCENTAGE_INT -> {
          valueCount = spaceIfNecessary(valueCount);

          int thisIndex = mainIndex(index) + 1;

          index += 3;

          auxAdd(ByteCode.PERCENTAGE_INT);

          int length = 4;

          System.arraycopy(main, thisIndex, aux, auxIndex, length);

          auxIndex += length;
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

          int elemIndex = mainIndex(index);

          index += 3;

          declaration(elemIndex);
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

          int elemIndex;
          elemIndex = mainIndex(index);

          index += 3;

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

          auxAdd(ByteCode.SELECTOR, main[index++], main[index++]);
        }

        case ByteProto.STYLE_RULE -> {
          // skip distance to end
          index += 2;
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

  private int mainIndex(int offset) {
    int idx0;
    idx0 = Bytes.toInt(main[offset + 0], 0);

    int idx1;
    idx1 = Bytes.toInt(main[offset + 1], 8);

    int idx2;
    idx2 = Bytes.toInt(main[offset + 2], 16);

    return idx2 | idx1 | idx0;
  }

}