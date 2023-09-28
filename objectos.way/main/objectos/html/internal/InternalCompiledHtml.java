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
import objectos.html.Html;
import objectos.lang.Check;

public final class InternalCompiledHtml implements Html {

  private static final String NL = System.lineSeparator();

  private static final String AMP = "&amp;";

  private static final String GT = "&gt;";

  private static final String LT = "&lt;";

  final byte[] main;

  final Object[] objects;

  InternalCompiledHtml(byte[] main, Object[] objects) {
    this.main = main;

    this.objects = objects;
  }

  @Override
  public final void writeTo(Appendable out) throws IOException {
    Check.notNull(out, "out == null");

    int index;
    index = 0;

    while (index < main.length) {
      byte code;
      code = main[index++];

      switch (code) {
        case ByteCode.ATTR_NAME -> {
          byte ordinalByte;
          ordinalByte = main[index++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          AttributeName name;
          name = AttributeName.getByCode(ordinal);

          out.append(name.getName());
        }

        case ByteCode.ATTR_VALUE -> {
          byte int0;
          int0 = main[index++];

          byte int1;
          int1 = main[index++];

          int objectIndex;
          objectIndex = Bytes.decodeInt(int0, int1);

          Object o;
          o = objects[objectIndex];

          String value;
          value = o.toString();

          writeAttributeValue(out, value);
        }

        case ByteCode.ATTR_VALUE_END -> out.append('\"');

        case ByteCode.ATTR_VALUE_START -> out.append("=\"");

        case ByteCode.DOCTYPE -> out.append("<!DOCTYPE html>");

        case ByteCode.END_TAG -> {
          byte ordinalByte;
          ordinalByte = main[index++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          StandardElementName name;
          name = StandardElementName.getByCode(ordinal);

          out.append('<');
          out.append('/');
          out.append(name.getName());
          out.append('>');
        }

        case ByteCode.GT -> out.append('>');

        case ByteCode.NL,
             ByteCode.NL_OPTIONAL -> out.append(NL);

        case ByteCode.RAW,
             ByteCode.TEXT_SCRIPT,
             ByteCode.TEXT_STYLE -> {
          byte int0;
          int0 = main[index++];

          byte int1;
          int1 = main[index++];

          int objectIndex;
          objectIndex = Bytes.decodeInt(int0, int1);

          String value;
          value = (String) objects[objectIndex];

          out.append(value);
        }

        case ByteCode.SPACE -> out.append(' ');

        case ByteCode.START_TAG -> {
          byte ordinalByte;
          ordinalByte = main[index++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          StandardElementName name;
          name = StandardElementName.getByCode(ordinal);

          out.append('<');
          out.append(name.getName());
        }

        case ByteCode.TAB,
             ByteCode.TAB_BLOCK -> index++;

        case ByteCode.TEXT -> {
          byte int0;
          int0 = main[index++];

          byte int1;
          int1 = main[index++];

          int objectIndex;
          objectIndex = Bytes.decodeInt(int0, int1);

          String value;
          value = (String) objects[objectIndex];

          writeText(out, value);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: code=" + code
        );
      }
    }
  }

  @Override
  public final String toString() {
    try {
      StringBuilder sb;
      sb = new StringBuilder();

      writeTo(sb);

      return sb.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
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

  private int writeAmpersandAttribute(
      Appendable out, String value, int idx, int len)
      throws IOException {
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

  private void writeAttributeValue(Appendable out, String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      char c;
      c = value.charAt(idx++);

      switch (c) {
        case '&' -> idx = writeAmpersandAttribute(out, value, idx, len);

        case '<' -> out.append(LT);

        case '>' -> out.append(GT);

        case '"' -> out.append("&quot;");

        case '\'' -> out.append("&#39;");

        default -> out.append(c);
      }
    }
  }

  private void writeText(Appendable out, String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      char c;
      c = value.charAt(idx++);

      switch (c) {
        case '&' -> out.append(AMP);

        case '<' -> out.append(LT);

        case '>' -> out.append(GT);

        default -> out.append(c);
      }
    }
  }

}