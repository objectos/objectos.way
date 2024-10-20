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
package objectos.way;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchService;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import objectos.notes.Level;
import objectos.notes.LongNote;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.Note3;
import objectos.notes.NoteSink;
import objectos.way.App.NoteSink2;
import objectos.way.Note.Marker;

/**
 * The <strong>Objectos App</strong> main class.
 */
public final class App {

  public static abstract class Bootstrap extends AppBootstrap {

    protected Bootstrap() {
    }

    /**
     * Starts the application with the specified command line arguments.
     */
    public final void start(String[] args) {
      parseArgs(args);

      int messagesSize;
      messagesSize = messagesSize();

      if (messagesSize > 0) {
        App.NoteSink2 noteSink;
        noteSink = App.NoteSink2.OfConsole.create(config -> {});

        Note.Ref1<String> note;
        note = Note.Ref1.create(getClass(), "Invalid argument", Note.ERROR);

        for (int idx = 0; idx < messagesSize; idx++) {
          String msg;
          msg = message(idx);

          noteSink.send(note, msg);
        }

        System.exit(1);
      }

      try {
        bootstrap();
      } catch (ServiceFailedException e) {
        App.NoteSink2 noteSink;
        noteSink = App.NoteSink2.OfConsole.create(config -> {});

        Note2<String, Throwable> note;
        note = Note2.error(getClass(), "Bootstrap failed [service]");

        String service;
        service = e.getMessage();

        Throwable cause;
        cause = e.getCause();

        noteSink.send(note, service, cause);

        System.exit(2);
      } catch (Throwable e) {
        App.NoteSink2 noteSink;
        noteSink = App.NoteSink2.OfConsole.create(config -> {});

        Note.Ref1<Throwable> note;
        note = Note.Ref1.create(getClass(), "Bootstrap failed", Note.ERROR);

        noteSink.send(note, e);

        System.exit(2);
      }
    }

    /**
     * Bootstraps the application.
     */
    protected abstract void bootstrap();

  }

  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface DoNotReload {}

  public sealed interface LoggerAdapter permits AppNoteSink {

    void log(String name, Note.Marker level, String message);

    void log(String name, Note.Marker level, String message, Throwable t);

  }

  private sealed interface NoteSinkConfig {

    void clock(Clock clock);

    void filter(Predicate<Note> filter);

    void legacyLevel(Level level);

  }

  public sealed interface NoteSink2 extends Note.Sink, NoteSink {

    sealed interface OfConsole extends NoteSink2 {

      sealed interface Config extends NoteSinkConfig {

        void target(PrintStream target);

      }

      static OfConsole create(Consumer<Config> config) {
        AppNoteSinkOfConsoleConfig builder;
        builder = new AppNoteSinkOfConsoleConfig();

        config.accept(builder);

        return builder.build();
      }

    }

    sealed interface OfFile extends NoteSink2, Closeable {

      sealed interface Config extends NoteSinkConfig {

      }

      static OfFile create(Path file, Consumer<Config> config) throws IOException {
        AppNoteSinkOfFileConfig builder;
        builder = new AppNoteSinkOfFileConfig(file);

        config.accept(builder);

        return builder.build();
      }

      void rotate() throws IOException;

    }

    void filter(Predicate<Note> filter);

  }

  /**
   * Represents an application command line option.
   *
   * @param <T> the option type
   */
  public sealed interface Option<T> permits AppOption {

    /**
     * An option configuration.
     */
    public sealed interface Configuration<T> {}

    /**
     * Converts the raw command line argument into the an instance of the target
     * option type.
     */
    @FunctionalInterface
    public interface Converter<T> {

      T convert(String value);

    }

    T get();

  }

  non-sealed static abstract class OptionConfiguration<T> implements Option.Configuration<T> {

    abstract void accept(AppOption<T> option);

  }

  /**
   * Reloads the application's HTTP handler and its dependencies if changes were
   * observed in configured directories. It is meant to be used during the
   * development of an application.
   */
  public sealed interface Reloader extends Closeable permits AppReloader {

    public sealed interface Option {}

    /**
     * Closes this class reloader.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    @Override
    void close() throws IOException;

    /**
     * Returns the class object representing the class of this reloader that is
     * in sync with any file system change.
     *
     * @return the class object that is in sync with any file system change
     *
     * @throws ClassNotFoundException
     *         if a class with the configured binary name could not be found
     * @throws IOException
     *         if an I/O error occurs
     */
    Class<?> get() throws ClassNotFoundException, IOException;

  }

  /**
   * Thrown to indicate that a particular service failed to start preventing the
   * bootstrap of the application.
   */
  public static final class ServiceFailedException extends RuntimeException {

    private static final long serialVersionUID = -4563807163596633953L;

    ServiceFailedException(String message, Throwable cause) {
      super(message, cause);
    }

  }

  /**
   * An utility for registering objects with the JVM shutdown hook facility.
   *
   * @see Runtime#addShutdownHook(Thread)
   */
  public sealed interface ShutdownHook permits AppShutdownHook {

    /**
     * A note that informs of a object registration in this shutdown hook.
     */
    Note1<Object> REGISTRATION = Note1.info(ShutdownHook.class, "Registration");

    /**
     * A note that informs that a specified resource was not registered.
     */
    Note1<Object> IGNORED = Note1.info(ShutdownHook.class, "Ignored");

    /**
     * Closes the specified {@link AutoCloseable} instance when this shutdown
     * hook runs.
     *
     * <p>
     * In other words, the closeable {@link AutoCloseable#close()} method is
     * called by the shutdown hook when the latter runs.
     *
     * @param closeable
     *        the auto closeable instance to be closed
     */
    void register(AutoCloseable closeable);

    /**
     * Registers the specified resource with this shutdown hook if it is
     * possible to do so.
     *
     * @param resource
     *        the resource to be registered (if possible)
     */
    void registerIfPossible(Object resource);

    /**
     * Interrupts the specified {@link Thread} instance when this shutdown hook
     * runs.
     *
     * <p>
     * In other words, the thread {@link Thread#interrupt()} is called by the
     * shutdown hook when the latter runs.
     *
     * @param thread
     *        the thread instance to be interrupted
     */
    void registerThread(Thread thread);

  }

  // non-public types

  non-sealed static class CreateOption implements Reloader.Option {

    void acceptReloader(AppReloader reloader) {
      throw new UnsupportedOperationException();
    }

  }

  private static class ReloadingHandlerFactory1 implements Http.HandlerFactory {

    private final App.Reloader reloader;

    private final Class<?> type1;

    private final Object value1;

    public ReloadingHandlerFactory1(Reloader reloader, Class<?> type1, Object value1) {
      this.reloader = reloader;
      this.type1 = type1;
      this.value1 = value1;
    }

    @Override
    public final Http.Handler create() throws Exception {
      Class<?> handlerClass;
      handlerClass = reloader.get();

      Constructor<?> constructor;
      constructor = handlerClass.getConstructor(type1);

      Object instance;
      instance = constructor.newInstance(value1);

      Http.Module module;
      module = (Http.Module) instance;

      return module.compile();
    }

  }

  private App() {}

  public static <T1> Http.HandlerFactory createHandlerFactory(Reloader reloader, Class<T1> type1, T1 value1) {
    Check.notNull(reloader, "reloader == null");
    Check.notNull(type1, "type1 == null");
    Check.notNull(value1, "value1 == null");

    return new ReloadingHandlerFactory1(reloader, type1, value1);
  }

  public static Reloader createReloader(String binaryName, WatchService watchService, Reloader.Option... options) throws IOException {
    Check.notNull(binaryName, "binaryName == null");
    Check.notNull(watchService, "watchService == null");

    AppReloader builder;
    builder = new AppReloader(binaryName, watchService);

    for (int i = 0; i < options.length; i++) {
      Reloader.Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      CreateOption option;
      option = (CreateOption) o;

      option.acceptReloader(builder);
    }

    return builder.init();
  }

  public static ShutdownHook createShutdownHook(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new AppShutdownHook(noteSink);
  }

  public static Reloader.Option noteSink(NoteSink value) {
    Check.notNull(value, "value == null");

    return new CreateOption() {
      @Override
      final void acceptReloader(AppReloader reloader) {
        reloader.noteSink(value);
      }
    };
  }

  public static ServiceFailedException serviceFailed(String name, Throwable cause) {
    return new ServiceFailedException(name, cause);
  }

  public static Reloader.Option watchDirectory(Path path) {
    if (!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Path does not represent a directory: " + path);
    }

    return new CreateOption() {
      @Override
      final void acceptReloader(AppReloader reloader) {
        reloader.addDirectory(path);
      }
    };
  }

}

abstract class LegacyNoteSink implements NoteSink {

  Level level;

  Clock clock;

  public final Level level() {
    return level;
  }

  public final void level(Level level) {
    this.level = Objects.requireNonNull(level, "level == null");
  }

  public final boolean isEnabled(Level level) {
    return this.level.compareTo(level) <= 0;
  }

  @Override
  public final boolean isEnabled(objectos.notes.Note note) {
    if (note == null) {
      return false;
    }

    return note.isEnabled(level);
  }

  @Override
  public final NoteSink replace(NoteSink sink) {
    return this;
  }

  @Override
  public final void send(Note0 note) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    write(out);
  }

  @Override
  public final void send(LongNote note, long value) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    formatLong(out, value);

    write(out);
  }

  @Override
  public final <T1> void send(Note1<T1> note, T1 v1) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    formatLastValue(out, v1);

    write(out);
  }

  @Override
  public final <T1, T2> void send(Note2<T1, T2> note, T1 v1, T2 v2) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    formatValue(out, v1);

    formatLastValue(out, v2);

    write(out);
  }

  @Override
  public final <T1, T2, T3> void send(Note3<T1, T2, T3> note, T1 v1, T2 v2, T3 v3) {
    if (note == null) {
      return;
    }

    if (!note.isEnabled(level)) {
      return;
    }

    StringBuilder out;
    out = format(note);

    formatValue(out, v1);

    formatValue(out, v2);

    formatLastValue(out, v3);

    write(out);
  }

  public final void slf4j(String name, Level level, String message) {
    StringBuilder out;
    out = format0(level, name, message);

    write(out);
  }

  public final void slf4j(String name, Level level, String message, Throwable t) {
    StringBuilder out;
    out = format0(level, name, message);

    if (t != null) {
      formatThrowable(out, t);
    }

    write(out);
  }

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private StringBuilder format(objectos.notes.Note note) {
    return format0(
        note.level(),

        note.source(),

        note.key()
    );
  }

  private StringBuilder format0(Level level, String source, Object key) {
    StringBuilder out;
    out = new StringBuilder();

    ZonedDateTime date;
    date = ZonedDateTime.now(clock);

    out.append(DATE_FORMAT.format(date));

    out.append(' ');

    String levelName;
    levelName = level.name();

    pad(out, levelName, 5);

    out.append(" --- ");

    out.append('[');

    Thread currentThread;
    currentThread = Thread.currentThread();

    String thread;
    thread = currentThread.getName();

    pad(out, thread, 15);

    out.append(']');

    out.append(' ');

    abbreviate(out, source, 40);

    out.append(' ');
    out.append(':');
    out.append(' ');

    out.append(key);

    return out;
  }

  private void abbreviate(StringBuilder out, String source, int length) {
    String result;
    result = source;

    int resultLength;
    resultLength = result.length();

    if (resultLength > length) {
      int start;
      start = resultLength - length;

      result = result.substring(start, resultLength);
    }

    out.append(result);

    int pad;
    pad = length - result.length();

    for (int i = 0; i < pad; i++) {
      out.append(' ');
    }
  }

  private void formatLong(StringBuilder out, long value) {
    out.append(' ');

    out.append(value);
  }

  private void formatValue(StringBuilder out, Object value) {
    out.append(' ');

    out.append(value);
  }

  private void formatLastValue(StringBuilder out, Object value) {
    if (value instanceof Throwable t) {
      formatThrowable(out, t);
    } else {
      formatValue(out, value);
    }
  }

  private void formatThrowable(StringBuilder out, Throwable t) {
    out.append('\n');

    StringBuilderWriter writer;
    writer = new StringBuilderWriter(out);

    PrintWriter printWriter;
    printWriter = new PrintWriter(writer);

    t.printStackTrace(printWriter);
  }

  private void pad(StringBuilder out, String source, int length) {
    String result;
    result = source;

    if (result.length() > length) {
      result = result.substring(0, length);
    }

    out.append(result);

    int pad;
    pad = length - result.length();

    for (int i = 0; i < pad; i++) {
      out.append(' ');
    }
  }

  private void write(StringBuilder out) {
    out.append('\n');

    String s;
    s = out.toString();

    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    writeBytes(bytes);
  }

  protected abstract void writeBytes(byte[] bytes);

  private static class StringBuilderWriter extends Writer {

    private final StringBuilder out;

    public StringBuilderWriter(StringBuilder out) {
      this.out = out;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
      out.append(cbuf, off, len);
    }

    @Override
    public void flush() {
      // noop, not buffered
    }

    @Override
    public void close() {
      // noop, in-memory only
    }

  }

}

sealed abstract class AppNoteSink extends LegacyNoteSink implements App.LoggerAdapter, NoteSink2 {

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private Predicate<Note> filter;

  private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

  private final Lock r = rwl.readLock();

  final Lock w = rwl.writeLock();

  AppNoteSink(Clock clock, Predicate<Note> filter) {
    this.clock = clock;

    this.filter = filter;
  }

  @Override
  public final void filter(Predicate<Note> filter) {
    Objects.requireNonNull(filter, "filter == null");

    w.lock();
    try {
      this.filter = filter;
    } finally {
      w.unlock();
    }
  }

  @Override
  public final boolean isEnabled(Note note) {
    if (note == null) {
      return false;
    }

    return test(note);
  }

  @Override
  public final void send(Note.Int1 note, int value) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatInt(out, value);

    write(out);
  }

  @Override
  public final void send(Note.Int2 note, int value1, int value2) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatInt(out, value1);

    formatInt(out, value2);

    write(out);
  }

  @Override
  public final void send(Note.Int3 note, int value1, int value2, int value3) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatInt(out, value1);

    formatInt(out, value2);

    formatInt(out, value3);

    write(out);
  }

  @Override
  public final void send(Note.Long1 note, long value) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatLong(out, value);

    write(out);
  }

  @Override
  public final void send(Note.Long2 note, long value1, long value2) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatLong(out, value1);

    formatLong(out, value2);

    write(out);
  }

  @Override
  public final void send(Note.Ref0 note) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    write(out);
  }

  @Override
  public final <T1> void send(Note.Ref1<T1> note, T1 value) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatLastValue(out, value);

    write(out);
  }

  @Override
  public final <T1, T2> void send(Note.Ref2<T1, T2> note, T1 value1, T2 value2) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatValue(out, value1);

    formatLastValue(out, value2);

    write(out);
  }

  @Override
  public final <T1, T2, T3> void send(Note.Ref3<T1, T2, T3> note, T1 value1, T2 value2, T3 value3) {
    if (note == null) {
      return;
    }

    if (!test(note)) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(out, note);

    formatValue(out, value1);

    formatValue(out, value2);

    formatLastValue(out, value3);

    write(out);
  }

  @Override
  public final void log(String name, Marker level, String message) {
    if (level == null) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(
        out,

        level,

        String.valueOf(name),

        String.valueOf(message)
    );

    write(out);
  }

  @Override
  public final void log(String name, Marker level, String message, Throwable t) {
    if (level == null) {
      return;
    }

    StringBuilder out;
    out = new StringBuilder();

    format(
        out,

        level,

        String.valueOf(name),

        String.valueOf(message)
    );

    if (t != null) {
      formatThrowable(out, t);
    }

    write(out);
  }

  @Override
  protected abstract void writeBytes(byte[] bytes);

  private boolean test(Note note) {
    r.lock();
    try {
      return filter.test(note);
    } finally {
      r.unlock();
    }
  }

  private void format(StringBuilder out, Note note) {
    format(
        out,

        note.marker(),

        note.source(),

        note.key()
    );
  }

  private void format(StringBuilder out, Marker marker, String source, String key) {
    LocalDateTime date;
    date = LocalDateTime.now(clock);

    out.append(dateFormat.format(date));

    out.append(' ');

    String markerName;
    markerName = marker.name();

    pad(out, markerName, 5);

    out.append(" --- ");

    out.append('[');

    Thread thread;
    thread = Thread.currentThread();

    String threadName;
    threadName = thread.getName();

    pad(out, threadName, 15);

    out.append(']');

    out.append(' ');

    pad(out, source, 40);

    out.append(' ');
    out.append(':');
    out.append(' ');

    out.append(key);
  }

  private void formatInt(StringBuilder out, int value) {
    out.append(' ');

    out.append(value);
  }

  private void formatLong(StringBuilder out, long value) {
    out.append(' ');

    out.append(value);
  }

  private void formatValue(StringBuilder out, Object value) {
    out.append(' ');

    out.append(value);
  }

  private void formatLastValue(StringBuilder out, Object value) {
    if (value instanceof Throwable t) {
      formatThrowable(out, t);
    } else {
      formatValue(out, value);
    }
  }

  private void formatThrowable(StringBuilder out, Throwable t) {
    out.append('\n');

    StringBuilderWriter writer;
    writer = new StringBuilderWriter(out);

    PrintWriter printWriter;
    printWriter = new PrintWriter(writer);

    t.printStackTrace(printWriter);
  }

  private void pad(StringBuilder out, String value, int length) {
    String result;
    result = value;

    if (result.length() > length) {
      result = result.substring(0, length);
    }

    out.append(result);

    int pad;
    pad = length - result.length();

    for (int i = 0; i < pad; i++) {
      out.append(' ');
    }
  }

  private void write(StringBuilder out) {
    out.append('\n');

    String s;
    s = out.toString();

    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    writeBytes(bytes);
  }

  private static class StringBuilderWriter extends Writer {

    private final StringBuilder out;

    public StringBuilderWriter(StringBuilder out) {
      this.out = out;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
      out.append(cbuf, off, len);
    }

    @Override
    public void flush() {
      // noop, not buffered
    }

    @Override
    public void close() {
      // noop, in-memory only
    }

  }

}

final class AppNoteSinkOfConsole extends AppNoteSink implements App.NoteSink2.OfConsole {

  private final PrintStream target;

  AppNoteSinkOfConsole(Clock clock, Predicate<Note> filter, PrintStream target) {
    super(clock, filter);

    this.target = target;
  }

  @Override
  protected final void writeBytes(byte[] bytes) {
    target.writeBytes(bytes);
  }

}

final class AppNoteSinkOfConsoleConfig implements App.NoteSink2.OfConsole.Config {

  private Clock clock = Clock.systemDefaultZone();

  private Predicate<Note> filter = note -> true;

  private PrintStream target = System.out;

  private Level legacyLevel = Level.TRACE;

  @Override
  public final void clock(Clock clock) {
    this.clock = Objects.requireNonNull(clock, "clock == null");
  }

  @Override
  public final void filter(Predicate<Note> filter) {
    this.filter = Objects.requireNonNull(filter, "filter == null");
  }

  @Override
  public final void legacyLevel(Level level) {
    legacyLevel = Objects.requireNonNull(level, "level == null");
  }

  @Override
  public final void target(PrintStream target) {
    this.target = Objects.requireNonNull(target, "target == null");
  }

  final AppNoteSinkOfConsole build() {
    AppNoteSinkOfConsole result;
    result = new AppNoteSinkOfConsole(clock, filter, target);

    result.level(legacyLevel);

    return result;
  }

}

final class AppNoteSinkOfFile extends AppNoteSink implements App.NoteSink2.OfFile {

  private final ByteBuffer buffer;

  private final SeekableByteChannel channel;

  private boolean active;

  AppNoteSinkOfFile(Clock clock, Predicate<Note> filter, ByteBuffer buffer, SeekableByteChannel channel) {
    super(clock, filter);

    this.buffer = buffer;

    this.channel = channel;

    active = true;
  }

  @Override
  public final void close() throws IOException {
    w.lock();
    try {
      channel.close();
    } finally {
      active = false;

      w.unlock();
    }
  }

  @Override
  public final void rotate() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  protected final void writeBytes(byte[] bytes) {
    if (!active) {
      return;
    }

    int remaining;
    remaining = bytes.length;

    w.lock();
    try {
      while (remaining > 0) {
        buffer.clear();

        int length;
        length = Math.min(remaining, buffer.remaining());

        int offset;
        offset = bytes.length - remaining;

        buffer.put(bytes, offset, length);

        buffer.flip();

        while (buffer.hasRemaining()) {
          channel.write(buffer);
        }

        remaining -= length;
      }
    } catch (IOException e) {

      // not much we can do here

      e.printStackTrace();

      active = false;

    } finally {
      w.unlock();
    }
  }

}

final class AppNoteSinkOfFileConfig implements App.NoteSink2.OfFile.Config {

  private final int bufferSize = 4096;

  private Clock clock = Clock.systemDefaultZone();

  private Predicate<Note> filter = note -> true;

  private Level legacyLevel = Level.TRACE;

  private final Path file;

  public AppNoteSinkOfFileConfig(Path file) {
    this.file = Objects.requireNonNull(file, "file == null");
  }

  @Override
  public final void clock(Clock clock) {
    this.clock = Objects.requireNonNull(clock, "clock == null");
  }

  @Override
  public final void filter(Predicate<Note> filter) {
    this.filter = Objects.requireNonNull(filter, "filter == null");
  }

  @Override
  public final void legacyLevel(Level level) {
    legacyLevel = Objects.requireNonNull(level, "level == null");
  }

  final AppNoteSinkOfFile build() throws IOException {
    ByteBuffer buffer;
    buffer = ByteBuffer.allocateDirect(bufferSize);

    Path parent;
    parent = file.getParent();

    if (!Files.exists(parent)) {
      Files.createDirectories(parent);
    }

    if (Files.exists(file)) {
      LocalDateTime now;
      now = LocalDateTime.now(clock);

      String suffix;
      suffix = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now);

      Path target;
      target = file.resolveSibling(file.getFileName().toString() + "." + suffix);

      Files.copy(file, target);
    }

    SeekableByteChannel channel;
    channel = Files.newByteChannel(
        file,

        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
    );

    AppNoteSinkOfFile result;
    result = new AppNoteSinkOfFile(clock, filter, buffer, channel);

    result.level(legacyLevel);

    return result;
  }

}