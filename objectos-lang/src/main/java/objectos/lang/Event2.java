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
 * A log event that takes two arguments.
 *
 * @param <T1> the type of the first log argument
 * @param <T2> the type of the second log argument
 */
public final class Event2<T1, T2> extends Event {

  /**
   * Creates a new event instance.
   *
   * @param source
   *        a name to identify the source class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source}
   * @param level
   *        the logging level of this event
   */
  public Event2(String source, Object key, Level level) {
    super(source, key, level);
  }

  /**
   * Creates a new DEBUG log event.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the log argument
   * @param <T2> the type of the second log argument
   *
   * @return a new DEBUG log event
   */
  public static <T1, T2> Event2<T1, T2> debug() {
    return create(Level.DEBUG, Event2::new);
  }

  /**
   * Creates a new ERROR log event.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the log argument
   * @param <T2> the type of the second log argument
   *
   * @return a new ERROR log event
   */
  public static <T1, T2> Event2<T1, T2> error() {
    return create(Level.ERROR, Event2::new);
  }

  /**
   * Creates a new INFO log event.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the log argument
   * @param <T2> the type of the second log argument
   *
   * @return a new INFO log event
   */
  public static <T1, T2> Event2<T1, T2> info() {
    return create(Level.INFO, Event2::new);
  }

  /**
   * Creates a new TRACE log event.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the log argument
   * @param <T2> the type of the second log argument
   *
   * @return a new TRACE log event
   */
  public static <T1, T2> Event2<T1, T2> trace() {
    return create(Level.TRACE, Event2::new);
  }

  /**
   * Creates a new WARN log event.
   *
   * <p>
   * The {@code source} and the {@code key} values are inferred from the method
   * immediately calling this method.
   *
   * <p>
   * The <strong>{@code source}</strong> value is the binary name of class
   * enclosing the method calling this method.
   *
   * <p>
   * The <strong>{@code key}</strong> value is a string containing the file
   * name, a colon ':' and the line number where this method has been invoked.
   *
   * @param <T1> the type of the log argument
   * @param <T2> the type of the second log argument
   *
   * @return a new WARN log event
   */
  public static <T1, T2> Event2<T1, T2> warn() {
    return create(Level.WARN, Event2::new);
  }

}