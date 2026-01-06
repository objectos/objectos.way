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
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import objectos.way.Y;
import objectos.way.dev.Script020;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Y.class)
public class Script020Test {

  @Test
  public void action() {
    assertEquals(
        Script020.ACTION.toString(),

        """
        ["TE",["JS","The TE action"]]"""
    );
  }

  @Test
  public void live() {
    try (var page = Y.page()) {
      var msgs = new ArrayList<String>();

      page.onPageError(msgs::add);

      page.navigate("/script/020");

      assertEquals(msgs.size(), 0);

      var clickMe = page.locator("#click-me");

      clickMe.click();

      assertEquals(msgs.size(), 1);

      var msg0 = msgs.get(0);

      assertTrue(msg0.contains("The TE action"));
    }
  }

}
