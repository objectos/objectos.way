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

import java.util.stream.IntStream;
import objectos.internal.Util;
import org.testng.annotations.Test;

public class RawRecorderTest {

  @Test(description = "returned markup")
  public void markup01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final RawRecorder subject;
    subject = new RawRecorder(sink);

    final String value;
    value = "Ipsum";

    final RawPointer res;
    res = subject.record(value);

    assertEquals(res.value(), 3);
  }

  @Test(
      description = "reject null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void reject01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final RawRecorder subject;
    subject = new RawRecorder(sink);

    subject.record(null);
  }

  @Test(description = "RAW8")
  public void raw8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final RawRecorder subject;
    subject = new RawRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.RAW8);
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test(description = "RAW16")
  public void raw16() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final RawRecorder subject;
    subject = new RawRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.RAW16);
          opts.addInt16(opts.addObject(value));
        })
    );
  }

  @Test(description = "RAW24")
  public void raw24() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final RawRecorder subject;
    subject = new RawRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.RAW24);
          opts.addInt24(opts.addObject(value));
        })
    );
  }

}
