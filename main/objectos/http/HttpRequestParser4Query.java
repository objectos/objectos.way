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
import objectos.internal.Bytes;

final class HttpRequestParser4Query {

  enum Invalid implements HttpClientException.Kind {
    // 414 URI Too Long
    URI_TOO_LONG(Http.REQ_LINE, HttpStatus.URI_TOO_LONG),

    // query has an invalid character
    QUERY_CHAR(Http.REQ_LINE, HttpStatus.BAD_REQUEST),

    // query has an invalid percent encoded sequence
    QUERY_PERCENT(Http.REQ_LINE, HttpStatus.BAD_REQUEST),

    // CRLF required
    LINE_TERMINATOR(Http.LINE_TERM, HttpStatus.BAD_REQUEST);

    private final String message;

    private final HttpStatus status;

    Invalid(String message, HttpStatus status) {
      this.message = message;

      this.status = status;
    }

    @Override
    public final String message() {
      return message;
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

  @SuppressWarnings("serial")
  static final class Http09Exception extends IOException {

    final Map<String, Object> params;

    Http09Exception(Map<String, Object> params) {
      this.params = params;
    }

  }

  private boolean done;

  private boolean emptyValue;

  private final HttpRequestParser0Input input;

  private Map<String, Object> params = Map.of();

  private HttpRequestParser1UrlDecoder urlDecoder;

  private boolean version09;

  HttpRequestParser4Query(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final Map<String, Object> parse() throws IOException {
    try {
      return parse0();
    } catch (DecodeException e) {
      throw new HttpClientException(Invalid.QUERY_PERCENT);
    } catch (HttpRequestParser0Input.Eof e) {
      throw new HttpClientException(Invalid.QUERY_CHAR, e);
    } catch (HttpRequestParser0Input.Overflow e) {
      throw new HttpClientException(Invalid.URI_TOO_LONG, e);
    }
  }

  private Map<String, Object> parse0() throws DecodeException, IOException {
    final byte prev;
    prev = input.peekPrev();

    if (prev == '?') {

      while (true) {
        final String name;
        name = parseName();

        if (done) {
          add(name);

          break;
        }

        if (emptyValue) {
          add(name, "");

          emptyValue = false;

          continue;
        }

        final String value;
        value = parseValue();

        add(name, value);

        if (done) {
          break;
        }
      }

    }

    if (version09) {
      throw new Http09Exception(params);
    } else {
      return params;
    }
  }

  private static final byte[] QUERY_TABLE;

  private static final byte QUERY_VALID = 1;
  private static final byte QUERY_PERCENT = 2;
  private static final byte QUERY_PLUS = 3;
  private static final byte QUERY_EQUALS = 4;
  private static final byte QUERY_AMPERSAND = 5;
  private static final byte QUERY_SPACE = 6;
  private static final byte QUERY_CR = 7;
  private static final byte QUERY_LF = 8;

  static {
    final byte[] table;
    table = new byte[128];

    // 0 = invalid
    // 1 = valid
    // 2 = %xx
    // 3 = '+' -> SPACE
    // 4 = '=' -> key/value separator
    // 5 = '&' -> next key
    // 6 = ' ' -> space
    // 7 = '\r' -> 0.9
    // 7 = '\n' -> 0.9

    Ascii.fill(table, Http.unreserved(), QUERY_VALID);

    Ascii.fill(table, Http.subDelims(), QUERY_VALID);

    table[':'] = QUERY_VALID;

    table['@'] = QUERY_VALID;

    table['/'] = QUERY_VALID;

    table['?'] = QUERY_VALID;

    table['%'] = QUERY_PERCENT;

    table['+'] = QUERY_PLUS;

    table['='] = QUERY_EQUALS;

    table['&'] = QUERY_AMPERSAND;

    table[' '] = QUERY_SPACE;

    table['\r'] = QUERY_CR;

    table['\n'] = QUERY_LF;

    QUERY_TABLE = table;
  }

  private String parseName() throws DecodeException, IOException {
    input.mark();

    while (true) {
      final byte code;
      code = input.readTable(QUERY_TABLE, Invalid.QUERY_CHAR);

      switch (code) {
        case QUERY_VALID -> {
          // noop
        }

        case QUERY_PERCENT -> {
          final StringBuilder name;
          name = input.makeStrBuilder();

          final int decoded;
          decoded = decodePerc();

          name.appendCodePoint(decoded);

          return parseName1(name);
        }

        case QUERY_PLUS -> {
          final StringBuilder name;
          name = input.makeStrBuilder();

          name.append(' ');

          return parseName1(name);
        }

        case QUERY_EQUALS -> {
          return input.makeStr();
        }

        case QUERY_SPACE -> {
          done = true;

          return input.makeStr();
        }

        case QUERY_AMPERSAND -> {
          emptyValue = true;

          return input.makeStr();
        }

        case QUERY_CR -> {
          final byte lf;
          lf = input.readByte();

          if (lf == Bytes.LF) {
            done = true;

            version09 = true;

            return input.makeStr();
          } else {
            throw new HttpClientException(Invalid.LINE_TERMINATOR);
          }
        }

        case QUERY_LF -> {
          throw new HttpClientException(Invalid.LINE_TERMINATOR);
        }

        default -> throw new HttpClientException(Invalid.QUERY_CHAR);
      }
    }
  }

  private String parseName1(StringBuilder name) throws DecodeException, IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      if (b < 0) {
        throw new HttpClientException(Invalid.QUERY_CHAR);
      }

      final byte code;
      code = QUERY_TABLE[b];

      switch (code) {
        case QUERY_VALID -> {
          name.append((char) b);
        }

        case QUERY_PERCENT -> {
          final int decoded;
          decoded = decodePerc();

          name.appendCodePoint(decoded);
        }

        case QUERY_PLUS -> {
          name.append(' ');
        }

        case QUERY_EQUALS -> {
          return name.toString();
        }

        case QUERY_SPACE -> {
          done = true;

          return name.toString();
        }

        case QUERY_AMPERSAND -> {
          emptyValue = true;

          return name.toString();
        }

        case QUERY_CR -> {
          final byte lf;
          lf = input.readByte();

          if (lf == Bytes.LF) {
            done = true;

            version09 = true;

            return name.toString();
          } else {
            throw new HttpClientException(Invalid.LINE_TERMINATOR);
          }
        }

        case QUERY_LF -> {
          throw new HttpClientException(Invalid.LINE_TERMINATOR);
        }

        default -> throw new HttpClientException(Invalid.QUERY_CHAR);
      }
    }
  }

  private String parseValue() throws DecodeException, IOException {
    input.mark();

    while (true) {
      final byte code;
      code = input.readTable(QUERY_TABLE, Invalid.QUERY_CHAR);

      switch (code) {
        case QUERY_VALID -> {
          // noop
        }

        case QUERY_PERCENT -> {
          final StringBuilder value;
          value = input.makeStrBuilder();

          final int decoded;
          decoded = decodePerc();

          value.appendCodePoint(decoded);

          return parseValue1(value);
        }

        case QUERY_PLUS -> {
          final StringBuilder value;
          value = input.makeStrBuilder();

          value.append(' ');

          return parseValue1(value);
        }

        case QUERY_AMPERSAND -> {
          return input.makeStr();
        }

        case QUERY_SPACE -> {
          done = true;

          return input.makeStr();
        }

        case QUERY_CR -> {
          final byte lf;
          lf = input.readByte();

          if (lf == Bytes.LF) {
            done = true;

            version09 = true;

            return input.makeStr();
          } else {
            throw new HttpClientException(Invalid.LINE_TERMINATOR);
          }
        }

        case QUERY_LF -> {
          throw new HttpClientException(Invalid.LINE_TERMINATOR);
        }

        default -> throw new HttpClientException(Invalid.QUERY_CHAR);
      }
    }
  }

  private String parseValue1(StringBuilder value) throws DecodeException, IOException {
    while (true) {
      final byte b;
      b = input.readByte();

      if (b < 0) {
        throw new HttpClientException(Invalid.QUERY_CHAR);
      }

      final byte code;
      code = QUERY_TABLE[b];

      switch (code) {
        case QUERY_VALID -> {
          value.append((char) b);
        }

        case QUERY_PERCENT -> {
          final int decoded;
          decoded = decodePerc();

          value.appendCodePoint(decoded);
        }

        case QUERY_PLUS -> {
          value.append(' ');
        }

        case QUERY_AMPERSAND -> {
          return value.toString();
        }

        case QUERY_SPACE -> {
          done = true;

          return value.toString();
        }

        case QUERY_CR -> {
          final byte lf;
          lf = input.readByte();

          if (lf == Bytes.LF) {
            done = true;

            version09 = true;

            return value.toString();
          } else {
            throw new HttpClientException(Invalid.LINE_TERMINATOR);
          }
        }

        case QUERY_LF -> {
          throw new HttpClientException(Invalid.LINE_TERMINATOR);
        }

        default -> throw new HttpClientException(Invalid.QUERY_CHAR);
      }
    }
  }

  private void add(String name) {
    if (!name.isEmpty()) {
      add(name, "");
    }
  }

  private void add(String name, String value) {
    if (params.isEmpty()) {
      params = new HashMap<>();
    }

    Http.queryParamsAdd(params, name, value);
  }

  private int decodePerc() throws DecodeException, IOException {
    if (urlDecoder == null) {
      urlDecoder = new HttpRequestParser1UrlDecoder(input);
    }

    return urlDecoder.decode();
  }

}
