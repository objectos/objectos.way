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
package objectos.testing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.function.Consumer;
import objectos.http.ServerExchange;
import objectos.http.SessionStore;
import objectos.http.WayServerLoop;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;

public final class WayTestingServerExchange implements TestingServerExchange {

  private int bufferSizeInitial = 1024;

  private int bufferSizeMax = 4096;

  private Clock clock = Clock.systemUTC();

  private NoteSink noteSink = NoOpNoteSink.of();

  private SessionStore sessionStore;

  public WayTestingServerExchange() {}

  public final void bufferSize(int initial, int max) {
    Check.argument(initial >= 128, "initial size must be >= 128");
    Check.argument(max >= 128, "max size must be >= 128");
    Check.argument(max >= initial, "max size must be >= initial size");

    this.bufferSizeInitial = initial;
    this.bufferSizeMax = max;
  }

  public final void clock(Clock clock) {
    this.clock = Check.notNull(clock, "clock == null");
  }

  public final void noteSink(NoteSink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");
  }

  /**
   * Use the specified {@link SessionStore} for session handling.
   *
   * <p>
   * If the specified value is {@code null} then session handling is disabled.
   *
   * @param sessionStore
   *        the session store to use or {@code null} to disable session
   *        handling
   */
  public final void sessionStore(SessionStore sessionStore) {
    this.sessionStore = sessionStore;
  }

  @Override
  public final String handle(String request, Consumer<ServerExchange> handler) {
    byte[] bytes;
    bytes = request.getBytes(StandardCharsets.UTF_8);

    try (
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        TestingSocket socket = new TestingSocket(inputStream);
        WayServerLoop loop = new WayServerLoop(socket)
    ) {
      loop.bufferSize(bufferSizeInitial, bufferSizeMax);

      loop.clock(clock);

      loop.noteSink(noteSink);

      loop.sessionStore(sessionStore);

      loop.parse();

      if (loop.badRequest()) {
        throw new UnsupportedOperationException("Bad request");
      }

      try {
        handler.accept(loop);
      } catch (Throwable t) {
        loop.internalServerError(t);
      }

      loop.commit();

      return socket.toString();
    } catch (IOException e) {
      throw new UncheckedIOException("Unexpected IOException: testing server exchange executed in-memory", e);
    }
  }

}