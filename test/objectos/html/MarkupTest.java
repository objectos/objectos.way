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

}
