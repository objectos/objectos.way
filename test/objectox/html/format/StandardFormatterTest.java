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

import java.io.IOException;
import objectos.html.stream.EndStartTagKind;
import objectos.html.stream.Event;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;
import objectox.html.stream.BeginStartTagEvent;
import objectox.html.stream.EndStartTagEvent;
import objectox.html.stream.RenderAttributeEvent;
import objectox.html.stream.RenderBooleanAttributeEvent;
import objectox.html.stream.RenderDoctypeEvent;
import objectox.html.stream.RenderEndTagEvent;
import objectox.html.stream.RenderJsActionAttributeEvent;
import objectox.html.stream.RenderRawEvent;
import objectox.html.stream.RenderTextEvent;
import org.testng.annotations.Test;

public class StandardFormatterTest {

  @Test(description = "BeginStartTag events are formatted in a single line")
  public void beginStartTag01() throws IOException {
    test(
        new BeginStartTagEvent(0, ElementName.DIV),

        """
        <div\
        """
    );
  }

  @Test(description = "EndStartTag events emit only the end of the start tag")
  public void endStartTag01() throws IOException {
    test(
        new EndStartTagEvent(0, EndStartTagKind.ELEMENT),

        """
        >\
        """
    );
  }

  @Test(description = "RenderAttribute events are formatted with double quotation marks")
  public void renderAttribute01() throws IOException {
    test(
        new RenderAttributeEvent(0, AttributeName.ID, "foo"),

        """
         id="foo"\
        """
    );
  }

  @Test(description = "RenderAttribute values are escaped")
  public void renderAttribute02() throws IOException {
    test(
        new RenderAttributeEvent(0, AttributeName.ID, "a\"b&c<d>e"),

        """
         id="a&quot;b&amp;c&lt;d&gt;e"\
        """
    );
  }

  @Test(description = "RenderBooleanAttribute events are formatted without a value")
  public void renderBooleanAttribute01() throws IOException {
    test(
        new RenderBooleanAttributeEvent(0, AttributeName.AUTOFOCUS),

        """
         autofocus\
        """
    );
  }

  @Test(description = "RenderDoctype events emit the upper case doctype")
  public void renderDoctype01() throws IOException {
    test(
        new RenderDoctypeEvent(0),

        """
        <!DOCTYPE html>\
        """
    );
  }

  @Test(description = "RenderEndTag events are formatted in a single line")
  public void renderEndTag01() throws IOException {
    test(
        new RenderEndTagEvent(0, ElementName.DIV),

        """
        </div>\
        """
    );
  }

  @Test(description = "RenderJsActionAttribute events are formatted with single quotation marks")
  public void renderJsActionAttribute01() throws IOException {
    final JsAction action;
    action = Js.noop();

    test(
        new RenderJsActionAttributeEvent(0, AttributeName.ONCLICK, action),

        """
         onclick='way.on(event,["NO"])'\
        """
    );
  }

  @Test(description = "RenderRaw events are formatted as is")
  public void renderRaw01() throws IOException {
    test(
        new RenderRawEvent(0, "<br/>"),

        """
        <br/>\
        """
    );
  }

  @Test(description = "RenderText events are escaped")
  public void renderText01() throws IOException {
    test(
        new RenderTextEvent(0, "<xml></xml>&copy;"),

        """
        &lt;xml&gt;&lt;/xml&gt;&amp;copy;\
        """
    );
  }

  @Test(description = "RenderText events preserve new line characters")
  public void renderText02() throws IOException {
    test(
        new RenderTextEvent(0, "a\nb"),

        """
        a
        b\
        """
    );
  }

  private void test(Event event, String expected) throws IOException {
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
