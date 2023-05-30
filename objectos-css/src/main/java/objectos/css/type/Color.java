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

public abstract class Color extends GeneratedColor implements ColorType, Value {

  Color() {}

  public static ColorHex hex(String text) {
    Check.argument(text.startsWith("#"), "Hex colors must start with #");
    return new ColorHex(text);
  }

  public static Color rgb(int hex) {
    checkHex(hex >= 0x0, hex);
    checkHex(hex <= 0xffffff, hex);
    String s = Integer.toHexString(hex);
    switch (s.length()) {
      case 1:
        return hex("#00000" + s);
      case 2:
        return hex("#0000" + s);
      case 3:
        return hex("#000" + s);
      case 4:
        return hex("#00" + s);
      case 5:
        return hex("#0" + s);
      case 6:
        return hex("#" + s);
      default:
        throw new AssertionError(s);
    }
  }

  private static void checkHex(boolean condition, int hex) {
    if (!condition) {
      throw new IllegalArgumentException("Invalid hex color value #" + Integer.toHexString(hex));
    }
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Color)) {
      return false;
    }
    Color that = (Color) obj;
    return getClass().equals(that.getClass())
        && equalsImpl(that);
  }

  @Override
  public abstract int hashCode();

  @Override
  public abstract String toString();

  abstract boolean equalsImpl(Color obj);

}