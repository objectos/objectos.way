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

}
