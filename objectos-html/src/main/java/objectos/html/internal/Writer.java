/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import java.io.IOException;
import objectos.html.pseudom.DocumentProcessor;

/**
 * Base {@link DocumentProcessor} implementation suitable for writing HTML
 * files.
 *
 * @since 0.5.1
 */
public abstract class Writer implements DocumentProcessor {

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

  protected final void writeText(String value) {
    if (ioException != null) {
      return;
    }

    try {
      writeText0(value);
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

  private void writeText0(String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      var c = value.charAt(idx++);

      switch (c) {
        case '&' -> {
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
            c = value.charAt(idx++);

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
        }

        case '"' -> out.append("&quot;");

        case '<' -> out.append("&lt;");

        case '>' -> out.append("&gt;");

        case '\u00A9' -> out.append("&copy;");

        default -> out.append(c);
      }
    }
  }

}