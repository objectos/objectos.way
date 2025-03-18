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

import org.testng.annotations.Test;

public class HtmlTemplateTestTestable {

  @Test
  public void cell01() {
    test(
        new Html.Template() {
          @Override
          protected void render() {
            table(
                tr(
                    td(testableCell("foo", 5)),
                    td(testableCell("bar", 5))
                ),

                testableNewLine(),

                tr(
                    td(testableCell("lorem", 5)),
                    td(testableCell("ipsum", 5))
                ),

                testableNewLine()
            );
          }
        },

        """
        <table>
        <tr>
        <td>foo</td>
        <td>bar</td>
        </tr>
        <tr>
        <td>lorem</td>
        <td>ipsum</td>
        </tr>
        </table>
        """,

        """
        foo   | bar
        lorem | ipsum
        """
    );
  }

  @Test
  public void field01() {
    test(
        new Html.Template() {
          @Override
          protected void render() {
            testableH1("Test");

            dl(
                dt("ID"),
                dd(testableField("order.id", "123")),
                dt("Qty"),
                dd(testableField("order.qty", "456"))
            );
          }
        },

        """
        <dl>
        <dt>ID</dt>
        <dd>123</dd>
        <dt>Qty</dt>
        <dd>456</dd>
        </dl>
        """,

        """
        # Test

        order.id: 123
        order.qty: 456
        """
    );
  }

  @Test(description = """
  The testable text node + attributes
  """)
  public void field02() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(className("x"), text(testableField("x", "123")));
          }
        },

        """
        <div class="x">123</div>
        """,

        """
        x: 123
        """
    );
  }

  @Test(description = """
  The testable text node + empty value
  """)
  public void field03() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(testableField("empty", ""));
          }
        },

        """
        <div></div>
        """,

        """
        empty:
        """
    );
  }

  @Test(description = """
  The testable text node + fragment
  """)
  public void field04() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(renderFragment(this::fragment));
          }

          private void fragment() {
            div(testableField("x", "abc"));
            testableField("y", "123");
          }
        },

        """
        <div>
        <div>abc</div>
        </div>
        """,

        """
        x: abc
        y: 123
        """
    );
  }

  @Test
  public void field05() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div("before");
            testableField("foo", "bar");
            div("after");
          }
        },

        """
        <div>before</div>
        <div>after</div>
        """,

        """
        foo: bar
        """
    );
  }

  @Test
  public void heading01() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            h1(testableH1("Level 1"));
            h2(testableH2("Level 2"));
            h3(testableH3("Level 3"));
            h4(testableH4("Level 4"));
            h5(testableH5("Level 5"));
            h6(testableH6("Level 6"));
          }
        },

        """
        <h1>Level 1</h1>
        <h2>Level 2</h2>
        <h3>Level 3</h3>
        <h4>Level 4</h4>
        <h5>Level 5</h5>
        <h6>Level 6</h6>
        """,

        """
        # Level 1

        ## Level 2

        ### Level 3

        #### Level 4

        ##### Level 5

        ###### Level 6

        """
    );
  }

  private void test(Html.Template template, String expectedHtml, String expectedTestable) {
    assertEquals(template.toString(), expectedHtml);

    assertEquals(template.toTestableText(), expectedTestable);
  }

}
