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
  public void arity02() {
    assertEquals(engine.formatValue(false, "2px_dashed"), "2px dashed");
    assertEquals(engine.formatValue(false, "1rem_solid"), "1rem solid");
    assertEquals(engine.formatValue(false, "10px_5%"), "10px 5%");
  }

  @Test(description = "arity: arity=3")
  public void arity03() {
    assertEquals(engine.formatValue(false, "4px_solid_red-50"), "4px solid var(--color-red-50)");
    assertEquals(engine.formatValue(false, "thick_double_#32a1ce"), "thick double #32a1ce");
  }

  @Test(description = "arity: arity=4")
  public void arity04() {
    assertEquals(engine.formatValue(false, "1px_0_3px_4px"), "1px 0 3px 4px");
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

  @Test(description = "percentage: integer len=1")
  public void percentage01() {
    assertEquals(engine.formatValue(false, "4%"), "4%");
  }

  @Test(description = "percentage: integer len>1")
  public void percentage02() {
    assertEquals(engine.formatValue(false, "789%"), "789%");
  }

  @Test(description = "percentage: decimal")
  public void percentage03() {
    assertEquals(engine.formatValue(false, "1.23%"), "1.23%");
  }

  @Test(description = "ratio: integer/integer")
  public void ratio01() {
    assertEquals(engine.formatValue(false, "16/9"), "16/9");
  }

  @Test(description = "rx: zero")
  public void rx01() {
    assertEquals(engine.formatValue(false, "0rx"), "calc(0px / var(--rx) * 1rem)");
  }

  @Test(description = "rx: integer")
  public void rx02() {
    assertEquals(engine.formatValue(false, "32rx"), "calc(32px / var(--rx) * 1rem)");
  }

  @Test(description = "rx: decimal")
  public void rx03() {
    assertEquals(engine.formatValue(false, "144.5rx"), "calc(144.5px / var(--rx) * 1rem)");
  }

  @Test(description = "rx: looks like rx but it is not")
  public void rx04() {
    assertEquals(engine.formatValue(false, "100ry"), "100ry");
    assertEquals(engine.formatValue(false, "100rxx"), "100rxx");
    assertEquals(engine.formatValue(false, "100rpx"), "100rpx");
  }

  @Test(description = "rx: arity 2")
  public void rx05() {
    assertEquals(engine.formatValue(false, "10rx_solid"), "calc(10px / var(--rx) * 1rem) solid");
    assertEquals(engine.formatValue(false, "10px_20rx"), "10px calc(20px / var(--rx) * 1rem)");
  }

  @Test(description = "slash")
  public void slash01() {
    assertEquals(engine.formatValue(false, "10px/20px"), "10px/20px");
  }

}