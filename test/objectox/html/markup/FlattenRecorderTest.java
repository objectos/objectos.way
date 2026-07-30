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

public class FlattenRecorderTest {

  private FlattenRecorder create(HtmlSink sink) {
    return new FlattenRecorder(
        sink,

        new ContentsPrecorder(
            new AttributeRecorder(sink),

            new BooleanAttributeRecorder(sink)
        ),

        new ContentsRecorder(sink)
    );
  }

  @Test
  public void record01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final FlattenRecorder subject;
    subject = create(sink);

    final FlattenPointer res;
    res = subject.record();

    assertEquals(res.value(), 0);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTFLAT);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(0);
        })
    );
  }

  @Test
  public void record02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final int attrIndex;
    attrIndex = sink.addByte(HtmlBytes.ATTRIBUTE88);

    final AttributePointer attr;
    attr = new AttributePointer(attrIndex);

    final int elemIndex;
    elemIndex = sink.addByte(HtmlBytes.STARTTAG8);

    final ElementPointer elem;
    elem = new ElementPointer(elemIndex);

    final FlattenRecorder subject;
    subject = create(sink);

    final FlattenPointer res;
    res = subject.record(attr, elem);

    assertEquals(res.value(), 2);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addByte(HtmlBytes.STARTFLAT);
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(2);
          opts.addInt8(0);
          opts.addInt8(1);
        })
    );
  }

}
