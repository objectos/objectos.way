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

import java.time.Duration;
import java.time.InstantSource;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.random.RandomGenerator;
import objectos.http.HttpToken.ParseException;
import objectos.internal.Ascii;
import objectos.internal.NoOpSinkSingleton;
import objectos.way.Media;
import objectos.way.Note;

final class HttpSessionStoreImpl implements HttpSessionLoader, HttpSessionStore {

  record Notes(
      Note.Ref1<HttpRequest> invalidCsrf,
      Note.Ref1<HttpRequest> invalidSession
  ) {

    static Notes create() {
      final Class<?> s;
      s = HttpSessionStore.class;

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

  @SuppressWarnings("unused")
  private final Media csrfInvalidResponse = Media.Bytes.textPlain("Invalid or missing CSRF token\n");

  private final String csrfParamName = "way-csrf-token";

  @SuppressWarnings("unused")
  private final Duration emptyMaxAge;

  private final InstantSource instantSource;

  private final Notes notes = Notes.create();

  private final Note.Sink noteSink = NoOpSinkSingleton.INSTANCE;

  private final RandomGenerator sessionGenerator;

  private final ConcurrentMap<HttpToken, HttpSession0> sessions;

  private final boolean skipCsrf = false;

  HttpSessionStoreImpl(HttpSessionStoreBuilder builder) {
    cookieMaxAge = builder.cookieMaxAge;

    cookieName = builder.cookieName;

    cookiePath = builder.cookiePath;

    cookieSecure = builder.cookieSecure;

    csrfGenerator = builder.csrfGenerator;

    emptyMaxAge = builder.emptyMaxAge;

    instantSource = builder.instantSource;

    sessionGenerator = builder.sessionGenerator;

    sessions = builder.sessions;
  }

  @Override
  public final HttpSession loadSession(HttpRequest request, HttpResponse response) {
    final HttpSession maybeExisting;
    maybeExisting = findSession(request);

    if (maybeExisting != null) {
      return maybeExisting;
    } else {
      return new HttpSession1(response, this);
    }
  }

  private static final Set<HttpMethod> SAFE_METHODS = EnumSet.of(HttpMethod.GET, HttpMethod.HEAD);

  @Override
  public final void requireCsrfToken(HttpExchange http) {
    final HttpMethod method;
    method = http.method();

    if (SAFE_METHODS.contains(method)) {
      return;
    }

    // prefer from the header
    String encoded;
    encoded = http.header(HttpHeaderName.WAY_CSRF_TOKEN);

    if (encoded == null) {
      // obtain from form param
      encoded = http.formParam(csrfParamName);
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

    if (csrf != null && http.sessionPresent()) {
      final HttpCsrfToken sessionToken;
      sessionToken = http.sessionAttr(HttpCsrfToken.class);

      valid = csrf.equals(sessionToken);
    }

    if (!valid) {
      noteSink.send(notes.invalidCsrf, http);

      http.error(HttpStatus.FORBIDDEN);
    }
  }

  private HttpSession0 findSession(HttpRequest impl) {
    final String cookie;
    cookie = impl.header(HttpHeaderName.COOKIE); // implicit null-check

    if (cookie == null) {
      return null;
    }

    enum Parser {
      START,

      TEST_NAME,

      SKIP_NAME,

      MAYBE_VALUE,

      TEST_VALUE,

      SKIP_VALUE;
    }

    // parser state
    Parser parser;
    parser = Parser.START;

    int startIndex = 0;

    final int len;
    len = cookie.length();

    final int cookieNameLength;
    cookieNameLength = cookieName.length();

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = cookie.charAt(idx);

      switch (parser) {
        case START -> {
          if (c == Ascii.SP || c == Ascii.TAB || c == ';') {
            parser = Parser.START;
          }

          else if (c == '=') {
            // empty name, skip value

            parser = Parser.SKIP_VALUE;
          }

          else {
            // safe as cookie name must not be blank
            final char firstChar;
            firstChar = cookieName.charAt(0);

            if (firstChar == c) {
              // continue from second char
              startIndex = 1;

              // first char matches name
              parser = Parser.TEST_NAME;
            } else {
              // first char does not match -> skip name
              parser = Parser.SKIP_NAME;
            }
          }
        }

        case TEST_NAME -> {
          if (c == '=') {
            if (startIndex == cookieNameLength) {
              // marks the equals sign
              startIndex = idx;

              parser = Parser.MAYBE_VALUE;
            } else {
              parser = Parser.SKIP_VALUE;
            }
          }

          else if (startIndex >= cookieNameLength) {
            // this name is longer than cookieName
            parser = Parser.SKIP_NAME;
          }

          else {
            final char currentChar;
            currentChar = cookieName.charAt(startIndex++);

            if (currentChar != c) {
              // current char does not match -> skip name
              parser = Parser.SKIP_NAME;
            } else {
              // current char matches
              parser = Parser.TEST_NAME;
            }
          }
        }

        case SKIP_NAME -> {
          if (c == '=') {
            parser = Parser.SKIP_VALUE;
          } else {
            parser = Parser.SKIP_NAME;
          }
        }

        case MAYBE_VALUE -> {
          startIndex = idx;

          if (c == ';') {
            // empty value
            parser = Parser.START;
          } else {
            parser = Parser.TEST_VALUE;
          }
        }

        case TEST_VALUE -> {
          if (c == ';') {
            final HttpSession0 maybe;
            maybe = findSession0(impl, cookie, startIndex, idx);

            if (maybe != null) {
              return maybe;
            }

            parser = Parser.START;
          } else {
            parser = Parser.TEST_VALUE;
          }
        }

        case SKIP_VALUE -> {
          if (c == ';') {
            parser = Parser.START;
          } else {
            parser = Parser.SKIP_VALUE;
          }
        }
      }
    }

    if (parser != Parser.TEST_VALUE) {
      return null;
    }

    return findSession0(impl, cookie, startIndex, len);
  }

  private HttpSession0 findSession0(HttpRequest impl, String cookie, int startIndex, int endIndex) {
    try {
      final String encoded;
      encoded = cookie.substring(startIndex, endIndex);

      final HttpToken id;
      id = HttpToken.parse(encoded, SESSION_LENGTH);

      return get(id);
    } catch (ParseException e) {
      noteSink.send(notes.invalidSession, impl);

      return null;
    }
  }

  public final HttpSession0 createSession() {
    HttpSession0 session, maybeExisting;

    do {
      final HttpToken id;
      id = HttpToken.of(sessionGenerator, SESSION_LENGTH);

      session = new HttpSession0(id);

      maybeExisting = sessions.putIfAbsent(id, session);
    } while (maybeExisting != null);

    session.touch(instantSource);

    if (!skipCsrf) {
      final Class<?> clazz;
      clazz = HttpCsrfToken.class;

      final String key;
      key = clazz.getName();

      session.set0(key, HttpToken.of(csrfGenerator, CSRF_LENGTH));
    }

    return session;
  }

  public final HttpSession0 get(HttpToken id) {
    HttpSession0 session;
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

  public final String setCookie(HttpSession0 session) {
    final HttpToken id;
    id = session.id();

    return setCookie(id);
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