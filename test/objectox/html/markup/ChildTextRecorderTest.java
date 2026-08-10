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

public class ChildTextRecorderTest {

  @Test(
      description = "reject null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void record01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ChildTextRecorder subject;
    subject = new ChildTextRecorder(sink);

    subject.record(null);
  }

  @Test(description = "TEXT8")
  public void record02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ChildTextRecorder subject;
    subject = new ChildTextRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test(description = "TEXT16")
  public void record03() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final ChildTextRecorder subject;
    subject = new ChildTextRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.TEXT16);
          opts.addInt16(opts.addObject(value));
        })
    );
  }

  @Test(description = "TEXT24")
  public void record04() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final ChildTextRecorder subject;
    subject = new ChildTextRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.TEXT24);
          opts.addInt24(opts.addObject(value));
        })
    );
  }

  @Test(description = "TEXT8 escaped")
  public void record05() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ChildTextRecorder subject;
    subject = new ChildTextRecorder(sink);

    final String value;
    value = "A&B";

    final String escaped;
    escaped = "A&amp;B";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject(escaped));
        })
    );
  }

}
