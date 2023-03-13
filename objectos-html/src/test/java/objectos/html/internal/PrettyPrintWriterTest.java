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
package objectos.html.internal;

import static org.testng.Assert.assertEquals;

import objectos.html.HtmlSink;
import objectos.html.HtmlTemplate;
import org.testng.annotations.Test;

public class PrettyPrintWriterTest {

  private final HtmlSink sink = new HtmlSink();

  private final StringBuilder stringBuilder = new StringBuilder();

  @Test(description = """
  PrettyPrintWriter TC01

  - empty element
  """)
  public void testCase01() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html();
        }
      },

      """
      <html></html>
      """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC02

  - doctype
  """)
  public void testCase02() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          doctype();
          html();
        }
      },

      """
      <!doctype html>
      <html></html>
      """
    );
  }

  @Test(enabled = false, description = """
  PrettyPrintWriter TC03

  - head/body (indentation)
  """)
  public void testCase03() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          doctype();
          html(
            head(),
            body()
          );
        }
      },

      """
      <!doctype html>
      <html>
      <head></head>
      <body></body>
      </html>
      """
    );
  }

  private void test(HtmlTemplate template, String expected) {
    stringBuilder.setLength(0);

    sink.toStringBuilder(template, stringBuilder);

    assertEquals(stringBuilder.toString(), expected);
  }

}