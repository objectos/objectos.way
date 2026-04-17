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

@SuppressWarnings("serial")
final class HttpClientException extends IOException implements HttpServerTask.Message {

  enum Kind {

    INVALID_REQUEST_LINE(HttpStatus.BAD_REQUEST, "Invalid request line.\n"),

    URI_TOO_LONG(HttpStatus.URI_TOO_LONG, "Invalid request line.\n"),

    INVALID_REQUEST_HEADERS(HttpStatus.BAD_REQUEST, "Invalid request headers.\n"),

    REQUEST_HEADER_FIELDS_TOO_LARGE(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE, "Invalid request headers.\n"),

    HOST_HEADER(HttpStatus.BAD_REQUEST, "Host header.\n"),

    HOST_NOT_FOUND(HttpStatus.NOT_FOUND, "Host not found.\n"),

    LINE_TERMINATOR(HttpStatus.BAD_REQUEST, "Invalid line terminator.\n"),

    CONTENT_TOO_LARGE(HttpStatus.CONTENT_TOO_LARGE, "The request message body exceeds the server's maximum allowed limit.\n"),

    LENGTH_REQUIRED(HttpStatus.LENGTH_REQUIRED, "Invalid request headers.\n"),

    INCOMPLETE_REQUEST_BODY(HttpStatus.BAD_REQUEST, "Incomplete request body.\n"),

    INVALID_FORM(HttpStatus.BAD_REQUEST, "Invalid application/x-www-form-urlencoded content in request body.\n");

    private final HttpStatus status;

    private final String message;

    private Kind(HttpStatus status, String message) {
      this.status = status;

      this.message = message;
    }

  }

  final Kind kind;

  HttpClientException(String message, Kind kind) {
    super(message);

    this.kind = kind;
  }

  HttpClientException(String message, Throwable cause, Kind kind) {
    super(message, cause);

    this.kind = kind;
  }

  @Override
  public final HttpStatus status() {
    return kind.status;
  }

  @Override
  public final String message() {
    return kind.message;
  }

}
