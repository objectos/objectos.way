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
import objectox.html.HtmlPojo;
import org.testng.annotations.Test;

public class ChildFragmentRecorderTest {

  @Test(description = "FRAG16")
  public void frag16() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[256]);

    final ChildFragmentRecorder subject;
    subject = new ChildFragmentRecorder(sink);

    final HtmlPojo html;
    html = html();

    subject.record(html);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 256).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.FRAG16);
          opts.addInt16(opts.addObject(html));
        })
    );
  }

  @Test(description = "FRAG24")
  public void frag24() {
    final HtmlSink sink;
    sink = new HtmlSink(Util.EMPTY_BYTE_ARRAY, new Object[1 << 16]);

    final ChildFragmentRecorder subject;
    subject = new ChildFragmentRecorder(sink);

    final HtmlPojo html;
    html = html();

    subject.record(html);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          IntStream.range(0, 1 << 16).forEach(_ -> opts.addObject(null));
          opts.addByte(HtmlBytes.FRAG24);
          opts.addInt24(opts.addObject(html));
        })
    );
  }

  @Test(description = "FRAG8")
  public void frag8() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ChildFragmentRecorder subject;
    subject = new ChildFragmentRecorder(sink);

    final HtmlPojo html;
    html = html();

    subject.record(html);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.FRAG8);
          opts.addInt8(opts.addObject(html));
        })
    );
  }

  private HtmlPojo html() {
    return new HtmlPojo(new HtmlSink());
  }

}
