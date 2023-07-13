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

final class Compiler02 extends Compiler01 {

  public final void optimize() {
    // we will use the aux list to store our byte code
    auxIndex = 0;

    // holds the indentation level
    mainStart = 0;

    int mainLength;
    mainLength = mainIndex;

    int index;
    index = mainIndex(mainLength - 3);

    loop: while (index < mainLength) {
      byte proto = main[index++];

      switch (proto) {
        case ByteProto.ROOT -> {}

        case ByteProto.ROOT_END -> {
          break loop;
        }

        case ByteProto.STYLE_RULE -> {
          int thisIndex = mainIndex(index);

          index += 3;

          styleRule(thisIndex);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

  private void declaration(int index) {
    loop: while (index < main.length) {
      byte proto = main[index++];

      switch (proto) {
        case ByteProto.DECLARATION_END -> {
          auxAdd(ByteCode.SEMICOLON);

          break loop;
        }

        case ByteProto.MARKED -> {
          // skip end index
          index += 3;

          auxAdd(ByteCode.PROPERTY_NAME, main[index++], main[index++]);
        }

        case ByteProto.STANDARD_NAME -> {
          auxAdd(ByteCode.KEYWORD, main[index++], main[index++]);
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

  private void styleRule(int index) {
    int declarationCount = 0,
        selectorCount = 0;

    loop: while (index < main.length) {
      byte proto = main[index++];

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

        case ByteProto.STANDARD_NAME -> {
          if (selectorCount == 0) {
            indentationWrite();
          }

          else {
            auxAdd(ByteCode.COMMA);
          }

          selectorCount++;

          auxAdd(ByteCode.SELECTOR, main[index++], main[index++]);
        }

        case ByteProto.STYLE_RULE -> {
          // skip end index
          index += 3;
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
      auxAdd(ByteCode.BLOCK_START, ByteCode.BLOCK_END);
    }

    else {
      semicolonOptional();
      indentationDec();
      indentationWrite();
      auxAdd(ByteCode.BLOCK_END);
    }
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

}