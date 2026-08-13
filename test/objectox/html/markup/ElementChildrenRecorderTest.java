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

import objectox.html.AmbiguousName;
import objectox.html.IntSize;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class ElementChildrenRecorderTest {

  @Test(description = "ambiguous name that resolves to a child element")
  public void ambiguous01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.AMBIGUOUS88);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addInt8(99);
    });

    final ElementChildrenRecorder subject;
    subject = new ElementChildrenRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.HEAD;

    final IntSize size;
    size = IntSize.BIT8;

    final AmbiguousPointer pointer;
    pointer = new AmbiguousPointer(3);

    final int res;
    res = subject.record(elementName, size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.eAMBIGUOUS88);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(99);
          opts.addInt8(3);
        })
    );
  }

  @Test(description = "ambiguous name that resolves to an attribute")
  public void ambiguous02() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.aAMBIGUOUS88);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addInt8(99);
    });

    final ElementChildrenRecorder subject;
    subject = new ElementChildrenRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final AmbiguousPointer pointer;
    pointer = new AmbiguousPointer(3);

    final int res;
    res = subject.record(elementName, size, pointer);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.aAMBIGUOUS88);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(99);
        })
    );
  }

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
    subject = new ElementChildrenRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final AttributePointer pointer;
    pointer = new AttributePointer(3);

    final int res;
    res = subject.record(elementName, size, pointer);

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
      opts.addByte(HtmlBytes.RELEMSTR8);
      opts.addInt8(ElementName.HEAD.index());
      opts.addByte(HtmlBytes.VALUES8);
      opts.addInt24(0);
      opts.addByte(HtmlBytes.RELEMEND8);
      opts.addInt8(ElementName.HEAD.index());
    });

    final ElementChildrenRecorder subject;
    subject = new ElementChildrenRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final ElementPointer pointer;
    pointer = new ElementPointer(3);

    final int res;
    res = subject.record(elementName, size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xRELEMSTR8);
          opts.addInt8(ElementName.HEAD.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt24(0);
          opts.addByte(HtmlBytes.RELEMEND8);
          opts.addInt8(ElementName.HEAD.index());
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
    subject = new ElementChildrenRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, NoOpInstructionPojo.INSTANCE);

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
    subject = new ElementChildrenRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final RawPointer pointer;
    pointer = new RawPointer(3);

    final int res;
    res = subject.record(elementName, size, pointer);

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
    subject = new ElementChildrenRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final RawPointer pointer;
    pointer = new RawPointer(3);

    final int res;
    res = subject.record(elementName, size, pointer);

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

}
