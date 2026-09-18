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
import objectos.html.TestableName;
import objectos.internal.Util;
import org.testng.annotations.Test;

public class TestableRecorderTest {

  @Test(description = "returned markup")
  public void markup01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.record(TestableName.H1, "Ipsum");

    assertEquals(res.value(), 3);
  }

  @Test(
      description = "reject null name",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "name == null")
  public void reject01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.record(null, "Ipsum");
  }

  @Test(
      description = "reject null value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void reject02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.record(TestableName.H1, null);
  }

  @Test(description = "TEST8")
  public void test8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.record(TestableName.H1, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TEST8);
          opts.addObject(TestableName.H1);
          opts.addInt8(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "TEST16")
  public void test16() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.record(TestableName.H1, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.TEST16);
          opts.addObject(TestableName.H1);
          opts.addInt16(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "TEST24")
  public void test24() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.record(TestableName.H1, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.TEST24);
          opts.addObject(TestableName.H1);
          opts.addInt24(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "TESTONLY8")
  public void testOnly8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.recordOnly(TestableName.H1, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TESTONLY8);
          opts.addObject(TestableName.H1);
          opts.addInt8(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "TESTONLY16")
  public void testOnly16() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.recordOnly(TestableName.H1, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.TESTONLY16);
          opts.addObject(TestableName.H1);
          opts.addInt16(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "TESTONLY24")
  public void testOnly24() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.recordOnly(TestableName.H1, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.TESTONLY24);
          opts.addObject(TestableName.H1);
          opts.addInt24(opts.addObject("Ipsum"));
        })
    );
  }

}
