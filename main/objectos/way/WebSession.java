/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

final class WebSession implements Web.Session {

  public final Web.Token id;

  private final Map<Object, Object> values = new HashMap<>();

  volatile Instant accessTime;

  volatile boolean valid = true;

  public WebSession(Web.Token id) {
    this.id = id;
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
  public final void put(String name, Object value) {
    synchronized (values) {
      values.put(name, value);
    }
  }

  @Override
  public final void remove(String name) {
    synchronized (values) {
      values.remove(name);
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
