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

import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UnmodifiableListTest {

  private static final int MANY = 100;

  private UnmodifiableList<Thing> ul0;

  private UnmodifiableList<Thing> ul1;

  private UnmodifiableList<Thing> ul2;

  private UnmodifiableList<Thing> ulX;

  @BeforeClass
  public void _beforeClass() {
    ul0 = UnmodifiableList.of();

    var t1 = Thing.next();

    ul1 = UnmodifiableList.of(t1);

    var t2 = Thing.next();

    ul2 = UnmodifiableList.of(t1, t2);

    Thing[] many = Thing.randomArray(MANY);

    ulX = UnmodifiableList.copyOf(many);
  }

  @Test
  public void add() {
    Consumer<UnmodifiableList<Thing>> tester = l -> {
      try {
        var t = Thing.next();

        l.add(t);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {

      }
    };

    tester.accept(ul0);
    tester.accept(ul1);
    tester.accept(ul2);
    tester.accept(ulX);
  }

  @Test
  public void add_withIndex() {
    Consumer<UnmodifiableList<Thing>> tester = l -> {
      try {
        var t = Thing.next();

        l.add(0, t);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {

      }
    };

    tester.accept(ul0);
    tester.accept(ul1);
    tester.accept(ul2);
    tester.accept(ulX);
  }

  @Test
  public void addAll() {
    final var arrayList = Thing.randomArrayList(MANY);

    Consumer<UnmodifiableList<Thing>> tester = l -> {
      try {
        l.addAll(arrayList);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {

      }
    };

    tester.accept(ul0);
    tester.accept(ul1);
    tester.accept(ul2);
    tester.accept(ulX);
  }

  @Test
  public void addAll_withIndex() {
    final var arrayList = Thing.randomArrayList(MANY);

    Consumer<UnmodifiableList<Thing>> tester = l -> {
      try {
        l.addAll(0, arrayList);

        Assert.fail("Expected an UnsupportedOperationException");
      } catch (UnsupportedOperationException expected) {

      }
    };

    tester.accept(ul0);
    tester.accept(ul1);
    tester.accept(ul2);
    tester.accept(ulX);
  }

}