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
import objectos.html.tmpl.StandardElementName;
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
  public void addAttribute(String name, String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void addAttributeOrElement(AttributeOrElement value, String text) {
    throw new UnsupportedOperationException("Implement me");
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
  public void addElement(ElementName name, String text) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void addElement(ElementName name, Value... values) {
    int code = name.getCode(); // name implicit null-check
    int length = values.length; // values implicit null-check

    int contents = protoIndex;

    int attr = NULL;
    int attrLast = NULL;

    int elem = NULL;
    int elemLast = NULL;

    int lambda = NULL;
    int lambdaLast = NULL;

    for (int i = 0; i < length; i++) {
      var value = Check.notNull(values[i], "values[", i, "] == null");

      executeIfNecessary(value);

      if (contents == 0) {
        // html(Margin.V02);
        // no protos but we would get values.length > 0
        continue;
      }

      var proto = protoArray[--contents];

      switch (proto) {
        case ByteProto2.ATTRIBUTE -> {
          contents = protoArray[--contents];

          if (attrLast == NULL) {
            attrLast = contents;
          }

          attr = contents;
        }

        case ByteProto2.ELEMENT -> {
          elem = protoArray[--contents];

          if (elemLast == NULL) {
            elemLast = elem;
          }

          contents = protoArray[--contents];
        }

        case ByteProto2.LAMBDA -> {
          lambda = protoArray[--contents];

          if (lambdaLast == NULL) {
            lambdaLast = lambda;
          }

          contents = lambda;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    int selfStart = protoIndex;

    protoAdd(ByteProto2.ELEMENT, NULL, code);

    for (int i = 0; i < length; i++) {
      var value = values[i];

      if (value instanceof StandardAttributeName) {
        if (attr == NULL) {
          throw new UnsupportedOperationException("Implement me");
        }

        int proto = protoArray[attr];

        while (proto != ByteProto2.ATTRIBUTE) {
          attr = protoArray[attr + 1];

          proto = protoArray[attr];
        }

        protoAdd(proto, attr);

        attr = protoArray[attr + 1];
      } else if (value instanceof StandardElementName) {
        if (elem == NULL) {
          throw new UnsupportedOperationException("Implement me");
        }

        int proto = protoArray[elem];

        while (proto != ByteProto2.ELEMENT) {
          elem = protoArray[elem + 1];

          proto = protoArray[elem];
        }

        protoAdd(proto, elem);

        elem = protoArray[elem + 1];
      } else if (value instanceof Lambda) {
        if (lambda == NULL) {
          throw new UnsupportedOperationException("Implement me");
        }

        lambda = executeLambda(lambda);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: type=" + value.getClass()
        );
      }
    }

    protoAdd(ByteProto2.ELEMENT_END);

    protoAdd(contents, selfStart, ByteProto2.ELEMENT);

    int selfEnd = protoIndex;

    protoArray[selfStart + 1] = selfEnd;
  }

  private int executeLambda(int lambda) {
    int proto = protoArray[lambda++];

    while (proto != ByteProto2.LAMBDA) {
      lambda = protoArray[lambda];

      proto = protoArray[lambda++];
    }

    int tail = protoArray[lambda++];

    int lambdaIndex = tail - 2;

    int start = lambda;

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

    while (stackIndex >= 0) {
      protoAdd(stackPop());
    }

    return tail;
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

  private void protoAdd(int v0, int v1, int v2, int v3) {
    protoArray = IntArrays.growIfNecessary(protoArray, protoIndex + 3);
    protoArray[protoIndex++] = v0;
    protoArray[protoIndex++] = v1;
    protoArray[protoIndex++] = v2;
    protoArray[protoIndex++] = v3;
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

  private void stackPush(int v0, int v1) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 2);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
  }

}