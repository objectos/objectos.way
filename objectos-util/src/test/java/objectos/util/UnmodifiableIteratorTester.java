/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.testng.Assert;

final class UnmodifiableIteratorTester<E> {

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