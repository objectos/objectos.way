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

  @Test(description = "arity: arity=2")
  public void arity01() {
    assertEquals(engine.formatValue(false, "2px_dashed"), "0.125rem dashed");
    assertEquals(engine.formatValue(false, "1rem_solid"), "1rem solid");
  }

  @Test(description = "arity: arity=3")
  public void arity02() {
    assertEquals(engine.formatValue(false, "4px_solid_red-50"), "0.25rem solid var(--color-red-50)");
    assertEquals(engine.formatValue(false, "thick_double_#32a1ce"), "thick double #32a1ce");
  }

  @Test(description = "color: valid color replace with var expr")
  public void color01() {
    assertEquals(engine.formatValue(false, "red-50"), "var(--color-red-50)");
  }

  @Test(description = "dimension: zero")
  public void dimension01() {
    assertEquals(engine.formatValue(false, "0rem"), "0rem");
  }

  @Test(description = "dimension: integer len=1")
  public void dimension02() {
    assertEquals(engine.formatValue(false, "3rem"), "3rem");
  }

  @Test(description = "dimension: integer len>1")
  public void dimension03() {
    assertEquals(engine.formatValue(false, "127vh"), "127vh");
  }

  @Test(description = "dimension: decimal fractional=1")
  public void dimension04() {
    assertEquals(engine.formatValue(false, "0.5rem"), "0.5rem");
  }

  @Test(description = "dimension: decimal fractional>1")
  public void dimension05() {
    assertEquals(engine.formatValue(false, "12.345s"), "12.345s");
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

  @Test(description = "pixel: zero")
  public void pixel01() {
    assertEquals(engine.formatValue(false, "0px"), "0px");
  }

  @Test(description = "pixel: value < 16px")
  public void pixel02() {
    assertEquals(engine.formatValue(false, "0.16px"), "0.01rem");
    assertEquals(engine.formatValue(false, "1px"), "0.0625rem");
    assertEquals(engine.formatValue(false, "2px"), "0.125rem");
    assertEquals(engine.formatValue(false, "8px"), "0.5rem");
    assertEquals(engine.formatValue(false, "10px"), "0.625rem");
  }

  @Test(description = "pixel: value >= 16px")
  public void pixel03() {
    assertEquals(engine.formatValue(false, "16px"), "1rem");
    assertEquals(engine.formatValue(false, "44px"), "2.75rem");
    assertEquals(engine.formatValue(false, "48px"), "3rem");
  }

  @Test(description = "raio: integer/integer")
  public void ratio01() {
    assertEquals(engine.formatValue(false, "16/9"), "16/9");
  }

}