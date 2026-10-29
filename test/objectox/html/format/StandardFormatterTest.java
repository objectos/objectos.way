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

import objectos.html.stream.EndStartTagKind;
import objectos.html.stream.Event;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.stream.BeginStartTagEvent;
import objectox.html.stream.EndDocumentEvent;
import objectox.html.stream.EndStartTagEvent;
import objectox.html.stream.RenderAttributeEvent;
import objectox.html.stream.RenderBooleanAttributeEvent;
import objectox.html.stream.RenderDoctypeEvent;
import objectox.html.stream.RenderEndTagEvent;
import objectox.html.stream.RenderJsActionAttributeEvent;
import objectox.html.stream.RenderRawEvent;
import objectox.html.stream.RenderTextEvent;
import objectox.html.stream.TestableCellEvent;
import objectox.html.stream.TestableEntryNameEvent;
import objectox.html.stream.TestableEntryValueEvent;
import objectox.html.stream.TestableHeadingEvent;
import objectox.html.stream.TestableNewLineEvent;
import objectox.html.stream.TestablePropEvent;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StandardFormatterTest {

  @DataProvider
  public Object[][] formatToProvider() {
    final JsAction action;
    action = Js.noop();

    return new Object[][] {{
        new BeginStartTagEvent(0, ElementName.DIV),
        """
        <div\
        """
    }, {
        new EndDocumentEvent(false),
        "\n"
    }, {
        new EndDocumentEvent(true),
        ""
    }, {
        new EndStartTagEvent(0, EndStartTagKind.ELEMENT),
        """
        >\
        """
    }, {
        new RenderAttributeEvent(0, AttributeName.ID, "foo"),
        """
         id="foo"\
        """
    }, {
        new RenderAttributeEvent(0, AttributeName.ID, "a\"b&c<d>e"),
        """
         id="a&quot;b&amp;c&lt;d&gt;e"\
        """
    }, {
        new RenderBooleanAttributeEvent(0, AttributeName.AUTOFOCUS),
        """
         autofocus\
        """
    }, {
        new RenderDoctypeEvent(0),
        """
        <!DOCTYPE html>\
        """
    }, {
        new RenderEndTagEvent(0, ElementName.DIV),
        """
        </div>\
        """
    }, {
        new RenderJsActionAttributeEvent(0, AttributeName.ONCLICK, action),
        """
         onclick='way.on(event,["NO"])'\
        """
    }, {
        new RenderRawEvent(0, "<br/>"),
        """
        <br/>\
        """
    }, {
        new RenderTextEvent(0, "<xml></xml>&copy;"),
        """
        &lt;xml&gt;&lt;/xml&gt;&amp;copy;\
        """
    }, {
        new RenderTextEvent(0, "a\nb"),
        """
        a
        b\
        """
    }, {
        new TestableCellEvent(0, 10, "foo"),
        ""
    }, {
        new TestableEntryNameEvent(0, "foo"),
        ""
    }, {
        new TestableEntryValueEvent(0, "foo"),
        ""
    }, {
        new TestableHeadingEvent(0, 1, "foo"),
        ""
    }, {
        new TestableNewLineEvent(0),
        ""
    }, {
        new TestablePropEvent(0, "key", "value"),
        ""
    }};
  }

  @Test(dataProvider = "formatToProvider")
  public void formatTo(Event event, String expected) {
    final StandardFormatter subject;
    subject = new StandardFormatter();

    final Output out;
    out = OutputY.create();

    subject.formatTo(event, out);

    assertEquals(
        out.toString(),

        expected
    );
  }

}
