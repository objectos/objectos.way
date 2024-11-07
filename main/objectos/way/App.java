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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.time.Clock;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
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

        Note.Ref2<String, Throwable> note;
        note = Note.Ref2.create(getClass(), "Bootstrap failed [service]", Note.ERROR);

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

  }

  public sealed interface NoteSink extends Note.Sink permits OfConsole, OfFile, AppNoteSink {

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

      sealed interface Config extends NoteSinkConfig permits AppNoteSinkOfFileConfig {

        void file(Path value);

      }

      static OfFile create(Consumer<Config> config) throws IOException {
        AppNoteSinkOfFileConfig builder;
        builder = new AppNoteSinkOfFileConfig();

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

    /**
     * Configures the creation of an {@code App.Reloader}.
     */
    public sealed interface Config permits AppReloaderConfig {

      /**
       * Sets the binary name of the application's HTTP handler class. This is
       * the class that the reloader will try to reload.
       *
       * @param value
       *        the binary name
       */
      void binaryName(String value);

      /**
       * Watch the specified directory for changes and reload its class files if
       * necessary.
       *
       * @param value
       *        the directory to watch
       *
       * @throws IllegalArgumentException
       *         if the path does not represent a directory
       */
      void directory(Path value);

      /**
       * Sets the note sink to the specified value.
       *
       * @param value
       *        a note sink instance
       */
      void noteSink(Note.Sink value);

      /**
       * Use the specified watch service.
       *
       * @param value
       *        the watch service to use
       */
      void watchService(WatchService value);

    }

    /**
     * Creates a new reloader with the specified configuration.
     *
     * @param config
     *        configuration options of this new reloader instance
     *
     * @return a newly created reloader instance
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    static Reloader create(Consumer<Config> config) throws IOException {
      AppReloaderConfig builder;
      builder = new AppReloaderConfig();

      config.accept(builder);

      return builder.build();
    }

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

  public static ServiceFailedException serviceFailed(String name, Throwable cause) {
    return new ServiceFailedException(name, cause);
  }

}
