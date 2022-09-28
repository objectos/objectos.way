/*
 * Copyright (C) 2022 Objectos Software LTDA.
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

import org.testng.Assert;
import org.testng.annotations.Test;

public class CharArraysTest {

  @Test
  public void append() {
    char[] subject;
    subject = new char[3];

    char[] noGrowthRequired;
    noGrowthRequired = CharArrays.append(subject, 1, "bc");

    assertEquals(noGrowthRequired[0], '\0');
    assertEquals(noGrowthRequired[1], 'b');
    assertEquals(noGrowthRequired[2], 'c');

    assertSame(noGrowthRequired, subject);
    assertEquals(noGrowthRequired.length, 3);

    char[] growthRequired;
    growthRequired = CharArrays.append(subject, 3, "def");

    assertEquals(growthRequired[0], '\0');
    assertEquals(growthRequired[1], 'b');
    assertEquals(growthRequired[2], 'c');
    assertEquals(growthRequired[3], 'd');
    assertEquals(growthRequired[4], 'e');
    assertEquals(growthRequired[5], 'f');
    assertEquals(growthRequired[6], '\0');

    assertNotSame(growthRequired, subject);
    assertTrue(growthRequired.length > subject.length);
  }

  @Test
  public void growIfNecessary() {
    var chars = new char[3];

    var charsNoGrowthRequired = CharArrays.growIfNecessary(chars, 2);

    assertSame(charsNoGrowthRequired, chars);
    assertEquals(charsNoGrowthRequired.length, 3);

    var charsGrowthRequired = CharArrays.growIfNecessary(chars, 3);

    assertNotSame(charsGrowthRequired, chars);
    assertTrue(charsGrowthRequired.length > chars.length);

    try {
      CharArrays.growIfNecessary(chars, -1);

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
  }

}