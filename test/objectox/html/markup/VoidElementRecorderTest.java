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
import objectos.html.Id;
import objectos.html.markup.VoidElementValue;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class VoidElementRecorderTest {

  @Test(description = """
  ElementAttributesRecorder delegation
  """)
  public void testCase01() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
        },

        ElementName.IMG, v(new AttributePointer(3)),
        6,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.IMG.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        }
    );
  }

  @Test(description = """
  Attribute object (savepoint rollback + pre-record)
  """)
  public void testCase02() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
        },

        ElementName.INPUT, v(Id.of("foo")),
        6,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        }
    );
  }

  @Test(description = """
  reg attr:
  - count: 2
  - markup
  - name (8 bit)
  - void elem
  """)
  public void testCase03() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.TYPE.index());
          opts.addInt8(opts.addObject("text"));
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.SIZE.index());
          opts.addInt8(opts.addObject("30"));
        },

        ElementName.INPUT, v(new AttributePointer(3), new AttributePointer(6)),
        9,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
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
          opts.addInt8(3);
          opts.addInt8(6);
        }
    );
  }

  @Test(description = """
  Attribute jsaction (savepoint rollback + pre-record)
  """)
  public void testCase04() {
    final JsAction action;
    action = Js.noop();

    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.JSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
        },

        ElementName.INPUT, v(new AttributePointer(3)),
        6,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        }
    );
  }

  @Test(description = """
  void element without values
  """)
  public void testCase05() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
        },

        ElementName.HR, v(),
        3,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        }
    );
  }

  private void test(
      Consumer<? super HtmlSink> input,
      ElementName name, VoidElementValue[] values,
      int pointer,
      Consumer<? super HtmlSink> expected
  ) {
    final HtmlSink sink;
    sink = HtmlSinkY.create(input);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder fragmentAttributeRecorder;
    fragmentAttributeRecorder = new FragmentAttributeRecorder(sink, valueRecorder);

    final ElementAttributesRecorder attributesRecorder;
    attributesRecorder = new ElementAttributesRecorder(fragmentAttributeRecorder, valueRecorder);

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final FragmentRecorder fragmentRecorder;
    fragmentRecorder = new FragmentRecorder(sink);

    final ElementPreRecorder elementPreRecorder;
    elementPreRecorder = new ElementPreRecorder(attributeRecorder, booleanAttributeRecorder, fragmentRecorder);

    final VoidElementRecorder subject;
    subject = new VoidElementRecorder(sink, attributesRecorder, elementPreRecorder);

    final ElementPointer res;
    res = subject.record(name, values);

    assertEquals(res.value(), pointer);

    final HtmlSink out;
    out = HtmlSinkY.create(expected);

    assertEquals(sink, out);
  }

  private VoidElementValue[] v(VoidElementValue... values) {
    return values;
  }

}
