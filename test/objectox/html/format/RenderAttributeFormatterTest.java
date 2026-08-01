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

import objectos.html.stream.RenderAttribute;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.stream.RenderAttributeEvent;
import org.testng.annotations.Test;

public class RenderAttributeFormatterTest {

  private String format(RenderAttributeFormatter subject, RenderAttribute event) {
    final char[] buf;
    buf = new char[256];

    final Output out;
    out = Output.wrap(buf, 0, buf.length);

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "formats attribute name and value")
  public void format01() {
    final RenderAttributeFormatter subject;
    subject = new RenderAttributeFormatter();

    final RenderAttribute event;
    event = new RenderAttributeEvent(AttributeNamePojo.LANG, "pt-BR");

    assertEquals(
        format(subject, event),

        " lang=\"pt-BR\""
    );
  }

}
