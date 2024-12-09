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

import org.testng.annotations.Test;

public class CssValueTypeTest {

  @Test(description = "string: empty")
  public void parseString01() {
    assertEquals(CssValueType.parse(""), CssValueType.STRING);
  }

  @Test(description = "string: len=1")
  public void parseString02() {
    assertEquals(CssValueType.parse("x"), CssValueType.STRING);
  }

  @Test(description = "string: len>1")
  public void parseString03() {
    assertEquals(CssValueType.parse("block"), CssValueType.STRING);
  }

  @Test(description = "string: box-shadow")
  public void parseString04() {
    assertEquals(CssValueType.parse("0_35px_60px_-15px_rgb(0_0_0_/_0.3)"), CssValueType.STRING);
  }

  @Test(description = "string: flex")
  public void parseString05() {
    assertEquals(CssValueType.parse("2_2_0%"), CssValueType.STRING);
  }

  @Test(description = "dimension: zero")
  public void parseDimension01() {
    assertEquals(CssValueType.parse("0rem"), CssValueType.DIMENSION);
  }

  @Test(description = "dimension: integer len=1")
  public void parseDimension02() {
    assertEquals(CssValueType.parse("3rem"), CssValueType.DIMENSION);
  }

  @Test(description = "dimension: integer len>1")
  public void parseDimension03() {
    assertEquals(CssValueType.parse("127vh"), CssValueType.DIMENSION);
  }

  @Test(description = "dimension: decimal fractional=1")
  public void parseDimension04() {
    assertEquals(CssValueType.parse("0.5rem"), CssValueType.DIMENSION);
  }

  @Test(description = "dimension: decimal fractional>1")
  public void parseDimension05() {
    assertEquals(CssValueType.parse("12.345s"), CssValueType.DIMENSION);
  }

  @Test(description = "percentage: integer len=1")
  public void parsePercentage01() {
    assertEquals(CssValueType.parse("4%"), CssValueType.PERCENTAGE);
  }

  @Test(description = "percentage: integer len>1")
  public void parsePercentage02() {
    assertEquals(CssValueType.parse("789%"), CssValueType.PERCENTAGE);
  }

  @Test(description = "percentage: decimal")
  public void parsePercentage03() {
    assertEquals(CssValueType.parse("1.23%"), CssValueType.PERCENTAGE);
  }

  @Test(description = "integer: len=1")
  public void parseInteger01() {
    assertEquals(CssValueType.parse("1"), CssValueType.INTEGER);
  }

  @Test(description = "integer: len>1")
  public void parseInteger02() {
    assertEquals(CssValueType.parse("345"), CssValueType.INTEGER);
  }

  @Test(description = "zero: success")
  public void parseZero01() {
    assertEquals(CssValueType.parse("0"), CssValueType.ZERO);
  }

}