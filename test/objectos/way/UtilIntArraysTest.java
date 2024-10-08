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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class UtilIntArraysTest {

  @Test
  public void empty() {
    assertEquals(Util.EMPTY_INT_ARRAY.length, 0);
  }

  @Test
  public void growIfNecessary() {
    // int
    var ints = new int[3];

    var intsNoGrowthRequired = Util.growIfNecessary(ints, 2);

    assertSame(intsNoGrowthRequired, ints);
    assertEquals(intsNoGrowthRequired.length, 3);

    var intsGrowthRequired = Util.growIfNecessary(ints, 3);

    assertNotSame(intsGrowthRequired, ints);
    assertTrue(intsGrowthRequired.length > ints.length);
  }

}