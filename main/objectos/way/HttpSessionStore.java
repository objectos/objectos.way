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

import java.time.Duration;
import java.time.InstantSource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.random.RandomGenerator;

final class HttpSessionStore implements Http.SessionStore {

  record Notes(
      Note.Ref1<Http.Request> invalidSession
  ) {

    static Notes create() {
      final Class<?> s;
      s = Http.SessionStore.class;

      return new Notes(
          Note.Ref1.create(s, "SES", Note.WARN)
      );
    }

  }

  private static final int SESSION_LENGTH = 32;

  private final Duration cookieMaxAge;

  private final String cookieName;

  private final String cookiePath;

  private boolean cookieSecure = true;

  @SuppressWarnings("unused")
  private final Duration emptyMaxAge;

  private final InstantSource instantSource;

  private final Notes notes = Notes.create();

  private final Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  private final RandomGenerator randomGenerator;

  private final ConcurrentMap<HttpToken, HttpSession> sessions = new ConcurrentHashMap<>();

  HttpSessionStore(HttpSessionStoreBuilder builder) {
    cookieMaxAge = builder.cookieMaxAge;

    cookieName = builder.cookieName;

    cookiePath = builder.cookiePath;

    cookieSecure = builder.cookieSecure;

    emptyMaxAge = builder.emptyMaxAge;

    instantSource = builder.instantSource;

    randomGenerator = builder.randomGenerator;
  }

  @Override
  public final void ensureSession(Http.Exchange http) {
    final HttpExchange impl;
    impl = (HttpExchange) http;

    if (impl.sessionLoaded()) {
      return;
    }

    final HttpSession session;

    final HttpSession maybeExisting;
    maybeExisting = getSession(http);

    if (maybeExisting != null) {
      session = maybeExisting;
    } else {
      session = createSession();
    }

    impl.session(session);
  }

  @Override
  public final void loadSession(Http.Exchange http) {
    final HttpExchange impl;
    impl = (HttpExchange) http;

    if (impl.sessionLoaded()) {
      return;
    }

    final HttpSession session;
    session = findSession(impl);

    if (session != null) {
      impl.session(session);
    }
  }

  private HttpSession findSession(HttpExchange impl) {
    final String cookieHeaderValue;
    cookieHeaderValue = impl.header(Http.HeaderName.COOKIE); // implicit null-check

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
      final HttpToken id;
      id = HttpToken.parse(encoded, SESSION_LENGTH);

      return get(id);
    } catch (HttpToken.ParseException e) {
      noteSink.send(notes.invalidSession, impl);

      return null;
    }
  }

  // private API

  public final HttpSession createSession() {
    HttpSession session, maybeExisting;

    do {
      final HttpToken id;
      id = nextId();

      final String setCookie;
      setCookie = setCookie(id);

      session = new HttpSession(id, setCookie);

      maybeExisting = sessions.putIfAbsent(id, session);
    } while (maybeExisting != null);

    session.touch(instantSource);

    return session;
  }

  public final HttpSession get(HttpToken id) {
    HttpSession session;
    session = sessions.get(id);

    if (session == null) {
      return null;
    }

    if (!session.valid()) {
      return null;
    }

    session.touch(instantSource);

    return session;
  }

  public final HttpSession getSession(Http.Request http) {
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
      final HttpToken id;
      id = HttpToken.parse(encoded, SESSION_LENGTH);

      return get(id);
    } catch (HttpToken.ParseException e) {
      noteSink.send(notes.invalidSession, http);

      return null;
    }
  }

  private String setCookie(HttpToken id) {
    final HttpSetCookieConfig builder;
    builder = new HttpSetCookieConfig();

    builder.name(cookieName);

    builder.value(id.toString());

    builder.httpOnly();

    if (cookieMaxAge != null) {
      builder.maxAge(cookieMaxAge);
    }

    if (cookiePath != null) {
      builder.path(cookiePath);
    }

    if (cookieSecure) {
      builder.secure();
    }

    return builder.buildString();
  }

  private HttpToken nextId() {
    return HttpToken.of(randomGenerator, SESSION_LENGTH);
  }

}