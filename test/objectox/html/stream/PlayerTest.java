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
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.Test;

public class PlayerTest {

  @Test(description = """
  boolean attr:
  - count: 1
  - object
  - name (8 bit)
  - reg elem
  """)
  public void booleanAttribute01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.AUTOFOCUS.index());
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderBooleanAttributeEvent(0, AttributeName.AUTOFOCUS));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  boolean attr:
  - count: 1
  - object
  - name (8 bit)
  - void elem
  """)
  public void booleanAttribute02() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.CHECKED.index());
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderBooleanAttributeEvent(0, AttributeName.CHECKED));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  document:
  - empty
  """)
  public void document01() {
    final Player subject;
    subject = PlayerY.create(_ -> {

    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(true));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  document:
  - no events emitted
  """)
  public void document02() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.FRAGSTR);

      opts.addInt24(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(true));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  js action attr:
  - count: 1
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void jsActionAttribute01() {
    final JsAction action;
    action = Js.noop();

    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xJSACTION88);
      opts.addInt8(AttributeName.ONCLICK.index());
      opts.addInt8(opts.addObject(action));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderJsActionAttributeEvent(0, AttributeName.ONCLICK, action));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  raw text:
  - document level
  - 8 bit
  """)
  public void raw01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.RAW8);
      opts.addInt8(opts.addObject("<br/>"));
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderRawEvent(0, "<br/>"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  raw text:
  - reg elem (no attrs)
  - 8 bit
  """)
  public void raw02() {
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
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  raw text:
  - document level
  - 16 bit
  """)
  public void raw03() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte(HtmlBytes.RAW16);
      opts.addInt16(opts.addObject("Ipsum"));
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderRawEvent(0, "Ipsum"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  raw text:
  - document level
  - 24 bit
  """)
  public void raw04() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte(HtmlBytes.RAW24);
      opts.addInt24(opts.addObject("Ipsum"));
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderRawEvent(0, "Ipsum"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute01_08bit() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt8(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.CONTENTEDITABLE, "false"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (16 bit)
  - reg elem
  """)
  public void regularAttribute01_16bit() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE816);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt16(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.CONTENTEDITABLE, "false"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (24 bit)
  - reg elem
  """)
  public void regularAttribute01_24bit() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE824);
      opts.addInt8(AttributeName.CONTENTEDITABLE.index());
      opts.addInt24(opts.addObject("false"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.CONTENTEDITABLE, "false"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (8 bit)
  - void elem
  """)
  public void regularAttribute02_08bit() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt8(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.TYPE, "password"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (16 bit)
  - void elem
  """)
  public void regularAttribute02_16bit() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE816);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt16(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.TYPE, "password"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (24 bit)
  - void elem
  """)
  public void regularAttribute02_24bit() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte(HtmlBytes.xATTRIBUTE824);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt24(opts.addObject("password"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.TYPE, "password"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 1
  - object
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute03() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.ID, "foo"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 1
  - object
  - name (8 bit)
  - void elem
  """)
  public void regularAttribute04() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.INPUT.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.ID, "foo"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  reg attr:
  - count: 2
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute05() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.LANG.index());
      opts.addInt8(opts.addObject("pt-BR"));
      opts.addByte(HtmlBytes.xATTRIBUTE88);
      opts.addInt8(AttributeName.DIR.index());
      opts.addInt8(opts.addObject("auto"));
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.HTML.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(2);
      opts.addInt8(0);
      opts.addInt8(3);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.HTML));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.LANG, "pt-BR"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(0, AttributeName.DIR, "auto"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.HTML));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - no attrs
  - no children
  """)
  public void regularElement01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
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
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - no attrs
  - reg elem (no attrs / no children)
  """)
  public void regularElement02() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
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
    assertEquals(subject.next(), new BeginStartTagEvent(1, ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(1, EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(1, ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - no attrs
  - void elem (no attrs)
  """)
  public void regularElement03() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMVOID8);
      opts.addInt8(ElementName.HR.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.ARTICLE.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.ARTICLE));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(1, ElementName.HR));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(1, EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.ARTICLE));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - no attrs
  - reg elem (no attrs / containing a void elem)
  - void elem (no attrs)
  """)
  public void regularElement04() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xELEMVOID8);
      opts.addInt8(ElementName.META.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.xELEMSTR8);
      opts.addInt8(ElementName.HEAD.index());
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
    assertEquals(subject.next(), new BeginStartTagEvent(1, ElementName.HEAD));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(1, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(2, ElementName.META));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(2, EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(1, ElementName.HEAD));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.HTML));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  testable heading:
  - document level
  - 8 bit
  - rendered as testable-only
  """)
  public void testable01() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.THEADING8);

      opts.addByte((byte) 1);

      opts.addInt8(opts.addObject("Heading 1"));
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new TestableHeadingEvent(0, 1, "Heading 1"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  testable heading:
  - document level
  - 16 bit
  """)
  public void testable02() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

      opts.addByte(HtmlBytes.THEADING16);

      opts.addByte((byte) 2);

      opts.addInt16(opts.addObject("Heading 2"));
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new TestableHeadingEvent(0, 2, "Heading 2"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  testable heading:
  - element child
  - regular elem
  """)
  public void testable03() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xTHEADING8);

      opts.addByte((byte) 1);

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
    assertEquals(subject.next(), new TestableHeadingEvent(1, 1, "Ipsum"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  testable cell:
  - document level
  - 24 bit
  """)
  public void testable06() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

      opts.addByte(HtmlBytes.TCELL24);

      opts.addInt24(5);

      opts.addInt24(opts.addObject("12345"));
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new TestableCellEvent(0, 5, "12345"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  testable heading:
  - element child
  - testable-only opcode
  - fragment body
  """)
  public void testable07() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xFRAGSTR);
      opts.addInt24(8);

      opts.addByte(HtmlBytes.oTHEADING8);

      opts.addByte((byte) 1);

      opts.addInt8(opts.addObject("Ipsum"));

      opts.addByte(HtmlBytes.FRAGEND);

      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.DIV.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(4);
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(0, EndStartTagKind.ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new TestableHeadingEvent(1, 1, "Ipsum"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(0, ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndDocumentEvent(false));
    assertEquals(subject.hasNext(), false);
  }

}
