/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.util.function.Consumer;
import org.testng.annotations.Test;

public class ScriptWriterTest {

  private static final Html.Id FOO = Html.Id.of("foo");

  @Test
  public void delay() {
    test(
        script -> script.delay(500, callback -> callback.submit(FOO)),

        """
        [["delay-0",500,[["submit-0","foo"]]]]"""
    );
  }

  @Test
  public void elementById() {
    test(
        script -> {
          var foo = script.elementById(FOO);

          script.request(req -> {
            req.url(foo.getAttribute("href"));
          });
        },

        """
        [["request-0","GET",["id-1","foo","getAttribute","href"],[]]]"""
    );
  }

  @Test
  public void html() {
    Html.Template template = new Html.Template() {
      @Override
      protected final void render() {
        doctype();
        html(
            lang("en"),
            body("Hello world!")
        );
      }
    };

    test(
        script -> script.html(template),

        """
        [["html-0","<!DOCTYPE html><html lang='en'><body>Hello world!</body></html>"]]"""
    );
  }

  @Test
  public void navigate() {
    test(
        Script::navigate,

        """
        [["navigate-0"]]"""
    );
  }

  @Test
  public void pushState() {
    test(
        script -> script.pushState("/next-location"),

        """
        [["push-state-0","/next-location"]]"""
    );
  }

  @Test
  public void replaceState() {
    test(
        script -> script.replaceState("/next-location"),

        """
        [["replace-state-0","/next-location"]]"""
    );
  }

  @Test
  public void request0Test01() {
    test(
        script -> script.request(req -> {
          req.url("/foo");
        }),

        """
        [["request-0","GET","/foo",[]]]"""
    );
  }

  @Test
  public void request0Test02() {
    test(
        script -> {
          var el = script.element();

          script.request(req -> {
            req.url(el.getAttribute("href"));
          });
        },

        """
        [["request-0","GET",["element-1","getAttribute","href"],[]]]"""
    );
  }

  @Test
  public void request0Test03() {
    test(
        script -> script.request(req -> {
          req.url("/foo");

          req.onSuccess(success -> {
            success.stopPropagation();
          });
        }),

        """
        [["request-0","GET","/foo",[["stop-propagation-0"]]]]"""
    );
  }

  @Test
  public void request0Test04() {
    test(
        script -> script.request(req -> {
          req.method(Script.POST);

          req.url("/foo");
        }),

        """
        [["request-0","POST","/foo",[]]]"""
    );
  }

  @Test
  public void setAttribute() {
    test(
        script -> script.setAttribute(FOO, "value", "x"),

        """
        [["set-attribute-0","foo","value","x"]]"""
    );
  }

  @Test
  public void stopPropagation0() {
    test(
        Script::stopPropagation,

        """
        [["stop-propagation-0"]]"""
    );
  }

  @Test
  public void submit() {
    test(
        script -> script.submit(FOO),

        """
        [["submit-0","foo"]]"""
    );
  }

  @Test
  public void toggleClass0() {
    test(
        script -> {
          var foo = script.elementById(FOO);
          foo.toggleClass("x");
          foo.toggleClass("c1 c2");
        },

        """
        [["id-2","foo","toggle-class-0","x"],["id-2","foo","toggle-class-0","c1","c2"]]"""
    );
  }

  private void test(Consumer<Script> script, String expected) {
    final ScriptWriter writer;
    writer = new ScriptWriter();

    script.accept(writer);

    final String result;
    result = writer.toString();

    assertEquals(result, expected);
  }

}