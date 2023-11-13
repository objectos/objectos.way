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
package objectos.notes;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class LevelTest {

  @Test
  public void orderingTest() {
    Level[] values;
    values = Level.values();

    assertEquals(values.length, 5);

    assertEquals(values[0], Level.TRACE);

    assertEquals(values[1], Level.DEBUG);

    assertEquals(values[2], Level.INFO);

    assertEquals(values[3], Level.WARN);

    assertEquals(values[4], Level.ERROR);
  }

}