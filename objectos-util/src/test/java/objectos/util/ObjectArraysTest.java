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
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class ObjectArraysTest {

  @Test
  public void copyIfNecessary() {
    // Object
    Object[] objectsInitial;
    objectsInitial = ObjectArrays.empty();

    Object[] objectsResult;
    objectsResult = ObjectArrays.copyIfNecessary(objectsInitial, 0);

    assertTrue(objectsResult.length > objectsInitial.length);
    assertEquals(objectsResult.length, 2);

    objectsInitial = objectsResult;

    objectsResult = ObjectArrays.copyIfNecessary(objectsInitial, 1);

    assertSame(objectsResult, objectsInitial);

    // E[]
    String[] stringsInitial;
    stringsInitial = new String[0];

    String[] stringsResult;
    stringsResult = ObjectArrays.copyIfNecessary(stringsInitial, 0);

    assertTrue(stringsResult.length > stringsInitial.length);
    assertEquals(stringsResult.length, 2);

    stringsInitial = stringsResult;

    stringsResult = ObjectArrays.copyIfNecessary(stringsInitial, 1);

    assertSame(stringsResult, stringsInitial);
  }

  @Test
  public void empty() {
    assertEquals(ObjectArrays.empty().length, 0);
  }

}