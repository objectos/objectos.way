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
import objectos.css.tmpl.PropertyValue;
import objectos.css.tmpl.StyleRuleElement;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class CssRecorder extends CssTemplateApi {

  private static final int MARK_INTERNAL = -1;
  private static final int MARK_VALUE1 = -2;
  private static final int MARK_VALUE2 = -3;

  static final int PSTYLE_SHEET = 0;
  static final int PSTYLE_RULE = 1;
  static final int PSELECTOR = 2;
  static final int PID_SELECTOR = 3;
  static final int PCLASS_SELECTOR = 4;
  static final int PATTRIBUTE_NAME_SELECTOR = 5;
  static final int PATTRIBUTE_VALUE_SELECTOR = 6;
  static final int PDECLARATION = 7;
  static final int PINT_VALUE = 8;
  static final int PDOUBLE_VALUE = 9;
  static final int PSTRING_VALUE = 10;
  static final int OBJECT_INDEX = 11;

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
  final InternalInstruction addDeclaration(Property property, double value) {
    int start = protoIndex;

    long bits = Double.doubleToLongBits(value);
    int high = (int) (bits >> 32);
    int low = (int) bits;

    protoAdd(
      ByteProto.DECLARATION,
      ByteProto.NULL,
      property.ordinal(),
      ByteProto.DOUBLE_VALUE, high, low,
      ByteProto.DECLARATION_END,
      start, start,
      ByteProto.DECLARATION
    );

    endSet(start);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final InternalInstruction addDeclaration(Property property, int value) {
    int start = protoIndex;

    protoAdd(
      ByteProto.DECLARATION,
      ByteProto.NULL,
      property.ordinal(),
      ByteProto.INT_VALUE, value,
      ByteProto.DECLARATION_END,
      start, start,
      ByteProto.DECLARATION
    );

    endSet(start);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final InternalInstruction addDeclaration(Property property, PropertyValue... values) {
    int listBase = listIndex;

    int contents = addDeclaration0Values(values);

    int start = protoIndex;

    protoAdd(ByteProto.DECLARATION, ByteProto.NULL, property.ordinal());

    addDeclaration1Mark(listBase, contents);

    protoAdd(ByteProto.DECLARATION_END);

    protoAdd(contents, start, ByteProto.DECLARATION);

    endSet(start);

    listIndex = listBase;

    return InternalInstruction.INSTANCE;
  }

  @Override
  final InternalInstruction addDeclaration(Property property, String value) {
    int start = protoIndex;

    int object = addObject(value);

    protoAdd(
      ByteProto.DECLARATION,
      ByteProto.NULL,
      property.ordinal(),
      ByteProto.STRING_VALUE, object,
      ByteProto.DECLARATION_END,
      start, start,
      ByteProto.DECLARATION
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
    int listBase = listIndex;

    int contents = addRule0Elements(elements);

    int start = protoIndex;

    protoAdd(ByteProto.STYLE_RULE, ByteProto.NULL);

    addRule1Mark(listBase, contents);

    protoAdd(ByteProto.STYLE_RULE_END);

    protoAdd(contents, start, ByteProto.STYLE_RULE);

    endSet(start);

    listIndex = listBase;
  }

  @Override
  final PropertyValue addValue(int type, int value) {
    protoAdd(
      type,
      value,
      type
    );

    return InternalInstruction.VALUE3;
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

  private int addDeclaration0Values(PropertyValue[] values) {
    int length = values.length; // elements implicit null-check

    int contents = protoIndex;

    for (int idx = 0; idx < length; idx++) {
      var value = Check.notNull(values[idx], "values[", idx, "] == null");

      if (value == InternalInstruction.VALUE3) {
        contents -= 3;

        listAdd(MARK_INTERNAL);
      } else if (value instanceof Keyword keyword) {
        listAdd(MARK_VALUE2, ByteProto.KEYWORD, keyword.ordinal());
      } else {
        var type = value.getClass();

        throw new UnsupportedOperationException(
          "Implement me :: type=" + type
        );
      }
    }

    return contents;
  }

  private void addDeclaration1Mark(int listBase, int contents) {
    int listMax = listIndex;

    int idx = listBase;

    int internal = contents;

    while (idx < listMax) {
      int marker = listArray[idx++];

      switch (marker) {
        case MARK_INTERNAL -> {
          int proto = protoArray[internal];

          switch (proto) {
            case ByteProto.PX1 -> {
              int value = protoArray[internal + 1];

              protoAdd(proto, value);

              protoArray[internal] = ByteProto.MARKED3;

              internal += 3;
            }

            default -> throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
            );
          }
        }

        case MARK_VALUE2 -> {
          protoAdd(listArray[idx++], listArray[idx++]);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }
  }

  private int addRule0Elements(StyleRuleElement... elements) {
    int length = elements.length; // elements implicit null-check

    int contents = protoIndex;

    for (int i = 0; i < length; i++) {
      var element = Check.notNull(elements[i], "elements[", i, "] == null");

      if (element instanceof InternalInstruction) {
        contents = updateContents(contents);

        listAdd(MARK_INTERNAL);
      } else if (element instanceof ClassSelector selector) {
        var className = selector.className();

        int index = addObject(className);

        listAdd(MARK_VALUE2, ByteProto.CLASS_SELECTOR, index);
      } else if (element instanceof Combinator combinator) {
        int value = combinator.ordinal();

        listAdd(MARK_VALUE2, ByteProto.COMBINATOR, value);
      } else if (element instanceof IdSelector selector) {
        var id = selector.id();

        int index = addObject(id);

        listAdd(MARK_VALUE2, ByteProto.ID_SELECTOR, index);
      } else if (element instanceof PseudoClassSelector selector) {
        int value = selector.ordinal();

        listAdd(MARK_VALUE2, ByteProto.PSEUDO_CLASS_SELECTOR, value);
      } else if (element instanceof PseudoElementSelector selector) {
        int value = selector.ordinal();

        listAdd(MARK_VALUE2, ByteProto.PSEUDO_ELEMENT_SELECTOR, value);
      } else if (element instanceof TypeSelector selector) {
        int value = selector.ordinal();

        listAdd(MARK_VALUE2, ByteProto.TYPE_SELECTOR, value);
      } else if (element instanceof UniversalSelector) {
        listAdd(MARK_VALUE1, ByteProto.UNIVERSAL_SELECTOR);
      } else {
        var type = element.getClass();

        throw new UnsupportedOperationException(
          "Implement me :: type=" + type
        );
      }
    }

    return contents;
  }

  private void addRule1Mark(int listBase, int contents) {
    int listMax = listIndex;

    int idx = listBase;

    int internal = contents;

    loop: while (idx < listMax) {
      int marker = listArray[idx++];

      switch (marker) {
        case MARK_INTERNAL -> {
          int proto = protoArray[internal];

          while (true) {
            switch (proto) {
              case ByteProto.ATTR_NAME_SELECTOR,
                   ByteProto.ATTR_VALUE_SELECTOR,
                   ByteProto.DECLARATION -> {
                protoArray[internal] = ByteProto.MARKED;

                protoAdd(proto, internal);

                internal = protoArray[internal + 1];

                continue loop;
              }

              case ByteProto.CLASS_SELECTOR,
                   ByteProto.ID_SELECTOR -> {
                protoArray[internal] = ByteProto.MARKED;

                int value = protoArray[internal + 2];

                protoAdd(proto, value);

                internal = protoArray[internal + 1];

                continue loop;
              }

              case ByteProto.MARKED -> {
                internal = protoArray[internal + 1];

                proto = protoArray[internal];
              }

              case ByteProto.MARKED3 -> {
                internal += 3;

                proto = protoArray[internal];
              }

              default -> {
                throw new UnsupportedOperationException(
                  "Implement me :: proto=" + proto
                );
              }
            }
          }
        }

        case MARK_VALUE1 -> protoAdd(listArray[idx++]);

        case MARK_VALUE2 -> protoAdd(listArray[idx++], listArray[idx++]);

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }
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

  private void listAdd(int v0, int v1, int v2) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 2);
    listArray[listIndex++] = v0;
    listArray[listIndex++] = v1;
    listArray[listIndex++] = v2;
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

  private void protoAdd(
      int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 8);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
    protoArray[protoIndex++] = v4;
    protoArray[protoIndex++] = v5;
    protoArray[protoIndex++] = v6;
    protoArray[protoIndex++] = v7;
    protoArray[protoIndex++] = v8;
  }

  private void protoAdd(
      int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8, int v9) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 9);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
    protoArray[protoIndex++] = v4;
    protoArray[protoIndex++] = v5;
    protoArray[protoIndex++] = v6;
    protoArray[protoIndex++] = v7;
    protoArray[protoIndex++] = v8;
    protoArray[protoIndex++] = v9;
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

      case ByteProto.DECLARATION -> {
        --contents;

        contents = protoArray[--contents];
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: proto=" + proto
      );
    }

    return contents;
  }

}