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

import objectos.html.stream.RenderEndTag;
import objectox.html.elem.ElementNamePojo;
import objectox.html.stream.RenderEndTagEvent;
import org.testng.annotations.Test;

public class RenderEndTagFormatterTest {

  private String format(RenderEndTagFormatter subject, RenderEndTag event) {
    final char[] buf;
    buf = new char[256];

    final Output out;
    out = Output.wrap(buf, 0, buf.length);

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "decrements indentation and emits newline")
  public void format01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    indentation.inc();
    indentation.inc();

    final RenderEndTagFormatter subject;
    subject = new RenderEndTagFormatter(indentation, NewLine.LF);

    final RenderEndTag event;
    event = new RenderEndTagEvent(ElementNamePojo.HTML);

    assertEquals(
        format(subject, event),

        "</html>\n"
    );

    assertEquals(indentation.toString(), "  ");
  }

}
