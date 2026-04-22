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
package objectos.lang;

import java.util.Objects;

/**
 * A typed key for associating values in map-like data structures.
 *
 * @param <T> the type of the objects to be mapped to the key
 */
public sealed interface Key<T> permits Key0 {

  /**
   * Creates a new key instance whose equality is based on the
   * specified {@code unique} object. In other words, two key instances are
   * equal if, and only if, their {@code unique} objects are also equal. As a
   * consequence, and due to Java's type system nature, the following two key
   * instances are equal to each other:
   *
   * <pre>{@code
   * Key<String> a = Key.of("KEY");
   * Key<LocalDate> b = Key.of("KEY");
   * }</pre>
   *
   * <p>
   * As {@code "KEY".equals("KEY")} evaluates to {@code true}, keys {@code a}
   * and {@code b} are equal to each other, even though they declare distinct
   * type arguments.
   *
   * @param <T> the type of the objects to be mapped to this key
   *
   * @param unique
   *        an object used to distinguish key instance
   *
   * @return a newly created key
   */
  static <T> Key<T> of(Object unique) {
    Objects.requireNonNull(unique, "unique == null");

    return new Key0<>(unique);
  }

}