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

public class TextRecorderTest {

  @Test(description = "returned markup")
  public void markup01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TextRecorder subject;
    subject = new TextRecorder(sink);

    final String value;
    value = "Ipsum";

    final TextPointer res;
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

    final TextRecorder subject;
    subject = new TextRecorder(sink);

    subject.record(null);
  }

  @Test(description = "TEXT8")
  public void text8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TextRecorder subject;
    subject = new TextRecorder(sink);

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
  public void text16() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT16);
    });

    final TextRecorder subject;
    subject = new TextRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT16);
          opts.addByte(HtmlBytes.TEXT16);
          opts.addInt16(opts.addObject(value));
        })
    );
  }

  @Test(description = "TEXT24")
  public void text24() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.objectsIndex(HtmlSink.FIRST_INT24);
    });

    final TextRecorder subject;
    subject = new TextRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.objectsIndex(HtmlSink.FIRST_INT24);
          opts.addByte(HtmlBytes.TEXT24);
          opts.addInt24(opts.addObject(value));
        })
    );
  }

}
