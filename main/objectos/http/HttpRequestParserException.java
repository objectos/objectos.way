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
final class HttpRequestParserException extends IOException {

  enum Kind {

    INVALID_REQUEST_LINE,

    LINE_TERMINATOR,

    URI_TOO_LONG;

  }

  final Kind kind;

  HttpRequestParserException(String message, Kind kind) {
    super(message);

    this.kind = kind;
  }

  HttpRequestParserException(String message, Throwable cause, Kind kind) {
    super(message, cause);

    this.kind = kind;
  }

}
