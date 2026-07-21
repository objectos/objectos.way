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
package objectox.html.rec;

import java.util.Arrays;
import objectos.html.rec.Instruction;

final class Root {

  private int index;

  private final Instruction[] values;

  private Root(Instruction[] values) {
    this.values = values;
  }

  public static Root create(int initialSize) {
    final Instruction[] values;
    values = new Instruction[initialSize];

    return new Root(values);
  }

  public final <T extends Instruction> T add(T value) {
    if (index == values.length) {
      compactOrGrow();
    }

    final int idx;
    idx = index++;

    values[idx] = value;

    return value;
  }

  public final Instruction[] compile() {
    for (int idx = 0; idx < index; idx++) {
      final Instruction v;
      v = values[idx];

      if (!v.consumed()) {
        continue;
      }

      compact(idx);

      break;
    }

    return Arrays.copyOf(values, index);
  }

  private void compactOrGrow() {
    for (int idx = 0; idx < index; idx++) {
      final Instruction v;
      v = values[idx];

      if (!v.consumed()) {
        continue;
      }

      compact(idx);

      return;
    }

    grow();
  }

  private void compact(final int startIndex) {
    int slotIndex;
    slotIndex = startIndex;

    for (int idx = startIndex + 1; idx < index; idx++) {
      final Instruction v;
      v = values[idx];

      if (v.consumed()) {
        continue;
      }

      final int slot;
      slot = slotIndex++;

      values[slot] = v;
    }

    index = slotIndex;
  }

  private void grow() {
    throw new UnsupportedOperationException("Implement me");
  }

}
