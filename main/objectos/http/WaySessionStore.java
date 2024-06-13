/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HexFormat;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import objectos.lang.object.Check;
import objectos.way.Http;

/**
 * The Objectos Way {@link SessionStore} implementation.
 */
public final class WaySessionStore implements SessionStore {

  private static final int ID_LENGTH_IN_BYTES = 16;

  private final Clock clock;

  private Duration cookieMaxAge;

  private String cookieName = "OBJECTOSWAY";

  private String cookiePath = "/";

  private Duration emptyMaxAge = Duration.ofMinutes(5);

  private final HexFormat hexFormat = HexFormat.of();

  private Random random = new SecureRandom();

  private final ConcurrentMap<String, WaySession> sessions = new ConcurrentHashMap<>();

  /**
   * Sole constructor.
   */
  public WaySessionStore() {
    this(Clock.systemDefaultZone());
  }

  // @VisibleForTesting
  WaySessionStore(Clock clock) {
    this.clock = clock;
  }

  /**
   * Use the specified {@code name} when setting the client session cookie.
   *
   * @param name
   *        the cookie name to use
   */
  public final void cookieName(String name) {
    this.cookieName = Check.notNull(name, "name == null");
  }

  /**
   * Sets the session cookie Path attribute to the specified value.
   *
   * @param path
   *        the value of the Path attribute
   */
  public final void cookiePath(String path) {
    this.cookiePath = Check.notNull(path, "path == null");
  }

  /**
   * Sets the session cookie Max-Age attribute to the specified value.
   *
   * @param duration
   *        the value of the Max-Age attribute
   */
  public final void cookieMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("maxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("maxAge must not be negative");
    }

    cookieMaxAge = duration;
  }

  /**
   * Discards empty sessions, during a {@link #cleanUp()} operation, whose last
   * access time is greater than the specified duration.
   * 
   * @param duration
   *        the duration value
   */
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

  /**
   * Use the specified {@link Random} instance for generating session IDs.
   *
   * @param random
   *        the {@link Random} instance to use
   *
   * @return this instance
   */
  public final WaySessionStore random(Random random) {
    this.random = Check.notNull(random, "random == null");

    return this;
  }

  /**
   * Adds the specified {@code session} to this session store.
   *
   * @param session
   *        the session instance to add
   *
   * @return this instance
   */
  public final WaySessionStore add(WaySession session) {
    String id;
    id = session.id();

    sessions.put(id, session);

    return this;
  }

  public final void cleanUp() {
    Instant now;
    now = Instant.now(clock);

    Instant min;
    min = now.minus(emptyMaxAge);

    Collection<WaySession> values;
    values = sessions.values();

    for (WaySession session : values) {
      if (session.shouldCleanUp(min)) {
        sessions.remove(session.id());
      }
    }
  }

  final void clear() {
    sessions.clear();
  }

  final WaySession put(String id, WaySession session) {
    Check.notNull(id, "id == null");
    Check.notNull(session, "session == null");

    return sessions.put(id, session);
  }

  @Override
  public final Session nextSession() {
    WaySession session, maybeExisting;

    do {
      String id;
      id = nextId();

      session = new WaySession(id);

      maybeExisting = sessions.putIfAbsent(id, session);
    } while (maybeExisting != null);

    return session;
  }

  @Override
  public final Session get(Http.Request.Cookies cookies) {
    String maybe;
    maybe = cookies.get(cookieName); // implicit cookies null check

    if (maybe == null) {
      return null;
    } else {
      return get(maybe);
    }
  }

  @Override
  public final Session get(String id) {
    WaySession session;
    session = sessions.get(id);

    if (session == null) {
      return null;
    }

    if (!session.valid) {
      return null;
    }

    session.touch(clock);

    return session;
  }

  @Override
  public final String setCookie(String id) {
    Check.notNull(id, "id == null");

    StringBuilder s;
    s = new StringBuilder();

    s.append(cookieName);

    s.append('=');

    s.append(id);

    if (cookieMaxAge != null) {
      s.append("; Max-Age=");

      s.append(cookieMaxAge.getSeconds());
    }

    if (cookiePath != null) {
      s.append("; Path=");

      s.append(cookiePath);
    }

    return s.toString();
  }

  private String nextId() {
    byte[] bytes;
    bytes = new byte[ID_LENGTH_IN_BYTES];

    random.nextBytes(bytes);

    return hexFormat.formatHex(bytes);
  }

}