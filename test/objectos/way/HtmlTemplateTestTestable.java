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

  @Test(description = """
  The testable text node
  """)
  public void field01() {
    test(
        new Html.Template() {
          @Override
          protected void render() {
            testableHeading1("Test");

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
        empty: \n\
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

  private void test(Html.Template template, String expectedHtml, String expectedTestable) {
    assertEquals(template.toString(), expectedHtml);

    assertEquals(template.toTestableText(), expectedTestable);
  }

}
