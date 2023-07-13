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

  private static final int MAX_INDEX = 1 << 24 - 1;

  private Bytes() {}

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

}