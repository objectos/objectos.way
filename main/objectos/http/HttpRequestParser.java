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

final class HttpRequestParser {

  private final HttpSocket socket;

  HttpRequestParser(HttpSocket socket) {
    this.socket = socket;
  }

  @SuppressWarnings("unused")
  public final HttpRequest parse() throws IOException {
    final HttpMethod method;
    method = parseMethod();

    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # BEGIN: Parse: Method
  // ##################################################################

  private enum $Method {
    CONNECT,
    DELETE,
    GET,
    HEAD,
    OPTIONS,
    PATCH,
    POST,
    PUT,
    TRACE;

    final byte[] ascii;

    final HttpMethod external;

    private $Method() {
      final String name;
      name = name();

      ascii = (name + ' ').getBytes(StandardCharsets.US_ASCII);

      external = HttpMethod.valueOf(name);
    }
  }

  private HttpMethod parseMethod() throws IOException {
    final byte first;
    first = socket.readByte();

    // based on the first char, we select out method candidate

    return switch (first) {
      case 'C' -> parseMethod($Method.CONNECT, -1);

      case 'D' -> parseMethod($Method.DELETE, -1);

      case 'G' -> parseMethod($Method.GET, -1);

      case 'H' -> parseMethod($Method.HEAD, -1);

      case 'O' -> parseMethod($Method.OPTIONS, -1);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod($Method.TRACE, -1);

      default -> throw clientError(InvalidRequestLine.METHOD);
    };
  }

  private HttpMethod parseMethod($Method candidate, int offset) throws IOException {
    final byte[] ascii;
    ascii = candidate.ascii;

    if (!socket.matches(ascii, offset)) {
      throw clientError(InvalidRequestLine.METHOD);
    }

    final HttpMethod method;
    method = candidate.external;

    if (!method.implemented) {
      throw new UnsupportedOperationException("Implement me :: throw MethodNotImplemented");
    }

    return method;
  }

  private HttpMethod parseMethodP() throws IOException {
    final byte second;
    second = socket.readByte();

    return switch (second) {
      case 'O' -> parseMethod($Method.POST, -2);

      case 'U' -> parseMethod($Method.PUT, -2);

      case 'A' -> parseMethod($Method.PATCH, -2);

      default -> throw clientError(InvalidRequestLine.METHOD);
    };
  }

  // ##################################################################
  // # END: Parse: Method
  // ##################################################################

  // ##################################################################
  // # BEGIN: Errors
  // ##################################################################

  private sealed interface ClientError {
    byte[] message();

    HttpStatus status();
  }

  private enum InvalidRequestLine implements ClientError {
    // do not reorder, do not rename

    // invalid method
    METHOD,

    // path does not start with solidus
    PATH_FIRST_CHAR,

    // path starts with two consecutive '/'
    PATH_SEGMENT_NZ,

    // path has an invalid character
    PATH_NEXT_CHAR,

    // path has an invalid percent encoded sequence
    PATH_PERCENT,

    // query has an invalid character
    QUERY_CHAR,

    // query has an invalid percent encoded sequence
    QUERY_PERCENT,

    // invalid version
    VERSION_CHAR;

    private static final byte[] MESSAGE = "Invalid request line.\n".getBytes(StandardCharsets.US_ASCII);

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final HttpStatus status() {
      return HttpStatus.BAD_REQUEST;
    }
  }

  private RuntimeException clientError(ClientError value) {
    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # END: Errors
  // ##################################################################

}
