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

import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import org.testng.annotations.Test;

public class TestableNodesTest {

  @Test(description = """
  testable headings:
  - document level
  """)
  public void heading01() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.h1("H1");
    t.h2("H2");
    t.h3("H3");
    t.h4("H4");
    t.h5("H5");
    t.h6("H6");

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("H1"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 2);
          opts.addInt8(opts.addObject("H2"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 3);
          opts.addInt8(opts.addObject("H3"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 4);
          opts.addInt8(opts.addObject("H4"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 5);
          opts.addInt8(opts.addObject("H5"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 6);
          opts.addInt8(opts.addObject("H6"));
        })
    );
  }

  @Test(description = """
  testable heading node:
  - element child
  """)
  public void heading02() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().h1("Ipsum")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xTHEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  testable cell node:
  - document level
  """)
  public void cell01() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.cell(2, "x");

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.TCELL8);
          opts.addInt8(opts.addObject("x "));
        })
    );
  }

  @Test(
      description = "testable cell node rejects a non-positive width",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "width must be > 0")
  public void cellValidation01() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.cell(0, "x");
  }

  @Test(
      description = "testable cell node rejects a null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void cellValidation02() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.cell(1, null);
  }

  @Test(
      description = "testable cell node rejects a value longer than the width",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Formatted value length will exceed the column width of 1")
  public void cellValidation03() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.cell(1, "xy");
  }

}
