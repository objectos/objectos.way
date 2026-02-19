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
  public void onclick01() {
    final JsAction remove;
    remove = Js.target().invoke("Element", "remove");

    class ThisComponent implements Html.Component {
      @Override
      public void renderHtml(Html.Markup m) {
        m.button(
            m.onclick(remove)
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
        <div><button onclick='way.on(event,["W1",["ET"],["IV","Element","remove",[]]])'></button></div>
        """
    );
  }

  @Test
  public void onload() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            body(
                onload(Js.byId("target").scrollIntoView())
            );
          }
        },

        """
        <body onload='way.on(event,["W1",["EI",["JS","target"]],["IV","Element","scrollIntoView",[]]])'></body>
        """
    );
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
