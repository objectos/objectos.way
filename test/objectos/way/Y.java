/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

final class Y {

  static final class HttpClient {

    public static final java.net.http.HttpClient INSTANCE;

    static {
      System.setProperty("jdk.httpclient.allowRestrictedHeaders", "connection,host");

      java.net.http.HttpClient httpClient;
      httpClient = java.net.http.HttpClient.newBuilder()
          .version(Version.HTTP_1_1)
          .build();

      TestingShutdownHook.register(httpClient);

      INSTANCE = httpClient;
    }

  }

  private Y() {}

  public static String cookie(String name, long l0, long l1, long l2, long l3) {
    final WebToken token;
    token = WebToken.of32(l0, l1, l2, l3);

    return name + "=" + token.toString();
  }

  public static HttpResponse<String> httpClient(String path, Consumer<HttpRequest.Builder> config) {
    try {
      // force early init
      java.net.http.HttpClient client;
      client = HttpClient.INSTANCE;

      HttpRequest.Builder builder;
      builder = HttpRequest.newBuilder();

      int port;
      port = TestingHttpServer.port();

      URI uri;
      uri = URI.create("http://localhost:" + port + path);

      builder.uri(uri);

      builder.timeout(Duration.ofMinutes(1));

      config.accept(builder);

      HttpRequest request;
      request = builder.build();

      return client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  // ##################################################################
  // # BEGIN: InputStream
  // ##################################################################

  public static final class InputStreamBuilder {

    private final UtilList<Object> data = new UtilList<>();

    private IOException onClose;

    private InputStreamBuilder() {}

    public final void add(byte[] value) {
      final byte[] copy;
      copy = value.clone();

      add0(copy);
    }

    public final void add(IOException value) {
      data.add(value);
    }

    public final void add(String s) {
      add(s, StandardCharsets.US_ASCII);
    }

    public final void add(String s, Charset charset) {
      final byte[] bytes;
      bytes = s.getBytes(charset);

      add0(bytes);
    }

    public final void onClose(IOException value) {
      onClose = value;
    }

    private void add0(final byte[] bytes) {
      final InputStreamBytes wrapper;
      wrapper = new InputStreamBytes(bytes);

      data.add(wrapper);
    }

  }

  private static final class InputStreamBytes {
    private final byte[] bytes;
    private int bytesIndex;

    InputStreamBytes(byte[] bytes) { this.bytes = bytes; }

    final boolean exhausted() {
      return bytesIndex >= bytes.length;
    }

    final int read(byte[] b, int off, int len) {
      final int remaining;
      remaining = bytes.length - bytesIndex;

      final int length;
      length = Math.min(remaining, len);

      System.arraycopy(bytes, bytesIndex, b, off, length);

      bytesIndex += length;

      return length;
    }
  }

  private static final class InputStreamImpl extends InputStream {

    private final List<Object> data;

    private int dataIndex;

    private final IOException onClose;

    private InputStreamImpl(InputStreamBuilder builder) {
      data = builder.data.toUnmodifiableList();

      onClose = builder.onClose;
    }

    @Override
    public final void close() throws IOException {
      if (onClose != null) {
        throw onClose;
      }
    }

    @Override
    public final int read() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public final int read(byte[] b, int off, int len) throws IOException {
      if (dataIndex < data.size()) {
        final Object next;
        next = data.get(dataIndex); // do not advance yet

        return switch (next) {
          case InputStreamBytes bytes -> {
            final int result;
            result = bytes.read(b, off, len);

            if (bytes.exhausted()) {
              dataIndex++;
            }

            yield result;
          }

          case IOException ioe -> { dataIndex++; throw ioe; }

          default -> throw new UnsupportedOperationException("Unsupported type: " + next.getClass());
        };
      }

      return -1;
    }

  }

  public static InputStream inputStream(Object... data) {
    return inputStream(config -> {
      for (Object o : data) {
        switch (o) {
          case byte[] bytes -> config.add(bytes);

          case String s -> config.add(s);

          case IOException e -> config.add(e);

          default -> throw new IllegalArgumentException("Only String and IOException are currently supported");
        }
      }
    });
  }

  public static InputStream inputStream(Consumer<InputStreamBuilder> config) {
    final InputStreamBuilder builder;
    builder = new InputStreamBuilder();

    config.accept(builder);

    return new InputStreamImpl(builder);
  }

  // ##################################################################
  // # END: InputStream
  // ##################################################################

  // ##################################################################
  // # BEGIN: Socket
  // ##################################################################

  public static final class SocketBuilder {
    private InputStream inputStream;

    private OutputStream outputStream;

    private SocketBuilder() {}

    public final void inputStream(InputStream value) {
      inputStream = Objects.requireNonNull(value, "value == null");
    }

    final Socket build() {
      if (inputStream == null) {
        inputStream = Y.inputStream();
      }

      if (outputStream == null) {
        outputStream = new ByteArrayOutputStream();
      }

      return new SocketImpl(this);
    }

  }

  private static final class SocketImpl extends Socket {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    SocketImpl(SocketBuilder builder) {
      inputStream = builder.inputStream;

      outputStream = builder.outputStream;
    }

    @Override
    public final InputStream getInputStream() throws IOException {
      return inputStream;
    }

    @Override
    public final OutputStream getOutputStream() throws IOException {
      return outputStream;
    }
  }

  public static Socket socket(Consumer<SocketBuilder> config) {
    final SocketBuilder builder;
    builder = new SocketBuilder();

    config.accept(builder);

    return builder.build();
  }

  public static Socket socket(Object... data) {
    return socket(socket -> {
      socket.inputStream(Y.inputStream(data));
    });
  }

  // ##################################################################
  // # END: Socket
  // ##################################################################

  public static void test(HttpResponse<String> response, String expected) {
    final StringBuilder sb;
    sb = new StringBuilder("HTTP/");

    switch (response.version()) {
      case HTTP_1_1 -> sb.append("1.1");

      case HTTP_2 -> sb.append("2");
    }

    sb.append(' ');

    sb.append(response.statusCode());

    sb.append('\n');

    final HttpHeaders headers;
    headers = response.headers();

    final Map<String, List<String>> map;
    map = headers.map();

    for (var entry : map.entrySet()) {
      sb.append(entry.getKey());

      sb.append(':');
      sb.append(' ');

      List<String> list;
      list = entry.getValue();

      sb.append(list.stream().collect(Collectors.joining("; ")));

      sb.append('\n');
    }

    sb.append('\n');

    sb.append(response.body());

    final String result;
    result = sb.toString();

    assertEquals(result, expected);
  }

  public static RandomGenerator randomGeneratorOfLongs(long... longs) {
    return new RandomGenerator() {
      private final long[] values = longs.clone();

      private int index;

      @Override
      public final long nextLong() {
        return values[index++];
      }
    };
  }

}
