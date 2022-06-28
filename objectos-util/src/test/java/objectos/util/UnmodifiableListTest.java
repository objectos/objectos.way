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
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UnmodifiableListTest {

  private static final int MANY = 100;

  private static final int HALF = MANY / 2;

  private UnmodifiableList<Thing> ul0;

  private UnmodifiableList<Thing> ul1;

  private UnmodifiableList<Thing> ul2;

  private UnmodifiableList<Thing> ul3;

  private UnmodifiableList<Thing> ulX;

  private List<Thing> jdk0;

  private List<Thing> jdk1;

  private List<Thing> jdk2;

  private List<Thing> jdk3;

  private List<Thing> jdkX;

  @BeforeClass
  public void _beforeClass() {
    ul0 = UnmodifiableList.of();
    jdk0 = List.of();

    var t1 = Thing.next();

    ul1 = UnmodifiableList.of(t1);
    jdk1 = List.of(t1);

    var t2 = Thing.next();

    ul2 = UnmodifiableList.of(t1, t2);
    jdk2 = List.of(t1, t2);

    var t3 = Thing.next();

    ul3 = UnmodifiableList.of(t1, t2, t3);
    jdk3 = List.of(t1, t2, t3);

    Thing[] many = Thing.randomArray(MANY);

    ulX = UnmodifiableList.copyOf(many);
    jdkX = List.of(many);
  }

  @Test
  public void add() {
    testAll(
      (it, els) -> {
        try {
          var t = Thing.next();

          it.add(t);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void add_withIndex() {
    testAll(
      (it, els) -> {
        try {
          var t = Thing.next();

          it.add(0, t);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void addAll() {
    final var arrayList = Thing.randomArrayList(MANY);

    testAll(
      (it, els) -> {
        try {
          it.addAll(arrayList);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void addAll_withIndex() {
    final var arrayList = Thing.randomArrayList(MANY);

    testAll(
      (it, els) -> {
        try {
          it.addAll(0, arrayList);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void clear() {
    testAll((it, els) -> {
      try {
        it.clear();

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {
        assertEquals(it, els);
      }
    });
  }

  @Test
  public void contains() {
    var t1 = jdk1.get(0);

    assertFalse(ul0.contains(t1));
    assertTrue(ul1.contains(t1));
    assertTrue(ul2.contains(t1));
    assertTrue(ul3.contains(t1));
    assertFalse(ulX.contains(t1));

    var t2 = jdk2.get(1);

    assertFalse(ul0.contains(t2));
    assertFalse(ul0.contains(t1, t2));

    assertFalse(ul1.contains(t2));
    assertFalse(ul1.contains(t1, t2));

    assertTrue(ul2.contains(t2));
    assertTrue(ul2.contains(t1, t2));

    assertTrue(ul3.contains(t2));
    assertTrue(ul3.contains(t1, t2));

    assertFalse(ulX.contains(t2));

    var t3 = jdk3.get(2);

    assertFalse(ul0.contains(t3));
    assertFalse(ul0.contains(t1, t2, t3));

    assertFalse(ul1.contains(t3));
    assertFalse(ul1.contains(t1, t2, t3));

    assertFalse(ul2.contains(t3));
    assertFalse(ul2.contains(t1, t2, t3));

    assertTrue(ul3.contains(t3));
    assertTrue(ul3.contains(t1, t2, t3));

    assertFalse(ulX.contains(t3));

    for (var e : jdkX) {
      assertFalse(ul0.contains(e));
      assertFalse(ul1.contains(e));
      assertFalse(ul2.contains(e));
      assertFalse(ul3.contains(e));
      assertTrue(ulX.contains(e));
    }
  }

  @Test
  public void containsAll() {
    assertFalse(ul0.containsAll(jdkX));
    assertFalse(ul1.containsAll(jdkX));
    assertFalse(ul2.containsAll(jdkX));
    assertFalse(ul3.containsAll(jdkX));
    assertTrue(ulX.containsAll(jdkX));

    var list = new ArrayList<Thing>(jdkX.size() + 1);

    var t1 = Thing.next();

    list.add(t1);

    list.addAll(jdkX);

    assertFalse(ulX.containsAll(list));

    var listWithNull = new ArrayList<Thing>(jdkX.size());

    listWithNull.addAll(jdkX);

    listWithNull.set(HALF, null);

    assertFalse(ulX.containsAll(listWithNull));
  }

  @Test
  public void equals() {
    // empty
    assertTrue(ul0.equals(jdk0));
    assertTrue(jdk0.equals(ul0));
    assertFalse(ul0.equals(null));
    assertTrue(ul0.equals(ul0));
    assertFalse(ul0.equals(ul1));
    assertFalse(ul0.equals(ul2));
    assertFalse(ul0.equals(ul3));
    assertFalse(ul0.equals(ulX));

    // one
    assertTrue(ul1.equals(jdk1));
    assertTrue(jdk1.equals(ul1));
    assertFalse(ul1.equals(null));
    assertFalse(ul1.equals(ul0));
    assertTrue(ul1.equals(ul1));
    assertFalse(ul1.equals(ul2));
    assertFalse(ul1.equals(ul3));
    assertFalse(ul1.equals(ulX));

    // two
    assertTrue(ul2.equals(jdk2));
    assertTrue(jdk2.equals(ul2));
    assertFalse(ul2.equals(null));
    assertFalse(ul2.equals(ul0));
    assertFalse(ul2.equals(ul1));
    assertTrue(ul2.equals(ul2));
    assertFalse(ul2.equals(ul3));
    assertFalse(ul2.equals(ulX));

    // many
    assertTrue(ulX.equals(jdkX));
    assertTrue(jdkX.equals(ulX));
    assertFalse(ulX.equals(null));
    assertFalse(ulX.equals(ul0));
    assertFalse(ulX.equals(ul1));
    assertFalse(ulX.equals(ul2));
    assertFalse(ulX.equals(ul3));
    assertTrue(ulX.equals(ulX));
  }

  @Test
  public void get() {
    class Tester {
      UnmodifiableList<Thing> it;

      public final void get(int index, Thing expected) {
        var value = it.get(index);

        assertEquals(value, expected);
      }

      public final void getOutOfBounds(int index) {
        try {
          it.get(index);

          fail();
        } catch (IndexOutOfBoundsException expected) {

        }
      }
    }

    var tester = new Tester();

    tester.it = ul0;

    tester.getOutOfBounds(-1);
    tester.getOutOfBounds(0);
    tester.getOutOfBounds(1);

    tester.it = ul1;

    tester.getOutOfBounds(-1);
    tester.get(0, jdk1.get(0));
    tester.getOutOfBounds(1);

    tester.it = ul2;

    tester.getOutOfBounds(-1);
    tester.get(0, jdk2.get(0));
    tester.get(1, jdk2.get(1));
    tester.getOutOfBounds(2);

    tester.it = ul3;

    tester.getOutOfBounds(-1);
    tester.get(0, jdk3.get(0));
    tester.get(1, jdk3.get(1));
    tester.get(2, jdk3.get(2));
    tester.getOutOfBounds(3);

    tester.it = ulX;

    tester.getOutOfBounds(-1);
    for (int i = 0, size = jdkX.size(); i < size; i++) {
      tester.get(i, jdkX.get(i));
    }
    tester.getOutOfBounds(jdkX.size());
  }

  @Test
  public void getOnly() {
    try {
      ul0.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: empty.");
    }

    assertEquals(ul1.getOnly(), jdk1.get(0));

    try {
      ul2.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: more than one element.");
    }
  }

  @Test
  public void hashCodeTest() {
    assertEquals(ul0.hashCode(), jdk0.hashCode());
    assertEquals(ul1.hashCode(), jdk1.hashCode());
    assertEquals(ul2.hashCode(), jdk2.hashCode());
    assertEquals(ul3.hashCode(), jdk3.hashCode());
    assertEquals(ulX.hashCode(), jdkX.hashCode());
  }

  @Test
  public void indexOf() {
    var t1 = jdk1.get(0);

    assertEquals(ul0.indexOf(t1), -1);
    assertEquals(ul1.indexOf(t1), 0);
    assertEquals(ul2.indexOf(t1), 0);
    assertEquals(ul3.indexOf(t1), 0);

    var t2 = jdk2.get(1);

    assertEquals(ul0.indexOf(t2), -1);
    assertEquals(ul1.indexOf(t2), -1);
    assertEquals(ul2.indexOf(t2), 1);
    assertEquals(ul3.indexOf(t2), 1);

    var t3 = jdk3.get(2);

    assertEquals(ul0.indexOf(t3), -1);
    assertEquals(ul1.indexOf(t3), -1);
    assertEquals(ul2.indexOf(t3), -1);
    assertEquals(ul3.indexOf(t3), 2);

    for (int i = 0, size = jdkX.size(); i < size; i++) {
      var t = jdkX.get(i);

      assertEquals(ulX.indexOf(t), i);
    }
  }

  @Test
  public void isEmpty() {
    assertTrue(ul0.isEmpty());
    assertFalse(ul1.isEmpty());
    assertFalse(ul2.isEmpty());
    assertFalse(ul3.isEmpty());
    assertFalse(ulX.isEmpty());
  }

  @Test
  public void iterator() {
    // empty
    var tester = new UnmodifiableIteratorTester<>(ul0);
    tester.set();
    tester.testNoMoreElements();

    // one
    tester = new UnmodifiableIteratorTester<>(ul1);
    tester.set();
    tester.testNext(jdk1.get(0));
    tester.testNoMoreElements();

    // two
    tester = new UnmodifiableIteratorTester<>(ul2);
    tester.set();
    tester.testNext(jdk2.get(0));
    tester.testNext(jdk2.get(1));
    tester.testNoMoreElements();

    // many
    tester = new UnmodifiableIteratorTester<>(ulX);
    tester.set();
    tester.testMany(jdkX);
    tester.testNoMoreElements();
  }

  @Test
  public void join() {
    assertEquals(ul0.join(), "");
    assertEquals(ul0.join("|"), "");
    assertEquals(ul0.join("|", "{", "}"), "{}");

    var t1 = jdk1.get(0);

    assertEquals(ul1.join(), t1.toString());
    assertEquals(ul1.join("|"), t1.toString());
    assertEquals(ul1.join("|", "{", "}"), "{" + t1 + "}");

    var t2 = jdk2.get(1);

    assertEquals(ul2.join(), t1.toString() + t2.toString());
    assertEquals(ul2.join("|"), t1 + "|" + t2);
    assertEquals(ul2.join("|", "{", "}"), "{" + t1 + "|" + t2 + "}");
  }

  @Test
  public void lastIndexOf() {
    var t1 = jdk1.get(0);

    assertEquals(ul0.lastIndexOf(t1), -1);
    assertEquals(ul1.lastIndexOf(t1), 0);
    assertEquals(ul2.lastIndexOf(t1), 0);
    assertEquals(ul3.lastIndexOf(t1), 0);
    assertEquals(ulX.lastIndexOf(t1), -1);

    var t2 = jdk2.get(1);

    assertEquals(ul0.lastIndexOf(t2), -1);
    assertEquals(ul1.lastIndexOf(t2), -1);
    assertEquals(ul2.lastIndexOf(t2), 1);
    assertEquals(ul3.lastIndexOf(t2), 1);
    assertEquals(ulX.lastIndexOf(t2), -1);

    var t3 = jdk3.get(2);

    assertEquals(ul0.lastIndexOf(t3), -1);
    assertEquals(ul1.lastIndexOf(t3), -1);
    assertEquals(ul2.lastIndexOf(t3), -1);
    assertEquals(ul3.lastIndexOf(t3), 2);
    assertEquals(ulX.lastIndexOf(t3), -1);

    var arrayList = new ArrayList<>(jdkX);

    var half = arrayList.get(HALF);

    arrayList.set(MANY - 1, half);

    var it = UnmodifiableList.copyOf(arrayList);

    assertEquals(it.lastIndexOf(half), MANY - 1);
  }

  @Test
  public void listIterator() {
    testAll(
      (it, els) -> {
        try {
          it.listIterator();

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }

        try {
          it.listIterator(0);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void remove() {
    var t1 = jdk1.get(0);

    testAll(
      (it, els) -> {
        try {
          it.remove(t1);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }

        try {
          it.remove(0);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void removeIf() {
    var t1 = jdk1.get(0);

    testAll(
      (it, els) -> {
        try {
          it.removeIf(e -> e.equals(t1));

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void replaceAll() {
    testAll(
      (it, els) -> {
        try {
          it.replaceAll(t -> Thing.next());

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void retainAll() {
    testAll(
      (it, els) -> {
        try {
          it.retainAll(els);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  @Test
  public void set() {
    var t1 = Thing.next();

    testAll(
      (it, els) -> {
        try {
          it.set(0, t1);

          Assert.fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
          assertEquals(it, els);
        }
      }
    );
  }

  private void testAll(BiConsumer<UnmodifiableList<Thing>, List<Thing>> tester) {
    tester.accept(ul0, jdk0);
    tester.accept(ul1, jdk1);
    tester.accept(ul2, jdk2);
    tester.accept(ul3, jdk3);
    tester.accept(ulX, jdkX);
  }

}