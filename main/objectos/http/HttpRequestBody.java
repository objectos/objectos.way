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

/**
 * Provides methods for reading the body of an HTTP request message.
 */
public interface HttpRequestBody {

  /**
   * Returns an input stream that reads the bytes of this request body.
   *
   * @return an input stream that reads the bytes of this request body.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  InputStream bodyInputStream() throws IOException;

  /**
   * Returns the names of all of the fields contained in this form data
   *
   * @return the names of all of the fields contained in this form data
   *
   * @throws IllegalStateException
   *         if the request body did not contain form data
   */
  Set<String> formParamNames();

  /**
   * Returns the first value of the specified form field name or {@code null}
   * if the field is not present.
   *
   * @param name
   *        the form field name
   *
   * @return the first value or {@code null}
   *
   * @throws IllegalStateException
   *         if the request body did not contain form data
   */
  String formParam(String name);

  /**
   * Returns the first value of the form field with the specified
   * {@code name}, converted to an {@code int} primitive. If the field is not
   * present in the form data or if its value cannot be converted to an
   * {@code int}, the specified {@code defaultValue} is returned.
   *
   * @param name
   *        the field name
   *
   * @param defaultValue
   *        the default value to return if the field is not present or cannot
   *        be converted to an {@code int}
   *
   * @return the value of the form field as an {@code int}, or
   *         {@code defaultValue} if the field is absent or cannot be
   *         converted
   */
  int formParamAsInt(String name, int defaultValue);

  /**
   * Returns the first value of the form field with the specified
   * {@code name}, converted to a {@code long} primitive. If the field is not
   * present in the form data or if its value cannot be converted to a
   * {@code long}, the specified {@code defaultValue} is returned.
   *
   * @param name
   *        the field name
   *
   * @param defaultValue
   *        the default value to return if the field is not present or cannot
   *        be converted to a {@code long}
   *
   * @return the value of the form field as an {@code long}, or
   *         {@code defaultValue} if the field is absent or cannot be
   *         converted
   */
  long formParamAsLong(String name, long defaultValue);

  /**
   * Returns a list containing all values associated to the specified form
   * field name. This method returns an empty list if the field name
   * is not present. In other words, this method never returns {@code null}.
   *
   * @param name
   *        the field name
   *
   * @return a list containing all of the decoded values in encounter order.
   *
   * @throws IllegalStateException
   *         if the request body did not contain form data
   */
  List<String> formParamAll(String name);

  /**
   * Returns an {@code IntStream} of all of the values, converted to
   * {@code int}, associated to the specified field name. Any value that
   * cannot be converted to an {@code int} is mapped to the specified
   * {@code defaultValue} instead.
   *
   * @param name
   *        the field name
   * @param defaultValue
   *        the default value to use if it cannot be converted to an
   *        {@code int}
   *
   * @return an {@code IntStream} of the values associated to the field name
   *
   * @throws IllegalStateException
   *         if the request body did not contain form data
   */
  IntStream formParamAllAsInt(String name, int defaultValue);

  /**
   * Returns a {@code LongStream} of all of the values, converted to
   * {@code long}, associated to the specified field name. Any value that
   * cannot be converted to an {@code long} is mapped to the specified
   * {@code defaultValue} instead.
   *
   * @param name
   *        the field name
   * @param defaultValue
   *        the default value to use if it cannot be converted to an
   *        {@code long}
   *
   * @return an {@code LongStream} of the values associated to the field name
   */
  LongStream formParamAllAsLong(String name, long defaultValue);

}