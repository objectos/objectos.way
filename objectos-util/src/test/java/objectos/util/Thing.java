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

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;
import objectos.lang.ToString;

final class Thing implements ToString.Formattable {

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

  static final Thing[] EMPTY_ARRAY = new Thing[0];

  static final Iterable<Thing> EMPTY_ITERABLE = new ArrayBackedIterable<>(EMPTY_ARRAY);

  static final List<Thing> EMPTY_LIST = Collections.emptyList();

  static final Set<Thing> EMPTY_SET = Collections.emptySet();

  private final byte[] value;

  private Thing(byte[] value) {
    this.value = value;
  }

  public static Thing next() {
    byte[] value;
    value = Next.bytes(16);

    return new Thing(value);
  }

  public static Thing parse(String s) {
    HexFormat format = HexFormat.of();

    var bytes = format.parseHex(s);

    return new Thing(bytes);
  }

  public static Thing[] randomArray(int size) {
    var array = new Thing[size];

    for (int i = 0; i < array.length; i++) {
      array[i] = next();
    }

    return array;
  }

  public static ArrayDeque<Thing> randomArrayDeque(int size) {
    var deque = new ArrayDeque<Thing>();

    for (int i = 0; i < size; i++) {
      deque.add(next());
    }

    return deque;
  }

  public static ArrayList<Thing> randomArrayList(int size) {
    var list = new ArrayList<Thing>();

    for (int i = 0; i < size; i++) {
      list.add(next());
    }

    return list;
  }

  public static GrowableList<Thing> randomGrowableList(int size) {
    var list = new GrowableList<Thing>();

    var array = randomArray(size);

    for (Thing thing : array) {
      list.add(thing);
    }

    return list;
  }

  public static GrowableSet<Thing> randomGrowableSet(int size) {
    GrowableSet<Thing> set;
    set = new GrowableSet<>();

    Thing[] array;
    array = randomArray(size);

    for (Thing thing : array) {
      set.add(thing);
    }

    return set;
  }

  public static HashSet<Thing> randomHashSet(int size) {
    HashSet<Thing> set;
    set = new HashSet<Thing>(size);

    Thing[] array;
    array = randomArray(size);

    for (Thing thing : array) {
      set.add(thing);
    }

    return set;
  }

  public static Iterable<Thing> randomIterable(int size) {
    var array = randomArray(size);

    return new ArrayBackedIterable<>(array);
  }

  public static UnmodifiableSet<Thing> randomUnmodifiableSet(int size) {
    Thing[] array;
    array = randomArray(size);

    return UnmodifiableSet.copyOf(array);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Thing that
        && Arrays.equals(value, that.value);
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
      sb, depth, this,
      "value", ByteArrays.toHexString(value)
    );
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
    return ToString.of(this);
  }

}