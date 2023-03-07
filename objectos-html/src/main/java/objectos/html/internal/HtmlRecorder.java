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

  Object[] objectArray = new Object[16];

  int objectIndex;

  int[] protoArray = new int[64];

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
    protoAdd(ByteProto2.ATTRIBUTE, code, objectAdd(value), startIndex, ByteProto2.ATTRIBUTE);
  }

  @Override
  public void addAttribute(String name, String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addAttributeOrElement(AttributeOrElement value, String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addDoctype() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addElement(ElementName name, String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void addElement(ElementName name, Value... values) {
    int code = name.getCode(); // name implicit null-check
    int length = values.length; // values implicit null-check

    int itemStart = protoIndex;

    int attr = NULL;
    int lastAttr = NULL;

    for (int i = 0; i < length; i++) {
      var value = Check.notNull(values[i], "values[", i, "] == null");

      executeIfNecessary(value);

      if (itemStart == 0) {
        // html(Margin.V02);
        // no protos but we would get values.length > 0
        continue;
      }

      var proto = protoArray[--itemStart];

      switch (proto) {
        case ByteProto2.ATTRIBUTE -> {
          itemStart = protoArray[--itemStart];

          if (lastAttr == NULL) {
            lastAttr = itemStart;
          }

          attr = itemStart;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    int elemStart = protoIndex;

    protoAdd(ByteProto2.ELEMENT, code);

    // attributes
    for (int i = 0; i < length; i++) {
      var value = values[i];

      if (value instanceof StandardAttributeName std) {
        if (attr == NULL) {
          throw new UnsupportedOperationException("Implement me");
        }

        int proto = protoArray[attr];

        if (proto != ByteProto2.ATTRIBUTE) {
          throw new UnsupportedOperationException("Implement me");
        }

        protoAdd(proto, attr);

        attr += 5;
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: value.class=" + value.getClass());
      }
    }

    var kind = name.getKind();

    if (kind.isVoid()) {
      protoAdd(ByteProto2.ELEMENT_VOID);
    } else {
      protoAdd(ByteProto2.ELEMENT_NORMAL);

      // elements

      protoAdd(ByteProto2.ELEMENT_END);
    }

    protoAdd(itemStart, elemStart, ByteProto2.ELEMENT);
  }

  @Override
  public void addFragment(HtmlFragment fragment) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addLambda(Lambda lambda) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addRaw(String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addText(String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markAttribute() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markAttributeOrElement() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markElement() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void markLambda() {
    throw new UnsupportedOperationException("Implement me");
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
  public void markText() {
    throw new UnsupportedOperationException("Implement me");
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

    protoIndex = returnTo;
  }

  final int stackPop() {
    return stackArray[stackIndex--];
  }

  final void stackPush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);
    stackArray[++stackIndex] = v0;
  }

  private void executeIfNecessary(Value value) {}

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

  private void protoAdd(int v0, int v1, int v2, int v3, int v4) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 4);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
    protoArray[protoIndex++] = v4;
  }

  private void stackPush(int v0, int v1) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 2);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
  }

}