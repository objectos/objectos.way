/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc.internal;

import java.util.Arrays;
import java.util.Objects;
import objectos.asciidoc.pseudom.Attributes;
import objectos.util.ObjectArrays;

public final class PseudoAttributes implements Attributes {

  private static final int ACTIVE = 1 << 0;

  private static final int BOUND = 1 << 1;

  @SuppressWarnings("unused")
  private final InternalSink sink;

  private String[] data = new String[8];

  private int index;

  private int state;

  PseudoAttributes(InternalSink sink) {
    this.sink = sink;
  }

  @Override
  public final String getOrDefault(String name, String defaultValue) {
    Objects.requireNonNull(name, "name == null");
    var result = defaultValue.toString(); // implicit null-check

    for (int i = 0; i < index; i += 2) {
      var candidate = data[i];

      if (name.equals(candidate)) {
        result = data[i + 1];

        break;
      }
    }

    return result;
  }

  @Override
  public final String getPositional(int index) {
    int thisIndex = index - 1;

    thisIndex *= 2;

    return data[thisIndex + 1];
  }

  public final boolean isEmpty() {
    return index == 0;
  }

  final void active() {
    set(ACTIVE);
  }

  final void add(String value) {
    addImpl(null, value);
  }

  final Attributes bindIfNecessary(PseudoInlineMacro macro) {
    if (!is(BOUND)) {
      set(BOUND);
    }

    return this;
  }

  final Attributes bindIfNecessary(PseudoSection section) {
    if (!is(BOUND)) {
      if (index > 1) {
        if (data[0] != null) {
          throw new UnsupportedOperationException("Implement me");
        }

        data[0] = "style";
      }

      set(BOUND);
    }

    return this;
  }

  final void clear() {
    Arrays.fill(data, null);

    index = 0;

    state = 0;
  }

  private void addImpl(String name, String value) {
    data = ObjectArrays.growIfNecessary(data, index + 1);
    data[index++] = name;
    data[index++] = value;
  }

  private boolean is(int value) {
    return (state & value) != 0;
  }

  private void set(int value) {
    state |= value;
  }

}