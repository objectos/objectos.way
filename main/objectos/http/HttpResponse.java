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

import java.nio.file.Path;
import java.util.function.Consumer;
import objectos.way.Media;

/**
 * Represents an HTTP response message.
 */
public sealed interface HttpResponse permits HttpExchangeImpl.ResponseHandle {

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

  /**
   * Ends this HTTP response message with an empty body.
   */
  void body();

  /**
   * Ends this HTTP response message with the specified body.
   */
  void body(byte[] bytes, int offset, int length);

  /**
   * Ends this HTTP response message with the specified body.
   */
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