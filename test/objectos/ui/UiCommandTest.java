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
package objectos.ui;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.css.select.IdSelector;
import objectos.html.HtmlTemplate;
import org.testng.annotations.Test;

public class UiCommandTest {

  private final IdSelector FOO = IdSelector.of("foo");

  @Test(description = """
  Replace
  """)
  public void testCase01() {
    test(
        new UiCommand()
            .html(new HtmlTemplate() {
              @Override
              protected final void definition() {
                doctype();
                body(FOO);
              }
            })
            .replace(FOO),

        """
        [{"cmd":"html","value":"<!DOCTYPE html><body id='foo'></body>"},{"cmd":"replace","id":"foo"}]
        """
    );
  }

  @Test(description = """
  location-href
  """)
  public void testCase02() {
    test(
        new UiCommand().locationHref("/"),

        """
        [{"cmd":"location-href","value":"/"}]
        """
    );
  }

  private void test(UiCommand command, String expected) {
    try {
      StringBuilder out;
      out = new StringBuilder();

      command.writeTo(out);

      assertEquals(out.toString(), expected);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

}