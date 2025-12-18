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
package objectos.way;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

import objectos.way.dev.Script006;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script006Test {

  @Test
  public void action() {
    assertEquals(
        Script006.ACTION.toString(),

        """
        [\
        ["CW","el",["LO","ID","subject"]],\
        [["CR","el"],["PR","Element","classList"],["IV","DOMTokenList","toggle",[["JS","height:32px"],["JS","height:64px"]]]]\
        ]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/006");

      var clickMe = page.locator("#click-me");
      var subject = page.locator("#subject");

      assertThat(subject).hasCSS("height", "32px");

      clickMe.click();

      assertThat(subject).hasCSS("height", "64px");
    }
  }

}
