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
package objectos.html;

import static org.testng.Assert.assertEquals;

import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class MarkupTest {

  @Test(description = """
  document:
  - empty
  """)
  public void document01() {
    final Markup subject;
    subject = Markup.of();

    assertEquals(
        subject.compile(),

        HtmlY.create(_ -> {

        })
    );
  }

  @Test(description = """
  regular elem:
  - no attrs
  - no children
  """)
  public void regularElement01() {
    final Markup subject;
    subject = Markup.of();

    subject.div();

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ELEMEND8);
          opts.addInt8(ElementName.DIV.index());
        })
    );
  }

  @Test(description = """
  regular elem:
  - 1 reg attr (object) (8bit)
  - no children
  """)
  public void regularElement04() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        Id.of("foo")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  regular elem:
  - 1 js action attr (object) (8bit)
  - no children
  """)
  public void regularElement05() {
    final Markup subject;
    subject = Markup.of();

    final JsAction action;
    action = Js.noop();

    subject.div(
        subject.onclick(action)
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  regular elem:
  - 2 reg attrs (markup) (8bit)
  - no children
  """)
  public void regularElement06() {
    final Markup subject;
    subject = Markup.of();

    subject.html(
        subject.lang("pt-BR"),
        subject.dir("auto")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
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
          opts.addByte(HtmlBytes.ELEMEND8);
          opts.addInt8(ElementName.HTML.index());
        })
    );
  }

  @Test(description = """
  regular elem:
  - 3 reg attrs (class) (8bit)
  - no children
  """)
  public void regularElement07() {
    final Markup subject;
    subject = Markup.of();

    subject.span(
        subject.className("c1"),
        ClassName.of("c2"),
        subject.className("c3")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject("c1"));
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject("c3"));
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject("c2"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.SPAN.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(3);
          opts.addInt8(0);
          opts.addInt8(6);
          opts.addInt8(3);
          opts.addByte(HtmlBytes.ELEMEND8);
          opts.addInt8(ElementName.SPAN.index());
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (object) (8bit)
  """)
  public void voidElement04() {
    final Markup subject;
    subject = Markup.of();

    subject.input(
        Id.of("foo")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 js action attr (object) (8bit)
  """)
  public void voidElement05() {
    final Markup subject;
    subject = Markup.of();

    final JsAction action;
    action = Js.noop();

    subject.input(
        subject.onclick(action)
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  void elem:
  - 2 reg attrs (markup) (8bit)
  """)
  public void voidElement06() {
    final Markup subject;
    subject = Markup.of();

    subject.input(
        subject.type("text"),
        subject.size("30")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.TYPE.index());
          opts.addInt8(opts.addObject("text"));
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.SIZE.index());
          opts.addInt8(opts.addObject("30"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addInt8(3);
        })
    );
  }

  @Test(description = """
  void elem:
  - 3 reg attrs (class) (8bit)
  """)
  public void voidElement07() {
    final Markup subject;
    subject = Markup.of();

    subject.input(
        ClassName.of("c1"),
        subject.className("c2"),
        ClassName.of("c3")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject("c2"));
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject("c1"));
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject("c3"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(3);
          opts.addInt8(3);
          opts.addInt8(0);
          opts.addInt8(6);
        })
    );
  }

}
