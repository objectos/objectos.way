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
import objectos.html.ElementName;
import objectos.internal.Util;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class ChildAmbiguousRecorderTest {

  private HtmlSink record(ElementName name, String text) {
    final HtmlSink sink;
    sink = new HtmlSink();

    record(sink, name, text);

    return sink;
  }

  private void record(HtmlSink sink, ElementName name, String text) {
    final ChildAmbiguousRecorder subject;
    subject = new ChildAmbiguousRecorder(sink);

    subject.record(name, text);
  }

  @Test(description = "ELTXT88")
  public void eltxt88() {
    final ElementName name;
    name = ElementNamePojo.TITLE;

    final String text;
    text = "foo";

    assertEquals(
        record(name, text),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELTXT88);
          opts.addInt8(ElementNamePojo.TITLE.index());
          opts.addInt8(opts.addObject(text));
        })
    );
  }

  @Test(description = "ELTXT816")
  public void eltxt816() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final ElementName name;
    name = ElementNamePojo.TITLE;

    record(sink, name, "foo");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.ELTXT816);
          opts.addInt8(ElementNamePojo.TITLE.index());
          opts.addInt16(opts.addObject("foo"));
        })
    );
  }

  @Test(description = "ELTXT824")
  public void eltxt824() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final ElementName name;
    name = ElementNamePojo.TITLE;

    record(sink, name, "foo");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.ELTXT824);
          opts.addInt8(ElementNamePojo.TITLE.index());
          opts.addInt24(opts.addObject("foo"));
        })
    );
  }

}
