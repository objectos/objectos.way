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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.internal.Util;
import objectos.way.Note;
import objectos.way.Y;

@SuppressWarnings("exports")
public final class HttpY {

  private HttpY() {}

  public static String cookie(String name, long l0, long l1, long l2, long l3) {
    final HttpToken token;
    token = HttpToken.of32(l0, l1, l2, l3);

    return name + "=" + token.toString();
  }

  // ##################################################################
  // # BEGIN: URL Decode
  // ##################################################################

  public static boolean[] queryValidBytes() {
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

  public static Object[][] queryValidProvider() {
    final boolean[] validBytes;
    validBytes = queryValidBytes();

    final List<Object[]> l;
    l = new ArrayList<>();

    l.add(arr("", Map.of(), "empty"));
    l.add(arr("key=value", Map.of("key", "value"), "one"));
    l.add(arr("=value", Map.of("", "value"), "one + empty key"));
    l.add(arr("key=", Map.of("key", ""), "one + empty value"));
    l.add(arr("key", Map.of("key", ""), "one + empty value + no equals"));
    l.add(arr("key1=value1&key2=value2", Map.of("key1", "value1", "key2", "value2"), "two"));
    l.add(arr("=value1&key2=value2", Map.of("", "value1", "key2", "value2"), "two + empty key1"));
    l.add(arr("key1=value1&=value2", Map.of("key1", "value1", "", "value2"), "two + empty key2"));
    l.add(arr("key1=&key2=value2", Map.of("key1", "", "key2", "value2"), "two + empty value1"));
    l.add(arr("key1=value1&key2=", Map.of("key1", "value1", "key2", ""), "two + empty value2"));
    l.add(arr("key1&key2=value2", Map.of("key1", "", "key2", "value2"), "two + empty value1 + no equals"));
    l.add(arr("key1=value1&key2", Map.of("key1", "value1", "key2", ""), "two + empty value2 + no equals"));
    l.add(arr("key=value1&key=value2", Map.of("key", List.of("value1", "value2")), "two + duplicate keys"));

    for (int value = 0; value < validBytes.length; value++) {
      switch (value) {
        case ' ' -> {/* will cause parsing to move to VERSION */}

        case '\n', '\r' -> {/* will trigger 505 not 400 */}

        case '&', '=' -> {/* valid in query string, but has special meaning*/}

        case '+' -> {
          l.add(arr("+=value", Map.of(" ", "value"), "key contains the '+' character"));
          l.add(arr("key=+", Map.of("key", " "), "value contains the '+' character"));
        }

        default -> {
          if (validBytes[value]) {
            l.add(queryValidKey(value));
            l.add(queryValidValue(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private static Object[] queryValidKey(int value) {
    final String key;
    key = Character.toString(value);

    return arr(key + "=value", Map.of(key, "value"), "key contains the " + Integer.toHexString(value) + " valid byte");
  }

  private static Object[] queryValidValue(int value) {
    final String val;
    val = Character.toString(value);

    return arr("key=" + val, Map.of("key", val), "value contains the " + Integer.toHexString(value) + " valid byte");
  }

  static Object[] arr(Object... arr) {
    // not safe, oh well...
    return arr;
  }

  // ##################################################################
  // # END: URL Decode
  // ##################################################################

  // ##################################################################
  // # BEGIN: HttpExchange
  // ##################################################################

  public static final class HttpExchangeTester {

    private HttpExchangeBodyFiles bodyFiles;

    private int bufferSizeInitial = 128;

    private int bufferSizeMax = 256;

    private Clock clock = Y.clockFixed();

    private Note.Sink noteSink = Y.noteSink();

    private long requestBodySizeMax = 1024;

    private final List<Xch> xchs = Util.createList();

    private HttpResponseListener responseListener = Http.NoopResponseListener.INSTANCE;

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

    public final void requestBodySize(long max) {
      requestBodySizeMax = max;
    }

    public final void respListener(HttpResponseListener value) {
      responseListener = value;
    }

    public final void xch(Consumer<Xch> config) {
      final Xch xch;
      xch = new Xch();

      config.accept(xch);

      xchs.add(xch);
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

      try (HttpExchangeImpl http = newHttpExchange(socket)) {
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

    private HttpExchangeImpl newHttpExchange(Socket socket) throws IOException {
      return new HttpExchangeImpl(
          bodyFiles != null ? bodyFiles : HttpExchangeBodyFiles.standard(),
          bufferSizeInitial,
          bufferSizeMax,
          clock,
          null,
          0,
          noteSink,
          requestBodySizeMax,
          responseListener,
          socket
      );
    }

  }

  public static final class Xch {

    private final List<Object> request = Util.createList();

    private boolean shouldHandle = true;

    private Consumer<HttpExchange> handler = _ -> {};

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

    public final void handle(Consumer<HttpExchange> value) {
      handler = Objects.requireNonNull(value, "value == null");
    }

    public final void handler(HttpHandler value) {
      final HttpHandler nonNull;
      nonNull = Objects.requireNonNull(value, "value == null");

      handler = exchange -> nonNull.handle(exchange);
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

  public static HttpExchangeImpl http(Socket socket, int bufferSizeInitial, int bufferSizeMax) {
    try {
      return new HttpExchangeImpl(
          HttpExchangeBodyFiles.standard(),
          bufferSizeInitial,
          bufferSizeMax,
          Y.clockFixed(),
          null,
          0,
          Y.noteSink(),
          0,
          Http.NoopResponseListener.INSTANCE,
          socket
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static HttpExchangeImpl http(Socket socket, int bufferSizeInitial, int bufferSizeMax, Clock clock, Note.Sink noteSink, long requestBodySizeMax) {
    try {
      return new HttpExchangeImpl(
          HttpExchangeBodyFiles.standard(),
          bufferSizeInitial,
          bufferSizeMax,
          clock,
          null,
          0,
          noteSink,
          requestBodySizeMax,
          Http.NoopResponseListener.INSTANCE,
          socket
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void httpExchange(Consumer<HttpExchangeTester> config) {
    final HttpExchangeTester tester;
    tester = new HttpExchangeTester();

    config.accept(tester);

    tester.execute();
  }

  // ##################################################################
  // # END: HttpExchange
  // ##################################################################

}
