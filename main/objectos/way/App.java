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
        NoteSink noteSink;
        noteSink = new ConsoleNoteSink(Level.ERROR);

        Note1<String> note;
        note = Note1.error(getClass(), "Invalid argument");

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