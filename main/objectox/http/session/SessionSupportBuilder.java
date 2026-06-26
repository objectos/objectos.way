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
package objectox.http.session;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.InstantSource;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.random.RandomGenerator;
import objectos.http.SessionOptions;
import objectox.http.HttpToken;
import objectox.http.SameSite;

public final class SessionSupportBuilder implements SessionOptions {

  private String cookieDomain;

  private final boolean cookieHttpOnly = true;

  private Duration cookieMaxAge;

  private String cookieName = "WAY";

  private String cookiePath = "/";

  private SameSite cookieSameSite;

  private boolean cookieSecure = true;

  Duration emptyMaxAge = Duration.ofMinutes(5);

  private InstantSource instantSource = InstantSource.system();

  private RandomGenerator randomGenerator;

  public final SessionSupport build() {
    final SessionCookieParser sessionCookieParser;
    sessionCookieParser = new SessionCookieParser(cookieName);

    final ConcurrentHashMap<HttpToken, SessionPojo> sessions;
    sessions = new ConcurrentHashMap<>();

    final SessionFinder sessionFinder;
    sessionFinder = new SessionFinder(instantSource, sessions);

    final RandomGenerator _randomGenerator;
    _randomGenerator = randomGenerator != null ? randomGenerator : new SecureRandom();

    final SessionFactory sessionFactory;
    sessionFactory = new SessionFactory(instantSource, _randomGenerator, sessions);

    final SessionSetCookie sessionSetCookie;
    sessionSetCookie = new SessionSetCookie(cookieDomain, cookieHttpOnly, cookieMaxAge, cookieName, cookiePath, cookieSameSite, cookieSecure);

    return new SessionSupport(
        new SessionRequest(sessionCookieParser, sessionFinder),

        new SessionResponse(sessionFactory, sessionSetCookie)
    );
  }

  @Override
  public final void cookieName(String name) {
    final SessionCookieName sessionCookieName;
    sessionCookieName = new SessionCookieName(name);

    cookieName = sessionCookieName.validate();
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

  @Override
  public final void randomGenerator(RandomGenerator value) {
    if (randomGenerator != null) {
      throw new IllegalStateException("randomGenerator was already set");
    }

    randomGenerator = Objects.requireNonNull(value, "value == null");
  }

}