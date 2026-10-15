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

import org.testng.annotations.Test;

public class TestableRecorderTest {

  @Test(description = "heading: THEADING8")
  public void heading8() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT8);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.heading(1, "Ipsum");

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "heading: THEADING16")
  public void heading16() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT16);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.heading(6, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.THEADING16);
          opts.addByte((byte) 6);
          opts.addInt16(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "heading: THEADING24")
  public void heading24() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT24);
    });

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.heading(3, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.THEADING24);
          opts.addByte((byte) 3);
          opts.addInt24(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(
      description = "reject invalid heading level",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "level must be between 1 and 6")
  public void headingReject01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.heading(0, "Ipsum");
  }

  @Test(
      description = "reject null heading value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void headingReject02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.heading(1, null);
  }

  @Test(description = "cell: TCELL8")
  public void cell8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.cell(3, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TCELL8);
          opts.addInt8(3);
          opts.addInt8(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(description = "cell: width determines the IntSize")
  public void cell16() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.cell(256, "Ipsum");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TCELL16);
          opts.addInt16(256);
          opts.addInt16(opts.addObject("Ipsum"));
        })
    );
  }

  @Test(
      description = "reject invalid cell width",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "width must be > 0")
  public void cellReject01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.cell(0, "Ipsum");
  }

  @Test(description = "property: TPROP8")
  public void property8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.property("foo", "bar");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TPROP8);
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(opts.addObject("bar"));
        })
    );
  }

  @Test(
      description = "reject null property key",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "key == null")
  public void propertyReject01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.property(null, "bar");
  }

  @Test(
      description = "reject null property value",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null")
  public void propertyReject02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.property("foo", null);
  }

  @Test(description = "prop key: TPROPKEY8")
  public void propKey8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.propKey("foo");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TPROPKEY8);
          opts.addInt8(opts.addObject("foo"));
        })
    );
  }

  @Test(description = "prop value: TPROPVAL8")
  public void propValue8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    subject.propValue("bar");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TPROPVAL8);
          opts.addInt8(opts.addObject("bar"));
        })
    );
  }

  @Test(description = "new line: TNEWLINE")
  public void newLine01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TestableRecorder subject;
    subject = new TestableRecorder(sink);

    final TestableMarkupPointer res;
    res = subject.newLine();

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TNEWLINE);
        })
    );
  }

}
