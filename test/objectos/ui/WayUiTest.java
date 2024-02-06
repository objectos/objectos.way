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

import objectos.html.HtmlTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WayUiTest {

  private UiBuilder standard;

  @BeforeClass
  public void setUp() {
    standard = new WayUi()
        .onHeadStart(h -> {
          h.meta(h.charset("utf-8"));
          h.meta(h.name("viewport"), h.content("width=device-width, initial-scale=1"));
        });
  }

  @Test
  public void testCase01() {
    test(
        new HtmlTemplate() {
          private final Ui ui = standard.create(this);

          private final UiPage page = ui.page();

          @Override
          protected final void definition() {
            page.lang("en");
            page.title("Test Case 01");

            page.render(this::bodyImpl);
          }

          private void bodyImpl() {}
        },

        """
        <!DOCTYPE html>
        <html lang="en">
        <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Test Case 01</title>
        </head>
        <body></body>
        </html>
        """
    );
  }

  @Test
  public void testCase02() {
    test(
        new HtmlTemplate() {
          private final Ui ui = standard.create(this);

          private final UiPage page = ui.page();

          @Override
          protected final void definition() {
            page.lang("en");
            page.title("Test Case 02");

            page.render(this::bodyImpl);
          }

          private void bodyImpl() {
            div();
          }
        },

        """
        <!DOCTYPE html>
        <html lang="en">
        <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Test Case 02</title>
        </head>
        <body>
        <div></div>
        </body>
        </html>
        """
    );
  }

  private void test(HtmlTemplate template, String expectedOutput) {
    String result;
    result = template.toString();

    assertEquals(result, expectedOutput);
  }

}