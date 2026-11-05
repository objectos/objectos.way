/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MoreArraysTest {

  @Test
  public void emptyByteArray() {
    assertEquals(MoreArrays.EMPTY_BYTE_ARRAY.length, 0);
  }

  @Test
  public void emptyObjectArray() {
    assertEquals(MoreArrays.EMPTY_OBJECT_ARRAY.length, 0);
  }

  @Test
  public void growIfNecessaryByteArray() {
    var bytes = new byte[3];

    var noGrowthRequired = MoreArrays.growIfNecessary(bytes, 2);

    assertSame(noGrowthRequired, bytes);

    assertEquals(noGrowthRequired.length, 3);

    var growthRequired = MoreArrays.growIfNecessary(bytes, 3);

    assertNotSame(growthRequired, bytes);

    assertTrue(growthRequired.length > bytes.length);

    try {
      MoreArrays.growIfNecessary(bytes, -1);

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
  }

  @Test
  public void growIfNecessaryObjectArray() {
    var array = new String[3];

    var noGrowthRequired = MoreArrays.growIfNecessary(array, 2);

    assertSame(noGrowthRequired, array);

    assertEquals(noGrowthRequired.length, 3);

    var growthRequired = MoreArrays.growIfNecessary(array, 3);

    var type = growthRequired.getClass();

    assertEquals(type.getComponentType(), String.class);

    assertNotSame(growthRequired, array);

    assertTrue(growthRequired.length > array.length);

    try {
      MoreArrays.growIfNecessary(array, -1);

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
  }

}
