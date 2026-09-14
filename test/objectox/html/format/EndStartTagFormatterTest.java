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

  private Output format(EndStartTagFormatter subject, EndStartTag event) {
    final Output out;
    out = OutputY.create();

    subject.format(event, out);

    return out;
  }

  @Test(description = "always with element kind emits newline and increments indentation")
  public void always01() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.always();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">\n"
    );

    assertEquals(out.level(), 1);
  }

  @Test(description = "always with void element emits newline and does not change indentation")
  public void always02() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.always();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">\n"
    );

    assertEquals(out.level(), 0);
  }

  @Test(description = "always with empty element emits newline and increments indentation")
  public void always03() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.always();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">\n"
    );

    assertEquals(out.level(), 1);
  }

  @Test(description = "never with element kind emits no newline and increments indentation")
  public void never01() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">"
    );

    assertEquals(out.level(), 1);
  }

  @Test(description = "never with void element emits no newline and does not change indentation")
  public void never02() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">"
    );

    assertEquals(out.level(), 0);
  }

  @Test(description = "never with empty element emits no newline and increments indentation")
  public void never03() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">"
    );

    assertEquals(out.level(), 1);
  }

  @Test(description = "ifNotEmpty with element kind emits newline and increments indentation")
  public void ifNotEmpty01() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.ifNotEmpty();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">\n"
    );

    assertEquals(out.level(), 1);
  }

  @Test(description = "ifNotEmpty with void element emits newline and does not change indentation")
  public void ifNotEmpty02() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.ifNotEmpty();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.VOID_ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">\n"
    );

    assertEquals(out.level(), 0);
  }

  @Test(description = "ifNotEmpty with empty element emits no newline and increments indentation")
  public void ifNotEmpty03() {
    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.ifNotEmpty();

    final EndStartTagFormatter subject;
    subject = new EndStartTagFormatter(newLine, newLineStyle);

    final EndStartTag event;
    event = new EndStartTagEvent(0, EndStartTagKind.EMPTY_ELEMENT);

    final Output out;
    out = format(subject, event);

    assertEquals(
        out.toString(),

        ">"
    );

    assertEquals(out.level(), 1);
  }

}
