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

import static org.testng.Assert.assertEquals;
import objectos.way.Y;
import objectos.way.dev.ScriptJsHistory00;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class ScriptJsHistory00Test {

  @Test
  public void action() {
    assertEquals(
        ScriptJsHistory00.ACTION.toString(),

        """
        ["W1",["GR"],["TY","Window"],["pr","history"],["IV","History","pushState",[["JS",{}],["JS",""],["JS","#history-test"]]]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      page.navigate("/script/history/00");

      var before = (String) page.evaluate("() => location.hash");

      assertEquals(before, "");

      var clickMe = page.locator("#click-me");

      clickMe.click();

      var after = (String) page.evaluate("() => location.hash");

      assertEquals(after, "#history-test");
    }
  }

}
