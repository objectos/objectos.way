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
import org.testng.annotations.Test;

public class RecorderTest {

  private Recorder create(HtmlSink sink) {
    return Recorder.of(sink);
  }

  @Test(description = "AmbiguousRecorder delegation")
  public void ambiguous01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    final AmbiguousName name;
    name = AmbiguousName.TITLE;

    final String value;
    value = "foo";

    final AmbiguousPointer res;
    res = subject.ambiguous(name, value);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.AMBIGUOUS88);
          opts.addInt8(name.index());
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test(description = "DoctypeRecorder delegation")
  public void doctype01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    subject.doctype();

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.DOCTYPE);
        })
    );
  }

  @SuppressWarnings("unused")
  @Test(description = "empty document")
  public void record00() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final Recorder subject;
    subject = create(sink);

    assertEquals(sink, HtmlSinkY.create(_ -> {}));
  }

}
