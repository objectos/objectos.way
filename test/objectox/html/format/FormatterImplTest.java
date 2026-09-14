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
import objectos.html.stream.Event;
import objectos.script.Js;
import objectos.script.JsAction;
import objectox.html.attr.AttributeName;
import objectox.html.stream.RenderJsActionAttributeEvent;
import objectox.html.stream.RenderRawEvent;
import org.testng.annotations.Test;

public class FormatterImplTest {

  @Test(description = "RenderJsActionAttribute events are formatted via the default options")
  public void renderJsActionAttribute01() {
    final JsAction action;
    action = Js.noop();

    final Event event;
    event = new RenderJsActionAttributeEvent(AttributeName.ONCLICK, action);

    final FormatterImpl subject;
    subject = (FormatterImpl) Formatter.standard();

    final Output out;
    out = OutputY.create();

    subject.formatTo(event, out);

    assertEquals(
        out.toString(),

        " onclick='way.on(event,[\"NO\"])'"
    );
  }

  @Test(description = "RenderRaw events are formatted via the default options")
  public void renderRaw01() {
    final Event event;
    event = new RenderRawEvent("<br/>");

    final FormatterImpl subject;
    subject = (FormatterImpl) Formatter.standard();

    final Output out;
    out = OutputY.create();

    subject.formatTo(event, out);

    assertEquals(
        out.toString(),

        "<br/>\n"
    );
  }

}
