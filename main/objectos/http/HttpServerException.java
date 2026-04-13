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
final class HttpServerException extends IOException implements HttpServerTask.Message {

  enum Kind {

    METHOD_NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "The requested method is not implemented by this server.\n"),

    HTTP_VERSION_NOT_SUPPORTED(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "Supported versions: HTTP/1.1\n"),

    TRANSFER_ENCODING(HttpStatus.NOT_IMPLEMENTED, "Support for the request Transfer-Encoding header is not implemented.\n"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "The requested HTTP method is not implemented.\n");

    private final HttpStatus status;

    private final String message;

    private Kind(HttpStatus status, String message) {
      this.status = status;

      this.message = message;
    }

  }

  final Kind kind;

  HttpServerException(Kind kind) {
    this.kind = kind;
  }

  HttpServerException(Throwable cause, Kind kind) {
    super(cause);

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
