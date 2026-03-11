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

/**
 * Listens to the response defined in a HTTP exchange.
 */
public interface HttpResponseListener {

  /**
   * Invoked when the response status line is set.
   *
   * @param version
   *        the HTTP version
   * @param status
   *        the response status
   */
  default void status(HttpVersion version, HttpStatus status) {}

  /**
   * Invoked when a response header is set.
   *
   * @param name
   *        the header name
   * @param value
   *        the header value
   */
  default void header(HttpHeaderName name, String value) {}

  /**
   * Invoked when a response body is set; if the response has no body this
   * method is invoked with {@code null}.
   *
   * @param body
   *        the response body; or {@code null} if the response has no body
   */
  default void body(Object body) {}

}