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

public class HtmlTemplateTestAttributes {

  @Test
  public void as() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            link(rel("preload"), href("/foo.css"), as("style"));
          }
        },

        """
        <link rel="preload" href="/foo.css" as="style">
        """
    );
  }

  @Test
  public void attribute() {
    final Html.AttributeName active;
    active = Html.AttributeName.of("data-active");

    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                attribute(active, "foo")
            );
          }
        },

        """
        <div data-active="foo"></div>
        """
    );
  }

  @Test
  public void checked() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            input(type("checkbox"), checked());
          }
        },

        """
        <input type="checkbox" checked>
        """
    );
  }

  @Test
  public void dataOnClick01() {
    class ThisComponent implements Html.Component {
      private final Html.Id id;

      public ThisComponent(Html.Id id) {
        this.id = id;
      }

      @Override
      public void renderHtml(Html.Markup m) {
        m.button(
            m.dataOnClick(s -> s.elementById(id).toggleClass("hidden block"))
        );
      }
    }

    test(
        new Html.Template() {
          final Html.Id NAV = Html.Id.of("nav");

          @Override
          protected final void render() {
            div(
                renderComponent(new ThisComponent(NAV))
            );
          }
        },

        """
        <div><button data-on-click='[["id-2","nav","toggle-class-0","hidden","block"]]'></button></div>
        """
    );
  }

  @Test
  public void dataOnClick02() {
    test(
        new Html.Template() {
          final Html.Id FOO = Html.Id.of("foo");

          @Override
          protected final void render() {
            div(
                dataOnClick(s -> s.elementById(FOO).toggleClass("a x")),
                dataOnClick(s -> s.elementById(FOO).toggleClass("b y"))
            );

            div(
                dataOnClick(s -> s.elementById(FOO).toggleClass("a x")),
                dataOnClick(s -> {
                  s.elementById(FOO).toggleClass("b y");
                  s.elementById(FOO).toggleClass("c z");
                })
            );

            div(
                dataOnClick(s -> {
                  s.elementById(FOO).toggleClass("a x");
                  s.elementById(FOO).toggleClass("b y");
                }),
                dataOnClick(s -> s.elementById(FOO).toggleClass("c z"))
            );
          }
        },

        """
        <div data-on-click='[["id-2","foo","toggle-class-0","a","x"],["id-2","foo","toggle-class-0","b","y"]]'></div>
        <div data-on-click='[["id-2","foo","toggle-class-0","a","x"],["id-2","foo","toggle-class-0","b","y"],["id-2","foo","toggle-class-0","c","z"]]'></div>
        <div data-on-click='[["id-2","foo","toggle-class-0","a","x"],["id-2","foo","toggle-class-0","b","y"],["id-2","foo","toggle-class-0","c","z"]]'></div>
        """
    );
  }

  @Test
  public void dataOnSuccess() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            form(
                dataOnSuccess(Script::stopPropagation)
            );
          }
        },

        """
        <form data-on-success='[["stop-propagation-0"]]'></form>
        """
    );
  }

  private void test(Html.Template template, String expected) {
    String result;
    result = template.toString();

    assertEquals(result, expected);
  }

}
