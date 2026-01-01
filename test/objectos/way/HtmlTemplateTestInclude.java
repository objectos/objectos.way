/*
 * Copyright (C) 2015-2026 Objectos Software LTDA.
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

public class HtmlTemplateTestInclude {

  @Test(description = """
  HtmlTemplate TC10

  - Test fragment inclusion.
  """)
  public void testCase01() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            html(
                head(
                    f(this::head0)
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
        new Html.Template() {
          @Override
          protected final void render() {
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
        new Html.Template() {
          @Override
          protected final void render() {
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
        new Html.Template() {
          private final Html.Template nav = new Html.Template() {
            @Override
            protected final void render() {
              nav(
                  a("o7html")
              );
            }
          };

          private final Html.Template hero = new Html.Template() {
            @Override
            protected final void render() {
              section(
                  p("is cool")
              );
            }
          };

          @Override
          protected final void render() {
            body(
                c(nav),
                c(hero)
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
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                f(this::f0),

                main(
                    article(
                        f(this::f1)
                    )
                ),

                f(this::f2)
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
        new Html.Template() {
          @Override
          protected final void render() {
            head(
                f(this::head0)
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
        new Html.Template() {
          @Override
          protected final void render() {
            h1(f(this::heading1));
          }

          private void heading1() {
            text("abc");
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
        new Html.Template() {
          @Override
          protected final void render() {
            h1(f(this::heading1));
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
    Html.Template component;
    component = new Html.Template() {
      @Override
      protected final void render() {
        div(
            className("component"),

            div(
                className("wrapper"),

                text("foobar")
            )
        );
      }
    };

    test(
        new Html.Template() {
          @Override
          protected final void render() {
            body(
                c(component)
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
    Html.Template component;
    component = new Html.Template() {
      @Override
      protected final void render() {
        div(
            className("component")
        );
      }
    };

    test(
        new Html.Template() {
          @Override
          protected final void render() {
            body(f(this::body));
          }

          private void body() {
            h1("Test");

            c(component);
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
    class Component implements Html.Component {
      final String text;

      public Component(String text) {
        this.text = text;
      }

      @Override
      public final void renderHtml(Html.Markup m) {
        m.div(
            m.className("component"),

            m.p(text)
        );
      }
    }

    test(
        new Html.Template() {
          @Override
          protected final void render() {
            body(f(this::body));
          }

          private void body() {
            h1("Test");

            c(new Component("Text"));
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
  public void testCase14() {
    class Contents extends Html.Template {
      @Override
      protected final void render() {
        head("hd");

        body("bd");
      }
    }

    test(
        new Html.Template() {
          @Override
          protected final void render() {
            html(
                c(new Contents())
            );
          }
        },

        """
        <html>
        <head>hd</head>
        <body>bd</body>
        </html>
        """
    );
  }

  @Test
  public void testCase15() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            html(
                f(this::contents)
            );
          }

          private void contents() {
            head("hd");

            body("bd");
          }
        },

        """
        <html>
        <head>hd</head>
        <body>bd</body>
        </html>
        """
    );
  }

  @Test(description = "it should allow offsets > 66k")
  public void testCase16() {
    final int COUNT = 8300;

    StringBuilder expected;
    expected = new StringBuilder();

    expected.append("<html>\n");

    for (int i = 0; i < COUNT; i++) {
      expected.append("<hr>\n");
    }

    expected.append("</html>\n");

    test(
        new Html.Template() {
          @Override
          protected final void render() {
            html(
                f(this::contents)
            );
          }

          private void contents() {
            for (int i = 0; i < COUNT; i++) {
              hr();
            }
          }
        },

        expected.toString()
    );
  }

  @Test(description = "it should allow attributes at the root of lambda")
  public void testCase17() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            input(f(this::attributes));
          }

          private void attributes() {
            id("foo");

            attr(disabled);
          }
        },

        """
        <input id="foo" disabled>
        """
    );
  }

  private void test(Html.Template template, String expected) {
    String result;
    result = template.toString();

    assertEquals(result, expected);
  }

}