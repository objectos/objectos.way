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
package objectos.way;

import static org.testng.Assert.assertEquals;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.ShardingKeyBuilder;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import objectos.html.Id;
import objectos.internal.Util;
import objectos.way.dev.DevStart;
import objectos.y.PathY;
import org.testng.ISuite;
import org.testng.ISuiteListener;

@SuppressWarnings("exports")
public final class Y implements ISuiteListener {

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

  /// Sole constructor. Required by TestNG.
  public Y() {}

  @Override
  public final void onStart(ISuite suite) {
    final App.Bootstrap bootstrap;
    bootstrap = new DevStart();

    bootstrap.start(new String[0]);
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

  private static final class IncrementingClock extends Clock {

    private final ZonedDateTime startTime;

    private int minutes;

    public IncrementingClock(int year, int month, int day) {
      LocalDateTime dateTime;
      dateTime = LocalDateTime.of(year, month, day, 10, 0);

      this.startTime = dateTime.atZone(ZoneId.systemDefault());
    }

    @Override
    public final Instant instant() {
      ZonedDateTime instant;
      instant = startTime.plusMinutes(minutes++);

      return Instant.from(instant);
    }

    @Override
    public final ZoneId getZone() {
      return startTime.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
      throw new UnsupportedOperationException();
    }

  }

  public static Clock clockIncMinutes(int year, int month, int day) {
    return new IncrementingClock(year, month, day);
  }

  public static Clock clockFixed() {
    return ClockHolder.FIXED;
  }

  // ##################################################################
  // # END: Clock
  // ##################################################################

  // ##################################################################
  // # BEGIN: DataSource
  // ##################################################################

  private static final class DataSourceAutoCloseable implements DataSource, AutoCloseable {

    private final DataSource ds;

    private final AutoCloseable closeOperation;

    public DataSourceAutoCloseable(DataSource ds, AutoCloseable closeOperation) {
      this.ds = ds;

      this.closeOperation = closeOperation;
    }

    @Override
    public final void close() throws Exception {
      closeOperation.close();
    }

    @Override
    public final <T> T unwrap(Class<T> iface) throws SQLException { return ds.unwrap(iface); }

    @Override
    public final boolean isWrapperFor(Class<?> iface) throws SQLException { return ds.isWrapperFor(iface); }

    @Override
    public final Connection getConnection() throws SQLException { return ds.getConnection(); }

    @Override
    public final Connection getConnection(String username, String password) throws SQLException { return ds.getConnection(username, password); }

    @Override
    public final Logger getParentLogger() throws SQLFeatureNotSupportedException { return ds.getParentLogger(); }

    @Override
    public final PrintWriter getLogWriter() throws SQLException { return ds.getLogWriter(); }

    @Override
    public final void setLogWriter(PrintWriter out) throws SQLException { ds.setLogWriter(out); }

    @Override
    public final void setLoginTimeout(int seconds) throws SQLException { ds.setLoginTimeout(seconds); }

    @Override
    public final int getLoginTimeout() throws SQLException { return ds.getLoginTimeout(); }

    @Override
    public final ConnectionBuilder createConnectionBuilder() throws SQLException { return ds.createConnectionBuilder(); }

    @Override
    public final ShardingKeyBuilder createShardingKeyBuilder() throws SQLException { return ds.createShardingKeyBuilder(); }

  }

  public static DataSource dataSourceAutoCloseable(DataSource ds, AutoCloseable closeOperation) {
    return new DataSourceAutoCloseable(ds, closeOperation);
  }

  // ##################################################################
  // # END: DataSource
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
      root = PathY.nextDir();

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
  // # BEGIN: Note.Sink
  // ##################################################################

  private static final App.NoteSink INSTANCE = App.NoteSink.ofAppendable(System.out, opts -> {
    opts.filter(note -> note.hasAny(Note.ERROR, Note.WARN, Note.INFO));
  });

  public static Note.Sink noteSink() {
    return INSTANCE;
  }

  // ##################################################################
  // # END: Note.Sink
  // ##################################################################

  // ##################################################################
  // # BEGIN: Playwright
  // ##################################################################

  static final class BrowserHolder {
    static final Browser BROWSER = init();

    private static Browser init() {
      final Playwright playwright;
      playwright = Playwright.create();

      shutdownHook(playwright);

      final BrowserType chromium;
      chromium = playwright.chromium();

      final boolean headless;
      headless = Boolean.getBoolean("playwright.headless");

      final LaunchOptions launchOptions;
      launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);

      return chromium.launch(launchOptions);
    }

  }

  public static Page page() {
    final String baseUrl;
    baseUrl = "http://localhost:" + DevStart.TESTING_HTTP_PORT;

    final Browser.NewPageOptions options;
    options = new Browser.NewPageOptions().setBaseURL(baseUrl);

    return BrowserHolder.BROWSER.newPage(options);
  }

  public sealed interface Tab extends AutoCloseable permits YTab {

    @SuppressWarnings("exports")
    TabElem byId(Id id);

    @SuppressWarnings("exports")
    TabElem bySelector(String selector);

    @Override
    void close();

    void dev();

    void keyPress(String key);

    void mouseDown();

    void mouseUp();

    void mouseTo(double x, double y);

    void navigate(String path);

    void press(String key);

    String title();

    void waitForFunction(String expression, Object arg);

  }

  public sealed interface TabElem permits YTab.ThisElem {

    void blur();

    void focus();

    void hover();

  }

  public static Y.Tab tabDev() {
    final String baseUrl;
    baseUrl = "http://localhost:" + DevStart.TESTING_HTTP_PORT;

    final Browser.NewPageOptions options;
    options = new Browser.NewPageOptions().setBaseURL(baseUrl);

    final Page page;
    page = BrowserHolder.BROWSER.newPage(options);

    return new YTab(baseUrl, page);
  }

  // ##################################################################
  // # END: Playwright
  // ##################################################################

  // ##################################################################
  // # BEGIN: ShutdownHook
  // ##################################################################

  private static final class ShutdownHookHolder {

    static final App.ShutdownHook INSTANCE = App.ShutdownHook.create(config -> config.noteSink(noteSink()));

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

  public static void test(HttpResponse<String> response, String expected) {
    final StringBuilder sb;
    sb = new StringBuilder("HTTP/");

    switch (response.version()) {
      case HTTP_1_1 -> sb.append("1.1");

      case HTTP_2 -> sb.append("2");

      default -> throw new IllegalArgumentException("Unexpected value: " + response.version());
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

}
