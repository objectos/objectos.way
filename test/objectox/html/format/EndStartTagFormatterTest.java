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

import objectos.html.stream.EndStartTag;
import objectos.html.stream.EndStartTagKind;
import objectox.html.stream.EndStartTagEvent;
import org.testng.annotations.Test;

public class EndStartTagFormatterTest {

  private String format(EndStartTagFormatter subject, EndStartTag event) {
    final char[] buf;
    buf = new char[256];

    final Output out;
    out = Output.wrap(buf, 0, buf.length);

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "void element emits newline and does not change indentation")
  public void voidElement01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF);

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT);

    assertEquals(
        format(subject, event),

        ">\n"
    );

    assertEquals(indentation.toString(), "");
  }

  @Test(description = "empty element does not emit newline and increments indentation")
  public void emptyElement01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF);

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT);

    assertEquals(
        format(subject, event),

        ">"
    );

    assertEquals(indentation.toString(), "  ");
  }

  @Test(description = "element with children emits newline and increments indentation")
  public void elementChild01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF);

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.ELEMENT);

    assertEquals(
        format(subject, event),

        ">\n"
    );

    assertEquals(indentation.toString(), "  ");
  }

}
