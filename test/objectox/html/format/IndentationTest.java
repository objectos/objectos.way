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
import objectos.html.format.IndentationStyle;
import org.testng.annotations.Test;

public class IndentationTest {

  @Test(description = "format with NEVER does not append anything")
  public void format01() throws IOException {
    final Indentation subject;
    subject = new Indentation("  ");

    final char[] buf;
    buf = new char[10];

    final Output out;
    out = new Output(buf);

    out.inc();
    out.inc();

    subject.format(IndentationStyle.never(), out);

    assertEquals(out.toString(), "");
  }

  @Test(description = "format with START_OF_LINE appends when output is empty")
  public void format02() throws IOException {
    final Indentation subject;
    subject = new Indentation("  ");

    final char[] buf;
    buf = new char[10];

    final Output out;
    out = new Output(buf);

    out.inc();
    out.inc();

    subject.format(IndentationStyle.startOfLine(), out);

    assertEquals(out.toString(), "    ");
  }

  @Test(description = "format with START_OF_LINE appends after newline")
  public void format03() throws IOException {
    final Indentation subject;
    subject = new Indentation("  ");

    final char[] buf;
    buf = new char[10];

    final Output out;
    out = new Output(buf);

    out.inc();

    out.append("hello\n");

    subject.format(IndentationStyle.startOfLine(), out);

    assertEquals(out.toString(), "hello\n  ");
  }

  @Test(description = "format with START_OF_LINE does not append when not at start of line")
  public void format04() throws IOException {
    final Indentation subject;
    subject = new Indentation("  ");

    final char[] buf;
    buf = new char[10];

    final Output out;
    out = new Output(buf);

    out.inc();

    out.append("hello");

    subject.format(IndentationStyle.startOfLine(), out);

    assertEquals(out.toString(), "hello");
  }

}
