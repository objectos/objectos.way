/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import org.testng.annotations.Test;

public class HttpSessionStoreInMemoryTest {

  @Test
  public void createSession01() {
    final HttpSessionStoreInMemory store;
    store = create(options -> {
      options.randomGenerator(generator(1L, 2L, 3L, 4L));
    });

    final HttpSession session;
    session = store.createSession();

    assertNotNull(session);

    final Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));
    });

    final HttpSession maybe;
    maybe = store.getSession(http);

    assertSame(maybe, session);
  }

  @Test
  public void loadSession01() {
    final HttpSessionStoreInMemory store;
    store = create(options -> {
      options.randomGenerator(generator(1L, 2L, 3L, 4L));
    });

    final HttpSession session;
    session = store.createSession();

    assertNotNull(session);

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));
    });

    assertEquals(http.sessionLoaded(), false);

    store.loadSession(http);

    assertEquals(http.sessionLoaded(), true);
  }

  private HttpSessionStoreInMemory create(Consumer<HttpSessionStoreBuilder> options) {
    final HttpSessionStoreBuilder builder;
    builder = new HttpSessionStoreBuilder();

    options.accept(builder);

    return (HttpSessionStoreInMemory) builder.build();
  }

  private String cookie(String name, long l0, long l1, long l2, long l3) {
    return Y.cookie(name, l0, l1, l2, l3);
  }

  private RandomGenerator generator(long... values) {
    return Y.randomGeneratorOfLongs(values);
  }

}