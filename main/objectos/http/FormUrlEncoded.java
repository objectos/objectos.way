/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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

/**
 * The parsed and decoded body of a {@code application/x-www-form-urlencoded}
 * HTTP message.
 */
public interface FormUrlEncoded {

  /**
   * Parse the specified body as if it is the body of a
   * {@code application/x-www-form-urlencoded} HTTP message.
   *
   * @param body
   *        the body of the HTTP message to parse
   *
   * @throws IOException
   *         if an I/O error occurs while reading the body
   */
  static FormUrlEncoded parse(Body body) throws IOException {
    return WayFormUrlEncoded.parse(body);
  }

  /**
   * Parse the specified body as if it is the body of a
   * {@code application/x-www-form-urlencoded} HTTP message.
   *
   * @param body
   *        the body of the HTTP message to parse
   *
   * @throws IOException
   *         if an I/O error occurs while reading the body
   */
  static FormUrlEncoded parse(ServerExchange http) throws IOException, UnsupportedMediaTypeException {
    return WayFormUrlEncoded.parse(http);
  }

  /**
   * Returns the first decoded value associated to the specified key or
   * {@code null} if the key is not present.
   *
   * @param key
   *        the key to search for
   *
   * @return the first decoded value or {@code null}
   */
  String get(String key);

  /**
   * Returns the first decoded value associated to the specified key or
   * the specified {@code defaultValue} if the key is not present.
   *
   * @param key
   *        the key to search for
   * @param defaultValue
   *        the value to return if the key is not present
   *
   * @return the first decoded value or the {@code defaultValue}
   */
  String getOrDefault(String key, String defaultValue);

  /**
   * Returns the number of distinct keys
   *
   * @return the number of distinct keys
   */
  int size();

}