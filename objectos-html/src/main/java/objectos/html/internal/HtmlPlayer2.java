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

import java.util.NoSuchElementException;
import objectos.html.pseudom.DocumentProcessor;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlElement;
import objectos.html.pseudom.HtmlNode;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.CustomAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.util.IntArrays;

public class HtmlPlayer2 extends HtmlRecorder {

  private static final int _START = -1,
      _DOCUMENT = -2,
      _DOCUMENT_NODES = -3,
      _ELEMENT = -4,
      _ELEMENT_ATTRS_REQ = -5,
      _ELEMENT_ATTRS_ITER = -6,
      _ELEMENT_NODES_REQ = -7,
      _ELEMENT_NODES_ITER = -8,
      _ATTRIBUTE = -9,
      _ATTRIBUTE_VALUES_REQ = -10,
      _ATTRIBUTE_VALUES_ITER = -11;

  private static final int ATTRS_END = -12,
      ATTRS_SINGLE = -13;

  private StringBuilder stringBuilder;

  public HtmlPlayer2() {
    objectArray[DOCUMENT] = new PseudoHtmlDocument(this);

    objectArray[ELEMENT] = new PseudoHtmlElement(this);

    objectArray[ATTRIBUTE] = new PseudoHtmlAttribute(this);
  }

  public final void play(DocumentProcessor processor) {
    listIndex = -1;

    protoArray = IntArrays.growIfNecessary(protoArray, objectIndex);

    ctxPush(_START);

    processor.process(document());
  }

  final void attributeValues() {
    ctxCheck(_ATTRIBUTE);

    ctxSet(0, _ATTRIBUTE_VALUES_REQ);
  }

  final boolean attributeValuesHasNext() {
    ctxCheck(_ATTRIBUTE_VALUES_ITER);

    var type = ctxPeek(1);

    var value = ctxPeek(2);

    var hasNext = false;

    if (type == ATTRS_SINGLE) {
      hasNext = value != NULL;
    } else {
      throw new UnsupportedOperationException("Implement me");
    }

    if (!hasNext) {
      ctxSet(0, _ATTRIBUTE);
    }

    return hasNext;
  }

  final void attributeValuesIterator() {
    ctxCheck(_ATTRIBUTE_VALUES_REQ);

    ctxSet(0, _ATTRIBUTE_VALUES_ITER);
  }

  final String attributeValuesNext(AttributeName name) {
    ctxCheck(_ATTRIBUTE_VALUES_ITER);

    var type = ctxPeek(1);

    var value = ctxPeek(2);

    if (type == ATTRS_SINGLE) {
      if (value == NULL) {
        throw new NoSuchElementException();
      }

      ctxSet(2, NULL);

      return attributeValueImpl(name, value);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  final boolean documentHasNext() {
    documentHasNextCheck();

    var result = false;

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.DOCTYPE,
             ByteProto.ELEMENT -> {
          result = true;

          break loop;
        }

        case ByteProto.ROOT -> protoNext();

        case ByteProto.ROOT_END -> {
          protoNext();

          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    proto2ctx();

    return result;
  }

  final void documentIterable() {
    ctxCheck(_START);

    ctxSet(0, _DOCUMENT);
  }

  final void documentIterator() {
    ctxCheck(_DOCUMENT);

    ctxPush(protoIndex, _DOCUMENT_NODES);
  }

  final HtmlNode documentNext() {
    ctxCheck(_DOCUMENT_NODES);

    ctx2proto();

    int proto = protoNext();

    return switch (proto) {
      case ByteProto.DOCTYPE -> throw new UnsupportedOperationException("Implement me");

      case ByteProto.ELEMENT -> htmlElement();

      case ByteProto.ROOT_END -> throw new NoSuchElementException();

      default -> throw new UnsupportedOperationException(
        "Implement me :: proto=" + proto
      );
    };
  }

  final void elementAttributes() {
    ctxCheck(_ELEMENT);

    ctxSet(0, _ELEMENT_ATTRS_REQ);
  }

  final boolean elementAttributesHasNext() {
    elementAttributesHasNextCheck();

    int index = ctxPeek(1);

    int value = listArray[index];

    if (value == ATTRS_END) {
      int start = ctxPeek(2);

      listIndex = start - 1;

      ctxCheck(_ELEMENT_ATTRS_REQ);

      ctxSet(0, _ELEMENT);

      return false;
    } else {
      return true;
    }
  }

  final void elementAttributesIterator(StandardElementName name) {
    ctxCheck(_ELEMENT_ATTRS_REQ);

    ctx2proto();

    int startIndex = listIndex + 1;

    // end marker
    ctxPush(ATTRS_END);

    // start index to be used in the while-loop
    ctxPush(startIndex);

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.ATTRIBUTE -> attribute();

        //case ByteProto.ATTR_OR_ELEM -> maybeAttribute(name);

        case ByteProto.ELEMENT,
             ByteProto.TEXT,
             ByteProto.RAW -> protoIndex += 2;

        case ByteProto.ELEMENT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    ctxPush(
      startIndex, // cursor for iteration

      _ELEMENT_ATTRS_ITER
    );
  }

  final HtmlAttribute elementAttributesNext() {
    ctxCheck(_ELEMENT_ATTRS_ITER);

    int index = ctxPeek(1);

    int code = listArray[index++];

    if (code == ATTRS_END) {
      throw new NoSuchElementException();
    }

    var attribute = htmlAttribute();

    var name = AttributeName.getByCode(code);

    attribute.name = name;

    var type = listArray[index++];

    var value = listArray[index++];

    ctxSet(1, index);

    ctxPush(index, value, type, _ATTRIBUTE);

    return attribute;
  }

  final void elementNodes() {
    ctxCheck(_ELEMENT);

    ctxSet(0, _ELEMENT_NODES_REQ);
  }

  final boolean elementNodesHasNext() {
    elementNodesHasNextCheck();

    ctx2proto();

    var hasNext = false;

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.ATTRIBUTE -> protoIndex += 2;

        //case ByteProto.ATTR_OR_ELEM -> maybeElement(name);

        case ByteProto.ELEMENT,
             ByteProto.TEXT,
             ByteProto.RAW -> {
          hasNext = true;

          break loop;
        }

        case ByteProto.ELEMENT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    proto2ctx();

    if (!hasNext) {
      ctxPop(2);

      ctxCheck(_ELEMENT_NODES_REQ);

      ctxSet(0, _ELEMENT);

      var htmlElement = element();

      int code = ctxPeek(3);

      htmlElement.name = StandardElementName.getByCode(code);
    }

    return hasNext;
  }

  final void elementNodesIterator() {
    ctxCheck(_ELEMENT_NODES_REQ);

    int index = ctxPeek(1);

    ctxPush(index, _ELEMENT_NODES_ITER);
  }

  final HtmlNode elementNodesNext() {
    ctxCheck(_ELEMENT_NODES_ITER);

    ctx2proto();

    HtmlNode node = null;

    loop: while (protoMore()) {
      int proto = protoNext();

      switch (proto) {
        case ByteProto.ATTRIBUTE -> protoNext();

        //case ByteProto.ATTR_OR_ELEM -> maybeElement(name);

        case ByteProto.ELEMENT -> {
          node = htmlElement();

          break loop;
        }

        case ByteProto.ELEMENT_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    if (node == null) {
      throw new NoSuchElementException();
    }

    return node;
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

    // skip ByteProto.ATTRIBUTE
    // skip tail index
    protoIndex = location + 2;

    int code = protoNext();

    int value = protoNext();

    attributeImpl(code, value);

    protoIndex = returnTo;
  }

  private void attributeImpl(int code, int value) {
    int startIndex = ctxPeek();

    int index = startIndex;

    while (index < listIndex) {
      int attr = ctxGet(index);

      if (attr == ATTRS_END) {
        listIndex = index;

        // pop ATTRS_END so we can overwrite it
        ctxPop(1);

        ctxPush(
          code,
          ATTRS_SINGLE,
          value,
          ATTRS_END,
          startIndex
        );

        break;
      }

      if (attr != code) {
        index += 3;

        continue;
      }

      throw new UnsupportedOperationException("Implement me");
    }
  }

  private String attributeValueImpl(AttributeName name, int value) {
    var attributeValue = (String) objectArray[value];

    return processAttributeValue(name, attributeValue);
  }

  private void ctx2proto() {
    protoIndex = ctxPeek(1);
  }

  private void ctxCheck(int expected) {
    int state = ctxPeek();

    ctxThrow(state, expected);
  }

  private int ctxGet(int index) {
    return listArray[index];
  }

  private int ctxPeek() {
    return listArray[listIndex];
  }

  private int ctxPeek(int offset) {
    return listArray[listIndex - offset];
  }

  private void ctxPop(int count) {
    listIndex -= count;
  }

  private void ctxPush(int v0) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 1);
    listArray[++listIndex] = v0;
  }

  private void ctxPush(int v0, int v1) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 2);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
  }

  private void ctxPush(int v0, int v1, int v2, int v3) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 4);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
    listArray[++listIndex] = v2;
    listArray[++listIndex] = v3;
  }

  private void ctxPush(int v0, int v1, int v2, int v3, int v4) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 5);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
    listArray[++listIndex] = v2;
    listArray[++listIndex] = v3;
    listArray[++listIndex] = v4;
  }

  private void ctxSet(int offset, int value) {
    listArray[listIndex - offset] = value;
  }

  private void ctxThrow(int actual, int expected) {
    if (actual != expected) {
      throw new IllegalStateException(
        """
      Found state '%d' but expected state '%d'
      """.formatted(actual, expected)
      );
    }
  }

  private PseudoHtmlDocument document() {
    return (PseudoHtmlDocument) objectArray[DOCUMENT];
  }

  private void documentHasNextCheck() {
    int peek = ctxPeek();

    switch (peek) {
      case _DOCUMENT_NODES -> ctx2proto();

      case _ELEMENT -> {
        elementPop(_DOCUMENT_NODES);

        ctx2proto();
      }

      default -> ctxThrow(peek, _DOCUMENT_NODES);
    }
  }

  private PseudoHtmlElement element() {
    return (PseudoHtmlElement) objectArray[ELEMENT];
  }

  private void elementAttributesHasNextCheck() {
    int peek = ctxPeek();

    switch (peek) {
      case _ELEMENT_ATTRS_ITER -> {}

      case _ATTRIBUTE -> {
        // this attributes's parent
        int parent = ctxPeek(3);

        // pop _ATTRIBUTE, attr type, attr value, attr parent
        ctxPop(4);

        // parent should be _ELEMENT_ATTRS_ITER
        ctxThrow(ctxPeek(), _ELEMENT_ATTRS_ITER);

        // actual parent index
        int actual = ctxPeek(1);

        if (parent != actual) {
          throw new IllegalStateException(
            """
          Last consumed attribute was not a child of this element
          """
          );
        }
      }

      default -> ctxThrow(peek, _ELEMENT_ATTRS_ITER);
    }
  }

  private void elementNodesHasNextCheck() {
    int peek = ctxPeek();

    switch (peek) {
      case _ELEMENT_NODES_ITER -> {}

      case _ELEMENT -> elementPop(_ELEMENT_NODES_ITER);

      default -> ctxThrow(peek, _DOCUMENT_NODES);
    }
  }

  private void elementPop(int context) {
    // this element's parent
    int parent = ctxPeek(2);

    // pop _ELEMENT, elem index, elem parent, elem code
    ctxPop(4);

    // parent
    int ctx = ctxPeek();

    // parent should be context
    ctxThrow(ctx, context);

    // actual parent index
    int actual = ctxPeek(1);

    if (parent != actual) {
      throw new IllegalStateException(
        """
      Last consumed element was not a child of this document or element
      """
      );
    }
  }

  private PseudoHtmlAttribute htmlAttribute() {
    return (PseudoHtmlAttribute) objectArray[ATTRIBUTE];
  }

  private HtmlElement htmlElement() {
    int location = protoNext();

    int parent = protoIndex;

    proto2ctx();

    var element = element();

    // skip ByteProto.ELEMENT
    // skip tail index
    protoIndex = location + 2;

    int elemCode = protoNext();

    element.name = StandardElementName.getByCode(elemCode);

    ctxPush(elemCode, parent, protoIndex, _ELEMENT);

    return element;
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

  private void proto2ctx() {
    ctxSet(1, protoIndex);
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

  private StringBuilder stringBuilder() {
    if (stringBuilder == null) {
      stringBuilder = new StringBuilder();
    } else {
      stringBuilder.setLength(0);
    }

    return stringBuilder;
  }

}