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

import org.testng.annotations.Test;

public class DoubleArraysTest {

  @Test
  public void empty() {
    assertEquals(DoubleArrays.empty().length, 0);
  }

  @Test
  public void growIfNecessary() {
    var doubles = new double[3];

    var doublesNoGrowthRequired = DoubleArrays.growIfNecessary(doubles, 2);

    assertSame(doublesNoGrowthRequired, doubles);
    assertEquals(doublesNoGrowthRequired.length, 3);

    var doublesGrowthRequired = DoubleArrays.growIfNecessary(doubles, 3);

    assertNotSame(doublesGrowthRequired, doubles);
    assertTrue(doublesGrowthRequired.length > doubles.length);
  }

}