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

final class HttpClientException extends IOException {

  private static final long serialVersionUID = -3212371438120698449L;

  sealed interface Kind
      permits
      HttpRequestParser.InvalidLineTerminator,
      HttpRequestParser.InvalidRequestLine,
      HttpRequestParser.InvalidRequestHeaders {

    byte[] message();

    HttpStatus status();

  }

  final Kind kind;

  private HttpClientException(Kind kind) {
    this.kind = kind;
  }

  private HttpClientException(Kind kind, Throwable cause) {
    super(cause);

    this.kind = kind;
  }

  public static HttpClientException of(Kind kind) {
    return new HttpClientException(kind);
  }

  public static HttpClientException of(Kind kind, Throwable cause) {
    return new HttpClientException(kind, cause);
  }

}
