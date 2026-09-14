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
import objectos.html.format.TextCaseStyle;
import objectos.html.stream.RenderDoctype;
import objectox.html.stream.RenderDoctypeEvent;
import org.testng.annotations.Test;

public class RenderDoctypeFormatterTest {

  private String format(RenderDoctypeFormatter subject) {
    final RenderDoctype event;
    event = RenderDoctypeEvent.of(0);

    final Output out;
    out = OutputY.create();

    subject.format(event, out);

    return out.toString();
  }

  @Test(description = "default text case and new line settings emit <!DOCTYPE html> followed by new line")
  public void format01() {
    final RenderDoctypeFormatter subject;
    subject = new RenderDoctypeFormatter(
        NewLine.LF,

        TextCaseStyle.upperCase(),

        NewLineStyle.always()
    );

    assertEquals(
        format(subject),

        "<!DOCTYPE html>\n"
    );
  }

  @Test(description = "lower case text case setting emits <!doctype html> followed by new line")
  public void format02() {
    final RenderDoctypeFormatter subject;
    subject = new RenderDoctypeFormatter(
        NewLine.LF,

        TextCaseStyle.lowerCase(),

        NewLineStyle.always()
    );

    assertEquals(
        format(subject),

        "<!doctype html>\n"
    );
  }

  @Test(description = "upper case text case and never new line settings emit <!DOCTYPE html> without new line")
  public void format03() {
    final RenderDoctypeFormatter subject;
    subject = new RenderDoctypeFormatter(
        NewLine.LF,

        TextCaseStyle.upperCase(),

        NewLineStyle.never()
    );

    assertEquals(
        format(subject),

        "<!DOCTYPE html>"
    );
  }

  @Test(description = "lower case text case and never new line settings emit <!doctype html> without new line")
  public void format04() {
    final RenderDoctypeFormatter subject;
    subject = new RenderDoctypeFormatter(
        NewLine.LF,

        TextCaseStyle.lowerCase(),

        NewLineStyle.never()
    );

    assertEquals(
        format(subject),

        "<!doctype html>"
    );
  }

}
