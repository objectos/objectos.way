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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import objectos.way.Y;
import objectos.y.RandomGeneratorY;

final class SessionLazyY {

  InstantSource instantSource = Y.clockFixed();

  RandomGenerator randomGenerator = RandomGeneratorY.ofLongs(1, 2, 3, 4);

  Map<HttpToken, SessionPojo> sessions = new HashMap<>();

  public static SessionLazy create(Consumer<? super SessionLazyY> opts) {
    final SessionLazyY y;
    y = new SessionLazyY();

    opts.accept(y);

    return y.build();
  }

  private SessionLazy build() {
    return new SessionLazy(
        SessionFactoryY.create(opts -> {
          opts.instantSource = instantSource;

          opts.randomGenerator = randomGenerator;

          opts.sessions = sessions;
        })
    );
  }

}
