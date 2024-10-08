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
import static org.testng.Assert.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.testng.Assert;

public final class UnmodifiableIteratorTester<E> {

  private final Iterable<E> source;

  private Iterator<E> it;

  public UnmodifiableIteratorTester(Iterable<E> source) {
    this.source = source;
  }

  public final void set() {
    it = source.iterator();
  }

  public final void testMany(Iterable<? extends E> many) {
    var other = many.iterator();

    while (other.hasNext()) {
      E next = other.next();

      testNext(next);
    }
  }

  public final void testNext(E next) {
    try {
      it.remove();

      Assert.fail("Expected remove() to be unsupported");
    } catch (UnsupportedOperationException expected) {

    }

    assertTrue(it.hasNext());

    assertEquals(it.next(), next);
  }

  public final void testNoMoreElements() {
    assertFalse(it.hasNext());

    try {
      E next = it.next();

      Assert.fail("expected no more elements. Found=" + next);
    } catch (NoSuchElementException expected) {

    }
  }

}