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
import java.util.random.RandomGenerator;

final class SessionFactory {

  private final InstantSource instantSource;

  private final RandomGenerator randomGenerator;

  private final Map<HttpToken, SessionPojo> sessions;

  SessionFactory(InstantSource instantSource, RandomGenerator randomGenerator, Map<HttpToken, SessionPojo> sessions) {
    this.instantSource = instantSource;

    this.randomGenerator = randomGenerator;

    this.sessions = sessions;
  }

  public final SessionPojo next() {
    SessionPojo session, maybeExisting;

    do {
      final HttpToken id;
      id = HttpToken.of(randomGenerator, SessionPojo.SESSION_LENGTH);

      session = new SessionPojo(id);

      maybeExisting = sessions.putIfAbsent(id, session);
    } while (maybeExisting != null);

    session.touch(instantSource);

    return session;
  }

}
