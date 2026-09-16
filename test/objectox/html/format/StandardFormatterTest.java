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
    final Event event;
    event = new BeginStartTagEvent(0, ElementName.DIV);

    assertEquals(
        format(event),

        """
        <div\
        """
    );
  }

  @Test(description = "EndStartTag events emit only the end of the start tag")
  public void endStartTag01() throws IOException {
    final Event event;
    event = new EndStartTagEvent(0, EndStartTagKind.ELEMENT);

    assertEquals(
        format(event),

        """
        >\
        """
    );
  }

  @Test(description = "RenderAttribute events are formatted with double quotation marks")
  public void renderAttribute01() throws IOException {
    final Event event;
    event = new RenderAttributeEvent(0, AttributeName.ID, "foo");

    assertEquals(
        format(event),

        """
         id="foo"\
        """
    );
  }

  @Test(description = "RenderAttribute values are escaped")
  public void renderAttribute02() throws IOException {
    final Event event;
    event = new RenderAttributeEvent(0, AttributeName.ID, "a\"b&c<d>e");

    assertEquals(
        format(event),

        """
         id="a&quot;b&amp;c&lt;d&gt;e"\
        """
    );
  }

  @Test(description = "RenderBooleanAttribute events are formatted without a value")
  public void renderBooleanAttribute01() throws IOException {
    final Event event;
    event = new RenderBooleanAttributeEvent(0, AttributeName.AUTOFOCUS);

    assertEquals(
        format(event),

        """
         autofocus\
        """
    );
  }

  @Test(description = "RenderDoctype events emit the upper case doctype")
  public void renderDoctype01() throws IOException {
    final Event event;
    event = new RenderDoctypeEvent(0);

    assertEquals(
        format(event),

        """
        <!DOCTYPE html>\
        """
    );
  }

  @Test(description = "RenderEndTag events are formatted in a single line")
  public void renderEndTag01() throws IOException {
    final Event event;
    event = new RenderEndTagEvent(0, ElementName.DIV);

    assertEquals(
        format(event),

        """
        </div>\
        """
    );
  }

  @Test(description = "RenderJsActionAttribute events are formatted with single quotation marks")
  public void renderJsActionAttribute01() throws IOException {
    final JsAction action;
    action = Js.noop();

    final Event event;
    event = new RenderJsActionAttributeEvent(0, AttributeName.ONCLICK, action);

    assertEquals(
        format(event),

        """
         onclick='way.on(event,["NO"])'\
        """
    );
  }

  @Test(description = "RenderRaw events are formatted as is")
  public void renderRaw01() throws IOException {
    final Event event;
    event = new RenderRawEvent(0, "<br/>");

    assertEquals(
        format(event),

        """
        <br/>\
        """
    );
  }

  @Test(description = "RenderText events are escaped")
  public void renderText01() throws IOException {
    final Event event;
    event = new RenderTextEvent(0, "<xml></xml>&copy;");

    assertEquals(
        format(event),

        """
        &lt;xml&gt;&lt;/xml&gt;&amp;copy;\
        """
    );
  }

  @Test(description = "RenderText events preserve new line characters")
  public void renderText02() throws IOException {
    final Event event;
    event = new RenderTextEvent(0, "a\nb");

    assertEquals(
        format(event),

        """
        a
        b\
        """
    );
  }

  private String format(Event event) throws IOException {
    final StandardFormatter subject;
    subject = new StandardFormatter();

    final Output out;
    out = OutputY.create();

    subject.formatTo(event, out);

    return out.toString();
  }

}
