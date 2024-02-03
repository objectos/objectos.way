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

import org.testng.annotations.Test;

public class InternalCompiledHtmlTest {

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
        new HtmlTemplate() {
          @Override
          protected final void definition() {
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
        new HtmlTemplate() {
          @Override
          protected final void definition() {
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
        new HtmlTemplate() {
          @Override
          protected final void definition() {
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
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            doctype();
            html(
                body(
                    p(t("abc "), em("def"), t(" ghi"))
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
        new HtmlTemplate() {
          @Override
          protected final void definition() {
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
        new HtmlTemplate() {
          @Override
          protected final void definition() {
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
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            doctype();
            a(href("index.html"), t("a"));
          }
        },

        """
      <!DOCTYPE html>
      <a href="index.html">a</a>
      """
    );
  }

  private void test(HtmlTemplate template, String expected) {
    assertEquals(template.toString(), expected);
  }

}