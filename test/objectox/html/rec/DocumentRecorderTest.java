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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import objectos.html.ElementName;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.elem.ElementNamePojo;
import org.testng.annotations.Test;

public class DocumentRecorderTest {

  private DocumentRecorder create(HtmlSink sink) {
    return DocumentRecorder.of(sink);
  }

  @SuppressWarnings("unused")
  @Test(description = "empty document")
  public void record00() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final DocumentRecorder subject;
    subject = create(sink);

    assertEquals(sink, HtmlSinkY.create(_ -> {}));
  }

  @Test(description = """
  <html></html>
  """)
  public void record01() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final DocumentRecorder subject;
    subject = create(sink);

    final ElementNamePojo name;
    name = ElementNamePojo.HTML;

    subject.element(name);

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(name.index());
          opts.addByte(HtmlBytes.VALUES0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(name.index());
        })
    );
  }

  @Test(description = """
  <html autofocus></html>
  """)
  public void record02() {
    final HtmlSink sink;
    sink = new HtmlSink();

    final DocumentRecorder subject;
    subject = create(sink);

    subject.element(
        ElementName.HTML,
        subject.attribute(AttributeNamePojo.AUTOFOCUS)
    );

    assertEquals(
        sink,

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.XBOOLEAN8);
          opts.addInt8(AttributeNamePojo.AUTOFOCUS.index());
          opts.addByte(HtmlBytes.STARTTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
          opts.addByte(HtmlBytes.VALUES8);
          opts.addInt8(1);
          opts.addInt8(0);
          opts.addByte(HtmlBytes.ENDTAG8);
          opts.addInt8(ElementNamePojo.HTML.index());
        })
    );
  }

}
