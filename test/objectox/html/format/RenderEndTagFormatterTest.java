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
import java.util.function.Consumer;
import objectos.html.format.IndentationStyle;
import objectos.html.format.NewLineStyle;
import objectos.html.stream.RenderEndTag;
import objectox.html.elem.ElementName;
import objectox.html.stream.RenderEndTagEvent;
import org.testng.annotations.Test;

public class RenderEndTagFormatterTest {

  private Output format(RenderEndTagFormatter subject, Consumer<? super OutputY> opts) throws IOException {
    final RenderEndTag event;
    event = RenderEndTagEvent.of(0, ElementName.DIV);

    final Output out;
    out = OutputY.create(opts);

    subject.format(event, out);

    return out;
  }

  @Test(description = "never indentation style emits tag without indentation")
  public void indentationStyle01() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.never();

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final RenderEndTagFormatter subject;
    subject = new RenderEndTagFormatter(indentation, indentationStyle, newLine, newLineStyle);

    final Output out;
    out = format(subject, y -> y.level = 1);

    assertEquals(
        out.toString(),

        "</div>"
    );
  }

  @Test(description = "startOfLine indentation style emits tag with indentation at start of line")
  public void indentationStyle02() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.startOfLine();

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final RenderEndTagFormatter subject;
    subject = new RenderEndTagFormatter(indentation, indentationStyle, newLine, newLineStyle);

    final Output out;
    out = format(subject, y -> y.level = 2);

    assertEquals(
        out.toString(),

        "  </div>"
    );
  }

  @Test(description = "startOfLine indentation style emits tag without indentation when not at start of line")
  public void indentationStyle03() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.startOfLine();

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final RenderEndTagFormatter subject;
    subject = new RenderEndTagFormatter(indentation, indentationStyle, newLine, newLineStyle);

    final Output out;
    out = format(subject, y -> {
      y.level = 1;

      y.text = "text";
    });

    assertEquals(
        out.toString(),

        "text</div>"
    );
  }

  @Test(description = "multiple indentation levels")
  public void indentationStyle04() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.startOfLine();

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final RenderEndTagFormatter subject;
    subject = new RenderEndTagFormatter(indentation, indentationStyle, newLine, newLineStyle);

    final Output out;
    out = format(subject, y -> y.level = 3);

    assertEquals(
        out.toString(),

        "    </div>"
    );
  }

  @Test(description = "always emits newline and decrements indentation")
  public void newLineStyle01() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.never();

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.always();

    final RenderEndTagFormatter subject;
    subject = new RenderEndTagFormatter(indentation, indentationStyle, newLine, newLineStyle);

    final Output out;
    out = format(subject, y -> y.level = 1);

    assertEquals(
        out.toString(),

        "</div>\n"
    );

    assertEquals(out.level(), 0);
  }

  @Test(description = "ifNotEmpty emits newline and decrements indentation")
  public void newLineStyle02() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.never();

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.ifNotEmpty();

    final RenderEndTagFormatter subject;
    subject = new RenderEndTagFormatter(indentation, indentationStyle, newLine, newLineStyle);

    final Output out;
    out = format(subject, y -> y.level = 1);

    assertEquals(
        out.toString(),

        "</div>\n"
    );

    assertEquals(out.level(), 0);
  }

  @Test(description = "never emits no newline and decrements indentation")
  public void newLineStyle03() throws IOException {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.never();

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final RenderEndTagFormatter subject;
    subject = new RenderEndTagFormatter(indentation, indentationStyle, newLine, newLineStyle);

    final Output out;
    out = format(subject, y -> y.level = 1);

    assertEquals(
        out.toString(),

        "</div>"
    );

    assertEquals(out.level(), 0);
  }

}
