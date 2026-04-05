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

final class HttpRequestParser2Method {

  enum Invalid implements HttpClientException.Kind {

    METHOD;

    @Override
    public final String message() {
      return "Invalid request line.\n";
    }

    @Override
    public final HttpStatus status() {
      return HttpStatus.BAD_REQUEST;
    }

  }

  private final HttpRequestParser0Input input;

  HttpRequestParser2Method(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final HttpMethod parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof | HttpRequestParser0Input.Overflow e) {
      throw HttpClientException.of(Invalid.METHOD, e);
    }
  }

  private HttpMethod parse0() throws IOException {
    final byte first;
    first = input.readByte();

    // based on the first char, we select out method candidate

    return switch (first) {
      case 'C' -> parseMethod(HttpMethod.CONNECT, 1);

      case 'D' -> parseMethod(HttpMethod.DELETE, 1);

      case 'G' -> parseMethod(HttpMethod.GET, 1);

      case 'H' -> parseMethod(HttpMethod.HEAD, 1);

      case 'O' -> parseMethod(HttpMethod.OPTIONS, 1);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod(HttpMethod.TRACE, 1);

      default -> throw HttpClientException.of(Invalid.METHOD);
    };
  }

  private HttpMethod parseMethod(HttpMethod method, int offset) throws IOException {
    final byte[] ascii;
    ascii = method.ascii;

    for (int idx = offset; idx < ascii.length; idx++) {
      final byte b;
      b = input.readByte();

      if (ascii[idx] == b) {
        continue;
      }

      throw HttpClientException.of(Invalid.METHOD);
    }

    return method;
  }

  private HttpMethod parseMethodP() throws IOException {
    final byte second;
    second = input.readByte();

    return switch (second) {
      case 'O' -> parseMethod(HttpMethod.POST, 2);

      case 'U' -> parseMethod(HttpMethod.PUT, 2);

      case 'A' -> parseMethod(HttpMethod.PATCH, 2);

      default -> throw HttpClientException.of(Invalid.METHOD);
    };
  }

}
