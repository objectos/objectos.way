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
package objectos.css.type;

import objectos.lang.Check;

public class ColorHex extends Color {

  private final String value;

  ColorHex(String value) {
    this.value = value;
  }

  public static void checkValidHexNotation(String hex) {
    if (!isValidHexNotation(hex)) {
      throw new IllegalArgumentException("Invalid CSS <color> hex notation: " + hex);
    }
  }

  public static boolean isValidHexNotation(String hex) {
    Check.notNull(hex, "hex == null");

    char[] array;
    array = hex.toCharArray();

    if (array.length < 4) {
      return false;
    }

    char first;
    first = array[0];

    if (first != '#') {
      return false;
    }

    switch (array.length) {
      case 4:
      case 5:
      case 7:
      case 9:
        return isValidHexNotation0(hex, array);
      default:
        return false;
    }
  }

  public static ColorHex of(String hex) {
    checkValidHexNotation(hex);

    return new ColorHex(hex);
  }

  private static boolean isValidHexNotation0(String hex, char[] array) {
    for (int i = 1; i < array.length; i++) {
      char c = array[i];

      if (c < '0') {
        return false;
      }

      if (c <= '9') {
        continue;
      }

      if (c < 'A') {
        return false;
      }

      if (c <= 'F') {
        continue;
      }

      if (c < 'a') {
        return false;
      }

      if (c <= 'f') {
        continue;
      }

      return false;
    }

    return true;
  }

  @Override
  public final <R> R accept(ColorVisitor<R> visitor) {
    return visitor.visitColorHex(this);
  }

  @Override
  public final void acceptValueCreator(Creator creator) {
    creator.createColor(value);
  }

  @Override
  public final void acceptValueMarker(Marker marker) {
    marker.markColor(ColorKind.HEX);
  }

  public final String getHexString() {
    return value;
  }

  public final String getMinifiedHexString() {
    switch (value.length()) {
      case 7:
      case 9:
        return getMinifiedHexString0();
      default:
        return value;
    }
  }

  @Override
  public final int hashCode() {
    return value.hashCode();
  }

  @Override
  public final String toString() {
    return value;
  }

  @Override
  final boolean equalsImpl(Color obj) {
    ColorHex that = (ColorHex) obj;
    return value.equals(that.value);
  }

  private String getMinifiedHexString0() {
    StringBuilder result;
    result = new StringBuilder(9);

    result.append('#');

    char[] array;
    array = value.toCharArray();

    for (int i = 1; i < array.length; i = i + 2) {
      char current;
      current = array[i];

      char next;
      next = array[i + 1];

      char currentLower;
      currentLower = Character.toLowerCase(current);

      char nextLower;
      nextLower = Character.toLowerCase(next);

      if (currentLower != nextLower) {
        return value;
      }

      result.append(currentLower);
    }

    return result.toString();
  }

}