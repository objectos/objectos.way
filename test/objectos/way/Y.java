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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

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
  // # BEGIN: Clock
  // ##################################################################

  private static final class ClockHolder {

    static Clock FIXED = fixed();

    private ClockHolder() {}

    private static Clock fixed() {
      LocalDateTime dateTime;
      dateTime = LocalDateTime.of(2023, 6, 28, 12, 8, 43);

      ZoneId zone;
      zone = ZoneId.of("GMT");

      ZonedDateTime zoned;
      zoned = dateTime.atZone(zone);

      Instant fixedInstant;
      fixedInstant = zoned.toInstant();

      return Clock.fixed(fixedInstant, zone);
    }

  }

  public static Clock clockFixed() {
    return ClockHolder.FIXED;
  }

  // ##################################################################
  // # END: Clock
  // ##################################################################

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

    public final void add(InputStream value) {
      data.add(value);
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

  private static final class InputStreamBytes extends InputStream {
    private final byte[] bytes;
    private int bytesIndex;

    InputStreamBytes(byte[] bytes) { this.bytes = bytes; }

    @Override
    public final int read() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public final int read(byte[] b, int off, int len) {
      final int remaining;
      remaining = bytes.length - bytesIndex;

      if (remaining <= 0) {
        return -1;
      }

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
      int result;
      result = -1;

      outer: while (dataIndex < data.size()) {
        final Object next;
        next = data.get(dataIndex); // do not advance yet

        switch (next) {
          case InputStream stream -> {
            result = stream.read(b, off, len);

            if (result < 0) {
              dataIndex++;

              continue;
            } else {
              break outer;
            }
          }

          case IOException ioe -> { dataIndex++; throw ioe; }

          default -> throw new UnsupportedOperationException("Unsupported type: " + next.getClass());
        }
      }

      return result;
    }

  }

  public static InputStream inputStream(Object... data) {
    return inputStream(config -> {
      for (Object o : data) {
        switch (o) {
          case byte[] bytes -> config.add(bytes);

          case InputStream is -> config.add(is);

          case IOException e -> config.add(e);

          case String s -> config.add(s);

          default -> throw new IllegalArgumentException(
              "Only byte[], InputStream, String and IOException are currently supported"
          );
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
  // # BEGIN: JavaProject
  // ##################################################################

  public sealed interface JavaProject extends AutoCloseable {

    Path classOutput();

    @Override
    void close();

    boolean compile();

    void writeJavaFile(Path path, String source);

  }

  private static final class JavaProjectImpl implements JavaProject {

    private final Path root;
    private final Path src;
    private final Path cls;

    private final JavaCompiler javaCompiler;
    private final StandardJavaFileManager fileManager;

    private final List<Path> sourceFiles = Util.createList();

    public JavaProjectImpl(
        Path root,
        Path src,
        Path cls,
        JavaCompiler javaCompiler,
        StandardJavaFileManager fileManager) {
      this.root = root;
      this.src = src;
      this.cls = cls;
      this.javaCompiler = javaCompiler;
      this.fileManager = fileManager;
    }

    @Override
    public final Path classOutput() {
      return cls;
    }

    @Override
    public final void close() {
      try (fileManager) {
        Io.deleteRecursively(root);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    @Override
    public final boolean compile() {
      final Iterable<? extends JavaFileObject> compilationUnits;
      compilationUnits = fileManager.getJavaFileObjectsFromPaths(sourceFiles);

      sourceFiles.clear();

      final CompilationTask task;
      task = javaCompiler.getTask(null, fileManager, null, null, null, compilationUnits);

      final Boolean result;
      result = task.call();

      return result.booleanValue();
    }

    @Override
    public final void writeJavaFile(Path path, String source) {
      try {
        Path javaFile;
        javaFile = src.resolve(path);

        Path parent;
        parent = javaFile.getParent();

        Files.createDirectories(parent);

        try (Writer w = Files.newBufferedWriter(javaFile)) {
          w.write(source);
        }

        sourceFiles.add(javaFile);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

  }

  public static JavaProject javaProject() {
    try {
      final Path root;
      root = nextTempDir();

      final JavaCompiler javaCompiler;
      javaCompiler = ToolProvider.getSystemJavaCompiler();

      final StandardJavaFileManager fileManager;
      fileManager = javaCompiler.getStandardFileManager(null, null, null);

      fileManager.setLocationForModule(StandardLocation.MODULE_PATH, "objectos.way", List.of(Path.of("work", "main")));

      final Path src;
      src = root.resolve("src");

      Files.createDirectories(src);

      fileManager.setLocationFromPaths(StandardLocation.SOURCE_PATH, List.of(src));

      final Path cls;
      cls = root.resolve("cls");

      Files.createDirectories(cls);

      fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(cls));

      return new JavaProjectImpl(
          root, src, cls, javaCompiler, fileManager
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  // ##################################################################
  // # End: JavaProject
  // ##################################################################

  // ##################################################################
  // # BEGIN: Media.Text
  // ##################################################################

  private static final class ThisMediaText implements Media.Text {

    private static final int LINE_LENGTH = 50;

    private static final String FULL_LINE = "..........".repeat(4).concat(".........\n");

    private final int length;

    ThisMediaText(int length) {
      this.length = length;
    }

    @Override
    public final String contentType() {
      return "text/plain; charset=utf-8";
    }

    @Override
    public final Charset charset() {
      return StandardCharsets.UTF_8;
    }

    @Override
    public final void writeTo(Appendable dest) throws IOException {
      if (length == 0) {
        return;
      }

      int count = 0;

      int fullLines = length / LINE_LENGTH;

      for (int fullLine = 0; fullLine < fullLines; fullLine++) {
        dest.append(FULL_LINE);

        count += LINE_LENGTH;
      }

      int digit = 1;

      for (; count < length; count++) {
        int print;
        print = digit % 10;

        dest.append(Integer.toString(print));

        digit++;
      }
    }

    @Override
    public final String toString() {
      try {
        StringBuilder sb;
        sb = new StringBuilder();

        writeTo(sb);

        return sb.toString();
      } catch (IOException e) {
        throw new AssertionError("StringBuilder should not have thrown", e);
      }
    }

  }

  public static Media.Text mediaTextOfLength(int length) {
    return new ThisMediaText(length);
  }

  // ##################################################################
  // # END: Media.Text
  // ##################################################################

  // ##################################################################
  // # BEGIN: Next
  // ##################################################################

  private static final class NextPath implements Closeable {

    private final Path root;

    private NextPath(Path root) {
      this.root = root;
    }

    private static NextPath create() {
      try {
        final Path root;
        root = Files.createTempDirectory("objectos-way-testing-");

        final NextPath nextPath;
        nextPath = new NextPath(root);

        shutdownHook(nextPath);

        return nextPath;
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    @Override
    public final void close() throws IOException {
      Io.deleteRecursively(root);
    }

    final Path nextTempDir() {
      try {
        return Files.createTempDirectory(root, "next-temp-dir");
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    final Path nextTempFile() {
      try {
        return Files.createTempFile(root, "next-temp-file", ".tmp");
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

  }

  private static final class NextPathHolder {

    static final NextPath INSTANCE = NextPath.create();

  }

  public static Path nextTempDir() {
    return NextPathHolder.INSTANCE.nextTempDir();
  }

  public static Path nextTempFile(String contents, Charset charset) {
    try {
      final Path file;
      file = NextPathHolder.INSTANCE.nextTempFile();

      Files.writeString(file, contents, charset);

      return file;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  // ##################################################################
  // # END: Next
  // ##################################################################

  // ##################################################################
  // # BEGIN: Note.Sink
  // ##################################################################

  private static final App.NoteSink INSTANCE = App.NoteSink.OfConsole.create(config -> {});

  public static Note.Sink noteSink() {
    return INSTANCE;
  }

  // ##################################################################
  // # END: Note.Sink
  // ##################################################################

  // ##################################################################
  // # BEGIN: OutputStream
  // ##################################################################

  public static final class TestingOutputStream extends OutputStream {

    private byte[] bytes = new byte[32];

    private int bytesIndex;

    private IOException onClose;

    private IOException onWrite;

    private TestingOutputStream() {}

    @Override
    public final void close() throws IOException {
      if (onClose != null) {
        throw onClose;
      }
    }

    public final void reset() {
      bytesIndex = 0;
    }

    public final void throwOnClose(IOException value) {
      onClose = value;
    }

    public final void throwOnWrite(IOException value) {
      onWrite = value;
    }

    public final String toString(Charset charset) {
      return new String(bytes, 0, bytesIndex, charset);
    }

    @Override
    public final void write(int b) throws IOException {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final void write(byte[] b, int off, int len) throws IOException {
      Objects.checkFromIndexSize(off, len, b.length);

      if (onWrite != null) {
        throw onWrite;
      }

      bytes = Util.growIfNecessary(bytes, bytesIndex + len);

      System.arraycopy(b, off, bytes, bytesIndex, len);

      bytesIndex += len;
    }

  }

  public static TestingOutputStream outputStream() {
    return new TestingOutputStream();
  }

  // ##################################################################
  // # END: OutputStream
  // ##################################################################

  // ##################################################################
  // # BEGIN: ShutdownHook
  // ##################################################################

  private static final class ShutdownHookHolder {

    static final App.ShutdownHook INSTANCE = App.ShutdownHook.create(config -> config.noteSink(TestingNoteSink.INSTANCE));

  }

  public static void shutdownHook(AutoCloseable closeable) {
    ShutdownHookHolder.INSTANCE.register(closeable);
  }

  // ##################################################################
  // # END: ShutdownHook
  // ##################################################################

  // ##################################################################
  // # BEGIN: SlowStream
  // ##################################################################

  private static final class SlowStream extends InputStream {

    private final int bytesPerRead;

    private final byte[] bytes;

    private int bytesIndex;

    private SlowStream(int bytesPerRead, byte[] bytes) {
      this.bytesPerRead = bytesPerRead;

      this.bytes = bytes;
    }

    @Override
    public final int read() throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public final int read(byte[] b, int off, int len) throws IOException {
      final int remaining;
      remaining = bytes.length - bytesIndex;

      if (remaining <= 0) {
        return -1;
      }

      final int length;
      length = Math.min(Math.min(bytesPerRead, remaining), len);

      System.arraycopy(bytes, bytesIndex, b, off, length);

      bytesIndex += length;

      return length;
    }

  }

  public static InputStream slowStream(int bytesPerRead, String ascii) {
    final byte[] bytes;
    bytes = ascii.getBytes(StandardCharsets.US_ASCII);

    return slowStream(bytesPerRead, bytes);
  }

  public static InputStream slowStream(int bytesPerRead, byte[] bytes) {
    if (bytesPerRead <= 0) {
      throw new IllegalArgumentException("bytesPerRead must be > 0");
    }
    Objects.requireNonNull(bytes, "bytes == null");

    return new SlowStream(bytesPerRead, bytes);
  }

  // ##################################################################
  // # END: SlowStream
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

    public final void outputStream(OutputStream value) {
      outputStream = Objects.requireNonNull(value, "value == null");
    }

    final Socket build() {
      if (inputStream == null) {
        inputStream = Y.inputStream();
      }

      if (outputStream == null) {
        outputStream = new TestingOutputStream();
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

  public static String toString(Socket socket) {
    try {
      final OutputStream outputStream;
      outputStream = socket.getOutputStream();

      if (outputStream instanceof TestingOutputStream os) {
        final String result;
        result = os.toString(StandardCharsets.UTF_8);

        os.reset();

        return result;
      } else {
        throw new IllegalArgumentException("OutputStream is not an instanceof TestingOutputStream");
      }
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  // ##################################################################
  // # END: Socket
  // ##################################################################

  // ##################################################################
  // # BEGIN: Throwable
  // ##################################################################

  public static <T extends Throwable> T trimStackTrace(T t, int newLength) {
    final StackTraceElement[] stackTrace;
    stackTrace = t.getStackTrace();

    final StackTraceElement[] copy;
    copy = Arrays.copyOf(stackTrace, newLength);

    t.setStackTrace(copy);

    return t;
  }

  // ##################################################################
  // # END: Throwable
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
        final int currentIndex;
        currentIndex = index++;

        if (index == values.length) {
          index = 0;
        }

        return values[currentIndex];
      }
    };
  }

}
