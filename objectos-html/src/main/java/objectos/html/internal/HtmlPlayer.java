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

import objectos.html.HtmlTemplate;
import objectos.html.HtmlTemplate.Visitor;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.AttributeOrElement;
import objectos.html.tmpl.CustomAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.util.IntArrays;

public class HtmlPlayer extends HtmlRecorder {

  private static final int CAPACITY = 10;

  private StringBuilder stringBuilder;

  private Visitor visitor;

  public final void play(HtmlTemplate.Visitor visitor) {
    this.visitor = visitor;

    protoArray = IntArrays.growIfNecessary(protoArray, objectIndex);

    stackIndex = 0;

    this.visitor.documentStart();

    try {
      rootElement();
    } finally {
      this.visitor.documentEnd();

      this.visitor = null;
    }
  }

  protected final String $pathName() {
    return (String) objectArray[0];
  }

  /*
   * Visible for testing.
   */
  final String processHref(String pathName, String attributeValue) {
    var thisName = pathName;
    var thatName = attributeValue;

    var thisLen = thisName.length();
    var thatLen = thatName.length();

    int baseDir = -1;
    int dirCount = 0;
    int mismatch = -1;

    for (int i = 0; i < thisLen; i++) {
      char thisChar = thisName.charAt(i);

      if (i < thatLen) {
        char thatChar = thatName.charAt(i);

        if (thisChar == thatChar) {
          if (thisChar == '/') {
            if (mismatch == -1) {
              baseDir = i;
            } else {
              dirCount++;
            }
          }
        } else {
          if (mismatch == -1) {
            mismatch = i;
          }

          if (thisChar == '/') {
            dirCount++;
          }
        }
      } else {
        if (thisChar == '/') {
          dirCount++;
        }
      }
    }

    return switch (mismatch) {
      case -1 -> {
        if (baseDir == -1) {
          yield attributeValue;
        } else {
          int valueIndex = baseDir + 1;

          yield attributeValue.substring(valueIndex);
        }
      }

      case 0 -> {
        if (dirCount == 0) {
          yield attributeValue;
        } else {
          var sb = stringBuilder();

          for (int i = 0; i < dirCount; i++) {
            sb.append("../");
          }

          sb.append(attributeValue);

          yield sb.toString();
        }
      }

      default -> {
        if (dirCount == 0) {
          int valueIndex = baseDir + 1;

          yield attributeValue.substring(valueIndex);
        }

        if (baseDir == -1) {
          var sb = stringBuilder();

          for (int i = 0; i < dirCount; i++) {
            sb.append("../");
          }

          sb.append(attributeValue);

          yield sb.toString();
        }

        var sb = stringBuilder();

        for (int i = 0; i < dirCount; i++) {
          sb.append("../");
        }

        int valueIndex = baseDir + 1;

        var subValue = attributeValue.substring(valueIndex);

        sb.append(subValue);

        yield sb.toString();
      }
    };
  }

  private void attribute() {
    protoNext(); // ByteProto.ATTRIBUTE

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    attributeImpl();

    protoIndex = returnTo;
  }

  private void attributeImpl() {
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

      if (proto == ByteProto.ATTRS_END) {
        protoArray = IntArrays.growIfNecessary(protoArray, index + 3);
        protoArray[index++] = code;
        protoArray[index++] = ByteProto.SINGLE;
        protoArray[index++] = value;
        protoArray[index++] = ByteProto.ATTRS_END;

        break;
      }

      if (proto != code) {
        index += 3;

        continue;
      }

      int cellStyle = protoArray[index + 1];

      if (cellStyle == ByteProto.SINGLE) {
        int requiredIndex = stackIndex + CAPACITY + 1;

        stackArray = IntArrays.growIfNecessary(stackArray, requiredIndex);
        stackArray[stackIndex + 0] = 2;
        stackArray[stackIndex + 1] = protoArray[index + 2];
        stackArray[stackIndex + 2] = value;

        protoArray[index + 1] = ByteProto.LIST;
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

  private String attributeValueImpl(AttributeName name, int obj) {
    var attributeValue = (String) objectGet(obj);

    return processAttributeValue(name, attributeValue);
  }

  private void element() {
    protoNext(); // ByteProto.ELEMENT

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    elementImpl();

    protoIndex = returnTo;
  }

  private void elementImpl() {
    protoNext(); // ByteProto.ELEMENT
    protoNext(); // tail index;

    protoArray[objectIndex] = ByteProto.ATTRS_END;

    var elemName = StandardElementName.getByCode(protoNext());

    int elem = NULL;

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.ATTRIBUTE -> attribute();

        case ByteProto.ATTR_OR_ELEM -> {
          int before = protoIndex;

          var attribute = maybeAttribute(elemName);

          if (!attribute && elem == NULL) {
            elem = before;
          }
        }

        case ByteProto.ELEMENT,
             ByteProto.TEXT,
             ByteProto.RAW -> {
          if (elem == NULL) {
            elem = protoIndex;
          }

          protoIndex += 2;
        }

        case ByteProto.ELEMENT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    visitor.startTag(elemName);

    int attrIndex = objectIndex;

    while (attrIndex < protoArray.length) {
      int code = protoArray[attrIndex++];

      if (code == ByteProto.ATTRS_END) {
        break;
      }

      int cellType = protoArray[attrIndex++];
      int value = protoArray[attrIndex++];

      var name = AttributeName.getByCode(code);

      visitor.attribute(name);

      if (cellType == ByteProto.SINGLE) {
        if (value != NULL) {
          var attrValue = attributeValueImpl(name, value);

          visitor.attributeFirstValue(attrValue);

          visitor.attributeValueEnd();
        }
      } else {
        int base = value;

        int count = 0;

        while (true) {
          int length = stackArray[base + 0];

          for (int i = 0; i < length; i++) {
            int obj = stackArray[base + i + 1];

            var attrValue = attributeValueImpl(name, obj);

            if (count == 0) {
              visitor.attributeFirstValue(attrValue);
            } else {
              visitor.attributeNextValue(attrValue);
            }

            count++;
          }

          if (length == CAPACITY) {
            base = stackArray[base + length + 1];

            continue;
          } else {
            break;
          }
        }

        visitor.attributeValueEnd();
      }
    }

    var kind = elemName.getKind();

    if (kind.isVoid()) {
      visitor.selfClosingEnd();
    } else {
      visitor.startTagEnd(elemName);

      if (elem != NULL) {
        protoIndex = elem;

        loop: while (protoMore()) {
          int proto = protoPeek();

          switch (proto) {
            case ByteProto.ATTRIBUTE -> {
              protoNext();
              protoNext();
            }

            case ByteProto.ATTR_OR_ELEM -> maybeElement(elemName);

            case ByteProto.ELEMENT -> element();

            case ByteProto.ELEMENT_END -> {
              break loop;
            }

            case ByteProto.RAW -> raw();

            case ByteProto.TEXT -> text();

            default -> throw new UnsupportedOperationException(
              "Implement me :: proto=" + proto
            );
          }
        }
      }

      visitor.endTag(elemName);
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

  private void maybeElement(StandardElementName parent) {
    protoNext(); // ByteProto.ATTR_OR_ELEM

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    maybeElementImpl(parent);

    protoIndex = returnTo;
  }

  private void maybeElementImpl(StandardElementName parent) {
    protoNext(); // ByteProto.ATTR_OR_ELEM
    protoNext(); // tail index

    int code = protoNext();

    var attributeOrElement = AttributeOrElement.get(code);

    if (!attributeOrElement.isAttributeOf(parent)) {
      code = attributeOrElement.elementByteCode();

      var element = StandardElementName.getByCode(code);

      visitor.startTag(element);

      visitor.startTagEnd(element);

      int value = protoNext();

      var text = (String) objectGet(value);

      visitor.text(text);

      visitor.endTag(element);
    }
  }

  private Object objectGet(int index) {
    return objectArray[index];
  }

  private String processAttributeValue(AttributeName name, String attributeValue) {
    var result = attributeValue;

    if (name == CustomAttributeName.PATH_TO) {
      var pathName = $pathName();

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

  private void raw() {
    protoNext(); // ByteProto.RAW

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    rawImpl();

    protoIndex = returnTo;
  }

  private void rawImpl() {
    protoNext(); // ByteProto.RAW
    protoNext(); // tail index

    int index = protoNext();

    var raw = (String) objectGet(index);

    visitor.raw(raw);
  }

  private void rootElement() {
    protoNext(); // ByteProto.ROOT

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.DOCTYPE -> {
          visitor.doctype();

          protoIndex++;
        }

        case ByteProto.ELEMENT -> element();

        case ByteProto.ROOT -> {}

        case ByteProto.ROOT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

  private StringBuilder stringBuilder() {
    if (stringBuilder == null) {
      stringBuilder = new StringBuilder();
    } else {
      stringBuilder.setLength(0);
    }

    return stringBuilder;
  }

  private void text() {
    protoNext(); // ByteProto.TEXT

    int location = protoNext();

    int returnTo = protoIndex;

    protoIndex = location;

    textImpl();

    protoIndex = returnTo;
  }

  private void textImpl() {
    protoNext(); // ByteProto.TEXT
    protoNext(); // tail index

    int index = protoNext();

    var text = (String) objectGet(index);

    visitor.text(text);
  }

}