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
      opts.objects(HtmlSink.MAX_INT8 + 1);
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
      opts.objects(HtmlSink.MAX_INT16 + 1);
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

}
