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
 * Provides convenience {@code static} methods for creating {@link Event}
 * instances.
 */
public final class Events {

  private Events() {}

  /**
   * Creates a new DEBUG log event.
   *
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   *
   * @return a new DEBUG log event
   */
  public static Event0 debug(
      Class<?> source, String key) {
    return new Event0(
        source, key, Level.DEBUG
    );
  }

  /**
   * Creates a new DEBUG log event requiring one argument of type {@code T1}
   * given by class {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the event type argument
   *
   * @return a new DEBUG log event
   */
  public static <T1> Event1<T1> debug(
      Class<?> source, String key,
      Class<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.DEBUG
    );
  }

  /**
   * Creates a new DEBUG log event requiring two arguments of types {@code T1}
   * and {@code T2} given by classes {@code t1} and {@code t2} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   *
   * @return a new DEBUG log event
   */
  public static <T1, T2> Event2<T1, T2> debug(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.DEBUG
    );
  }

  /**
   * Creates a new DEBUG log event requiring three arguments of types
   * {@code T1}, {@code T2} and {@code T3} given by classes {@code t1},
   * {@code t2} and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   * @param t3
   *        the class of the third event type parameter
   *
   * @return a new DEBUG log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> debug(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2,
      Class<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.DEBUG
    );
  }

  /**
   * Creates a new DEBUG log event requiring one argument of type {@code T1}
   * given by the type hint {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the log argument
   *
   * @return a new DEBUG log event
   *
   * @see TypeHint
   */
  public static <T1> Event1<T1> debug(
      Class<?> source, String key,
      TypeHint<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.DEBUG
    );
  }

  /**
   * Creates a new DEBUG log event requiring two arguments of types {@code T1}
   * and {@code T2} given by the type hints {@code t1} and {@code t2}
   * respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   *
   * @return a new DEBUG log event
   */
  public static <T1, T2> Event2<T1, T2> debug(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.DEBUG
    );
  }

  /**
   * Creates a new DEBUG log event requiring two arguments of types {@code T1},
   * {@code T2} and {@code T3} given by the type hints {@code t1}, {@code t2}
   * and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   * @param t3
   *        the type hint of the third log argument
   *
   * @return a new DEBUG log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> debug(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2,
      TypeHint<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.DEBUG
    );
  }

  /**
   * Creates a new ERROR log event.
   *
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   *
   * @return a new ERROR log event
   */
  public static Event0 error(
      Class<?> source, String key) {
    return new Event0(
        source, key, Level.ERROR
    );
  }

  /**
   * Creates a new ERROR log event requiring one argument of type {@code T1}
   * given by class {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the event type argument
   *
   * @return a new ERROR log event
   */
  public static <T1> Event1<T1> error(
      Class<?> source, String key,
      Class<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.ERROR
    );
  }

  /**
   * Creates a new ERROR log event requiring two arguments of types {@code T1}
   * and {@code T2} given by classes {@code t1} and {@code t2} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   *
   * @return a new ERROR log event
   */
  public static <T1, T2> Event2<T1, T2> error(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.ERROR
    );
  }

  /**
   * Creates a new ERROR log event requiring three arguments of types
   * {@code T1}, {@code T2} and {@code T3} given by classes {@code t1},
   * {@code t2} and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   * @param t3
   *        the class of the third event type parameter
   *
   * @return a new ERROR log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> error(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2,
      Class<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.ERROR
    );
  }

  /**
   * Creates a new ERROR log event requiring one argument of type {@code T1}
   * given by the type hint {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the log argument
   *
   * @return a new ERROR log event
   *
   * @see TypeHint
   */
  public static <T1> Event1<T1> error(
      Class<?> source, String key,
      TypeHint<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.ERROR
    );
  }

  /**
   * Creates a new ERROR log event requiring two arguments of types {@code T1}
   * and {@code T2} given by the type hints {@code t1} and {@code t2}
   * respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   *
   * @return a new ERROR log event
   */
  public static <T1, T2> Event2<T1, T2> error(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.ERROR
    );
  }

  /**
   * Creates a new ERROR log event requiring two arguments of types {@code T1},
   * {@code T2} and {@code T3} given by the type hints {@code t1}, {@code t2}
   * and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   * @param t3
   *        the type hint of the third log argument
   *
   * @return a new ERROR log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> error(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2,
      TypeHint<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.ERROR
    );
  }

  /**
   * Creates a new INFO log event.
   *
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   *
   * @return a new INFO log event
   */
  public static Event0 info(
      Class<?> source, String key) {
    return new Event0(
        source, key, Level.INFO
    );
  }

  /**
   * Creates a new INFO log event requiring one argument of type {@code T1}
   * given by class {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the event type argument
   *
   * @return a new INFO log event
   */
  public static <T1> Event1<T1> info(
      Class<?> source, String key,
      Class<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.INFO
    );
  }

  /**
   * Creates a new INFO log event requiring two arguments of types {@code T1}
   * and {@code T2} given by classes {@code t1} and {@code t2} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   *
   * @return a new INFO log event
   */
  public static <T1, T2> Event2<T1, T2> info(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.INFO
    );
  }

  /**
   * Creates a new INFO log event requiring three arguments of types
   * {@code T1}, {@code T2} and {@code T3} given by classes {@code t1},
   * {@code t2} and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   * @param t3
   *        the class of the third event type parameter
   *
   * @return a new INFO log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> info(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2,
      Class<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.INFO
    );
  }

  /**
   * Creates a new INFO log event requiring one argument of type {@code T1}
   * given by the type hint {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the log argument
   *
   * @return a new INFO log event
   *
   * @see TypeHint
   */
  public static <T1> Event1<T1> info(
      Class<?> source, String key,
      TypeHint<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.INFO
    );
  }

  /**
   * Creates a new INFO log event requiring two arguments of types {@code T1}
   * and {@code T2} given by the type hints {@code t1} and {@code t2}
   * respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   *
   * @return a new INFO log event
   */
  public static <T1, T2> Event2<T1, T2> info(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.INFO
    );
  }

  /**
   * Creates a new INFO log event requiring two arguments of types {@code T1},
   * {@code T2} and {@code T3} given by the type hints {@code t1}, {@code t2}
   * and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   * @param t3
   *        the type hint of the third log argument
   *
   * @return a new INFO log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> info(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2,
      TypeHint<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.INFO
    );
  }

  /**
   * Creates a new TRACE log event.
   *
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   *
   * @return a new TRACE log event
   */
  public static Event0 trace(
      Class<?> source, String key) {
    return new Event0(
        source, key, Level.TRACE
    );
  }

  /**
   * Creates a new TRACE log event requiring one argument of type {@code T1}
   * given by class {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the event type argument
   *
   * @return a new TRACE log event
   */
  public static <T1> Event1<T1> trace(
      Class<?> source, String key,
      Class<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.TRACE
    );
  }

  /**
   * Creates a new TRACE log event requiring two arguments of types {@code T1}
   * and {@code T2} given by classes {@code t1} and {@code t2} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   *
   * @return a new TRACE log event
   */
  public static <T1, T2> Event2<T1, T2> trace(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.TRACE
    );
  }

  /**
   * Creates a new TRACE log event requiring three arguments of types
   * {@code T1}, {@code T2} and {@code T3} given by classes {@code t1},
   * {@code t2} and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   * @param t3
   *        the class of the third event type parameter
   *
   * @return a new TRACE log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> trace(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2,
      Class<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.TRACE
    );
  }

  /**
   * Creates a new TRACE log event requiring one argument of type {@code T1}
   * given by the type hint {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the log argument
   *
   * @return a new TRACE log event
   *
   * @see TypeHint
   */
  public static <T1> Event1<T1> trace(
      Class<?> source, String key,
      TypeHint<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.TRACE
    );
  }

  /**
   * Creates a new TRACE log event requiring two arguments of types {@code T1}
   * and {@code T2} given by the type hints {@code t1} and {@code t2}
   * respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   *
   * @return a new TRACE log event
   */
  public static <T1, T2> Event2<T1, T2> trace(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.TRACE
    );
  }

  /**
   * Creates a new TRACE log event requiring two arguments of types {@code T1},
   * {@code T2} and {@code T3} given by the type hints {@code t1}, {@code t2}
   * and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   * @param t3
   *        the type hint of the third log argument
   *
   * @return a new TRACE log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> trace(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2,
      TypeHint<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.TRACE
    );
  }

  /**
   * Creates a new WARN log event.
   *
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   *
   * @return a new WARN log event
   */
  public static Event0 warn(
      Class<?> source, String key) {
    return new Event0(
        source, key, Level.WARN
    );
  }

  /**
   * Creates a new WARN log event requiring one argument of type {@code T1}
   * given by class {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the event type argument
   *
   * @return a new WARN log event
   */
  public static <T1> Event1<T1> warn(
      Class<?> source, String key,
      Class<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.WARN
    );
  }

  /**
   * Creates a new WARN log event requiring two arguments of types {@code T1}
   * and {@code T2} given by classes {@code t1} and {@code t2} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   *
   * @return a new WARN log event
   */
  public static <T1, T2> Event2<T1, T2> warn(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.WARN
    );
  }

  /**
   * Creates a new WARN log event requiring three arguments of types
   * {@code T1}, {@code T2} and {@code T3} given by classes {@code t1},
   * {@code t2} and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the class of the first event type parameter
   * @param t2
   *        the class of the second event type parameter
   * @param t3
   *        the class of the third event type parameter
   *
   * @return a new WARN log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> warn(
      Class<?> source, String key,
      Class<T1> t1,
      Class<T2> t2,
      Class<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.WARN
    );
  }

  /**
   * Creates a new WARN log event requiring one argument of type {@code T1}
   * given by the type hint {@code t1}.
   *
   * @param <T1> the type of the log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the log argument
   *
   * @return a new WARN log event
   *
   * @see TypeHint
   */
  public static <T1> Event1<T1> warn(
      Class<?> source, String key,
      TypeHint<T1> t1) {
    Checks.checkNotNull(t1, "t1 == null");

    return new Event1<T1>(
        source, key, Level.WARN
    );
  }

  /**
   * Creates a new WARN log event requiring two arguments of types {@code T1}
   * and {@code T2} given by the type hints {@code t1} and {@code t2}
   * respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   *
   * @return a new WARN log event
   */
  public static <T1, T2> Event2<T1, T2> warn(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");

    return new Event2<T1, T2>(
        source, key, Level.WARN
    );
  }

  /**
   * Creates a new WARN log event requiring two arguments of types {@code T1},
   * {@code T2} and {@code T3} given by the type hints {@code t1}, {@code t2}
   * and {@code t3} respectively.
   *
   * @param <T1> the type of the first log argument
   * @param <T2> the type of the second log argument
   * @param <T3> the type of the third log argument
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param t1
   *        the type hint of the first log argument
   * @param t2
   *        the type hint of the second log argument
   * @param t3
   *        the type hint of the third log argument
   *
   * @return a new WARN log event
   */
  public static <T1, T2, T3> Event3<T1, T2, T3> warn(
      Class<?> source, String key,
      TypeHint<T1> t1,
      TypeHint<T2> t2,
      TypeHint<T3> t3) {
    Checks.checkNotNull(t1, "t1 == null");
    Checks.checkNotNull(t2, "t2 == null");
    Checks.checkNotNull(t3, "t3 == null");

    return new Event3<T1, T2, T3>(
        source, key, Level.WARN
    );
  }

}