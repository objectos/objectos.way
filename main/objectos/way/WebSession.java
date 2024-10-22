/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.way;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class WebSession implements Web.Session {

  private final String id;

  private final Map<Object, Object> values = new HashMap<>();

  volatile Instant accessTime;

  volatile boolean valid = true;

  public WebSession(String id) {
    this.id = Objects.requireNonNull(id, "id == null");
  }

  @Override
  public final String id() {
    return id;
  }

  @Override
  public final <T> T get(Class<T> type) {
    String key;
    key = type.getName();

    Object value;
    value = null;

    synchronized (values) {
      value = values.get(key);
    }

    return type.cast(value);
  }

  @Override
  public final <T> Object put(Class<T> type, T value) {
    String key;
    key = type.getName();

    synchronized (values) {
      return values.put(key, value);
    }
  }

  @Override
  public final Object get(String name) {
    synchronized (values) {
      return values.get(name);
    }
  }

  @Override
  public final Object put(String name, Object value) {
    synchronized (values) {
      return values.put(name, value);
    }
  }

  @Override
  public final Object remove(String name) {
    synchronized (values) {
      return values.remove(name);
    }
  }

  @Override
  public final void invalidate() {
    valid = false;
  }

  final void touch(Clock clock) {
    accessTime = Instant.now(clock);
  }

  final boolean shouldCleanUp(Instant min) {
    if (!valid) {
      return true;
    }

    if (!values.isEmpty()) {
      return false;
    }

    if (accessTime == null) {
      return false;
    }

    return accessTime.isBefore(min);
  }

}