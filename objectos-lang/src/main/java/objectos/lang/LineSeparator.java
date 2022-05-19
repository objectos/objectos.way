/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
package objectos.lang;

/**
 * The value of the {@code line.separator} system property.
 *
 * @since 0.2
 */
public final class LineSeparator {

  static final String VALUE = System.lineSeparator();

  private LineSeparator() {}

  /**
   * Returns the value of the {@code line.separator} system property.
   *
   * @return the value of the {@code line.separator} system property
   */
  public static final String get() {
    return VALUE;
  }

  /**
   * Returns a new string by joining together the specified lines with this line
   * separator.
   *
   * @param lines
   *        the lines to be joined together
   *
   * @return a new
   */
  public static String join(CharSequence... lines) {
    if (lines == null) {
      throw new NullPointerException("lines == null");
    }

    switch (lines.length) {
      case 0:
        return "";
      case 1:
        CharSequence first;
        first = lines[0];

        if (first == null) {
          throw new NullPointerException("lines[0] == null");
        }

        return first.toString();
      default:
        first = lines[0];

        if (first == null) {
          throw new NullPointerException("lines[0] == null");
        }

        StringBuilder s;
        s = new StringBuilder(first);

        for (int i = 1; i < lines.length; i++) {
          s.append(VALUE);

          CharSequence l;
          l = lines[i];

          if (l == null) {
            throw new NullPointerException("lines[" + i + "] == null");
          }

          s.append(l);
        }

        return s.toString();
    }
  }

}