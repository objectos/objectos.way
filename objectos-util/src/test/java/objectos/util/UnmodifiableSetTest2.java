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

import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UnmodifiableSetTest2 extends AbstractObjectosSetsTest {

  private UnmodifiableSet<Thing> t1Set;

  @BeforeClass
  public void _beforeClass() {
    t1Set = UnmodifiableSet.of(t1);
  }

  @Test(description = "UnmodifiableSet.copyOf(E[])")
  public void copyOf0() {
    UnmodifiableSet<Thing> result;
    result = UnmodifiableSet.copyOf(new Thing[] {});

    assertSame(result, UnmodifiableSet.of());

    result = UnmodifiableSet.copyOf(thingArray);

    Set<Thing> expected;
    expected = new HashSet<>();

    for (Thing t : thingArray) {
      expected.add(t);
    }

    assertSet(result, expected);
  }

  @Test
  public void remove() {
    try {
      t1Set.remove(t1);

      Assert.fail();
    } catch (UnsupportedOperationException expected) {
      assertTrue(t1Set.contains(t1));
    }
  }

  @Test
  public void removeAll() {
    UnmodifiableSet<Thing> set;
    set = UnmodifiableSet.copyOf(thingList);

    try {
      set.removeAll(thingList);

      Assert.fail();
    } catch (UnsupportedOperationException expected) {
      assertTrue(set.containsAll(thingList));
    }
  }

  @Test
  public void retainAll() {
    UnmodifiableSet<Thing> set;
    set = UnmodifiableSet.copyOf(thingList);

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