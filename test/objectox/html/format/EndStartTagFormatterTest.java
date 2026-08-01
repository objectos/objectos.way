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

import objectos.html.format.NewLineStyle;
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

  @Test(description = "always with element kind emits newline and increments indentation")
  public void always01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.always());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.ELEMENT);

    assertEquals(
        format(subject, event),

        ">\n"
    );

    assertEquals(indentation.toString(), "  ");
  }

  @Test(description = "always with void element emits newline and does not change indentation")
  public void always02() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.always());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT);

    assertEquals(
        format(subject, event),

        ">\n"
    );

    assertEquals(indentation.toString(), "");
  }

  @Test(description = "always with empty element emits newline and increments indentation")
  public void always03() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.always());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT);

    assertEquals(
        format(subject, event),

        ">\n"
    );

    assertEquals(indentation.toString(), "  ");
  }

  @Test(description = "never with element kind emits no newline and increments indentation")
  public void never01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.never());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.ELEMENT);

    assertEquals(
        format(subject, event),

        ">"
    );

    assertEquals(indentation.toString(), "  ");
  }

  @Test(description = "never with void element emits no newline and does not change indentation")
  public void never02() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.never());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT);

    assertEquals(
        format(subject, event),

        ">"
    );

    assertEquals(indentation.toString(), "");
  }

  @Test(description = "never with empty element emits no newline and increments indentation")
  public void never03() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.never());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT);

    assertEquals(
        format(subject, event),

        ">"
    );

    assertEquals(indentation.toString(), "  ");
  }

  @Test(description = "ifNotEmpty with element kind emits newline and increments indentation")
  public void ifNotEmpty01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.ifNotEmpty());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.ELEMENT);

    assertEquals(
        format(subject, event),

        ">\n"
    );

    assertEquals(indentation.toString(), "  ");
  }

  @Test(description = "ifNotEmpty with void element emits newline and does not change indentation")
  public void ifNotEmpty02() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.ifNotEmpty());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.VOID_ELEMENT);

    assertEquals(
        format(subject, event),

        ">\n"
    );

    assertEquals(indentation.toString(), "");
  }

  @Test(description = "ifNotEmpty with empty element emits no newline and increments indentation")
  public void ifNotEmpty03() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(indentation, NewLine.LF, NewLineStyle.ifNotEmpty());

    final EndStartTag event;
    event = new EndStartTagEvent(EndStartTagKind.EMPTY_ELEMENT);

    assertEquals(
        format(subject, event),

        ">"
    );

    assertEquals(indentation.toString(), "  ");
  }

}
