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
import objectox.html.IntSize;
import objectox.html.elem.ElementName;
import org.testng.annotations.Test;

public class AmbiguousAttributeRecorderTest {

  @Test(description = "ambiguous name that resolves to an attribute")
  public void ambiguous01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.AMBIGUOUS88);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt8(opts.addObject("foo"));
    });

    final AmbiguousAttributeRecorder subject;
    subject = new AmbiguousAttributeRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 3);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.aAMBIGUOUS88);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(3);
        })
    );
  }

  @Test(description = "ambiguous name that does not resolve to an attribute")
  public void ambiguous02() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addInt24(HtmlSink.MAX_INT24);
      opts.addByte(HtmlBytes.AMBIGUOUS88);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt8(opts.addObject("foo"));
    });

    final AmbiguousAttributeRecorder subject;
    subject = new AmbiguousAttributeRecorder(sink);

    final ElementName elementName;
    elementName = ElementName.HEAD;

    final IntSize size;
    size = IntSize.BIT8;

    final int res;
    res = subject.record(elementName, size, 0);

    assertEquals(res, 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addInt24(HtmlSink.MAX_INT24);
          opts.addByte(HtmlBytes.AMBIGUOUS88);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt8(opts.addObject("foo"));
        })
    );
  }

}
