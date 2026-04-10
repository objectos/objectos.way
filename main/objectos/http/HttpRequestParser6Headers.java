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
import objectos.http.HttpRequestParserException.Kind;
import objectos.internal.Bytes;

final class HttpRequestParser6Headers {

  private final HttpRequestParser0Input input;

  private Map<HttpHeaderName, Object> map = Map.of();

  private final StringBuilder sb = new StringBuilder();

  HttpRequestParser6Headers(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final Map<HttpHeaderName, Object> parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof e) {
      final String msg;
      msg = "EOF while parsing HTTP headers";

      throw new HttpRequestParserException(msg, e, Kind.INVALID_REQUEST_HEADERS);
    } catch (HttpRequestParser0Input.Overflow e) {
      final String msg;
      msg = "Buffer overflow while parsing HTTP headers";

      throw new HttpRequestParserException(msg, e, Kind.REQUEST_HEADER_FIELDS_TOO_LARGE);
    }
  }

  private Map<HttpHeaderName, Object> parse0() throws IOException {
    while (true) {
      if (parseTerminator()) {
        break;
      }

      final HttpHeaderName name;
      name = parseName();

      final String value;
      value = parseValue();

      put(name, value);
    }

    return map;
  }

  private boolean parseTerminator() throws IOException {
    final byte first;
    first = input.peekByte();

    return switch (first) {
      case Bytes.CR -> {
        input.skipByte();

        final byte second;
        second = input.readByte();

        if (second == Bytes.LF) {
          yield true;
        }

        final String msg;
        msg = "CRLF sequence required as line terminator";

        throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
      }

      case Bytes.LF -> {
        final String msg;
        msg = "CRLF sequence required as line terminator";

        throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
      }

      default -> false;
    };
  }

  private HttpHeaderName parseName() throws IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      final byte mapped;
      mapped = HttpHeaderName0.map(b);

      switch (mapped) {
        case HttpHeaderName0.INVALID -> {
          final String msg;
          msg = "Invalid HTTP header field: unexpected byte 0x%02X while parsing name".formatted(b);

          throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_HEADERS);
        }

        case HttpHeaderName0.COLON -> {
          final String lowerCase;
          lowerCase = makeString();

          final HttpHeaderName0 standard;
          standard = HttpHeaderName0.byLowerCase(lowerCase);

          if (standard != null) {
            return standard;
          } else {
            return HttpHeaderName0.ofLowerCase(lowerCase);
          }
        }

        default -> {
          appendChar(mapped);
        }
      }
    }
  }

  private static final byte[] HEADER_VALUE_TABLE;

  private static final byte HEADER_VALUE_VALID = 1;

  private static final byte HEADER_VALUE_WS = 2;

  private static final byte HEADER_VALUE_CR = 3;

  private static final byte HEADER_VALUE_LF = 4;

  static {
    final byte[] table;
    table = new byte[128];

    for (int b = 0x21; b < 0x7F; b++) {
      // VCHAR are valid
      table[b] = HEADER_VALUE_VALID;
    }

    // valid under certain circustances
    table[' '] = HEADER_VALUE_WS;

    table['\t'] = HEADER_VALUE_WS;

    table['\r'] = HEADER_VALUE_CR;

    table['\n'] = HEADER_VALUE_LF;

    HEADER_VALUE_TABLE = table;
  }

  private String parseValue() throws IOException {
    // skip OWS
    loop: while (true) {
      input.mark();

      final byte b;
      b = input.readByte(Kind.INVALID_REQUEST_HEADERS);

      final byte code;
      code = HEADER_VALUE_TABLE[b];

      switch (code) {
        case HEADER_VALUE_WS -> {
          // noop
        }

        case HEADER_VALUE_VALID -> {
          break loop;
        }

        case HEADER_VALUE_CR -> {
          final int endIndex;
          endIndex = input.bufferIndex();

          return parseHeaderValueCR(endIndex);
        }

        case HEADER_VALUE_LF -> {
          final String msg;
          msg = "CRLF sequence required as line terminator";

          throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
        }

        default -> {
          final String msg;
          msg = "Invalid HTTP header field: unexpected byte 0x%02X while parsing value".formatted(b);

          throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_HEADERS);
        }
      }
    }

    int validIndex;
    validIndex = input.bufferIndex();

    // value contents
    while (true) {
      final byte b;
      b = input.readByte(Kind.INVALID_REQUEST_HEADERS);

      final byte code;
      code = HEADER_VALUE_TABLE[b];

      switch (code) {
        case HEADER_VALUE_WS -> {
          // noop
        }

        case HEADER_VALUE_VALID -> {
          validIndex = input.bufferIndex();
        }

        case HEADER_VALUE_CR -> {
          final int endIndex;
          endIndex = validIndex + 1;

          return parseHeaderValueCR(endIndex);
        }

        case HEADER_VALUE_LF -> {
          final String msg;
          msg = "CRLF sequence required as line terminator";

          throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
        }

        default -> {
          final String msg;
          msg = "Invalid HTTP header field: unexpected byte 0x%02X while parsing value".formatted(b);

          throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_HEADERS);
        }
      }
    }
  }

  private String parseHeaderValueCR(int endIndex) throws IOException {
    final String value;
    value = input.makeStr(endIndex);

    final byte lf;
    lf = input.readByte();

    if (lf != Bytes.LF) {
      final String msg;
      msg = "CRLF sequence required as line terminator";

      throw new HttpRequestParserException(msg, Kind.LINE_TERMINATOR);
    }

    return value;
  }

  private void appendChar(byte b) {
    sb.append((char) b);
  }

  private String makeString() {
    final String res;
    res = sb.toString();

    sb.setLength(0);

    return res;
  }

  private void put(HttpHeaderName name, String value) {
    if (map.isEmpty()) {
      map = new HashMap<>();
    }

    Http.mapAdd(map, name, value);
  }

}
