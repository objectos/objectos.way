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

import java.util.function.Consumer;

final class SessionRequest implements Consumer<Request> {

  private final SessionCookieParser sessionCookieParser;

  private final SessionFactory sessionFactory;

  private final SessionFinder sessionFinder;

  SessionRequest(SessionCookieParser sessionCookieParser, SessionFactory sessionFactory, SessionFinder sessionFinder) {
    this.sessionCookieParser = sessionCookieParser;

    this.sessionFactory = sessionFactory;

    this.sessionFinder = sessionFinder;
  }

  @Override
  public final void accept(Request request) {
    final String cookieValue;
    cookieValue = request.header(HttpHeaderName.COOKIE);

    Session session;
    session = sessionCookieParser.parse(cookieValue, sessionFinder::find);

    if (session == null) {
      session = new SessionLazy(sessionFactory);
    }

    request.attr(Session.KEY, session);
  }

}
