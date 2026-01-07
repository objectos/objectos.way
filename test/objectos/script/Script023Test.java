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
import objectos.way.dev.Script023;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script023Test {

  @Test
  public void action() {
    assertEquals(
        Script023.ACTION.toString(),

        """
        ["W1",["GR"],["IU","fetch",[["W1",["ET"],["IV","Element","getAttribute",[["JS","href"]]]]]],["IV","Promise","then",[["FN",["WS",["CW","resp",["W1",["AX",0],["TY","Response"]]],["W1",["CR","resp"],["TY","Response"],["PR","Response","ok"],["TY","boolean"],["IF",["W1",["CR","resp"],["TY","Response"],["IV","Response","text",[]],["IV","Promise","then",[["FN",["W1",["GR"],["PR","Window","document"],["TY","Document"],["PR","Document","documentElement"],["TY","Element"],["MO",["W1",["AX",0],["TY","string"]]]]]]]],["TE",["JS","Resp not OK!"]]]]]]]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/023");

      var clickMe = page.locator("#click-me");
      var keepMe = page.locator("#keep-me");
      var subject = page.locator("#subject");

      assertThat(keepMe).hasCount(1);
      assertThat(subject).hasText("Before");

      clickMe.click();

      assertThat(keepMe).hasCount(1);
      assertThat(subject).hasText("After");
    }
  }

}
