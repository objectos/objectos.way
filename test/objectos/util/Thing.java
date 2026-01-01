/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import objectos.way.Next;
import objectos.way.TestingArrayBackedIterable;
import objectos.way.Util;

public final class Thing {

  static final Comparator<Thing> NATURAL_ORDER = new Comparator<Thing>() {
    @Override
    public final int compare(Thing o1, Thing o2) {
      String hex1;
      hex1 = o1.toHexString();

      String hex2;
      hex2 = o2.toHexString();

      return hex1.compareTo(hex2);
    }
  };

  public static final Thing[] EMPTY_ARRAY = new Thing[0];

  public static final Iterable<Thing> EMPTY_ITERABLE = new TestingArrayBackedIterable<>(EMPTY_ARRAY);

  public static final List<Thing> EMPTY_LIST = Collections.emptyList();

  public static final Set<Thing> EMPTY_SET = Collections.emptySet();

  public static final int MANY = 100;

  public static final int HALF = MANY / 2;

  private final byte[] value;

  private Thing(byte[] value) {
    this.value = value;
  }

  public static Thing next() {
    var value = Next.bytes(16);

    return new Thing(value);
  }

  public static Thing[] nextArray() {
    var array = new Thing[MANY];

    for (int i = 0; i < array.length; i++) {
      array[i] = next();
    }

    return array;
  }

  public static ArrayDeque<Thing> nextArrayDeque() {
    var deque = new ArrayDeque<Thing>(MANY);

    for (int i = 0; i < MANY; i++) {
      deque.add(next());
    }

    return deque;
  }

  public static ArrayList<Thing> nextArrayList() {
    var list = new ArrayList<Thing>(MANY);

    for (int i = 0; i < MANY; i++) {
      list.add(next());
    }

    return list;
  }

  public static Iterable<Thing> nextIterable() {
    var array = nextArray();

    return new TestingArrayBackedIterable<>(array);
  }

  public static Thing parse(String s) {
    var format = HexFormat.of();

    var bytes = format.parseHex(s);

    return new Thing(bytes);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Thing that
        && Arrays.equals(value, that.value);
  }

  @Override
  public final int hashCode() {
    return Arrays.hashCode(value);
  }

  public final String toDecimalString() {
    BigInteger bigInteger;
    bigInteger = new BigInteger(value);

    return bigInteger.toString();
  }

  public final String toHexString() {
    var format = HexFormat.of();

    return format.formatHex(value);
  }

  @Override
  public final String toString() {
    return "Thing[" + Util.toHexString(value) + "]";
  }

  public final String putDec(Map<Thing, String> map) {
    return map.put(this, toDecimalString());
  }

  public final String putHex(Map<Thing, String> map) {
    return map.put(this, toHexString());
  }

}