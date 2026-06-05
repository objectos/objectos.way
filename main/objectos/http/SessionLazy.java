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

import objectos.lang.Key;

final class SessionLazy implements Session {

  private SessionPojo session;

  private final SessionFactory sessionFactory;

  SessionLazy(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public final HttpToken id() {
    return session != null ? session.id() : null;
  }

  @Override
  public final <T> T attr(Class<T> key) {
    final SessionPojo s;
    s = session;

    return s != null ? s.attr(key) : null;
  }

  @Override
  public final <T> T attr(Key<T> key) {
    final SessionPojo s;
    s = session;

    return s != null ? s.attr(key) : null;
  }

  @Override
  public final <T> T attr(Class<T> key, T value) {
    ensureSession();

    return session.attr(key, value);
  }

  @Override
  public final <T> T attr(Key<T> key, T value) {
    ensureSession();

    return session.attr(key, value);
  }

  @Override
  public final boolean isPresent() {
    return session != null;
  }

  private void ensureSession() {
    if (session == null) {
      session = sessionFactory.next();
    }
  }

}
