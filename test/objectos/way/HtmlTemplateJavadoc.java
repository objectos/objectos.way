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

import java.util.List;
import org.testng.annotations.Test;

public class HtmlTemplateJavadoc {

  @Test
  public void main() {
    // @start region="main"
    class HelloWorld extends Html.Template {
      @Override
      protected void render() {
        doctype();
        html(
            head(
                title("Objectos HTML")
            ),

            body(
                p("Hello world!")
            )
        );
      }
    }
    // @end

    assertEquals(
        new HelloWorld().toString(),

        """
      <!DOCTYPE html>
      <html>
      <head>
      <title>Objectos HTML</title>
      </head>
      <body>
      <p>Hello world!</p>
      </body>
      </html>
      """
    );
  }

  @Test
  public void main_iteration() {
    // @start region="main-iteration"
    class HelloWorld extends Html.Template {
      @Override
      protected void render() {
        ul(
            include(this::languages)
        );
      }

      private void languages() {
        List<String> langs = List.of(
            "Java", "Scala", "Clojure", "Kotlin");

        for (var lang : langs) {
          li(lang);
        }
      }
    }
    // @end

    assertEquals(
        new HelloWorld().toString(),

        """
      <ul>
      <li>Java</li>
      <li>Scala</li>
      <li>Clojure</li>
      <li>Kotlin</li>
      </ul>
      """
    );
  }

  @Test
  public void main_condition01() {
    // @start region="main-condition01"
    class HelloWorld extends Html.Template {
      @Override
      protected void render() {
        div(
            h1("Actions"),
            include(this::actions)
        );
      }

      private void actions() {
        button("Read");

        if (hasWritePermission()) {
          button("Write");
        }
      }

      private boolean hasWritePermission() {
        return true; // @replace regex='return true;' replacement="// implementation..."
      }
    }
    // @end

    assertEquals(
        new HelloWorld().toString(),

        """
      <div>
      <h1>Actions</h1>
      <button>Read</button><button>Write</button></div>
      """
    );
  }

  @Test
  public void main_condition02() {
    // @start region="main-condition02"
    class HelloWorld extends Html.Template {
      @Override
      protected void render() {
        div(
            h1("Actions"),
            div(button("Read")),
            hasWritePermission() ? div(button("Write")) : noop()
        );
      }

      private boolean hasWritePermission() {
        return true; // @replace regex='return true;' replacement="// implementation..."
      }
    }
    // @end

    assertEquals(
        new HelloWorld().toString(),

        """
      <div>
      <h1>Actions</h1>
      <div><button>Read</button></div>
      <div><button>Write</button></div>
      </div>
      """
    );
  }

}