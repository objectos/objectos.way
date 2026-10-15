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
package objectox.html;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class IntSizeTest {

  @Test(
      description = "reject negative value",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: negative values \\(-1\\) are now allowed")
  public void of01() {
    IntSize.of(-1);
  }

  @Test(description = "zero is BIT8")
  public void of02() {
    final IntSize res;
    res = IntSize.of(0);

    assertEquals(res, IntSize.BIT8);
  }

  @Test(description = "max BIT8 value")
  public void of03() {
    final IntSize res;
    res = IntSize.of(255);

    assertEquals(res, IntSize.BIT8);
  }

  @Test(description = "min BIT16 value")
  public void of04() {
    final IntSize res;
    res = IntSize.of(256);

    assertEquals(res, IntSize.BIT16);
  }

  @Test(description = "max BIT16 value")
  public void of05() {
    final IntSize res;
    res = IntSize.of(65535);

    assertEquals(res, IntSize.BIT16);
  }

  @Test(description = "min BIT24 value")
  public void of06() {
    final IntSize res;
    res = IntSize.of(65536);

    assertEquals(res, IntSize.BIT24);
  }

  @Test(description = "max BIT24 value")
  public void of07() {
    final IntSize res;
    res = IntSize.of(16777215);

    assertEquals(res, IntSize.BIT24);
  }

  @Test(
      description = "reject too large value",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: \\(16777216\\) is too large")
  public void of08() {
    IntSize.of(16777216);
  }

  @Test(description = "BIT8 skip 0")
  public void skip01() {
    final int res;
    res = IntSize.BIT8.skip(0);

    assertEquals(res, 1);
  }

  @Test(description = "BIT8 skip non-zero")
  public void skip02() {
    final int res;
    res = IntSize.BIT8.skip(5);

    assertEquals(res, 6);
  }

  @Test(description = "BIT16 skip 0")
  public void skip03() {
    final int res;
    res = IntSize.BIT16.skip(0);

    assertEquals(res, 2);
  }

  @Test(description = "BIT16 skip non-zero")
  public void skip04() {
    final int res;
    res = IntSize.BIT16.skip(5);

    assertEquals(res, 7);
  }

  @Test(description = "BIT24 skip 0")
  public void skip05() {
    final int res;
    res = IntSize.BIT24.skip(0);

    assertEquals(res, 3);
  }

  @Test(description = "BIT24 skip non-zero")
  public void skip06() {
    final int res;
    res = IntSize.BIT24.skip(5);

    assertEquals(res, 8);
  }

  @Test(description = "equal values are BIT8")
  public void max01() {
    final IntSize res;
    res = IntSize.max(1, 1);

    assertEquals(res, IntSize.BIT8);
  }

  @Test(description = "max BIT8 value in a")
  public void max02() {
    final IntSize res;
    res = IntSize.max(255, 0);

    assertEquals(res, IntSize.BIT8);
  }

  @Test(description = "max BIT8 value in b")
  public void max03() {
    final IntSize res;
    res = IntSize.max(0, 255);

    assertEquals(res, IntSize.BIT8);
  }

  @Test(description = "max BIT16 value in b")
  public void max04() {
    final IntSize res;
    res = IntSize.max(255, 256);

    assertEquals(res, IntSize.BIT16);
  }

  @Test(description = "max BIT16 value in a")
  public void max05() {
    final IntSize res;
    res = IntSize.max(256, 255);

    assertEquals(res, IntSize.BIT16);
  }

  @Test(description = "max BIT16 value in a and b")
  public void max06() {
    final IntSize res;
    res = IntSize.max(65535, 65535);

    assertEquals(res, IntSize.BIT16);
  }

  @Test(description = "max BIT24 value in b")
  public void max07() {
    final IntSize res;
    res = IntSize.max(65535, 65536);

    assertEquals(res, IntSize.BIT24);
  }

  @Test(description = "max BIT24 value in a and b")
  public void max08() {
    final IntSize res;
    res = IntSize.max(65536, 16777215);

    assertEquals(res, IntSize.BIT24);
  }

  @Test(description = "zero values are BIT8")
  public void max09() {
    final IntSize res;
    res = IntSize.max(0, 0);

    assertEquals(res, IntSize.BIT8);
  }

  @Test(
      description = "reject negative a and b",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: negative values \\(-1\\) are now allowed")
  public void max10() {
    IntSize.max(-1, -1);
  }

  @Test(
      description = "reject negative maximum",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: negative values \\(-1\\) are now allowed")
  public void max11() {
    IntSize.max(-5, -1);
  }

  @Test(description = "negative a ignored when b is greater")
  public void max12() {
    final IntSize res;
    res = IntSize.max(-1, 255);

    assertEquals(res, IntSize.BIT8);
  }

  @Test(
      description = "reject too large a",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: \\(16777216\\) is too large")
  public void max13() {
    IntSize.max(16777216, 0);
  }

  @Test(
      description = "reject too large b",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: \\(16777216\\) is too large")
  public void max14() {
    IntSize.max(0, 16777216);
  }

}
