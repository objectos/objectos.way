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

import objectos.way.Lang.Key;

final class HttpSession1 implements HttpSession {

  private final HttpResponse response;

  private HttpSession session;

  private final HttpSessionStoreImpl store;

  HttpSession1(HttpResponse response, HttpSessionStoreImpl store) {
    this.response = response;

    this.store = store;
  }

  @Override
  public final boolean sessionAbsent() {
    return session == null;
  }

  @Override
  public final <T> T sessionAttr(Class<T> key) {
    final HttpSession s;
    s = session;

    return s != null ? s.sessionAttr(key) : null;
  }

  @Override
  public final <T> T sessionAttr(Key<T> key) {
    final HttpSession s;
    s = session;

    return s != null ? s.sessionAttr(key) : null;
  }

  @Override
  public final <T> T sessionAttr(Class<T> key, T value) {
    ensureSession();

    return session.sessionAttr(key, value);
  }

  @Override
  public final <T> T sessionAttr(Key<T> key, T value) {
    ensureSession();

    return session.sessionAttr(key, value);
  }

  @Override
  public final void sessionInvalidate() {
    final HttpSession s;
    s = session;

    if (s != null) {
      s.sessionInvalidate();
    }
  }

  @Override
  public final boolean sessionPresent() {
    return session != null;
  }

  private void ensureSession() {
    if (session == null) {
      final HttpSession0 created;
      created = store.createSession();

      final String setCookie;
      setCookie = store.setCookie(created);

      response.header(HttpHeaderName.SET_COOKIE, setCookie);

      session = created;
    }
  }

}