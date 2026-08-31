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
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class TextElementRecorderTest {

  @Test(description = "returned markup")
  public void markup01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(0);
    });

    final TextElementRecorder subject;
    subject = new TextElementRecorder(sink);

    final String value;
    value = "Ipsum";

    final ElementPointer res;
    res = subject.record(ElementName.SPAN, value);

    assertEquals(res.value(), 3);
  }

  @Test(
      description = "reject null text",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "text == null")
  public void reject01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TextElementRecorder subject;
    subject = new TextElementRecorder(sink);

    subject.record(ElementName.SPAN, null);
  }

  @Test(description = "ELEMTXT88")
  public void elemTxt88() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TextElementRecorder subject;
    subject = new TextElementRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(ElementName.SPAN, value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELEMTXT88);
          opts.addInt8(ElementName.SPAN.index());
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test(description = "ELEMTXT816")
  public void elemTxt816() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final TextElementRecorder subject;
    subject = new TextElementRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(ElementName.SPAN, value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.ELEMTXT816);
          opts.addInt8(ElementName.SPAN.index());
          opts.addInt16(opts.addObject(value));
        })
    );
  }

  @Test(description = "ELEMTXT824")
  public void elemTxt824() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final TextElementRecorder subject;
    subject = new TextElementRecorder(sink);

    final String value;
    value = "Ipsum";

    subject.record(ElementName.SPAN, value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.ELEMTXT824);
          opts.addInt8(ElementName.SPAN.index());
          opts.addInt24(opts.addObject(value));
        })
    );
  }

  @Test(description = "ELEMTXT88 escaped")
  public void textEscaper() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final TextElementRecorder subject;
    subject = new TextElementRecorder(sink);

    final String value;
    value = "A&B";

    final String escaped;
    escaped = "A&amp;B";

    subject.record(ElementName.SPAN, value);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELEMTXT88);
          opts.addInt8(ElementName.SPAN.index());
          opts.addInt8(opts.addObject(escaped));
        })
    );
  }

}
