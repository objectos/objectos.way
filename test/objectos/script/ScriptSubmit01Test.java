/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.script;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

import objectos.way.Y;
import objectos.way.dev.ScriptSubmit01;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class ScriptSubmit01Test {

  @Test
  public void action() {
    assertEquals(
        ScriptSubmit01.ACTION.toString(),

        """
        ["SU"]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/submit/01");

      var search = page.locator("#search");
      var clickMe = page.locator("#click-me");
      var subject = page.locator("#subject");
      var items = page.locator("#subject li");
      var url = page.url();

      assertThat(subject).hasText("AAA BBB CCC");
      assertThat(items).hasCount(3);

      search.fill("BB");

      clickMe.click();

      assertThat(subject).hasText("BBB");
      assertThat(items).hasCount(1);
      assertEquals(page.url(), url + "?search=BB");
    }
  }

}
