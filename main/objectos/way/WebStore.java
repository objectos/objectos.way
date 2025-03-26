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
import java.util.HexFormat;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.random.RandomGenerator;

/**
 * The Objectos Way {@link SessionStore} implementation.
 */
final class WebStore implements Web.Store {

  private static final int ENCODED_LENGTH = 32;

  private final Clock clock;

  private final Duration cookieMaxAge;

  private final String cookieName;

  private final String cookiePath;

  private final Duration emptyMaxAge;

  private final RandomGenerator randomGenerator;

  private final ConcurrentMap<Web.Token, WebSession> sessions = new ConcurrentHashMap<>();

  WebStore(WebStoreConfig builder) {
    clock = builder.clock;

    cookieMaxAge = builder.cookieMaxAge;

    cookieName = builder.cookieName;

    cookiePath = builder.cookiePath;

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
  public final Web.Session createNext() {
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
  public final Web.Session get(Http.Request request) {
    final String cookieHeaderValue;
    cookieHeaderValue = request.header(Http.HeaderName.COOKIE); // implicit null-check

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

    if (encoded.length() != ENCODED_LENGTH) {
      return null;
    }

    final long high;
    high = fromHexDigitsToLong(encoded, 0, 16);

    final long low;
    low = fromHexDigitsToLong(encoded, 16, 32);

    final WebToken16 id;
    id = new WebToken16(high, low);

    return get(id);
  }

  private long fromHexDigitsToLong(CharSequence string, int fromIndex, int toIndex) {
    long value;
    value = 0L;

    for (int i = fromIndex; i < toIndex; i++) {
      final char c;
      c = string.charAt(i);

      final int iter;
      iter = HexFormat.fromHexDigit(c);

      value = (value << 4) + iter;
    }

    return value;
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
    final long high;
    high = randomGenerator.nextLong();

    final long low;
    low = randomGenerator.nextLong();

    return new WebToken16(high, low);
  }

}