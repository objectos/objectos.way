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

import objectos.html.markup.Fragment0;
import objectox.html.attr.AttributeName;
import org.testng.annotations.Test;

public class FragmentRecorderTest {

  @Test
  public void testCase01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FragmentRecorder subject;
    subject = new FragmentRecorder(sink);

    final Fragment0 f;
    f = () -> {

    };

    final FragmentPointer res;
    res = subject.record(f);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(5);
          opts.addByte(HtmlBytes.FRAGEND);
        })
    );
  }

  @Test
  public void testCase02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FragmentRecorder subject;
    subject = new FragmentRecorder(sink);

    final Fragment0 f;
    f = () -> {
      sink.addByte(HtmlBytes.ATTRIBUTE88);
      sink.addInt8(AttributeName.ID.index());
      sink.addInt8(sink.addObject("foo"));
    };

    final FragmentPointer res;
    res = subject.record(f);

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.FRAGSTR);
          opts.addInt24(8);
          opts.addByte(HtmlBytes.ATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addByte(HtmlBytes.FRAGEND);
        })
    );
  }

}
