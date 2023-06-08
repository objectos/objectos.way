/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class CssTemplateTest {

  private final CssSink sink = new CssSink();

  private final StringBuilder stringBuilder = new StringBuilder();

  @Test
  public void testCase00() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(BODY);
        }
      },

      """
      body {}
      """,

      """
      body{}
      """
    );
  }

  @Test
  public void testCase01() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(id("myid"));
        }
      },

      """
      #myid {}
      """,

      """
      #myid{}
      """
    );
  }

  @Test
  public void testCase02() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(A, id("myid"));
        }
      },

      """
      a#myid {}
      """,

      """
      a#myid{}
      """
    );
  }

  @Test
  public void testCase03() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(A, IdSelector.of("myid"));
        }
      },

      """
      a#myid {}
      """,

      """
      a#myid{}
      """
    );
  }

  @Test
  public void testCase04() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(BODY, className("dsl"), ClassSelector.of("obj"));
        }
      },

      """
      body.dsl.obj {}
      """,

      """
      body.dsl.obj{}
      """
    );
  }

  @Test
  public void testCase05() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(UL, SP, LI);
        }
      },

      """
      ul li {}
      """,

      """
      ul li{}
      """
    );
  }

  @Test
  public void testCase06() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            A, AFTER, OR,
            A, VISITED
          );
        }
      },

      """
      a::after, a:visited {}
      """,

      """
      a::after,a:visited{}
      """
    );
  }

  @Test
  public void testCase07() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            attr("type"), OR,
            attr("type", eq("input")), OR,
            attr("data-attr", startsWith("start"))
          );
        }
      },

      """
      [type], [type="input"], [data-attr^="start"] {}
      """,

      """
      [type],[type=input],[data-attr^=start]{}
      """
    );
  }

  @Test
  public void testCase08() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            ANY,

            display(BLOCK)
          );
        }
      },

      """
      * {
        display: block;
      }
      """,

      """
      *{display:block}
      """
    );
  }

  @Test
  public void testCase09() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            ANY,

            zIndex(-300)
          );
        }
      },

      """
      * {
        z-index: -300;
      }
      """,

      """
      *{z-index:-300}
      """
    );
  }

  @Test
  public void testCase10() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            ANY,

            lineHeight(2.5)
          );
        }
      },

      """
      * {
        line-height: 2.5;
      }
      """,

      """
      *{line-height:2.5}
      """
    );
  }

  @Test
  public void testCase11() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            ANY,

            content("Chapter ")
          );
        }
      },

      """
      * {
        content: "Chapter ";
      }
      """,

      """
      *{content:"Chapter "}
      """
    );
  }

  @Test
  public void testCase12() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            ANY,

            minHeight(px(160))
          );
        }
      },

      """
      * {
        min-height: 160px;
      }
      """,

      """
      *{min-height:160px}
      """
    );
  }

  @Test
  public void testCase13() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            ANY,

            lineHeight(pt(1.4))
          );
        }
      },

      """
      * {
        line-height: 1.4pt;
      }
      """,

      """
      *{line-height:1.4pt}
      """
    );
  }

  private void test(CssTemplate template, String pretty, String minified) {
    stringBuilder.setLength(0);

    sink.toStringBuilder(template, stringBuilder);

    assertEquals(stringBuilder.toString(), pretty);
  }

}