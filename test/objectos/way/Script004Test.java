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
package objectos.way;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

import objectos.way.dev.Script004;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script004Test {

  @Test
  public void action() {
    assertEquals(
        Script004.ACTION.toString(),

        """
        [["EI",[["JS","subject"]]],["IV","Element","removeAttribute",[[["JS","style"]]]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/004");

      var clickMe = page.locator("#click-me");
      var subject = page.locator("#subject");

      assertThat(subject).hasCSS("width", "64px");

      clickMe.click();

      assertThat(subject).not().hasCSS("width", "64px");
    }
  }

}
