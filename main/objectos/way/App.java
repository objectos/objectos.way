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

import java.util.function.Function;
import objectos.args.CommandLine;
import objectos.args.CommandLineException;
import objectos.args.EnumOption;
import objectos.args.IntegerOption;
import objectos.args.PathOption;
import objectos.args.SetOption;
import objectos.args.StringOption;
import objectos.notes.Level;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;
import objectos.notes.impl.ConsoleNoteSink;

/**
 * The <strong>Objectos App</strong> main class.
 */
public final class App {

  public static abstract class Bootstrap extends AppBootstrap {

    private final CommandLine cli = new CommandLine();

    protected Bootstrap() {
    }

    /**
     * Starts the application with the specified command line arguments.
     */
    public final void start(String[] args) {
      try {
        cli.parse(args);
      } catch (CommandLineException e) {
        e.printMessage();

        System.exit(1);
      }

      try {
        bootstrap();
      } catch (ServiceFailedException e) {
        NoteSink noteSink;
        noteSink = new ConsoleNoteSink(Level.ERROR);

        Note2<String, Throwable> note;
        note = Note2.error(getClass(), "Bootstrap failed [service]");

        String service;
        service = e.getMessage();

        Throwable cause;
        cause = e.getCause();

        noteSink.send(note, service, cause);

        System.exit(2);
      } catch (Throwable e) {
        NoteSink noteSink;
        noteSink = new ConsoleNoteSink(Level.ERROR);

        Note1<Throwable> note;
        note = Note1.error(getClass(), "Bootstrap failed");

        noteSink.send(note, e);

        System.exit(2);
      }
    }

    /**
     * Bootstraps the application.
     */
    protected abstract void bootstrap();

    protected final <E extends Enum<E>> EnumOption<E> newEnumOption(Class<E> type, String name) {
      return cli.newEnumOption(type, name);
    }

    protected final IntegerOption newIntegerOption(String name) {
      return cli.newIntegerOption(name);
    }

    protected final PathOption newPathOption(String name) {
      return cli.newPathOption(name);
    }

    protected final <T> SetOption<T> newSetOption(String string, Function<String, ? extends T> converter) {
      return cli.newSetOption(string, converter);
    }

    protected final StringOption newStringOption(String name) {
      return cli.newStringOption(name);
    }

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

  private App() {}

  public static ServiceFailedException serviceFailed(String name, Throwable cause) {
    return new ServiceFailedException(name, cause);
  }

}