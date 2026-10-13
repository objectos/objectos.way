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
import org.testng.annotations.Test;

public class ValueRecorderTest {

  @Test(description = "ATTRIBUTE88")
  public void attribute88() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(sink);

    final int res;
    res = subject.record(IntSize.BIT8, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(3);
        })
    );
  }

  @Test(description = "ATTRIBUTE816")
  public void attribute816() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ATTRIBUTE816);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt16(opts.addObject("foo"));
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(sink);

    final int res;
    res = subject.record(IntSize.BIT8, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE816);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt16(opts.addObject("foo"));
          opts.addInt8(3);
        })
    );
  }

  @Test(description = "ATTRIBUTE824")
  public void attribute824() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.ATTRIBUTE824);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt24(opts.addObject("foo"));
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(sink);

    final int res;
    res = subject.record(IntSize.BIT8, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xATTRIBUTE824);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt24(opts.addObject("foo"));
          opts.addInt8(3);
        })
    );
  }

  @Test(description = "THEADING8 is consumed")
  public void heading8() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.THEADING8);
      opts.addByte((byte) 1);
      opts.addInt8(opts.addObject("Ipsum"));
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(sink);

    final int res;
    res = subject.record(IntSize.BIT8, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.xTHEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
          opts.addInt8(3);
        })
    );
  }

  @Test(description = """
  THEADING8 is not consumed
  """)
  public void testableOnly01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.THEADING8);
      opts.addByte((byte) 1);
      opts.addInt8(opts.addObject("Ipsum"));
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(sink);

    final int res;
    res = subject.testableOnly(IntSize.BIT8, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
          opts.addInt8(3);
        })
    );
  }

  @Test(description = """
  TCELL24 is not consumed
  """)
  public void testableOnly02() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.TCELL24);
      opts.addInt24(5);
      opts.addInt24(opts.addObject("12345"));
    });

    final ValueRecorder subject;
    subject = new ValueRecorder(sink);

    final int res;
    res = subject.testableOnly(IntSize.BIT24, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.TCELL24);
          opts.addInt24(5);
          opts.addInt24(opts.addObject("12345"));
          opts.addInt24(3);
        })
    );
  }

}
