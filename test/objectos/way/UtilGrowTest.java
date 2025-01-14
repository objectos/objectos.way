/*
 * Copyright (C) 2022-2025 Objectos Software LTDA.
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
import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UtilGrowTest {

  @Test
  public void arrayLength() {
    int delta = 1;

    int iterations = 1;

    int length = 4;

    int oldLength;

    int requiredLength;

    while (true) {
      oldLength = length;

      requiredLength = length + delta;

      length = Util.arrayLength(oldLength, requiredLength);

      assertTrue(length > oldLength);

      if (length == Integer.MAX_VALUE) {
        break;
      }

      if (iterations > 60) {
        Assert.fail(iterations + " > 60");
      }

      iterations++;
    }

    assertEquals(iterations, 51);

    assertEquals(length, Integer.MAX_VALUE);
  }

  @Test(description = //
  """
  growBy (test case 0)
  ------------------------------

  - start with default capacity
  - simulate add(Collection.size = 1)
  """)
  public void growBy() {
    var length = Util.DEFAULT_CAPACITY;

    length = Util.growBy(length, 1);

    assertEquals(length, 15);

    length = Util.growBy(length, 1);

    assertEquals(length, 22);

    length = Util.growBy(1_197_571_635, 1);

    assertEquals(length, 1_796_357_452);

    length = Util.growBy(length, 1);

    assertEquals(length, Util.JVM_SOFT_LIMIT);

    try {
      Util.growByOne(Util.JVM_SOFT_LIMIT);

      Assert.fail();
    } catch (OutOfMemoryError expected) {

    }
  }

  @Test(description = //
  """
  growByOne (test case 0)
  ------------------------------

  - start with default capacity
  - simulate add(E e)
  """)
  public void growByOne() {
    var length = Util.DEFAULT_CAPACITY;

    length = Util.growByOne(length);

    assertEquals(length, 15);

    length = Util.growByOne(length);

    assertEquals(length, 22);

    length = Util.growByOne(1_197_571_635);

    assertEquals(length, 1_796_357_452);

    length = Util.growByOne(length);

    assertEquals(length, Util.JVM_SOFT_LIMIT);

    try {
      Util.growByOne(Util.JVM_SOFT_LIMIT);

      Assert.fail();
    } catch (OutOfMemoryError expected) {

    }
  }

}