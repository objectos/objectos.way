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
package objectox.http.session;

import java.util.function.Function;
import objectos.internal.Ascii;

final class SessionCookieParser {

  private final String cookieName;

  SessionCookieParser(String cookieName) {
    this.cookieName = cookieName;
  }

  public final <T> T parse(String cookie, Function<String, T> processor) {
    if (cookie == null) {
      return null;
    } else {
      return parse0(cookie, processor);
    }
  }

  private enum State {
    START,

    TEST_NAME,

    SKIP_NAME,

    MAYBE_VALUE,

    TEST_VALUE,

    SKIP_VALUE;
  }

  private <T> T parse0(String cookie, Function<String, T> processor) {
    State state;
    state = State.START;

    int startIndex;
    startIndex = 0;

    final int len;
    len = cookie.length();

    final int cookieNameLength;
    cookieNameLength = cookieName.length();

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = cookie.charAt(idx);

      switch (state) {
        case START -> {
          if (c == Ascii.SP || c == Ascii.TAB || c == ';') {
            state = State.START;
          }

          else if (c == '=') {
            // empty name, skip value

            state = State.SKIP_VALUE;
          }

          else {
            // safe as cookie name must not be blank
            final char firstChar;
            firstChar = cookieName.charAt(0);

            if (firstChar == c) {
              // continue from second char
              startIndex = 1;

              // first char matches name
              state = State.TEST_NAME;
            } else {
              // first char does not match -> skip name
              state = State.SKIP_NAME;
            }
          }
        }

        case TEST_NAME -> {
          if (c == '=') {
            if (startIndex == cookieNameLength) {
              // marks the equals sign
              startIndex = idx;

              state = State.MAYBE_VALUE;
            } else {
              state = State.SKIP_VALUE;
            }
          }

          else if (startIndex >= cookieNameLength) {
            // this name is longer than cookieName
            state = State.SKIP_NAME;
          }

          else {
            final char currentChar;
            currentChar = cookieName.charAt(startIndex++);

            if (currentChar != c) {
              // current char does not match -> skip name
              state = State.SKIP_NAME;
            } else {
              // current char matches
              state = State.TEST_NAME;
            }
          }
        }

        case SKIP_NAME -> {
          if (c == '=') {
            state = State.SKIP_VALUE;
          } else {
            state = State.SKIP_NAME;
          }
        }

        case MAYBE_VALUE -> {
          startIndex = idx;

          if (c == ';') {
            // empty value
            state = State.START;
          } else {
            state = State.TEST_VALUE;
          }
        }

        case TEST_VALUE -> {
          if (c == ';') {
            final String value;
            value = cookie.substring(startIndex, idx);

            final T maybe;
            maybe = processor.apply(value);

            if (maybe != null) {
              return maybe;
            }

            state = State.START;
          } else {
            state = State.TEST_VALUE;
          }
        }

        case SKIP_VALUE -> {
          if (c == ';') {
            state = State.START;
          } else {
            state = State.SKIP_VALUE;
          }
        }
      }
    }

    if (state == State.TEST_VALUE) {
      final String value;
      value = cookie.substring(startIndex, len);

      return processor.apply(value);
    } else {
      return null;
    }
  }

}
