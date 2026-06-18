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
package objectox.http;

import objectos.http.HeaderName;

public record Header(HeaderName name, String value) {

  public static final Header DATE = new Header(HeaderName.DATE, null);

  private enum ValueParser {

    START,

    NORMAL,

    WS,

    INVALID;

  }

  public static Header of(HeaderName name, String value) {
    final String v;
    v = checkValue(value);

    return new Header(name, v);
  }

  private static String checkValue(String value) {
    final int len;
    len = value.length(); // early implicit null-check

    ValueParser parser;
    parser = ValueParser.START;

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      if (c >= 128) {
        throw Rfc.invalidFieldContent(idx, c);
      }

      final byte flag;
      flag = Rfc.HEADER_VALUE_TABLE[c];

      switch (parser) {
        case START -> {
          if (flag == Rfc.HEADER_VALUE_VALID) {
            parser = ValueParser.NORMAL;
          }

          else if (flag == Rfc.HEADER_VALUE_WS) {
            throw new IllegalArgumentException("Leading SPACE or HTAB characters are not allowed");
          }

          else {
            throw Rfc.invalidFieldContent(idx, c);
          }
        }

        case NORMAL, WS -> {
          if (flag == Rfc.HEADER_VALUE_VALID) {
            parser = ValueParser.NORMAL;
          }

          else if (flag == Rfc.HEADER_VALUE_WS) {
            parser = ValueParser.WS;
          }

          else {
            throw Rfc.invalidFieldContent(idx, c);
          }
        }

        case INVALID -> {
          throw Rfc.invalidFieldContent(idx, c);
        }
      }
    }

    return switch (parser) {
      case START, NORMAL -> value;

      case WS -> {
        throw new IllegalArgumentException("Trailing SPACE or HTAB characters are not allowed");
      }

      case INVALID -> {
        throw new IllegalStateException("Unexpected INVALID state");
      }
    };
  }

}