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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableSetTest {

  private static final int MANY = 100;

  private GrowableSet<Thing> set;

  @BeforeMethod
  public void _beforeMethod() {
    set = new GrowableSet<>();
  }

  @Test
  public void add() {
    // empty
    assertEquals(set.size(), 0);
    assertTrue(set.isEmpty());

    // one
    var t1 = Thing.next();

    assertTrue(set.add(t1));
    assertFalse(set.add(t1));

    assertSet(t1);

    // two
    var t2 = Thing.next();

    assertTrue(set.add(t2));
    assertFalse(set.add(t2));

    assertSet(t1, t2);

    // many
    var many = Thing.randomArray(MANY);

    for (var t : many) {
      assertTrue(set.add(t));
      assertFalse(set.add(t));
    }

    assertSet(t1, t2, many);

    // must reject null
    try {
      Thing t = null;

      set.add(t);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "e == null");
    }
  }

  private void assertSet(Object... expected) {
    var jdk = new HashSet<Thing>();

    for (var o : expected) {
      if (o instanceof Thing t) {
        jdk.add(t);
      } else if (o instanceof Thing[] a) {
        for (var t : a) {
          jdk.add(t);
        }
      } else {
        throw new UnsupportedOperationException("Implement me: " + o.getClass());
      }
    }

    Object[] elements = set.toArray();

    for (Object e : elements) {
      assertTrue(jdk.remove(e));
    }

    assertTrue(jdk.isEmpty());
  }

}