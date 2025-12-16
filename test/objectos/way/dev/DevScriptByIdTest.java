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
package objectos.way.dev;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import objectos.way.Y;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class DevScriptByIdTest {

  @Test(description = """
  IV: invoke virtual
  - args = empty
  """)
  public void invoke0() {
    try (var page = Y.page()) {
      page.navigate("/script/by-id/invoke0");

      var clickMe = page.locator("#click-me");
      var subject = page.locator("#subject");

      assertThat(subject).hasCount(1);

      clickMe.click();

      assertThat(subject).hasCount(0);
    }
  }

  @Test(description = """
  IV: invoke virtual
  - args = 1
  """)
  public void invoke1() {
    try (var page = Y.page()) {
      page.navigate("/script/by-id/invoke1");

      var clickMe = page.locator("#click-me");
      var subject = page.locator("#subject");

      assertThat(subject).hasCSS("width", "64px");

      clickMe.click();

      assertThat(subject).not().hasCSS("width", "64px");
    }
  }

  @Test(description = """
  PR/PW: property read/write
  """)
  public void property0() {
    try (var page = Y.page()) {
      page.navigate("/script/by-id/property0");

      var clickMe = page.locator("#click-me");
      var subject = page.locator("#subject");

      assertThat(subject).hasText("My ID is...");

      clickMe.click();

      assertThat(subject).hasText("click-me");
    }
  }

}
