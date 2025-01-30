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
