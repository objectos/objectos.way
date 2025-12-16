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

import objectos.way.dev.Script000;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script000Test {

  @Test
  public void action() {
    assertEquals(
        Script000.ACTION.toString(),

        """
        [["LO","TT"],["IV","Element","remove",[]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/000");

      var btn1 = page.locator("#btn-1");
      var btn2 = page.locator("#btn-2");
      var btn3 = page.locator("#btn-3");

      assertThat(btn1).hasCount(1);
      assertThat(btn2).hasCount(1);
      assertThat(btn3).hasCount(1);

      btn2.click();

      assertThat(btn1).hasCount(1);
      assertThat(btn2).hasCount(0);
      assertThat(btn3).hasCount(1);
    }
  }

}
