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
import objectos.way.dev.ScriptSubmit02;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class ScriptSubmit02Test {

  @Test
  public void action() {
    assertEquals(
        ScriptSubmit02.ACTION.toString(),

        """
        ["SU",["SE",["EI",["JS","bottom"]]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/submit/02");

      var clickMe = page.locator("#click-me");

      clickMe.scrollIntoViewIfNeeded();

      var top = page.locator("#top");
      var bot = page.locator("#bottom");

      assertThat(top).hasText("top:before");
      assertThat(bot).hasText("bottom:before");
      assertThat(top).not().isInViewport();
      assertThat(bot).not().isInViewport();

      clickMe.click();

      assertThat(top).hasText("top:after");
      assertThat(bot).hasText("bottom:after");
      assertThat(top).not().isInViewport();
      assertThat(bot).isInViewport();
    }
  }

}
