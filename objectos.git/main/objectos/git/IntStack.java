/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import objectos.lang.object.Check;
import objectos.util.array.IntArrays;

final class IntStack {

  private int size;

  private int[] values;

  public IntStack() {
    values = IntArrays.empty();
  }

  public IntStack(int initialCapacity) {
    values = new int[initialCapacity];
  }

  public final void clear() {
    size = 0;
  }

  public final boolean isEmpty() {
    return size == 0;
  }

  public final int peek() {
    Check.state(size > 0, "stack is empty");

    return values[size - 1];
  }

  public final int pop() {
    Check.state(size > 0, "stack is empty");

    return values[--size];
  }

  public final void push(int value) {
    values = IntArrays.growIfNecessary(values, size);

    values[size++] = value;
  }

  public final int size() {
    return size;
  }

}