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

import java.util.List;
import java.util.Set;

final class CssEngineValueParser {

  private static final byte[] CSS;

  private static final byte CSS_WS = 1;
  private static final byte CSS_ASTERISK = 2;
  private static final byte CSS_HYPHEN = 3;
  private static final byte CSS_COLON = 4;
  private static final byte CSS_SEMICOLON = 5;
  private static final byte CSS_REV_SOLIDUS = 6;
  private static final byte CSS_IDENT_START = 7;
  private static final byte CSS_IDENT = 8;
  private static final byte CSS_NON_ASCII = 9;

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
    table['*'] = CSS_ASTERISK;
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

  private static final Set<String> NAMESPACES = Set.of(
      "breakpoint",
      "color",
      "font"
  );

  int cursor, idx, mark0, mark1;

  String id, ns, varName;

  final StringBuilder sb = new StringBuilder();

  final String text;

  final List<CssEngineValue> values;

  CssEngineValueParser(String text, List<CssEngineValue> values) {
    this.text = text;

    this.values = values;
  }

  static final byte $DECLARATION = 0;

  static final byte $HYPHEN1 = 1;
  static final byte $HYPHEN2 = 2;

  static final byte $VAR_NAME = 3;

  static final byte $COLON = 4;

  static final byte $VALUE = 5;
  static final byte $VALUE_CHAR = 6;
  static final byte $VALUE_WS = 7;

  static final byte $NS_GLOBAL = 8;
  static final byte $NS_VALUE = 9;
  static final byte $NS_VALUE_CHAR = 10;
  static final byte $NS_VALUE_WS = 11;

  final void parse() {
    byte state;
    state = $DECLARATION;

    while (hasNext()) {
      final char c;
      c = next();

      state = switch (state) {
        case $DECLARATION -> parseDeclaration(c);

        case $HYPHEN1 -> parseHyphen1(c);
        case $HYPHEN2 -> parseHyphen2(c);

        case $VAR_NAME -> parseVarName(c);

        case $COLON -> parseColon(c);

        case $VALUE -> parseValue(c);
        case $VALUE_CHAR -> parseValueChar(c);
        case $VALUE_WS -> parseValueWs(c);

        case $NS_GLOBAL -> parseNsGlobal(c);
        case $NS_VALUE -> parseNsValue(c);
        case $NS_VALUE_CHAR -> parseNsValueChar(c);
        case $NS_VALUE_WS -> parseNsValueWs(c);

        default -> throw new AssertionError("Unexpected state=" + state);
      };
    }

    switch (state) {
      case $DECLARATION -> { /* success! */ }

      case $VAR_NAME -> throw error("Unexpected EOF while parsing a custom property name");

      case $COLON -> throw error("Declaration with no ':' colon character");

      case $VALUE, $VALUE_CHAR -> throw error("Unexpected EOF while parsing a declaration value");

      default -> throw error("Unexpected EOF");
    }
  }

  private byte parseDeclaration(char c) {
    return switch (test(c)) {
      case CSS_WS -> $DECLARATION;

      case CSS_HYPHEN -> { mark0 = idx; yield $HYPHEN1; }

      default -> throw error("Expected start of --variable declaration");
    };
  }

  private byte parseHyphen1(char c) {
    return switch (test(c)) {
      case CSS_HYPHEN -> $HYPHEN2;

      default -> throw error("Expected start of --variable declaration");
    };
  }

  private byte parseHyphen2(char c) {
    return switch (test(c)) {
      case CSS_IDENT_START -> { mark1 = idx; yield $VAR_NAME; }

      case CSS_ASTERISK -> { ns = id = "*"; yield $NS_GLOBAL; }

      case CSS_REV_SOLIDUS -> throw error("Escape sequences are currently not supported");

      case CSS_NON_ASCII -> throw error("Non ASCII characters are currently not supported");

      default -> throw error("--variable name must start with a letter");
    };
  }

  private byte parseVarName(char c) {
    return switch (test(c)) {
      case CSS_IDENT_START, CSS_IDENT -> $VAR_NAME;

      case CSS_HYPHEN -> {
        if (ns == null) {
          ns = text.substring(mark1, idx);
        }

        yield $VAR_NAME;
      }

      case CSS_WS -> { parseVarName0(); yield $COLON; }

      case CSS_COLON -> { parseVarName0(); yield $VALUE; }

      default -> throw error("CSS variable name with invalid character=" + c);
    };
  }

  private void parseVarName0() {
    if (ns != null && NAMESPACES.contains(ns)) {
      final int beginIndex;
      beginIndex = mark1 + ns.length() + 1;

      id = text.substring(beginIndex, idx);
    } else {
      ns = null;

      varName = text.substring(mark0, idx);
    }
  }

  private byte parseColon(char c) {
    return switch (test(c)) {
      case CSS_WS -> $COLON;

      case CSS_COLON -> $VALUE;

      default -> throw error("Declaration with no ':' colon character");
    };
  }

  private byte parseValue(char c) {
    return switch (test(c)) {
      case CSS_WS -> $VALUE;

      case CSS_SEMICOLON -> throw error("Declaration with an empty value");

      default -> { sb.setLength(0); sb.append(c); yield $VALUE_CHAR; }
    };
  }

  private byte parseValueChar(char c) {
    return switch (test(c)) {
      case CSS_SEMICOLON -> { parseValue1(); yield $DECLARATION; }

      case CSS_WS -> $VALUE_WS;

      default -> { sb.append(c); yield $VALUE_CHAR; }
    };
  }

  private byte parseValueWs(char c) {
    return switch (test(c)) {
      case CSS_SEMICOLON -> { parseValue1(); yield $DECLARATION; }

      case CSS_WS -> $VALUE_WS;

      default -> { sb.append(' '); sb.append(c); yield $VALUE_CHAR; }
    };
  }

  private void parseValue1() {
    final CssEngineValue result;

    final String value;
    value = sb.toString();

    if (ns != null) {
      result = CssEngineValue.themeVar(ns, id, value);
    } else {
      result = CssEngineValue.customProp(varName, value);
    }

    result(result);
  }

  private byte parseNsGlobal(char c) {
    return switch (test(c)) {
      case CSS_WS -> $NS_GLOBAL;

      case CSS_COLON -> $NS_VALUE;

      default -> throw error("Expected the global namespace '--*'");
    };
  }

  private byte parseNsValue(char c) {
    return switch (test(c)) {
      case CSS_WS -> $NS_VALUE;

      case CSS_IDENT_START -> { sb.setLength(0); sb.append(c); yield $NS_VALUE_CHAR; }

      default -> throw error("Expected the keyword 'initial'");
    };
  }

  private byte parseNsValueChar(char c) {
    return switch (test(c)) {
      case CSS_SEMICOLON -> { parseNsValue1(); yield $DECLARATION; }

      case CSS_WS -> { parseNsValue1(); yield $NS_VALUE_WS; }

      case CSS_IDENT_START -> { sb.append(c); yield $NS_VALUE_CHAR; }

      default -> throw error("Expected the keyword 'initial'");
    };
  }

  private byte parseNsValueWs(char c) {
    return switch (test(c)) {
      case CSS_SEMICOLON -> $DECLARATION;

      case CSS_WS -> $NS_VALUE_WS;

      default -> throw error("Expected the keyword 'initial'");
    };
  }

  private void parseNsValue1() {
    final String initial;
    initial = sb.toString();

    if (!"initial".equals(initial)) {
      throw error("Expected the keyword 'initial' but found '" + initial + "'");
    }

    final CssEngineValue result;
    result = CssEngineValue.themeSkip(ns);

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