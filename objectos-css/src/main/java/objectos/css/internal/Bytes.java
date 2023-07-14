/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.lang.Check;

final class Bytes {

  private static final int BYTE_MASK = 0xFF;

  private static final int MAX_INDEX = 1 << 24 - 1;

  private Bytes() {}

  public static double doubleValue(
      byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
    int v0 = toInt(b0, 0);
    int v1 = toInt(b1, 8);
    int v2 = toInt(b2, 16);
    int v3 = toInt(b3, 24);
    int v4 = toInt(b4, 32);
    int v5 = toInt(b5, 40);
    int v6 = toInt(b6, 48);
    int v7 = toInt(b7, 56);

    return v7 | v6 | v5 | v4 | v3 | v2 | v1 | v0;
  }

  // we use 3 bytes for internal indices
  public static byte idx0(int value) {
    Check.argument(value <= MAX_INDEX, "CssTemplate is too large.");

    return (byte) value;
  }

  // we use 3 bytes for internal indices
  public static byte idx1(int value) {
    return (byte) (value >>> 8);
  }

  // we use 3 bytes for internal indices
  public static byte idx2(int value) {
    return (byte) (value >>> 16);
  }

  public static byte int0(int value) {
    return (byte) (value >>> 0);
  }

  public static byte int1(int value) {
    return (byte) (value >>> 8);
  }

  public static byte int2(int value) {
    return (byte) (value >>> 16);
  }

  public static byte int3(int value) {
    return (byte) (value >>> 24);
  }

  public static int intValue(byte b0, byte b1, byte b2, byte b3) {
    int v0 = toInt(b0, 0);
    int v1 = toInt(b1, 8);
    int v2 = toInt(b2, 16);
    int v3 = toInt(b3, 24);

    return v3 | v2 | v1 | v0;
  }

  public static byte lng0(long value) {
    return (byte) (value >>> 0);
  }

  public static byte lng1(long value) {
    return (byte) (value >>> 8);
  }

  public static byte lng2(long value) {
    return (byte) (value >>> 16);
  }

  public static byte lng3(long value) {
    return (byte) (value >>> 24);
  }

  public static byte lng4(long value) {
    return (byte) (value >>> 32);
  }

  public static byte lng5(long value) {
    return (byte) (value >>> 40);
  }

  public static byte lng6(long value) {
    return (byte) (value >>> 48);
  }

  public static byte lng7(long value) {
    return (byte) (value >>> 56);
  }

  // we use 2 bytes for the StandardName enum
  public static byte name0(StandardName name) {
    int ordinal;
    ordinal = name.ordinal();

    return (byte) ordinal;
  }

  // we use 2 bytes for the StandardName enum
  public static byte name1(StandardName name) {
    int ordinal;
    ordinal = name.ordinal();

    return (byte) (ordinal >>> 8);
  }

  public static String standardNameValue(byte b0, byte b1) {
    int ordinal0 = Bytes.toInt(b0, 0);
    int ordinal1 = Bytes.toInt(b1, 8);

    int ordinal = ordinal1 | ordinal0;

    StandardName name;
    name = StandardName.byOrdinal(ordinal);

    return name.cssName;
  }

  public static int toInt(byte b, int shift) {
    return (b & BYTE_MASK) << shift;
  }

  public static byte unit(LengthUnit unit) {
    return int0(unit.ordinal());
  }

}