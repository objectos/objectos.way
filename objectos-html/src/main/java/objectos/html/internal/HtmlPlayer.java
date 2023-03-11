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

import java.io.IOException;
import objectos.html.AttributeOrElement;
import objectos.html.HtmlTemplate;
import objectos.html.HtmlTemplate.Visitor;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.util.IntArrays;

public class HtmlPlayer extends HtmlRecorder {

  private static final int CAPACITY = 10;

  private Visitor visitor;

  public final void play(HtmlTemplate.Visitor visitor) throws IOException {
    this.visitor = visitor;

    protoArray = IntArrays.growIfNecessary(protoArray, objectIndex);

    stackIndex = 0;

    try {
      rootElement();
    } finally {
      this.visitor = null;
    }
  }

  final String processHref(String pathName, String attributeValue) {
    var thisName = pathName;
    var thatName = attributeValue;

    int mismatch = 0;
    int minLength = Math.min(pathName.length(), attributeValue.length());

    for (; mismatch < minLength; mismatch++) {
      char thisChar = thisName.charAt(mismatch);
      char thatChar = thatName.charAt(mismatch);

      if (thisChar != thatChar) {
        break;
      }
    }

    if (mismatch == 0) {
      return attributeValue;
    }

    throw new UnsupportedOperationException("Implement me");
  }

  private void attribute() throws IOException {
    protoNext(); // ByteProto.ATTRIBUTE

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    attributeImpl();

    protoIndex = returnTo;
  }

  private void attributeImpl() throws IOException {
    protoNext(); // ByteProto.ATTRIBUTE
    protoNext(); // tail index

    int code = protoNext();
    int value = protoNext();

    attributeImpl(code, value);
  }

  private void attributeImpl(int code, int value) {
    int index = objectIndex;

    while (index < protoArray.length) {
      int proto = protoArray[index];

      if (proto == ByteProto2.ATTRS_END) {
        protoArray = IntArrays.growIfNecessary(protoArray, index + 3);
        protoArray[index++] = code;
        protoArray[index++] = ByteProto2.SINGLE;
        protoArray[index++] = value;
        protoArray[index++] = ByteProto2.ATTRS_END;

        break;
      }

      if (proto != code) {
        index += 3;

        continue;
      }

      int cellStyle = protoArray[index + 1];

      if (cellStyle == ByteProto2.SINGLE) {
        int requiredIndex = stackIndex + CAPACITY + 1;

        stackArray = IntArrays.growIfNecessary(stackArray, requiredIndex);
        stackArray[stackIndex + 0] = 2;
        stackArray[stackIndex + 1] = protoArray[index + 2];
        stackArray[stackIndex + 2] = value;

        protoArray[index + 1] = ByteProto2.LIST;
        protoArray[index + 2] = stackIndex;

        stackIndex = requiredIndex + 1;

        break;
      }

      int listIndex = protoArray[index + 2];

      int length = stackArray[listIndex + 0];

      while (length == CAPACITY) {
        listIndex = stackArray[listIndex + CAPACITY + 1];

        length = stackArray[listIndex + 0];
      }

      int newLength = length + 1;

      stackArray[listIndex + 0] = newLength;
      stackArray[listIndex + newLength] = value;

      if (newLength == CAPACITY) {
        int requiredIndex = stackIndex + CAPACITY + 1;

        stackArray = IntArrays.growIfNecessary(stackArray, requiredIndex);
        stackArray[stackIndex + 0] = 0;

        stackArray[listIndex + newLength + 1] = stackIndex;

        stackIndex = requiredIndex + 1;
      }

      break;
    }
  }

  private void element() throws IOException {
    protoNext(); // ByteProto.ELEMENT

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    elementImpl();

    protoIndex = returnTo;
  }

  private void elementImpl() throws IOException {
    protoNext(); // ByteProto.ELEMENT
    protoNext(); // tail index;

    protoArray[objectIndex] = ByteProto2.ATTRS_END;

    var elemName = StandardElementName.getByCode(protoNext());

    int elem = NULL;

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto2.ATTRIBUTE -> attribute();

        case ByteProto2.ATTR_OR_ELEM -> {
          int before = protoIndex;

          var attribute = maybeAttribute(elemName);

          if (!attribute && elem == NULL) {
            elem = before;
          }
        }

        case ByteProto2.ELEMENT,
             ByteProto2.TEXT,
             ByteProto2.RAW -> {
          if (elem == NULL) {
            elem = protoIndex;
          }

          protoIndex += 2;
        }

        case ByteProto2.ELEMENT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    visitor.startTag(elemName.getName());

    int attrIndex = objectIndex;

    while (attrIndex < protoArray.length) {
      int code = protoArray[attrIndex++];

      if (code == ByteProto2.ATTRS_END) {
        break;
      }

      int cellType = protoArray[attrIndex++];
      int value = protoArray[attrIndex++];

      var name = StandardAttributeName.getByCode(code);

      visitor.attributeStart(name.getName());

      if (cellType == ByteProto2.SINGLE) {
        if (value != NULL) {
          var string = (String) objectGet(value);

          visitor.attributeValue(string);
        }
      } else {
        int base = value;

        while (true) {
          int length = stackArray[base + 0];

          for (int i = 0; i < length; i++) {
            int obj = stackArray[base + i + 1];

            var attributeValue = (String) objectGet(obj);

            attributeValue = processAttributeValue(name, attributeValue);

            visitor.attributeValue(attributeValue);
          }

          if (length == CAPACITY) {
            base = stackArray[base + length + 1];

            continue;
          } else {
            break;
          }
        }
      }

      visitor.attributeEnd();
    }

    var kind = elemName.getKind();

    if (kind.isVoid()) {
      visitor.selfClosingEnd();
    } else {
      visitor.startTagEnd(elemName.getName());

      if (elem != NULL) {
        protoIndex = elem;

        loop: while (protoMore()) {
          int proto = protoPeek();

          switch (proto) {
            case ByteProto2.ATTRIBUTE -> {
              protoNext();
              protoNext();
            }

            case ByteProto2.ATTR_OR_ELEM -> maybeElement(elemName);

            case ByteProto2.ELEMENT -> element();

            case ByteProto2.ELEMENT_END -> {
              break loop;
            }

            case ByteProto2.RAW -> raw();

            case ByteProto2.TEXT -> text();

            default -> throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
            );
          }
        }
      }

      visitor.endTag(elemName.getName());
    }
  }

  private boolean maybeAttribute(StandardElementName parent) {
    protoNext(); // ByteProto.ATTR_OR_ELEM

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    var result = maybeAttributeImpl(parent);

    protoIndex = returnTo;

    return result;
  }

  private boolean maybeAttributeImpl(StandardElementName parent) {
    protoNext(); // ByteProto.ATTR_OR_ELEM
    protoNext(); // tail index

    int code = protoNext();

    var attributeOrElement = AttributeOrElement.get(code);

    if (attributeOrElement.isAttributeOf(parent)) {
      code = attributeOrElement.attributeByteCode();

      int value = protoNext();

      attributeImpl(code, value);

      return true;
    } else {
      return false;
    }
  }

  private void maybeElement(StandardElementName parent) throws IOException {
    protoNext(); // ByteProto.ATTR_OR_ELEM

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    maybeElementImpl(parent);

    protoIndex = returnTo;
  }

  private void maybeElementImpl(StandardElementName parent) throws IOException {
    protoNext(); // ByteProto.ATTR_OR_ELEM
    protoNext(); // tail index

    int code = protoNext();

    var attributeOrElement = AttributeOrElement.get(code);

    if (!attributeOrElement.isAttributeOf(parent)) {
      code = attributeOrElement.elementByteCode();

      var element = StandardElementName.getByCode(code);

      var name = element.getName();

      visitor.startTag(name);

      visitor.startTagEnd(name);

      int value = protoNext();

      var text = (String) objectGet(value);

      visitor.text(text);

      visitor.endTag(name);
    }
  }

  private Object objectGet(int index) {
    return objectArray[index];
  }

  private String processAttributeValue(StandardAttributeName name, String attributeValue) {
    var result = attributeValue;

    if (name == StandardAttributeName.HREF) {
      var pathName = (String) objectArray[0];

      if (pathName != null) {
        result = processHref(pathName, attributeValue);
      }
    }

    return result;
  }

  private boolean protoMore() {
    return protoIndex < protoArray.length;
  }

  private int protoNext() {
    return protoArray[protoIndex++];
  }

  private int protoPeek() {
    return protoArray[protoIndex];
  }

  private void raw() throws IOException {
    protoNext(); // ByteProto.RAW

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    rawImpl();

    protoIndex = returnTo;
  }

  private void rawImpl() throws IOException {
    protoNext(); // ByteProto.RAW
    protoNext(); // tail index

    int index = protoNext();

    var raw = (String) objectGet(index);

    visitor.raw(raw);
  }

  private void rootElement() throws IOException {
    protoNext(); // ByteProto.ROOT

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto2.DOCTYPE -> {
          visitor.doctype();

          protoIndex++;
        }

        case ByteProto2.ELEMENT -> element();

        case ByteProto2.ROOT -> {}

        case ByteProto2.ROOT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

  private void text() throws IOException {
    protoNext(); // ByteProto.TEXT

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    textImpl();

    protoIndex = returnTo;
  }

  private void textImpl() throws IOException {
    protoNext(); // ByteProto.TEXT
    protoNext(); // tail index

    int index = protoNext();

    var text = (String) objectGet(index);

    visitor.text(text);
  }

}