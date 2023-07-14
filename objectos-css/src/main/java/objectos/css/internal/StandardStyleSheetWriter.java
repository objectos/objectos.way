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

    byte[] data;
    data = compiled.main;

    while (index < data.length) {
      byte code;
      code = data[index++];

      switch (code) {
        case ByteCode.BLOCK_END -> {
          appendable.append('}');
          appendable.append(NL);
        }

        case ByteCode.BLOCK_START -> {
          appendable.append(" {");
          appendable.append(NL);
        }

        case ByteCode.KEYWORD,
             ByteCode.SELECTOR -> {
          String name;
          name = Bytes.standardNameValue(data[index++], data[index++]);

          appendable.append(name);
        }

        case ByteCode.LENGTH_DOUBLE -> {
          double value;
          value = Bytes.doubleValue(
            data[index++], data[index++], data[index++], data[index++],
            data[index++], data[index++], data[index++], data[index++]
          );

          appendable.append(Double.toString(value));

          LengthUnit unit;
          unit = LengthUnit.byOrdinal(data[index++]);

          appendable.append(unit.cssName);
        }

        case ByteCode.LENGTH_INT -> {
          int value;
          value = Bytes.intValue(data[index++], data[index++], data[index++], data[index++]);

          appendable.append(Integer.toString(value));

          LengthUnit unit;
          unit = LengthUnit.byOrdinal(data[index++]);

          appendable.append(unit.cssName);
        }

        case ByteCode.PROPERTY_NAME -> {
          String name;
          name = Bytes.standardNameValue(data[index++], data[index++]);

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
          int level = data[index++];

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