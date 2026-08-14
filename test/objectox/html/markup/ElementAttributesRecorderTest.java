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
import objectox.html.attr.AttributeName;
import org.testng.annotations.Test;

public class ElementAttributesRecorderTest {

  @Test(description = "AmbiguousAttributeRecorder delegation")
  public void ambiguous01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addByte(HtmlBytes.AMBIGUOUS88);
      opts.addInt8(AmbiguousName.TITLE.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addInt8(99);
    });

    final ElementAttributesRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final AmbiguousPointer pointer;
    pointer = new AmbiguousPointer(0);

    final int res;
    res = subject.record(elementName, size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.aAMBIGUOUS88);
          opts.addInt8(AmbiguousName.TITLE.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(99);
          opts.addInt8(0);
        })
    );
  }

  @Test(description = "ValueRecorder delegation")
  public void attribute01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addByte(HtmlBytes.ATTRIBUTE88);
      opts.addInt8(AttributeName.ID.index());
      opts.addInt8(opts.addObject("foo"));
      opts.addInt8(99);
    });

    final ElementAttributesRecorder subject;
    subject = create(sink);

    final ElementName elementName;
    elementName = ElementName.DIV;

    final IntSize size;
    size = IntSize.BIT8;

    final AttributePointer pointer;
    pointer = new AttributePointer(0);

    final int res;
    res = subject.record(elementName, size, pointer);

    assertEquals(res, 1);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xATTRIBUTE88);
          opts.addInt8(AttributeName.ID.index());
          opts.addInt8(opts.addObject("foo"));
          opts.addInt8(99);
          opts.addInt8(0);
        })
    );
  }

  private ElementAttributesRecorder create(HtmlSink sink) {
    final AmbiguousAttributeRecorder ambiguousAttributeRecorder;
    ambiguousAttributeRecorder = new AmbiguousAttributeRecorder(sink);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder fragmentAttributeRecorder;
    fragmentAttributeRecorder = new FragmentAttributeRecorder(sink, ambiguousAttributeRecorder, valueRecorder);

    return new ElementAttributesRecorder(ambiguousAttributeRecorder, fragmentAttributeRecorder, valueRecorder);
  }

}
