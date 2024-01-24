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
package objectos.lang;

import objectos.notes.Note1;

/**
 * An utility for registering objects with the JVM shutdown hook facility.
 *
 * @see Runtime#addShutdownHook(Thread)
 */
public interface ShutdownHook {

  /**
   * A note that informs of a object registration in this shutdown hook.
   */
  Note1<Object> REGISTRATION = Note1.info(ShutdownHook.class, "Registration [hook]");

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
