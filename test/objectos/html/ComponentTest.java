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

import java.util.function.Consumer;
import objectos.html.format.Formatter;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ComponentTest {

  @DataProvider
  public Object[][] formatProvider() {
    return new Object[][] {
        {
            "document, empty",

            c(_ -> {}),

            "",

            ""
        },
        {
            "regular element, nested",

            c(m -> m.div(m.span(m.text("Hello")))),

            """
            <div>\
            <span>Hello</span>\
            </div>
            """,

            ""
        },
        {
            "testable text, top-level testable heading",

            c(m -> m.t().h1("Heading 1")),

            "",

            """
            # Heading 1
            """
        },
        {
            "testable text, testable heading after a regular element",

            c(m -> {
              m.div(m.text("Hello"));
              m.t().h1("Heading 1");
            }),

            """
            <div>Hello</div>
            """,

            """
            # Heading 1
            """
        },
        {
            "testable text, testable heading followed by testable cell",

            c(m -> {
              m.t().h1("Heading 1");
              m.t().cell(1, "x");
            }),

            "",

            """
            # Heading 1

            | x |
            """
        },
        {
            "testable text, testable list items",

            c(m -> {
              m.t().item("Foo");
              m.t().item("Bar");
            }),

            "",

            """
            - Foo
            - Bar
            """
        },
        {
            "testable text, testable props",

            c(m -> {
              m.t().prop("First name", "John");
              m.t().prop("Last name", "Smith");
            }),

            "",

            """
            - First name: John
            - Last name: Smith
            """
        }
    };
  }

  @Test(dataProvider = "formatProvider")
  public void format(
      String description,
      Component subject,
      String expectedStandard,
      String expectedTestable) {
    final String standard;
    standard = subject.compile().format(Formatter.standard());

    assertEquals(standard, expectedStandard, description);

    final String testable;
    testable = subject.compile().format(Formatter.testable());

    assertEquals(testable, expectedTestable, description);
  }

  @DataProvider
  public Object[][] compileProvider() {
    return new Object[][] {
        {
            "document, empty",

            c(_ -> {}),

            html(_ -> {})
        },
        {
            "void element, no attributes",

            c(m -> m.hr()),

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.HR.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);
            })
        },
        {
            "regular element, no attributes, nested elements",

            c(m -> m.html(m.head(m.meta()))),

            html(opts -> {
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
            })
        },
        {
            "testable text, top-level testable heading",

            c(m -> m.t().h1("Heading 1")),

            html(opts -> {
              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
            })
        },
        {
            "testable text, testable heading followed by testable cell",

            c(m -> {
              m.t().h1("Heading 1");
              m.t().cell(1, "x");
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("x"));
            })
        }
    };
  }

  @Test(dataProvider = "compileProvider")
  public void compile(String description, Component subject, Html expected) {
    final Html actual;
    actual = subject.compile();

    assertEquals(actual, expected, description);
  }

  private Component c(Component c) {
    return c;
  }

  private Html html(Consumer<? super HtmlSink> opts) {
    return HtmlY.create(opts);
  }

}
