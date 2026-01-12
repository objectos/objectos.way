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
import objectos.way.dev.Script024;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script024Test {

  @Test
  public void action() {
    assertEquals(
        Script024.ACTION.toString(),

        """
        ["WS",["CW","literal",["JS",{"p1":123,"p2":true,"p3":"third property"}]],["W1",["EI",["JS","div-1"]],["PW","Node","textContent",["W1",["CR","literal"],["pr","p1"],["IU","toString",[]]]]],["W1",["EI",["JS","div-2"]],["PW","Node","textContent",["W1",["CR","literal"],["pr","p2"],["IU","toString",[]]]]],["W1",["EI",["JS","div-3"]],["PW","Node","textContent",["W1",["CR","literal"],["pr","p3"],["IU","toString",[]]]]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/024");

      var clickMe = page.locator("#click-me");
      var div1 = page.locator("#div-1");
      var div2 = page.locator("#div-2");
      var div3 = page.locator("#div-3");

      assertThat(div1).hasText("Div 1");
      assertThat(div2).hasText("Div 2");
      assertThat(div3).hasText("Div 3");

      clickMe.click();

      assertThat(div1).hasText("123");
      assertThat(div2).hasText("true");
      assertThat(div3).hasText("third property");
    }
  }

}
