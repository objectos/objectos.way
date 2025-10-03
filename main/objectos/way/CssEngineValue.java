/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

final class CssEngineValue {

  private static final Set<String> NAMESPACES = Set.of(
      "breakpoint",
      "color",
      "font"
  );

  private final Value value;

  private CssEngineValue(Value value) {
    this.value = value;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof CssEngineValue that
        && Objects.equals(value, that.value);
  }

  @Override
  public final String toString() {
    return "CssEngineValue[value=" + value + "]";
  }

  // ##################################################################
  // # BEGIN: Value
  // ##################################################################

  private sealed interface Value {}

  private record ThemeVar(String ns, String id, String value) implements Value {}

  static CssEngineValue themeVar(String ns, String id, String value) {
    return new CssEngineValue(
        new ThemeVar(ns, id, value)
    );
  }

  // ##################################################################
  // # END: Value
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse
  // ##################################################################

  private static final byte[] CSS;

  private static final byte CSS_WS = 1;
  private static final byte CSS_HYPHEN = 2;
  private static final byte CSS_COLON = 3;
  private static final byte CSS_SEMICOLON = 4;
  private static final byte CSS_REV_SOLIDUS = 5;
  private static final byte CSS_IDENT_START = 6;
  private static final byte CSS_IDENT = 7;
  private static final byte CSS_NON_ASCII = 8;

  static {
    final byte[] table;
    table = new byte[128];

    // start with invalid

    // https://www.w3.org/TR/2021/CRD-css-syntax-3-20211224/#whitespace
    table['\n'] = CSS_WS;
    table['\t'] = CSS_WS;
    table[' '] = CSS_WS;

    // https://www.w3.org/TR/2021/CRD-css-syntax-3-20211224/#input-preprocessing
    table['\f'] = CSS_WS;
    table['\r'] = CSS_WS;

    // symbols
    table['-'] = CSS_HYPHEN;
    table[':'] = CSS_COLON;
    table[';'] = CSS_SEMICOLON;
    table['\\'] = CSS_REV_SOLIDUS;

    // ident start
    Ascii.fill(table, Ascii.alphaLower(), CSS_IDENT_START);
    Ascii.fill(table, Ascii.alphaUpper(), CSS_IDENT_START);
    table['_'] = CSS_IDENT_START;

    // ident
    Ascii.fill(table, Ascii.digit(), CSS_IDENT);

    CSS = table;
  }

  private static final class Parser {

    int cursor, idx, mark0, mark1;

    String id, ns, varName;

    final StringBuilder sb = new StringBuilder();

    final String text;

    final List<CssEngineValue> values = new ArrayList<>();

    Parser(String text) {
      this.text = text;
    }

    final List<CssEngineValue> parse() {
      while (hasNext()) {
        parseDeclaration(next());
      }

      return values;
    }

    private void parseDeclaration(char c) {
      switch (test(c)) {
        case CSS_WS -> {}

        case CSS_HYPHEN -> {
          mark0 = idx; // mark0 = hyphen

          parseHyphen1(next());
        }

        default -> throw error("Expected start of --variable declaration");
      }
    }

    private void parseHyphen1(char c) {
      switch (test(c)) {
        case CSS_HYPHEN -> parseHyphen2(next());

        default -> throw error("Expected start of --variable declaration");
      }
    }

    private void parseHyphen2(char c) {
      switch (test(c)) {
        case CSS_IDENT_START -> {
          mark1 = idx; // mark1 = var first letter

          parseVarName();
        }

        case CSS_REV_SOLIDUS -> throw error("Escape sequences are currently not supported");

        case CSS_NON_ASCII -> throw error("Non ASCII characters are currently not supported");

        default -> throw error("--variable name must start with a letter");
      }
    }

    private void parseVarName() {
      while (hasNext()) {
        final char c;
        c = next();

        switch (test(c)) {
          case CSS_IDENT_START, CSS_IDENT -> {
            continue;
          }

          case CSS_HYPHEN -> {
            if (ns == null) {
              ns = text.substring(mark1, idx);
            }

            continue;
          }

          case CSS_WS -> {
            parseVarName0();

            parseColon();

            parseValue();

            return;
          }

          case CSS_COLON -> {
            parseVarName0();

            parseValue();

            return;
          }

          default -> throw error("CSS variable name with invalid character=" + c);
        }
      }

      throw new UnsupportedOperationException("Implement me");
    }

    private void parseVarName0() {
      if (ns != null && NAMESPACES.contains(ns)) {
        final int beginIndex;
        beginIndex = mark1 + ns.length() + 1;

        id = text.substring(beginIndex, idx);
      } else {
        ns = null;

        varName = text.substring(mark0, idx);

        throw new UnsupportedOperationException("Implement me :: varName=" + varName);
      }
    }

    private void parseColon() {
      while (hasNext()) {
        final char c;
        c = next();

        switch (test(c)) {
          case CSS_WS -> {
            continue;
          }

          case CSS_COLON -> {
            return;
          }

          default -> {
            break;
          }
        }
      }

      throw error("Declaration with no ':' colon character");
    }

    private void parseValue() {
      while (hasNext()) {
        final char c;
        c = next();

        switch (test(c)) {
          case CSS_WS -> {
            continue;
          }

          case CSS_SEMICOLON -> throw error("Declaration with an empty value");

          default -> {
            sb.setLength(0);

            sb.append(c);

            parseValue0();

            return;
          }
        }
      }

      throw new UnsupportedOperationException("Implement me");
    }

    private void parseValue0() {
      boolean ws;
      ws = false;

      while (hasNext()) {
        final char c;
        c = next();

        switch (test(c)) {
          case CSS_SEMICOLON -> {
            parseValue1();

            return;
          }

          case CSS_WS -> ws = true;

          default -> {
            if (ws) {
              sb.append(' ');

              ws = false;
            }

            sb.append(c);
          }
        }
      }

      throw new UnsupportedOperationException("Implement me");
    }

    private void parseValue1() {
      final CssEngineValue result;

      final String value;
      value = sb.toString();

      if (ns != null) {
        result = themeVar(ns, id, value);
      } else {
        throw new UnsupportedOperationException("Implement me");
      }

      result(result);
    }

    private IllegalArgumentException error(String message) {
      return new IllegalArgumentException(message);
    }

    private boolean hasNext() {
      return cursor < text.length();
    }

    private char next() {
      idx = cursor++;

      return text.charAt(idx);
    }

    private byte test(char c) {
      return c < 128 ? CSS[c] : CSS_NON_ASCII;
    }

    private void result(CssEngineValue result) {
      values.add(result);

      mark0 = mark1 = 0;

      id = ns = varName = null;
    }

  }

  public static List<CssEngineValue> parse(String text) {
    final Parser parser;
    parser = new Parser(text);

    return parser.parse();
  }

  // ##################################################################
  // # END: Parse
  // ##################################################################

}
