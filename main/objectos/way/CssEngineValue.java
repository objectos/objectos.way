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
        final char c;
        c = next();

        if (Ascii.isWhitespace(c)) {
          // trim leading ws
          continue;
        }

        reset();

        parseNormal(c);
      }

      return values;
    }

    private void reset() {
      mark0 = mark1 = 0;

      id = ns = varName = null;
    }

    private void parseNormal(char c) {
      if (c == '-') {
        mark0 = idx; // mark0 = hyphen

        parseHyphen1(next());
      }

      else {
        throw error("Expected start of --variable declaration");
      }
    }

    private void parseHyphen1(char c) {
      if (c == '-') {
        parseHyphen2(next());
      }

      else {
        throw error("Expected start of --variable declaration");
      }
    }

    private void parseHyphen2(char c) {
      if (Ascii.isLetter(c)) {
        mark1 = idx; // mark1 = var first letter

        parseVarName();
      }

      else {
        throw error("--variable name must start with a letter");
      }
    }

    private void parseVarName() {
      while (hasNext()) {
        final char c;
        c = next();

        if (Ascii.isLetterOrDigit(c)) {
          continue;
        }

        else if (c == '-') {
          if (ns == null) {
            ns = text.substring(mark1, idx);
          }

          continue;
        }

        else if (Ascii.isWhitespace(c)) {
          parseVarName0();

          throw new UnsupportedOperationException("Implement me");
        }

        else if (c == ':') {
          parseVarName0();

          parseValue();

          return;
        }

        else {
          throw error("CSS variable name with invalid character=" + c);
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

    private void parseValue() {
      while (hasNext()) {
        final char c;
        c = next();

        if (Ascii.isWhitespace(c)) {
          // trim leading ws
          continue;
        }

        else if (c == ';') {
          throw error("Declaration with an empty value");
        }

        else {
          sb.setLength(0);

          sb.append(c);

          parseValue0();

          return;
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

        if (c == ';') {
          parseValue1();

          return;
        }

        else if (Ascii.isWhitespace(c)) {
          ws = true;
        }

        else {
          if (ws) {
            sb.append(' ');

            ws = false;
          }

          sb.append(c);
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

      values.add(result);
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

  }

  public static List<CssEngineValue> parse(String text) {
    final Parser parser;
    parser = new Parser(text);

    return parser.parse();
  }

  // ##################################################################
  // # END: Parse
  // ##################################################################

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof CssEngineValue that
        && Objects.equals(value, that.value);
  }

}
