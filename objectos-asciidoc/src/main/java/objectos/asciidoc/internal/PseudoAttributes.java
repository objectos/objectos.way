/*
 * Copyright (C) 2021-2025 Objectos Software LTDA.
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

  private String[] named = new String[8];

  private int namedIndex;

  private String[] positional = new String[4];

  private int positionalIndex;

  private int state;

  PseudoAttributes(InternalSink sink) {
  }

  @Override
  public final String getNamed(String name) {
    Objects.requireNonNull(name, "name == null");

    String result = null;

    for (int i = 0; i < namedIndex; i += 2) {
      var candidate = named[i];

      if (name.equals(candidate)) {
        result = named[i + 1];

        break;
      }
    }

    return result;
  }

  @Override
  public final String getNamed(String name, String defaultValue) {
    Objects.requireNonNull(defaultValue, "defaultValue == null");

    var result = getNamed(name);

    if (result != null) {
      return result;
    } else {
      return defaultValue;
    }
  }

  public final String getPositional(int index) {
    int actualIndex = index - 1;

    if (actualIndex < 0) {
      return null;
    }

    if (actualIndex >= positionalIndex) {
      return null;
    }

    return positional[actualIndex];
  }

  public final boolean isEmpty() {
    return namedIndex == 0;
  }

  final void active() {
    set(ACTIVE);
  }

  final void addPositional(String value) {
    positional = ObjectArrays.growIfNecessary(positional, positionalIndex);
    positional[positionalIndex++] = value;
  }

  final Attributes bindIfNecessary(PseudoListingBlock block) {
    if (!is(BOUND)) {
      bindPseudoListingBlock();

      set(BOUND);
    }

    return this;
  }

  final Attributes bindIfNecessary(PseudoSection section) {
    if (!is(BOUND)) {
      bindPseudoSection();

      set(BOUND);
    }

    return this;
  }

  final void clear() {
    Arrays.fill(named, null);
    Arrays.fill(positional, null);

    namedIndex = positionalIndex = state = 0;
  }

  private void bindPseudoListingBlock() {
    var style = getNamed("style");

    if (style == null) {
      style = "listing";

      putNamedUnchecked("style", style);
    }

    var language = getPositional(2);

    if (language != null) {
      if (style.equals("listing")) {
        style = "source";

        replaceNamed("style", style);

        putNamedUnchecked("language", language);
      } else if (style.equals("source")) {
        putNamedUnchecked("language", language);
      }
    }
  }

  private void bindPseudoSection() {
    if (!hasNamed("style")) {
      var style = getPositional(1);

      putNamedUnchecked("style", style);
    }
  }

  private boolean hasNamed(String name) {
    return getNamed(name) != null;
  }

  private boolean is(int value) {
    return (state & value) != 0;
  }

  private void putNamedUnchecked(String name, String value) {
    named = ObjectArrays.growIfNecessary(named, namedIndex + 1);
    named[namedIndex++] = name;
    named[namedIndex++] = value;
  }

  private void replaceNamed(String name, String value) {
    for (int i = 0; i < namedIndex; i += 2) {
      var candidate = named[i];

      if (name.equals(candidate)) {
        named[i + 1] = value;

        break;
      }
    }
  }

  private void set(int value) {
    state |= value;
  }

}