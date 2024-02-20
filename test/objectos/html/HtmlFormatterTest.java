/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.html.pseudom.HtmlDocument;
import org.testng.annotations.Test;

public class HtmlFormatterTest {

  private final StringBuilder out = new StringBuilder();

  @Test
  public void writeAttributeValue() {
    // normal text
    writeAttributeValue("abc", "abc");

    // html tokens
    writeAttributeValue("if (a < b && c > d) {}", "if (a < b &amp;&amp; c > d) {}");

    // named entities
    writeAttributeValue("foo&nbsp;bar", "foo&nbsp;bar");
    writeAttributeValue("foo&nb#;bar", "foo&amp;nb#;bar");
    writeAttributeValue("foo&nbsp bar", "foo&amp;nbsp bar");

    // decimal entities
    writeAttributeValue("foo&#39;bar", "foo&#39;bar");
    writeAttributeValue("foo &# 39;", "foo &amp;# 39;");

    // hex entities
    writeAttributeValue("foo&#xa9;bar&Xa9;baz", "foo&#xa9;bar&Xa9;baz");
    writeAttributeValue("foo &#xxa9;", "foo &amp;#xxa9;");

    // ampersand edge cases
    writeAttributeValue("&", "&amp;");
    writeAttributeValue("int a = value & MASK;", "int a = value &amp; MASK;");

    // new lines should be left alone
    writeAttributeValue("foo\nbar", "foo\nbar");
  }

  private void writeAttributeValue(String source, String expected) {
    try {
      out.setLength(0);

      var writer = new HtmlFormatter() {
        public final void test(StringBuilder out, String text) throws IOException {
          for (int idx = 0, len = text.length(); idx < len;) {
            char c;
            c = text.charAt(idx++);

            switch (c) {
              case '&' -> idx = ampersand(out, text, idx, len);

              default -> out.append(c);
            }
          }
        }

        @Override
        protected void format(HtmlDocument document, Appendable out) throws IOException {
          // noop
        }
      };

      writer.test(out, source);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw", e);
    }
  }

}