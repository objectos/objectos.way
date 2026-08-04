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
import org.testng.annotations.Test;

public class RenderDoctypeFormatterBuilderTest {

  @Test(
      description = "reject null doctypeTextCaseStyle",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null"
  )
  public void doctypeTextCaseStyle01() {
    final RenderDoctypeFormatterBuilder subject;
    subject = new RenderDoctypeFormatterBuilder();

    subject.doctypeTextCaseStyle(null);
  }

  @Test(description = "default doctypeTextCaseStyle is UPPER_CASE")
  public void doctypeTextCaseStyle02() {
    final RenderDoctypeFormatterBuilder subject;
    subject = new RenderDoctypeFormatterBuilder();

    final RenderDoctypeFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.doctypeTextCaseStyle, TextCaseOption.UPPER_CASE);
  }

  @Test(description = "accept the specified value")
  public void doctypeTextCaseStyle03() {
    final RenderDoctypeFormatterBuilder subject;
    subject = new RenderDoctypeFormatterBuilder();

    subject.doctypeTextCaseStyle(TextCaseStyle.lowerCase());

    final RenderDoctypeFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.doctypeTextCaseStyle, TextCaseOption.LOWER_CASE);
  }

  @Test(
      description = "reject null newLineStyle",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null"
  )
  public void newLineStyle01() {
    final RenderDoctypeFormatterBuilder subject;
    subject = new RenderDoctypeFormatterBuilder();

    subject.newLineStyle(null);
  }

  @Test(description = "default newLineStyle is ALWAYS")
  public void newLineStyle02() {
    final RenderDoctypeFormatterBuilder subject;
    subject = new RenderDoctypeFormatterBuilder();

    final RenderDoctypeFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.newLineStyle, NewLineOption.ALWAYS);
  }

  @Test(description = "accept the specified value")
  public void newLineStyle03() {
    final RenderDoctypeFormatterBuilder subject;
    subject = new RenderDoctypeFormatterBuilder();

    subject.newLineStyle(NewLineStyle.never());

    final RenderDoctypeFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.newLineStyle, NewLineOption.NEVER);
  }

}
