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

import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class ElementChildrenRecorderTest {

  @Test(description = "attribute is not a child; sink unchanged")
  public void attribute01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
    });

    final ElementChildrenRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final AttributePointer pointer;
    pointer = new AttributePointer(3);

    final int res;
    res = subject.record(size, pointer);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
        })
    );
  }

  @Test
  public void element01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ELEMSTR8);
      opts.addInt8(ElementName.HEAD.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt24(0);
    });

    final ElementChildrenRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final ElementPointer pointer;
    pointer = new ElementPointer(3);

    final int res;
    res = subject.record(size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xELEMSTR8);
          opts.addInt8(ElementName.HEAD.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt24(0);
          opts.addInt8(3);
        })
    );
  }

  @Test(description = "no-op is not a child; sink unchanged")
  public void noop01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
    });

    final ElementChildrenRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(size, NoOpMarkupPojo.INSTANCE);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
        })
    );
  }

  @Test(description = "raw node child")
  public void raw01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.RAW8);
      opts.addInt8(opts.addObject("ipsum"));
    });

    final ElementChildrenRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final RawPointer pointer;
    pointer = new RawPointer(3);

    final int res;
    res = subject.record(size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xRAW8);
          opts.addInt8(opts.addObject("ipsum"));
          opts.addInt8(3);
        })
    );
  }

  @Test(description = "text node child")
  public void text01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.TEXT8);
      opts.addInt8(opts.addObject("ipsum"));
    });

    final ElementChildrenRecorder subject;
    subject = create(sink);

    final IntSize size;
    size = IntSize.BIT8;

    final TextPointer pointer;
    pointer = new TextPointer(3);

    final int res;
    res = subject.record(size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xTEXT8);
          opts.addInt8(opts.addObject("ipsum"));
          opts.addInt8(3);
        })
    );
  }

  private ElementChildrenRecorder create(HtmlSink sink) {
    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentChildRecorder fragmentChildRecorder;
    fragmentChildRecorder = new FragmentChildRecorder(sink, valueRecorder);

    return new ElementChildrenRecorder(fragmentChildRecorder, valueRecorder);
  }

}
