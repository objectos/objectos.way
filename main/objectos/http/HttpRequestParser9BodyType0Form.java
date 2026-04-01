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

final class HttpRequestParser9BodyType0Form {

  private static final byte[] MESSAGE = "Invalid request body.\n".getBytes(StandardCharsets.US_ASCII);

  enum Invalid implements HttpClientException.Kind {
    // invalid URL-encoded character
    CHAR,

    // invalid percent-encoded value
    PERCENT;

    private final HttpStatus status;

    private Invalid() {
      this(HttpStatus.BAD_REQUEST);
    }

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

  private boolean done;

  private boolean emptyValue;

  private final InputStream input;

  private Map<String, Object> params = Map.of();

  private HttpRequestParser1UrlDecoder urlDecoder;

  HttpRequestParser9BodyType0Form(InputStream input) {
    this.input = input;
  }

  public final Map<String, Object> parse() throws IOException {
    try {
      return parse0();
    } catch (DecodeException e) {
      throw HttpClientException.of(Invalid.PERCENT, e);
    }
  }

  private Map<String, Object> parse0() throws DecodeException, IOException {
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

    return params;
  }

  private static final byte[] FORM_TABLE;

  private static final byte FORM_VALID = 1;
  private static final byte FORM_PERCENT = 2;
  private static final byte FORM_PLUS = 3;
  private static final byte FORM_EQUALS = 4;
  private static final byte FORM_AMPERSAND = 5;

  static {
    final byte[] table;
    table = new byte[128];

    // 0 = invalid
    // 1 = valid
    // 2 = %xx
    // 3 = '+' -> SPACE
    // 4 = '=' -> key/value separator
    // 5 = '&' -> next key

    Ascii.fill(table, Http.unreserved(), FORM_VALID);

    Ascii.fill(table, Http.subDelims(), FORM_VALID);

    table[':'] = FORM_VALID;

    table['@'] = FORM_VALID;

    table['/'] = FORM_VALID;

    table['?'] = FORM_VALID;

    table['%'] = FORM_PERCENT;

    table['+'] = FORM_PLUS;

    table['='] = FORM_EQUALS;

    table['&'] = FORM_AMPERSAND;

    FORM_TABLE = table;
  }

  private String parseName() throws DecodeException, IOException {
    final StringBuilder name;
    name = new StringBuilder();

    while (true) {
      final int c;
      c = input.read();

      if (c == -1) {
        done = true;

        return name.toString();
      }

      if (c > 0xF) {
        throw HttpClientException.of(Invalid.CHAR);
      }

      final byte code;
      code = FORM_TABLE[c];

      switch (code) {
        case FORM_VALID -> {
          name.append((char) c);
        }

        case FORM_PERCENT -> {
          final int decoded;
          decoded = decodePerc();

          name.appendCodePoint(decoded);
        }

        case FORM_PLUS -> {
          name.append(' ');
        }

        case FORM_EQUALS -> {
          return name.toString();
        }

        case FORM_AMPERSAND -> {
          emptyValue = true;

          return name.toString();
        }

        default -> throw HttpClientException.of(Invalid.CHAR);
      }
    }
  }

  private String parseValue() throws DecodeException, IOException {
    final StringBuilder value;
    value = new StringBuilder();

    while (true) {
      final int c;
      c = input.read();

      if (c == -1) {
        done = true;

        return value.toString();
      }

      if (c > 0xF) {
        throw HttpClientException.of(Invalid.CHAR);
      }

      final byte code;
      code = FORM_TABLE[c];

      switch (code) {
        case FORM_VALID -> {
          value.append((char) c);
        }

        case FORM_PERCENT -> {
          final int decoded;
          decoded = decodePerc();

          value.appendCodePoint(decoded);
        }

        case FORM_PLUS -> {
          value.append(' ');
        }

        case FORM_AMPERSAND -> {
          return value.toString();
        }

        default -> throw HttpClientException.of(Invalid.CHAR);
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

    Http.queryParamsAdd(params, Function.identity(), name, value);
  }

  private int decodePerc() throws DecodeException, IOException {
    if (urlDecoder == null) {
      urlDecoder = new HttpRequestParser1UrlDecoder(input);
    }

    return urlDecoder.decode();
  }

}
