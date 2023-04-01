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

import java.util.Objects;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.FragmentAction;
import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.Instruction.ExternalAttribute;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class HtmlRecorder extends HtmlTemplateApi {

  private static final int MARK_INTERNAL = -1;
  private static final int MARK_FRAGMENT = -2;
  private static final int MARK_TEMPLATE = -3;

  static final int PATH_NAME = 0;
  static final int DOCUMENT = 1;
  static final int ELEMENT = 2;
  static final int ATTRIBUTE = 3;
  static final int TEXT = 4;
  static final int RAW_TEXT = 5;
  static final int OBJECT_INDEX = 6;

  static final int NULL = Integer.MIN_VALUE;

  int[] listArray = new int[8];

  int listIndex;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  @Override
  public final void addAttribute(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    var std = StandardAttributeName.getByName(name);

    if (std == null) {
      throw new IllegalArgumentException(
        """
        The attribute '%s' is either invalid or not implemented yet.
        """.formatted(name)
      );
    }

    addAttribute(std, value);
  }

  @Override
  public final void addNoOp() {
    int start = protoIndex;

    protoAdd(
      ByteProto.NOOP,
      NULL,

      start,
      ByteProto.NOOP
    );

    endSet(start);
  }

  @Override
  public final void markAttribute() {
    listAdd(ByteProto.ATTRIBUTE);
  }

  protected final String $pathName() {
    return (String) objectArray[PATH_NAME];
  }

  protected final void record(InternalHtmlTemplate template) {
    // objectArray[0] is reserved for the path name value
    objectArray[PATH_NAME] = null;

    // objectIndex starts @ 1
    // as objectArray[0] is the path name value
    objectIndex = OBJECT_INDEX;

    listIndex = protoIndex = 0;

    template.acceptTemplateDsl(this);

    var rootIndex = protoIndex;

    while (rootIndex > 0) {
      int proto = protoArray[--rootIndex];

      switch (proto) {
        case ByteProto.DOCTYPE -> {
          rootIndex = protoArray[--rootIndex];

          listPush(proto);
        }

        case ByteProto.ELEMENT -> {
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

    protoAdd(ByteProto.ROOT_END);

    objectIndex = protoIndex;

    protoIndex = returnTo;
  }

  @Override
  final void addAmbiguous(Ambiguous name, String text) {
    int code = name.code(); // name implicit null-check
    text = text.toString(); // text implicit null-check

    int start = protoIndex;

    protoAdd(
      ByteProto.AMBIGUOUS,
      NULL,

      code,
      objectAdd(text),

      start,
      ByteProto.AMBIGUOUS
    );

    endSet(start);
  }

  @Override
  final void addAttribute(AttributeName name) {
    int code = name.getCode(); // name implicit null-check

    int start = protoIndex;

    protoAdd(
      ByteProto.ATTRIBUTE,
      NULL,

      code,
      NULL,

      start,
      ByteProto.ATTRIBUTE
    );

    endSet(start);
  }

  @Override
  final void addAttribute(AttributeName name, String value) {
    int code = name.getCode(); // name implicit null-check
    value = value.toString(); // value implicit null-check

    int start = protoIndex;

    protoAdd(
      ByteProto.ATTRIBUTE,
      NULL,

      code,
      objectAdd(value),

      start,
      ByteProto.ATTRIBUTE
    );

    endSet(start);
  }

  @Override
  final void addDoctype() {
    int start = protoIndex;

    protoAdd(
      ByteProto.DOCTYPE,
      NULL,

      start,
      ByteProto.DOCTYPE
    );

    endSet(start);
  }

  @Override
  final void addElement(ElementName name, Instruction... values) {
    int code = name.getCode(); // name implicit null-check
    int length = values.length; // contents implicit null-check

    int listBase = listIndex;

    int contents = protoIndex;

    for (int i = 0; i < length; i++) {
      var value = Check.notNull(values[i], "values[", i, "] == null");

      if (value instanceof InternalInstruction) {
        listAdd(MARK_INTERNAL);

        contents = updateContents(contents);
      } else if (value instanceof InternalFragment) {
        listAdd(MARK_FRAGMENT);

        contents = updateContents(contents);
      } else if (value instanceof ExternalAttribute attribute) {
        addElementExternalAttribute(attribute);
      } else if (value instanceof HtmlTemplate template) {
        listAdd(MARK_TEMPLATE);

        addTemplate(template);
      } else if (value instanceof InternalNoOp) {
        // no-op
      } else {
        var type = value.getClass();

        throw new UnsupportedOperationException(
          "Implement me :: type=" + type
        );
      }
    }

    int start = protoIndex;

    protoAdd(ByteProto.ELEMENT, NULL, code);

    int listMax = listIndex;

    listAdd(
      /*3=template*/contents,
      /*2=fragment*/contents,
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
              case ByteProto.AMBIGUOUS,
                   ByteProto.ATTRIBUTE,
                   ByteProto.ELEMENT,
                   ByteProto.RAW,
                   ByteProto.TEXT -> {
                break search;
              }

              case ByteProto.FRAGMENT,
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

          listOffset(1, index);
        }

        case MARK_FRAGMENT -> markImplLambda(2, ByteProto.FRAGMENT);

        case MARK_TEMPLATE -> markImplLambda(3, ByteProto.TEMPLATE);

        default -> throw new UnsupportedOperationException(
          "Implement me :: marker=" + marker
        );
      }
    }

    protoAdd(ByteProto.ELEMENT_END);

    protoAdd(contents, start, ByteProto.ELEMENT);

    endSet(start);

    listIndex = listBase;
  }

  @Override
  final void addElement(ElementName name, String text) {
    int code = name.getCode(); // name implicit null-check

    int contents = protoIndex;

    int type;

    if (name == StandardElementName.SCRIPT || name == StandardElementName.STYLE) {
      type = ByteProto.RAW;

      addRaw(text);
    } else {
      type = ByteProto.TEXT;

      addText(text);
    }

    protoArray[contents] = ByteProto.MARKED;

    int start = protoIndex;

    protoAdd(
      ByteProto.ELEMENT,
      NULL,
      code,

      type, contents,
      ByteProto.ELEMENT_END,

      contents,
      start,
      ByteProto.ELEMENT
    );

    endSet(start);
  }

  @Override
  final void addFragment(FragmentAction action) {
    int start = protoIndex;

    protoAdd(ByteProto.FRAGMENT, NULL);

    action.execute();

    protoAdd(start, ByteProto.FRAGMENT);

    endSet(start);
  }

  @Override
  final void addRaw(String text) {
    Objects.requireNonNull(text, "text == null");

    int start = protoIndex;

    protoAdd(
      ByteProto.RAW,
      NULL,

      objectAdd(text),

      start,
      ByteProto.RAW
    );

    endSet(start);
  }

  @Override
  final void addTemplate(InternalHtmlTemplate template) {
    int start = protoIndex;

    protoAdd(ByteProto.TEMPLATE, NULL);

    template.acceptTemplateDsl(this);

    protoAdd(start, ByteProto.TEMPLATE);

    endSet(start);
  }

  @Override
  final void addText(String text) {
    Objects.requireNonNull(text, "text == null");

    int start = protoIndex;

    protoAdd(
      ByteProto.TEXT,
      NULL,

      objectAdd(text),

      start,
      ByteProto.TEXT
    );

    endSet(start);
  }

  @Override
  final void pathName(String path) {
    objectArray[PATH_NAME] = path;
  }

  private void addElementExternalAttribute(ExternalAttribute attribute) {
    if (attribute instanceof ExternalAttribute.Id ext) {
      var value = externalValue(ext.value());
      addAttribute(StandardAttributeName.ID, value);
      listAdd(MARK_INTERNAL);
    } else if (attribute instanceof ExternalAttribute.StyleClass ext) {
      var value = externalValue(ext.value());
      addAttribute(StandardAttributeName.CLASS, value);
      listAdd(MARK_INTERNAL);
    } else {
      var type = attribute.getClass();

      throw new UnsupportedOperationException(
        "Implement me :: type=" + type
      );
    }
  }

  private String externalValue(String value) {
    if (value == null) {
      return "null";
    } else {
      return value;
    }
  }

  private void endSet(int start) {
    protoArray[start + 1] = protoIndex;
  }

  private void listAdd(int v0) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 0);
    listArray[listIndex++] = v0;
  }

  private void listAdd(int v0, int v1, int v2) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 2);
    listArray[listIndex++] = v0;
    listArray[listIndex++] = v1;
    listArray[listIndex++] = v2;
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

  private void listPush(int v0) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex);

    listArray[listIndex++] = v0;
  }

  private void listPush(int v0, int v1) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 1);
    listArray[listIndex++] = v0;
    listArray[listIndex++] = v1;
  }

  private void markImplLambda(int offset, int type) {
    int thisStart = markSearch(offset, type);

    protoArray[thisStart] = ByteProto.MARKED;

    int tail = protoArray[thisStart + 1];

    int thisIndex = tail - 2;

    int start = thisStart + 2;

    int stackStart = listIndex + 1;

    while (thisIndex > start) {
      int proto = protoArray[--thisIndex];

      switch (proto) {
        case ByteProto.AMBIGUOUS -> {
          int elem = thisIndex = protoArray[--thisIndex];

          listPush(elem, proto);
        }

        case ByteProto.ELEMENT -> {
          int elem = protoArray[--thisIndex];

          thisIndex = protoArray[--thisIndex];

          listPush(elem, proto);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    while (listIndex >= stackStart) {
      protoAdd(listPop());
    }

    thisStart = tail;

    listOffset(offset, thisStart);
  }

  private int markSearch(int offset, int condition) {
    var index = listOffset(offset);

    int proto = protoArray[index];

    while (proto != condition) {
      index = protoArray[index + 1];

      proto = protoArray[index];
    }

    return index;
  }

  private int objectAdd(Object value) {
    int result = objectIndex;
    objectArray = ObjectArrays.growIfNecessary(objectArray, objectIndex);
    objectArray[objectIndex++] = value;
    return result;
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

  private void protoAdd(int v0, int v1, int v2, int v3) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 3);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
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

  private void protoAdd(int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8) {
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

  private int updateContents(int contents) {
    var proto = protoArray[--contents];

    switch (proto) {
      case ByteProto.ATTRIBUTE,
           ByteProto.AMBIGUOUS,
           ByteProto.FRAGMENT,
           ByteProto.RAW,
           ByteProto.TEXT -> {
        contents = protoArray[--contents];
      }

      case ByteProto.ELEMENT -> {
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