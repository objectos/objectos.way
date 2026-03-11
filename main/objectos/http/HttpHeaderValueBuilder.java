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

import java.nio.charset.Charset;

/**
 * Configures the creation of a header value.
 */
public sealed interface HttpHeaderValueBuilder permits HttpExchangeImpl.HttpHeaderValueBuilderImpl {

  /**
   * Begins the next value.
   *
   * @param value
   *        the header value
   */
  void value(String value);

  /**
   * Appends a parameter to the current header value with the specified
   * name and value.
   *
   * @param name
   *        the parameter name
   * @param value
   *        the parameter value
   *
   * @throws IllegalArgumentException
   *         if either the name or the value contains invalid characters
   */
  void param(String name, String value);

  /**
   * Appends a parameter to the current header value with the specified
   * name, charset and value.
   *
   * @param name
   *        the parameter name
   * @param charset
   *        the charset to use when encoding the value
   * @param value
   *        the parameter value
   *
   * @throws IllegalArgumentException
   *         if the name contains invalid characters
   */
  void param(String name, Charset charset, String value);

}