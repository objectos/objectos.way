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
import objectos.html.pseudom.HtmlElement;
import objectos.html.pseudom.HtmlNode;
import objectos.html.tmpl.StandardElementName;
import objectos.util.IntArrays;

public class HtmlPlayer2 extends HtmlRecorder {

  private static final int _START = 1,
      _DOCUMENT = 2,
      _DOCUMENT_NODES = 3,
      _ELEMENT = 4,
      _ELEMENT_ATTRS_REQ = 5,
      _ELEMENT_ATTRS_ITER = 6,
      _ELEMENT_NODES_REQ = 7,
      _ELEMENT_NODES_ITER = 8;

  private static final int ATTRS_END = -1;

  public HtmlPlayer2() {
    objectArray[DOCUMENT] = new PseudoHtmlDocument(this);

    objectArray[ELEMENT] = new PseudoHtmlElement(this);
  }

  public final void play(DocumentProcessor processor) {
    listIndex = -1;

    protoArray = IntArrays.growIfNecessary(protoArray, objectIndex);

    ctxPush(_START);

    processor.process(document());
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
    ctxCheck(_ELEMENT_ATTRS_ITER);

    int index = ctxGet(1);

    int value = listArray[index];

    if (value == ATTRS_END) {
      int start = ctxGet(2);

      listIndex = start;

      ctxCheck(_ELEMENT_ATTRS_REQ);

      ctxSet(0, _ELEMENT);

      return false;
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  final void elementAttributesIterator(StandardElementName name) {
    ctxCheck(_ELEMENT_ATTRS_REQ);

    ctx2proto();

    int startIndex = listIndex;
    ctxPush(ATTRS_END); // end marker
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
      startIndex + 1, // marks the current index
      _ELEMENT_ATTRS_ITER
    );
  }

  final void elementNodes() {
    ctxCheck(_ELEMENT);

    ctxSet(0, _ELEMENT_NODES_REQ);
  }

  final boolean elementNodesHasNext() {
    ctxCheck(_ELEMENT_NODES_ITER);

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
    }

    return hasNext;
  }

  final void elementNodesIterator() {
    ctxCheck(_ELEMENT_NODES_REQ);

    int index = ctxGet(1);

    ctxPush(index, _ELEMENT_NODES_ITER);
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
    throw new UnsupportedOperationException("Implement me");
  }

  private void ctx2proto() {
    protoIndex = ctxGet(1);
  }

  private void ctxCheck(int expected) {
    int state = ctxPeek();

    ctxThrow(state, expected);
  }

  private int ctxGet(int offset) {
    return listArray[listIndex - offset];
  }

  private int ctxPeek() {
    return listArray[listIndex];
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

  private void ctxPush(int v0, int v1, int v2) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 3);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
    listArray[++listIndex] = v2;
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
        int parent = ctxGet(2);
        ctxPop(3);
        int ctx = ctxPeek();
        ctxThrow(ctx, _DOCUMENT_NODES);
        int actual = ctxGet(1);
        if (parent != actual) {
          throw new IllegalStateException(
            """
          Last consumed element was not a child of document
          """
          );
        }
        ctx2proto();
      }

      default -> ctxThrow(peek, _DOCUMENT_NODES);
    }
  }

  private PseudoHtmlElement element() {
    return (PseudoHtmlElement) objectArray[ELEMENT];
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

    ctxPush(parent, protoIndex, _ELEMENT);

    return element;
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

}