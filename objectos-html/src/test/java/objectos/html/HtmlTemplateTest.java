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

public class HtmlTemplateTest {

  @Test(description = """
  HtmlTemplate TC00

  - single html element
  """)
  public void testCase00() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html();
        }
      }.minified(),

      """
      <html></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC01

  - single html element with a single attribute.
  """)
  public void testCase01() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(lang("pt-BR"));
        }
      }.minified(),

      """
      <html lang="pt-BR"></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC02

  - Single html element with two attributes
  """)
  public void testCase02() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(_class("no-js"), lang("pt-BR"));
        }
      }.minified(),

      """
      <html class="no-js" lang="pt-BR"></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC03

  - Single html element with a single head child element
  """)
  public void testCase03() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            head()
          );
        }
      }.minified(),

      """
      <html><head></head></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC04

  - Single html element with an attribute and child.
  - Attribute is defined in Java after the element.
  """)
  public void testCase04() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            head(),
            lang("pt-BR")
          );
        }
      }.minified(),

      """
      <html lang="pt-BR"><head></head></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC05

  - Nest depth=2
  - self closing tag (meta)
  """)
  public void testCase05() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            head(
              meta()
            )
          );
        }
      }.minified(),

      """
      <html><head><meta></head></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC06

  - Nest depth=2
  - self closing tag (meta)
  - with attributes
  """)
  public void testCase06() {
    assertEquals(
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
      }.minified(),

      """
      <html lang="pt-BR"><head><meta charset="utf-8"></head></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC07

  - Siblings as children of a single html element.
  """)
  public void testCase07() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            head(),
            body()
          );
        }
      }.minified(),

      """
      <html><head></head><body></body></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC08

  - Siblings + single attribute.
  """)
  public void testCase08() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            lang("pt-BR"),
            head(),
            body()
          );
        }
      }.minified(),

      """
      <html lang="pt-BR"><head></head><body></body></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC09

  - Siblings at root.
  - Also doctype.
  """)
  public void testCase09() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          doctype();
          html();
        }
      }.minified(),

      """
      <!doctype html><html></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC10

  - Test fragment inclusion.
  """)
  public void testCase10() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            head(
              f(this::head0)
            )
          );
        }

        private void head0() {
          meta(charset("utf-8"));
        }
      }.minified(),

      """
      <html><head><meta charset="utf-8"></head></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC11

  - Nested fragments.
  """)
  public void testCase11() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            body(
              f(this::body0)
            )
          );
        }

        private void body0() {
          header(
            f(this::hero)
          );
        }

        private void hero() {
          nav();
        }
      }.minified(),

      """
      <html><body><header><nav></nav></header></body></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC12

  - Siblings fragments.
  """)
  public void testCase12() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            f(this::head0),
            f(this::body0)
          );
        }

        private void head0() {
          head(
            meta()
          );
        }

        private void body0() {
          body(
            header()
          );
        }
      }.minified(),

      """
      <html><head><meta></head><body><header></header></body></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC13

  - attributes come from another object.
  """)
  public void testCase13() {
    assertEquals(
      new HtmlTemplate() {
        final TestIdSelector FOO = new TestIdSelector("foo");
        final TestIdSelector BAR = new TestIdSelector("bar");

        @Override
        protected final void definition() {
          html(
            FOO,
            body(BAR)
          );
        }
      }.minified(),

      """
      <html id="foo"><body id="bar"></body></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC14

  - text child element
  """)
  public void testCase14() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            body(
              p("o7html")
            )
          );
        }
      }.minified(),

      """
      <html><body><p>o7html</p></body></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC15

  - text nodes + element nodes
  """)
  public void testCase15() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          div(
            t("a"),
            p("b"),
            t("c")
          );
        }
      }.minified(),

      """
      <div>a<p>b</p>c</div>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC16

  - attribute or element (title)
  """)
  public void testCase16() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            head(
              title("element")
            ),
            body(
              title("attribute")
            )
          );
        }
      }.minified(),

      """
      <html><head><title>element</title></head><body title="attribute"></body></html>"""
    );
  }

  @Test(description = """
  HtmlTemplate TC17

  - attribute or element (title)
  """)
  public void testCase17() {
    assertEquals(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            head(
              meta(charset("utf8")),
              title("test case 17")
            ),
            body(
              id("id"),
              title("t1"),
              title("t2"),
              p("tc17")
            )
          );
        }
      }.minified(),

      """
      <html><head><meta charset="utf8"><title>test case 17</title></head><body id="id" title="t1 t2"><p>tc17</p></body></html>"""
    );
  }

}
