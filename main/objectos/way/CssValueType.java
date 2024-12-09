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
package objectos.way;

enum CssValueType {

  STRING,

  ZERO,

  DIMENSION,

  PERCENTAGE,

  INTEGER,

  DECIMAL;

  private enum Parser {

    START,

    TOKEN,

    MAYBE_ZERO,

    INTEGER,

    MAYBE_DECIMAL,

    DECIMAL,

    DIMENSION,

    PERCENTAGE;

  }

  public static CssValueType parse(String s) {
    Parser parser;
    parser = Parser.START;

    outer: for (int idx = 0, len = s.length(); idx < len;) {
      char c;
      c = s.charAt(idx++);

      switch (parser) {
        case START -> {
          if (c == '0') {
            parser = Parser.MAYBE_ZERO;
          }

          else if (isDigit(c)) {
            parser = Parser.INTEGER;
          }

          else {
            parser = Parser.TOKEN;
          }
        }

        case MAYBE_ZERO -> {
          if (c == '.') {
            parser = Parser.MAYBE_DECIMAL;
          }

          else if (c == '%') {
            parser = Parser.PERCENTAGE;
          }

          else if (isLetter(c)) {
            parser = Parser.DIMENSION;
          }

          else {
            parser = Parser.TOKEN;
          }
        }

        case INTEGER -> {
          if (c == '.') {
            parser = Parser.MAYBE_DECIMAL;
          }

          else if (c == '%') {
            parser = Parser.PERCENTAGE;
          }

          else if (isLetter(c)) {
            parser = Parser.DIMENSION;
          }

          else if (!isDigit(c)) {
            parser = Parser.TOKEN;
          }

          else {
            parser = Parser.INTEGER;
          }
        }

        case MAYBE_DECIMAL -> {
          if (isDigit(c)) {
            parser = Parser.DECIMAL;
          }

          else {
            parser = Parser.TOKEN;
          }
        }

        case DECIMAL -> {
          if (c == '%') {
            parser = Parser.PERCENTAGE;
          }

          else if (isLetter(c)) {
            parser = Parser.DIMENSION;
          }

          else if (!isDigit(c)) {
            parser = Parser.TOKEN;
          }

          else {
            parser = Parser.DECIMAL;
          }
        }

        case DIMENSION -> {
          if (!isLetter(c)) {
            parser = Parser.TOKEN;
          }

          else {
            parser = Parser.DIMENSION;
          }
        }

        case PERCENTAGE -> {
          parser = Parser.TOKEN;
        }

        case TOKEN -> {
          break outer;
        }
      }
    }

    return switch (parser) {
      case START -> CssValueType.STRING;

      case TOKEN -> CssValueType.STRING;

      case MAYBE_ZERO -> CssValueType.ZERO;

      case INTEGER -> CssValueType.INTEGER;

      case MAYBE_DECIMAL -> CssValueType.STRING;

      case DECIMAL -> CssValueType.DECIMAL;

      case DIMENSION -> CssValueType.DIMENSION;

      case PERCENTAGE -> CssValueType.PERCENTAGE;
    };
  }

  private static boolean isDigit(char c) {
    return '0' <= c && c <= '9';
  }

  private static boolean isLetter(char c) {
    return 'A' <= c && c <= 'Z'
        || 'a' <= c && c <= 'z';
  }

  final String get(String value) {
    return switch (this) {
      case STRING -> value.replace('_', ' ');

      case ZERO,

           DIMENSION,

           PERCENTAGE,

           INTEGER,

           DECIMAL -> value;
    };
  }

}