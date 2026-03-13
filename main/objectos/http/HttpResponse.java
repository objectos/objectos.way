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

import module java.base;
import module objectos.way;

/// Represents an HTTP response message.
public sealed interface HttpResponse permits HttpResponseImpl {

  // high-level

  // 2xx responses

  /// Respond with a `200 OK` message with the specified media entity.
  ///
  /// @param media the media entity
  void ok(Media.Bytes media);

  /// Respond with a `200 OK` message with the specified media entity.
  ///
  /// @param media the media entity
  void ok(Media.Stream media);

  /// Respond with a `200 OK` message with the specified media entity.
  ///
  /// @param media the media entity
  void ok(Media.Text media);

  // 3xx responses

  /// Respond with a `301 Moved Permanently` message with the specified
  /// `Location` header.
  ///
  /// @param location the value of the `Location` header
  void movedPermanently(String location);

  /// Respond with a `302 Found` message with the specified `Location` header.
  ///
  /// @param location the value of the `Location` header
  void found(String location);

  /// Respond with a `See Other` message with the specified `Location` header.
  ///
  /// @param location the value of the `Location` header
  void seeOther(String location);

  // 4xx responses

  /// Respond with a `400 Bad Request` message with the specified media entity.
  ///
  /// @param media the media entity
  void badRequest(Media media);

  /// Respond with a `403 Forbidden` message with the specified media entity.
  ///
  /// @param media the media entity
  void forbidden(Media media);

  /// Respond with a `404 Not Found` message with the specified media entity.
  ///
  /// @param media the media entity
  void notFound(Media media);

  /// Respond with a `405 Method Not Allowed` message with the specified methods
  /// in the `Allow` response header.
  ///
  /// @param methods the allowed methods
  void allow(HttpMethod... methods);

  // 5xx responses

  /// Respond with a `500 Internal Server Error` message with the specified
  /// media entity. The specified `Throwable` will be noted.
  ///
  /// @param media the media entity
  /// @param error the `Throwable` to be noted
  void internalServerError(Media media, Throwable error);

  // low-level

  /**
   * Begins this HTTP response message by writing out the status line.
   *
   * @param value
   *        the response status
   */
  void status(HttpStatus value);

  /**
   * Writes an HTTP response header field with the specified name and
   * value.
   *
   * @param name
   *        the header name
   * @param value
   *        the header value
   */
  void header(HttpHeaderName name, long value);

  /**
   * Writes an HTTP response header field with the specified name and
   * value.
   *
   * @param name
   *        the header name
   * @param value
   *        the header value
   */
  void header(HttpHeaderName name, String value);

  /**
   * Writes an HTTP response header field with the specified name and
   * value.
   *
   * <p>
   * Example usage:
   * <pre>{@code
   * response.header(Http.HeaderName.CONTENT_DISPOSITION, builder -> {
   *   builder.value("attachment");
   *   builder.param("filename", "document.pdf");
   *   builder.param("filename*", StandardCharsets.UTF_8, "document.pdf");
   * });
   * }</pre>
   *
   * <p>
   * Which would result in the following header field written out to the
   * response:
   *
   * <pre>{@code
   * Content-Disposition: attachment; filename=document.pdf; filename*=UTF-8''document.pdf
   * }</pre>
   *
   * @param name
   *        the header name
   * @param builder
   *        a handle for creating the header field value
   */
  void header(HttpHeaderName name, Consumer<? super HttpHeaderValueBuilder> builder);

  /**
   * Returns the server's current time.
   *
   * @return the RFC-5322 formatted server time
   */
  String now();

  /// Ends this HTTP response message with an empty body.
  void body();

  /// Ends this HTTP response message with the specified body.
  ///
  /// @param bytes the array of bytes with the body contents
  /// @param offset index where the actual message begins
  /// @param length the message length in bytes
  void body(byte[] bytes, int offset, int length);

  /// Ends this HTTP response message with the specified body.
  ///
  /// @param file the path to a regular file containing the body contents
  void body(Path file);

  /**
   * Writes the required response headers for the specified media and ends
   * this HTTP response message with the contents from the specified media.
   *
   * @param media
   *        the media entity
   */
  void media(Media media);

}