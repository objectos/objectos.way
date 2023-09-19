/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.code.internal;

/**
 * <p>
 * This class consists of {@code static} utility methods for implementing
 * fail-fast checks.
 *
 * <p>
 * The checks help a constructor or a method verify if it was invoked
 * under the correct conditions and, if those conditions are not met, inform the
 * calller by throwing a {@code RuntimeException} of a particular type.
 *
 * <p>
 * These are <i>fail-fast</i> checks as they are intended to detect any
 * programming error as early as possible.
 *
 * <p>
 * If any of the methods receive a {@code null} value for a message parameter or
 * as message part parameter then it will act as if it received the string
 * {@code "null"} instead.
 *
 * @since 0.2
 */
public final class Check {

  private Check() {}

  /**
   * <p>
   * Checks if a constructor or method argument pass a certain condition. The
   * condition must be true for the check to pass.
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Check.argument(length &gt; 0, "Length must be greater than zero");</pre>
   *
   * @param condition
   *        the boolean condition to check
   * @param message
   *        the exception message to use if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method
   *
   * @throws IllegalArgumentException
   *         if {@code condition} is false
   */
  public static void argument(boolean condition, Object message) {
    if (!condition) {
      String formatted;
      formatted = format(message);

      throw new IllegalArgumentException(formatted);
    }
  }

  /**
   * <p>
   * Checks if a constructor or method argument pass a certain condition. The
   * condition must be true for the check to pass.
   *
   * <p>
   * If the check fails, the exception message will be constructed from the
   * concatenation of the string representations of the message parts
   * {@code msg1} and {@code msg2}. For example, if invoked like this:
   *
   * <pre>
   * Check.argument(false, "This is an ", "example");</pre>
   *
   * <p>
   * It will throw an exception with a message equal to the following string
   * literal:
   *
   * <pre>"This is an example"</pre>
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Check.argument(param.isCorrect(), param, " is not correct");</pre>
   *
   * @param condition
   *        the boolean condition to check
   * @param msg1
   *        the first part of exception message if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method
   * @param msg2
   *        the second part of exception message if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method
   *
   * @throws IllegalArgumentException
   *         if {@code condition} is false
   */
  public static void argument(boolean condition, Object msg1, Object msg2) {
    if (!condition) {
      String formatted;
      formatted = format(msg1, msg2);

      throw new IllegalArgumentException(formatted);
    }
  }

  /**
   * <p>
   * Checks if a constructor or method argument is not null. The
   * object must not be null for the check to pass.
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Check.notNull(value, "value == null");</pre>
   *
   * @param <T> the type of the reference
   * @param object
   *        the object to check for non-nullness
   * @param message
   *        the exception message to use if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method.
   *
   * @return the {@code object} if it is not null
   *
   * @throws NullPointerException
   *         if {@code object} is null
   */
  public static <T> T notNull(T object, Object message) {
    if (object == null) {
      String formatted;
      formatted = format(message);

      throw new NullPointerException(formatted);
    }

    return object;
  }

  /**
   * <p>
   * Checks if a constructor or method argument is not null. The
   * object must not be null for the check to pass.
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * array[i] = Check.notNull(values[i], "values[", i, "] == null");</pre>
   *
   * @param <T> the type of the reference
   * @param object
   *        the object to check for non-nullness
   * @param msg1
   *        the first part of exception message. It will be converted to a
   *        string using the {@link String#valueOf(Object)} method
   * @param msg2
   *        the second part of exception message
   * @param msg3
   *        the third part of exception message. It will be converted to a
   *        string using the {@link String#valueOf(Object)} method
   *
   * @return the {@code object} if it is not null
   *
   * @throws NullPointerException
   *         if {@code object} is null
   */
  public static <T> T notNull(T object, Object msg1, int msg2, Object msg3) {
    if (object == null) {
      String formatted;
      formatted = format(msg1, msg2, msg3);

      throw new NullPointerException(formatted);
    }

    return object;
  }

  /**
   * <p>
   * Checks if a constructor or method argument is not null. The
   * object must not be null for the check to pass.
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Check.notNull(value, "foo ", " == null");</pre>
   *
   * @param <T> the type of the reference
   * @param object
   *        the object to check for non-nullness
   * @param msg1
   *        the first part of exception message if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method
   * @param msg2
   *        the second part of exception message if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method
   *
   * @return the {@code object} if it is not null
   *
   * @throws NullPointerException
   *         if {@code object} is null
   */
  public static <T> T notNull(T object, Object msg1, Object msg2) {
    if (object == null) {
      String formatted;
      formatted = format(msg1, msg2);

      throw new NullPointerException(formatted);
    }

    return object;
  }

  /**
   * <p>
   * Checks if the enclosing object's state pass a certain condition when this
   * constructor or this method was invoked. The condition must be true for the
   * check to pass.
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Check.state(state == State.NEW, "Cannot call start(): already started!");</pre>
   *
   * @param condition
   *        the boolean condition to check
   * @param message
   *        the exception message to use if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method
   *
   * @throws IllegalStateException
   *         if {@code condition} is false
   */
  public static void state(boolean condition, Object message) {
    if (!condition) {
      String formatted;
      formatted = format(message);

      throw new IllegalStateException(formatted);
    }
  }

  /**
   * <p>
   * Checks if the enclosing object's state pass a certain condition when this
   * constructor or this method was invoked. The condition must be true for the
   * check to pass.
   *
   * <p>
   * If the check fails, the exception message will be constructed from the
   * concatenation of the string representations of the message parts
   * {@code msg1} and {@code msg2}. For example, if invoked like this:
   *
   * <pre>
   * Check.state(false, "This is an ", "example");</pre>
   *
   * <p>
   * It will throw an exception with a message equal to the following string
   * literal:
   *
   * <pre>"This is an example"</pre>
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Check.state(state != State.NEW, "Cannot call start(): state not ", State.NEW);</pre>
   *
   * @param condition
   *        the boolean condition to check
   * @param msg1
   *        the first part of exception message if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method
   * @param msg2
   *        the second part of exception message if the check fails. It will be
   *        converted to a string using the {@link String#valueOf(Object)}
   *        method
   *
   * @throws IllegalStateException
   *         if {@code condition} is false
   */
  public static void state(boolean condition, Object msg1, Object msg2) {
    if (!condition) {
      String formatted;
      formatted = format(msg1, msg2);

      throw new IllegalStateException(formatted);
    }
  }

  private static String format(Object arg) {
    return String.valueOf(arg);
  }

  private static String format(Object p1, int p2, Object p3) {
    StringBuilder sb;
    sb = new StringBuilder();

    sb.append(p1);

    sb.append(p2);

    sb.append(p3);

    return sb.toString();
  }

  private static String format(Object p1, Object p2) {
    StringBuilder sb;
    sb = new StringBuilder();

    sb.append(p1);

    sb.append(p2);

    return sb.toString();
  }

}