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
import objectos.html.TemplateDsl;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.AttributeOrElement;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.Lambda;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.html.tmpl.StandardTextElement;
import objectos.html.tmpl.Value;
import objectos.lang.Check;
import objectos.util.IntArrays;
import objectos.util.ObjectArrays;

class HtmlRecorder implements TemplateDsl {

  static final int PATH_NAME = 0;
  static final int DOCUMENT = 1;
  static final int ELEMENT = 2;
  static final int OBJECT_INDEX = 3;

  static final int NULL = Integer.MIN_VALUE;

  int[] listArray = new int[8];

  int listIndex;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  @Override
  public final void addAttribute(AttributeName name) {
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
  public final void addAttribute(AttributeName name, String value) {
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
  public final void addAttributeOrElement(AttributeOrElement value, String text) {
    int code = value.code(); // name implicit null-check
    text = text.toString(); // text implicit null-check

    int start = protoIndex;

    protoAdd(
      ByteProto.ATTR_OR_ELEM,
      NULL,

      code,
      objectAdd(text),

      start,
      ByteProto.ATTR_OR_ELEM
    );

    endSet(start);
  }

  @Override
  public final void addDoctype() {
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
  public final void addElement(ElementName name, String text) {
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
  public final void addElement(ElementName name, Value... values) {
    int code = name.getCode(); // name implicit null-check
    int length = values.length; // values implicit null-check

    int listBase = listIndex;

    int contents = protoIndex;

    for (int i = 0; i < length; i++) {
      var value = Check.notNull(values[i], "values[", i, "] == null");

      if (value instanceof AttributeName) {
        listAdd(ByteProto.ATTRIBUTE);

        contents = updateContents(contents);
      } else if (value instanceof ElementName) {
        listAdd(ByteProto.ELEMENT);

        contents = updateContents(contents);
      } else if (value instanceof Lambda) {
        listAdd(ByteProto.LAMBDA);

        contents = updateContents(contents);
      } else if (value instanceof AttributeOrElement) {
        listAdd(ByteProto.ATTR_OR_ELEM);

        contents = updateContents(contents);
      } else if (value instanceof HtmlTemplate template) {
        listAdd(ByteProto.TEMPLATE);

        addTemplate(template);
      } else if (value instanceof StandardTextElement) {
        listAdd(ByteProto.TEXT);

        contents = updateContents(contents);
      } else if (value instanceof Raw) {
        listAdd(ByteProto.RAW);

        contents = updateContents(contents);
      } else if (value instanceof NoOp) {
        // noop
      } else {
        value.render(this);
        value.mark(this);
      }
    }

    int start = protoIndex;

    protoAdd(ByteProto.ELEMENT, NULL, code);

    int listMax = listIndex;

    listAdd(
      /*6=raw      */contents,
      /*5=template */contents,
      /*4=ambiguous*/contents,
      /*3=lambda   */contents,
      /*2=element  */contents,
      /*1=attribute*/contents
    );

    for (int i = listBase; i < listMax; i++) {
      int proto = listArray[i];

      switch (proto) {
        case ByteProto.ATTRIBUTE -> markImplStandard(1, proto);

        case ByteProto.ELEMENT -> markImplStandard(2, proto);

        case ByteProto.LAMBDA -> markImplLambda(3, proto);

        case ByteProto.ATTR_OR_ELEM -> markImplStandard(4, proto);

        case ByteProto.TEMPLATE -> markImplLambda(5, proto);

        case ByteProto.TEXT -> markImplStandard(2, proto);

        case ByteProto.RAW -> markImplStandard(6, proto);

        default -> throw new UnsupportedOperationException(
          "Implement me :: mark proto=" + proto
        );
      }
    }

    protoAdd(ByteProto.ELEMENT_END);

    protoAdd(contents, start, ByteProto.ELEMENT);

    endSet(start);

    listIndex = listBase;
  }

  @Override
  public final void addLambda(Lambda lambda) {
    int start = protoIndex;

    protoAdd(ByteProto.LAMBDA, NULL);

    lambda.apply();

    protoAdd(start, ByteProto.LAMBDA);

    endSet(start);
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
  public final void addRaw(String text) {
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
  public final void addTemplate(HtmlTemplate template) {
    int start = protoIndex;

    protoAdd(ByteProto.TEMPLATE, NULL);

    template.acceptTemplateDsl(this);

    protoAdd(start, ByteProto.TEMPLATE);

    endSet(start);
  }

  @Override
  public final void addText(String text) {
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
  public final void markAttribute() {
    listAdd(ByteProto.ATTRIBUTE);
  }

  @Override
  public final void pathName(String path) {
    objectArray[PATH_NAME] = path;
  }

  public final void record(HtmlTemplate template) {
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

  private void endSet(int start) {
    protoArray[start + 1] = protoIndex;
  }

  private void listAdd(int v0) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 0);
    listArray[listIndex++] = v0;
  }

  private void listAdd(int v0, int v1, int v2, int v3, int v4, int v5) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 5);
    listArray[listIndex++] = v0;
    listArray[listIndex++] = v1;
    listArray[listIndex++] = v2;
    listArray[listIndex++] = v3;
    listArray[listIndex++] = v4;
    listArray[listIndex++] = v5;
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
        case ByteProto.ATTR_OR_ELEM -> {
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

  private void markImplStandard(int offset, int proto) {
    var index = markSearch(offset, proto);

    protoArray[index] = ByteProto.MARKED;

    protoAdd(proto, index);

    index = protoArray[index + 1];

    listOffset(offset, index);
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
           ByteProto.ATTR_OR_ELEM,
           ByteProto.LAMBDA,
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