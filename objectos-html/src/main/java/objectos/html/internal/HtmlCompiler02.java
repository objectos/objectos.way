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

final class HtmlCompiler02 extends HtmlCompiler01 {

  public final CompiledMarkup compile() {
    return new CompiledMarkup(
      Arrays.copyOf(aux, auxIndex)
    );
  }

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

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      };

      index += length;
    }
  }

  private void element(int index) {
    int children;
    children = 0;

    byte name;
    name = Byte.MIN_VALUE;

    loop: while (index < main.length) {
      byte proto;
      proto = main[index++];

      switch (proto) {
        case ByteProto2.ELEMENT_STANDARD -> {
          name = main[index++];

          auxAdd(ByteCode.START_TAG, name);
        }

        case ByteProto2.END -> {
          if (children == 0) {
            auxAdd(
              ByteCode.GT,
              ByteCode.EMPTY_ELEMENT,
              (byte) mainStart
            );
          }

          auxAdd(ByteCode.END_TAG, name);

          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

}