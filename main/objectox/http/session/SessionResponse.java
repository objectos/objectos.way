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

import java.util.function.BiConsumer;
import objectos.http.Request;
import objectox.http.HttpStatus0;
import objectox.http.HttpToken;
import objectox.http.resp.ResponsePojo;

final class SessionResponse implements BiConsumer<Request, ResponsePojo> {

  private final SessionFactory sessionFactory;

  private final SessionSetCookie sessionSetCookie;

  private final String sessionUnsetCookie;

  SessionResponse(SessionFactory sessionFactory, SessionSetCookie sessionSetCookie) {
    this.sessionFactory = sessionFactory;

    this.sessionSetCookie = sessionSetCookie;

    sessionUnsetCookie = sessionSetCookie.forInvalid();
  }

  @Override
  public final void accept(Request request, ResponsePojo response) {
    final Session session;
    session = request.attr(Session.KEY);

    switch (session) {
      case SessionLazy lazy -> acceptLazy(response, lazy);

      case SessionPojo pojo -> acceptPojo(response, pojo);

      case null, default -> {}
    }
  }

  private void acceptLazy(ResponsePojo response, SessionLazy lazy) {
    final HttpStatus0 status;
    status = response.status();

    if (status.isError()) {
      return;
    }

    if (!lazy.isPresent()) {
      return;
    }

    final HttpToken next;
    next = sessionFactory.next(lazy);

    final String token;
    token = next.toString();

    final String setCookie;
    setCookie = sessionSetCookie.forValid(token);

    response.setCookie(setCookie);
  }

  private void acceptPojo(ResponsePojo response, SessionPojo pojo) {
    if (pojo.shouldUnset()) {
      response.setCookie(sessionUnsetCookie);
    }
  }

}
