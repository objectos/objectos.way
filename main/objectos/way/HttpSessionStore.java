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
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.random.RandomGenerator;
import objectos.way.Http.CsrfToken;
import objectos.way.HttpToken.ParseException;

final class HttpSessionStore implements Http.SessionStore {

  record Notes(
      Note.Ref1<Http.Request> invalidCsrf,
      Note.Ref1<Http.Request> invalidSession
  ) {

    static Notes create() {
      final Class<?> s;
      s = Http.SessionStore.class;

      return new Notes(
          Note.Ref1.create(s, "CSR", Note.WARN),
          Note.Ref1.create(s, "SES", Note.WARN)
      );
    }

  }

  private static final int CSRF_LENGTH = 32;

  private static final int SESSION_LENGTH = 32;

  private String cookieDomain;

  private ZonedDateTime cookieExpires;

  private final boolean cookieHttpOnly = true;

  private final Duration cookieMaxAge;

  private final String cookieName;

  private final String cookiePath;

  private Http.SameSite cookieSameSite;

  private boolean cookieSecure = true;

  private final RandomGenerator csrfGenerator;

  private final Media csrfInvalidResponse = Media.Bytes.textPlain("Invalid or missing CSRF token\n");

  private final String csrfParamName = "way-csrf-token";

  @SuppressWarnings("unused")
  private final Duration emptyMaxAge;

  private final InstantSource instantSource;

  private final Notes notes = Notes.create();

  private final Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  private final RandomGenerator sessionGenerator;

  private final ConcurrentMap<HttpToken, HttpSession> sessions = new ConcurrentHashMap<>();

  private final boolean skipCsrf = false;

  HttpSessionStore(HttpSessionStoreBuilder builder) {
    cookieMaxAge = builder.cookieMaxAge;

    cookieName = builder.cookieName;

    cookiePath = builder.cookiePath;

    cookieSecure = builder.cookieSecure;

    csrfGenerator = builder.csrfGenerator;

    emptyMaxAge = builder.emptyMaxAge;

    instantSource = builder.instantSource;

    sessionGenerator = builder.sessionGenerator;
  }

  @Override
  public final void ensureSession(Http.Exchange http) {
    final HttpExchange impl;
    impl = (HttpExchange) http;

    if (impl.sessionPresent()) {
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

    if (impl.sessionPresent()) {
      return;
    }

    final HttpSession session;
    session = findSession(impl);

    if (session != null) {
      impl.session(session);
    }
  }

  private static final Set<Http.Method> SAFE_METHODS = EnumSet.of(Http.Method.GET, Http.Method.HEAD);

  @Override
  public final void requireCsrfToken(Http.Exchange http) {
    final HttpExchange impl;
    impl = (HttpExchange) http;

    final Http.Method method;
    method = impl.method();

    if (SAFE_METHODS.contains(method)) {
      return;
    }

    // prefer from the header
    String encoded;
    encoded = impl.header(Http.HeaderName.WAY_CSRF_TOKEN);

    if (encoded == null) {
      // obtain from form param
      encoded = impl.formParam(csrfParamName);
    }

    HttpToken csrf;
    csrf = null;

    if (encoded != null) {
      try {
        csrf = HttpToken.parse(encoded, CSRF_LENGTH);
      } catch (ParseException e) {

      }
    }

    boolean valid;
    valid = false;

    if (csrf != null && impl.sessionPresent()) {
      final CsrfToken sessionToken;
      sessionToken = impl.sessionAttr(Http.CsrfToken.class);

      valid = csrf.equals(sessionToken);
    }

    if (!valid) {
      noteSink.send(notes.invalidCsrf, http);

      http.forbidden(csrfInvalidResponse);
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
      id = HttpToken.of(sessionGenerator, SESSION_LENGTH);

      final String setCookie;
      setCookie = setCookie(id);

      session = new HttpSession(id, setCookie);

      maybeExisting = sessions.putIfAbsent(id, session);
    } while (maybeExisting != null);

    session.touch(instantSource);

    if (!skipCsrf) {
      session.computeIfAbsent(Http.CsrfToken.class, () -> HttpToken.of(csrfGenerator, CSRF_LENGTH));
    }

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
    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append(cookieName);

    sb.append("=");

    sb.append(id.toString());

    if (cookieDomain != null) {
      sb.append("; Domain=");

      sb.append(cookieDomain);
    }

    if (cookieExpires != null) {
      sb.append("; Expires=");

      sb.append(Http.formatDate(cookieExpires));
    }

    if (cookieHttpOnly) {
      sb.append("; HttpOnly");
    }

    if (cookieMaxAge != null) {
      sb.append("; Max-Age=");

      sb.append(cookieMaxAge.getSeconds());
    }

    if (cookiePath != null) {
      sb.append("; Path=");

      sb.append(cookiePath);
    }

    if (cookieSameSite != null) {
      sb.append("; SameSite=");

      sb.append(cookieSameSite.text);
    }

    if (cookieSecure) {
      sb.append("; Secure");
    }

    return sb.toString();
  }

}