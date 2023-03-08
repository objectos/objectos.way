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
import objectos.html.HtmlTemplate;
import objectos.html.HtmlTemplate.Visitor;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.util.IntArrays;

public class HtmlPlayer extends HtmlRecorder {

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

      throw new UnsupportedOperationException("Implement me");
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

        case ByteProto2.ELEMENT,
             ByteProto2.TEXT -> {
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

      if (cellType == ByteProto2.SINGLE) {
        var name = StandardAttributeName.getByCode(code);
        var string = (String) objectGet(value);

        visitor.attributeStart(name.getName());
        visitor.attributeValue(string);
        visitor.attributeEnd();
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: cellType=" + cellType
        );
      }
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

            case ByteProto2.ELEMENT -> element();

            case ByteProto2.ELEMENT_END -> {
              break loop;
            }

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

  private Object objectGet(int index) {
    return objectArray[index];
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