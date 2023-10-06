/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import objectox.lang.Check;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ByteArraysTest {

  @Test
  public void emptyByteArray() {
    assertEquals(ByteArrays.empty().length, 0);
  }

  @Test
  public void growIfNecessary() {
    var bytes = new byte[3];

    var bytesNoGrowthRequired = ByteArrays.growIfNecessary(bytes, 2);

    assertSame(bytesNoGrowthRequired, bytes);

    assertEquals(bytesNoGrowthRequired.length, 3);

    var bytesGrowthRequired = ByteArrays.growIfNecessary(bytes, 3);

    assertNotSame(bytesGrowthRequired, bytes);

    assertTrue(bytesGrowthRequired.length > bytes.length);

    try {
      ByteArrays.growIfNecessary(bytes, -1);

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
  }

  @Test
  public void toHexString() {
    toHexStringImpl("");
    toHexStringImpl("00", 0x00);
    toHexStringImpl("01", 0x01);
    toHexStringImpl("0123", 0x01, 0x23);
    toHexStringImpl("012345", 0x01, 0x23, 0x45);
    toHexStringImpl("01234567", 0x01, 0x23, 0x45, 0x67);
    toHexStringImpl("0123456789", 0x01, 0x23, 0x45, 0x67, 0x89);
    toHexStringImpl("0123456789ab", 0x01, 0x23, 0x45, 0x67, 0x89, 0xab);
    toHexStringImpl("0123456789abcd", 0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd);
    toHexStringImpl("0123456789abcdef", 0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd, 0xef);

    byte[] longBytes;
    longBytes = new byte[8];

    for (int i = 0; i < 1024; i++) {
      ThreadLocalRandom.current().nextBytes(longBytes);

      long longValue;
      longValue = longValue(longBytes);

      String expected;
      expected = Long.toHexString(longValue);

      int pad;
      pad = 16 - expected.length();

      char[] zeroes;
      zeroes = new char[pad];

      Arrays.fill(zeroes, '0');

      assertEquals(ByteArrays.toHexString(longBytes), new String(zeroes) + expected);
    }

    try {
      ByteArrays.toHexString(null);

      Assert.fail();
    } catch (NullPointerException expected) {

    }
  }

  private static final int BYTE_MASK = 0xFF;

  private long longValue(byte[] array) {
    Check.notNull(array, "array == null");
    Check.argument(array.length == 8, "array.length must be 8");

    return longValueUnchecked(array);
  }

  private long longValueUnchecked(byte[] array) {
    long result = array[0] & BYTE_MASK;
    result = (result << 8) | (array[1] & BYTE_MASK);
    result = (result << 8) | (array[2] & BYTE_MASK);
    result = (result << 8) | (array[3] & BYTE_MASK);
    result = (result << 8) | (array[4] & BYTE_MASK);
    result = (result << 8) | (array[5] & BYTE_MASK);
    result = (result << 8) | (array[6] & BYTE_MASK);
    result = (result << 8) | (array[7] & BYTE_MASK);
    return result;
  }

  private void toHexStringImpl(String expected, int... ints) {
    int length;
    length = ints.length;

    byte[] bytes;
    bytes = new byte[length];

    for (int i = 0; i < length; i++) {
      int intValue;
      intValue = ints[i];

      bytes[i] = (byte) (intValue & 0xff);
    }

    assertEquals(ByteArrays.toHexString(bytes), expected);
  }

}