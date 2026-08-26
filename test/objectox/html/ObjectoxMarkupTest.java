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

import objectos.html.Markup;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import objectox.html.markup.HtmlSinkY;
import org.testng.annotations.Test;

public class ObjectoxMarkupTest {

  @Test(description = """
  document:
  - empty
  """)
  public void document01() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkup.create();

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(_ -> {})
    );
  }

  @Test(description = """
  regular elem:
  - no attrs
  - no children
  """)
  public void regularElement01() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkup.create();

    subject.div();

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
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
  - 1 reg attr (markup) (8bit)
  - no children
  """)
  public void regularElement02_8bit() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkup.create();

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
  regular elem:
  - 1 reg attr (markup) (16bit)
  - no children
  """)
  public void regularElement02_16bit() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkupY.create(opts -> {
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
  regular elem:
  - 1 reg attr (markup) (24bit)
  - no children
  """)
  public void regularElement02_24bit() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkupY.create(opts -> {
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
  void elem:
  - no attrs
  """)
  public void voidElement01() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkup.create();

    subject.hr();

    assertEquals(
        subject.unwrap(),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  void elem:
  - 1 reg attr (markup) (8bit)
  """)
  public void voidElement02_8bit() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkupY.create(opts -> {
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
  void elem:
  - 1 reg attr (markup) (16bit)
  """)
  public void voidElement02_16bit() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkupY.create(opts -> {
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
  void elem:
  - 1 reg attr (markup) (24bit)
  """)
  public void voidElement02_24bit() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkupY.create(opts -> {
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
  regular elem:
  - 1 boolean attr (object) (8bit)
  - no children
  """)
  public void regularElement03() {
    final ObjectoxMarkup subject;
    subject = ObjectoxMarkup.create();

    subject.span(
        subject.attr(Markup.autofocus)
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

}
