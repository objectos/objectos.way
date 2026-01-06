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

import objectos.script.JsAction;
import objectos.script.Js;
import org.testng.annotations.Test;

public class HtmlTemplateTestAttributes {

  private final Html.Id foo = Html.Id.of("foo");

  private final Html.AttributeName dataActive = HtmlAttributeName.custom("data-active");

  @Test
  public void aria() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                ariaCurrent("page"),
                ariaDisabled("true"),
                ariaHidden("false"),
                ariaInvalid("false"),
                ariaLabel("Hello"),
                ariaLabelledBy("id-123"),
                ariaModal("bar"),
                ariaPlaceholder("abc"),
                ariaReadonly("false"),
                ariaRequired("true"),
                ariaSelected("false")
            );
          }
        },

        """
        <div \
        aria-current="page" \
        aria-disabled="true" \
        aria-hidden="false" \
        aria-invalid="false" \
        aria-label="Hello" \
        aria-labelledby="id-123" \
        aria-modal="bar" \
        aria-placeholder="abc" \
        aria-readonly="false" \
        aria-required="true" \
        aria-selected="false"></div>
        """
    );
  }

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
  public void attr01() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                attr(dataActive, "foo")
            );
          }
        },

        """
        <div data-active="foo"></div>
        """
    );
  }

  @Test
  public void attr02() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                f(this::attribute)
            );
          }

          private void attribute() {
            attr(dataActive, "foo");
          }
        },

        """
        <div data-active="foo"></div>
        """
    );
  }

  @Test
  public void attr03() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                className("bar"),
                attr(dataActive, "foo")
            );
          }
        },

        """
        <div class="bar" data-active="foo"></div>
        """
    );
  }

  @Test
  public void attr04() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                attr(dataActive, "v1"),
                attr(dataActive, "v2"),
                attr(dataActive, "v3")
            );
          }
        },

        """
        <div data-active="v1 v2 v3"></div>
        """
    );
  }

  @Test
  public void checked() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            input(type("checkbox"), checked);
          }
        },

        """
        <input type="checkbox" checked>
        """
    );
  }

  @Test
  public void dataOnClick01() {
    final JsAction remove;
    remove = Js.target().invoke("Element", "remove");

    class ThisComponent implements Html.Component {
      @Override
      public void renderHtml(Html.Markup m) {
        m.button(
            m.dataOnClick(remove)
        );
      }
    }

    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                c(new ThisComponent())
            );
          }
        },

        """
        <div><button data-on-click='["W1",["ET"],["IV","Element","remove",[]]]'></button></div>
        """
    );
  }

  @Test
  public void dataOnClick02() {
    /*
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
    */
  }

  @Test
  public void dataOnLoad() {
    /*
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(
                dataOnLoad(s -> {
                  s.elementById(foo).toggleClass("x");
                })
            );
          }
        },

        """
        <div data-on-load='[["id-2","foo","toggle-class-0","x"]]'></div>
        """
    );
    */
  }

  @Test
  public void dataOnSuccess() {
    /*
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
    */
  }

  @Test
  public void form() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            form(foo);

            div(
                input(form("foo")),
                select(form("foo")),
                textarea(form("foo"))
            );

            div(
                button(form("foo"))
            );
          }
        },

        """
        <form id="foo"></form>
        <div><input form="foo"><select form="foo"></select><textarea form="foo"></textarea></div>
        <div><button form="foo"></button></div>
        """
    );
  }

  @Test
  public void download() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            a(href("https://example.com/document.pdf"), download("hey.pdf"));
          }
        },

        """
        <a href="https://example.com/document.pdf" download="hey.pdf"></a>
        """
    );
  }

  @Test
  public void open() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            dialog(open);
            dialog(id("foo"), open);
            dialog(id("foo"), open, className("bar"));
          }
        },

        """
        <dialog open></dialog>
        <dialog id="foo" open></dialog>
        <dialog id="foo" open class="bar"></dialog>
        """
    );
  }

  private void test(Html.Template template, String expected) {
    String result;
    result = template.toString();

    assertEquals(result, expected);
  }

}
