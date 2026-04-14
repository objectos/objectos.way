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
package objectos.http;

import java.time.Instant;
import java.time.InstantSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import objectos.way.Lang.Key;

final class HttpSession0 implements HttpSession {

  private Instant accessTime;

  private final HttpToken id;

  private final Lock lock = new ReentrantLock();

  private String setCookie;

  private Map<Object, Object> store = null;

  private boolean valid = true;

  HttpSession0(HttpToken id, String setCookie) {
    this.id = id;

    this.setCookie = setCookie;
  }

  HttpSession0(HttpToken id, Map<Object, Object> map) {
    this.id = id;

    setCookie = null;

    store = map;
  }

  @Override
  public final boolean sessionAbsent() {
    return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Class<T> key) {
    final String name;
    name = key.getName();

    return (T) get0(name);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Key<T> key) {
    Objects.requireNonNull(key, "key == null");

    return (T) get0(key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T sessionAttr(Class<T> key, T value) {
    final String name;
    name = key.getName();

    return (T) set0(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Key<T> key, T value) {
    Objects.requireNonNull(key, "key == null");

    return (T) set0(key, value);
  }

  @Override
  public final void sessionInvalidate() {
    invalidate();
  }

  @Override
  public final boolean sessionPresent() {
    return true;
  }

  public final Object get0(Object key) {
    lock.lock();
    try {
      checkValid();

      return store == null ? null : store.get(key);
    } finally {
      lock.unlock();
    }
  }

  public final Object set0(Object key, Object value) {
    lock.lock();
    try {
      checkValid();

      if (value == null) {
        return store == null ? null : store.remove(key);
      } else {
        if (store == null) {
          store = new HashMap<>();
        }

        return store.put(key, value);
      }
    } finally {
      lock.unlock();
    }
  }

  public final void invalidate() {
    lock.lock();
    try {
      checkValid();

      store = null;

      valid = false;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final boolean equals(Object obj) {
    // I think the first test should be enough
    // but better be safe than sorry I guess
    return obj == this || obj instanceof HttpSession0 that
        && id.equals(that.id);
  }

  @Override
  public final int hashCode() {
    return id.hashCode();
  }

  @Override
  public final String toString() {
    return "HttpSession[accessTime=" + accessTime + ";valid=" + valid + "]";
  }

  final String consumeSetCookie() {
    String s;
    s = setCookie;

    if (s != null) {

      lock.lock();
      try {
        s = setCookie;

        if (s != null) {
          setCookie = null;
        }
      } finally {
        lock.unlock();
      }

    }

    return s;
  }

  final HttpToken id() {
    return id;
  }

  final void touch(InstantSource source) {
    lock.lock();
    try {
      accessTime = source.instant();
    } finally {
      lock.unlock();
    }
  }

  final boolean valid() {
    return valid;
  }

  private void checkValid() {
    if (!valid) {
      throw new IllegalStateException("This operation can only be performed on a valid and active session");
    }
  }

}