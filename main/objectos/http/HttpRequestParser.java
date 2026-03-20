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
import objectos.internal.Bytes;

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

    try {
      path = parsePath();
    } catch (DecodePercException e) {
      throw HttpClientException.of(InvalidRequestLine.PATH_PERCENT);
    }

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

  private String parsePath() throws IOException, DecodePercException {
    // where our path begins
    final int startIndex;
    startIndex = socket.bufferIndex();

    // first char must be a '/' (solidus)
    final byte first;
    first = socket.readByte();

    final char firstChar;

    final boolean firstPerc;

    if (first < 0) {
      throw HttpClientException.of(InvalidRequestLine.PATH_FIRST_CHAR);
    }

    else if (first != '%') {
      firstChar = (char) first;

      firstPerc = false;
    }

    else {
      firstChar = decodePerc();

      firstPerc = true;
    }

    if (firstChar != '/') {
      throw HttpClientException.of(InvalidRequestLine.PATH_FIRST_CHAR);
    }

    // remaining chars
    if (!firstPerc) {
      return parsePath0(startIndex);
    } else {
      final StringBuilder path;
      path = new StringBuilder("/");

      return parsePath1(path);
    }
  }

  private String parsePath0(int startIndex) throws IOException, DecodePercException {
    while (true) {
      final byte code;
      code = readTable(PARSE_PATH_TABLE, InvalidRequestLine.PATH_NEXT_CHAR);

      switch (code) {
        case PATH_VALID -> {
          // noop
        }

        case PATH_PERCENT -> {
          final StringBuilder path;
          path = makePathBuilder(startIndex);

          final char decoded;
          decoded = decodePerc();

          path.append(decoded);

          return parsePath1(path);
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

  private String parsePath1(StringBuilder path) throws IOException, DecodePercException {
    while (true) {
      final byte b;
      b = socket.readByte();

      if (b < 0) {
        throw HttpClientException.of(InvalidRequestLine.PATH_NEXT_CHAR);
      }

      final byte code;
      code = PARSE_PATH_TABLE[b];

      switch (code) {
        case PATH_VALID -> {
          path.append((char) b);
        }

        case PATH_PERCENT -> {
          final char decoded;
          decoded = decodePerc();

          path.append(decoded);
        }

        case PATH_SPACE -> {
          return validatePath(
              path.toString()
          );
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

  private StringBuilder makePathBuilder(int startIndex) {
    final String prefix;
    prefix = makePath(startIndex);

    return new StringBuilder(prefix);
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
  // # BEGIN: URL Decode
  // ##################################################################

  @SuppressWarnings("serial")
  private final class DecodePercException extends Exception {}

  private char decodePerc() throws IOException, DecodePercException {
    final byte high1;
    high1 = readPerc();

    return switch (high1) {
      // 0yyyzzzz
      case 0b0000, 0b0001,
           0b0010, 0b0011,
           0b0100, 0b0101, 0b0110, 0b0111 -> decodePerc1(high1);

      // 110xxxyy 10yyzzzz
      case 0b1100, 0b1101 -> decodePerc2(high1);

      // 1110wwww 10xxxxyy 10yyzzzz
      case 0b1110 -> decodePerc3(high1);

      // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
      case 0b1111 -> decodePerc4(high1);

      default -> throw new DecodePercException();
    };
  }

  private char decodePerc1(byte high1) throws IOException {
    final byte low1;
    low1 = readPerc();

    return (char) decodePerc(high1, low1);
  }

  private char decodePerc2(byte high1) throws IOException, DecodePercException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    readPercSep();

    final byte high2;
    high2 = readPerc();

    final byte low2;
    low2 = readPerc();

    final int perc2;
    perc2 = decodePerc(high2, low2);

    if (!utf8Byte(perc2)) {
      throw new DecodePercException();
    }

    final int c;
    c = (perc1 & 0b1_1111) << 6 | (perc2 & 0b11_1111);

    if (c < 0x80 || c > 0x7FF) {
      throw new DecodePercException();
    }

    return (char) c;
  }

  private char decodePerc3(byte high1) throws IOException, DecodePercException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    readPercSep();

    final byte high2;
    high2 = readPerc();

    final byte low2;
    low2 = readPerc();

    final int perc2;
    perc2 = decodePerc(high2, low2);

    if (!utf8Byte(perc2)) {
      throw new DecodePercException();
    }

    readPercSep();

    final byte high3;
    high3 = readPerc();

    final byte low3;
    low3 = readPerc();

    final int perc3;
    perc3 = decodePerc(high3, low3);

    if (!utf8Byte(perc3)) {
      throw new DecodePercException();
    }

    final int c;
    c = (perc1 & 0b1111) << 12 | (perc2 & 0b11_1111) << 6 | (perc3 & 0b11_1111);

    if (c < 0x800 || c > 0xFFFF || Character.isSurrogate((char) c)) {
      throw new DecodePercException();
    }

    return (char) c;
  }

  private char decodePerc4(byte high1) throws IOException, DecodePercException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    readPercSep();

    final byte high2;
    high2 = readPerc();

    final byte low2;
    low2 = readPerc();

    final int perc2;
    perc2 = decodePerc(high2, low2);

    if (!utf8Byte(perc2)) {
      throw new DecodePercException();
    }

    readPercSep();

    final byte high3;
    high3 = readPerc();

    final byte low3;
    low3 = readPerc();

    final int perc3;
    perc3 = decodePerc(high3, low3);

    if (!utf8Byte(perc3)) {
      throw new DecodePercException();
    }

    final byte high4;
    high4 = readPerc();

    final byte low4;
    low4 = readPerc();

    final int perc4;
    perc4 = decodePerc(high4, low4);

    if (!utf8Byte(perc4)) {
      throw new DecodePercException();
    }

    final int c;
    c = (perc1 & 0b111) << 18 | (perc2 & 0b11_1111) << 12 | (perc3 & 0b11_1111) << 6 | (perc4 & 0b11_1111);

    if (c < 0x1_0000 || !Character.isValidCodePoint(c)) {
      throw new DecodePercException();
    }

    return (char) c;
  }

  private int decodePerc(byte high, byte low) {
    return (high << 4) | low;
  }

  private byte readPerc() throws IOException {
    final byte b;
    b = socket.readByte();

    final byte perc;
    perc = Bytes.fromHexDigit(b);

    if (perc < 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    return perc;
  }

  private void readPercSep() throws IOException {
    final byte b;
    b = socket.readByte();

    if (b != '%') {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private boolean utf8Byte(int utf8) {
    final int topTwoBits;
    topTwoBits = utf8 & 0b1100_0000;

    return topTwoBits == 0b1000_0000;
  }

  // ##################################################################
  // # END: URL Decode
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

  private byte readTable(byte[] table, HttpClientException.Kind kind) throws IOException {
    final byte next;
    next = socket.readByte();

    if (next < 0) {
      throw HttpClientException.of(kind);
    }

    return table[next];
  }

  // ##################################################################
  // # END: Util
  // ##################################################################

}
