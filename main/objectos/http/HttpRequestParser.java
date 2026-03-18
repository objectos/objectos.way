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
import objectos.internal.Ascii;

final class HttpRequestParser {

  private final HttpSocket socket;

  HttpRequestParser(HttpSocket socket) {
    this.socket = socket;
  }

  @SuppressWarnings("unused")
  public final HttpRequest parse() throws IOException {
    final HttpMethod method;
    method = parseMethod();

    final String path;
    path = parsePath();

    return new HttpRequestImpl(method, path);
  }

  // ##################################################################
  // # BEGIN: Method
  // ##################################################################

  private HttpMethod parseMethod() throws IOException {
    final byte first;
    first = socket.readByte();

    // based on the first char, we select out method candidate

    return switch (first) {
      case 'C' -> parseMethod(HttpMethod.CONNECT, 1);

      case 'D' -> parseMethod(HttpMethod.DELETE, 1);

      case 'G' -> parseMethod(HttpMethod.GET, 1);

      case 'H' -> parseMethod(HttpMethod.HEAD, 1);

      case 'O' -> parseMethod(HttpMethod.OPTIONS, 1);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod(HttpMethod.TRACE, 1);

      default -> throw HttpClientException.of(InvalidRequestLine.METHOD);
    };
  }

  private HttpMethod parseMethod(HttpMethod method, int offset) throws IOException {
    final byte[] ascii;
    ascii = method.ascii;

    if (!socket.matches(ascii, offset)) {
      throw HttpClientException.of(InvalidRequestLine.METHOD);
    }

    if (!method.implemented) {
      throw HttpServerException.methodNotImplemented();
    }

    return method;
  }

  private HttpMethod parseMethodP() throws IOException {
    final byte second;
    second = socket.readByte();

    return switch (second) {
      case 'O' -> parseMethod(HttpMethod.POST, 2);

      case 'U' -> parseMethod(HttpMethod.PUT, 2);

      case 'A' -> parseMethod(HttpMethod.PATCH, 2);

      default -> throw HttpClientException.of(InvalidRequestLine.METHOD);
    };
  }

  // ##################################################################
  // # END: Method
  // ##################################################################

  // ##################################################################
  // # BEGIN: Path
  // ##################################################################

  private static final byte[] PARSE_PATH_TABLE;

  private static final byte SOLIDUS = '/';

  private static final byte PATH_INVALID = -1;
  private static final byte PATH_VALID = 1;
  private static final byte PATH_PERCENT = 2;
  private static final byte PATH_SPACE = 3;
  private static final byte PATH_QUESTION = 4;
  private static final byte PATH_CRLF = 5;

  static {
    final byte[] table;
    table = new byte[128];

    // 0 = invalid
    // 1 = valid
    // 2 = %xx
    // 3 = ' ' -> version
    // 4 = '?' -> stop
    // 5 = '\r' -> 0.9
    // 5 = '\n' -> 0.9

    Ascii.fill(table, Http.unreserved(), PATH_VALID);

    Ascii.fill(table, Http.subDelims(), PATH_VALID);

    table[':'] = PATH_VALID;

    table['@'] = PATH_VALID;

    // solidus acts as segment separator
    table[SOLIDUS] = PATH_VALID;

    table['%'] = PATH_PERCENT;

    table[' '] = PATH_SPACE;

    table['?'] = PATH_QUESTION;

    table['\r'] = PATH_CRLF;

    table['\n'] = PATH_CRLF;

    PARSE_PATH_TABLE = table;
  }

  private String parsePath() throws IOException {
    final int startIndex;
    startIndex = socket.bufferIndex();

    loop: while (true) {
      final byte code;
      code = readTable(PARSE_PATH_TABLE, PATH_INVALID);

      switch (code) {
        case PATH_VALID -> {
          continue loop;
        }

        case PATH_SPACE -> {
          final String path;
          path = makePath(startIndex);

          return validatePath(path);
        }

        default -> throw HttpClientException.of(InvalidRequestLine.PATH_NEXT_CHAR);
      }
    }
  }

  private String makePath(int startIndex) {
    final int bufferIndex;
    bufferIndex = socket.bufferIndex();

    final int endIndex;
    endIndex = bufferIndex - 1;

    return socket.bufferToAscii(startIndex, endIndex);
  }

  private String validatePath(String path) throws HttpClientException {
    final int length;
    length = path.length();

    if (length == 0) {
      throw HttpClientException.of(InvalidRequestLine.PATH_FIRST_CHAR);
    }

    final char first;
    first = path.charAt(0);

    if (first != '/') {
      throw HttpClientException.of(InvalidRequestLine.PATH_FIRST_CHAR);
    }

    if (length == 1) {
      return path;
    }

    final char second;
    second = path.charAt(1);

    if (second == '/') {
      throw HttpClientException.of(InvalidRequestLine.PATH_SEGMENT_NZ);
    }

    return path;
  }

  // ##################################################################
  // # END: Path
  // ##################################################################

  // ##################################################################
  // # BEGIN: Errors
  // ##################################################################

  enum InvalidRequestLine implements HttpClientException.Kind {
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

  // ##################################################################
  // # END: Errors
  // ##################################################################

  // ##################################################################
  // # BEGIN: Util
  // ##################################################################

  private byte readTable(byte[] table, byte invalid) throws IOException {
    final byte next;
    next = socket.readByte();

    if (next < 0) {
      return invalid;
    } else {
      return table[next];
    }
  }

  // ##################################################################
  // # END: Util
  // ##################################################################

}
