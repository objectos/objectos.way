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
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.MarkupImpl;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;
import objectox.html.markup.HtmlSink;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MarkupTest {

  @DataProvider
  public Object[][] compileProvider() {
    final JsAction action;
    action = Js.noop();

    return new Object[][] {
        {
            "empty document",

            markup(_ -> {}),

            html(_ -> {})
        },
        {
            "js action attribute, 1 count, 8bit, regular element",

            markup(m -> {
              m.div(
                  m.onclick(action)
              );
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.xJSACTION88);
              opts.addInt8(AttributeName.ONCLICK.index());
              opts.addInt8(opts.addObject(action));
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            })
        },
        {
            "js action attribute, 1 count, 8bit, void element",

            markup(m -> {
              m.input(
                  m.onclick(action)
              );
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.xJSACTION88);
              opts.addInt8(AttributeName.ONCLICK.index());
              opts.addInt8(opts.addObject(action));
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            })
        },
        {
            "regular attribute, 1 count, 8bit, regular element",

            markup(m -> {
              m.div(
                  Id.of("foo")
              );
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.xATTRIBUTE88);
              opts.addInt8(AttributeName.ID.index());
              opts.addInt8(opts.addObject("foo"));
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            })
        },
        {
            "regular attribute, 1 count, 8bit, void element",

            markup(m -> {
              m.input(
                  Id.of("foo")
              );
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.xATTRIBUTE88);
              opts.addInt8(AttributeName.ID.index());
              opts.addInt8(opts.addObject("foo"));
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            })
        },
        {
            "regular attribute, 2 count, 8bit, regular element",

            markup(m -> {
              m.html(
                  m.lang("pt-BR"),
                  m.dir("auto")
              );
            }),

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
            })
        },
        {
            "regular attribute, 2 count, 8bit",

            markup(m -> {
              m.input(
                  m.type("text"),
                  m.size("30")
              );
            }),

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
            })
        },
        {
            "regular attribute, 3 count, 8bit, regular element",

            markup(m -> {
              m.span(
                  m.className("c1"),
                  ClassName.of("c2"),
                  m.className("c3")
              );
            }),

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
            })
        },
        {
            "regular attribute, 3 count, 8bit, void element",

            markup(m -> {
              m.input(
                  ClassName.of("c1"),
                  m.className("c2"),
                  ClassName.of("c3")
              );
            }),

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
            })
        },
        {
            "regular element, no attributes, no children",

            markup(m -> {
              m.div();
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);
            })
        },
        {
            "boolean attribute, 1 count, 8bit, regular element",

            markup(m -> {
              m.span(
                  Markup.autofocus
              );
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.xBOOLEAN8);
              opts.addInt8(AttributeName.AUTOFOCUS.index());
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.SPAN.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            })
        },
        {
            "boolean attribute, 1 count, 8bit, void element",

            markup(m -> {
              m.input(
                  Markup.checked
              );
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.xBOOLEAN8);
              opts.addInt8(AttributeName.CHECKED.index());
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            })
        },
        {
            "regular attribute contenteditable, 1 count, 8bit, regular element",

            markup(m -> {
              m.div(
                  m.contenteditable("false")
              );
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.xATTRIBUTE88);
              opts.addInt8(AttributeName.CONTENTEDITABLE.index());
              opts.addInt8(opts.addObject("false"));
              opts.addByte(HtmlBytes.ELEMSTR8);
              opts.addInt8(ElementName.DIV.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            })
        },
        {
            "regular attribute contenteditable, 1 count, 16bit, regular element",

            markup(HtmlSink.FIRST_INT16, m -> {
              m.div(
                  m.contenteditable("false")
              );
            }),

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
            })
        },
        {
            "regular attribute contenteditable, 1 count, 24bit, regular element",

            markup(HtmlSink.FIRST_INT24, m -> {
              m.div(
                  m.contenteditable("false")
              );
            }),

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
            })
        },
        {
            "regular attribute type, 1 count, 8bit, void element",

            markup(m -> {
              m.input(
                  m.type("password")
              );
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.xATTRIBUTE88);
              opts.addInt8(AttributeName.TYPE.index());
              opts.addInt8(opts.addObject("password"));
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.INPUT.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(1);
              opts.addInt8(0);
            })
        },
        {
            "regular attribute type, 1 count, 16bit, void element",

            markup(HtmlSink.FIRST_INT16, m -> {
              m.input(
                  m.type("password")
              );
            }),

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
            })
        },
        {
            "regular attribute type, 1 count, 24bit, void element",

            markup(HtmlSink.FIRST_INT24, m -> {
              m.input(
                  m.type("password")
              );
            }),

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
            })
        },
        {
            "regular element, no attributes, regular element child",

            markup(m -> {
              m.div(
                  m.span()
              );
            }),

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
            })
        },
        {
            "regular element, no attributes, void element child",

            markup(m -> {
              m.article(
                  m.hr()
              );
            }),

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
            })
        },
        {
            "regular element, no attributes, nested elements",

            markup(m -> {
              m.html(
                  m.head(
                      m.meta()
                  )
              );
            }),

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
            "void element, no attributes",

            markup(m -> {
              m.hr();
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.ELEMVOID8);
              opts.addInt8(ElementName.HR.index());
              opts.addByte(HtmlBytes.VALUES8);
              opts.addInt8(0);
            })
        },
        {
            "testable text, top-level testable heading",

            markup(m -> {
              final TestableNodes t;
              t = m.t();

              t.h1("Heading 1");
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
            })
        },
        {
            "testable text, top-level testable cells",

            markup(m -> {
              final TestableNodes t;
              t = m.t();

              t.cell(1, "1");
              t.cell(1, "2");
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("1"));
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("2"));
            })
        },
        {
            "testable text, testable heading followed by testable cell",

            markup(m -> {
              final TestableNodes t;
              t = m.t();

              t.h1("Heading 1");
              t.cell(1, "x");
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("x"));
            })
        },
        {
            "testable text, document without testable nodes",

            markup(_ -> {}),

            html(_ -> {})
        },
        {
            "testable text, testable list items",

            markup(m -> {
              final TestableNodes t;
              t = m.t();

              t.item("Foo");
              t.item("Bar");
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("Foo"));
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("Bar"));
            })
        },
        {
            "testable text, testable list items separated by a list",

            markup(m -> {
              final TestableNodes t;
              t = m.t();

              t.item("A");
              t.item("B");
              t.list();
              t.item("C");
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("A"));
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("B"));
              opts.addByte(HtmlBytes.TLIST);
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("C"));
            })
        },
        {
            "testable text, testable heading, list items and cells",

            markup(m -> {
              final TestableNodes t;
              t = m.t();

              t.h1("Heading 1");
              t.item("Item");
              t.cell(1, "x");
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.THEADING8);
              opts.addByte((byte) 1);
              opts.addInt8(opts.addObject("Heading 1"));
              opts.addByte(HtmlBytes.TITEM8);
              opts.addInt8(opts.addObject("Item"));
              opts.addByte(HtmlBytes.TCELL8);
              opts.addInt8(opts.addObject("x"));
            })
        },
        {
            "testable text, testable props",

            markup(m -> {
              final TestableNodes t;
              t = m.t();

              t.prop("First name", "John");
              t.prop("Last name", "Smith");
            }),

            html(opts -> {
              opts.addByte(HtmlBytes.TPROP8);
              opts.addObject("First name");
              opts.addInt8(opts.addObject("John"));
              opts.addByte(HtmlBytes.TPROP8);
              opts.addObject("Last name");
              opts.addInt8(opts.addObject("Smith"));
            })
        }};
  }

  @Test(dataProvider = "compileProvider")
  public void compile(String description, Html actual, Html expected) {
    assertEquals(actual, expected, description);
  }

  private Html html(Consumer<? super HtmlSink> opts) {
    return HtmlY.create(opts);
  }

  private Html markup(Consumer<Markup> consumer) {
    final Markup subject;
    subject = Markup.of();

    consumer.accept(subject);

    return subject.compile();
  }

  private Html markup(int objectsIndex, Consumer<? super Markup> consumer) {
    final HtmlSink sink;
    sink = new HtmlSink();

    sink.objectsIndex(objectsIndex);

    final Markup subject;
    subject = MarkupImpl.of(sink);

    consumer.accept(subject);

    return subject.compile();
  }

}
