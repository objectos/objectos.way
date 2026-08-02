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

import objectos.html.format.IndentationStyle;
import objectos.html.format.NewLineStyle;
import org.testng.annotations.Test;

public class RenderEndTagFormatterBuilderTest {

  @Test(
      description = "reject null indentationStyle",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null"
  )
  public void indentationStyle01() {
    final RenderEndTagFormatterBuilder subject;
    subject = new RenderEndTagFormatterBuilder();

    subject.indentationStyle(null);
  }

  @Test(description = "default indentationStyle is START_OF_LINE")
  public void indentationStyle02() {
    final RenderEndTagFormatterBuilder subject;
    subject = new RenderEndTagFormatterBuilder();

    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderEndTagFormatter res;
    res = subject.build(indentation, NewLine.LF);

    assertEquals(res.indentationStyle, IndentationOption.START_OF_LINE);
  }

  @Test(description = "accept the specified value")
  public void indentationStyle03() {
    final RenderEndTagFormatterBuilder subject;
    subject = new RenderEndTagFormatterBuilder();

    subject.indentationStyle(IndentationStyle.never());

    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderEndTagFormatter res;
    res = subject.build(indentation, NewLine.LF);

    assertEquals(res.indentationStyle, IndentationOption.NEVER);
  }

  @Test(
      description = "reject null newLineStyle",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "value == null"
  )
  public void newLineStyle01() {
    final RenderEndTagFormatterBuilder subject;
    subject = new RenderEndTagFormatterBuilder();

    subject.newLineStyle(null);
  }

  @Test(description = "default newLineStyle is IF_NOT_EMPTY")
  public void newLineStyle02() {
    final RenderEndTagFormatterBuilder subject;
    subject = new RenderEndTagFormatterBuilder();

    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderEndTagFormatter res;
    res = subject.build(indentation, NewLine.LF);

    assertEquals(res.newLineStyle, NewLineOption.IF_NOT_EMPTY);
  }

  @Test(description = "accept the specified value")
  public void newLineStyle03() {
    final RenderEndTagFormatterBuilder subject;
    subject = new RenderEndTagFormatterBuilder();

    subject.newLineStyle(NewLineStyle.always());

    final Indentation indentation;
    indentation = new Indentation("  ");

    final RenderEndTagFormatter res;
    res = subject.build(indentation, NewLine.LF);

    assertEquals(res.newLineStyle, NewLineOption.ALWAYS);
  }

}
