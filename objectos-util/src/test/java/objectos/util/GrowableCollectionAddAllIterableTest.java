/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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

import java.util.Arrays;
import java.util.List;
import org.testng.Assert;

final class GrowableCollectionAddAllIterableTest {

  private final GrowableCollection<Thing> it;

  private final AssertContents assertContents;

  public GrowableCollectionAddAllIterableTest(GrowableCollection<Thing> it,
                                              AssertContents assertContents) {
    this.it = it;
    this.assertContents = assertContents;
  }

  public void execute() {
    // empty
    assertEquals(it.size(), 0);

    assertFalse(it.addAllIterable(Thing.EMPTY_ITERABLE));
    assertContents.execute();

    assertFalse(it.addAllIterable(Thing.EMPTY_LIST));
    assertContents.execute();

    // one
    var t1 = Thing.next();

    it.addAllIterable(ArrayBackedIterable.of(t1));
    assertContents.execute(t1);

    // two
    var t2 = Thing.next();

    it.addAllIterable(List.of(t2));
    assertContents.execute(t1, t2);

    // many
    var iterable = Thing.nextIterable();

    it.addAllIterable(iterable);
    assertContents.execute(t1, t2, iterable);

    var arrayList = Thing.nextArrayList();

    it.addAllIterable(arrayList);
    assertContents.execute(t1, t2, iterable, arrayList);

    // must reject null
    var arrayWithNull = Thing.nextArray();

    arrayWithNull[Thing.HALF] = null;

    var iterWithNull = new ArrayBackedIterable<>(arrayWithNull);

    try {
      it.addAllIterable(iterWithNull);

      Assert.fail("Must throw NullPointerException");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "iterable[50] == null");
    }

    var copy = Arrays.copyOf(arrayWithNull, Thing.HALF);

    var sub = List.of(copy);

    assertContents.execute(t1, t2, iterable, arrayList, sub);
  }

}