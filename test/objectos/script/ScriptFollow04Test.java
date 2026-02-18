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
import objectos.way.dev.ScriptFollow04;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class ScriptFollow04Test {

  @Test
  public void action() {
    assertEquals(
        ScriptFollow04.ACTION.toString(),

        """
        ["FO",["HI",false]]"""
    );
  }

  @Test
  public void actionTrue() {
    assertEquals(
        Js.follow(opts -> opts.history(true)).toString(),

        """
        ["FO"]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/follow/04");

      var clickMe = page.locator("#click-me");
      var subject = page.locator("#subject");

      assertThat(subject).hasText("Before");
      assertThat(page).hasURL("/script/follow/04");

      clickMe.click();

      assertThat(subject).hasText("After");
      assertThat(page).hasURL("/script/follow/04");
    }
  }

}
