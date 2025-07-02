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

import java.security.SecureRandom;
import java.time.Duration;
import java.time.InstantSource;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.random.RandomGenerator;

final class HttpSessionStoreBuilder implements Http.SessionStore.Options {

  Duration cookieMaxAge;

  String cookieName = "OBJECTOSWAY";

  String cookiePath = "/";

  boolean cookieSecure = true;

  RandomGenerator csrfGenerator;

  Duration emptyMaxAge = Duration.ofMinutes(5);

  InstantSource instantSource = InstantSource.system();

  RandomGenerator sessionGenerator;

  final ConcurrentMap<HttpToken, HttpSession> sessions = new ConcurrentHashMap<>();

  @Override
  public final void cookieName(String name) {
    cookieName = Objects.requireNonNull(name, "name == null");

    if (cookieName.isBlank()) {
      throw new IllegalArgumentException("Cookie name must not be blank");
    }
  }

  @Override
  public final void cookiePath(String path) {
    cookiePath = Objects.requireNonNull(path, "path == null");
  }

  @Override
  public final void cookieMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("Cookie max age must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("Cookie max age must not be negative");
    }

    cookieMaxAge = duration;
  }

  @Override
  public final void cookieSecure(boolean value) {
    cookieSecure = value;
  }

  @Override
  public final void csrfGenerator(RandomGenerator value) {
    if (csrfGenerator != null) {
      throw new IllegalStateException("csrfGenerator was already set");
    }

    csrfGenerator = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void emptyMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("emptyMaxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("emptyMaxAge must not be negative");
    }

    emptyMaxAge = duration;
  }

  @Override
  public final void instantSource(InstantSource value) {
    instantSource = Objects.requireNonNull(value, "value == null");
  }

  public final void session(HttpSession session) {
    final HttpToken key;
    key = session.id();

    sessions.put(key, session);
  }

  @Override
  public final void sessionGenerator(RandomGenerator value) {
    if (sessionGenerator != null) {
      throw new IllegalStateException("sessionGenerator was already set");
    }

    sessionGenerator = Objects.requireNonNull(value, "value == null");
  }

  final Http.SessionStore build() {
    RandomGenerator generator = null;

    if (csrfGenerator == null) {
      if (generator == null) {
        generator = new SecureRandom();
      }

      csrfGenerator = generator;
    }

    if (sessionGenerator == null) {
      if (generator == null) {
        generator = new SecureRandom();
      }

      sessionGenerator = generator;
    }

    return new HttpSessionStore(this);
  }

}