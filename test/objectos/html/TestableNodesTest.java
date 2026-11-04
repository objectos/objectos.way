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

  @Test(description = """
  testable list item node:
  - document level
  """)
  public void item01() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.item("Foo");

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.TITEM8);
          opts.addInt8(opts.addObject("Foo"));
        })
    );
  }

  @Test(description = """
  testable list item node:
  - element child
  """)
  public void item02() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().item("Ipsum")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xTITEM8);
          opts.addInt8(opts.addObject("Ipsum"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(
      description = "testable list item node rejects a null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void item03() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.item(null);
  }

  @Test(description = """
  testable list item node:
  - multiple items are formatted as a markdown list
  """)
  public void item04() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.item("Foo");
    t.item("Bar");
    t.item("Baz");

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "- Foo\n- Bar\n- Baz\n");
  }

  @Test(description = """
  testable list item node:
  - element child
  - rendered as text in regular HTML
  """)
  public void item05() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().item("Foo")
    );

    final String actual;
    actual = subject.compile().toString();

    assertEquals(actual, "<div>Foo</div>\n");
  }

  @Test(description = """
  testable list item node:
  - element child
  - contributes to the testable text
  """)
  public void item06() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    subject.div(
        t.item("a")
    );

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "- a\n");
  }

  @Test(description = """
  testable prop node:
  - document level
  """)
  public void prop01() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.prop("First name", "John");

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.TPROP8);
          opts.addObject("First name");
          opts.addInt8(opts.addObject("John"));
        })
    );
  }

  @Test(description = """
  testable prop node:
  - element child
  """)
  public void prop02() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().prop("First name", "John")
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xTPROP8);
          opts.addObject("First name");
          opts.addInt8(opts.addObject("John"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(
      description = "testable prop node rejects a null name",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "name == null")
  public void prop03() {
    final Markup subject;
    subject = Markup.of();

    subject.t().prop(null, "John");
  }

  @Test(
      description = "testable prop node rejects a null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void prop04() {
    final Markup subject;
    subject = Markup.of();

    subject.t().prop("First name", null);
  }

  @Test(description = """
  testable prop node:
  - multiple props are formatted as a markdown list
  """)
  public void prop05() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.prop("First name", "John");
    t.prop("Last name", "Smith");

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "- First name: John\n- Last name: Smith\n");
  }

  @Test(description = """
  testable prop node:
  - element child
  - rendered as text in regular HTML using only the value
  """)
  public void prop06() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().prop("First name", "John")
    );

    final String actual;
    actual = subject.compile().toString();

    assertEquals(actual, "<div>John</div>\n");
  }

  @Test(description = """
  testable prop node:
  - element child
  - contributes to the testable text
  """)
  public void prop07() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().prop("First name", "John")
    );

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "- First name: John\n");
  }

  @Test(description = """
  testable prop node:
  - begins a new list after preceding items
  """)
  public void prop08() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.item("Item");
    t.prop("Name", "Value");
    t.list();
    t.prop("Other", "Prop");

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "- Item\n- Name: Value\n\n- Other: Prop\n");
  }

  @Test(description = """
  testable prop node:
  - fragment child
  - does not produce regular HTML output
  """)
  public void prop09() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.f(() -> {
          subject.t().prop("Name", "Value");
        })
    );

    final String actual;
    actual = subject.compile().toString();

    assertEquals(actual, "<div></div>\n");
  }

  @Test(description = """
  testable prop node:
  - fragment child
  - contributes to the testable text
  """)
  public void prop10() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.f(() -> {
          subject.t().prop("Name", "Value");
        })
    );

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "- Name: Value\n");
  }

  @Test(description = """
  testable list node:
  - document level
  """)
  public void list01() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.list();

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.TLIST);
        })
    );
  }

  @Test(description = """
  testable list node:
  - element child
  """)
  public void list02() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().list()
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xTLIST);
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  testable list node:
  - begins a new list in the testable text
  """)
  public void list03() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.item("A");
    t.item("B");
    t.list();
    t.item("C");

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "- A\n- B\n\n- C\n");
  }

  @Test(description = """
  testable list node:
  - does not produce HTML output
  """)
  public void list04() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().list()
    );

    final String actual;
    actual = subject.compile().toString();

    assertEquals(actual, "<div></div>\n");
  }

  @Test(description = """
  testable list node:
  - element child
  - contributes to the testable text
  """)
  public void list05() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    subject.div(
        t.list(),
        t.item("a")
    );

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "- a\n");
  }

  @Test(description = """
  testable row node:
  - document level
  """)
  public void row01() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.row();

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.TROW);
        })
    );
  }

  @Test(description = """
  testable row node:
  - element child
  """)
  public void row02() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().row()
    );

    assertEquals(
        subject.compile(),

        HtmlY.create(opts -> {
          opts.addByte(HtmlBytes.xTROW);
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = """
  testable row node:
  - begins a new row in the testable text
  """)
  public void row03() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.row();
    t.cell(1, "a");
    t.row();
    t.cell(1, "b");

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "| a |\n| b |\n");
  }

  @Test(description = """
  testable row node:
  - starts a new row after preceding cells
  """)
  public void row04() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    t.cell(1, "a");
    t.cell(1, "b");
    t.row();
    t.cell(1, "c");

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "| a | b |\n| c |\n");
  }

  @Test(description = """
  testable row node:
  - does not produce HTML output
  """)
  public void row05() {
    final Markup subject;
    subject = Markup.of();

    subject.div(
        subject.t().row()
    );

    final String actual;
    actual = subject.compile().toString();

    assertEquals(actual, "<div></div>\n");
  }

  @Test(description = """
  testable row node:
  - element child
  - contributes to the testable text
  """)
  public void row06() {
    final Markup subject;
    subject = Markup.of();

    final TestableNodes t;
    t = subject.t();

    subject.div(
        t.row(),
        t.cell(1, "a")
    );

    final String actual;
    actual = subject.compile().toTestableText();

    assertEquals(actual, "| a |\n");
  }

}
