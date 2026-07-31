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

public class BeginDocumentFormatterBuilderTest {

  @Test(
      description = "reject null comment",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null"
  )
  public void comment01() {
    final BeginDocumentFormatterBuilder subject;
    subject = new BeginDocumentFormatterBuilder();

    subject.comment(null);
  }

  @Test(description = "it should return the default value")
  public void comment02() {
    final BeginDocumentFormatterBuilder subject;
    subject = new BeginDocumentFormatterBuilder();

    final BeginDocumentFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.comment, "BEGIN Objectos HTML");
  }

  @Test(description = "It should accept the specified value")
  public void comment03() {
    final BeginDocumentFormatterBuilder subject;
    subject = new BeginDocumentFormatterBuilder();

    subject.comment("abc");

    final BeginDocumentFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.comment, "abc");
  }

  @Test(description = "escape special characters")
  public void comment04() {
    final BeginDocumentFormatterBuilder subject;
    subject = new BeginDocumentFormatterBuilder();

    subject.comment("-->");

    final BeginDocumentFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.comment, "--&gt;");
  }

  @Test(
      description = "reject null newLineStyle",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null"
  )
  public void newLineStyle01() {
    final BeginDocumentFormatterBuilder subject;
    subject = new BeginDocumentFormatterBuilder();

    subject.newLineStyle(null);
  }

  @Test(description = "default newLineStyle is IF_NOT_EMPTY")
  public void newLineStyle02() {
    final BeginDocumentFormatterBuilder subject;
    subject = new BeginDocumentFormatterBuilder();

    final BeginDocumentFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.newLineStyle, NewLineOption.IF_NOT_EMPTY);
  }

  @Test(description = "accept the specified value")
  public void newLineStyle03() {
    final BeginDocumentFormatterBuilder subject;
    subject = new BeginDocumentFormatterBuilder();

    subject.newLineStyle(NewLineStyle.always());

    final BeginDocumentFormatter res;
    res = subject.build(NewLine.LF);

    assertEquals(res.newLineStyle, NewLineOption.ALWAYS);
  }
}
