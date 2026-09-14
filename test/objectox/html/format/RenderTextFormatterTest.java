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
import objectos.html.format.NewLineStyle;
import objectos.html.stream.RenderText;
import objectox.html.stream.RenderTextEvent;
import org.testng.annotations.Test;

public class RenderTextFormatterTest {

  private String format(RenderTextFormatter subject) {
    return format(subject, "ipsum", "");
  }

  private String format(RenderTextFormatter subject, String text) {
    return format(subject, "ipsum", text);
  }

  private String format(RenderTextFormatter subject, String value, String text) {
    final RenderText event;
    event = RenderTextEvent.of(0, value);

    final Output out;
    out = OutputY.create(opts -> {
      opts.level = 1;

      opts.text = text;
    });

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "startOfLine indentation + always new line emits indented text followed by new line")
  public void format01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.startOfLine(),

        NewLine.LF,

        NewLineStyle.always()
    );

    assertEquals(
        format(subject),

        "  ipsum\n"
    );
  }

  @Test(description = "startOfLine indentation + ifNotEmpty new line emits indented text followed by new line")
  public void format02() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.startOfLine(),

        NewLine.LF,

        NewLineStyle.ifNotEmpty()
    );

    assertEquals(
        format(subject),

        "  ipsum\n"
    );
  }

  @Test(description = "startOfLine indentation + never new line emits indented text without new line")
  public void format03() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.startOfLine(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject),

        "  ipsum"
    );
  }

  @Test(description = "startOfLine indentation does not emit indentation when not at start of line")
  public void format04() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.startOfLine(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject, "text"),

        "textipsum"
    );
  }

  @Test(description = "never indentation + always new line emits text followed by new line")
  public void format05() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.always()
    );

    assertEquals(
        format(subject),

        "ipsum\n"
    );
  }

  @Test(description = "never indentation + ifNotEmpty new line emits text followed by new line")
  public void format06() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.ifNotEmpty()
    );

    assertEquals(
        format(subject),

        "ipsum\n"
    );
  }

  @Test(description = "never indentation + never new line emits text without indentation or new line")
  public void format07() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject),

        "ipsum"
    );
  }

  @Test(description = "ampersand is escaped")
  public void escape01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject, "A&B", ""),

        "A&amp;B"
    );
  }

  @Test(description = "less-than sign is escaped")
  public void escape02() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject, "A<B", ""),

        "A&lt;B"
    );
  }

  @Test(description = "greater-than sign is escaped")
  public void escape03() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject, "A>C", ""),

        "A&gt;C"
    );
  }

  @Test(description = "all escapable characters are escaped")
  public void escape04() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject, "A&B<C>D", ""),

        "A&amp;B&lt;C&gt;D"
    );
  }

  @Test(description = "quotation marks are not escaped in text content")
  public void escape05() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject, "A\"B'C", ""),

        "A\"B'C"
    );
  }

  @Test(description = "already-escaped entity is escaped again")
  public void escape06() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderTextFormatter subject;
    subject = new RenderTextFormatter(
        indentation,

        IndentationStyle.never(),

        NewLine.LF,

        NewLineStyle.never()
    );

    assertEquals(
        format(subject, "&amp;", ""),

        "&amp;amp;"
    );
  }

}
