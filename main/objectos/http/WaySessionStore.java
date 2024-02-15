/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import objectos.lang.object.Check;

/**
 * The Objectos Way {@link SessionStore} implementation.
 */
public final class WaySessionStore implements SessionStore {

  private static final int ID_LENGTH_IN_BYTES = 16;

  private String cookieName = "OBJECTOSWAY";

  private final HexFormat hexFormat = HexFormat.of();

  private Random random = new SecureRandom();

  private final ConcurrentMap<String, WaySession> sessions = new ConcurrentHashMap<>();

  /**
   * Sole constructor.
   */
  public WaySessionStore() {}

  /**
   * Use the specified {@code name} when setting the client session cookie.
   *
   * @param name
   *        the cookie name to use
   *
   * @return this instance
   */
  public final WaySessionStore cookieName(String name) {
    this.cookieName = Check.notNull(name, "name == null");

    return this;
  }

  /**
   * Use the specified {@link Random} instance for generating session IDs.
   *
   * @param random
   *        the {@link Random} instance to use
   *
   * @return this instance
   */
  public final WaySessionStore random(Random random) {
    this.random = Check.notNull(random, "random == null");

    return this;
  }

  final WaySession put(String id, WaySession session) {
    Check.notNull(id, "id == null");
    Check.notNull(session, "session == null");

    return sessions.put(id, session);
  }

  @Override
  public final Session nextSession() {
    WaySession session, maybeExisting;

    do {
      String id;
      id = nextId();

      session = new WaySession(id);

      maybeExisting = sessions.putIfAbsent(id, session);
    } while (maybeExisting != null);

    return session;
  }

  @Override
  public final Session get(Cookies cookies) {
    String maybe;
    maybe = cookies.get(cookieName); // implicit cookies null check

    return get(maybe);
  }

  @Override
  public final Session get(String id) {
    WaySession session;
    session = sessions.get(id);

    if (session != null && !session.valid) {
      session = null;
    }

    return session;
  }

  private String nextId() {
    byte[] bytes;
    bytes = new byte[ID_LENGTH_IN_BYTES];

    random.nextBytes(bytes);

    return hexFormat.formatHex(bytes);
  }

}