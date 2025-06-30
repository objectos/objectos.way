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

import java.time.Instant;
import java.time.InstantSource;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

final class HttpSession {

  private enum State {

    EMPTY,

    SINGLE,

    DUO,

    MAP;

  }

  private record Store1(Object key1, Object value1) {

    final boolean containsKey(Object key) {
      return key1.equals(key);
    }

    final Store2 add(Object key2, Object value2) {
      return new Store2(key1, value1, key2, value2);
    }

    final Object get(Object key) {
      if (key1.equals(key)) {
        return value1;
      }

      return null;
    }

  }

  private record Store2(Object key1, Object value1, Object key2, Object value2) {

    final boolean containsKey(Object key) {
      return key1.equals(key) || key2.equals(key);
    }

    final Map<Object, Object> add(Object key3, Object value3) {
      Map<Object, Object> map;
      map = Util.createMap();

      map.put(key1, value1);
      map.put(key2, value2);
      map.put(key3, value3);

      return map;
    }

    final Object get(Object key) {
      if (key1.equals(key)) {
        return value1;
      }

      if (key2.equals(key)) {
        return value2;
      }

      return null;
    }

  }

  private volatile Instant accessTime;

  private final HttpToken id;

  private final Lock lock = new ReentrantLock();

  private volatile String setCookie;

  private State state = State.EMPTY;

  private Object store;

  private volatile boolean valid = true;

  HttpSession(HttpToken id, String setCookie) {
    this.id = id;

    this.setCookie = setCookie;
  }

  HttpSession(Map<Object, Object> map) {
    id = HttpToken.of32(0, 0, 0, 0);

    setCookie = null;

    state = State.MAP;

    store = map;
  }

  public final void computeIfAbsent(Class<?> clazz, Supplier<?> supplier) {
    final String key;
    key = clazz.getName();

    computeIfAbsent0(key, supplier);
  }

  public final void computeIfAbsent(Lang.Key<?> key, Supplier<?> supplier) {
    computeIfAbsent0(key, supplier);
  }

  private void computeIfAbsent0(Object key, Supplier<?> supplier) {
    lock.lock();
    try {

      switch (state) {
        case EMPTY -> {
          store = new Store1(key, get(supplier));

          state = State.SINGLE;
        }

        case SINGLE -> {
          final Store1 single;
          single = single();

          if (!single.containsKey(key)) {
            store = single.add(key, get(supplier));

            state = State.DUO;
          }
        }

        case DUO -> {
          final Store2 duo;
          duo = duo();

          if (!duo.containsKey(key)) {
            store = duo.add(key, get(supplier));

            state = State.MAP;
          }
        }

        case MAP -> {
          final Map<Object, Object> map;
          map = map();

          if (!map.containsKey(key)) {
            map.put(key, get(supplier));
          }
        }
      }

    } finally {
      lock.unlock();
    }
  }

  public final <T> T get(Class<T> clazz) {
    final String key;
    key = clazz.getName();

    return get0(key);
  }

  public final <T> T get(Lang.Key<T> key) {
    return get0(key);
  }

  @SuppressWarnings("unchecked")
  private <T> T get0(Object key) {
    lock.lock();
    try {
      return (T) switch (state) {
        case EMPTY -> null;

        case SINGLE -> single().get(key);

        case DUO -> duo().get(key);

        case MAP -> map().get(key);
      };
    } finally {
      lock.unlock();
    }
  }

  private Object get(Supplier<?> supplier) {
    final Object value;
    value = supplier.get();

    if (value == null) {
      throw new NullPointerException("supplier provided a null value");
    }

    return value;
  }

  private Store1 single() {
    return (Store1) store;
  }

  private Store2 duo() {
    return (Store2) store;
  }

  @SuppressWarnings("unchecked")
  private Map<Object, Object> map() {
    return (Map<Object, Object>) store;
  }

  public final void invalidate() {
    valid = false;
  }

  @Override
  public final boolean equals(Object obj) {
    // I think the first test should be enough
    // but better be safe than sorry I guess
    return obj == this || obj instanceof HttpSession that
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
    accessTime = source.instant();
  }

  final boolean valid() {
    return valid;
  }

}
