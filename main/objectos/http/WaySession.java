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
package objectos.http;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import objectos.lang.object.Check;

public final class WaySession implements Session {

  private final String id;

  private final Map<String, Object> values = new HashMap<>();

  volatile Instant accessTime;
  
  volatile boolean valid = true;

  public WaySession(String id) {
    this.id = Check.notNull(id, "id == null");
  }

  @Override
  public final String id() {
    return id;
  }

  @Override
  public final <T> T get(Class<T> type) {
    String name;
    name = type.getName(); // implicit null-check

    Object value;
    value = null;

    synchronized (values) {
      value = values.get(name);
    }

    return type.cast(value);
  }

  @Override
  public final <T> Object put(Class<T> type, T value) {
    String name;
    name = type.getName(); // implicit null-check

    synchronized (values) {
      return values.put(name, value);
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

}