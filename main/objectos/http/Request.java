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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import objectos.lang.Key;

/// An HTTP request message.
///
/// Unless otherwise specified the values returned by the methods of this
/// interface are decoded.
public sealed interface Request extends Result permits RequestPojo {

  /// Configures the creation of a stand-alone request instance.
  sealed interface Options permits RequestBuilder {

    /// Adds the specified header field to this request.
    ///
    /// @param name the header field name
    /// @param value the header field value
    void header(HttpHeaderName name, String value);

    /// Sets the request method to the specified value. Defaults to the `GET`
    /// method when not specified.
    ///
    /// @param value the HTTP method
    void method(HttpMethod value);

    /// Sets the request-target path component to the specified value. Defaults
    /// to `/` when not specified.
    ///
    /// @param value the decoded path value
    void path(String value);

    /// Associates a session to this request and store the provided key-value
    /// pair in the former.
    ///
    /// @param <T> the type of the attribute
    /// @param key the class object providing the attribute name
    /// @param value the value to be stored
    ///
    /// @return the previously associated value or `null`
    <T> T sessionAttr(Class<T> key, T value);

  }

  /// Creates a stand-alone request instance; typically used in test cases.
  ///
  /// @param opts allows for setting the options
  ///
  /// @return a newly created request instance with the configured options
  static Request create(Consumer<? super Options> opts) {
    final RequestBuilder builder;
    builder = new RequestBuilder();

    opts.accept(builder);

    return builder.build();
  }

  // ##################################################################
  // # BEGIN: request attributes
  // ##################################################################

  /// Returns the request value associated to the name of the specified class,
  /// or `null` if no value exists.
  ///
  /// @param <T> the type of the request value
  /// @param name the class providing the name
  ///
  /// @return the request value or `null`
  <T> T attr(Class<T> name);

  /// Returns the request value associated to the specified key, or `null` if no
  /// value exists.
  ///
  /// @param <T> the type of the request value
  /// @param key the key to look for
  ///
  /// @return the request value or `null`
  <T> T attr(Key<T> key);

  /// Stores, in this request, the specified value by associating it to the name
  /// of the specified class.
  ///
  /// Stored values are reset between requests. If an object is already
  /// associated to the specified key it is replaced with the specified value.
  /// Values must not be `null`.
  ///
  /// @param <T> the type of the request value
  /// @param name the class providing the name
  /// @param value the object to be stored in this request
  <T> void attr(Class<T> name, T value);

  /// Stores, in this request, the specified value by associating it to the
  /// specified key.
  ///
  /// Stored values are reset between requests. If an object is already
  /// associated to the specified key it is replaced with the specified value.
  /// Values must not be `null`.
  ///
  /// @param <T> the type of the request value
  /// @param key the object to serve as key
  /// @param value the object to be stored in this request
  <T> void attr(Key<T> key, T value);

  // ##################################################################
  // # END: request attributes
  // ##################################################################

  // ##################################################################
  // # BEGIN: Session Support
  // ##################################################################

  /// Returns `true` if a session is associated to this request, returns `false`
  /// otherwise.
  ///
  /// @return {@code true} if a session is associated to this request,
  ///         otherwise {@code false}
  boolean sessionPresent();

  /// Returns the session value associated to the specified class name, or
  /// `null` if no value is associated.
  ///
  /// @param <T> the type of the session value
  /// @param key the class object whose name serves as the key
  ///
  /// @return the session value, or `null` if no value is associated
  <T> T sessionAttr(Class<T> key);

  /// Returns the session value associated to the specified key, or `null` if no
  /// value is associated.
  ///
  /// @param <T> the type of the session value
  /// @param key the key object whose associated value is to be returned
  ///
  /// @return the session value, or `null` if no value is associated
  <T> T sessionAttr(Key<T> key);

  /// Using the name of the specified class as the key, associate the specified
  /// value to this session.
  ///
  /// @param <T> the type of the session value
  /// @param key the class object whose name will serve as the key
  /// @param value the session value
  ///
  /// @return the previous session value, or `null` if no value was associated
  <T> T sessionAttr(Class<T> key, T value);

  /// Using the specified key, associates the specified value to this session.
  ///
  /// @param <T> the type of the session value
  /// @param key the key object
  /// @param value the session value
  ///
  /// @return the previous session value, or `null` if no value was associated
  <T> T sessionAttr(Key<T> key, T value);

  /// Invalidates the session associated to this request. This method performs
  /// no operation if no session is associated to the request.
  void sessionInvalidate();

  // ##################################################################
  // # END: Session Support
  // ##################################################################

  // ##################################################################
  // # BEGIN: Request line
  // ##################################################################

  /// Returns the method of this request message.
  ///
  /// @return the method of this request message
  HttpMethod method();

  /// Returns the value of the path component.
  ///
  /// @return the value of the path component
  String path();

  /// Returns the first value of the query parameter with the specified name or
  /// `null` if there are no values.
  ///
  /// @param name the name of the query parameter
  ///
  /// @return the first value if it exists or `null` if it does not
  String queryParam(String name);

  /// Returns all values of the query parameter with the specified name or an
  /// empty list if there are no values. The list contains the values in
  /// encounter order.
  ///
  /// @param name the name of the query parameter
  ///
  /// @return a list containing all values in encounter order; or an empty list
  ///         if the parameter was not present in the request query
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

  /// The names of all of the query parameters in this request-target.
  ///
  /// @return the names of all of the query parameters
  Set<String> queryParamNames();

  /// The raw (encoded) value of the path component.
  ///
  /// @return the raw (encoded) value of the path component
  String rawPath();

  /// The raw (encoded) value of the query component. This method returns `null`
  /// if this request-target does not have a query component.
  ///
  /// @return the raw (encoded) value of the query component or `null`
  String rawQuery();

  /// Returns the raw (encoded) value of the query component with the specified
  /// parameter added or replaced if it exists.
  ///
  /// If a parameter with the same name already exists in the query, its value is
  /// replaced with the specified value. If no such parameter exists, a new
  /// parameter is added.
  ///
  /// Usage example:
  ///
  /// ```java
  /// // original query is "search=java&sort=asc";
  /// Http.RequestTarget target = ...
  ///
  /// // returns "search=java&sort=desc"
  /// target.rawQueryWith("sort", "desc");
  ///
  /// // returns "search=java&sort=asc&page=2"
  /// target.rawQueryWith("page", "2");
  /// ```
  ///
  /// @param name the name of the parameter to be added or replaced
  /// @param value the value of the parameter to be added or set
  ///
  /// @return the raw query string with the updated parameter
  ///
  /// @throws IllegalArgumentException if `name` is blank
  String rawQueryWith(String name, String value);

  // ##################################################################
  // # End: Request line
  // ##################################################################

  /// Returns the value of the first field line having the specified name;
  /// returns `null` if the field line is not present.
  ///
  /// @param name the name of the header field line
  ///
  /// @return the value of first field line or `null` if a field line with the
  ///         specified name is not present.
  String header(HttpHeaderName name);

  /// Returns an input stream that reads the bytes of this request body.
  ///
  /// @return an input stream that reads the bytes of this request body.
  ///
  /// @throws IOException if an I/O error occurs
  InputStream bodyInputStream() throws IOException;

  /// Returns the names of all of the fields contained in this form data
  ///
  /// @return the names of all of the fields contained in this form data
  ///
  /// @throws IllegalStateException if the request body did not contain form data
  Set<String> formParamNames();

  /// Returns the first value of the specified form field name or `null` if the
  /// field is not present.
  ///
  /// @param name the form field name
  ///
  /// @return the first value or `null`
  ///
  /// @throws IllegalStateException if the request body did not contain form data
  String formParam(String name);

  /// Returns the first value of the form field with the specified `name`,
  /// converted to an `int` primitive. If the field is not present in the form
  /// data or if its value cannot be converted to an `int`, the specified
  /// `defaultValue` is returned.
  ///
  /// @param name the field name
  ///
  /// @param defaultValue the default value to return if the field is not present
  ///        or cannot be converted to an `int`
  ///
  /// @return the value of the form field as an `int`, or `defaultValue` if the
  ///         field is absent or cannot be converted
  int formParamAsInt(String name, int defaultValue);

  /// Returns the first value of the form field with the specified `name`,
  /// converted to a `long` primitive. If the field is not present in the form
  /// data or if its value cannot be converted to a `long`, the specified
  /// `defaultValue` is returned.
  ///
  /// @param name the field name
  ///
  /// @param defaultValue the default value to return if the field is not present
  ///        or cannot be converted to a `long`
  ///
  /// @return the value of the form field as an `long`, or `defaultValue` if the
  ///         field is absent or cannot be converted
  long formParamAsLong(String name, long defaultValue);

  /// Returns a list containing all values associated to the specified form
  /// field name. This method returns an empty list if the field name is not
  /// present. In other words, this method never returns `null`.
  ///
  /// @param name the field name
  ///
  /// @return a list containing all of the decoded values in encounter order.
  ///
  /// @throws IllegalStateException if the request body did not contain form data
  List<String> formParamAll(String name);

  /// Returns an `IntStream` of all of the values, converted to `int`,
  /// associated to the specified field name. Any value that cannot be converted
  /// to an `int` is mapped to the specified `defaultValue` instead.
  ///
  /// @param name the field name
  /// @param defaultValue the default value to use if it cannot be converted to
  ///        an `int`
  ///
  /// @return an `IntStream` of the values associated to the field name
  ///
  /// @throws IllegalStateException if the request body did not contain form data
  IntStream formParamAllAsInt(String name, int defaultValue);

  /// Returns a `LongStream` of all of the values, converted to `long`,
  /// associated to the specified field name. Any value that cannot be converted
  /// to an `long` is mapped to the specified `defaultValue` instead.
  ///
  /// @param name the field name
  /// @param defaultValue the default value to use if it cannot be converted to
  ///        an `long`
  ///
  /// @return an `LongStream` of the values associated to the field name
  LongStream formParamAllAsLong(String name, long defaultValue);

}