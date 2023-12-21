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

import objectos.html.internal.PrettyPrintWriter;
import org.testng.annotations.Test;

public class HtmlTemplateTest2 {

  @Test(description = """
  HtmlTemplate TC00

  - single html element
  """)
  public void testCase00() {
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
  HtmlTemplate TC01

  - single html element with a single attribute.
  """)
  public void testCase01() {
    test(
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            html(lang("pt-BR"));
          }
        },

        """
      <html lang="pt-BR"></html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC02

  - Single html element with two attributes
  """)
  public void testCase02() {
    test(
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            html(className("no-js"), lang("pt-BR"));
          }
        },

        """
      <html class="no-js" lang="pt-BR"></html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC03

  - Single html element with a single head child element
  """)
  public void testCase03() {
    test(
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            html(
                head()
            );
          }
        },

        """
      <html>
      <head></head>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC04

  - Single html element with an attribute and child.
  - Attribute is defined in Java after the element.
  """)
  public void testCase04() {
    test(
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            html(
                head(),
                lang("pt-BR")
            );
          }
        },

        """
      <html lang="pt-BR">
      <head></head>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC05

  - Nest depth=2
  - self closing tag (meta)
  """)
  public void testCase05() {
    test(
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            html(
                head(
                    meta()
                )
            );
          }
        },

        """
      <html>
      <head>
      <meta>
      </head>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC06

  - Nest depth=2
  - self closing tag (meta)
  - with attributes
  """)
  public void testCase06() {
    test(
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            html(
                lang("pt-BR"),
                head(
                    meta(charset("utf-8"))
                )
            );
          }
        },

        """
      <html lang="pt-BR">
      <head>
      <meta charset="utf-8">
      </head>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC07

  - Siblings as children of a single html element.
  """)
  public void testCase07() {
    test(
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            html(
                head(),
                body()
            );
          }
        },

        """
      <html>
      <head></head>
      <body></body>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC08

  - Siblings + single attribute.
  """)
  public void testCase08() {
    test(
        new HtmlTemplate() {
          @Override
          protected final void definition() {
            html(
                lang("pt-BR"),
                head(),
                body()
            );
          }
        },

        """
      <html lang="pt-BR">
      <head></head>
      <body></body>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC09

  - Siblings at root.
  - Also doctype.
  """)
  public void testCase09() {
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

  private void test(HtmlTemplate template, String expected) {
    StringBuilder out;
    out = new StringBuilder();

    PrettyPrintWriter writer;
    writer = new PrettyPrintWriter();

    writer.out = out;

    template.process(writer);

    String result;
    result = out.toString();

    assertEquals(result, expected);
  }

}
