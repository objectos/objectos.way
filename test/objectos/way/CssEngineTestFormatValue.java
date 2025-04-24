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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CssEngineTestFormatValue {

  private final CssEngine engine = CssEngine.create(engine -> {
    engine.noteSink(Y.noteSink());
  });

  @BeforeClass
  public void beforeClass() {
    engine.execute();
  }

  @Test(description = "arity: arity=2")
  public void arity02() {
    assertEquals(engine.formatValue("2px_dashed"), "2px dashed");
    assertEquals(engine.formatValue("1rem_solid"), "1rem solid");
    assertEquals(engine.formatValue("10px_5%"), "10px 5%");
  }

  @Test(description = "arity: arity=3")
  public void arity03() {
    assertEquals(engine.formatValue("4px_solid_red-50"), "4px solid var(--color-red-50)");
    assertEquals(engine.formatValue("thick_double_#32a1ce"), "thick double #32a1ce");
  }

  @Test(description = "arity: arity=4")
  public void arity04() {
    assertEquals(engine.formatValue("1px_0_3px_4px"), "1px 0 3px 4px");
  }

  @Test(description = "color: valid color replace with var expr")
  public void color01() {
    assertEquals(engine.formatValue("red-50"), "var(--color-red-50)");
  }

  @Test(description = "color: valid color with opacity")
  public void color02() {
    assertEquals(engine.formatValue("red-50/20"), "color-mix(in oklab, var(--color-red-50) 20%, transparent)");
  }

  @Test(description = "color: valid color with opacity + arity")
  public void color03() {
    assertEquals(engine.formatValue("red-50/48_dashed"), "color-mix(in oklab, var(--color-red-50) 48%, transparent) dashed");
    assertEquals(engine.formatValue("4px_solid_red-50/20"), "4px solid color-mix(in oklab, var(--color-red-50) 20%, transparent)");
  }

  @Test(description = "dimension: zero")
  public void dimension01() {
    assertEquals(engine.formatValue("0rem"), "0rem");
  }

  @Test(description = "dimension: integer len=1")
  public void dimension02() {
    assertEquals(engine.formatValue("3rem"), "3rem");
  }

  @Test(description = "dimension: integer len>1")
  public void dimension03() {
    assertEquals(engine.formatValue("127vh"), "127vh");
  }

  @Test(description = "dimension: decimal fractional=1")
  public void dimension04() {
    assertEquals(engine.formatValue("0.5rem"), "0.5rem");
  }

  @Test(description = "dimension: decimal fractional>1")
  public void dimension05() {
    assertEquals(engine.formatValue("12.345s"), "12.345s");
  }

  @Test(description = "dimension: calc")
  public void dimension06() {
    assertEquals(engine.formatValue("calc(100%_-_192px)"), "calc(100% - 192px)");
  }

  @Test(description = "keyword: len=1")
  public void keyword01() {
    assertEquals(engine.formatValue("z"), "z");
  }

  @Test(description = "keyword: len>1")
  public void keyword02() {
    assertEquals(engine.formatValue("inherit"), "inherit");
  }

  @Test(description = "keyword: len>1 + hyphen")
  public void keyword03() {
    assertEquals(engine.formatValue("revert-layer"), "revert-layer");
  }

  @Test(description = "keyword: negative should have no effect")
  public void keyword04() {
    assertEquals(engine.formatValue("z"), "z");
    assertEquals(engine.formatValue("inherit"), "inherit");
    assertEquals(engine.formatValue("revert-layer"), "revert-layer");
  }

  @Test(description = "keyword: looks like color but no match")
  public void keyword05() {
    assertEquals(engine.formatValue("foo-400/20"), "foo-400/20");
  }

  @Test(description = "percentage: integer len=1")
  public void percentage01() {
    assertEquals(engine.formatValue("4%"), "4%");
  }

  @Test(description = "percentage: integer len>1")
  public void percentage02() {
    assertEquals(engine.formatValue("789%"), "789%");
  }

  @Test(description = "percentage: decimal")
  public void percentage03() {
    assertEquals(engine.formatValue("1.23%"), "1.23%");
  }

  @Test(description = "ratio: integer/integer")
  public void ratio01() {
    assertEquals(engine.formatValue("16/9"), "16/9");
  }

  @Test(description = "rx: zero")
  public void rx01() {
    assertEquals(engine.formatValue("0rx"), "calc(0 / var(--rx) * 1rem)");
  }

  @Test(description = "rx: integer")
  public void rx02() {
    assertEquals(engine.formatValue("32rx"), "calc(32 / var(--rx) * 1rem)");
  }

  @Test(description = "rx: decimal")
  public void rx03() {
    assertEquals(engine.formatValue("144.5rx"), "calc(144.5 / var(--rx) * 1rem)");
  }

  @Test(description = "rx: looks like rx but it is not")
  public void rx04() {
    assertEquals(engine.formatValue("100ry"), "100ry");
    assertEquals(engine.formatValue("100rxx"), "100rxx");
    assertEquals(engine.formatValue("100rpx"), "100rpx");
  }

  @Test(description = "rx: arity 2")
  public void rx05() {
    assertEquals(engine.formatValue("10rx_solid"), "calc(10 / var(--rx) * 1rem) solid");
    assertEquals(engine.formatValue("10px_20rx"), "10px calc(20 / var(--rx) * 1rem)");
  }

  @Test(description = "rx: negative value")
  public void rx06() {
    assertEquals(engine.formatValue("-10rx"), "calc(-10 / var(--rx) * 1rem)");
  }

  @Test(description = "rx: calc")
  public void rx07() {
    assertEquals(engine.formatValue("calc(100%_-_192rx)"), "calc(100% - calc(192 / var(--rx) * 1rem))");
  }

  @Test(description = "slash")
  public void slash01() {
    assertEquals(engine.formatValue("10px/20px"), "10px/20px");
  }

}