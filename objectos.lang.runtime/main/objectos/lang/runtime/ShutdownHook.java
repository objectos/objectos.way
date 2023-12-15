/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.lang.runtime;

import objectos.lang.object.Check;
import objectos.notes.NoteSink;

/**
 * An utility for registering objects with the JVM shutdown hook facility.
 *
 * @see Runtime#addShutdownHook(Thread)
 */
public interface ShutdownHook {

  /**
   * Configures the creation of a {@link ShutdownHook} instance.
   */
  sealed interface Option permits OptionValue {

    /**
     * Defines the note sink to be used by the shutdown hook.
     *
     * @param noteSink
     *        the note sink instance
     *
     * @return an option instance
     */
    static Option noteSink(NoteSink noteSink) {
      Check.notNull(noteSink, "noteSink == null");

      return new OptionValue() {
        @Override
        public final void accept(StandardShutdownHook builder) {
          builder.noteSink(noteSink);
        }
      };
    }

  }

  /**
   * Creates a new {@code ShutdownHook} instance with the default options.
   *
   * @return a new {@code ShutdownHook} instance
   */
  static ShutdownHook of() {
    StandardShutdownHook hook;
    hook = new StandardShutdownHook();

    return hook.register();
  }

  /**
   * Creates a new {@code ShutdownHook} instance with the specified option.
   *
   * @param option
   *        a configuration option
   *
   * @return a new {@code ShutdownHook} instance
   */
  static ShutdownHook of(Option option) {
    Check.notNull(option, "option == null");

    StandardShutdownHook hook;
    hook = new StandardShutdownHook();

    ((OptionValue) option).accept(hook);

    return hook.register();
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
  void addAutoCloseable(AutoCloseable closeable);

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
  void addThread(Thread thread);

}
