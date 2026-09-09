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

import java.util.function.Consumer;
import objectos.html.format.IndentationStyle;
import objectos.html.stream.BeginStartTag;
import objectox.html.elem.ElementName;
import objectox.html.stream.BeginStartTagEvent;
import org.testng.annotations.Test;

public class BeginStartTagFormatterTest {

  private String format(BeginStartTagFormatter subject) {
    return format(subject, _ -> {});
  }

  private String format(BeginStartTagFormatter subject, Consumer<? super OutputY> opts) {
    final BeginStartTag event;
    event = new BeginStartTagEvent(ElementName.DIV);

    final Output out;
    out = OutputY.create(opts);

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "never indentation style emits tag without indentation")
  public void indentationStyle01() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.never();

    final BeginStartTagFormatter subject;
    subject = new BeginStartTagFormatter(indentation, indentationStyle);

    assertEquals(
        format(subject),

        "<div"
    );
  }

  @Test(description = "startOfLine indentation style emits tag with indentation at start of line")
  public void indentationStyle02() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.startOfLine();

    final BeginStartTagFormatter subject;
    subject = new BeginStartTagFormatter(indentation, indentationStyle);

    assertEquals(
        format(subject, y -> y.level = 1),

        "  <div"
    );
  }

  @Test(description = "startOfLine indentation style emits tag without indentation when not at start of line")
  public void indentationStyle03() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.startOfLine();

    final BeginStartTagFormatter subject;
    subject = new BeginStartTagFormatter(indentation, indentationStyle);

    assertEquals(
        format(subject, y -> {
          y.level = 1;

          y.text = "text";
        }),

        "text<div"
    );
  }

  @Test(description = "multiple indentation levels")
  public void indentationStyle04() {
    final Indentation indentation;
    indentation = new Indentation("  ");

    final IndentationStyle indentationStyle;
    indentationStyle = IndentationStyle.startOfLine();

    final BeginStartTagFormatter subject;
    subject = new BeginStartTagFormatter(indentation, indentationStyle);

    assertEquals(
        format(subject, y -> y.level = 2),

        "    <div"
    );
  }

}