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

import org.testng.annotations.Test;

public class IntArraysTest {

  @Test
  public void empty() {
    assertEquals(IntArrays.empty().length, 0);
  }

  @Test
  public void growIfNecessary() {
    // int
    var ints = new int[3];

    var intsNoGrowthRequired = IntArrays.growIfNecessary(ints, 2);

    assertSame(intsNoGrowthRequired, ints);
    assertEquals(intsNoGrowthRequired.length, 3);

    var intsGrowthRequired = IntArrays.growIfNecessary(ints, 3);

    assertNotSame(intsGrowthRequired, ints);
    assertTrue(intsGrowthRequired.length > ints.length);

    // int[]
    int[][] ints2;
    ints2 = new int[3][];

    int[][] ints2NoGrowthRequired;
    ints2NoGrowthRequired = IntArrays.growIfNecessary(ints2, 2);

    assertSame(ints2NoGrowthRequired, ints2);
    assertEquals(ints2NoGrowthRequired.length, 3);

    int[][] ints2GrowthRequired = IntArrays.growIfNecessary(ints2, 3);

    assertNotSame(ints2GrowthRequired, ints2);
    assertTrue(ints2GrowthRequired.length > ints2.length);
  }

}