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

import objectos.html.format.IndentationStyle;
import objectos.html.stream.BeginStartTag;
import objectox.html.elem.ElementNamePojo;
import objectox.html.stream.BeginStartTagEvent;
import org.testng.annotations.Test;

public class BeginStartTagFormatterTest {

  private String format(BeginStartTagFormatter subject, BeginStartTag event) {
    final char[] buf;
    buf = new char[256];

    final Output out;
    out = Output.wrap(buf, 0, buf.length);

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "never indentation style emits tag without indentation")
  public void indentationStyle01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final BeginStartTagFormatter subject;
    subject = new BeginStartTagFormatter(indentation, IndentationStyle.never());

    final BeginStartTag event;
    event = new BeginStartTagEvent(ElementNamePojo.DIV);

    assertEquals(
        format(subject, event),

        "<div"
    );
  }

  @Test(description = "startOfLine indentation style emits tag with indentation at start of line")
  public void indentationStyle02() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    indentation.inc();

    final BeginStartTagFormatter subject;
    subject = new BeginStartTagFormatter(indentation, IndentationStyle.startOfLine());

    final BeginStartTag event;
    event = new BeginStartTagEvent(ElementNamePojo.DIV);

    assertEquals(
        format(subject, event),

        "  <div"
    );
  }

  @Test(description = "startOfLine indentation style emits tag without indentation when not at start of line")
  public void indentationStyle03() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    indentation.inc();

    final BeginStartTagFormatter subject;
    subject = new BeginStartTagFormatter(indentation, IndentationStyle.startOfLine());

    final char[] buf;
    buf = new char[256];

    final Output out;
    out = Output.wrap(buf, 0, buf.length);

    out.append("text");

    final BeginStartTag event;
    event = new BeginStartTagEvent(ElementNamePojo.DIV);

    subject.format(event, out);

    assertEquals(out.toString(), "text<div");
  }

  @Test(description = "multiple indentation levels")
  public void indentationStyle04() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    indentation.inc();
    indentation.inc();

    final BeginStartTagFormatter subject;
    subject = new BeginStartTagFormatter(indentation, IndentationStyle.startOfLine());

    final BeginStartTag event;
    event = new BeginStartTagEvent(ElementNamePojo.SPAN);

    assertEquals(
        format(subject, event),

        "    <span"
    );
  }

}