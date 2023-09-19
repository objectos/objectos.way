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

import objectos.html.tmpl.Api;
import org.testng.annotations.Test;

public class HtmlTemplateIncludeTest {

  @Test(description = """
  HtmlTemplate TC10

  - Test fragment inclusion.
  """)
  public void testCase01() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            head(
              include(this::head0)
            )
          );
        }

        private void head0() {
          meta(charset("utf-8"));
        }
      },

      """
      <html>
      <head>
      <meta charset="utf-8">
      </head>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC11

  - Nested fragments.
  """)
  public void testCase02() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            body(
              include(this::body0)
            )
          );
        }

        private void body0() {
          header(
            include(this::hero)
          );
        }

        private void hero() {
          nav();
        }
      },

      """
      <html>
      <body>
      <header>
      <nav></nav>
      </header>
      </body>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC12

  - Siblings fragments.
  """)
  public void testCase03() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          html(
            include(this::head0),
            include(this::body0)
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
      },

      """
      <html>
      <head>
      <meta>
      </head>
      <body>
      <header></header>
      </body>
      </html>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC20

  - include template
  """)
  public void testCase04() {
    test(
      new HtmlTemplate() {
        private final HtmlTemplate nav = new HtmlTemplate() {
          @Override
          protected final void definition() {
            nav(
              a("o7html")
            );
          }
        };

        private final HtmlTemplate hero = new HtmlTemplate() {
          @Override
          protected final void definition() {
            section(
              p("is cool")
            );
          }
        };

        @Override
        protected final void definition() {
          body(
            include(nav),
            include(hero)
          );
        }
      },

      """
      <body>
      <nav><a>o7html</a></nav>
      <section>
      <p>is cool</p>
      </section>
      </body>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC26

  - multi-level include
  """)
  public void testCase05() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          div(
            include(this::f0),

            main(
              article(
                include(this::f1)
              )
            ),

            include(this::f2)
          );
        }

        private void f0() {
          div(id("f0"));
        }

        private void f1() {
          div(id("f1"));
        }

        private void f2() {
          div(id("f2"));
        }
      },

      """
      <div>
      <div id="f0"></div>
      <main>
      <article>
      <div id="f1"></div>
      </article>
      </main>
      <div id="f2"></div>
      </div>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC34

  - allow attributeOrElement in the root of lambda
  """)
  public void testCase06() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          head(
            include(this::head0)
          );
        }

        private void head0() {
          meta(charset("utf-8"));
          title("Test Case 34");
        }
      },

      """
      <head>
      <meta charset="utf-8">
      <title>Test Case 34</title>
      </head>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC40

  - text in lambda
  """)
  public void testCase07() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          h1(include(this::heading1));
        }

        private void heading1() {
          t("abc");
        }
      },

      """
      <h1>abc</h1>
      """
    );
  }

  @Test(description = """
  HtmlTemplate TC41

  - raw in lambda
  """)
  public void testCase08() {
    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          h1(include(this::heading1));
        }

        private void heading1() {
          raw("abc");
        }
      },

      """
      <h1>abc</h1>
      """
    );
  }

  @Test
  public void testCase09() {
    HtmlTemplate component;
    component = new HtmlTemplate() {
      @Override
      protected final void definition() {
        div(
          className("component"),

          div(
            className("wrapper"),

            t("foobar")
          )
        );
      }
    };

    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          body(
            include(component)
          );
        }
      },

      """
      <body>
      <div class="component">
      <div class="wrapper">foobar</div>
      </div>
      </body>
      """
    );
  }

  @Test
  public void testCase10() {
    HtmlTemplate component;
    component = new HtmlTemplate() {
      @Override
      protected final void definition() {
        div(
          className("component")
        );
      }
    };

    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          body(include(this::body));
        }

        private void body() {
          h1("Test");

          include(component);
        }
      },

      """
      <body>
      <h1>Test</h1>
      <div class="component"></div>
      </body>
      """
    );
  }

  @Test
  public void testCase11() {
    class Component extends HtmlComponent {
      public Component(HtmlTemplate parent) {
        super(parent);
      }

      public final void render(Api.ElementContents child) {
        div(
          className("component"),

          child
        );
      }
    }

    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          body(include(this::body));
        }

        private void body() {
          h1("Test");

          Component component;
          component = new Component(this);

          component.render(p("Text"));
        }
      },

      """
      <body>
      <h1>Test</h1>
      <div class="component">
      <p>Text</p>
      </div>
      </body>
      """
    );
  }

  @Test
  public void testCase12() {
    class Component1 extends HtmlComponent {
      public Component1(HtmlTemplate parent) {
        super(parent);
      }

      public final Api.ElementContents render(Api.ElementContents e) {
        return div(
          className("c1"),
          e
        );
      }
    }

    class Component2 extends HtmlComponent {
      public Component2(HtmlTemplate parent) {
        super(parent);
      }

      public final Api.ElementContents render(Api.ElementContents e) {
        return div(
          className("c2"),
          e
        );
      }
    }

    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          body(include(this::body));
        }

        private void body() {
          Component1 c1;
          c1 = new Component1(this);

          Component2 c2;
          c2 = new Component2(this);

          h1("Test");

          c1.render(
            c2.render(t("A"))
          );
        }
      },

      """
      <body>
      <h1>Test</h1>
      <div class="c1">
      <div class="c2">A</div>
      </div>
      </body>
      """
    );
  }

  @Test
  public void testCase13() {
    class Navigation extends HtmlComponent {
      public Navigation(HtmlTemplate parent) {
        super(parent);
      }

      public final Api.ElementContents render(Api.UnorderedListInstruction... elements) {
        return nav(
          ul(elements)
        );
      }
    }

    class Link extends HtmlComponent {
      public Link(HtmlTemplate parent) { super(parent); }

      public final Api.ElementContents render(Api.AnchorInstruction... elements) {
        return li(
          a(elements)
        );
      }
    }

    test(
      new HtmlTemplate() {
        @Override
        protected final void definition() {
          Navigation navigation = new Navigation(this);

          Link link = new Link(this);

          header(
            navigation.render(
              link.render(href("#"), t("Products")),

              link.render(href("#"), t("Services"))
            )
          );
        }
      },

      """
      <header>
      <nav>
      <ul>
      <li><a href="#">Products</a></li>
      <li><a href="#">Services</a></li>
      </ul>
      </nav>
      </header>
      """
    );
  }

  private void test(HtmlTemplate template, String expected) {
    String result;
    result = template.toString();

    assertEquals(result, expected);
  }

}