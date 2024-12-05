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

  @Test(description = "boxed: empty")
  public void parseBoxed01() {
    assertEquals(CssValueType.parse("[]"), CssValueType.BOXED);
  }

  @Test(description = "boxed: len=1")
  public void parseBoxed02() {
    assertEquals(CssValueType.parse("[b]"), CssValueType.BOXED);
  }

  @Test(description = "boxed: len>1")
  public void parseBoxed03() {
    assertEquals(CssValueType.parse("[foo]"), CssValueType.BOXED);
  }

  @Test(description = "boxed: box-shadow")
  public void parseBoxed04() {
    assertEquals(CssValueType.parse("[0_35px_60px_-15px_rgb(0_0_0_/_0.3)]"), CssValueType.BOXED);
  }

  @Test(description = "boxed: flex")
  public void parseBoxed05() {
    assertEquals(CssValueType.parse("[2_2_0%]"), CssValueType.BOXED);
  }

  @Test(description = "boxed: not boxed")
  public void parseBoxed06() {
    assertEquals(CssValueType.parse("["), CssValueType.TOKEN);
  }

  @Test(description = "boxed integer: len=1")
  public void parseBoxedInteger01() {
    assertEquals(CssValueType.parse("[1]"), CssValueType.BOXED_INTEGER);
  }

  @Test(description = "boxed integer: len>1")
  public void parseBoxedInteger02() {
    assertEquals(CssValueType.parse("[345]"), CssValueType.BOXED_INTEGER);
  }

  @Test(description = "boxed integer negative: len=1")
  public void parseBoxedIntegerNegative01() {
    assertEquals(CssValueType.parse("[-3]"), CssValueType.BOXED_INTEGER_NEGATIVE);
  }

  @Test(description = "boxed integer negative: len>1")
  public void parseBoxedIntegerNegative02() {
    assertEquals(CssValueType.parse("[-987]"), CssValueType.BOXED_INTEGER_NEGATIVE);
  }

  @Test(description = "boxed length: integer len=1")
  public void parseBoxedLength01() {
    assertEquals(CssValueType.parse("[1px]"), CssValueType.BOXED_LENGTH);
  }

  @Test(description = "boxed length: integer len>1")
  public void parseBoxedLength02() {
    assertEquals(CssValueType.parse("[123px]"), CssValueType.BOXED_LENGTH);
  }

  @Test(description = "boxed length: decimal")
  public void parseBoxedLength03() {
    assertEquals(CssValueType.parse("[5.6px]"), CssValueType.BOXED_LENGTH);
  }

  @Test(description = "boxed length negative: integer len=1")
  public void parseBoxedLengthNegative01() {
    assertEquals(CssValueType.parse("[-3px]"), CssValueType.BOXED_LENGTH_NEGATIVE);
  }

  @Test(description = "boxed length negative: integer len>1")
  public void parseBoxedLengthNegative02() {
    assertEquals(CssValueType.parse("[-345px]"), CssValueType.BOXED_LENGTH_NEGATIVE);
  }

  @Test(description = "boxed percentage: integer len=1")
  public void parseBoxedPercentage01() {
    assertEquals(CssValueType.parse("[4%]"), CssValueType.BOXED_PERCENTAGE);
  }

  @Test(description = "boxed percentage: integer len>1")
  public void parseBoxedPercentage02() {
    assertEquals(CssValueType.parse("[789%]"), CssValueType.BOXED_PERCENTAGE);
  }

  @Test(description = "boxed percentage: decimal")
  public void parseBoxedPercentage03() {
    assertEquals(CssValueType.parse("[1.23%]"), CssValueType.BOXED_PERCENTAGE);
  }

  @Test(description = "boxed zero: success")
  public void parseBoxedZero01() {
    assertEquals(CssValueType.parse("[0]"), CssValueType.BOXED_ZERO);
  }

  @Test(description = "token: empty")
  public void parseToken01() {
    assertEquals(CssValueType.parse(""), CssValueType.TOKEN);
  }

  @Test(description = "token: len=1")
  public void parseToken02() {
    assertEquals(CssValueType.parse("x"), CssValueType.TOKEN);
  }

  @Test(description = "token: len>1")
  public void parseToken03() {
    assertEquals(CssValueType.parse("block"), CssValueType.TOKEN);
  }

  @Test(description = "token length: zero")
  public void parseTokenLength01() {
    assertEquals(CssValueType.parse("0px"), CssValueType.TOKEN_LENGTH);
  }

  @Test(description = "token length: integer len=1")
  public void parseTokenLength02() {
    assertEquals(CssValueType.parse("3rem"), CssValueType.TOKEN_LENGTH);
  }

  @Test(description = "token length: integer len>1")
  public void parseTokenLength03() {
    assertEquals(CssValueType.parse("127vh"), CssValueType.TOKEN_LENGTH);
  }

  @Test(description = "token length: decimal fractional=1")
  public void parseTokenLength04() {
    assertEquals(CssValueType.parse("0.5px"), CssValueType.TOKEN_LENGTH);
  }

  @Test(description = "token length: decimal fractional>1")
  public void parseTokenLength05() {
    assertEquals(CssValueType.parse("12.345px"), CssValueType.TOKEN_LENGTH);
  }

}