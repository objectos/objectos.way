/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.stream;

import static org.testng.Assert.assertEquals;

import objectos.html.stream.EndStartTagKind;
import objectos.html.stream.Event;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.AmbiguousName;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class PlayerLevelTest {

  @Test(description = """
  doctype:
  - document level
  """)
  public void doctype01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.DOCTYPE);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderDoctypeEvent(0));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.HTML));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.HTML));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  malformed document:
  - doctype at a non-zero nesting level
  """)
  public void doctype02() {
    final Frame[] stack;
    stack = new Frame[] {new Frame(0), new Frame(0), new Frame(0)};

    final byte[] code;
    code = new byte[] {HtmlBytes.DOCTYPE};

    final Player subject;
    subject = new Player(code, new Object[] {}, stack, 2);

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderDoctypeEvent(3));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  nesting:
  - html
  - body
  - div
  """)
  public void nesting01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);

      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.BODY.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(4);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.HTML));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(1, ElementName.BODY));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(1, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);

    final Event event;
    event = subject.next();

    assertEquals(event, new BeginStartTagEvent(2, ElementName.DIV));
    assertEquals(event.level(), 2);

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(2, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(2, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(1, ElementName.BODY));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.HTML));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  attributes:
  - merged class attribute
  - root level
  """)
  public void attributes01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c1"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CLASS.index());
      opts.addInt8(opts.addObject("c2"));

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.CLASS, "c1 c2"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  attributes:
  - boolean attribute
  - nested element
  """)
  public void attributes02() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.AUTOFOCUS.index());

      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(2);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(1, ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderBooleanAttributeEvent(1, AttributeName.AUTOFOCUS));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(1, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(1, ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  attributes:
  - js action attribute
  - nested element
  """)
  public void attributes03() {
    final JsAction action;
    action = Js.noop();

    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xJSACTION88);
      opts.addInt8(AttributeName.ONCLICK.index());
      opts.addInt8(opts.addObject(action));

      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(3);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(1, ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderJsActionAttributeEvent(1, AttributeName.ONCLICK, action));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(1, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(1, ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  text:
  - document level
  """)
  public void text01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.TEXT8);
      opts.addInt8(opts.addObject("Ipsum"));
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderTextEvent(0, "Ipsum"));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  text:
  - nested element
  """)
  public void text02() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xTEXT8);
      opts.addInt8(opts.addObject("Ipsum"));

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderTextEvent(1, "Ipsum"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  raw:
  - nested element
  """)
  public void raw01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xRAW8);
      opts.addInt8(opts.addObject("<br/>"));

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderRawEvent(1, "<br/>"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  ambiguous element:
  - title
  - nested element
  """)
  public void ambiguousElement01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.eAMBIGUOUS88);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt8(opts.addObject("Ipsum"));

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(1, ElementName.TITLE));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(1, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderTextEvent(1, "Ipsum"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.TITLE));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  multiple root nodes:
  - level resets to 0
  """)
  public void multipleRoots01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  fragment:
  - does not affect the nesting level
  """)
  public void fragment01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.FRAGSTR);
      opts.addInt24(9);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);

      opts.addByte(HtmlBytes.FRAGEND);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

}
