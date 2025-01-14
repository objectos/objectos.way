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
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class UtilObjectArraysTest {

  @Test
  public void empty() {
    assertEquals(Util.EMPTY_OBJECT_ARRAY.length, 0);
  }

  @Test
  public void growIfNecessary() {
    // String
    var array = new String[3];

    var noGrowthRequired = Util.growIfNecessary(array, 2);

    assertSame(noGrowthRequired, array);
    assertEquals(noGrowthRequired.length, 3);

    var growthRequired = Util.growIfNecessary(array, 3);

    var c = growthRequired.getClass();

    assertEquals(c.getComponentType(), String.class);
    assertNotSame(growthRequired, array);
    assertTrue(growthRequired.length > array.length);
  }

}