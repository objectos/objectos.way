/*
 * Copyright (C) 2015-2025 Objectos Software LTDA.
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

import java.io.IOException;
import objectos.way.DomFormatter.Quotes;
import org.testng.annotations.Test;

public class DomFormatterTest {

  private final StringBuilder out = new StringBuilder();

  @Test(description = """
  PrettyPrintWriter TC01

  - empty element
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
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            html();
          }
        },

        """
        <!DOCTYPE html>
        <html></html>
        """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC03

  - head/body
  """)
  public void testCase03() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            html(
                head(),
                body()
            );
          }
        },

        """
      <!DOCTYPE html>
      <html>
      <head></head>
      <body></body>
      </html>
      """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC04

  - head/meta (self closing)
  """)
  public void testCase04() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            html(
                head(
                    meta(charset("utf-8"))
                ),
                body()
            );
          }
        },

        """
        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="utf-8">
        </head>
        <body></body>
        </html>
        """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC05

  - head/meta (self closing)
  - + attributes
  """)
  public void testCase05() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            html(
                id("a"),
                head(
                    id("b"),
                    meta(charset("utf-8"))
                ),
                body(
                    id("c")
                )
            );
          }
        },

        """
        <!DOCTYPE html>
        <html id="a">
        <head id="b">
        <meta charset="utf-8">
        </head>
        <body id="c"></body>
        </html>
        """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC06

  - paragraph w/ text
  """)
  public void testCase06() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            html(
                body(
                    p("abc")
                )
            );
          }
        },

        """
        <!DOCTYPE html>
        <html>
        <body>
        <p>abc</p>
        </body>
        </html>
        """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC07

  - paragraph w/ text
  - + inline elements
  """)
  public void testCase07() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            html(
                body(
                    p(text("abc "), em("def"), text(" ghi"))
                )
            );
          }
        },

        """
        <!DOCTYPE html>
        <html>
        <body>
        <p>abc <em>def</em> ghi</p>
        </body>
        </html>
        """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC08

  - ul/li
  """)
  public void testCase08() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            html(
                body(
                    ul(
                        li("a"),
                        li(p("b")),
                        li(em("c"))
                    )
                )
            );
          }
        },

        """
        <!DOCTYPE html>
        <html>
        <body>
        <ul>
        <li>a</li>
        <li>
        <p>b</p>
        </li>
        <li><em>c</em></li>
        </ul>
        </body>
        </html>
        """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC09

  - style
  """)
  public void testCase09() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            html(
                head(
                    style(
                        """
                #a {border: 0}

                #b {margin: 0}"""

                    ),
                    style(
                        """
                #c {border: 0}

                #d {margin: 0}
                """
                    )
                )
            );
          }
        },

        """
      <!DOCTYPE html>
      <html>
      <head>
      <style>
      #a {border: 0}

      #b {margin: 0}
      </style>
      <style>
      #c {border: 0}

      #d {margin: 0}
      </style>
      </head>
      </html>
      """
    );
  }

  @Test(description = """
  PrettyPrintWriter TC10

  - always end with a NL
  """)
  public void testCase10() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            doctype();
            a(href("index.html"), text("a"));
          }
        },

        """
        <!DOCTYPE html>
        <a href="index.html">a</a>
        """
    );
  }

  private void test(Html.Template template, String expected) {
    try {
      StringBuilder out;
      out = new StringBuilder();

      DomFormatter.STANDARD.formatTo(template, out);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  @Test
  public void ampersand01() {
    // normal text
    ampersand("abc", "abc");

    // html tokens
    ampersand("if (a < b && c > d) {}", "if (a < b &amp;&amp; c > d) {}");

    // named entities
    ampersand("foo&nbsp;bar", "foo&nbsp;bar");
    ampersand("foo&nb#;bar", "foo&amp;nb#;bar");
    ampersand("foo&nbsp bar", "foo&amp;nbsp bar");

    // decimal entities
    ampersand("foo&#39;bar", "foo&#39;bar");
    ampersand("foo &# 39;", "foo &amp;# 39;");

    // hex entities
    ampersand("foo&#xa9;bar&Xa9;baz", "foo&#xa9;bar&Xa9;baz");
    ampersand("foo &#xxa9;", "foo &amp;#xxa9;");

    // ampersand edge cases
    ampersand("&", "&amp;");
    ampersand("int a = value & MASK;", "int a = value &amp; MASK;");

    // new lines should be left alone
    ampersand("foo\nbar", "foo\nbar");
  }

  private void ampersand(String source, String expected) {
    try {
      StringBuilder out;
      out = new StringBuilder();

      out.setLength(0);

      DomFormatter formatter;
      formatter = DomFormatter.STANDARD;

      for (int idx = 0, len = source.length(); idx < len;) {
        char c;
        c = source.charAt(idx++);

        switch (c) {
          case '&' -> idx = formatter.ampersand(out, source, idx, len);

          default -> out.append(c);
        }
      }

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw", e);
    }
  }

  @Test
  public void attributeValue01() {
    attributeValue(Quotes.DOUBLE, "abc", "abc");
    attributeValue(Quotes.DOUBLE, "123 foo", "123 foo");
    attributeValue(Quotes.DOUBLE, "if (a < b && c > d) {}", "if (a < b &amp;&amp; c > d) {}");
    attributeValue(Quotes.DOUBLE, "\"abc'", "&#34;abc'");
    attributeValue(Quotes.SINGLE, "\"abc'", "\"abc&#39;");
  }

  private void attributeValue(Quotes quotes, String source, String expected) {
    try {
      out.setLength(0);

      DomFormatter fmt;
      fmt = DomFormatter.STANDARD;

      fmt.attributeValue(out, quotes, source);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw", e);
    }
  }

  @Test
  public void writeText() {
    // normal text
    writeText("abc", "abc");

    // html tokens
    writeText("if (a < b && c > d) {}", "if (a &lt; b &amp;&amp; c &gt; d) {}");

    // named entities
    writeText("foo&nbsp;bar", "foo&amp;nbsp;bar");
    writeText("foo&nb#;bar", "foo&amp;nb#;bar");
    writeText("foo&nbsp bar", "foo&amp;nbsp bar");

    // decimal entities
    writeText("foo&#39;bar", "foo&amp;#39;bar");
    writeText("foo &# 39;", "foo &amp;# 39;");

    // hex entities
    writeText("foo&#xa9;bar&Xa9;baz", "foo&amp;#xa9;bar&amp;Xa9;baz");
    writeText("foo &#xxa9;", "foo &amp;#xxa9;");

    // ampersand edge cases
    writeText("&", "&amp;");
    writeText("int a = value & MASK;", "int a = value &amp; MASK;");

    // quotes should be left alone
    writeText("\"", "\"");
    writeText("'", "'");

    // new lines should be left alone
    writeText("foo\nbar", "foo\nbar");
  }

  private void writeText(String source, String expected) {
    try {
      out.setLength(0);

      var writer = DomFormatter.STANDARD;

      writer.writeText(out, source);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw", e);
    }
  }

}