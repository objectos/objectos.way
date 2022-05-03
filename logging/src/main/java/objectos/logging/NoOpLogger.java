/*
 * Copyright (C) 2021-2022 Objectos Software LTDA.
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
package objectos.logging;

/**
 * A no-op {@code Logger} implementation (for the most part). The one exception
 * is the {@link Logger#replace(Logger)} operation that actually returns the
 * supplied value.
 */
public class NoOpLogger implements Logger {

  static final NoOpLogger INSTANCE = new NoOpLogger();

  /**
   * The sole {@code public} constructor.
   */
  public NoOpLogger() {}

  /**
   * Returns the {@code static} instance. This is method is provided as a
   * convenience and to avoid creating multiple instances of this instance.
   *
   * @return the {@code static} instance
   */
  public static NoOpLogger getInstance() {
    return INSTANCE;
  }

  /**
   * Returns {@code false}.
   *
   * @param event
   *        an event instance (ignored)
   *
   * @return {@code false}
   */
  @Override
  public boolean isEnabled(Event event) {
    return false;
  }

  /**
   * Does nothing, this is a no-op logger.
   *
   * @param event
   *        an event instance (ignored)
   */
  @Override
  public void log(Event0 event) {
    // noop
  }

  /**
   * Does nothing, this is a no-op logger.
   *
   * @param event
   *        an event instance (ignored)
   * @param v1
   *        a value (ignored)
   */
  @Override
  public <T1> void log(Event1<T1> event, T1 v1) {
    // noop
  }

  /**
   * Does nothing, this is a no-op logger.
   *
   * @param event
   *        an event instance (ignored)
   * @param v1
   *        a first value (ignored)
   * @param v2
   *        a second value (ignored)
   */
  @Override
  public <T1, T2> void log(Event2<T1, T2> event, T1 v1, T2 v2) {
    // noop
  }

  /**
   * Does nothing, this is a no-op logger.
   *
   * @param event
   *        an event instance (ignored)
   * @param v1
   *        a first value (ignored)
   * @param v2
   *        a second value (ignored)
   * @param v3
   *        a third value (ignored)
   */
  @Override
  public <T1, T2, T3> void log(Event3<T1, T2, T3> event, T1 v1, T2 v2, T3 v3) {
    // noop
  }

  /**
   * Returns the given {@code logger} value.
   *
   * @param logger
   *        a {@code Logger} instance
   *
   * @return the given {@code logger} value
   */
  @Override
  public final Logger replace(Logger logger) {
    Checks.checkNotNull(logger, "logger == null");

    return logger;
  }

}