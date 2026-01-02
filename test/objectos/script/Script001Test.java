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
import objectos.way.dev.Script001;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script001Test {

  @Test
  public void action() {
    assertEquals(
        Script001.ACTION.toString(),

        """
        [["ET"],["IV","Element","removeAttribute",[[["JS","style"]]]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/001");

      var div1 = page.locator("#div-1");
      var div2 = page.locator("#div-2");
      var div3 = page.locator("#div-3");

      assertThat(div1).hasCSS("height", "64px");
      assertThat(div2).hasCSS("height", "64px");
      assertThat(div3).hasCSS("height", "64px");

      div2.click();

      assertThat(div1).hasCSS("height", "64px");
      assertThat(div2).not().hasCSS("height", "64px");
      assertThat(div3).hasCSS("height", "64px");
    }
  }

}
