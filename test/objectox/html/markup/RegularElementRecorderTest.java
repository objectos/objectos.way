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
import objectos.html.markup.RegularElementValue;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class RegularElementRecorderTest {

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

        ElementName.DIV, v(new AttributePointer(3)),
        6,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        }
    );
  }

  @Test(description = """
  ElementAttributesRecorder before ElementsChildrenRecorder
  """)
  public void testCase02() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
        },

        ElementName.DIV, v(new ElementPointer(3), new AttributePointer(7)),
        10,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(7);
          opts.addInt8(3);
        }
    );
  }

  @Test(description = """
  Attribute object (savepoint rollback + pre-record)
  """)
  public void testCase03() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
        },

        ElementName.DIV, v(Id.of("foo")),
        6,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        }
    );
  }

  @Test(description = """
  ElementChildrenRecorder delegation
  """)
  public void testCase04() {
    test(
        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        },

        ElementName.DIV, v(new ElementPointer(3)),
        7,

        opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        }
    );
  }

  private void test(
      Consumer<? super HtmlSink> input,
      ElementName name, RegularElementValue[] values,
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

    final FragmentChildRecorder fragmentChildRecorder;
    fragmentChildRecorder = new FragmentChildRecorder(sink, valueRecorder);

    final ElementChildrenRecorder childrenRecorder;
    childrenRecorder = new ElementChildrenRecorder(fragmentChildRecorder, valueRecorder);

    final RegularElementRecorder subject;
    subject = new RegularElementRecorder(sink, attributesRecorder, childrenRecorder, elementPreRecorder);

    final ElementPointer res;
    res = subject.record(name, values);

    assertEquals(res.value(), pointer);

    final HtmlSink out;
    out = HtmlSinkY.create(expected);

    assertEquals(sink, out);
  }

  private RegularElementValue[] v(RegularElementValue... values) {
    return values;
  }

}
