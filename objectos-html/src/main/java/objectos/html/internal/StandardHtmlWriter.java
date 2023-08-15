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
import objectos.html.CompiledHtml;
import objectos.html.HtmlWriter;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;

public final class StandardHtmlWriter implements HtmlWriter {

  private static final String NL = System.lineSeparator();

  private final Appendable appendable;

  public StandardHtmlWriter(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public final void write(CompiledHtml html) throws IOException {
    CompiledMarkup compiled;
    compiled = (CompiledMarkup) html;

    int index;
    index = 0;

    byte[] bytes;
    bytes = compiled.main;

    Object[] objects;
    objects = compiled.objects;

    while (index < bytes.length) {
      byte code;
      code = bytes[index++];

      switch (code) {
        case ByteCode.ATTR_NAME -> {
          byte ordinalByte;
          ordinalByte = bytes[index++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          StandardAttributeName name;
          name = StandardAttributeName.getByCode(ordinal);

          appendable.append(name.getName());
        }

        case ByteCode.ATTR_VALUE -> {
          byte int0;
          int0 = bytes[index++];

          byte int1;
          int1 = bytes[index++];

          int objectIndex;
          objectIndex = Bytes.decodeInt(int0, int1);

          Object o;
          o = objects[objectIndex];

          String value;
          value = o.toString();

          writeAttributeValue(value);
        }

        case ByteCode.ATTR_VALUE_END -> appendable.append('\"');

        case ByteCode.ATTR_VALUE_START -> appendable.append("=\"");

        case ByteCode.DOCTYPE -> appendable.append("<!DOCTYPE html>");

        case ByteCode.END_TAG -> {
          byte ordinalByte;
          ordinalByte = bytes[index++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          StandardElementName name;
          name = StandardElementName.getByCode(ordinal);

          appendable.append('<');
          appendable.append('/');
          appendable.append(name.getName());
          appendable.append('>');
        }

        case ByteCode.GT -> appendable.append('>');

        case ByteCode.NL,
             ByteCode.NL_OPTIONAL -> appendable.append(NL);

        case ByteCode.RAW,
             ByteCode.TEXT_SCRIPT,
             ByteCode.TEXT_STYLE -> {
          byte int0;
          int0 = bytes[index++];

          byte int1;
          int1 = bytes[index++];

          int objectIndex;
          objectIndex = Bytes.decodeInt(int0, int1);

          String value;
          value = (String) objects[objectIndex];

          appendable.append(value);
        }

        case ByteCode.SPACE -> appendable.append(' ');

        case ByteCode.START_TAG -> {
          byte ordinalByte;
          ordinalByte = bytes[index++];

          int ordinal;
          ordinal = Bytes.decodeInt(ordinalByte);

          StandardElementName name;
          name = StandardElementName.getByCode(ordinal);

          appendable.append('<');
          appendable.append(name.getName());
        }

        case ByteCode.TAB,
             ByteCode.TAB_BLOCK -> index++;

        case ByteCode.TEXT -> {
          byte int0;
          int0 = bytes[index++];

          byte int1;
          int1 = bytes[index++];

          int objectIndex;
          objectIndex = Bytes.decodeInt(int0, int1);

          String value;
          value = (String) objects[objectIndex];

          writeText(value);
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: code=" + code
        );
      }
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
    appendable.append("&amp;");
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
        appendable.append("&amp;");
      }

      case ENTITY -> {
        appendable.append('&');

        appendable.append(value, start, idx);
      }

      case TEXT -> {
        appendable.append("&amp;");

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

  private void writeAttributeValue(String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      var c = value.charAt(idx++);

      switch (c) {
        case '&' -> idx = writeAmpersandAttribute(value, idx, len);

        case '<' -> writeLesserThan();

        case '>' -> writeGreaterThan();

        case '"' -> appendable.append("&quot;");

        case '\'' -> appendable.append("&#39;");

        default -> appendable.append(c);
      }
    }
  }

  private void writeGreaterThan() throws IOException {
    appendable.append("&gt;");
  }

  private void writeLesserThan() throws IOException {
    appendable.append("&lt;");
  }

  private void writeText(String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      var c = value.charAt(idx++);

      switch (c) {
        case '&' -> writeAmpersand();

        case '<' -> writeLesserThan();

        case '>' -> writeGreaterThan();

        default -> appendable.append(c);
      }
    }
  }

}
