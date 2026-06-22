/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http;

final class HeaderCaseFormatter {

  private final String input;

  HeaderCaseFormatter(String input) {
    this.input = input;
  }

  public final String format() {
    final StringBuilder sb;
    sb = new StringBuilder();

    boolean capitalizeNext;
    capitalizeNext = true;

    for (int i = 0, len = input.length(); i < len; i++) {
      final char lower;
      lower = input.charAt(i);

      if (lower == '-') {
        sb.append('-');

        capitalizeNext = true;
      } else if (capitalizeNext) {
        final char upper;
        upper = Character.toUpperCase(lower);

        sb.append(upper);

        capitalizeNext = false;
      } else {
        sb.append(lower);
      }
    }

    return sb.toString();
  }

}
