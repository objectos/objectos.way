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

import java.time.InstantSource;
import java.util.Map;
import java.util.function.Supplier;
import objectos.http.HttpToken.ParseException;

final class SessionFinder {

  private final InstantSource instantSource;

  private final Map<HttpToken, SessionPojo> sessions;

  SessionFinder(InstantSource instantSource, Map<HttpToken, SessionPojo> sessions) {
    this.instantSource = instantSource;

    this.sessions = sessions;
  }

  public final Session findOr(String id, Supplier<Session> supplier) {
    try {
      final HttpToken key;
      key = HttpToken.parse(id, SessionPojo.SESSION_LENGTH);

      if (key == null) {
        return supplier.get();
      }

      final SessionPojo existing;
      existing = sessions.get(key);

      if (existing == null) {
        return supplier.get();
      }

      existing.touch(instantSource);

      return existing;
    } catch (ParseException e) {
      return supplier.get();
    }
  }

}
