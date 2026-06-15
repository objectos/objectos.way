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
package objectox.http.req;

import module java.base;
import objectox.http.HttpClientException;
import objectox.http.RequestMethodEnum;
import objectox.http.HttpClientException.Kind;

final class RequestParser2Method {

  private final RequestParser0Input input;

  RequestParser2Method(RequestParser0Input input) {
    this.input = input;
  }

  public final RequestMethodEnum parse() throws IOException {
    try {
      return parse0();
    } catch (RequestParser0Input.Eof e) {
      final String msg;
      msg = "EOF while parsing method";

      throw new HttpClientException(msg, e, Kind.INVALID_REQUEST_LINE);
    } catch (RequestParser0Input.Overflow e) {
      final String msg;
      msg = "Buffer overflow while parsing method";

      throw new HttpClientException(msg, e, Kind.INVALID_REQUEST_LINE);
    }
  }

  private RequestMethodEnum parse0() throws IOException {
    final byte first;
    first = input.readByte();

    return switch (first) {
      case 'C' -> parseMethod(RequestMethodEnum.CONNECT, 1);

      case 'D' -> parseMethod(RequestMethodEnum.DELETE, 1);

      case 'G' -> parseMethod(RequestMethodEnum.GET, 1);

      case 'H' -> parseMethod(RequestMethodEnum.HEAD, 1);

      case 'O' -> parseMethod(RequestMethodEnum.OPTIONS, 1);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod(RequestMethodEnum.TRACE, 1);

      default -> {
        final String msg;
        msg = "Unexpected byte 0x%02X while parsing method first char".formatted(first);

        throw new HttpClientException(msg, Kind.INVALID_REQUEST_LINE);
      }
    };
  }

  private RequestMethodEnum parseMethod(RequestMethodEnum method, int offset) throws IOException {
    final byte[] ascii;
    ascii = method.ascii;

    for (int idx = offset; idx < ascii.length; idx++) {
      final byte b;
      b = input.readByte();

      if (ascii[idx] == b) {
        continue;
      }

      final String msg;
      msg = "Unexpected byte 0x%02X while parsing method %s".formatted(b, method);

      throw new HttpClientException(msg, Kind.INVALID_REQUEST_LINE);
    }

    return method;
  }

  private RequestMethodEnum parseMethodP() throws IOException {
    final byte second;
    second = input.readByte();

    return switch (second) {
      case 'O' -> parseMethod(RequestMethodEnum.POST, 2);

      case 'U' -> parseMethod(RequestMethodEnum.PUT, 2);

      case 'A' -> parseMethod(RequestMethodEnum.PATCH, 2);

      default -> {
        final String msg;
        msg = "Unexpected byte 0x%02X while parsing POST/PUT/PATCH".formatted(second);

        throw new HttpClientException(msg, Kind.INVALID_REQUEST_LINE);
      }
    };
  }

}
