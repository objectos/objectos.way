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

import objectos.css.ClassSelector;
import objectos.css.IdSelector;
import objectos.css.tmpl.AttributeValueElement;
import objectos.css.tmpl.StyleRuleElement;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class CssRecorder extends CssTemplateApi {

  private static final int MARK_INTERNAL = -1;
  private static final int MARK_EXTERNAL = -2;

  static final int PSTYLE_SHEET = 0;
  static final int PSTYLE_RULE = 1;
  static final int PSELECTOR = 2;
  static final int PID_SELECTOR = 3;
  static final int PCLASS_SELECTOR = 4;
  static final int PATTRIBUTE_NAME_SELECTOR = 5;
  static final int PATTRIBUTE_VALUE_SELECTOR = 6;
  static final int OBJECT_INDEX = 7;

  int[] listArray = new int[8];

  int listIndex;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  protected final void executeRecorder(InternalCssTemplate template) {
    executeRecorderBefore();

    template.acceptTemplateApi(this);

    executeRecorderAfter();
  }

  @Override
  final InternalInstruction addAttribute(int name, AttributeValueElement element) {
    int index = protoIndex;

    int elementType = protoArray[--index];

    assert elementType == ByteProto.ATTR_VALUE_ELEMENT;

    int elementStart = protoArray[--index];

    protoArray[elementStart] = ByteProto.MARKED;

    int start = protoIndex;

    protoAdd(
      ByteProto.ATTR_VALUE_SELECTOR,
      ByteProto.NULL,
      name,
      elementStart,
      elementStart,
      ByteProto.ATTR_VALUE_SELECTOR
    );

    endSet(start);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final InternalInstruction addInternal(int type, int value) {
    int start = protoIndex;

    protoAdd(
      type,
      ByteProto.NULL,
      value,
      start,
      type
    );

    endSet(start);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final InternalInstruction addInternal(int type, int value0, int value1) {
    int start = protoIndex;

    protoAdd(
      type,
      ByteProto.NULL,
      value0,
      value1,
      start,
      type
    );

    endSet(start);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final int addObject(Object value) {
    int result = objectIndex;
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);
    objectArray[objectIndex++] = value;
    return result;
  }

  @Override
  final void addRule(StyleRuleElement... elements) {
    int length = elements.length; // elements implicit null-check

    int listBase = listIndex;

    int contents = protoIndex;

    for (int i = 0; i < length; i++) {
      var element = Check.notNull(elements[i], "elements[", i, "] == null");

      if (element instanceof InternalInstruction) {
        listAdd(MARK_INTERNAL);

        contents = updateContents(contents);
      } else if (element instanceof ClassSelector classSelector) {
        var className = classSelector.className();

        int index = addObject(className);

        addInternal(ByteProto.CLASS_SELECTOR_EXTERNAL, index);

        listAdd(MARK_EXTERNAL);
      } else if (element instanceof Combinator combinator) {
        int value = combinator.ordinal();

        addInternal(ByteProto.COMBINATOR, value);

        listAdd(MARK_EXTERNAL);
      } else if (element instanceof IdSelector idSelector) {
        var id = idSelector.id();

        int index = addObject(id);

        addInternal(ByteProto.ID_SELECTOR_EXTERNAL, index);

        listAdd(MARK_EXTERNAL);
      } else if (element instanceof PseudoClassSelector pseudoClass) {
        int value = pseudoClass.ordinal();

        addInternal(ByteProto.PSEUDO_CLASS_SELECTOR, value);

        listAdd(MARK_EXTERNAL);
      } else if (element instanceof PseudoElementSelector pseudoElement) {
        int value = pseudoElement.ordinal();

        addInternal(ByteProto.PSEUDO_ELEMENT_SELECTOR, value);

        listAdd(MARK_EXTERNAL);
      } else if (element instanceof TypeSelector typeSelector) {
        int value = typeSelector.ordinal();

        addInternal(ByteProto.TYPE_SELECTOR, value);

        listAdd(MARK_EXTERNAL);
      } else {
        var type = element.getClass();

        throw new UnsupportedOperationException(
          "Implement me :: type=" + type
        );
      }
    }

    int start = protoIndex;

    protoAdd(ByteProto.STYLE_RULE, ByteProto.NULL);

    int listMax = listIndex;

    listAdd(
      /*2=external*/contents,
      /*1=internal*/contents
    );

    for (int i = listBase; i < listMax; i++) {
      int marker = listArray[i];

      switch (marker) {
        case MARK_INTERNAL -> {
          var index = listOffset(1);

          int proto = protoArray[index];

          search: while (true) {
            switch (proto) {
              case ByteProto.ATTR_NAME_SELECTOR,
                   ByteProto.ATTR_VALUE_SELECTOR,
                   ByteProto.CLASS_SELECTOR,
                   ByteProto.ID_SELECTOR -> {
                break search;
              }

              case ByteProto.MARKED -> {
                index = protoArray[index + 1];

                proto = protoArray[index];
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

        case MARK_EXTERNAL -> {
          var index = listOffset(2);

          int proto = protoArray[index];

          search: while (true) {
            switch (proto) {
              case ByteProto.CLASS_SELECTOR_EXTERNAL,
                   ByteProto.COMBINATOR,
                   ByteProto.ID_SELECTOR_EXTERNAL,
                   ByteProto.PSEUDO_CLASS_SELECTOR,
                   ByteProto.PSEUDO_ELEMENT_SELECTOR,
                   ByteProto.TYPE_SELECTOR -> {
                break search;
              }

              case ByteProto.ATTR_VALUE_SELECTOR,
                   ByteProto.CLASS_SELECTOR,
                   ByteProto.ID_SELECTOR,
                   ByteProto.MARKED -> {
                index = protoArray[index + 1];

                proto = protoArray[index];
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

          listOffset(2, index);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }

    protoAdd(ByteProto.STYLE_RULE_END);

    protoAdd(contents, start, ByteProto.STYLE_RULE);

    endSet(start);

    listIndex = listBase;
  }

  final void executeRecorderAfter() {
    var rootIndex = protoIndex;

    while (rootIndex > 0) {
      int proto = protoArray[--rootIndex];

      switch (proto) {
        case ByteProto.STYLE_RULE -> {
          int elem = protoArray[--rootIndex];

          rootIndex = protoArray[--rootIndex];

          listPush(elem, proto);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    int returnTo = protoIndex;

    protoAdd(ByteProto.ROOT);

    while (listIndex > 0) {
      protoAdd(listPop());
    }

    protoAdd(ByteProto.ROOT_END, returnTo);
  }

  final void executeRecorderBefore() {
    listIndex = protoIndex = 0;

    objectIndex = OBJECT_INDEX;
  }

  private void endSet(int start) {
    protoArray[start + 1] = protoIndex;
  }

  private void listAdd(int v0) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 0);
    listArray[listIndex++] = v0;
  }

  private void listAdd(int v0, int v1) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 1);
    listArray[listIndex++] = v0;
    listArray[listIndex++] = v1;
  }

  private int listOffset(int offset) {
    return listArray[listIndex - offset];
  }

  private void listOffset(int offset, int value) {
    listArray[listIndex - offset] = value;
  }

  private int listPop() {
    return listArray[--listIndex];
  }

  private void listPush(int v0, int v1) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 1);
    listArray[listIndex++] = v0;
    listArray[listIndex++] = v1;
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

  private void protoAdd(int v0, int v1, int v2, int v3, int v4, int v5) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 5);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
    protoArray[protoIndex++] = v4;
    protoArray[protoIndex++] = v5;
  }

  private int updateContents(int contents) {
    var proto = protoArray[--contents];

    switch (proto) {
      case ByteProto.ATTR_NAME_SELECTOR,
           ByteProto.ATTR_VALUE_SELECTOR,
           ByteProto.CLASS_SELECTOR,
           ByteProto.ID_SELECTOR -> {
        contents = protoArray[--contents];
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: proto=" + proto
      );
    }

    return contents;
  }

}