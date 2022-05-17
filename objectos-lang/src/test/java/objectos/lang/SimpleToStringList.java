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

import java.util.Arrays;

final class SimpleToStringList implements ToStringObject {

  private final int size;

  SimpleToStringList(int size) {
    this.size = size;
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.formatStart(sb, this);

    if (size > 0) {
      ToString.formatFirstPair(sb, depth, "0", "x");

      for (int i = 1; i < size; i++) {
        char[] c;
        c = new char[i + 1];

        Arrays.fill(c, 'x');

        ToString.formatNextPair(
            sb, depth,
            Integer.toString(i), new String(c)
        );
      }
    }

    ToString.formatEnd(sb, depth);
  }

  @Override
  public final String toString() {
    return ToString.toString(this);
  }

}