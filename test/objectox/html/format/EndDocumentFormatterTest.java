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
import objectox.html.play.EndDocumentEvent;
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

  @Test
  public void comment01() {
    final String comment;
    comment = null;

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment);

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        ""
    );
  }

  @Test
  public void comment02() {
    final String comment;
    comment = "END Objectos HTML";

    final EndDocumentFormatter subject;
    subject = new EndDocumentFormatter(comment);

    final EndDocumentEvent event;
    event = EndDocumentEvent.INSTANCE;

    assertEquals(
        format(subject, event),

        """
        <!-- END Objectos HTML -->
        """
    );
  }

}
