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
import objectos.html.AttributeOrElement;
import objectos.html.HtmlFragment;
import objectos.html.HtmlTemplate;
import objectos.html.Lambda;
import objectos.html.TemplateDsl;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.StandardAttributeName;
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
  public void addAttribute(AttributeName name) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void addAttribute(AttributeName name, String value) {
    int code = name.getCode(); // name implicit null-check
    value = value.toString(); // value implicit null-check

    int startIndex = protoIndex;

    protoAdd(
      ByteProto2.ATTRIBUTE, NULL,
      code, objectAdd(value),
      startIndex, ByteProto2.ATTRIBUTE
    );

    int endIndex = protoIndex;

    protoArray[startIndex + 1] = endIndex;
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

    int startIndex = protoIndex;

    protoAdd(
      ByteProto2.ATTR_OR_ELEM, NULL,
      code, objectAdd(text),
      startIndex, ByteProto2.ATTR_OR_ELEM
    );

    int endIndex = protoIndex;

    protoArray[startIndex + 1] = endIndex;
  }

  @Override
  public final void addDoctype() {
    int startIndex = protoIndex;

    protoAdd(
      ByteProto2.DOCTYPE, NULL,
      startIndex, ByteProto2.DOCTYPE
    );

    int endIndex = protoIndex;

    protoArray[startIndex + 1] = endIndex;
  }

  @Override
  public final void addElement(ElementName name, String text) {
    int code = name.getCode(); // name implicit null-check

    int textIndex = protoIndex;

    addText(text);

    protoArray[textIndex] = ByteProto2.MARKED;

    int startIndex = protoIndex;

    protoAdd(
      ByteProto2.ELEMENT, NULL, code,
      ByteProto2.TEXT, textIndex,
      ByteProto2.ELEMENT_END,
      textIndex, startIndex, ByteProto2.ELEMENT
    );

    int endIndex = protoIndex;

    protoArray[startIndex + 1] = endIndex;
  }

  @Override
  public final void addElement(ElementName name, Value... values) {
    int code = name.getCode(); // name implicit null-check
    int length = values.length; // values implicit null-check

    int contents = protoIndex;

    int attr = NULL;
    int elem = NULL;
    int lambda = NULL;
    int attrElem = NULL;

    for (int i = 0; i < length; i++) {
      var value = Check.notNull(values[i], "values[", i, "] == null");

      int before = protoIndex;

      value.render(this);

      if (protoIndex > before) {
        continue;
      }

      var proto = protoArray[--contents];

      switch (proto) {
        case ByteProto2.ATTRIBUTE -> {
          attr = protoArray[--contents];

          contents = attr;
        }

        case ByteProto2.ATTR_OR_ELEM -> {
          attrElem = protoArray[--contents];

          contents = attrElem;
        }

        case ByteProto2.ELEMENT -> {
          elem = protoArray[--contents];

          contents = protoArray[--contents];
        }

        case ByteProto2.LAMBDA -> {
          lambda = protoArray[--contents];

          contents = lambda;
        }

        case ByteProto2.TEXT -> {
          elem = protoArray[--contents];

          contents = elem;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    int selfStart = protoIndex;

    protoAdd(ByteProto2.ELEMENT, NULL, code);

    stackPush(attrElem);
    stackPush(lambda); // 2
    stackPush(elem); // 1
    if (attr == NULL) {
      stackPush(contents); // 0
    } else {
      stackPush(attr); // 0
    }

    for (int i = 0; i < length; i++) {
      var value = values[i];

      value.mark(this);
    }

    stackPop();
    stackPop();
    stackPop();
    stackPop();

    protoAdd(ByteProto2.ELEMENT_END);

    protoAdd(contents, selfStart, ByteProto2.ELEMENT);

    int selfEnd = protoIndex;

    protoArray[selfStart + 1] = selfEnd;
  }

  @Override
  public void addFragment(HtmlFragment fragment) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void addLambda(Lambda lambda) {
    int lambdaStart = protoIndex;

    protoAdd(ByteProto2.LAMBDA, NULL);

    lambda.apply();

    protoAdd(lambdaStart, ByteProto2.LAMBDA);

    int end = protoIndex;

    protoArray[lambdaStart + 1] = end;
  }

  @Override
  public void addRaw(String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void addText(String text) {
    Objects.requireNonNull(text, "text == null");

    int startIndex = protoIndex;

    protoAdd(
      ByteProto2.TEXT, NULL,
      objectAdd(text),
      startIndex, ByteProto2.TEXT
    );

    int endIndex = protoIndex;

    protoArray[startIndex + 1] = endIndex;
  }

  @Override
  public final void markAttribute() {
    var attr = stackPeek(0);

    int proto = protoArray[attr];

    while (proto != ByteProto2.ATTRIBUTE) {
      attr = protoArray[attr + 1];

      proto = protoArray[attr];
    }

    protoArray[attr] = ByteProto2.MARKED;

    protoAdd(proto, attr);

    attr = protoArray[attr + 1];

    stackSet(0, attr);
  }

  @Override
  public final void markAttributeOrElement() {
    var attrElem = stackPeek(3);

    int proto = protoArray[attrElem];

    while (proto != ByteProto2.ATTR_OR_ELEM) {
      attrElem = protoArray[attrElem + 1];

      proto = protoArray[attrElem];
    }

    protoArray[attrElem] = ByteProto2.MARKED;

    protoAdd(proto, attrElem);

    attrElem = protoArray[attrElem + 1];

    stackSet(3, attrElem);
  }

  @Override
  public final void markElement() {
    var elem = stackPeek(1);

    if (elem == NULL) {
      throw new UnsupportedOperationException("Implement me");
    }

    int proto = protoArray[elem];

    while (proto != ByteProto2.ELEMENT) {
      elem = protoArray[elem + 1];

      proto = protoArray[elem];
    }

    protoArray[elem] = ByteProto2.MARKED;

    protoAdd(proto, elem);

    elem = protoArray[elem + 1];

    stackSet(1, elem);
  }

  @Override
  public final void markLambda() {
    var lambda = stackPeek(2);

    if (lambda == NULL) {
      throw new UnsupportedOperationException("Implement me");
    }

    int proto = protoArray[lambda];

    while (proto != ByteProto2.LAMBDA) {
      lambda = protoArray[lambda + 1];

      proto = protoArray[lambda];
    }

    protoArray[lambda] = ByteProto2.MARKED;

    int tail = protoArray[lambda + 1];

    int lambdaIndex = tail - 2;

    int start = lambda + 2;

    int stackStart = stackIndex + 1;

    while (lambdaIndex > start) {
      proto = protoArray[--lambdaIndex];

      switch (proto) {
        case ByteProto2.ELEMENT -> {
          int elem = protoArray[--lambdaIndex];

          lambdaIndex = protoArray[--lambdaIndex];

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

    lambda = tail;

    stackSet(2, lambda);
  }

  @Override
  public void markRaw() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void markRootElement() {
  }

  @Override
  public void markTemplate() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void markText() {
    var elem = stackPeek(1);

    if (elem == NULL) {
      throw new UnsupportedOperationException("Implement me");
    }

    int proto = protoArray[elem];

    while (proto != ByteProto2.TEXT) {
      elem = protoArray[elem + 1];

      proto = protoArray[elem];
    }

    protoArray[elem] = ByteProto2.MARKED;

    protoAdd(proto, elem);

    elem = protoArray[elem + 1];

    stackSet(1, elem);
  }

  public final void record(HtmlTemplate template) {
    objectIndex = 0;

    protoIndex = 0;

    stackIndex = -1;

    template.acceptTemplateDsl(this);

    var rootIndex = protoIndex;

    while (rootIndex > 0) {
      int proto = protoArray[--rootIndex];

      switch (proto) {
        case ByteProto2.DOCTYPE -> {
          rootIndex = protoArray[--rootIndex];

          stackPush(proto);
        }

        case ByteProto2.ELEMENT -> {
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

    protoAdd(ByteProto2.ROOT);

    while (stackIndex >= 0) {
      protoAdd(stackPop());
    }

    protoAdd(ByteProto2.ROOT_END);

    objectIndex = protoIndex;

    protoIndex = returnTo;
  }

  final int stackPop() {
    return stackArray[stackIndex--];
  }

  final void stackPush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);
    stackArray[++stackIndex] = v0;
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

  private void stackSet(int offset, int value) {
    stackArray[stackIndex - offset] = value;
  }

}