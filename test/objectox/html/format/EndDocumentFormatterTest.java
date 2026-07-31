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
import objectox.html.stream.EndDocumentEvent;
import org.testng.annotations.Test;

public class EndDocumentFormatterTest {

  private String format(EndDocumentFormatter subject, EndDocumentEvent event) {
    try {
      final StringBuilder out;
      out = new StringBuilder();

      subject.format(event, out);

      return out.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw", e);
    }
  }

  @Test(description = "empty comment emits nothing")
  public void comment01() {
    final String comment;
    comment = "";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment, NewLine.LF, NewLineStyle.never());

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        ""
    );
  }

  @Test(description = "non-empty comment emits comment without newline")
  public void comment02() {
    final String comment;
    comment = "END Objectos HTML";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment, NewLine.LF, NewLineStyle.never());

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        "<!-- END Objectos HTML -->"
    );
  }

  @Test(description = "always with empty comment emits newline")
  public void newLineStyle01() {
    final String comment;
    comment = "";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment, NewLine.LF, NewLineStyle.always());

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        "\n"
    );
  }

  @Test(description = "always with non-empty comment emits comment and newline")
  public void newLineStyle02() {
    final String comment;
    comment = "Test";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment, NewLine.LF, NewLineStyle.always());

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        """
        <!-- Test -->
        """
    );
  }

  @Test(description = "ifNotEmpty with empty comment emits nothing")
  public void newLineStyle03() {
    final String comment;
    comment = "";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment, NewLine.LF, NewLineStyle.ifNotEmpty());

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        ""
    );
  }

  @Test(description = "ifNotEmpty with non-empty comment emits comment and newline")
  public void newLineStyle04() {
    final String comment;
    comment = "Test";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment, NewLine.LF, NewLineStyle.ifNotEmpty());

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        """
        <!-- Test -->
        """
    );
  }

  @Test(description = "never with empty comment emits nothing")
  public void newLineStyle05() {
    final String comment;
    comment = "";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment, NewLine.LF, NewLineStyle.never());

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        ""
    );
  }

  @Test(description = "never with non-empty comment emits comment without newline")
  public void newLineStyle06() {
    final String comment;
    comment = "Test";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment, NewLine.LF, NewLineStyle.never());

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        "<!-- Test -->"
    );
  }

}
