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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UnmodifiableListTest extends AbstractObjectosListsTest {

  private UnmodifiableList<Integer> il0;

  private UnmodifiableList<Integer> il1;

  private UnmodifiableList<Integer> il2;

  private UnmodifiableList<Integer> ilN;

  private Integer[] intArray;

  private Integer randomInt;

  private Integer singleton;

  @BeforeClass
  public void _beforeClass() {
    il0 = UnmodifiableList.of();

    singleton = Next.intValue();

    randomInt = Next.intValue();

    il1 = UnmodifiableList.of(singleton);

    il2 = UnmodifiableList.of(singleton, randomInt);

    intArray = randomIntegerArray(2345);

    ilN = UnmodifiableList.copyOf(intArray);
  }

  @Test
  public void contains() {
    Object[] objectIntArray;
    objectIntArray = Arrays.copyOf(intArray, intArray.length, Object[].class);

    assertFalse(il0.contains(singleton));
    assertFalse(il0.contains(randomInt));
    assertFalse(il0.contains(singleton, randomInt));
    assertFalse(il0.contains(randomInt, objectIntArray));

    assertTrue(il1.contains(singleton));
    assertFalse(il1.contains(randomInt));
    assertFalse(il1.contains(singleton, randomInt));
    assertFalse(il0.contains(randomInt, objectIntArray));

    for (int i = 0; i < intArray.length; i++) {
      Integer e;
      e = intArray[i];

      assertTrue(ilN.contains(e));
    }

    assertTrue(ilN.contains(intArray[0], objectIntArray));
    assertFalse(ilN.contains(randomInt, objectIntArray));
  }

  @Test
  public void copyOf() {
    GrowableList<Integer> growable;
    growable = randomIntGrowableList(1234);

    UnmodifiableList<Integer> immutable;
    immutable = growable.toUnmodifiableList();

    UnmodifiableList<Integer> result;
    result = UnmodifiableList.copyOf(growable);

    assertEquals(result, immutable);

    result = UnmodifiableList.copyOf(immutable);

    assertSame(result, immutable);

    ArrayBackedIterable<Integer> iterable;
    iterable = randomIntArrayBackedIterable(1234);

    result = UnmodifiableList.copyOf(iterable);

    Iterator<Integer> iterator;
    iterator = iterable.iterator();

    for (int i = 0; i < result.size(); i++) {
      assertTrue(iterator.hasNext());

      assertEquals(result.get(i), iterator.next());
    }

    assertFalse(iterator.hasNext());

    iterator = iterable.iterator();

    result = UnmodifiableList.copyOf(iterator);

    iterator = iterable.iterator();

    for (int i = 0; i < result.size(); i++) {
      assertTrue(iterator.hasNext());

      assertEquals(result.get(i), iterator.next());
    }

    assertFalse(iterator.hasNext());
  }

  @Test
  public void equals() {
    assertTrue(il0.equals(UnmodifiableList.of()));
    assertTrue(il0.equals(new UnmodifiableList<Integer>(ObjectArrays.empty())));
    assertFalse(il0.equals(null));
    assertTrue(il0.equals(il0));
    assertFalse(il0.equals(il1));
    assertFalse(il0.equals(il2));
    assertFalse(il0.equals(ilN));

    assertTrue(il1.equals(UnmodifiableList.of(singleton)));
    assertFalse(il1.equals(null));
    assertFalse(il1.equals(il0));
    assertTrue(il1.equals(il1));
    assertFalse(il1.equals(il2));
    assertFalse(il1.equals(ilN));

    assertTrue(ilN.equals(UnmodifiableList.copyOf(intArray)));
    assertFalse(ilN.equals(null));
    assertFalse(ilN.equals(il0));
    assertFalse(ilN.equals(il1));
    assertFalse(ilN.equals(il2));
    assertTrue(ilN.equals(ilN));
  }

  @Test
  public void get() {
    class Tester {
      UnmodifiableList<Integer> it;

      public final void get(int index, Integer expected) {
        Integer value;
        value = it.get(index);

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

    Tester tester;
    tester = new Tester();

    tester.it = il0;

    tester.getOutOfBounds(-1);
    tester.getOutOfBounds(0);
    tester.getOutOfBounds(1);

    tester.it = il1;

    tester.getOutOfBounds(-1);
    tester.get(0, singleton);
    tester.getOutOfBounds(1);

    tester.it = ilN;

    tester.getOutOfBounds(-1);
    tester.get(0, intArray[0]);
    tester.get(1, intArray[1]);
    tester.get(2, intArray[2]);
    tester.getOutOfBounds(intArray.length);
  }

  @Test
  public void getOnly() {
    try {
      il0.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: empty.");
    }

    assertEquals(il1.getOnly(), singleton);

    try {
      ilN.getOnly();

      Assert.fail();
    } catch (IllegalStateException expected) {
      assertEquals(expected.getMessage(), "Could not getOnly: more than one element.");
    }
  }

  @Test
  public void isEmpty() {
    assertTrue(il0.isEmpty());
    assertFalse(il1.isEmpty());
    assertFalse(ilN.isEmpty());
  }

  @Test
  public void join() {
    assertEquals(il0.join(), "");
    assertEquals(il0.join("|"), "");
    assertEquals(il0.join("|", "{", "}"), "{}");

    assertEquals(il1.join(), singleton.toString());
    assertEquals(il1.join("|"), singleton.toString());
    assertEquals(il1.join("|", "{", "}"), "{" + singleton + "}");

    assertEquals(il2.join(), singleton.toString() + randomInt.toString());
    assertEquals(il2.join("|"), singleton + "|" + randomInt);
    assertEquals(il2.join("|", "{", "}"), "{" + singleton + "|" + randomInt + "}");
  }

  @Test
  public void size() {
    assertEquals(il0.size(), 0);
    assertEquals(il1.size(), 1);
    assertEquals(ilN.size(), intArray.length);
  }

  @Test
  public void toArray() {
    Integer[] emptyArray;
    emptyArray = new Integer[0];

    Integer[] lastNull;
    lastNull = new Integer[intArray.length + 2];

    assertEquals(il0.toArray(), new Object[] {});
    assertSame(il0.toArray(emptyArray), emptyArray);

    Integer[] result;
    result = il0.toArray(lastNull);

    assertSame(result, lastNull);
    assertNull(result[0]);
    assertNull(result[1]);

    assertEquals(il1.toArray(), new Object[] {singleton});
    assertEquals(il1.toArray(emptyArray), new Integer[] {singleton});

    result = il1.toArray(lastNull);

    assertSame(result, lastNull);
    assertEquals(result[0], singleton);
    assertNull(result[1]);

    result = il2.toArray(lastNull);

    assertSame(result, lastNull);
    assertEquals(result[0], singleton);
    assertEquals(result[1], randomInt);
    assertNull(result[2]);

    assertEquals(ilN.toArray(), Arrays.copyOf(intArray, intArray.length, Object[].class));
    assertEquals(ilN.toArray(emptyArray), Arrays.copyOf(intArray, intArray.length));

    result = ilN.toArray(lastNull);

    assertSame(result, lastNull);

    int i = 0;

    for (; i < intArray.length; i++) {
      assertEquals(result[i], intArray[i]);
    }

    assertNull(result[i]);
  }

  @Test
  public void toStringTest() {
    assertEquals(il0.toString(), "UnmodifiableList []");

    assertEquals(
      il1.toString(),
      lines(
        "UnmodifiableList [",
        "  0 = " + singleton,
        "]"
      )
    );

    assertEquals(
      il2.toString(),
      lines(
        "UnmodifiableList [",
        "  0 = " + singleton,
        "  1 = " + randomInt,
        "]"
      )
    );
  }

}