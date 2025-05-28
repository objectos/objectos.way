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
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class HttpExchangeTest {

  static final Media.Bytes OK = Media.Bytes.textPlain("OK\n");

  static final String OK_RESP = """
      HTTP/1.1 200 OK\r
      Date: Wed, 28 Jun 2023 12:08:43 GMT\r
      Content-Type: text/plain; charset=utf-8\r
      Content-Length: 3\r
      \r
      OK
      """;

  static final class Tester {

    private HttpExchangeBodyFiles bodyFiles;

    private int bufferSizeInitial = 128;

    private int bufferSizeMax = 256;

    private Clock clock = Y.clockFixed();

    private Note.Sink noteSink = Y.noteSink();

    private final long requestBodySizeMax = 1024;

    private final List<Xch> xchs = Util.createList();

    public final void bodyFiles(HttpExchangeBodyFiles value) {
      bodyFiles = Objects.requireNonNull(value, "value == null");
    }

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

    private HttpExchangeBodyFiles bodyFiles() {
      return bodyFiles != null ? bodyFiles : HttpExchangeBodyFiles.standard();
    }

    private void execute() {
      final Y.TestingOutputStream outputStream;
      outputStream = Y.outputStream();

      final Socket socket;
      socket = Y.socket(config -> {
        final Object[] data;
        data = xchs.stream().flatMap(Xch::data).toArray();

        config.inputStream(
            Y.inputStream(data)
        );

        config.outputStream(outputStream);
      });

      String previousResponse;
      previousResponse = null;

      try (HttpExchange http = new HttpExchange(socket, bodyFiles(), bufferSizeInitial, bufferSizeMax, clock, noteSink, requestBodySizeMax)) {
        for (Xch xch : xchs) {
          assertEquals(http.shouldHandle(), xch.shouldHandle);

          if (previousResponse != null) {
            assertEquals(Y.toString(socket), previousResponse);
          }

          previousResponse = xch.response;

          outputStream.throwOnWrite(xch.responseException);

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

    @SuppressWarnings("unused")
    private IOException responseException;

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

    public final void resp(IOException writeException, int newLength) {
      responseException = Y.trimStackTrace(writeException, newLength);
    }

    private Stream<Object> data() {
      return request.stream();
    }

  }

  final Object[] arr(Object... arr) {
    // not safe, oh well...
    return arr;
  }

  final void exec(Consumer<Tester> config) {
    final Tester tester;
    tester = new Tester();

    config.accept(tester);

    tester.execute();
  }

  final void queryAssert(HttpExchange http, Map<String, Object> expected) {
    assertEquals(http.queryParamNames(), expected.keySet());

    for (var entry : expected.entrySet()) {
      final String key;
      key = entry.getKey();

      final Object value;
      value = entry.getValue();

      if (value instanceof String s) {
        assertEquals(http.queryParam(key), s, key);
        assertEquals(http.queryParamAll(key), List.of(s));
      }

      else {
        List<?> list = (List<?>) value;
        assertEquals(http.queryParam(key), list.get(0), key);
        assertEquals(http.queryParamAll(key), value, key);
      }
    }
  }

  final boolean[] queryValidBytes() {
    final boolean[] valid;
    valid = new boolean[256];

    final String validString;
    validString = Http.unreserved() + Http.subDelims() + ":@/?";

    for (int idx = 0, len = validString.length(); idx < len; idx++) {
      final char c;
      c = validString.charAt(idx);

      valid[c] = true;
    }

    return valid;
  }

}