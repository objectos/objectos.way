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
package objectox.http.req;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import objectos.lang.Key;

public final class RequestAttributes {

  private Map<Object, Object> map;

  public RequestAttributes() {
    map = Map.of();
  }

  RequestAttributes(Map<Object, Object> map) {
    this.map = map;
  }

  @SuppressWarnings("unchecked")
  public final <T> T get(Class<T> key) {
    final String name;
    name = key.getName(); // implicit null check

    return (T) map.get(name);
  }

  @SuppressWarnings("unchecked")
  public final <T> T get(Key<T> key) {
    Objects.requireNonNull(key, "key == null");

    return (T) map.get(key);
  }

  @SuppressWarnings("unchecked")
  public final <T> T getOrDefault(Key<T> key, T defaultValue) {
    return (T) map.getOrDefault(key, defaultValue);
  }

  public final <T> void set(Class<T> key, T value) {
    final String name;
    name = key.getName(); // implicit null check

    Objects.requireNonNull(value, "value == null");

    if (map.isEmpty()) {
      map = new HashMap<>();
    }

    map.put(name, value);
  }

  public final <T> void set(Key<T> key, T value) {
    Objects.requireNonNull(key, "key == null");
    Objects.requireNonNull(value, "value == null");

    if (map.isEmpty()) {
      map = new HashMap<>();
    }

    map.put(key, value);
  }

}
