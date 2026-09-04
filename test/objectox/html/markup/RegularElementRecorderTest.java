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

import objectos.html.Id;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class RegularElementRecorderTest {

  @Test(description = """
  ElementAttributesRecorder delegation
  """)
  public void attrs01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
    });

    final RegularElementRecorder subject;
    subject = create(sink);

    final AttributePointer pointer;
    pointer = new AttributePointer(3);

    final ElementPointer res;
    res = subject.record(ElementName.DIV, pointer);

    assertEquals(res.value(), 6);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        })
    );
  }

  @Test(description = """
  ElementAttributesRecorder before ElementsChildrenRecorder
  """)
  public void attrs02() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.HR.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
    });

    final RegularElementRecorder subject;
    subject = create(sink);

    final ElementPointer pointer1;
    pointer1 = new ElementPointer(3);

    final AttributePointer pointer2;
    pointer2 = new AttributePointer(7);

    final ElementPointer res;
    res = subject.record(ElementName.DIV, pointer1, pointer2);

    assertEquals(res.value(), 10);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  Attribute object (savepoint rollback + pre-record)
  """)
  public void regularAttribute03() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
    });

    final RegularElementRecorder subject;
    subject = create(sink);

    final Id id;
    id = Id.of("foo");

    final ElementPointer res;
    res = subject.record(ElementName.DIV, id);

    assertEquals(res.value(), 6);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMSTR8);
          opts.addInt8(ElementName.DIV.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        })
    );
  }

  @Test(description = """
  ElementChildrenRecorder delegation
  """)
  public void children01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ELEMVOID8);
      opts.addInt8(ElementName.HR.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt8(0);
    });

    final RegularElementRecorder subject;
    subject = create(sink);

    final ElementPointer pointer;
    pointer = new ElementPointer(3);

    final ElementPointer res;
    res = subject.record(ElementName.DIV, pointer);

    assertEquals(res.value(), 7);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  private RegularElementRecorder create(HtmlSink sink) {
    final AmbiguousAttributeRecorder ambiguousAttributeRecorder;
    ambiguousAttributeRecorder = new AmbiguousAttributeRecorder(sink);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder fragmentAttributeRecorder;
    fragmentAttributeRecorder = new FragmentAttributeRecorder(sink, ambiguousAttributeRecorder, valueRecorder);

    final ElementAttributesRecorder attributesRecorder;
    attributesRecorder = new ElementAttributesRecorder(ambiguousAttributeRecorder, fragmentAttributeRecorder, valueRecorder);

    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    final FragmentRecorder fragmentRecorder;
    fragmentRecorder = new FragmentRecorder(sink);

    final ElementPreRecorder elementPreRecorder;
    elementPreRecorder = new ElementPreRecorder(attributeRecorder, booleanAttributeRecorder, fragmentRecorder);

    final AmbiguousChildRecorder ambiguousChildRecorder;
    ambiguousChildRecorder = new AmbiguousChildRecorder(sink);

    final FragmentChildRecorder fragmentChildRecorder;
    fragmentChildRecorder = new FragmentChildRecorder(sink, ambiguousChildRecorder, valueRecorder);

    final ElementChildrenRecorder childrenRecorder;
    childrenRecorder = new ElementChildrenRecorder(ambiguousChildRecorder, fragmentChildRecorder, valueRecorder);

    return new RegularElementRecorder(sink, attributesRecorder, childrenRecorder, elementPreRecorder);
  }

}
