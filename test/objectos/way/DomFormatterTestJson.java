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
package objectos.way;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class DomFormatterTestJson {

  @Test(description = """
  Empty element
  """)
  public void testCase01() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            html();
          }
        },

        """
        <html></html>"""
    );
  }

  @Test(description = """
  attributes
  """)
  public void testCase02() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(id("foo"));
          }
        },

        """
        <div id='foo'></div>"""
    );
  }

  @Test(description = """
  attributes escaping (HTML)
  """)
  public void testCase03() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            StringBuilder sb;
            sb = new StringBuilder();

            sb.append('\'');
            sb.append(' ');
            sb.append('\"');
            sb.append(' ');
            sb.append('<');
            sb.append(' ');
            sb.append('>');
            sb.append(' ');
            sb.append('&');

            div(id(sb.toString()));
          }
        },

        "<div id='&#39; \\\" < > &amp;'></div>"
    );
  }

  @Test(description = """
  attributes escaping (JSON)
  """)
  public void testCase04() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            StringBuilder sb;
            sb = new StringBuilder();

            for (char c = 0; c < 32; c++) {
              sb.append(c);
            }

            sb.append(' ');
            sb.append('\"');

            div(id(sb.toString()));
          }
        },

        "<div id='\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\b\\t\\n\\u000B\\f\\r\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \\\"'></div>"
    );
  }

  @Test(description = """
  text
  """)
  public void testCase05() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div("foo");
          }
        },

        "<div>foo</div>"
    );
  }

  @Test(description = """
  text escape (HTML)
  """)
  public void testCase06() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            StringBuilder sb;
            sb = new StringBuilder();

            sb.append('\'');
            sb.append(' ');
            sb.append('\"');
            sb.append(' ');
            sb.append('<');
            sb.append(' ');
            sb.append('>');
            sb.append(' ');
            sb.append('&');

            div(sb.toString());
          }
        },

        "<div>' \\\" &lt; &gt; &amp;</div>"
    );
  }

  @Test(description = """
  text escape (JSON)
  """)
  public void testCase07() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            StringBuilder sb;
            sb = new StringBuilder();

            for (char c = 0; c < 32; c++) {
              sb.append(c);
            }

            sb.append(' ');
            sb.append('\"');

            div(sb.toString());
          }
        },

        "<div>\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\b\\t\\n\\u000B\\f\\r\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \\\"</div>"
    );
  }

  @Test(description = """
  raw
  """)
  public void testCase08() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(raw("foo"));
          }
        },

        "<div>foo</div>"
    );
  }

  @Test(description = """
  raw escape (HTML)
  """)
  public void testCase09() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            StringBuilder sb;
            sb = new StringBuilder();

            sb.append('\'');
            sb.append(' ');
            sb.append('\"');
            sb.append(' ');
            sb.append('<');
            sb.append(' ');
            sb.append('>');
            sb.append(' ');
            sb.append('&');

            div(raw(sb.toString()));
          }
        },

        "<div>' \\\" < > &</div>"
    );
  }

  @Test(description = """
  raw escape (JSON)
  """)
  public void testCase10() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            StringBuilder sb;
            sb = new StringBuilder();

            for (char c = 0; c < 32; c++) {
              sb.append(c);
            }

            sb.append(' ');
            sb.append('\"');

            div(raw(sb.toString()));
          }
        },

        "<div>\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\b\\t\\n\\u000B\\f\\r\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \\\"</div>"
    );
  }

  private void test(Html.Template template, String expected) {
    assertEquals(template.toJsonString(), expected);
  }

}
