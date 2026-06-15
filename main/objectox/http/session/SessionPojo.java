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

import java.time.Instant;
import java.time.InstantSource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import objectos.lang.Key;

public final class SessionPojo implements Session {

  private enum Private {

    UNSET_COOKIE;

  }

  static final int SESSION_LENGTH = 32;

  private Instant accessTime;

  private final Map<Object, Object> attributes;

  private final Lock lock = new ReentrantLock();

  public SessionPojo(Map<Object, Object> attributes) {
    this.attributes = attributes;
  }

  public final Instant accessTime() {
    return accessTime;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T attr(Class<T> key) {
    final String name;
    name = key.getName();

    return (T) get0(name);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T attr(Key<T> key) {
    Objects.requireNonNull(key, "key == null");

    return (T) get0(key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T attr(Class<T> key, T value) {
    final String name;
    name = key.getName();

    return (T) set0(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T attr(Key<T> key, T value) {
    Objects.requireNonNull(key, "key == null");

    return (T) set0(key, value);
  }

  @Override
  public final void invalidate() {
    lock.lock();
    try {
      accessTime = null;

      attributes.clear();

      attributes.put(Private.UNSET_COOKIE, Private.UNSET_COOKIE);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final boolean isPresent() {
    return true;
  }

  public final boolean shouldUnset() {
    return attributes.remove(Private.UNSET_COOKIE) == Private.UNSET_COOKIE;
  }

  public final void touch(InstantSource source) {
    lock.lock();
    try {
      accessTime = source.instant();
    } finally {
      lock.unlock();
    }
  }

  public final boolean valid() {
    lock.lock();
    try {
      return accessTime != null;
    } finally {
      lock.unlock();
    }
  }

  private Object get0(Object key) {
    lock.lock();
    try {
      return attributes.get(key);
    } finally {
      lock.unlock();
    }
  }

  private Object set0(Object key, Object value) {
    lock.lock();
    try {
      return attributes.put(key, value);
    } finally {
      lock.unlock();
    }
  }

}
