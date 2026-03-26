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
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/// Provides methods for reading the body of an HTTP request message.
public sealed interface HttpRequestBody permits HttpRequest {

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