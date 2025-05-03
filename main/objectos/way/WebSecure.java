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
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.random.RandomGenerator;

final class WebSecure implements Web.Secure {

  record Notes(
      Note.Ref1<Http.Request> invalidSession
  ) {

    static Notes create() {
      final Class<?> s;
      s = Web.Secure.class;

      return new Notes(
          Note.Ref1.create(s, "SES", Note.WARN)
      );
    }

  }

  private static final int SESSION_LENGTH = 32;

  private final Clock clock;

  private final Duration cookieMaxAge;

  private final String cookieName;

  private final String cookiePath;

  private boolean cookieSecure = true;

  private final Duration emptyMaxAge;

  private final Notes notes = Notes.create();

  private final Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  private final RandomGenerator randomGenerator;

  private final ConcurrentMap<Web.Token, WebSession> sessions = new ConcurrentHashMap<>();

  WebSecure(WebSecureBuilder builder) {
    clock = builder.clock;

    cookieMaxAge = builder.cookieMaxAge;

    cookieName = builder.cookieName;

    cookiePath = builder.cookiePath;

    cookieSecure = builder.cookieSecure;

    emptyMaxAge = builder.emptyMaxAge;

    randomGenerator = builder.randomGenerator;
  }

  public final void cleanUp() {
    Instant now;
    now = Instant.now(clock);

    Instant min;
    min = now.minus(emptyMaxAge);

    Collection<WebSession> values;
    values = sessions.values();

    for (WebSession session : values) {
      if (session.shouldCleanUp(min)) {
        sessions.remove(session.id);
      }
    }
  }

  public final void clear() {
    sessions.clear();
  }

  @Override
  public final Web.Session createSession() {
    WebSession session, maybeExisting;

    do {
      Web.Token id;
      id = nextId();

      session = new WebSession(id);

      maybeExisting = sessions.putIfAbsent(id, session);
    } while (maybeExisting != null);

    session.touch(clock);

    return session;
  }

  public final Web.Session get(Web.Token id) {
    WebSession session;
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
  public final Web.Token ensureCsrfToken(Web.Session session) {
    final WebSession impl;
    impl = (WebSession) session;

    Web.Token token;
    token = impl.csrf;

    if (token == null) {
      synchronized (impl) {
        token = impl.csrf;

        if (token == null) {
          token = nextId();

          impl.csrf = token;
        }
      }
    }

    return token;
  }

  @Override
  public final Web.Session ensureSession(Http.Exchange http) {
    final Web.Session session;

    final Web.Session maybeExisting;
    maybeExisting = getSession(http);

    if (maybeExisting != null) {
      session = maybeExisting;
    } else {
      session = createSession();
    }

    http.set(Web.Session.class, session);

    return session;
  }

  @Override
  public final Web.Session getSession(Http.Request http) {
    final String cookieHeaderValue;
    cookieHeaderValue = http.header(Http.HeaderName.COOKIE); // implicit null-check

    if (cookieHeaderValue == null) {
      return null;
    }

    final Http.Cookies cookies;
    cookies = Http.Cookies.parse(cookieHeaderValue);

    final String encoded;
    encoded = cookies.get(cookieName);

    if (encoded == null) {
      return null;
    }

    try {
      final Web.Token id;
      id = WebToken.parse(encoded, SESSION_LENGTH);

      return get(id);
    } catch (WebToken.ParseException e) {
      noteSink.send(notes.invalidSession, http);

      return null;
    }
  }

  @Override
  public final Http.SetCookie setCookie(Web.Session session) {
    final WebSession impl;
    impl = (WebSession) session;

    final Web.Token id;
    id = impl.id;

    return Http.SetCookie.create(set -> {
      set.name(cookieName);

      set.value(id.toString());

      set.httpOnly();

      if (cookieMaxAge != null) {
        set.maxAge(cookieMaxAge);
      }

      if (cookiePath != null) {
        set.path(cookiePath);
      }

      if (cookieSecure) {
        set.secure();
      }
    });
  }

  public final String setCookie(String id) {
    Objects.requireNonNull(id, "id == null");

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

  private Web.Token nextId() {
    return WebToken.of(randomGenerator, SESSION_LENGTH);
  }

}