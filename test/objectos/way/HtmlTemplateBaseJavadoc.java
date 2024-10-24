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
package objectos.way;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HtmlTemplateBaseJavadoc {

  @Test
  public void javadoc_include() {
    // @start region="IncludeExample"
    class IncludeExample extends Html.Template {
      @Override
      protected void render() {
        doctype();
        html(
            head(
                include(this::head0)
            ),
            body(
                include(this::body0)
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
        new IncludeExample().toString(),

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
  public void javadoc_flatten() {
    // @start region="flatten"
    class MyComponent extends Html.Component {
      public MyComponent(Html.Template parent) { super(parent); }

      public Html.Instruction.OfElement render(Html.Instruction... contents) {
        return div(
            className("my-component"),

            flatten(contents) // @highlight substring="flatten"
        );
      }
    }

    class MyTemplate extends Html.Template {
      @Override
      protected void render() {
        MyComponent comp = new MyComponent(this);

        body(
            comp.render(
                h1("Flatten example"),

                p("First paragraph"),

                p("Second paragraph")
            )
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
  public void javadoc_noop() {
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
  public void javadoc_text() {
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