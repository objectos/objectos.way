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
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class VoidElementRecorderTest {

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

    final VoidElementRecorder subject;
    subject = create(sink);

    final AttributePointer pointer;
    pointer = new AttributePointer(3);

    final ElementPointer res;
    res = subject.record(ElementName.IMG, pointer);

    assertEquals(res.value(), 6);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.IMG.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        })
    );
  }

  @Test(description = """
  Attribute object (savepoint rollback + pre-record)
  """)
  public void voidElement04() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
    });

    final VoidElementRecorder subject;
    subject = create(sink);

    final Id id;
    id = Id.of("foo");

    final ElementPointer res;
    res = subject.record(ElementName.INPUT, id);

    assertEquals(res.value(), 6);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        })
    );
  }

  @Test(description = """
  void elem:
  - 2 reg attrs (markup) (8bit)
  """)
  public void voidElement06() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.TYPE.index());
      opts.addInt8(opts.addObject("text"));
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.SIZE.index());
      opts.addInt8(opts.addObject("30"));
    });

    final VoidElementRecorder subject;
    subject = create(sink);

    final AttributePointer type;
    type = new AttributePointer(3);

    final AttributePointer size;
    size = new AttributePointer(6);

    final ElementPointer res;
    res = subject.record(ElementName.INPUT, type, size);

    assertEquals(res.value(), 9);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
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
        })
    );
  }

  @Test(description = """
  Attribute jsaction (savepoint rollback + pre-record)
  """)
  public void voidElement05() {
    final JsAction action;
    action = Js.noop();

    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.JSACTION88);
      opts.addInt8(AttributeName.ONCLICK.index());
      opts.addInt8(opts.addObject(action));
    });

    final VoidElementRecorder subject;
    subject = create(sink);

    final AttributePointer pointer;
    pointer = new AttributePointer(3);

    final ElementPointer res;
    res = subject.record(ElementName.INPUT, pointer);

    assertEquals(res.value(), 6);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xJSACTION88);
          opts.addInt8(AttributeName.ONCLICK.index());
          opts.addInt8(opts.addObject(action));
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.INPUT.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(3);
        })
    );
  }

  @Test(description = """
  void element without values
  """)
  public void void01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
    });

    final VoidElementRecorder subject;
    subject = create(sink);

    final ElementPointer res;
    res = subject.record(ElementName.HR);

    assertEquals(res.value(), 3);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.ELEMVOID8);
          opts.addInt8(ElementName.HR.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        })
    );
  }

  private VoidElementRecorder create(HtmlSink sink) {
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

    final ElementAttributesPreRecorder elementAttributesPreRecorder;
    elementAttributesPreRecorder = new ElementAttributesPreRecorder(attributeRecorder, booleanAttributeRecorder);

    return new VoidElementRecorder(sink, attributesRecorder, elementAttributesPreRecorder);
  }

}
