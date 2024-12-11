/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CssEngineTestFormatValue {

  private final CssEngine engine = new CssEngine();

  @BeforeClass
  public void beforeClass() {
    engine.execute();
  }

  @Test(description = "color: valid color replace with var expr")
  public void color01() {
    assertEquals(engine.formatValue(false, "red-50"), "var(--color-red-50)");
  }

  @Test(description = "keyword: len=1")
  public void keyword01() {
    assertEquals(engine.formatValue(false, "z"), "z");
  }

  @Test(description = "keyword: len>1")
  public void keyword02() {
    assertEquals(engine.formatValue(false, "inherit"), "inherit");
  }

  @Test(description = "keyword: len>1 + hyphen")
  public void keyword03() {
    assertEquals(engine.formatValue(false, "revert-layer"), "revert-layer");
  }

  @Test(description = "keyword: negative should have no effect")
  public void keyword04() {
    assertEquals(engine.formatValue(true, "z"), "z");
    assertEquals(engine.formatValue(true, "inherit"), "inherit");
    assertEquals(engine.formatValue(true, "revert-layer"), "revert-layer");
  }

  @Test
  public void ratio01() {
    assertEquals(engine.formatValue(false, "16/9"), "16/9");
  }

}