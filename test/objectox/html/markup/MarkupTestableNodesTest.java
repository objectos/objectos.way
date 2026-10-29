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

public class MarkupTestableNodesTest {

  @Test(description = "TestableRecorder delegation (h1..h6)")
  public void headings01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final MarkupTestableNodes subject;
    subject = create(sink);

    subject.h1("H1");
    subject.h2("H2");
    subject.h3("H3");
    subject.h4("H4");
    subject.h5("H5");
    subject.h6("H6");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 1);
          opts.addInt8(opts.addObject("H1"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 2);
          opts.addInt8(opts.addObject("H2"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 3);
          opts.addInt8(opts.addObject("H3"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 4);
          opts.addInt8(opts.addObject("H4"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 5);
          opts.addInt8(opts.addObject("H5"));

          opts.addByte(HtmlBytes.THEADING8);
          opts.addByte((byte) 6);
          opts.addInt8(opts.addObject("H6"));
        })
    );
  }

  @Test(description = "TestableRecorder delegation (cell)")
  public void cell01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final MarkupTestableNodes subject;
    subject = create(sink);

    subject.cell(2, "x");

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TCELL8);
          opts.addInt8(2);
          opts.addInt8(opts.addObject("x"));
        })
    );
  }

  private MarkupTestableNodes create(HtmlSink sink) {
    final TestableRecorder recorder;
    recorder = new TestableRecorder(sink);

    return new MarkupTestableNodes(recorder);
  }

}
