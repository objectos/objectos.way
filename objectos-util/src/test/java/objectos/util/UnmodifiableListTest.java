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

import java.util.List;
import java.util.function.BiConsumer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UnmodifiableListTest {

  private static final int MANY = 100;

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

  private void testAll(BiConsumer<UnmodifiableList<Thing>, List<Thing>> tester) {
    tester.accept(ul0, jdk0);
    tester.accept(ul1, jdk1);
    tester.accept(ul2, jdk2);
    tester.accept(ul3, jdk3);
    tester.accept(ulX, jdkX);
  }

}