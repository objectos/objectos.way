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
import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableListTest {

  private GrowableList<Thing> it;

  @BeforeMethod
  public void _beforeMethod() {
    it = new GrowableList<>();
  }

  @Test
  public void add() {
    // empty
    assertEquals(it.size(), 0);

    // one
    var t1 = Thing.next();

    assertTrue(it.add(t1));
    assertContents(t1);

    // two
    var t2 = Thing.next();

    assertTrue(it.add(t2));
    assertContents(t1, t2);

    // many
    var array = Thing.randomArray(100);

    for (int i = 0; i < array.length; i++) {
      var t = array[i];

      assertTrue(it.add(t));
    }

    assertContents(t1, t2, array);

    // must reject null
    try {
      Thing t = null;

      it.add(t);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "e == null");
    }
  }

  private void assertContents(Object... expected) {
    int i = 0;

    for (Object o : expected) {
      if (o instanceof Thing t) {
        assertEquals(it.get(i++), t);
      } else if (o instanceof Thing[] arr) {
        for (var t : arr) {
          assertEquals(it.get(i++), t);
        }
      } else if (o instanceof Iterable<?> iter) {
        for (var t : iter) {
          assertEquals(it.get(i++), t);
        }
      } else {
        throw new UnsupportedOperationException("Implement me: " + o.getClass());
      }
    }

    assertEquals(it.size(), i);
  }

}