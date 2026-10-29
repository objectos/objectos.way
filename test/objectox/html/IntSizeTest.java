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

import objectox.html.markup.HtmlBytes;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class IntSizeTest {

  @DataProvider
  public Object[][] max01Provider() {
    return new Object[][] {
        {1, 1, IntSize.BIT8},
        {255, 0, IntSize.BIT8},
        {0, 255, IntSize.BIT8},
        {255, 256, IntSize.BIT16},
        {256, 255, IntSize.BIT16},
        {65535, 65535, IntSize.BIT16},
        {65535, 65536, IntSize.BIT24},
        {65536, 16777215, IntSize.BIT24},
        {0, 0, IntSize.BIT8},
        {-1, 255, IntSize.BIT8}
    };
  }

  @Test(dataProvider = "max01Provider", description = "returns the size of the greater of the two values")
  public void max01(int a, int b, IntSize expected) {
    final IntSize res;
    res = IntSize.max(a, b);

    assertEquals(res, expected);
  }

  @Test(
      description = "reject negative a and b",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: negative values \\(-1\\) are now allowed")
  public void max02() {
    IntSize.max(-1, -1);
  }

  @Test(
      description = "reject negative maximum",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: negative values \\(-1\\) are now allowed")
  public void max03() {
    IntSize.max(-5, -1);
  }

  @Test(
      description = "reject too large a",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: \\(16777216\\) is too large")
  public void max04() {
    IntSize.max(16777216, 0);
  }

  @Test(
      description = "reject too large b",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: \\(16777216\\) is too large")
  public void max05() {
    IntSize.max(0, 16777216);
  }

  @DataProvider
  public Object[][] of01Provider() {
    return new Object[][] {
        {0, IntSize.BIT8},
        {255, IntSize.BIT8},
        {256, IntSize.BIT16},
        {65535, IntSize.BIT16},
        {65536, IntSize.BIT24},
        {16777215, IntSize.BIT24}
    };
  }

  @Test(dataProvider = "of01Provider", description = "returns the size associated with the value")
  public void of01(int value, IntSize expected) {
    final IntSize res;
    res = IntSize.of(value);

    assertEquals(res, expected);
  }

  @Test(
      description = "reject negative value",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: negative values \\(-1\\) are now allowed")
  public void of02() {
    IntSize.of(-1);
  }

  @Test(
      description = "reject too large value",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: \\(16777216\\) is too large")
  public void of03() {
    IntSize.of(16777216);
  }

  @DataProvider
  public Object[][] skip01Provider() {
    return new Object[][] {
        {IntSize.BIT8, 0, 1},
        {IntSize.BIT8, 5, 6},
        {IntSize.BIT16, 0, 2},
        {IntSize.BIT16, 5, 7},
        {IntSize.BIT24, 0, 3},
        {IntSize.BIT24, 5, 8}
    };
  }

  @Test(dataProvider = "skip01Provider", description = "returns the index incremented by the number of bytes the size occupies")
  public void skip01(IntSize size, int index, int expected) {
    final int res;
    res = size.skip(index);

    assertEquals(res, expected);
  }

  @Test(description = "BIT8 is ELEMSTR8")
  public void toRegularStartTagByte01() {
    final byte res;
    res = IntSize.BIT8.toRegularStartTagByte();

    assertEquals(res, HtmlBytes.ELEMSTR8);
  }

  @Test(
      description = "BIT16 is unsupported",
      expectedExceptions = UnsupportedOperationException.class,
      expectedExceptionsMessageRegExp = "toRegularStartTagByte: BIT16")
  public void toRegularStartTagByte02() {
    IntSize.BIT16.toRegularStartTagByte();
  }

  @Test(
      description = "BIT24 is unsupported",
      expectedExceptions = UnsupportedOperationException.class,
      expectedExceptionsMessageRegExp = "toRegularStartTagByte: BIT24")
  public void toRegularStartTagByte03() {
    IntSize.BIT24.toRegularStartTagByte();
  }

  @DataProvider
  public Object[][] toTestableCellByteProvider() {
    return new Object[][] {
        {IntSize.BIT8, HtmlBytes.TCELL8},
        {IntSize.BIT16, HtmlBytes.TCELL16},
        {IntSize.BIT24, HtmlBytes.TCELL24}
    };
  }

  @Test(dataProvider = "toTestableCellByteProvider", description = "each size is associated with its TCELL opcode")
  public void toTestableCellByte(IntSize size, byte expected) {
    final byte res;
    res = size.toTestableCellByte();

    assertEquals(res, expected);
  }

  @DataProvider
  public Object[][] toTestableHeadingByteProvider() {
    return new Object[][] {
        {IntSize.BIT8, HtmlBytes.THEADING8},
        {IntSize.BIT16, HtmlBytes.THEADING16},
        {IntSize.BIT24, HtmlBytes.THEADING24}
    };
  }

  @Test(dataProvider = "toTestableHeadingByteProvider", description = "each size is associated with its THEADING opcode")
  public void toTestableHeadingByte(IntSize size, byte expected) {
    final byte res;
    res = size.toTestableHeadingByte();

    assertEquals(res, expected);
  }

  @DataProvider
  public Object[][] toValuesByteProvider() {
    return new Object[][] {
        {IntSize.BIT8, HtmlBytes.VALUES8},
        {IntSize.BIT16, HtmlBytes.VALUES16},
        {IntSize.BIT24, HtmlBytes.VALUES24}
    };
  }

  @Test(dataProvider = "toValuesByteProvider", description = "each size is associated with its VALUES opcode")
  public void toValuesByte(IntSize size, byte expected) {
    final byte res;
    res = size.toValuesByte();

    assertEquals(res, expected);
  }

  @Test(description = "BIT8 is ELEMVOID8")
  public void toVoidStartTagByte01() {
    final byte res;
    res = IntSize.BIT8.toVoidStartTagByte();

    assertEquals(res, HtmlBytes.ELEMVOID8);
  }

  @Test(
      description = "BIT16 is unsupported",
      expectedExceptions = UnsupportedOperationException.class,
      expectedExceptionsMessageRegExp = "toVoidStartTagByte: BIT16")
  public void toVoidStartTagByte02() {
    IntSize.BIT16.toVoidStartTagByte();
  }

  @Test(
      description = "BIT24 is unsupported",
      expectedExceptions = UnsupportedOperationException.class,
      expectedExceptionsMessageRegExp = "toVoidStartTagByte: BIT24")
  public void toVoidStartTagByte03() {
    IntSize.BIT24.toVoidStartTagByte();
  }

}
