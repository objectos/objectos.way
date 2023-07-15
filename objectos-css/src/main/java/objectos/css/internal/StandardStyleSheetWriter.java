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
import objectos.css.StyleSheet;
import objectos.css.StyleSheetWriter;

public final class StandardStyleSheetWriter implements StyleSheetWriter {

  private static final String INDENTATION = "  ";

  private static final String NL = System.lineSeparator();

  private final Appendable appendable;

  public StandardStyleSheetWriter(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public final void write(StyleSheet sheet) throws IOException {
    CompiledStyleSheet compiled;
    compiled = (CompiledStyleSheet) sheet;

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
        case ByteCode.BLOCK_END -> {
          appendable.append('}');
          appendable.append(NL);
        }

        case ByteCode.BLOCK_START -> {
          appendable.append(" {");
          appendable.append(NL);
        }

        case ByteCode.BLOCK_EMPTY -> {
          appendable.append(" {}");
          appendable.append(NL);
        }

        case ByteCode.COMMA -> {
          appendable.append(", ");
        }

        case ByteCode.KEYWORD,
             ByteCode.SELECTOR -> {
          String name;
          name = Bytes.standardNameValue(bytes[index++], bytes[index++]);

          appendable.append(name);
        }

        case ByteCode.JAVA_DOUBLE -> {
          double value;
          value = Bytes.doubleValue(
            bytes[index++], bytes[index++], bytes[index++], bytes[index++],
            bytes[index++], bytes[index++], bytes[index++], bytes[index++]
          );

          appendable.append(Double.toString(value));
        }

        case ByteCode.JAVA_INT -> {
          int value;
          value = Bytes.intValue(bytes[index++], bytes[index++], bytes[index++], bytes[index++]);

          appendable.append(Integer.toString(value));
        }

        case ByteCode.JAVA_STRING -> {
          int objectIndex;
          objectIndex = Bytes.decodeIndex2(bytes[index++], bytes[index++]);

          Object object;
          object = objects[objectIndex];

          String s;
          s = (String) object;

          appendable.append('"');
          appendable.append(s);
          appendable.append('"');
        }

        case ByteCode.LENGTH_DOUBLE -> {
          double value;
          value = Bytes.doubleValue(
            bytes[index++], bytes[index++], bytes[index++], bytes[index++],
            bytes[index++], bytes[index++], bytes[index++], bytes[index++]
          );

          appendable.append(Double.toString(value));

          LengthUnit unit;
          unit = LengthUnit.byOrdinal(bytes[index++]);

          appendable.append(unit.cssName);
        }

        case ByteCode.LENGTH_INT -> {
          int value;
          value = Bytes.intValue(bytes[index++], bytes[index++], bytes[index++], bytes[index++]);

          appendable.append(Integer.toString(value));

          LengthUnit unit;
          unit = LengthUnit.byOrdinal(bytes[index++]);

          appendable.append(unit.cssName);
        }

        case ByteCode.PERCENTAGE_DOUBLE -> {
          double value;
          value = Bytes.doubleValue(
            bytes[index++], bytes[index++], bytes[index++], bytes[index++],
            bytes[index++], bytes[index++], bytes[index++], bytes[index++]
          );

          appendable.append(Double.toString(value));
          appendable.append('%');
        }

        case ByteCode.PERCENTAGE_INT -> {
          int value;
          value = Bytes.intValue(bytes[index++], bytes[index++], bytes[index++], bytes[index++]);

          appendable.append(Integer.toString(value));
          appendable.append('%');
        }

        case ByteCode.PROPERTY_NAME -> {
          String name;
          name = Bytes.propertyName(bytes[index++], bytes[index++]);

          appendable.append(name);
          appendable.append(':');
        }

        case ByteCode.SEMICOLON, ByteCode.SEMICOLON_OPTIONAL -> {
          appendable.append(';');
          appendable.append(NL);
        }

        case ByteCode.SPACE, ByteCode.SPACE_OPTIONAL -> {
          appendable.append(' ');
        }

        case ByteCode.TAB -> {
          int level = bytes[index++];

          for (int i = 0; i < level; i++) {
            appendable.append(INDENTATION);
          }
        }

        case ByteCode.ZERO -> {
          appendable.append('0');
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: code=" + code
        );
      }
    }
  }

}