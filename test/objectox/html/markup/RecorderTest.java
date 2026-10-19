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
package objectox.html.markup;

import static org.testng.Assert.assertEquals;

import java.util.function.Consumer;
import objectos.html.Markup;
import objectos.html.markup.Fragment0;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.HtmlImpl;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class RecorderTest {

  @Test(description = "empty document")
  public void testCase01() {
    test(
        _ -> {},

        _ -> {}
    );
  }

  @Test(description = "attribute(AttributeName, String) delegation")
  public void testCase02() {
    final String value;
    value = "foo";

    test(
        subject -> subject.attribute(AttributeName.CLASS, value),

        opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "attribute(AttributeName, JsAction) delegation")
  public void testCase03() {
    final JsAction action;
    action = Js.noop();

    test(
        subject -> subject.attribute(AttributeName.ONCLICK, action),

        opts -> {
          opts.addByte(HtmlBytes.JSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
        }
    );
  }

  @Test(description = "attribute(String, String) delegation")
  public void testCase04() {
    final String value;
    value = "foo";

    test(
        subject -> subject.attribute("class", value),

        opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "attribute(String, JsAction) delegation")
  public void testCase05() {
    final JsAction action;
    action = Js.noop();

    test(
        subject -> subject.attribute("onclick", action),

        opts -> {
          opts.addByte(HtmlBytes.JSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
        }
    );
  }

  @Test(description = "css(String) delegation")
  public void testCase06() {
    final String value;
    value = "foo";

    test(
        subject -> subject.css(value),

        opts -> {
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.CLASS.index());
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "booleanAttribute(AttributeName) delegation")
  public void testCase07() {
    test(
        subject -> subject.booleanAttribute(AttributeName.CHECKED),

        opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(AttributeName.CHECKED.index());
        }
    );
  }

  @Test(description = "booleanAttribute(BooleanAttribute) delegation")
  public void testCase08() {
    test(
        subject -> subject.booleanAttribute(Markup.autofocus),

        opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(AttributeName.AUTOFOCUS.index());
        }
    );
  }

  @Test(description = "booleanAttribute(String) delegation")
  public void testCase09() {
    test(
        subject -> subject.booleanAttribute("checked"),

        opts -> {
          opts.addByte(HtmlBytes.BOOLEAN8);
          opts.addInt8(AttributeName.CHECKED.index());
        }
    );
  }

  @Test(description = "DoctypeRecorder delegation")
  public void testCase10() {
    test(
        subject -> subject.doctype(),

        opts -> {
          opts.addByte(HtmlBytes.DOCTYPE);
        }
    );
  }

  @Test(description = "DocumentCompiler delegation (compile)")
  public void testCase11() {
    test(
        subject -> {
          final HtmlImpl compiled;
          compiled = subject.compile();

          assertEquals(compiled, new HtmlImpl(new HtmlSink()));
        },

        _ -> {}
    );
  }

  @Test(description = "DocumentCompiler delegation (unwrap)")
  public void testCase12() {
    test(
        subject -> {
          final HtmlSink unwrapped;
          unwrapped = subject.unwrap();

          unwrapped.addByte(HtmlBytes.DOCTYPE);
        },

        opts -> {
          opts.addByte(HtmlBytes.DOCTYPE);
        }
    );
  }

  @Test(description = "FragmentRecorder delegation (Fragment0)")
  public void testCase13() {
    final Fragment0 fragment;
    fragment = () -> {

    };

    test(
        subject -> subject.f(fragment),

        opts -> {
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(5);
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "FragmentRecorder delegation (Fragment1)")
  public void testCase14() {
    test(
        subject -> subject.f((arg1) -> {
          subject.text(arg1);
        }, "a"),

        opts -> {
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(7);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("a"));
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "FragmentRecorder delegation (Fragment2)")
  public void testCase15() {
    test(
        subject -> subject.f((arg1, arg2) -> {
          subject.text(arg1);
          subject.text(arg2);
        }, "a", "b"),

        opts -> {
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(9);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("a"));
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("b"));
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "FragmentRecorder delegation (Fragment3)")
  public void testCase16() {
    test(
        subject -> subject.f((arg1, arg2, arg3) -> {
          subject.text(arg1);
          subject.text(arg2);
          subject.text(arg3);
        }, "a", "b", "c"),

        opts -> {
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(11);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("a"));
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("b"));
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("c"));
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "FragmentRecorder delegation (Fragment4)")
  public void testCase17() {
    test(
        subject -> subject.f((arg1, arg2, arg3, arg4) -> {
          subject.text(arg1);
          subject.text(arg2);
          subject.text(arg3);
          subject.text(arg4);
        }, "a", "b", "c", "d"),

        opts -> {
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(13);
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("a"));
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("b"));
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("c"));
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject("d"));
          opts.addByte(HtmlBytes.FRAGEND);
        }
    );
  }

  @Test(description = "RawRecorder delegation")
  public void testCase18() {
    final String value;
    value = "Ipsum";

    test(
        subject -> subject.raw(value),

        opts -> {
          opts.addByte(HtmlBytes.RAW8);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "RegularElementRecorder delegation (ElementName)")
  public void testCase19() {
    test(
        subject -> subject.regularElement(ElementName.DIV),

        opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }
    );
  }

  @Test(description = "RegularElementRecorder delegation (ElementName, text)")
  public void testCase20() {
    final String value;
    value = "Hello";

    test(
        subject -> subject.regularElement(ElementName.DIV, value),

        opts -> {
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject(value));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        }
    );
  }

  @Test(description = "RegularElementRecorder delegation (String)")
  public void testCase21() {
    test(
        subject -> subject.regularElement("div"),

        opts -> {
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }
    );
  }

  @Test(description = "RegularElementRecorder delegation (String, text)")
  public void testCase22() {
    final String value;
    value = "Hello";

    test(
        subject -> subject.regularElement("div", value),

        opts -> {
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject(value));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
        }
    );
  }

  @Test(description = "TestableRecorder delegation (tbr)")
  public void testCase23() {
    test(
        subject -> subject.tbr(),

        opts -> {
          opts.addByte(HtmlBytes.TNEWLINE);
        }
    );
  }

  @Test(description = "TestableRecorder delegation (tcell)")
  public void testCase24() {
    final String value;
    value = "x";

    test(
        subject -> subject.tcell(2, value),

        opts -> {
          opts.addByte(HtmlBytes.TCELL8);
          opts.addInt8(2);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (th1)")
  public void testCase25() {
    final String value;
    value = "x";

    test(
        subject -> subject.th1(value),

        opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (th2)")
  public void testCase26() {
    final String value;
    value = "x";

    test(
        subject -> subject.th2(value),

        opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 2);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (th3)")
  public void testCase27() {
    final String value;
    value = "x";

    test(
        subject -> subject.th3(value),

        opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 3);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (th4)")
  public void testCase28() {
    final String value;
    value = "x";

    test(
        subject -> subject.th4(value),

        opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 4);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (th5)")
  public void testCase29() {
    final String value;
    value = "x";

    test(
        subject -> subject.th5(value),

        opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 5);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (th6)")
  public void testCase30() {
    final String value;
    value = "x";

    test(
        subject -> subject.th6(value),

        opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 6);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (tprop)")
  public void testCase31() {
    final String key;
    key = "k";

    final String value;
    value = "v";

    test(
        subject -> subject.tprop(key, value),

        opts -> {
          opts.addByte(HtmlBytes.TPROP8);
          opts.addInt8(opts.addObject(key));
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (tpropkey)")
  public void testCase32() {
    final String key;
    key = "k";

    test(
        subject -> subject.tpropkey(key),

        opts -> {
          opts.addByte(HtmlBytes.TPROPKEY8);
          opts.addInt8(opts.addObject(key));
        }
    );
  }

  @Test(description = "TestableRecorder delegation (tpropval)")
  public void testCase33() {
    final String value;
    value = "v";

    test(
        subject -> subject.tpropval(value),

        opts -> {
          opts.addByte(HtmlBytes.TPROPVAL8);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "TextRecorder delegation")
  public void testCase34() {
    final String value;
    value = "Ipsum";

    test(
        subject -> subject.text(value),

        opts -> {
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject(value));
        }
    );
  }

  @Test(description = "VoidElementRecorder delegation (ElementName)")
  public void testCase35() {
    test(
        subject -> subject.voidElement(ElementName.BR),

        opts -> {
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.BR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }
    );
  }

  @Test(description = "VoidElementRecorder delegation (String)")
  public void testCase36() {
    test(
        subject -> subject.voidElement("br"),

        opts -> {
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.BR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }
    );
  }

  private Recorder create(HtmlSink sink) {
    return Recorder.of(sink);
  }

  private void test(Consumer<? super Recorder> action, Consumer<? super HtmlSink> opts) {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    action.accept(subject);

    assertEquals(sink, HtmlSinkY.create(opts));
  }

}
