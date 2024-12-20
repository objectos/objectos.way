/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.nio.charset.StandardCharsets;
import objectos.way.Script.Action;
import org.testng.annotations.Test;

public class ScriptActionTest {

  private static final Html.Id _OVERLAY = Html.Id.of("overlay");

  @Test
  public void action() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            Action x = Script.replaceClass(Html.Id.of("x"), "on", "off");
            Action y = Script.replaceClass(Html.Id.of("y"), "foo", "bar");

            div(
                dataOnClick(Script.actions())
            );
            div(
                dataOnClick(Script.actions(x))
            );
            div(
                dataOnClick(Script.actions(x, y))
            );
          }
        },

        """
        <div></div>
        <div data-on-click='[{"cmd":"replace-class","args":["x","on","off"]}]'></div>
        <div data-on-click='[{"cmd":"replace-class","args":["x","on","off"]},{"cmd":"replace-class","args":["y","foo","bar"]}]'></div>
        """
    );
  }

  @Test
  public void addClass() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(dataOnClick(Script.addClass(_OVERLAY, "hidden")));
            div(dataOnClick(Script.addClass(_OVERLAY, "hidden", "block")));
          }
        },

        """
        <div data-on-click='[{"cmd":"add-class","args":["overlay","hidden"]}]'></div>
        <div data-on-click='[{"cmd":"add-class","args":["overlay","hidden","block"]}]'></div>
        """
    );
  }

  @Test
  public void contentType() {
    assertEquals(Script.addClass(_OVERLAY, "hidden").contentType(), "application/json");
  }

  @Test
  public void delay() {
    test(
        new Html.Template() {
          static final Html.Id FORM = Html.Id.of("foo");

          @Override
          protected final void render() {
            form(FORM,
                input(dataOnInput(Script.delay(500, Script.submit(FORM))))
            );
          }
        },

        """
        <form id="foo"><input data-on-input='[{"cmd":"delay","ms":500,"actions":[{"cmd":"submit","id":"foo"}]}]'></form>
        """
    );
  }

  @Test
  public void html() {
    Html.Template template = new Html.Template() {
      @Override
      protected final void render() {
        doctype();
        html(
            body("Hello world!")
        );
      }
    };

    Script.Action action;
    action = Script.html(template);

    assertEquals(action.toString(), """
    [{"cmd":"html","value":"<!DOCTYPE html><html><body>Hello world!</body></html>"}]""");
  }

  @Test
  public void location() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            a(dataOnClick(Script.location("/foo")), text("Foo"));
          }
        },

        """
        <a data-on-click='[{"cmd":"location","value":"/foo"}]'>Foo</a>
        """
    );
  }

  @Test
  public void mediaBytes() {
    Script.Action action;
    action = Script.addClass(_OVERLAY, "hidden");

    byte[] mediaBytes;
    mediaBytes = action.mediaBytes();

    String s;
    s = new String(mediaBytes, StandardCharsets.UTF_8);

    assertEquals(s, """
    [{"cmd":"add-class","args":["overlay","hidden"]}]""");
  }

  @Test
  public void navigate() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            a(dataOnClick(Script.navigate()), text("Foo"));
          }
        },

        """
        <a data-on-click='[{"cmd":"navigate"}]'>Foo</a>
        """
    );
  }

  @Test
  public void removeClass() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(dataOnClick(Script.removeClass(_OVERLAY, "hidden")));
            div(dataOnClick(Script.removeClass(_OVERLAY, "hidden", "block")));
          }
        },

        """
        <div data-on-click='[{"cmd":"remove-class","args":["overlay","hidden"]}]'></div>
        <div data-on-click='[{"cmd":"remove-class","args":["overlay","hidden","block"]}]'></div>
        """
    );
  }

  @Test
  public void setProperty() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(dataOnClick(Script.setProperty(_OVERLAY, "display", "hidden")));
          }
        },

        """
        <div data-on-click='[{"cmd":"set-property","args":["overlay","display","hidden"]}]'></div>
        """
    );
  }

  @Test
  public void stopPropagation() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            div(dataOnClick(Script.stopPropagation()));
          }
        },

        """
        <div data-on-click='[{"cmd":"stop-propagation"}]'></div>
        """
    );
  }

  @Test
  public void submit() {
    test(
        new Html.Template() {
          static final Html.Id FORM = Html.Id.of("foo");

          @Override
          protected final void render() {
            form(FORM,
                input(dataOnInput(Script.submit(FORM)))
            );
          }
        },

        """
        <form id="foo"><input data-on-input='[{"cmd":"submit","id":"foo"}]'></form>
        """
    );
  }

  @Test
  public void toggleClass() {
    test(
        new Html.Template() {
          static final Html.Id OVERLAY = Html.Id.of("overlay");

          @Override
          protected final void render() {
            div(dataOnClick(Script.toggleClass(OVERLAY, "foo")));
            div(dataOnClick(Script.toggleClass(OVERLAY, "c1", "c2")));
          }
        },

        """
        <div data-on-click='[{"cmd":"toggle-class","args":["overlay","foo"]}]'></div>
        <div data-on-click='[{"cmd":"toggle-class","args":["overlay","c1","c2"]}]'></div>
        """
    );
  }

  private void test(Html.Template template, String expected) {
    String result;
    result = template.toString();

    assertEquals(result, expected);
  }

}