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
import objectos.internal.Bytes;

final class HttpRequestParser6Headers {

  private static final byte[] MESSAGE = "Invalid request headers.\n".getBytes(StandardCharsets.US_ASCII);

  enum Invalid implements HttpClientException.Kind {
    // header name has an invalid character
    NAME_CHAR(HttpStatus.BAD_REQUEST),

    // header value has an invalid character
    VALUE_CHAR(HttpStatus.BAD_REQUEST),

    // invalid header terminator, i.e., the last '\r\n'
    TERMINATOR(HttpStatus.BAD_REQUEST),

    // Unexpected end of stream
    EOF(HttpStatus.BAD_REQUEST),

    // 431 Request Header Fields Too Large
    REQUEST_HEADER_FIELDS_TOO_LARGE(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);

    private final HttpStatus status;

    private Invalid(HttpStatus status) {
      this.status = status;
    }

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

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
      throw HttpClientException.of(Invalid.EOF, e);
    } catch (HttpRequestParser0Input.Overflow e) {
      throw HttpClientException.of(Invalid.REQUEST_HEADER_FIELDS_TOO_LARGE, e);
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

        throw HttpClientException.of(Invalid.TERMINATOR);
      }

      case Bytes.LF -> {
        throw HttpClientException.of(Invalid.TERMINATOR);
      }

      default -> false;
    };
  }

  private HttpHeaderName parseName() throws IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      final byte mapped;
      mapped = HttpHeaderNameImpl.map(b);

      switch (mapped) {
        case HttpHeaderNameImpl.INVALID -> {
          throw HttpClientException.of(Invalid.NAME_CHAR);
        }

        case HttpHeaderNameImpl.COLON -> {
          final String lowerCase;
          lowerCase = makeString();

          final HttpHeaderNameImpl standard;
          standard = HttpHeaderNameImpl.byLowerCase(lowerCase);

          if (standard != null) {
            return standard;
          } else {
            return HttpHeaderNameImpl.ofLowerCase(lowerCase);
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

      final byte code;
      code = input.readTable(HEADER_VALUE_TABLE, Invalid.VALUE_CHAR);

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
          throw HttpClientException.of(Invalid.TERMINATOR);
        }

        default -> {
          throw HttpClientException.of(Invalid.VALUE_CHAR);
        }
      }
    }

    int validIndex;
    validIndex = input.bufferIndex();

    // value contents
    while (true) {
      final byte code;
      code = input.readTable(HEADER_VALUE_TABLE, Invalid.VALUE_CHAR);

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
          throw HttpClientException.of(Invalid.TERMINATOR);
        }

        default -> {
          throw HttpClientException.of(Invalid.VALUE_CHAR);
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
      throw HttpClientException.of(Invalid.VALUE_CHAR);
    } else {
      return value;
    }
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
