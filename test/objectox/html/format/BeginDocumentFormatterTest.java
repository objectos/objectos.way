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
import objectos.html.format.NewLineStyle;
import objectox.html.stream.BeginDocumentEvent;
import org.testng.annotations.Test;

public class BeginDocumentFormatterTest {

  private String format(BeginDocumentFormatter subject, BeginDocumentEvent event) {
    try {
      final StringBuilder out;
      out = new StringBuilder();

      subject.format(event, out);

      return out.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw", e);
    }
  }

  @Test(description = "null comment emits nothing")
  public void comment01() {
    final String comment;
    comment = null;

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, NewLine.LF, NewLineStyle.never());

    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        ""
    );
  }

  @Test(description = "non-null comment emits comment without newline")
  public void comment02() {
    final String comment;
    comment = "BEGIN Objectos HTML";

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, NewLine.LF, NewLineStyle.never());

    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        "<!-- BEGIN Objectos HTML -->"
    );
  }

  @Test(description = "always with null comment emits newline")
  public void newLineStyle01() {
    final String comment;
    comment = null;

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, NewLine.LF, NewLineStyle.always());

    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        "\n"
    );
  }

  @Test(description = "always with non-null comment emits comment and newline")
  public void newLineStyle02() {
    final String comment;
    comment = "Test";

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, NewLine.LF, NewLineStyle.always());

    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        """
        <!-- Test -->
        """
    );
  }

  @Test(description = "ifNotEmpty with null comment emits nothing")
  public void newLineStyle03() {
    final String comment;
    comment = null;

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, NewLine.LF, NewLineStyle.ifNotEmpty());

    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        ""
    );
  }

  @Test(description = "ifNotEmpty with non-null comment emits comment and newline")
  public void newLineStyle04() {
    final String comment;
    comment = "Test";

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, NewLine.LF, NewLineStyle.ifNotEmpty());

    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        """
        <!-- Test -->
        """
    );
  }

  @Test(description = "never with null comment emits nothing")
  public void newLineStyle05() {
    final String comment;
    comment = null;

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, NewLine.LF, NewLineStyle.never());

    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        ""
    );
  }

  @Test(description = "never with non-null comment emits comment without newline")
  public void newLineStyle06() {
    final String comment;
    comment = "Test";

    final BeginDocumentFormatter subject;
    subject = new BeginDocumentFormatter(comment, NewLine.LF, NewLineStyle.never());

    final BeginDocumentEvent event;
    event = BeginDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        "<!-- Test -->"
    );
  }

}
