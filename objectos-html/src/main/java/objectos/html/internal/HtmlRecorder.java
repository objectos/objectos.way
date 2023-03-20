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
import objectos.html.Template;
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

public class HtmlRecorder implements TemplateDsl {

  static final int NULL = Integer.MIN_VALUE;

  Object[] objectArray = new Object[64];

  int objectIndex;

  int[] protoArray = new int[256];

  int protoIndex;

  int[] stackArray = new int[8];

  int stackIndex;

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

    if (std != null) {
      addAttribute(std, value);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
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

    int contents = protoIndex;

    int attributes = NULL;
    int elements = NULL;
    int lambdas = NULL;
    int attrOrElems = NULL;
    int templates = NULL;
    int raws = NULL;

    for (int i = 0; i < length; i++) {
      var value = Check.notNull(values[i], "values[", i, "] == null");

      int before = protoIndex;

      value.render(this);

      if (protoIndex > before) {
        if (templates == NULL) {
          // maybe this should be implemented in HtmlTemplate::render instead?
          templates = before;
        }

        continue;
      }

      var proto = protoArray[--contents];

      switch (proto) {
        case ByteProto.ATTRIBUTE -> {
          contents = attributes = protoArray[--contents];
        }

        case ByteProto.ATTR_OR_ELEM -> {
          contents = attrOrElems = protoArray[--contents];
        }

        case ByteProto.ELEMENT -> {
          elements = protoArray[--contents];
          contents = protoArray[--contents];
        }

        case ByteProto.LAMBDA -> {
          contents = lambdas = protoArray[--contents];
        }

        case ByteProto.RAW -> {
          contents = raws = protoArray[--contents];
        }

        case ByteProto.TEXT -> {
          contents = elements = protoArray[--contents];
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    int start = protoIndex;

    protoAdd(ByteProto.ELEMENT, NULL, code);

    stackPush(
      /*5*/raws,
      /*4*/templates,
      /*3*/attrOrElems,
      /*2*/lambdas,
      /*1*/elements,
      /*0*/attributes != NULL ? attributes : contents
    );

    for (int i = 0; i < length; i++) {
      var value = values[i];

      mark(value);
    }

    stackPop(6);

    protoAdd(ByteProto.ELEMENT_END);

    protoAdd(contents, start, ByteProto.ELEMENT);

    endSet(start);
  }

  private void mark(Value value) {
    if (value instanceof AttributeName) {
      markImplStandard(0, ByteProto.ATTRIBUTE);
    } else if (value instanceof ElementName) {
      markImplStandard(1, ByteProto.ELEMENT);
    } else if (value instanceof Lambda) {
      markImplLambda(2, ByteProto.LAMBDA);
    } else if (value instanceof AttributeOrElement) {
      markImplStandard(3, ByteProto.ATTR_OR_ELEM);
    } else if (value instanceof Template) {
      markImplLambda(4, ByteProto.TEMPLATE);
    } else if (value instanceof StandardTextElement) {
      markImplStandard(1, ByteProto.TEXT);
    } else if (value instanceof Raw) {
      markImplStandard(5, ByteProto.RAW);
    } else {
      value.mark(this);
    }
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
    markImplStandard(0, ByteProto.ATTRIBUTE);
  }

  @Override
  public final void pathName(String path) {
    objectArray[0] = path;
  }

  public final void record(HtmlTemplate template) {
    // objectArray[0] is reserved for the path name value
    objectArray[0] = null;

    // objectIndex starts @ 1
    // as objectArray[0] is the path name value
    objectIndex = 1;

    protoIndex = 0;

    stackIndex = -1;

    template.acceptTemplateDsl(this);

    var rootIndex = protoIndex;

    while (rootIndex > 0) {
      int proto = protoArray[--rootIndex];

      switch (proto) {
        case ByteProto.DOCTYPE -> {
          rootIndex = protoArray[--rootIndex];

          stackPush(proto);
        }

        case ByteProto.ELEMENT -> {
          int elem = protoArray[--rootIndex];

          rootIndex = protoArray[--rootIndex];

          stackPush(elem, proto);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    int returnTo = protoIndex;

    protoAdd(ByteProto.ROOT);

    while (stackIndex >= 0) {
      protoAdd(stackPop());
    }

    protoAdd(ByteProto.ROOT_END);

    objectIndex = protoIndex;

    protoIndex = returnTo;
  }

  final int stackPop() {
    return stackArray[stackIndex--];
  }

  final void stackPop(int count) {
    stackIndex -= count;
  }

  final void stackPush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);
    stackArray[++stackIndex] = v0;
  }

  private void endSet(int start) {
    protoArray[start + 1] = protoIndex;
  }

  private void markImplLambda(int offset, int type) {
    int thisStart = markSearch(offset, type);

    protoArray[thisStart] = ByteProto.MARKED;

    int tail = protoArray[thisStart + 1];

    int thisIndex = tail - 2;

    int start = thisStart + 2;

    int stackStart = stackIndex + 1;

    while (thisIndex > start) {
      int proto = protoArray[--thisIndex];

      switch (proto) {
        case ByteProto.ATTR_OR_ELEM -> {
          int elem = thisIndex = protoArray[--thisIndex];

          stackPush(elem, proto);
        }

        case ByteProto.ELEMENT -> {
          int elem = protoArray[--thisIndex];

          thisIndex = protoArray[--thisIndex];

          stackPush(elem, proto);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    while (stackIndex >= stackStart) {
      protoAdd(stackPop());
    }

    thisStart = tail;

    stackSet(offset, thisStart);
  }

  private void markImplStandard(int offset, int proto) {
    var index = markSearch(offset, proto);

    protoArray[index] = ByteProto.MARKED;

    protoAdd(proto, index);

    index = protoArray[index + 1];

    stackSet(offset, index);
  }

  private int markSearch(int offset, int condition) {
    var index = stackPeek(offset);

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

  private int stackPeek(int offset) {
    return stackArray[stackIndex - offset];
  }

  private void stackPush(int v0, int v1) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 2);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
  }

  private void stackPush(int v0, int v1, int v2, int v3, int v4, int v5) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 6);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
    stackArray[++stackIndex] = v2;
    stackArray[++stackIndex] = v3;
    stackArray[++stackIndex] = v4;
    stackArray[++stackIndex] = v5;
  }

  private void stackSet(int offset, int value) {
    stackArray[stackIndex - offset] = value;
  }

}