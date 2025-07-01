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

  private sealed interface Store {}

  private static final class Store1 implements Store {

    Object key1;

    Object value1;

    Store1(Object key1, Object value1) {
      this.key1 = key1;

      this.value1 = value1;
    }

  }

  private static final class Store2 implements Store {

    Object key1;

    Object value1;

    Object key2;

    Object value2;

    Store2(Store1 store, Object key2, Object value2) {
      this.key1 = store.key1;

      this.value1 = store.value1;

      this.key2 = key2;

      this.value2 = value2;
    }

  }

  private static final class StoreMap implements Store {

    private final Map<Object, Object> map;

    StoreMap(Map<Object, Object> map) {
      this.map = map;
    }

    StoreMap(Store2 store, Object key, Object value) {
      Map<Object, Object> map;
      map = Util.createMap();

      map.put(store.key1, store.value1);
      map.put(store.key2, store.value2);
      map.put(key, value);

      this.map = map;
    }

  }

  private Instant accessTime;

  private final HttpToken id;

  private final Lock lock = new ReentrantLock();

  private String setCookie;

  private Store store = null;

  private boolean valid = true;

  HttpSession(HttpToken id, String setCookie) {
    this.id = id;

    this.setCookie = setCookie;
  }

  HttpSession(Map<Object, Object> map) {
    id = HttpToken.of32(0, 0, 0, 0);

    setCookie = null;

    store = new StoreMap(map);
  }

  public final Object get0(Object key) {
    lock.lock();
    try {
      checkValid();

      return switch (store) {
        case null -> null;

        case Store1 store -> {
          if (store.key1.equals(key)) {
            yield store.value1;
          }

          yield null;
        }

        case Store2 store -> {
          if (store.key1.equals(key)) {
            yield store.value1;
          }

          if (store.key2.equals(key)) {
            yield store.value2;
          }

          yield null;
        }

        case StoreMap store -> store.map.get(key);
      };
    } finally {
      lock.unlock();
    }
  }

  public final Object set0(Object key, Object value) {
    lock.lock();
    try {

      checkValid();

      return switch (store) {
        case null -> {
          store = new Store1(key, value);

          yield null;
        }

        case Store1 impl -> {
          if (impl.key1.equals(key)) {
            final Object previous;
            previous = impl.value1;

            impl.value1 = value;

            yield previous;
          }

          store = new Store2(impl, key, value);

          yield null;
        }

        case Store2 impl -> {
          if (impl.key1.equals(key)) {
            final Object previous;
            previous = impl.value1;

            impl.value1 = value;

            yield previous;
          }

          if (impl.key2.equals(key)) {
            final Object previous;
            previous = impl.value2;

            impl.value2 = value;

            yield previous;
          }

          store = new StoreMap(impl, key, value);

          yield null;
        }

        case StoreMap impl -> {
          final Map<Object, Object> delegate;
          delegate = impl.map;

          yield delegate.put(key, value);
        }
      };
    } finally {
      lock.unlock();
    }
  }

  public final Object setIfAbsent0(Object key, Supplier<?> supplier) {
    lock.lock();
    try {

      checkValid();

      return switch (store) {
        case null -> {
          final Object value;
          value = get(supplier);

          store = new Store1(key, value);

          yield value;
        }

        case Store1 impl -> {
          if (impl.key1.equals(key)) {
            yield impl.value1;
          }

          final Object value;
          value = get(supplier);

          store = new Store2(impl, key, value);

          yield value;
        }

        case Store2 impl -> {
          if (impl.key1.equals(key)) {
            yield impl.value1;
          }

          if (impl.key2.equals(key)) {
            yield impl.value2;
          }

          final Object value;
          value = get(supplier);

          store = new StoreMap(impl, key, value);

          yield value;
        }

        case StoreMap impl -> {
          final Map<Object, Object> delegate;
          delegate = impl.map;

          final Object existing;
          existing = delegate.get(key);

          if (existing != null) {
            yield existing;
          }

          final Object value;
          value = get(supplier);

          delegate.put(key, value);

          yield value;
        }
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
