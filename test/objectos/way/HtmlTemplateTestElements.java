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

import org.testng.annotations.Test;

public class HtmlTemplateTestElements {

  @Test
  public void aside() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            aside();
            aside(p("child"));
            aside("Text only");
          }
        },

        """
        <aside></aside>
        <aside>
        <p>child</p>
        </aside>
        <aside>Text only</aside>
        """
    );
  }

  @Test
  public void dialog() {
    test(
        new Html.Template() {
          @Override
          protected final void render() {
            dialog();
            dialog(p("child"));
            dialog("Text only");
            dialog(open);
            dialog(closedby("foo"));
          }
        },

        """
        <dialog></dialog>
        <dialog>
        <p>child</p>
        </dialog>
        <dialog>Text only</dialog>
        <dialog open></dialog>
        <dialog closedby="foo"></dialog>
        """
    );
  }

  private void test(Html.Template template, String expected) {
    String result;
    result = template.toString();

    assertEquals(result, expected);
  }

}
