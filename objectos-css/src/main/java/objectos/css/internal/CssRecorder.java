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

import objectos.css.tmpl.Instruction;
import objectos.css.tmpl.Instruction.ExternalSelector;
import objectos.lang.Check;
import objectos.util.IntArrays;

class CssRecorder extends CssTemplateApi {

  private static final int MARK_SELECTOR = -1;

  static final int NULL = Integer.MIN_VALUE;

  int[] listArray = new int[8];

  int listIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  protected final void executeRecorder(InternalCssTemplate template) {
    listIndex = protoIndex = 0;

    template.acceptTemplateApi(this);
  }

  @Override
  final void addRule(Instruction[] elements) {
    int length = elements.length; // elements implicit null-check

    int listBase = listIndex;

    int contents = protoIndex;

    for (int i = 0; i < length; i++) {
      var element = Check.notNull(elements[i], "elements[", i, "] == null");

      if (element instanceof ExternalSelector selector) {
        addRuleExternalSelector(selector);
      } else {
        var type = element.getClass();

        throw new UnsupportedOperationException(
          "Implement me :: type=" + type
        );
      }
    }

    int start = protoIndex;

    protoAdd(ByteProto.RULE, NULL);

    int listMax = listIndex;

    listAdd(
      /*1=internal*/contents
    );

    for (int i = listBase; i < listMax; i++) {
      int marker = listArray[i];

      switch (marker) {
        case MARK_SELECTOR -> {
          var index = listOffset(1);

          int proto = protoArray[index];

          search: while (true) {
            switch (proto) {
              case ByteProto.TYPE_SELECTOR -> {
                break search;
              }

              default -> {
                throw new UnsupportedOperationException(
                  "Implement me :: proto=" + proto
                );
              }
            }
          }

          protoArray[index] = ByteProto.MARKED;

          protoAdd(proto, index);

          index = protoArray[index + 1];

          listOffset(1, index);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }

    protoAdd(ByteProto.RULE_END);

    protoAdd(contents, start, ByteProto.RULE);

    endSet(start);

    listIndex = listBase;
  }

  private void addRuleExternalSelector(ExternalSelector selector) {
    if (selector instanceof ExternalTypeSelector typeSelector) {
      int value = typeSelector.ordinal();

      int start = protoIndex;

      protoAdd(
        ByteProto.TYPE_SELECTOR,
        NULL,

        value,

        start,
        ByteProto.TYPE_SELECTOR
      );

      listAdd(MARK_SELECTOR);
    } else {
      var type = selector.getClass();

      throw new UnsupportedOperationException(
        "Implement me :: selector=" + type
      );
    }
  }

  private void endSet(int start) {
    protoArray[start + 1] = protoIndex;
  }

  private void listAdd(int v0) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 0);
    listArray[listIndex++] = v0;
  }

  private int listOffset(int offset) {
    return listArray[listIndex - offset];
  }

  private void listOffset(int offset, int value) {
    listArray[listIndex - offset] = value;
  }

  private void protoAdd(int v0) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 0);
    protoArray[protoIndex++] = v0;
  }

  private void protoAdd(int v0, int v1) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 1);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
  }

  private void protoAdd(int v0, int v1, int v2) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 2);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
  }

  private void protoAdd(int v0, int v1, int v2, int v3, int v4) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 4);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
    protoArray[protoIndex++] = v4;
  }

}