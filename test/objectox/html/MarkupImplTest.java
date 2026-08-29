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
package objectox.html;

import static org.testng.Assert.assertEquals;

import objectos.html.ClassName;
import objectos.html.HtmlY;
import objectos.html.Id;
import objectos.html.Markup;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import objectox.html.markup.HtmlSinkY;
import org.testng.annotations.Test;

public class MarkupImplTest {

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute01_08bit() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    subject.div(
        subject.contenteditable("false")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (16 bit)
  - reg elem
  """)
  public void regularAttribute01_16bit() {
    final MarkupImpl subject;
    subject = MarkupImplY.create(opts -> {
      opts.objects(IntSize.BIT16);
    });

    subject.div(
        subject.contenteditable("false")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (24 bit)
  - reg elem
  """)
  public void regularAttribute01_24bit() {
    final MarkupImpl subject;
    subject = MarkupImplY.create(opts -> {
      opts.objects(IntSize.BIT24);
    });

    subject.div(
        subject.contenteditable("false")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  boolean attr:
  - count: 1
  - object
  - name (8 bit)
  - reg elem
  """)
  public void booleanAttribute01() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    subject.span(
        Markup.autofocus
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xBOOLEAN8);
          opts.addInt8(AttributeName.AUTOFOCUS.index());
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.SPAN.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ELEMEND8);
          opts.addInt8(ElementName.SPAN.index());
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - object
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute03() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    subject.div(
        Id.of("foo")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
  js action attr:
  - count: 1
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void jsActionAttribute01() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    final JsAction action;
    action = Js.noop();

    subject.div(
        subject.onclick(action)
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
  reg attr:
  - count: 2
  - markup
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute05() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    subject.html(
        subject.lang("pt-BR"),
        subject.dir("auto")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
  reg attr:
  - count: 3
  - object (class)
  - name (8 bit)
  - reg elem
  """)
  public void regularAttribute07() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    subject.span(
        subject.className("c1"),
        ClassName.of("c2"),
        subject.className("c3")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
  reg attr:
  - count: 1
  - markup
  - name (8 bit)
  - void elem
  """)
  public void regularAttribute02_08bit() {
    final MarkupImpl subject;
    subject = MarkupImplY.create(opts -> {
      opts.objects(IntSize.BIT8);
    });

    subject.input(
        subject.type("password")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(0);

          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.TYPE.index());
          opts.addInt8(opts.addObject("password"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (16 bit)
  - void elem
  """)
  public void regularAttribute02_16bit() {
    final MarkupImpl subject;
    subject = MarkupImplY.create(opts -> {
      opts.objects(IntSize.BIT16);
    });

    subject.input(
        subject.type("password")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT8 + 1);

          opts.addByte(HtmlBytes.xATTRIBUTE816);
          opts.addInt8(AttributeName.TYPE.index());
          opts.addInt16(opts.addObject("password"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - markup
  - name (24 bit)
  - void elem
  """)
  public void regularAttribute02_24bit() {
    final MarkupImpl subject;
    subject = MarkupImplY.create(opts -> {
      opts.objects(IntSize.BIT24);
    });

    subject.input(
        subject.type("password")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.MAX_INT16 + 1);

          opts.addByte(HtmlBytes.xATTRIBUTE824);
          opts.addInt8(AttributeName.TYPE.index());
          opts.addInt24(opts.addObject("password"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  boolean attr:
  - count: 1
  - object
  - name (8 bit)
  - void elem
  """)
  public void booleanAttribute02() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    subject.input(
        Markup.checked
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xBOOLEAN8);
          opts.addInt8(AttributeName.CHECKED.index());
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  reg attr:
  - count: 1
  - object
  - name (8 bit)
  - void elem
  """)
  public void regularAttribute04() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    subject.input(
        Id.of("foo")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
  js action attr:
  - count: 1
  - markup
  - name (8 bit)
  - void elem
  """)
  public void jsActionAttribute02() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

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
  reg attr:
  - count: 2
  - markup
  - name (8 bit)
  - void elem
  """)
  public void regularAttribute06() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

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
  reg attr:
  - count: 3
  - object (class)
  - name (8 bit)
  - void elem
  """)
  public void regularAttribute08() {
    final MarkupImpl subject;
    subject = MarkupImpl.create();

    subject.input(
        ClassName.of("c1"),
        subject.className("c2"),
        ClassName.of("c3")
    );

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
