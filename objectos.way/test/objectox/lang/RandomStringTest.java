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
package objectox.lang;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import objectos.core.object.RandomString;
import org.testng.annotations.Test;

public class RandomStringTest {

  @Test
  public void nextString() {
    Random random;
    random = new Random();

    String dictionary;
    dictionary = RandomString.DEFAULT_DICTIONARY;

    RandomString generator;
    generator = new RandomString(random, dictionary);

    char[] dictionaryChars;
    dictionaryChars = dictionary.toCharArray();

    for (int length = 1; length < 256; length++) {
      BitSet charset;
      charset = new BitSet(dictionary.length());

      for (int i = 0; i < 768; i++) {
        String result;
        result = generator.nextString(length);

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
