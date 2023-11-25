/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.core.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import org.testng.annotations.Test;

public class NextTest {

  @Test
  public void bytes() {
    byte[] EMPTY;
    EMPTY = new byte[128];

    byte[] bytes;
    bytes = new byte[128];

    assertEquals(bytes, EMPTY);

    Next.bytes(bytes);

    assertNotEquals(bytes, EMPTY);
  }

  @Test
  public void bytes_() {
    byte[] EMPTY;
    EMPTY = new byte[123];

    byte[] array;
    array = Next.bytes(123);

    assertEquals(array.length, 123);

    assertNotEquals(array, EMPTY);
  }

  @Test
  public void intValue() {
    int count;
    count = 1024;

    int[] values;
    values = new int[count];

    for (int i = 0; i < count; i++) {
      values[i] = Next.intValue();
    }

    Arrays.sort(values);

    for (int i = 1; i < count; i++) {
      int previous;
      previous = values[i - 1];

      int current;
      current = values[i];

      assertNotEquals(previous, current);
    }

    int bound;
    bound = 1234;

    for (int i = 0; i < count; i++) {
      int randomInt;
      randomInt = Next.intValue(bound);

      assertTrue(randomInt >= 0);
      assertTrue(randomInt < bound);
    }
  }

  @Test
  public void string() {
    String dictionary;
    dictionary = RandomString.DEFAULT_DICTIONARY;

    char[] dictionaryChars;
    dictionaryChars = dictionary.toCharArray();

    for (int length = 1; length < 256; length++) {
      BitSet charset;
      charset = new BitSet(dictionary.length());

      for (int i = 0; i < 768; i++) {
        String result;
        result = Next.string(length);

        assertEquals(result.length(), length);

        char[] charArray;
        charArray = result.toCharArray();

        for (int j = 0; j < charArray.length; j++) {
          char c;
          c = charArray[j];

          int index;
          index = Arrays.binarySearch(dictionaryChars, c);

          charset.set(index);
        }
      }

      for (int i = 0; i < charset.length(); i++) {
        assertTrue(charset.get(i));
      }
    }
  }

}