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

import java.util.Objects;

record HttpResponse1Header(HttpHeaderName name, String value) {

  public HttpResponse1Header {

    Objects.requireNonNull(name, "name == null");

    checkValue();

  }

  public HttpResponse1Header(HttpHeaderName name, long value) {

    this(name, Long.toString(value));

  }

  private enum ValueParser {

    START,

    NORMAL,

    WS,

    INVALID;

  }

  private void checkValue() {
    final int len;
    len = value.length(); // early implicit null-check

    ValueParser parser;
    parser = ValueParser.START;

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      if (c >= 128) {
        throw Http.invalidFieldContent(idx, c);
      }

      final byte flag;
      flag = Http.HEADER_VALUE_TABLE[c];

      switch (parser) {
        case START -> {
          if (flag == Http.HEADER_VALUE_VALID) {
            parser = ValueParser.NORMAL;
          }

          else if (flag == Http.HEADER_VALUE_WS) {
            throw new IllegalArgumentException("Leading SPACE or HTAB characters are not allowed");
          }

          else {
            throw Http.invalidFieldContent(idx, c);
          }
        }

        case NORMAL, WS -> {
          if (flag == Http.HEADER_VALUE_VALID) {
            parser = ValueParser.NORMAL;
          }

          else if (flag == Http.HEADER_VALUE_WS) {
            parser = ValueParser.WS;
          }

          else {
            throw Http.invalidFieldContent(idx, c);
          }
        }

        case INVALID -> {
          throw Http.invalidFieldContent(idx, c);
        }
      }
    }

    switch (parser) {
      case START, NORMAL -> {
        // valid - noop
      }

      case WS -> {
        throw new IllegalArgumentException("Trailing SPACE or HTAB characters are not allowed");
      }

      case INVALID -> {
        throw new IllegalStateException("Unexpected INVALID state");
      }
    }
  }

}