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

import objectos.html.ElementName;
import objectos.html.markup.ElementValue;
import objectox.html.AmbiguousName;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class ElementChildrenRecorderTest {

  @Test(description = "ambiguous name that resolves to a child element")
  public void ambiguous01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final String text;
    text = "foo";

    final AmbiguousPojo value;
    value = AmbiguousPojo.of0(AmbiguousName.TITLE, text);

    assertEquals(
        record(sink, ElementNamePojo.HEAD, value),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELTXT88);
          opts.addInt8(ElementNamePojo.TITLE.index());
          opts.addInt8(opts.addObject(text));
        })
    );
  }

  @Test(description = "ambiguous name that resolves to an attribute")
  public void ambiguous02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final AmbiguousPojo value;
    value = AmbiguousPojo.of0(AmbiguousName.TITLE, "foo");

    assertEquals(
        record(sink, ElementName.HTML, value),

        HtmlSinkY.create(_ -> {})
    );
  }

  @Test
  public void element01() {
    final HtmlSink sink;
    sink = HtmlSinkY.create(opts -> {
      opts.addByte(HtmlBytes.STARTTAG8);
      opts.addInt8(ElementNamePojo.HEAD.index());
      opts.addByte(HtmlBytes.JMP24);
      opts.addInt24(8);
      opts.addByte(HtmlBytes.ENDTAG8);
      opts.addInt8(ElementNamePojo.HEAD.index());
    });

    final int childStart;
    childStart = 0;

    final ElementPointer child;
    child = new ElementPointer(childStart);

    assertEquals(
        record(sink, ElementName.HTML, child),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.xSTARTTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.JMP24);
          opts.addInt24(8);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HEAD.index());
          opts.addByte(HtmlBytes.ELEM8);
          opts.addInt8(childStart);
        })
    );
  }

  @Test(description = "raw node child")
  public void raw01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final String value;
    value = "Ipsum";

    final RawNode raw;
    raw = new RawNode(value);

    assertEquals(
        record(sink, ElementNamePojo.HEAD, raw),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.RAW8);
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  @Test(description = "text node child")
  public void text01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final String value;
    value = "Ipsum";

    final TextNode text;
    text = new TextNode(value);

    assertEquals(
        record(sink, ElementNamePojo.HEAD, text),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.TEXT8);
          opts.addInt8(opts.addObject(value));
        })
    );
  }

  private HtmlSink record(HtmlSink sink, ElementName elementName, ElementValue... contents) {
    final ChildAmbiguousRecorder childAmbiguousRecorder;
    childAmbiguousRecorder = new ChildAmbiguousRecorder(sink);

    final ChildElementMarker childElementMarker;
    childElementMarker = new ChildElementMarker(sink);

    final ChildElementRecorder childElementRecorder;
    childElementRecorder = new ChildElementRecorder(sink);

    final ChildRawRecorder childRawRecorder;
    childRawRecorder = new ChildRawRecorder(sink);

    final ChildTextRecorder childTextRecorder;
    childTextRecorder = new ChildTextRecorder(sink);

    final ElementChildrenRecorder subject;
    subject = new ElementChildrenRecorder(childAmbiguousRecorder, childElementMarker, childElementRecorder, childRawRecorder, childTextRecorder);

    subject.record(elementName, contents);

    return sink;
  }

}
