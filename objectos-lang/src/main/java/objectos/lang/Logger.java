/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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

/**
 * An object responsible for logging the events of a program.
 *
 * @see Event
 */
public interface Logger {

  /**
   * Returns {@code true} if the given {@code event} would be logged by this
   * {@code Logger} instance.
   *
   * @param event
   *        the event to test
   *
   * @return {@code true} if the given {@code event} would be logged
   */
  boolean isEnabled(Event event);

  /**
   * Logs the given event that takes no argument.
   *
   * @param event
   *        an event instance
   */
  void log(Event0 event);

  /**
   * Logs the given event that takes one argument.
   *
   * @param <T1> type of the log argument
   * @param event
   *        an event instance
   * @param v1
   *        argument of the logged event
   */
  <T1> void log(Event1<T1> event, T1 v1);

  /**
   * Logs the given event that takes two arguments.
   *
   * @param <T1> type of the first log argument
   * @param <T2> type of the second log argument
   * @param event
   *        an event instance
   * @param v1
   *        first argument of the logged event
   * @param v2
   *        second argument of the logged event
   */
  <T1, T2> void log(Event2<T1, T2> event, T1 v1, T2 v2);

  /**
   * Logs the given event that takes three arguments.
   *
   * @param <T1> type of the first log argument
   * @param <T2> type of the second log argument
   * @param <T3> type of the third log argument
   * @param event
   *        an event instance
   * @param v1
   *        first argument of the logged event
   * @param v2
   *        second argument of the logged event
   * @param v3
   *        third argument of the logged event
   */
  <T1, T2, T3> void log(Event3<T1, T2, T3> event, T1 v1, T2 v2, T3 v3);

  /**
   * Replaces this instance with the given {@code logger} instance if it is
   * possible to do so.
   *
   * @param logger
   *        a {@code Logger} instance that may replace this instance
   *
   * @return the given {@code logger} instance or this instance
   */
  Logger replace(Logger logger);

}
