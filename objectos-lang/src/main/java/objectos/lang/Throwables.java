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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Provides {@code static} utility methods for working with {@link Throwable}
 * instances. In particular, provides a single API for working with
 * {@link Throwable} instances in a Java multi-release manner.
 *
 * @since 0.2
 */
public final class Throwables {

  private Throwables() {}

  /**
   * Adds to the first exception the second one as a suppressed
   * exception and returns the first exception. If the first exception is
   * {@code null} then the second exception is returned unchanged.
   *
   * @param exception
   *        the (maybe null) exception
   * @param suppressed
   *        the suppresed exception
   *
   * @return {@code exception} if it not null, {@code suppressed} otherwise
   */
  public static Throwable addSuppressed(Throwable exception, Throwable suppressed) {
    Checks.checkNotNull(suppressed, "suppressed == null");

    if (exception != null) {
      exception.addSuppressed(suppressed);

      return exception;
    } else {
      return suppressed;
    }
  }

  /**
   * Returns a new string whose contents is what would be sent to the standard
   * error stream as a result of invoking {@link Throwable#printStackTrace()}.
   *
   * @param throwable
   *        a throwable instance to analyze
   *
   * @return a string containing the throwable and its backtrace
   */
  public static String printStackTraceToString(Throwable throwable) {
    StringWriter output;
    output = new StringWriter();

    PrintWriter printWriter;
    printWriter = new PrintWriter(output);

    throwable.printStackTrace(printWriter);

    return output.toString();
  }

  /**
   * Throws the specified {@code throwable} as the specified {@code type} if it
   * is possible to do so. More formally:
   *
   * <p>
   * If {@code throwable} is {@code null} then no action is taken.
   *
   * <p>
   * If {@code throwable} is an instance of {@code type}, i.e., if the
   * expression {@code type.isInstance(rethrow)} evaluates to {@code true}, then
   * {@code throwable} is cast to {@code type} and thrown as it is.
   *
   * <p>
   * In all other cases, a new {@link RethrowException} is thrown having the
   * specified {@code throwable} as its cause.
   *
   * @param <X> will be thrown as this {@code Throwable} type
   * @param throwable
   *        a possibly null {@code Throwable} instance to be rethrown if
   *        possible
   * @param type
   *        the type which to throw the {@code throwable} as
   *
   * @throws X
   *         if {@code throwable} is an instance of {@code type} and is
   *         non-null
   * @throws RethrowException
   *         if {@code throwable} is not an instance of {@code type} and is
   *         non-null
   */
  public static <X extends Throwable> void rethrowIfPossible(
      Throwable throwable, Class<X> type)
      throws X {
    Checks.checkNotNull(type, "type == null");

    if (throwable == null) {
      return;
    }

    if (type.isInstance(throwable)) {
      throw type.cast(throwable);
    }

    if (throwable instanceof RuntimeException) {
      throw (RuntimeException) throwable;
    }

    if (throwable instanceof Error) {
      throw (Error) throwable;
    }

    throw new RethrowException("Not of type " + type, throwable);
  }

}