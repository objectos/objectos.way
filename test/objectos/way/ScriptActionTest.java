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

import objectos.way.Html.Id;
import org.testng.annotations.Test;

public class ScriptActionTest {

  @Test
  public void delay() {
    test(
        new Html.Template() {
          static final Id FORM = Html.id("foo");

          @Override
          protected final void definition() {
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
  public void location() {
    test(
        new Html.Template() {
          @Override
          protected final void definition() {
            a(dataOnClick(Script.location("/foo")), t("Foo"));
          }
        },

        """
        <a data-on-click='[{"cmd":"location","value":"/foo"}]'>Foo</a>
        """
    );
  }
  
  @Test
  public void submit() {
    test(
        new Html.Template() {
          static final Id FORM = Html.id("foo");

          @Override
          protected final void definition() {
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

  private void test(Html.Template template, String expected) {
    String result;
    result = template.toString();

    assertEquals(result, expected);
  }

}