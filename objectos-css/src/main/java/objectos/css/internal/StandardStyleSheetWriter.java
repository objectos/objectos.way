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
        case ByteCode.AT_MEDIA -> {
          appendable.append("@media");
        }

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

        case ByteCode.DOUBLE_LITERAL -> {
          double value;
          value = Bytes.doubleValue(
            bytes[index++], bytes[index++], bytes[index++], bytes[index++],
            bytes[index++], bytes[index++], bytes[index++], bytes[index++]
          );

          appendable.append(Double.toString(value));
        }

        case ByteCode.INT_LITERAL -> {
          int value;
          value = Bytes.intValue(bytes[index++], bytes[index++], bytes[index++], bytes[index++]);

          appendable.append(Integer.toString(value));
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

        case ByteCode.MEDIA_QUERY -> {
          byte ordinal;
          ordinal = bytes[index++];

          MediaType value;
          value = MediaType.ofOrdinal(ordinal);

          appendable.append(value.cssName);
        }

        case ByteCode.NEXT_RULE -> {
          appendable.append(NL);
        }

        case ByteCode.PARENS_CLOSE -> appendable.append(')');

        case ByteCode.PARENS_OPEN -> appendable.append('(');

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

        case ByteCode.PROPERTY_CUSTOM -> {
          int objectIndex;
          objectIndex = Bytes.decodeIndex2(bytes[index++], bytes[index++]);

          String name;
          name = (String) objects[objectIndex];

          appendable.append(name);
          appendable.append(':');
        }

        case ByteCode.PROPERTY_STANDARD -> {
          String name;
          name = Bytes.propertyName(bytes[index++], bytes[index++]);

          appendable.append(name);
          appendable.append(':');
        }

        case ByteCode.SELECTOR_ATTR -> {
          int objectIndex;
          objectIndex = Bytes.decodeIndex2(bytes[index++], bytes[index++]);

          String name;
          name = (String) objects[objectIndex];

          appendable.append('[');
          appendable.append(name);
          appendable.append(']');
        }

        case ByteCode.SELECTOR_ATTR_VALUE -> {
          int nameIndex;
          nameIndex = Bytes.decodeIndex2(bytes[index++], bytes[index++]);

          String name;
          name = (String) objects[nameIndex];

          InternalAttributeOperator operator;
          operator = InternalAttributeOperator.ofOrdinal(bytes[index++]);

          int valueIndex;
          valueIndex = Bytes.decodeIndex2(bytes[index++], bytes[index++]);

          String value;
          value = (String) objects[valueIndex];

          appendable.append('[');
          appendable.append(name);
          appendable.append(operator.cssName);
          appendable.append('"');
          appendable.append(value);
          appendable.append('"');
          appendable.append(']');
        }

        case ByteCode.SELECTOR_CLASS -> {
          int objectIndex;
          objectIndex = Bytes.decodeIndex2(bytes[index++], bytes[index++]);

          String name;
          name = (String) objects[objectIndex];

          appendable.append(name);
        }

        case ByteCode.SELECTOR_COMBINATOR -> {
          int ordinal;
          ordinal = bytes[index++];

          Combinator combinator;
          combinator = Combinator.ofOrdinal(ordinal);

          appendable.append(combinator.cssName);
        }

        case ByteCode.SELECTOR_PSEUDO_CLASS -> {
          int ordinal;
          ordinal = bytes[index++];

          StandardPseudoClassSelector selector;
          selector = StandardPseudoClassSelector.ofOrdinal(ordinal);

          appendable.append(selector.cssName);
        }

        case ByteCode.SELECTOR_PSEUDO_ELEMENT -> {
          int ordinal;
          ordinal = bytes[index++];

          StandardPseudoElementSelector selector;
          selector = StandardPseudoElementSelector.ofOrdinal(ordinal);

          appendable.append(selector.cssName);
        }

        case ByteCode.SELECTOR_TYPE -> {
          byte b0;
          b0 = bytes[index++];

          int ordinal;
          ordinal = b0;

          if (ordinal < 0) {
            byte b1;
            b1 = bytes[index++];

            ordinal = Bytes.toVarInt(b0, b1);
          }

          StandardTypeSelector selector;
          selector = StandardTypeSelector.ofOrdinal(ordinal);

          appendable.append(selector.cssName);
        }

        case ByteCode.SEMICOLON, ByteCode.SEMICOLON_OPTIONAL -> {
          appendable.append(';');
          appendable.append(NL);
        }

        case ByteCode.SPACE, ByteCode.SPACE_OPTIONAL -> {
          appendable.append(' ');
        }

        case ByteCode.STRING_LITERAL -> {
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

        case ByteCode.COLOR_HEX,
             ByteCode.STRING_QUOTES_OPTIONAL -> {
          int objectIndex;
          objectIndex = Bytes.decodeIndex2(bytes[index++], bytes[index++]);

          Object object;
          object = objects[objectIndex];

          String s;
          s = (String) object;

          appendable.append(s);
        }

        case ByteCode.URL -> {
          int objectIndex;
          objectIndex = Bytes.decodeIndex2(bytes[index++], bytes[index++]);

          Object object;
          object = objects[objectIndex];

          String s;
          s = (String) object;

          appendable.append("url(\"");
          appendable.append(s);
          appendable.append("\")");
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