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
import org.testng.annotations.Test;

public class NewLineTest {

  private String empty(NewLine subject, NewLineStyle style) {
    final char[] buf;
    buf = new char[256];

    final Output out;
    out = new Output(buf);

    subject.empty(style, out);

    return out.toString();
  }

  private String notEmpty(NewLine subject, NewLineStyle style) {
    final char[] buf;
    buf = new char[256];

    final Output out;
    out = new Output(buf);

    subject.notEmpty(style, out);

    return out.toString();
  }

  @Test(description = "ALWAYS emits newline")
  public void empty01() {
    final NewLine subject;
    subject = NewLine.LF;

    assertEquals(
        empty(subject, NewLineStyle.always()),

        "\n"
    );
  }

  @Test(description = "IF_NOT_EMPTY does not emit newline")
  public void empty02() {
    final NewLine subject;
    subject = NewLine.LF;

    assertEquals(
        empty(subject, NewLineStyle.ifNotEmpty()),

        ""
    );
  }

  @Test(description = "NEVER does not emit newline")
  public void empty03() {
    final NewLine subject;
    subject = NewLine.LF;

    assertEquals(
        empty(subject, NewLineStyle.never()),

        ""
    );
  }

  @Test(description = "ALWAYS emits newline")
  public void notEmpty01() {
    final NewLine subject;
    subject = NewLine.LF;

    assertEquals(
        notEmpty(subject, NewLineStyle.always()),

        "\n"
    );
  }

  @Test(description = "IF_NOT_EMPTY emits newline")
  public void notEmpty02() {
    final NewLine subject;
    subject = NewLine.LF;

    assertEquals(
        notEmpty(subject, NewLineStyle.ifNotEmpty()),

        "\n"
    );
  }

  @Test(description = "NEVER does not emit newline")
  public void notEmpty03() {
    final NewLine subject;
    subject = NewLine.LF;

    assertEquals(
        notEmpty(subject, NewLineStyle.never()),

        ""
    );
  }

}