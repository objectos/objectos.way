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
import objectos.http.HttpClientException.Kind;
import objectos.internal.Ascii;

final class HttpRequestParser3Path {

  private boolean done;

  private int dot;

  private final HttpRequestParser0Input input;

  private final StringBuilder path = new StringBuilder();

  private int solidus;

  private HttpRequestParser1UrlDecoder urlDecoder;

  HttpRequestParser3Path(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final String parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof e) {
      final String msg;
      msg = "EOF while parsing path";

      throw new HttpClientException(msg, e, Kind.INVALID_REQUEST_LINE);
    } catch (HttpRequestParser0Input.Overflow e) {
      final String msg;
      msg = "Buffer overflow while parsing path";

      throw new HttpClientException(msg, e, Kind.URI_TOO_LONG);
    }
  }

  private String parse0() throws IOException {
    // first char
    parseChar();

    if (path.isEmpty()) {
      final String msg;
      msg = "Invalid path: it must not be empty";

      throw new HttpClientException(msg, Kind.INVALID_REQUEST_LINE);
    }

    final char first;
    first = path.charAt(0);

    if (first != '/') {
      final String msg;
      msg = "Invalid path: only absolute paths are supported";

      throw new HttpClientException(msg, Kind.INVALID_REQUEST_LINE);
    }

    if (done) {
      return path.toString();
    }

    // second char
    parseChar();

    if (done) {
      return path.toString();
    }

    final char second;
    second = path.charAt(1);

    if (second == '/') {
      final String msg;
      msg = "Invalid path: first segment must not be empty";

      throw new HttpClientException(msg, Kind.INVALID_REQUEST_LINE);
    }

    // remaining
    do {
      parseChar();
    } while (!done);

    return path.toString();
  }

  private static final byte[] PATH_TABLE;

  private static final byte PATH_VALID = 1;
  private static final byte PATH_SOLIDUS = 2;
  private static final byte PATH_DOT = 3;
  private static final byte PATH_PERCENT = 4;
  private static final byte PATH_SPACE = 5;
  private static final byte PATH_QUESTION = 6;

  static {
    final byte[] table;
    table = new byte[128];

    Ascii.fill(table, Http.unreserved(), PATH_VALID);

    Ascii.fill(table, Http.subDelims(), PATH_VALID);

    table[':'] = PATH_VALID;

    table['@'] = PATH_VALID;

    table['/'] = PATH_SOLIDUS;

    table['.'] = PATH_DOT;

    table['%'] = PATH_PERCENT;

    table[' '] = PATH_SPACE;

    table['?'] = PATH_QUESTION;

    PATH_TABLE = table;
  }

  private void parseChar() throws IOException {
    final byte b;
    b = input.readByte(Kind.INVALID_REQUEST_LINE);

    final byte code;
    code = PATH_TABLE[b];

    switch (code) {
      case PATH_VALID -> {
        dot = 0;

        final char c;
        c = (char) b;

        path.append(c);
      }

      case PATH_SOLIDUS -> {
        checkDots();

        dot = 0;

        solidus = path.length();

        path.append('/');
      }

      case PATH_DOT -> {
        path.append('.');

        dot += 1;
      }

      case PATH_PERCENT -> {
        final int decoded;
        decoded = decodePerc();

        if (decoded == '/') {
          checkDots();

          dot = 0;

          solidus = path.length();

          path.append('/');
        } else {
          path.appendCodePoint(decoded);
        }
      }

      case PATH_SPACE, PATH_QUESTION -> {
        checkDots();

        done = true;
      }

      default -> {
        final String msg;
        msg = "Unexpected byte 0x%02X while parsing path".formatted(b);

        throw new HttpClientException(msg, Kind.INVALID_REQUEST_LINE);
      }
    }
  }

  private void checkDots() throws HttpClientException {
    final int startIndex;
    startIndex = solidus + 1;

    final int len;
    len = path.length() - startIndex;

    if ((dot == 1 && len == dot) ||
        (dot == 2 && len == dot)) {
      final String msg;
      msg = "Invalid path: dot-segments are not allowed";

      throw new HttpClientException(msg, Kind.INVALID_REQUEST_LINE);
    }
  }

  private int decodePerc() throws IOException {
    if (urlDecoder == null) {
      urlDecoder = new HttpRequestParser1UrlDecoder(input);
    }

    return urlDecoder.decode(Kind.INVALID_REQUEST_LINE);
  }

}
