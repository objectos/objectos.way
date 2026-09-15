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
import objectos.html.format.IndentationStyle;
import objectos.html.format.NewLineStyle;
import objectos.html.stream.RenderRaw;
import objectox.html.stream.RenderRawEvent;
import org.testng.annotations.Test;

public class RenderRawFormatterTest {

  private String format(RenderRawFormatter subject) throws IOException {
    return format(subject, "");
  }

  private String format(RenderRawFormatter subject, String text) throws IOException {
    final RenderRaw event;
    event = RenderRawEvent.of(0, "ipsum");

    final Output out;
    out = OutputY.create(opts -> {
      opts.level = 1;

      opts.text = text;
    });

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "startOfLine indentation + always new line emits indented raw text followed by new line")
  public void format01() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderRawFormatter subject;
    subject = new RenderRawFormatter(
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

  @Test(description = "startOfLine indentation + ifNotEmpty new line emits indented raw text followed by new line")
  public void format02() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderRawFormatter subject;
    subject = new RenderRawFormatter(
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

  @Test(description = "startOfLine indentation + never new line emits indented raw text without new line")
  public void format03() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderRawFormatter subject;
    subject = new RenderRawFormatter(
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
  public void format04() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderRawFormatter subject;
    subject = new RenderRawFormatter(
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

  @Test(description = "never indentation + always new line emits raw text followed by new line")
  public void format05() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderRawFormatter subject;
    subject = new RenderRawFormatter(
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

  @Test(description = "never indentation + ifNotEmpty new line emits raw text followed by new line")
  public void format06() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderRawFormatter subject;
    subject = new RenderRawFormatter(
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

  @Test(description = "never indentation + never new line emits raw text without indentation or new line")
  public void format07() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderRawFormatter subject;
    subject = new RenderRawFormatter(
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

  @Test(description = "raw value is emitted verbatim, i.e., it is not escaped")
  public void format08() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderRawFormatter subject;
    subject = new RenderRawFormatter(
        indentation,

        IndentationStyle.startOfLine(),

        NewLine.LF,

        NewLineStyle.always()
    );

    final RenderRaw event;
    event = RenderRawEvent.of(0, "<br/> & <script>");

    final Output out;
    out = OutputY.create(y -> y.level = 1);

    subject.format(event, out);

    assertEquals(
        out.toString(),

        "  <br/> & <script>\n"
    );
  }

}
