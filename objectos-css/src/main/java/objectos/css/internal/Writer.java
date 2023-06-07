/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import java.io.IOException;
import objectos.css.pseudom.StyleSheetProcessor;

public abstract class Writer implements StyleSheetProcessor {

  private IOException ioException;

  public Appendable out;

  public final void throwIfNecessary() throws IOException {
    if (ioException == null) {
      return;
    }

    var toThrow = ioException;

    ioException = null;

    throw toThrow;
  }

  protected final void write(char c) {
    if (ioException != null) {
      return;
    }

    try {
      out.append(c);
    } catch (IOException e) {
      ioException = e;
    }
  }

  protected final void write(int value) {
    var s = Integer.toString(value);

    write(s);
  }

  protected final void write(String s) {
    if (ioException != null) {
      return;
    }

    try {
      out.append(s);
    } catch (IOException e) {
      ioException = e;
    }
  }

  protected final void writeAttributeValue(String value) {
    if (ioException != null) {
      return;
    }

    try {
      for (int idx = 0, len = value.length(); idx < len;) {
        var c = value.charAt(idx++);

        switch (c) {
          case '&' -> idx = writeAmpersandAttribute(value, idx, len);

          case '<' -> writeLesserThan();

          case '>' -> writeGreaterThan();

          case '"' -> out.append("&quot;");

          case '\'' -> out.append("&#39;");

          default -> out.append(c);
        }
      }
    } catch (IOException e) {
      ioException = e;
    }
  }

  protected final void writeText(String value) {
    if (ioException != null) {
      return;
    }

    try {
      for (int idx = 0, len = value.length(); idx < len;) {
        var c = value.charAt(idx++);

        switch (c) {
          case '&' -> writeAmpersand();

          case '<' -> writeLesserThan();

          case '>' -> writeGreaterThan();

          default -> out.append(c);
        }
      }
    } catch (IOException e) {
      ioException = e;
    }
  }

  private boolean isAsciiAlpha(char c) {
    return 'A' <= c && c <= 'Z'
        || 'a' <= c && c <= 'z';
  }

  private boolean isAsciiAlphanumeric(char c) {
    return isAsciiDigit(c) || isAsciiAlpha(c);
  }

  private boolean isAsciiDigit(char c) {
    return '0' <= c && c <= '9';
  }

  private boolean isAsciiHexDigit(char c) {
    return isAsciiDigit(c)
        || 'a' <= c && c <= 'f'
        || 'A' <= c && c <= 'F';
  }

  private void writeAmpersand() throws IOException {
    out.append("&amp;");
  }

  private int writeAmpersandAttribute(String value, int idx, int len) throws IOException {
    enum State {
      START,
      MAYBE_NAMED,
      MAYBE_NUMERIC,
      MAYBE_DECIMAL,
      MAYBE_HEX,
      ENTITY,
      TEXT;
    }

    int start = idx;

    var state = State.START;

    loop: while (idx < len) {
      char c = value.charAt(idx++);

      switch (state) {
        case START -> {
          if (c == '#') {
            state = State.MAYBE_NUMERIC;
          } else if (isAsciiAlphanumeric(c)) {
            state = State.MAYBE_NAMED;
          } else {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_NAMED -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiAlphanumeric(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_NUMERIC -> {
          if (c == 'x' || c == 'X') {
            state = State.MAYBE_HEX;
          } else if (isAsciiDigit(c)) {
            state = State.MAYBE_DECIMAL;
          } else {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_DECIMAL -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiDigit(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_HEX -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiHexDigit(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case ENTITY, TEXT -> {
          throw new AssertionError();
        }

        default -> {
          throw new UnsupportedOperationException(
            "Implement me :: state=" + state
          );
        }
      }
    }

    switch (state) {
      case START -> {
        out.append("&amp;");
      }

      case ENTITY -> {
        out.append('&');

        out.append(value, start, idx);
      }

      case TEXT -> {
        out.append("&amp;");

        idx = start;
      }

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      }
    }

    return idx;
  }

  private void writeGreaterThan() throws IOException {
    out.append("&gt;");
  }

  private void writeLesserThan() throws IOException {
    out.append("&lt;");
  }

}