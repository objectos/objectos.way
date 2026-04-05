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
import objectos.http.HttpRequestParser1UrlDecoder.DecodeException;
import objectos.internal.Ascii;

final class HttpRequestParser3Path {

  enum Invalid implements HttpClientException.Kind {
    // do not reorder, do not rename

    // 414 URI Too Long
    URI_TOO_LONG(HttpStatus.URI_TOO_LONG),

    // path does not start with solidus
    PATH_FIRST_CHAR,

    // path starts with two consecutive '/'
    PATH_SEGMENT_NZ,

    // path has an invalid character
    PATH_NEXT_CHAR,

    // path has an invalid percent encoded sequence
    PATH_PERCENT,

    // 505 HTTP Version Not Supported
    HTTP_VERSION_NOT_SUPPORTED(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);

    private final HttpStatus status;

    private Invalid() {
      this(HttpStatus.BAD_REQUEST);
    }

    private Invalid(HttpStatus status) {
      this.status = status;
    }

    @Override
    public final String message() {
      return "Invalid request line.\n";
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

  private final HttpRequestParser0Input input;

  private HttpRequestParser1UrlDecoder urlDecoder;

  HttpRequestParser3Path(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final String parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser1UrlDecoder.DecodeException e) {
      throw HttpClientException.of(Invalid.PATH_PERCENT, e);
    } catch (HttpRequestParser0Input.Eof e) {
      throw HttpClientException.of(Invalid.PATH_NEXT_CHAR, e);
    } catch (HttpRequestParser0Input.Overflow e) {
      throw HttpClientException.of(Invalid.URI_TOO_LONG, e);
    }
  }

  private String parse0() throws DecodeException, IOException {
    // where our path begins
    input.mark();

    // first char must be a '/' (solidus)
    final byte first;
    first = input.readByte();

    final int firstCodePoint;

    final boolean firstPerc;

    if (first < 0) {
      throw HttpClientException.of(Invalid.PATH_FIRST_CHAR);
    }

    else if (first != '%') {
      firstCodePoint = first;

      firstPerc = false;
    }

    else {
      firstCodePoint = decodePerc();

      firstPerc = true;
    }

    if (firstCodePoint != '/') {
      throw HttpClientException.of(Invalid.PATH_FIRST_CHAR);
    }

    final String result;

    // remaining chars
    if (!firstPerc) {
      result = parse1();
    } else {
      final StringBuilder path;
      path = new StringBuilder("/");

      result = parse2(path);
    }

    final int length;
    length = result.length();

    if (length >= 2) {
      final char second;
      second = result.charAt(1);

      if (second == '/') {
        throw HttpClientException.of(Invalid.PATH_SEGMENT_NZ);
      }
    }

    return result;
  }

  private static final byte[] PATH_TABLE;

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

    PATH_TABLE = table;
  }

  private String parse1() throws DecodeException, IOException {
    while (true) {
      final byte code;
      code = input.readTable(PATH_TABLE, Invalid.PATH_NEXT_CHAR);

      switch (code) {
        case PATH_VALID -> {
          // noop
        }

        case PATH_PERCENT -> {
          final StringBuilder path;
          path = input.makeStrBuilder();

          final int decoded;
          decoded = decodePerc();

          path.appendCodePoint(decoded);

          return parse2(path);
        }

        case PATH_SPACE, PATH_QUESTION -> {
          return input.makeStr();
        }

        case PATH_CRLF -> {
          // assume version 0.9
          throw HttpClientException.of(Invalid.HTTP_VERSION_NOT_SUPPORTED);
        }

        default -> throw HttpClientException.of(Invalid.PATH_NEXT_CHAR);
      }
    }
  }

  private String parse2(StringBuilder path) throws DecodeException, IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      if (b < 0) {
        throw HttpClientException.of(Invalid.PATH_NEXT_CHAR);
      }

      final byte code;
      code = PATH_TABLE[b];

      switch (code) {
        case PATH_VALID -> {
          path.append((char) b);
        }

        case PATH_PERCENT -> {
          final int decoded;
          decoded = decodePerc();

          path.appendCodePoint(decoded);
        }

        case PATH_SPACE, PATH_QUESTION -> {
          return path.toString();
        }

        case PATH_CRLF -> {
          // assume version 0.9
          throw HttpClientException.of(Invalid.HTTP_VERSION_NOT_SUPPORTED);
        }

        default -> throw HttpClientException.of(Invalid.PATH_NEXT_CHAR);
      }
    }
  }

  private int decodePerc() throws DecodeException, IOException {
    if (urlDecoder == null) {
      urlDecoder = new HttpRequestParser1UrlDecoder(input);
    }

    return urlDecoder.decode();
  }

}
