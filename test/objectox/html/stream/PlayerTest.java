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
  document:
  - empty
  """)
  public void document01() {
    final Player subject;
    subject = PlayerY.create(_ -> {

    });

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
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (markup) (8bit)
  - no children
  """)
  public void regularElement02_08bit() {
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
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(AttributeName.CONTENTEDITABLE, "false"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (markup) (16bit)
  - no children
  """)
  public void regularElement02_16bit() {
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
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(AttributeName.CONTENTEDITABLE, "false"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (markup) (24bit)
  - no children
  """)
  public void regularElement02_24bit() {
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
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(AttributeName.CONTENTEDITABLE, "false"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - 1 boolean attr (object) (8bit)
  - no children
  """)
  public void regularElement03() {
    final Player subject;
    subject = PlayerY.create(opts -> {
      opts.addByte(HtmlBytes.xBOOLEAN8);
      opts.addInt8(AttributeName.AUTOFOCUS.index());
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.SPAN.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(1);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.SPAN.index());
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.SPAN));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderBooleanAttributeEvent(AttributeName.AUTOFOCUS));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(ElementName.SPAN));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (object) (8bit)
  - no children
  """)
  public void regularElement04() {
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
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(AttributeName.ID, "foo"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  regular elem:
  - 1 js action attr (object) (8bit)
  - no children
  """)
  public void regularElement05() {
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
      opts.addByte(HtmlBytes.ELEMEND8);
      opts.addInt8(ElementName.DIV.index());
    });

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderJsActionAttributeEvent(AttributeName.ONCLICK, action));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderEndTagEvent(ElementName.DIV));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (8bit)
  """)
  public void voidElement02_08bit() {
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
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(AttributeName.TYPE, "password"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (16bit)
  """)
  public void voidElement02_16bit() {
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
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(AttributeName.TYPE, "password"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (24bit)
  """)
  public void voidElement02_24bit() {
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
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(AttributeName.TYPE, "password"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  void elem:
  - 1 boolean attr (object) (8bit)
  """)
  public void voidElement03() {
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
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderBooleanAttributeEvent(AttributeName.CHECKED));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), false);
  }

  @Test(description = """
  void elem:
  - 1 reg attr (object) (8bit)
  """)
  public void voidElement04() {
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
    assertEquals(subject.next(), new BeginStartTagEvent(ElementName.INPUT));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new RenderAttributeEvent(AttributeName.ID, "foo"));
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT));
    assertEquals(subject.hasNext(), false);
  }

}
