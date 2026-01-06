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
import objectos.way.dev.Script010;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script010Test {

  @Test
  public void action() {
    assertEquals(
        Script010.ACTION.toString(),

        """
        ["W1",["EI",["JS","subject"]],["MO",["W1",["EI",["JS","src"]],["PR","Element","innerHTML"],["TY","string"]]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/010");

      var clickMe = page.locator("#click-me");

      var subject = page.locator("#subject");

      assertThat(subject).hasText("Before");

      clickMe.click();

      assertThat(subject).hasText("After");
    }
  }

}
