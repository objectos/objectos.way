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
package objectox.html;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import objectos.html.Html;
import objectos.html.Markup;
import objectos.html.markup.ElementMarkup;
import org.testng.annotations.Test;

public class HtmlReaderTest {

  @Test(description = """
  read(char[], int, int) must not return more characters than the length of
  the target buffer
  """)
  public void read01() throws IOException {
    readImpl(1024, 64);
  }

  @Test(description = """
  read(char[], int, int) with a target buffer much smaller than the formatted
  output
  """)
  public void read02() throws IOException {
    readImpl(256, 8);
  }

  private void readImpl(int count, int bufferSize) throws IOException {
    final Markup markup;
    markup = MarkupImpl.create();

    final ElementMarkup[] items;
    items = new ElementMarkup[count];

    for (int i = 0; i < count; i++) {
      items[i] = markup.li(Integer.toString(i));
    }

    markup.ul(items);

    final Html html;
    html = markup.compile();

    final StringBuilder expected;
    expected = new StringBuilder();

    expected.append("<ul>\n");

    for (int i = 0; i < count; i++) {
      expected.append("  <li>\n    ");
      expected.append(i);
      expected.append("\n  </li>\n");
    }

    expected.append("</ul>\n");

    final Reader reader;
    reader = html.openStream();

    final StringBuilder result;
    result = new StringBuilder();

    final char[] cbuf;
    cbuf = new char[bufferSize];

    int nread;

    while ((nread = reader.read(cbuf, 0, cbuf.length)) != -1) {
      assertTrue(nread > 0, "read() returned " + nread);

      assertTrue(nread <= cbuf.length, "read() returned " + nread);

      result.append(cbuf, 0, nread);
    }

    reader.close();

    assertEquals(result.toString(), expected.toString());
  }

}
