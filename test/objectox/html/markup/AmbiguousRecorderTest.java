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
import objectox.html.AmbiguousName;
import org.testng.annotations.Test;

public class AmbiguousRecorderTest {

  @Test(
      description = "reject null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void reject01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AmbiguousRecorder subject;
    subject = new AmbiguousRecorder(sink);

    final AmbiguousName ambiguous;
    ambiguous = AmbiguousName.TITLE;

    final String value;
    value = null;

    subject.record(ambiguous, value);
  }

  @Test(description = "AMBIGUOUS88")
  public void ambiguous88() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AmbiguousRecorder subject;
    subject = new AmbiguousRecorder(sink);

    final AmbiguousName ambiguous;
    ambiguous = AmbiguousName.TITLE;

    final String value;
    value = "foo";

    final AmbiguousPointer res;
    res = subject.record(ambiguous, value);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.AMBIGUOUS88);
          opts.addInt8(ambiguous.index());
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test(description = "AMBIGUOUS816")
  public void ambiguous816() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final AmbiguousRecorder subject;
    subject = new AmbiguousRecorder(sink);

    final AmbiguousName ambiguous;
    ambiguous = AmbiguousName.TITLE;

    final String value;
    value = "foo";

    final AmbiguousPointer res;
    res = subject.record(ambiguous, value);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.AMBIGUOUS816);
          opts.addInt8(ambiguous.index());
          opts.addInt16(opts.addObject("foo"));
        })
    );
  }

  @Test(description = "AMBIGUOUS824")
  public void ambiguous824() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final AmbiguousRecorder subject;
    subject = new AmbiguousRecorder(sink);

    final AmbiguousName ambiguous;
    ambiguous = AmbiguousName.TITLE;

    final String value;
    value = "foo";

    final AmbiguousPointer res;
    res = subject.record(ambiguous, value);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.AMBIGUOUS824);
          opts.addInt8(ambiguous.index());
          opts.addInt24(opts.addObject("foo"));
        })
    );
  }

}
