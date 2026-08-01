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
import objectox.html.stream.BeginDocumentEvent;
import org.testng.annotations.Test;

public class BeginDocumentFormatterTest {

  private String format(BeginDocumentFormatter subject) {
    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    final Output out;
    out = OutputY.create();

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "empty comment emits nothing")
  public void comment01() {
    final String comment;
    comment = "";

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, newLine, newLineStyle);

    assertEquals(
        format(subject),

        ""
    );
  }

  @Test(description = "non-null comment emits comment without newline")
  public void comment02() {
    final String comment;
    comment = "BEGIN Objectos HTML";

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, newLine, newLineStyle);

    assertEquals(
        format(subject),

        "<!-- BEGIN Objectos HTML -->"
    );
  }

  @Test(description = "always with empty comment emits newline")
  public void newLineStyle01() {
    final String comment;
    comment = "";

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.always();

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, newLine, newLineStyle);

    assertEquals(
        format(subject),

        "\n"
    );
  }

  @Test(description = "always with non-empty comment emits comment and newline")
  public void newLineStyle02() {
    final String comment;
    comment = "Test";

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.always();

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, newLine, newLineStyle);

    assertEquals(
        format(subject),

        """
        <!-- Test -->
        """
    );
  }

  @Test(description = "ifNotEmpty with empty comment emits nothing")
  public void newLineStyle03() {
    final String comment;
    comment = "";

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.ifNotEmpty();

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, newLine, newLineStyle);

    assertEquals(
        format(subject),

        ""
    );
  }

  @Test(description = "ifNotEmpty with non-empty comment emits comment and newline")
  public void newLineStyle04() {
    final String comment;
    comment = "Test";

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.ifNotEmpty();

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, newLine, newLineStyle);

    assertEquals(
        format(subject),

        """
        <!-- Test -->
        """
    );
  }

  @Test(description = "never with empty comment emits nothing")
  public void newLineStyle05() {
    final String comment;
    comment = "";

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, newLine, newLineStyle);

    assertEquals(
        format(subject),

        ""
    );
  }

  @Test(description = "never with non-empty comment emits comment without newline")
  public void newLineStyle06() {
    final String comment;
    comment = "Test";

    final NewLine newLine;
    newLine = NewLine.LF;

    final NewLineStyle newLineStyle;
    newLineStyle = NewLineStyle.never();

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, newLine, newLineStyle);

    assertEquals(
        format(subject),

        "<!-- Test -->"
    );
  }

}
