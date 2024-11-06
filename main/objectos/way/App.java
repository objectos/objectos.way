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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import objectos.notes.Level;
import objectos.notes.LongNote;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.Note3;
import objectos.way.App.NoteSink.OfConsole;
import objectos.way.App.NoteSink.OfFile;

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
        App.NoteSink noteSink;
        noteSink = App.NoteSink.OfConsole.create(config -> {});

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
        App.NoteSink noteSink;
        noteSink = App.NoteSink.OfConsole.create(config -> {});

        Note2<String, Throwable> note;
        note = Note2.error(getClass(), "Bootstrap failed [service]");

        String service;
        service = e.getMessage();

        Throwable cause;
        cause = e.getCause();

        noteSink.send(note, service, cause);

        System.exit(2);
      } catch (Throwable e) {
        App.NoteSink noteSink;
        noteSink = App.NoteSink.OfConsole.create(config -> {});

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

  private sealed interface NoteSinkConfig {

    void clock(Clock clock);

    void filter(Predicate<Note> filter);

    void legacyLevel(Level level);

  }

  public sealed interface NoteSink extends Note.Sink, objectos.notes.NoteSink permits OfConsole, OfFile, AppNoteSink {

    sealed interface OfConsole extends NoteSink permits AppNoteSinkOfConsole {

      sealed interface Config extends NoteSinkConfig permits AppNoteSinkOfConsoleConfig {

        void target(PrintStream target);

      }

      static OfConsole create() {
        AppNoteSinkOfConsoleConfig builder;
        builder = new AppNoteSinkOfConsoleConfig();

        return builder.build();
      }

      static OfConsole create(Consumer<Config> config) {
        AppNoteSinkOfConsoleConfig builder;
        builder = new AppNoteSinkOfConsoleConfig();

        config.accept(builder);

        return builder.build();
      }

    }

    sealed interface OfFile extends NoteSink, Closeable permits AppNoteSinkOfFile {

      sealed interface Config extends NoteSinkConfig permits AppNoteSinkOfFileConfig {}

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

    public sealed interface Notes permits AppShutdownHook.Notes {

      static Notes create() {
        return AppShutdownHook.Notes.get();
      }

      /**
       * A note that informs of a object registration in this shutdown hook.
       */
      Note.Ref1<Object> registered();

      /**
       * A note that informs that a specified resource was not registered.
       */
      Note.Ref1<Object> ignored();

    }

    static ShutdownHook create(Note.Sink noteSink) {
      Objects.requireNonNull(noteSink, "noteSink == null");

      return new AppShutdownHook(noteSink);
    }

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

  public static Reloader.Option noteSink(objectos.notes.NoteSink value) {
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

abstract class LegacyNoteSink implements objectos.notes.NoteSink {

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
  public final objectos.notes.NoteSink replace(objectos.notes.NoteSink sink) {
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

  final void pad(StringBuilder out, String value, int length) {
    int valueLength;
    valueLength = value.length();

    if (valueLength > length) {
      out.append(value, 0, length);

      valueLength = length;
    } else {
      out.append(value);

      int pad;
      pad = length - valueLength;

      for (int i = 0; i < pad; i++) {
        out.append(' ');
      }
    }
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

    out.append(' ');

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