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
import objectos.way.dev.Script026;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script026Test {

  @Test
  public void action() {
    assertEquals(
        Script026.ACTION.toString(),

        """
        ["NA"]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/026");

      var clickMe = page.locator("#click-me");
      var subject = page.locator("#subject");
      var remElem = page.locator("meta[name=initial]");

      assertThat(page).hasTitle("026: initial");
      assertThat(subject).hasText("Before");
      assertThat(remElem).hasCount(1);

      clickMe.click();

      assertThat(page).hasTitle("026: next");
      assertThat(subject).hasText("After");
      assertThat(remElem).hasCount(0);
    }
  }

}
