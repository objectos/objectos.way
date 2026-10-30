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
import objectox.html.stream.EmitAttributeEvent;
import objectox.html.stream.EmitBooleanAttributeEvent;
import objectox.html.stream.EmitDoctypeEvent;
import objectox.html.stream.EmitEndTagEvent;
import objectox.html.stream.EmitJsActionAttributeEvent;
import objectox.html.stream.EmitRawEvent;
import objectox.html.stream.EmitTestableCellEvent;
import objectox.html.stream.EmitTestableHeadingEvent;
import objectox.html.stream.EmitTextEvent;
import objectox.html.stream.EndDocumentEvent;
import objectox.html.stream.EndStartTagEvent;
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
        new EmitAttributeEvent(0, AttributeName.ID, "foo"),
        """
         id="foo"\
        """
    }, {
        new EmitAttributeEvent(0, AttributeName.ID, "a\"b&c<d>e"),
        """
         id="a&quot;b&amp;c&lt;d&gt;e"\
        """
    }, {
        new EmitBooleanAttributeEvent(0, AttributeName.AUTOFOCUS),
        """
         autofocus\
        """
    }, {
        new EmitDoctypeEvent(0),
        """
        <!DOCTYPE html>\
        """
    }, {
        new EmitEndTagEvent(0, ElementName.DIV),
        """
        </div>\
        """
    }, {
        new EmitJsActionAttributeEvent(0, AttributeName.ONCLICK, action),
        """
         onclick='way.on(event,["NO"])'\
        """
    }, {
        new EmitRawEvent(0, "<br/>"),
        """
        <br/>\
        """
    }, {
        new EmitTextEvent(0, "<xml></xml>&copy;"),
        """
        &lt;xml&gt;&lt;/xml&gt;&amp;copy;\
        """
    }, {
        new EmitTextEvent(0, "a\nb"),
        """
        a
        b\
        """
    }, {
        new EmitTestableCellEvent(0, 10, "foo"),
        ""
    }, {
        new EmitTestableHeadingEvent(0, 1, "foo"),
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
