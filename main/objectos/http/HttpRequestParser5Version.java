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
import java.nio.charset.StandardCharsets;
import objectos.http.HttpRequestParserException.Kind;
import objectos.internal.Bytes;

final class HttpRequestParser5Version {

  private boolean done;

  private final HttpRequestParser0Input input;

  HttpRequestParser5Version(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final HttpVersion0 parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof e) {
      final String msg;
      msg = "EOF while parsing HTTP version";

      throw new HttpRequestParserException(msg, e, Kind.INVALID_REQUEST_LINE);
    } catch (HttpRequestParser0Input.Overflow e) {
      final String msg;
      msg = "Buffer overflow while parsing HTTP version";

      throw new HttpRequestParserException(msg, e, Kind.URI_TOO_LONG);
    }
  }

  private HttpVersion0 parse0() throws IOException {
    parseHttp();

    final int major;
    major = parseMajor();

    final int minor;
    minor = done ? 0 : parseMinor();

    if (major == 1 && minor == 1) {
      return HttpVersion0.HTTP_1_1;
    } else {
      return HttpVersion0.of(major, minor);
    }
  }

  private static final byte[] HTTP = "HTTP/".getBytes(StandardCharsets.US_ASCII);

  private void parseHttp() throws IOException {
    for (int idx = 0; idx < HTTP.length; idx++) {
      final byte b;
      b = input.readByte(Kind.INVALID_REQUEST_LINE);

      final byte expected;
      expected = HTTP[idx];

      if (b == expected) {
        continue;
      }

      final String msg;
      msg = "Invalid HTTP version: expected byte 0x%02X but got 0x%02X".formatted(expected, b);

      throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_LINE);
    }
  }

  private int parseMajor() throws IOException {
    final byte first;
    first = input.readByte();

    if (!Http.isDigit(first)) {
      final String msg;
      msg = "Invalid HTTP version: byte 0x%02X is not a valid digit".formatted(first);

      throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_LINE);
    }

    int major;
    major = first & 0xF;

    final byte next;
    next = input.readByte();

    if (Http.isDigit(next)) {
      final String msg;
      msg = "Invalid HTTP version: major version should contain a single digit";

      throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_LINE);
    }

    if (next == '.') {
      return major;
    }

    if (next == '\n') {
      final String msg;
      msg = "CRLF sequence required as line terminator";

      throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
    }

    if (next != '\r') {
      final String msg;
      msg = "Invalid HTTP version: unexpected byte 0x%02X while parsing version (major)".formatted(next);

      throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_LINE);
    }

    final byte lf;
    lf = input.readByte();

    if (lf != Bytes.LF) {
      final String msg;
      msg = "CRLF sequence required as line terminator";

      throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
    }

    done = true;

    return major;
  }

  private int parseMinor() throws IOException {
    final byte first;
    first = input.readByte();

    if (!Http.isDigit(first)) {
      final String msg;
      msg = "Invalid HTTP version: byte 0x%02X is not a valid digit".formatted(first);

      throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_LINE);
    }

    int minor;
    minor = first & 0xF;

    final byte b;
    b = input.readByte();

    if (Http.isDigit(b)) {
      final String msg;
      msg = "Invalid HTTP version: minor version should contain a single digit";

      throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_LINE);
    }

    if (b == '\n') {
      final String msg;
      msg = "CRLF sequence required as line terminator";

      throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
    }

    if (b != '\r') {
      final String msg;
      msg = "Invalid HTTP version: unexpected byte 0x%02X while parsing version (minor)".formatted(b);

      throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_LINE);
    }

    final byte lf;
    lf = input.readByte();

    if (lf != Bytes.LF) {
      final String msg;
      msg = "CRLF sequence required as line terminator";

      throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
    }

    return minor;
  }

}
