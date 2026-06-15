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
package objectox.http.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import objectos.lang.Key;

final class SessionLazy implements Session {

  private Map<Object, Object> attributes = Map.of();

  SessionLazy() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T attr(Class<T> key) {
    final String name;
    name = key.getName();

    return (T) attributes.get(name);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T attr(Key<T> key) {
    final Key<T> k;
    k = Objects.requireNonNull(key, "key == null");

    return (T) attributes.get(k);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T attr(Class<T> key, T value) {
    final String name;
    name = key.getName();

    if (attributes.isEmpty()) {
      attributes = new HashMap<>();
    }

    return (T) attributes.put(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T attr(Key<T> key, T value) {
    final Key<T> k;
    k = Objects.requireNonNull(key, "key == null");

    if (attributes.isEmpty()) {
      attributes = new HashMap<>();
    }

    return (T) attributes.put(k, value);
  }

  public final Map<Object, Object> attributes() {
    return attributes;
  }

  @Override
  public final void invalidate() {
    if (!attributes.isEmpty()) {
      attributes.clear();
    }
  }

  @Override
  public final boolean isPresent() {
    return !attributes.isEmpty();
  }

}
