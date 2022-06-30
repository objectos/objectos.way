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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableSetTest {

  private static final int MANY = 100;

  private static final int HALF = MANY / 2;

  private GrowableSet<Thing> it;

  @BeforeMethod
  public void _beforeMethod() {
    it = new GrowableSet<>();
  }

  @Test
  public void add() {
    // empty
    assertEquals(it.size(), 0);
    assertTrue(it.isEmpty());

    // one
    var t1 = Thing.next();

    assertTrue(it.add(t1));
    assertFalse(it.add(t1));

    assertContents(t1);

    // two
    var t2 = Thing.next();

    assertTrue(it.add(t2));
    assertFalse(it.add(t2));

    assertContents(t1, t2);

    // many
    var many = Thing.nextArray();

    for (var t : many) {
      assertTrue(it.add(t));
      assertFalse(it.add(t));
    }

    assertContents(t1, t2, many);

    // must reject null
    try {
      Thing t = null;

      it.add(t);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "e == null");
    }
  }

  @Test
  public void addAll() {
    Consumer<Collection<? extends Thing>> tester = c -> {
      assertEquals(it.addAll(c), !c.isEmpty());
      assertFalse(it.addAll(c));

      assertContents(c);

      _beforeMethod();
    };

    // empty
    tester.accept(Thing.EMPTY_LIST);
    tester.accept(Thing.EMPTY_SET);

    // one
    var t1 = Thing.next();

    tester.accept(List.of(t1));
    tester.accept(Set.of(t1));

    // two
    var t2 = Thing.next();

    tester.accept(List.of(t1, t2));
    tester.accept(Set.of(t1, t2));

    // many
    var many = Thing.randomArray(MANY);

    tester.accept(List.of(many));
    tester.accept(Set.of(many));

    // must reject null
    Consumer<Collection<? extends Thing>> nullTester = c -> {
      try {
        it.addAll(c);

        Assert.fail("Must throw NullPointerException");
      } catch (NullPointerException expected) {
        String msg = expected.getMessage();

        assertTrue(msg.matches("c\\[[0-9]{1,2}\\] == null"));
      }

      _beforeMethod();
    };

    many[HALF] = null;

    var listWithNull = new ArrayList<Thing>(MANY);
    var setWithNull = new HashSet<Thing>(MANY);

    for (var t : many) {
      listWithNull.add(t);
      setWithNull.add(t);
    }

    nullTester.accept(listWithNull);
    nullTester.accept(setWithNull);
  }

  @Test
  public void addAllIterable() {
    var test = new GrowableCollectionAddAllIterableTest(it, this::assertContents);

    test.execute();
  }

  @Test
  public void addWithNullMessage() {
    var test = new GrowableCollectionAddWithNullMessageTest(it);

    test.execute();
  }

  private void assertContents(Object... expected) {
    var jdk = new HashSet<>();

    for (var o : expected) {
      if (o instanceof Thing t) {
        jdk.add(t);
      } else if (o instanceof Thing[] a) {
        for (var t : a) {
          jdk.add(t);
        }
      } else if (o instanceof Iterable<?> iter) {
        for (var t : iter) {
          jdk.add(t);
        }
      } else {
        throw new UnsupportedOperationException("Implement me: " + o.getClass());
      }
    }

    Object[] elements = it.toArray();

    for (Object e : elements) {
      assertTrue(jdk.remove(e));
    }

    assertTrue(jdk.isEmpty());
  }

}