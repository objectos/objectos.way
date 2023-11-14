/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectox.html.style;

import java.util.concurrent.atomic.AtomicInteger;

public class SeqId {

  private static final char[] FIRST;

  private static final char[] REST;

  static {
    FIRST = "abcdefghijklmnop".toCharArray();

    // length must be power of 2
    assert FIRST.length == 16;

    REST = "abcdefghijklmnopqrstuvwxyz012345".toCharArray();

    // length must be power of 2
    assert REST.length == 32;
  }

  private static final int MASK4 = 0b01111;

  private static final int MASK5 = 0b11111;

  private static final int MAX_VALUE = (1 << 19) - 1;

  private final AtomicInteger next;

  public SeqId() {
    this(0);
  }

  // visible for testing
  SeqId(int initialValue) {
    this.next = new AtomicInteger(initialValue);
  }

  public final String next() {
    char[] result;
    result = new char[4];

    int value;
    value = next.getAndIncrement();

    if (value > MAX_VALUE) {
      throw new IllegalStateException("Cannot generate more than 2^19 distinct identifiers");
    }

    int index;
    index = 0;

    result[index++] = FIRST[(value >>> 15) & MASK4];

    result[index++] = REST[(value >>> 10) & MASK5];

    result[index++] = REST[(value >>> 5) & MASK5];

    result[index++] = REST[value & MASK5];

    return new String(result);
  }

}