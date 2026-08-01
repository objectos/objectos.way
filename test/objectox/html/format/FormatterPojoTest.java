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
package objectox.html.format;

import static org.testng.Assert.assertEquals;

import objectos.html.format.Formatter;
import objectos.html.stream.BeginDocument;
import objectos.html.stream.BeginStartTag;
import objectos.html.stream.EndDocument;
import objectos.html.stream.EndStartTag;
import objectos.html.stream.EndStartTagKind;
import objectos.html.stream.Event;
import objectos.html.stream.RenderAttribute;
import objectos.html.stream.RenderEndTag;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.elem.ElementNamePojo;
import objectox.html.stream.BeginDocumentEvent;
import objectox.html.stream.BeginStartTagEvent;
import objectox.html.stream.EndDocumentEvent;
import objectox.html.stream.EndStartTagEvent;
import objectox.html.stream.RenderAttributeEvent;
import objectox.html.stream.RenderEndTagEvent;
import org.testng.annotations.Test;

public class FormatterPojoTest {

  private String format(Event... events) {
    final Formatter formatter;
    formatter = Formatter.standard();

    final char[] buf;
    buf = new char[512];

    final Output out;
    out = Output.wrap(buf, 0, buf.length);

    for (Event event : events) {
      formatter.formatTo(event, out);
    }

    return out.toString();
  }

  @Test(description = """
  <html>
    <head></head>
    <body></body>
  </html>
  """)
  public void format01() {
    final BeginDocument beginDocument;
    beginDocument = BeginDocumentEvent.INSTANCE;

    final BeginStartTag beginHtml;
    beginHtml = BeginStartTagEvent.of(ElementNamePojo.HTML);

    final EndStartTag endHtml;
    endHtml = new EndStartTagEvent(EndStartTagKind.ELEMENT);

    final BeginStartTag beginHead;
    beginHead = BeginStartTagEvent.of(ElementNamePojo.HEAD);

    final EndStartTag endHead;
    endHead = new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT);

    final RenderEndTag renderHead;
    renderHead = RenderEndTagEvent.of(ElementNamePojo.HEAD);

    final BeginStartTag beginBody;
    beginBody = BeginStartTagEvent.of(ElementNamePojo.BODY);

    final EndStartTag endBody;
    endBody = new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT);

    final RenderEndTag renderBody;
    renderBody = RenderEndTagEvent.of(ElementNamePojo.BODY);

    final RenderEndTag renderHtml;
    renderHtml = RenderEndTagEvent.of(ElementNamePojo.HTML);

    final EndDocument endDocument;
    endDocument = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(
            beginDocument,
            beginHtml,
            endHtml,
            beginHead,
            endHead,
            renderHead,
            beginBody,
            endBody,
            renderBody,
            renderHtml,
            endDocument
        ),

        """
        <!-- BEGIN Objectos HTML -->
        <html>
          <head></head>
          <body></body>
        </html>
        """
    );
  }

  @Test(description = """
  <head>
    <meta charset="UTF-8">
  </head>
  """)
  public void format02() {
    final BeginDocument beginDocument;
    beginDocument = BeginDocumentEvent.INSTANCE;

    final BeginStartTag beginHead;
    beginHead = BeginStartTagEvent.of(ElementNamePojo.HEAD);

    final EndStartTag endHead;
    endHead = new EndStartTagEvent(EndStartTagKind.ELEMENT);

    final BeginStartTag beginMeta;
    beginMeta = BeginStartTagEvent.of(ElementNamePojo.META);

    final RenderAttribute charset;
    charset = RenderAttributeEvent.of(AttributeNamePojo.CHARSET, "UTF-8");

    final EndStartTag endMeta;
    endMeta = new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT);

    final RenderEndTag renderHead;
    renderHead = RenderEndTagEvent.of(ElementNamePojo.HEAD);

    final EndDocument endDocument;
    endDocument = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(
            beginDocument,
            beginHead,
            endHead,
            beginMeta,
            charset,
            endMeta,
            renderHead,
            endDocument
        ),

        """
        <!-- BEGIN Objectos HTML -->
        <head>
          <meta charset="UTF-8">
        </head>
        """
    );
  }

}
