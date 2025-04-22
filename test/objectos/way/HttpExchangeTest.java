/*
 * Copyright (C) 2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless httpuired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class HttpExchangeTest {

  static final class Tester {

    private int bufferSizeInitial = 128;

    private int bufferSizeMax = 256;

    private Clock clock = Y.clockFixed();

    private Note.Sink noteSink = Y.noteSink();

    private final List<Xch> xchs = Util.createList();

    public final void bufferSize(int initial, int max) {
      bufferSizeInitial = initial;

      bufferSizeMax = max;
    }

    public final void clock(Clock value) {
      clock = Objects.requireNonNull(value, "value == null");
    }

    public final void noteSink(Note.Sink value) {
      noteSink = Objects.requireNonNull(value, "value == null");
    }

    public final void xch(Consumer<Xch> config) {
      final Xch xch;
      xch = new Xch();

      config.accept(xch);

      xchs.add(xch);
    }

    private void execute() {
      final Object[] data;
      data = xchs.stream().flatMap(Xch::data).toArray();

      final Socket socket;
      socket = Y.socket(data);

      String previousResponse;
      previousResponse = null;

      try (HttpExchange http = new HttpExchange(socket, bufferSizeInitial, bufferSizeMax, clock, noteSink)) {
        for (Xch xch : xchs) {
          assertEquals(http.shouldHandle(), xch.shouldHandle);

          if (previousResponse != null) {
            assertEquals(Y.toString(socket), previousResponse);
          }

          previousResponse = xch.response;

          xch.handler.accept(http);
        }

        assertEquals(http.shouldHandle(), false);

        if (previousResponse != null) {
          assertEquals(Y.toString(socket), previousResponse);
        }
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

  }

  static final class Xch {

    private final List<Object> request = Util.createList();

    private boolean shouldHandle = true;

    private Consumer<HttpExchange> handler = http -> {};

    private String response = "";

    public final void req(Object value) {
      request.add(value);
    }

    public final void req(Throwable error, int newLength) {
      request.add(
          Y.trimStackTrace(error, newLength)
      );
    }

    public final void shouldHandle(boolean value) {
      shouldHandle = value;
    }

    public final void handler(Consumer<HttpExchange> value) {
      handler = Objects.requireNonNull(value, "value == null");
    }

    public final void resp(String value) {
      response = Objects.requireNonNull(value, "value == null");
    }

    private Stream<Object> data() {
      return request.stream();
    }

  }

  final void exec(Consumer<Tester> config) {
    final Tester tester;
    tester = new Tester();

    config.accept(tester);

    tester.execute();
  }

}