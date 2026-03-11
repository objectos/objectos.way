/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.http;

import java.util.List;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

/**
 * Provides methods for inspecting the request-target of an HTTP request
 * message.
 *
 * <p>
 * Unless otherwise specified the values returned by the methods of this
 * interface are decoded.
 */
public sealed interface HttpRequestTarget permits HttpRequestLine {

  /**
   * The value of the path component.
   *
   * @return the value of the path component
   */
  String path();

  /// Returns the value of the path parameter with the specified name if it
  /// exists or returns `null` otherwise.
  ///
  /// @param name the name of the path parameter
  ///
  /// @return the value if it exists or `null` if it does not
  String pathParam(String name);

  /// Returns, as an `int`, the value of the path parameter with the specified
  /// name. If the path parameter does not exist or if the value cannot be
  /// converted to an `int` value then the specified default value is returned
  /// instead.
  ///
  /// @param name the name of the path parameter
  /// @param defaultValue the value to be returned if the parameter does exist of
  ///        if its value cannot be converted to an `int` value
  ///
  /// @return the value converted to an `int` if it exists or the specified
  ///         default value otherwise
  default int pathParamAsInt(String name, int defaultValue) {
    String maybe;
    maybe = pathParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  /**
   * Returns the first value of the query parameter with the specified name
   * or {@code null} if there are no values.
   *
   * @param name
   *        the name of the query parameter
   *
   * @return the first value if it exists or {@code null} if it does not
   */
  String queryParam(String name);

  /**
   * Returns all values of the query parameter with the specified name
   * or an empty list if there are no values. The list contains the values in
   * encounter order.
   *
   * @param name
   *        the name of the query parameter
   *
   * @return a list containing all values in encounter order; or an empty list
   *         if the parameter was not present in the request query
   */
  List<String> queryParamAll(String name);

  /// Returns, as an `int`, the first value of the query parameter with the
  /// specified name or returns the specified default value.
  ///
  /// The specified default value will be returned if the query component does
  /// not contain a parameter with the specified name or if the first value of
  /// such parameter does not represent an `int` value.
  ///
  /// @param name the name of the query parameter
  /// @param defaultValue the value to be returned if the parameter does not
  ///        exist or if its first value cannot be converted to an `int` value
  ///
  /// @return the first value converted to `int` if it exists or the specified
  ///         default value otherwise
  default int queryParamAsInt(String name, int defaultValue) {
    final String maybe;
    maybe = queryParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  /// Returns, as a `int`, the first value of the query parameter with the
  /// specified name or returns the value from the specified supplier.
  ///
  /// The value from the specified supplier will be returned if the query
  /// component does not contain a parameter with the specified name or if the
  /// first value of such parameter does not represent a `int` value.
  ///
  /// @param name the name of the query parameter
  /// @param defaultSupplier supplies the value if the parameter does not exist
  ///        or if its first value cannot be converted to an `int` value
  ///
  /// @return the first value converted to `int` if it exists or the value from
  ///         the supplier otherwise
  default int queryParamAsInt(String name, IntSupplier defaultSupplier) {
    final String maybe;
    maybe = queryParam(name);

    if (maybe == null) {
      return defaultSupplier.getAsInt();
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultSupplier.getAsInt();
    }
  }

  /// Returns, as a `long`, the first value of the query parameter with the
  /// specified name or returns the specified default value.
  ///
  /// The specified default value will be returned if the query component does
  /// not contain a parameter with the specified name or if the first value of
  /// such parameter does not represent a `long` value.
  ///
  /// @param name the name of the query parameter
  /// @param defaultValue the value to be returned if the parameter does not
  ///        exist or if its first value cannot be converted to an `long` value
  ///
  /// @return the first value converted to `long` if it exists or the specified
  ///         default value otherwise
  default long queryParamAsLong(String name, long defaultValue) {
    final String maybe;
    maybe = queryParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Long.parseLong(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  /// Returns, as a `long`, the first value of the query parameter with the
  /// specified name or returns the value from the specified supplier.
  ///
  /// The value from the specified supplier will be returned if the query
  /// component does not contain a parameter with the specified name or if the
  /// first value of such parameter does not represent a `long` value.
  ///
  /// @param name the name of the query parameter
  /// @param defaultSupplier supplies the value if the parameter does not exist
  ///        or if its first value cannot be converted to an `long` value
  ///
  /// @return the first value converted to `long` if it exists or the value from
  ///         the supplier otherwise
  default long queryParamAsLong(String name, LongSupplier defaultSupplier) {
    final String maybe;
    maybe = queryParam(name);

    if (maybe == null) {
      return defaultSupplier.getAsLong();
    }

    try {
      return Long.parseLong(maybe);
    } catch (NumberFormatException expected) {
      return defaultSupplier.getAsLong();
    }
  }

  /**
   * The names of all of the query parameters in this request-target.
   *
   * @return the names of all of the query parameters
   */
  Set<String> queryParamNames();

  /**
   * The raw (encoded) value of the path component.
   *
   * @return the raw (encoded) value of the path component
   */
  String rawPath();

  /**
   * The raw (encoded) value of the query component. This method returns
   * {@code null} if this request-target does not have a query component.
   *
   * @return the raw (encoded) value of the query component or {@code null}
   */
  String rawQuery();

  /**
   * Returns the raw (encoded) value of the query component with the specified
   * parameter added or replaced if it exists.
   *
   * <p>
   * If a parameter with the same name already exists in the query, its value
   * is replaced with the specified value. If no such parameter exists, a new
   * parameter is added.
   *
   * <p>
   * Usage example:
   *
   * <pre>{@code
   * // original query is "search=java&sort=asc";
   * Http.RequestTarget target = ...
   *
   * // returns "search=java&sort=desc"
   * target.rawQueryWith("sort", "desc");
   *
   * // returns "search=java&sort=asc&page=2"
   * target.rawQueryWith("page", "2");
   * }</pre>
   *
   * @param name
   *        the name of the parameter to be added or replaced
   * @param value
   *        the value of the parameter to be added or set
   *
   * @return the raw query string with the updated parameter
   *
   * @throws IllegalArgumentException
   *         if {@code name} is blank
   */
  String rawQueryWith(String name, String value);

}