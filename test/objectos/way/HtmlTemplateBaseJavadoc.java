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

import java.util.List;
import org.testng.annotations.Test;

public class HtmlTemplateBaseJavadoc {

  @Test
  public void flatten() {
    // @start region="flatten"
    class MyTemplate extends Html.Template {
      @Override
      protected void render() {
        body(
            myComponent(
                h1("Flatten example"),

                p("First paragraph"),

                p("Second paragraph")
            )
        );
      }

      private Html.Instruction.OfElement myComponent(Html.Instruction... contents) {
        return div(
            className("my-component"),

            flatten(contents) // @highlight substring="flatten"
        );
      }
    }
    // @end

    assertEquals(
        new MyTemplate().toString(),

        """
        <body>
        <div class="my-component">
        <h1>Flatten example</h1>
        <p>First paragraph</p>
        <p>Second paragraph</p>
        </div>
        </body>
        """
    );
  }

  @Test
  public void renderFragment0() {
    // @start region="renderFragment0"
    class FragmentExample extends Html.Template {
      @Override
      protected void render() {
        doctype();
        html(
            head(
                renderFragment(this::head0)
            ),
            body(
                renderFragment(this::body0)
            )
        );
      }

      private void head0() {
        title("Include fragment example");
      }

      private void body0() {
        h1("Objectos HTML");

        p("Using the include instruction");
      }
    }
    // @end

    assertEquals(
        new FragmentExample().toString(),

        """
        <!DOCTYPE html>
        <html>
        <head>
        <title>Include fragment example</title>
        </head>
        <body>
        <h1>Objectos HTML</h1>
        <p>Using the include instruction</p>
        </body>
        </html>
        """
    );
  }

  @Test
  public void renderFragment1() {
    // @start region="renderFragment1"
    class FragmentExample extends Html.Template {
      @Override
      protected void render() {
        List<String> names = List.of("Foo", "Bar");

        body(
            renderFragment(this::frag, names)
        );
      }

      private void frag(List<String> names) {
        for (String name : names) {
          div(name);
        }
      }
    }
    // @end

    assertEquals(
        new FragmentExample().toString(),

        """
        <body>
        <div>Foo</div>
        <div>Bar</div>
        </body>
        """
    );
  }

  @Test
  public void renderFragment2() {
    // @start region="renderFragment2"
    class FragmentExample extends Html.Template {
      @Override
      protected void render() {
        body(
            renderFragment(this::frag, "City", "Tokyo"),
            renderFragment(this::frag, "Country", "Japan")
        );
      }

      private void frag(String name, String value) {
        p(text(name), span(value));
      }
    }
    // @end

    assertEquals(
        new FragmentExample().toString(),

        """
        <body>
        <p>City<span>Tokyo</span></p>
        <p>Country<span>Japan</span></p>
        </body>
        """
    );
  }

  @Test
  public void noop() {
    class NoopExample extends Html.Template {
      boolean error_;

      @Override
      protected void render() {
        // @start region="noop"
        boolean error = isError();

        div(
            className("alert"),
            error ? className("alert-error") : noop(),
            text("This is an alert!")
        );
        // @end
      }

      private boolean isError() {
        return error_;
      }
    }

    NoopExample example;
    example = new NoopExample();

    assertEquals(
        example.toString(),

        """
        <div class="alert">This is an alert!</div>
        """
    );

    example.error_ = true;

    assertEquals(
        example.toString(),

        """
        <div class="alert alert-error">This is an alert!</div>
        """
    );
  }

  @Test
  public void testable() {
    // @start region="testable0"
    class TestableExample extends Html.Template {
      @Override
      protected void render() {
        dl(
            dt("ID"),
            dd(testableField("order.id", "123")), // @highlight substring="testable"
            dt("Qty"),
            dd(testableField("order.qty", "456")) // @highlight substring="testable"
        );
      }
    }
    // @end

    TestableExample example;
    example = new TestableExample();

    // @start region="testable1"
    assertEquals(
        example.toString(),

        """
        <dl>
        <dt>ID</dt>
        <dd>123</dd>
        <dt>Qty</dt>
        <dd>456</dd>
        </dl>
        """
    );
    // @end

    // @start region="testable2"
    assertEquals(
        example.toTestableText(),

        """
        order.id: 123
        order.qty: 456
        """
    );
    // @end
  }

  @Test
  public void text() {
    class Example extends Html.Template {
      @Override
      protected void render() {
        // @start region="text"
        p(
            strong("This is in bold"),
            text(" & this is not")
        );
        // @end
      }
    }

    Example example;
    example = new Example();

    assertEquals(
        example.toString(),

        """
        <p><strong>This is in bold</strong> &amp; this is not</p>
        """
    );
  }

}