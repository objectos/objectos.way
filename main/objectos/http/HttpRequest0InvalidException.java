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

import objectos.way.Media;

@SuppressWarnings("serial")
final class HttpRequest0InvalidException extends Exception {

  enum Kind {

    METHOD_NOT_IMPLEMENTED,

    HTTP_VERSION_NOT_SUPPORTED,

    HOST_HEADER;

  }

  private final Kind kind;

  HttpRequest0InvalidException(Kind kind) {
    this.kind = kind;
  }

  public final void respond(HttpResponse0 r) {
    switch (kind) {
      case METHOD_NOT_IMPLEMENTED -> {
        r.status(HttpStatus.NOT_IMPLEMENTED);

        r.header(HttpHeaderName.DATE, r.now());

        final Media.Bytes message;
        message = Media.Bytes.textPlain("The requested method is not implemented by this server.\n");

        r.send(message);
      }

      case HTTP_VERSION_NOT_SUPPORTED -> {
        r.status(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);

        r.header(HttpHeaderName.DATE, r.now());

        r.header(HttpHeaderName.CONNECTION, "close");

        final Media.Bytes message;
        message = Media.Bytes.textPlain("Supported versions: HTTP/1.1\n");

        r.send(message);
      }

      case HOST_HEADER -> {
        r.status(HttpStatus.BAD_REQUEST);

        r.header(HttpHeaderName.DATE, r.now());

        r.header(HttpHeaderName.CONNECTION, "close");

        final Media.Bytes message;
        message = Media.Bytes.textPlain("Host header.\n");

        r.send(message);
      }
    }
  }

}
