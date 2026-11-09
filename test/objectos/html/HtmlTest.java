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
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

// Any update to this test must have a corresponding test in
// test/objectos/html/MarkupTest.java.
public class HtmlTest {

  @DataProvider
  public Object[][] toStringProvider() {
    final JsAction action;
    action = Js.noop();

    return new Object[][] {
        {
            "boolean attribute, 1 count, 8bit, regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.xBOOLEAN8);
              opts.addInt8(AttributeName.AUTOFOCUS.index());
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.SPAN.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <span autofocus></span>
            """
        },
        {
            "boolean attribute, 1 count, 8bit, void element",

            html(opts -> {
              opts.addByte(HtmlBytes.xBOOLEAN8);
              opts.addInt8(AttributeName.CHECKED.index());
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <input checked>
            """
        },
        {
            "document, empty",

            html(_ -> {}),

            ""
        },
        {
            "js action attribute, 1 count, 8bit, regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.xJSACTION88);
              opts.addInt8(AttributeName.ONCLICK.index());
              opts.addInt8(opts.addObject(action));
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div onclick='way.on(event,["NO"])'></div>
            """
        },
        {
            "js action attribute, 1 count, 8bit, void element",

            html(opts -> {
              opts.addByte(HtmlBytes.xJSACTION88);
              opts.addInt8(AttributeName.ONCLICK.index());
              opts.addInt8(opts.addObject(action));
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <input onclick='way.on(event,["NO"])'>
            """
        },
        {
            "regular attribute contenteditable, 1 count, 8bit, regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.xATTRIBUTE88);
              opts.addInt8(AttributeName.CONTENTEDITABLE.index());
              opts.addInt8(opts.addObject("false"));
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div contenteditable="false"></div>
            """
        },
        {
            "regular attribute contenteditable, 1 count, 16bit, regular element",

            html(opts -> {
              opts.objectsIndex(HtmlSink.FIRST_INT16);

              opts.addByte(HtmlBytes.xATTRIBUTE816);
              opts.addInt8(AttributeName.CONTENTEDITABLE.index());
              opts.addInt16(opts.addObject("false"));
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div contenteditable="false"></div>
            """
        },
        {
            "regular attribute contenteditable, 1 count, 24bit, regular element",

            html(opts -> {
              opts.objectsIndex(HtmlSink.FIRST_INT24);

              opts.addByte(HtmlBytes.xATTRIBUTE824);
              opts.addInt8(AttributeName.CONTENTEDITABLE.index());
              opts.addInt24(opts.addObject("false"));
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div contenteditable="false"></div>
            """
        },
        {
            "regular attribute type, 1 count, 8bit, void element",

            html(opts -> {
              opts.addByte(HtmlBytes.xATTRIBUTE88);
              opts.addInt8(AttributeName.TYPE.index());
              opts.addInt8(opts.addObject("password"));
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <input type="password">
            """
        },
        {
            "regular attribute type, 1 count, 16bit, void element",

            html(opts -> {
              opts.objectsIndex(HtmlSink.FIRST_INT16);

              opts.addByte(HtmlBytes.xATTRIBUTE816);
              opts.addInt8(AttributeName.TYPE.index());
              opts.addInt16(opts.addObject("password"));
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <input type="password">
            """
        },
        {
            "regular attribute type, 1 count, 24bit, void element",

            html(opts -> {
              opts.objectsIndex(HtmlSink.FIRST_INT24);

              opts.addByte(HtmlBytes.xATTRIBUTE824);
              opts.addInt8(AttributeName.TYPE.index());
              opts.addInt24(opts.addObject("password"));
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <input type="password">
            """
        },
        {
            "regular attribute, 1 count, 8bit, regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.xATTRIBUTE88);
              opts.addInt8(AttributeName.ID.index());
              opts.addInt8(opts.addObject("foo"));
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div id="foo"></div>
            """
        },
        {
            "regular attribute, 1 count, 8bit, void element",

            html(opts -> {
              opts.addByte(HtmlBytes.xATTRIBUTE88);
              opts.addInt8(AttributeName.ID.index());
              opts.addInt8(opts.addObject("foo"));
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <input id="foo">
            """
        },
        {
            "regular attribute, 2 count, 8bit, regular element",

            html(opts -> {
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
            }),

            """
            <html lang="pt-BR" dir="auto"></html>
            """
        },
        {
            "regular attribute, 2 count, 8bit, void element",

            html(opts -> {
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
            }),

            """
            <input type="text" size="30">
            """
        },
        {
            "regular attribute, 3 count, 8bit, regular element",

            html(opts -> {
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
            }),

            """
            <span class="c1 c2 c3"></span>
            """
        },
        {
            "regular attribute, 3 count, 8bit, void element",

            html(opts -> {
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
            }),

            """
            <input class="c1 c2 c3">
            """
        },
        {
            "regular element, no attributes, no children",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);
            }),

            """
            <div></div>
            """
        },
        {
            "regular element, no attributes, regular element child",

            html(opts -> {
              opts.addByte(HtmlBytes.xELEMSTR8);
              opts.addInt8(ElementName.SPAN.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div>\
            <span></span>\
            </div>
            """
        },
        {
            "regular element, no attributes, void element child",

            html(opts -> {
              opts.addByte(HtmlBytes.xELEMVOID8);
              opts.addInt8(ElementName.HR.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.ARTICLE.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <article>\
            <hr>\
            </article>
            """
        },
        {
            "regular element, no attributes, nested elements",

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
            }),

            """
            <html>\
            <head>\
            <meta>\
            </head>\
            </html>
            """
        },
        {
            "void element, no attributes",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.HR.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);
            }),

            """
            <hr>
            """
        }
    };
  }

  @Test(dataProvider = "toStringProvider")
  public void toString(String description, Html subject, String expected) {
    assertEquals(subject.toString(), expected, description);
  }

  @DataProvider
  public Object[][] formatProvider() {
    return new Object[][] {
        {
            "testable text, top-level testable heading",

            html(opts -> {
              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
            }),

            "",

            """
            # Heading 1
            """
        },
        {
            "testable text, testable heading after a regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);

              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
            }),

            """
            <div></div>
            """,

            """
            # Heading 1
            """
        },
        {
            "testable text, testable heading inside a regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.xTHEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));

              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div>Heading 1</div>
            """,

            """
            # Heading 1
            """
        },
        {
            "testable text, top-level testable cells",

            html(opts -> {
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("1"));
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("2"));
            }),

            "",

            """
            | 1 | 2 |
            """
        },
        {
            "testable text, testable cells after a regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.HR.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);

              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("1"));
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("2"));
            }),

            """
            <hr>
            """,

            """
            | 1 | 2 |
            """
        },
        {
            "testable text, testable heading followed by testable cell",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.SPAN.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);

              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("x"));
            }),

            """
            <span></span>
            """,

            """
            # Heading 1

            | x |
            """
        },
        {
            "testable text, document without testable nodes",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);
            }),

            """
            <div></div>
            """,

            ""
        },
        {
            "testable text, testable list items",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);

              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("Foo"));
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("Bar"));
            }),

            """
            <div></div>
            """,

            """
            - Foo
            - Bar
            """
        },
        {
            "testable text, testable list items separated by a list",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.HR.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);

              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("A"));
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("B"));
              opts.addByte(HtmlBytes.TLIST);
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("C"));
            }),

            """
            <hr>
            """,

            """
            - A
            - B

            - C
            """
        },
        {
            "testable text, testable heading, list items and cells",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.SPAN.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);

              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("Item"));
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("x"));
            }),

            """
            <span></span>
            """,

            """
            # Heading 1

            - Item

            | x |
            """
        },
        {
            "testable text, testable props",

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);

              opts.addByte(HtmlBytes.TPROP8);
              opts.addObject("First name");
              opts.addInt8(opts.addObject("John"));
              opts.addByte(HtmlBytes.TPROP8);
              opts.addObject("Last name");
              opts.addInt8(opts.addObject("Smith"));
            }),

            """
            <div></div>
            """,

            """
            - First name: John
            - Last name: Smith
            """
        },
        {
            "testable text, testable item inside a regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.xTITEM8);
              opts.addInt8(opts.addObject("Foo"));

              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div>Foo</div>
            """,

            """
            - Foo
            """
        },
        {
            "testable text, testable prop inside a regular element",

            html(opts -> {
              opts.addByte(HtmlBytes.xTPROP8);
              opts.addObject("First name");
              opts.addInt8(opts.addObject("John"));

              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            }),

            """
            <div>John</div>
            """,

            """
            - First name: John
            """
        }
    };
  }

  @Test(dataProvider = "formatProvider")
  public void format(
      String description,
      Html subject,
      String expectedStandard,
      String expectedTestable) {
    final String standard;
    standard = subject.format(Formatter.standard());

    assertEquals(standard, expectedStandard, description);

    final String testable;
    testable = subject.format(Formatter.testable());

    assertEquals(testable, expectedTestable, description);
  }

  private Html html(Consumer<? super HtmlSink> opts) {
    return HtmlY.create(opts);
  }

}
