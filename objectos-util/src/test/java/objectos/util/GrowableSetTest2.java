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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableSetTest2 extends AbstractObjectosSetsTest {

  private GrowableSet<Thing> set;

  @BeforeClass
  public void _beforeClass() {
    set = new GrowableSet<>();
  }

  @BeforeMethod
  public void _beforeMethod() {
    set.clear();
  }

  @Test
  public void iterator() {
    assertTrue(set.isEmpty());

    Set<Thing> expected;
    expected = new HashSet<>();

    assertIterator(set.iterator(), expected);

    for (Thing t : thingArray) {
      set.add(t);

      expected.add(t);
    }

    assertIterator(set.iterator(), expected);
  }

  @Test
  public void join() {
    assertEquals(
      set.join(),
      ""
    );
    assertEquals(
      set.join("|"),
      ""
    );
    assertEquals(
      set.join("|", "{", "}"),
      "{}"
    );

    set.add(t1);

    assertEquals(
      set.join(),
      t1.toString()
    );
    assertEquals(
      set.join("|"),
      t1.toString()
    );
    assertEquals(
      set.join("|", "{", "}"),
      "{" + t1.toString() + "}"
    );

    set.add(t2);

    Iterator<Thing> iterator;
    iterator = set.iterator();

    Object o1;
    o1 = iterator.next();

    Object o2;
    o2 = iterator.next();

    assertEquals(
      set.join(),
      o1.toString() + o2.toString()
    );
    assertEquals(
      set.join("|"),
      o1.toString() + "|" + o2.toString()
    );
    assertEquals(
      set.join("|", "{", "}"),
      "{" + o1.toString() + "|" + o2.toString() + "}"
    );
  }

  @Test
  public void remove() {
    set.add(t1);

    try {
      set.remove(t1);

      Assert.fail();
    } catch (UnsupportedOperationException expected) {
      assertTrue(set.contains(t1));
    }
  }

  @Test
  public void removeAll() {
    set.addAll(thingList);

    try {
      set.removeAll(thingList);

      Assert.fail();
    } catch (UnsupportedOperationException expected) {
      assertTrue(set.containsAll(thingList));
    }
  }

  @Test
  public void retainAll() {
    set.addAll(thingList);

    List<Thing> retain;
    retain = new ArrayList<Thing>();

    retain.add(thingList.get(0));

    retain.add(thingList.get(1));

    try {
      set.retainAll(retain);

      Assert.fail();
    } catch (UnsupportedOperationException expected) {
      assertTrue(set.containsAll(thingList));
    }
  }

}