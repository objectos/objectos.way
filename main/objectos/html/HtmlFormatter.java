/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.html;

import java.io.IOException;
import objectos.html.pseudom.HtmlDocument;
import objectos.lang.object.Check;

public abstract class HtmlFormatter {

  protected HtmlFormatter() {}

  public final void formatTo(Html html, Appendable appendable) throws IOException {
    Check.notNull(html, "html == null");
    Check.notNull(appendable, "appendable == null");

    HtmlDocument document;
    document = html.compile();

    format(document, appendable);
  }

  public final void formatTo(HtmlTemplate template, Appendable appendable) throws IOException {
    Check.notNull(template, "template == null");
    Check.notNull(appendable, "appendable == null");

    Html html;
    html = new Html();

    HtmlDocument document;
    document = template.compile(html);

    format(document, appendable);
  }

  protected abstract void format(HtmlDocument document, Appendable out) throws IOException;

  protected final int ampersand(Appendable out, String value, int idx, int len) throws IOException {
    enum State {
      START,
      MAYBE_NAMED,
      MAYBE_NUMERIC,
      MAYBE_DECIMAL,
      MAYBE_HEX,
      ENTITY,
      TEXT;
    }

    int start;
    start = idx;

    State state;
    state = State.START;

    loop: while (idx < len) {
      char c;
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

}