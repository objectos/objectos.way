/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
 * An object responsible for consuming the notes of a program; it behaves as an
 * event listener.
 *
 * <p>
 * <em>Note on {@code null} handling:</em> unlike other methods in this package,
 * the parameterized arguments of the various {@code log} methods of this
 * interface can be {@code null}.
 *
 * @see Note
 *
 * @since 0.2
 */
public interface NoteSink {

  /**
   * Returns {@code true} if the given {@code note} would be consumed by this
   * {@code NoteSink} instance.
   *
   * @param note
   *        the note to test
   *
   * @return {@code true} if the given {@code note} would be consumed
   */
  boolean isEnabled(Note note);

  /**
   * Logs the given note that takes no argument.
   *
   * @param note
   *        an note instance
   */
  void log(Note0 note);

  /**
   * Logs the given note that takes one argument.
   *
   * @param <T1> type of the log argument
   * @param note
   *        an note instance
   * @param v1
   *        argument of the consumed note (can be null)
   */
  <T1> void log(Note1<T1> note, T1 v1);

  /**
   * Logs the given note that takes two arguments.
   *
   * @param <T1> type of the first log argument
   * @param <T2> type of the second log argument
   * @param note
   *        an note instance
   * @param v1
   *        first argument of the consumed note (can be null)
   * @param v2
   *        second argument of the consumed note (can be null)
   */
  <T1, T2> void log(Note2<T1, T2> note, T1 v1, T2 v2);

  /**
   * Logs the given note that takes three arguments.
   *
   * @param <T1> type of the first log argument
   * @param <T2> type of the second log argument
   * @param <T3> type of the third log argument
   * @param note
   *        an note instance
   * @param v1
   *        first argument of the consumed note (can be null)
   * @param v2
   *        second argument of the consumed note (can be null)
   * @param v3
   *        third argument of the consumed note (can be null)
   */
  <T1, T2, T3> void log(Note3<T1, T2, T3> note, T1 v1, T2 v2, T3 v3);

  /**
   * Replaces this instance with the given {@code sink} instance if it is
   * possible to do so.
   *
   * @param sink
   *        a {@code NoteSink} instance that may replace this instance
   *
   * @return the given {@code sink} instance or this instance
   */
  NoteSink replace(NoteSink sink);

}
