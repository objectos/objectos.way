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
package objectos.way;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HexFormat;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import objectos.lang.object.Check;
import objectos.way.Session.Repository;

/**
 * The Objectos Way {@link SessionStore} implementation.
 */
final class SessionRepository implements Session.Repository {

  static class Builder {

    Clock clock = Clock.systemDefaultZone();

    Duration cookieMaxAge;

    String cookieName = "OBJECTOSWAY";

    String cookiePath = "/";

    Duration emptyMaxAge = Duration.ofMinutes(5);

    Random random = new SecureRandom();

    public final Repository build() {
      return new SessionRepository(this);
    }

  }

  private static final int ID_LENGTH_IN_BYTES = 16;

  private final Clock clock;

  private final Duration cookieMaxAge;

  private final String cookieName;

  private final String cookiePath;

  private final Duration emptyMaxAge;

  private final HexFormat hexFormat = HexFormat.of();

  private final Random random;

  private final ConcurrentMap<String, SessionInstance> sessions = new ConcurrentHashMap<>();

  SessionRepository(Builder builder) {
    clock = builder.clock;

    cookieMaxAge = builder.cookieMaxAge;

    cookieName = builder.cookieName;

    cookiePath = builder.cookiePath;

    emptyMaxAge = builder.emptyMaxAge;

    random = builder.random;
  }

  @Override
  public final void cleanUp() {
    Instant now;
    now = Instant.now(clock);

    Instant min;
    min = now.minus(emptyMaxAge);

    Collection<SessionInstance> values;
    values = sessions.values();

    for (SessionInstance session : values) {
      if (session.shouldCleanUp(min)) {
        sessions.remove(session.id());
      }
    }
  }

  public final void clear() {
    sessions.clear();
  }

  final SessionInstance put(String id, SessionInstance session) {
    Check.notNull(id, "id == null");
    Check.notNull(session, "session == null");

    return sessions.put(id, session);
  }

  @Override
  public final Session.Instance createNext() {
    SessionInstance session, maybeExisting;

    do {
      String id;
      id = nextId();

      session = new SessionInstance(id);

      maybeExisting = sessions.putIfAbsent(id, session);
    } while (maybeExisting != null);

    return session;
  }

  @Override
  public final void filter(Http.Exchange http) {
    Http.Request.Headers headers;
    headers = http.headers();

    String cookieHeaderValue;
    cookieHeaderValue = headers.first(Http.COOKIE);

    if (cookieHeaderValue == null) {
      return;
    }

    Http.Request.Cookies cookies;
    cookies = Http.parseCookies(cookieHeaderValue);

    String id;
    id = cookies.get(cookieName);

    if (id == null) {
      return;
    }

    Session.Instance session;
    session = get(id);

    if (session == null) {
      return;
    }

    http.set(Session.Instance.class, session);
  }

  @Override
  public final Session.Instance get(String id) {
    SessionInstance session;
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

  @Override
  public final Session.Instance store(Session.Instance session) {
    String id;
    id = session.id();

    return sessions.put(id, (SessionInstance) session);
  }

  private String nextId() {
    byte[] bytes;
    bytes = new byte[ID_LENGTH_IN_BYTES];

    random.nextBytes(bytes);

    return hexFormat.formatHex(bytes);
  }

}