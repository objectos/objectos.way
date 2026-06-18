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
package objectox.http;

import java.io.IOException;
import objectos.http.Status;

@SuppressWarnings("serial")
public final class HttpClientException extends IOException implements objectox.http.srv.ServerTaskMessage {

  public enum Kind {

    INVALID_REQUEST_LINE(Status.BAD_REQUEST, "Invalid request line.\n"),

    URI_TOO_LONG(Status.URI_TOO_LONG, "Invalid request line.\n"),

    INVALID_REQUEST_HEADERS(Status.BAD_REQUEST, "Invalid request headers.\n"),

    REQUEST_HEADER_FIELDS_TOO_LARGE(Status.REQUEST_HEADER_FIELDS_TOO_LARGE, "Invalid request headers.\n"),

    HOST_HEADER(Status.BAD_REQUEST, "Host header.\n"),

    HOST_NOT_FOUND(Status.NOT_FOUND, "Host not found.\n"),

    LINE_TERMINATOR(Status.BAD_REQUEST, "Invalid line terminator.\n"),

    CONTENT_TOO_LARGE(Status.CONTENT_TOO_LARGE, "The request message body exceeds the server's maximum allowed limit.\n"),

    LENGTH_REQUIRED(Status.LENGTH_REQUIRED, "Invalid request headers.\n"),

    INCOMPLETE_REQUEST_BODY(Status.BAD_REQUEST, "Incomplete request body.\n"),

    INVALID_FORM(Status.BAD_REQUEST, "Invalid application/x-www-form-urlencoded content in request body.\n");

    private final Status status;

    private final String message;

    private Kind(Status status, String message) {
      this.status = status;

      this.message = message;
    }

  }

  public final Kind kind;

  public HttpClientException(String message, Kind kind) {
    super(message);

    this.kind = kind;
  }

  public HttpClientException(String message, Throwable cause, Kind kind) {
    super(message, cause);

    this.kind = kind;
  }

  @Override
  public final Status status() {
    return kind.status;
  }

  @Override
  public final String message() {
    return kind.message;
  }

}
