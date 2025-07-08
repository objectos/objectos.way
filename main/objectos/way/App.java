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

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.time.Clock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import objectos.way.App.NoteSink.OfConsole;
import objectos.way.App.NoteSink.OfFile;

/**
 * The <strong>Objectos App</strong> main class.
 */
public final class App {

  /**
   * Base class for bootstrapping and starting an Objectos Way application.
   */
  public static abstract class Bootstrap extends AppBootstrap {

    /**
     * Sole constructor.
     */
    protected Bootstrap() {
    }

    /**
     * Starts the application with the specified command line arguments.
     *
     * @param args
     *        the command line arguments
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

  /**
   * Prevents the annotated class from being reloaded by {@link Reloader}.
   */
  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface DoNotReload {}

  /**
   * Allows for registering and obtaining application-level object instances.
   */
  public sealed interface Injector permits AppInjector, Injector.Options {

    /**
     * Configures the creation of an injector.
     */
    sealed interface Options extends Injector permits AppInjectorBuilder {

      /**
       * Registers the specified instance to the specified class.
       *
       * @param <T>
       *        the instance type
       *
       * @param type
       *        the class to which the value is to be associated
       * @param instance
       *        the value to be associated with the specified key
       */
      <T> void putInstance(Class<T> type, T instance);

      /**
       * Registers the specified instance to the specified key.
       *
       * @param <T>
       *        the instance type
       *
       * @param key
       *        the key to which the value is to be associated
       * @param instance
       *        the value to be associated with the specified key
       */
      <T> void putInstance(Lang.Key<T> key, T instance);

    }

    /// Creates a new injector with the specified options.
    ///
    /// @param opts
    ///        allows for setting the options
    ///
    /// @return a newly created injector instance
    static Injector create(Consumer<? super Options> opts) {
      final AppInjectorBuilder builder;
      builder = new AppInjectorBuilder();

      opts.accept(builder);

      return builder.build();
    }

    /**
     * Returns the instance associated to the specified class, or throws if no
     * instance was associated.
     *
     * @param <T>
     *        the instance type
     *
     * @param type
     *        the class object
     *
     * @return the instance associated to the specified class
     */
    <T> T getInstance(Class<T> type);

    /**
     * Returns the instance associated to the specified key object, or throws if
     * no instance was associated.
     *
     * @param <T>
     *        the instance type
     *
     * @param key
     *        the key object
     *
     * @return the instance associated to the specified key
     */
    <T> T getInstance(Lang.Key<T> key);

  }

  private sealed interface NoteSinkOptions {

    /**
     * Sets the clock to the specified value.
     *
     * @param value
     *        a clock instance
     */
    void clock(Clock value);

    /**
     * Sets the note filter to the specified value.
     *
     * @param value
     *        a note predicate
     */
    void filter(Predicate<Note> value);

  }

  /**
   * Provides note sink implementations.
   */
  public sealed interface NoteSink extends Note.Sink permits OfConsole, OfFile, AppNoteSink {

    /**
     * A note sink implementation that sends notes to the console.
     */
    sealed interface OfConsole extends NoteSink permits AppNoteSinkOfConsole {

      /**
       * Configures the creation of a console note sink.
       */
      sealed interface Options extends NoteSinkOptions permits AppNoteSinkOfConsoleBuilder {

        /**
         * Sets the note target to the specified value.
         *
         * @param value
         *        a {@code PrintStream} instance
         */
        void target(PrintStream value);

      }

      /**
       * Creates a console note sink with the default options.
       *
       * @return a newly created console note sink instance
       */
      static OfConsole create() {
        AppNoteSinkOfConsoleBuilder builder;
        builder = new AppNoteSinkOfConsoleBuilder();

        return builder.build();
      }

      /**
       * Creates a console note sink with the specified options.
       *
       * @param opts
       *        allows for setting the options
       *
       * @return a newly created console note sink instance
       */
      static OfConsole create(Consumer<? super Options> opts) {
        final AppNoteSinkOfConsoleBuilder builder;
        builder = new AppNoteSinkOfConsoleBuilder();

        opts.accept(builder);

        return builder.build();
      }

    }

    /**
     * A note sink implementation that writes notes to a regular file.
     */
    sealed interface OfFile extends NoteSink, Closeable permits AppNoteSinkOfFile {

      /**
       * Configures the creation of a file note sink.
       */
      sealed interface Options extends NoteSinkOptions permits AppNoteSinkOfFileBuilder {

        /**
         * Sets the target file to the specified value.
         *
         * @param value
         *        the path instance representing the file
         */
        void file(Path value);

      }

      /**
       * Creates a file note sink with the specified options.
       *
       * @param opts
       *        allows for setting the options
       *
       * @return a newly created file note sink instance
       *
       * @throws IOException
       *         if an I/O error occurs
       */
      static OfFile create(Consumer<? super Options> opts) throws IOException {
        final AppNoteSinkOfFileBuilder builder;
        builder = new AppNoteSinkOfFileBuilder();

        opts.accept(builder);

        return builder.build();
      }

      void rotate() throws IOException;

    }

    /**
     * Sets the note filter to the specified value.
     *
     * @param value
     *        a note predicate
     */
    void filter(Predicate<Note> value);

  }

  /**
   * Represents an application command line option.
   *
   * @param <T> the option type
   */
  public sealed interface Option<T> permits AppOption {

    /**
     * An option configuration.
     *
     * @param <T> the option type
     */
    public sealed interface Configuration<T> {}

    /**
     * Allows for converting command line arguments into instances of the
     * target option type.
     *
     * @param <T> the option type
     */
    @FunctionalInterface
    public interface Converter<T> {

      /// Converts the raw command line argument into the an instance of the target option type.
      ///
      /// @param value
      /// the raw command line argument value
      ///
      /// @return the converted option value
      T convert(String value);

    }

    /**
     * Returns the option value.
     *
     * @return the option value
     */
    T get();

  }

  non-sealed static abstract class OptionConfiguration<T> implements Option.Configuration<T> {

    abstract void accept(AppOption<T> option);

  }

  /**
   * An HTTP handler which reloads the classes of the configured module if
   * changes are observed in the module's location. It is meant to be used
   * during the development of an application.
   */
  public sealed interface Reloader extends Closeable, Http.Handler permits AppReloader {

    /**
     * Configures the creation of an {@code App.Reloader}.
     */
    public sealed interface Options permits AppReloaderBuilder {

      /**
       * Reloads the module when changes are observed in the specified
       * directory.
       *
       * @param value
       *        the directory to watch
       *
       * @throws IllegalArgumentException
       *         if the path does not represent a directory
       */
      void directory(Path value);

      /**
       * Use the specified factory to recreate the HTTP handler instance after
       * filesystem changes are processed.
       *
       * @param value
       *        an HTTP handler factory instance
       */
      void handlerFactory(HandlerFactory value);

      /**
       * Sets the module to be reloaded to the one from the specified name and
       * location.
       *
       * @param name
       *        the module's name
       * @param location
       *        the module's location
       */
      void module(String name, Path location);

      /**
       * Sets the module to be reloaded to the one from the specified class.
       *
       * @param value
       *        the class whose module is to be reloaded
       */
      void moduleOf(Class<?> value);

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
     * A factory for HTTP handler instances. Implementations MUST create the new
     * HTTP handler instance using the provided class loader.
     */
    @FunctionalInterface
    public interface HandlerFactory {

      /**
       * Creates a new HTTP handler by loading classes from the specified class
       * loader.
       *
       * @param classLoader
       *        a newly created class loader instance bound to the reloaded
       *        module
       *
       * @return a newly created HTTP handler instance
       *
       * @throws Exception
       *         when trying to load a non-existing class, trying to reflect a
       *         non-existing member or other error preventing the creation of a
       *         new HTTP handler instance
       */
      Http.Handler reload(ClassLoader classLoader) throws Exception;

    }

    /**
     * Creates a new reloader with the specified options.
     *
     * @param opts
     *        allows for setting the options
     *
     * @return a newly created reloader instance
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    static Reloader create(Consumer<? super Options> opts) throws IOException {
      final AppReloaderBuilder builder;
      builder = new AppReloaderBuilder();

      opts.accept(builder);

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

  }

  /**
   * Thrown to indicate that a particular service failed to start preventing the
   * bootstrap of the application.
   */
  public static final class ServiceFailedException extends RuntimeException {

    private static final long serialVersionUID = -4563807163596633953L;

    ServiceFailedException(String message) {
      super(message);
    }

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
     * Configure the creation of a {@code ShutdownHook}.
     */
    public sealed interface Options permits AppShutdownHookBuilder {

      /**
       * Sets the note sink to the specified value.
       *
       * @param value
       *        a note sink instance
       */
      void noteSink(Note.Sink value);

    }

    /**
     * The notes emitted by a {@code ShutdownHook}.
     */
    public sealed interface Notes permits AppShutdownHook.Notes {

      /**
       * Creates a new {@code Notes} instance.
       *
       * @return a newly created {@code Notes} instance
       */
      static Notes create() {
        return AppShutdownHook.Notes.get();
      }

      /**
       * A note that informs of a object registration in this shutdown hook.
       *
       * @return a note that informs of a object registration in this shutdown
       *         hook
       */
      Note.Ref1<Object> registered();

      /**
       * A note that informs that a specified resource was not registered.
       *
       * @return a note that informs that a specified resource was not
       *         registered
       */
      Note.Ref1<Object> ignored();

    }

    /**
     * Creates a new shutdown hook with the specified options.
     *
     * @param options
     *        allows for setting the options
     *
     * @return a newly created shutdown hook instance
     */
    static ShutdownHook create(Consumer<? super Options> options) {
      final AppShutdownHookBuilder builder;
      builder = new AppShutdownHookBuilder();

      options.accept(builder);

      return builder.build();
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

  private App() {}

  /// Returns a new [ServiceFailedException] indicating that the service with
  /// the specified name failed to start.
  ///
  /// @param name
  /// the name of the service which failed to start
  ///
  /// @return a newly created `ServiceFailedException` instance
  public static ServiceFailedException serviceFailed(String name) {
    return new ServiceFailedException(name);
  }

  /// Returns a new [ServiceFailedException] indicating that the service with
  /// the specified name failed to start due to the specified cause.
  ///
  /// @param name
  /// the name of the service which failed to start
  /// @param cause
  /// the underlying cause
  ///
  /// @return a newly created `ServiceFailedException` instance
  public static ServiceFailedException serviceFailed(String name, Throwable cause) {
    return new ServiceFailedException(name, cause);
  }

}
