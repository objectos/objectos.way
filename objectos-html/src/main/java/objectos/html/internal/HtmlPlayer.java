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

public class HtmlPlayer extends HtmlRecorder {

  private Visitor visitor;

  public final void play(HtmlTemplate.Visitor visitor) throws IOException {
    this.visitor = visitor;

    stackIndex = -1;

    try {
      rootElement();
    } finally {
      this.visitor = null;
    }
  }

  private void attribute() throws IOException {
    jumpPrepare();

    attributeImpl();

    jumpReturn();
  }

  private void attributeImpl() throws IOException {
    protoNext(); // ByteProto.ATTRIBUTE
    protoNext(); // tail index

    int code = protoNext();

    var attr = StandardAttributeName.getByCode(code);

    var value = (String) objectGet(protoNext());

    visitor.attribute(attr, value);
  }

  private void element() throws IOException {
    jumpPrepare();

    elementImpl();

    jumpReturn();
  }

  private void elementImpl() throws IOException {
    protoNext(); // ByteProto.ELEMENT
    protoNext(); // tail index;

    var elem = StandardElementName.getByCode(protoNext());

    visitor.startTag(elem);

    loop: while (protoMore()) {
      int proto = protoNext();

      switch (proto) {
        case ByteProto2.ATTRIBUTE -> attribute();

        case ByteProto2.ELEMENT -> element();

        case ByteProto2.ELEMENT_END -> {
          visitor.endTag(elem);

          break loop;
        }

        case ByteProto2.ELEMENT_NORMAL -> visitor.startTagEnd(elem);

        case ByteProto2.ELEMENT_VOID -> {
          visitor.startTagEndSelfClosing();

          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }
  }

  private void jumpPrepare() {
    int location = protoNext();

    stackPush(protoIndex);

    protoIndex = location;
  }

  private void jumpReturn() {
    protoIndex = stackPop();
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

  private void rootElement() throws IOException {
    loop: while (protoMore()) {
      int proto = protoNext();

      switch (proto) {
        case ByteProto2.DOCTYPE -> visitor.doctype();

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

}