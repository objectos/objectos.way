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

import java.time.InstantSource;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import objectox.http.HttpToken;

final class SessionFinderY {

  InstantSource instantSource;

  Map<HttpToken, SessionPojo> sessions;

  public static SessionFinder create(Consumer<? super SessionFinderY> opts) {
    final SessionFinderY y;
    y = new SessionFinderY();

    opts.accept(y);

    return y.build();
  }

  public final void session(HttpToken id, SessionPojo pojo) {
    if (sessions == null) {
      sessions = new HashMap<>();
    }

    sessions.put(id, pojo);
  }

  private SessionFinder build() {
    return new SessionFinder(
        instantSource,

        sessions
    );
  }

}
