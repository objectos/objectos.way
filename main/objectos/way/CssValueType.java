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

  // non-arbitrary

  TOKEN,

  // maybe arbitrary

  TOKEN_ZERO,

  TOKEN_LENGTH,
  TOKEN_LENGTH_NEGATIVE,

  TOKEN_PERCENTAGE,
  TOKEN_PERCENTAGE_NEGATIVE,

  TOKEN_INTEGER,
  TOKEN_INTEGER_NEGATIVE,

  TOKEN_DECIMAL,
  TOKEN_DECIMAL_NEGATIVE,

  // boxed

  BOXED,

  BOXED_ZERO,

  BOXED_LENGTH,
  BOXED_LENGTH_NEGATIVE,

  BOXED_PERCENTAGE,
  BOXED_PERCENTAGE_NEGATIVE,

  BOXED_INTEGER,
  BOXED_INTEGER_NEGATIVE,

  BOXED_DECIMAL,
  BOXED_DECIMAL_NEGATIVE;

  private enum Parser {

    START,

    TOKEN,

    VALUE_START,

    MAYBE_ZERO,

    INTEGER,

    MAYBE_DECIMAL,

    DECIMAL,

    DIMENSION,

    PERCENTAGE,

    MAYBE_BOXED,

    BOXED,

    BOXED_ZERO,

    BOXED_INTEGER,

    BOXED_DECIMAL,

    BOXED_DIMENSION,

    BOXED_PERCENTAGE,

    NOT_BOXED;

  }

  public static CssValueType parse(String s) {
    Parser parser;
    parser = Parser.START;

    boolean boxed;
    boxed = false;

    boolean positive;
    positive = true;

    outer: for (int idx = 0, len = s.length(); idx < len;) {
      char c;
      c = s.charAt(idx++);

      switch (parser) {
        case START -> {
          if (c == '[') {
            boxed = true;

            parser = Parser.VALUE_START;
          }

          else if (c == '0') {
            parser = Parser.MAYBE_ZERO;
          }

          else if (isDigit(c)) {
            parser = Parser.INTEGER;
          }

          else {
            parser = Parser.TOKEN;
          }
        }

        case VALUE_START -> {
          if (c == ']') {
            parser = boxed ? Parser.BOXED : Parser.NOT_BOXED;
          }

          else if (c == '-') {
            if (positive) {
              positive = false;
            } else {
              parser = Parser.TOKEN;
            }
          }

          else if (c == '0') {
            parser = Parser.MAYBE_ZERO;
          }

          else if (isDigit(c)) {
            parser = Parser.INTEGER;
          }

          else {
            parser = boxed ? Parser.MAYBE_BOXED : Parser.TOKEN;
          }
        }

        case MAYBE_ZERO -> {
          if (c == ']') {
            parser = boxed ? Parser.BOXED_ZERO : Parser.NOT_BOXED;
          }

          else if (c == '.') {
            parser = Parser.MAYBE_DECIMAL;
          }

          else if (c == '%') {
            parser = Parser.PERCENTAGE;
          }

          else if (isLetter(c)) {
            parser = Parser.DIMENSION;
          }

          else {
            parser = boxed ? Parser.MAYBE_BOXED : Parser.TOKEN;
          }
        }

        case INTEGER -> {
          if (c == ']') {
            parser = boxed ? Parser.BOXED_INTEGER : Parser.NOT_BOXED;
          }

          else if (c == '.') {
            parser = Parser.MAYBE_DECIMAL;
          }

          else if (c == '%') {
            parser = Parser.PERCENTAGE;
          }

          else if (isLetter(c)) {
            parser = Parser.DIMENSION;
          }

          else if (!isDigit(c)) {
            parser = boxed ? Parser.MAYBE_BOXED : Parser.TOKEN;
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
          if (c == ']') {
            parser = boxed ? Parser.BOXED_DECIMAL : Parser.NOT_BOXED;
          }

          else if (c == '%') {
            parser = Parser.PERCENTAGE;
          }

          else if (isLetter(c)) {
            parser = Parser.DIMENSION;
          }

          else if (!isDigit(c)) {
            parser = boxed ? Parser.MAYBE_BOXED : Parser.TOKEN;
          }

          else {
            parser = Parser.DECIMAL;
          }
        }

        case DIMENSION -> {
          if (c == ']') {
            parser = boxed ? Parser.BOXED_DIMENSION : Parser.TOKEN;
          }

          else if (!isLetter(c)) {
            parser = boxed ? Parser.MAYBE_BOXED : Parser.TOKEN;
          }

          else {
            parser = Parser.DIMENSION;
          }
        }

        case PERCENTAGE -> {
          if (c == ']') {
            parser = boxed ? Parser.BOXED_PERCENTAGE : Parser.TOKEN;
          }

          else {
            parser = Parser.TOKEN;
          }
        }

        case MAYBE_BOXED -> {
          if (c == ']') {
            parser = Parser.BOXED;
          }

          else {
            idx = s.indexOf(']', idx);

            if (idx < 0) {
              parser = Parser.NOT_BOXED;
            }
          }
        }

        case BOXED, BOXED_ZERO, BOXED_INTEGER, BOXED_DECIMAL, BOXED_DIMENSION, BOXED_PERCENTAGE -> {
          // expected end of boxed value but got more chars...
          parser = Parser.NOT_BOXED;
        }

        case TOKEN, NOT_BOXED -> {
          break outer;
        }
      }
    }

    return switch (parser) {
      case START -> CssValueType.TOKEN;

      case TOKEN -> CssValueType.TOKEN;

      case VALUE_START -> CssValueType.TOKEN;

      case MAYBE_ZERO -> boxed ? CssValueType.TOKEN : CssValueType.TOKEN_ZERO;

      case INTEGER -> boxed
          ? CssValueType.TOKEN
          : positive
              ? CssValueType.TOKEN_INTEGER
              : CssValueType.TOKEN_INTEGER_NEGATIVE;

      case MAYBE_DECIMAL -> CssValueType.TOKEN;

      case DECIMAL -> boxed
          ? CssValueType.TOKEN
          : positive
              ? CssValueType.TOKEN_DECIMAL
              : CssValueType.TOKEN_DECIMAL_NEGATIVE;

      case DIMENSION -> boxed
          ? CssValueType.TOKEN
          : positive
              ? CssValueType.TOKEN_LENGTH
              : CssValueType.TOKEN_LENGTH_NEGATIVE;

      case PERCENTAGE -> boxed
          ? CssValueType.TOKEN
          : positive
              ? CssValueType.TOKEN_PERCENTAGE
              : CssValueType.TOKEN_PERCENTAGE_NEGATIVE;

      case MAYBE_BOXED -> CssValueType.TOKEN;

      case NOT_BOXED -> CssValueType.TOKEN;

      case BOXED -> CssValueType.BOXED;

      case BOXED_ZERO -> CssValueType.BOXED_ZERO;

      case BOXED_INTEGER -> positive ? CssValueType.BOXED_INTEGER : CssValueType.BOXED_INTEGER_NEGATIVE;

      case BOXED_DECIMAL -> positive ? CssValueType.BOXED_DECIMAL : CssValueType.BOXED_DECIMAL_NEGATIVE;

      case BOXED_DIMENSION -> positive ? CssValueType.BOXED_LENGTH : CssValueType.BOXED_LENGTH_NEGATIVE;

      case BOXED_PERCENTAGE -> positive ? CssValueType.BOXED_PERCENTAGE : CssValueType.BOXED_PERCENTAGE_NEGATIVE;
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
      case TOKEN,
           TOKEN_ZERO,

           TOKEN_LENGTH,
           TOKEN_LENGTH_NEGATIVE,

           TOKEN_PERCENTAGE,
           TOKEN_PERCENTAGE_NEGATIVE,

           TOKEN_INTEGER,
           TOKEN_INTEGER_NEGATIVE,

           TOKEN_DECIMAL,
           TOKEN_DECIMAL_NEGATIVE -> value;

      case BOXED -> unbox(value).replace('_', ' ');

      case BOXED_ZERO,

           BOXED_LENGTH,
           BOXED_LENGTH_NEGATIVE,

           BOXED_PERCENTAGE,
           BOXED_PERCENTAGE_NEGATIVE,

           BOXED_INTEGER,
           BOXED_INTEGER_NEGATIVE,

           BOXED_DECIMAL,
           BOXED_DECIMAL_NEGATIVE -> unbox(value);
    };
  }

  private String unbox(String value) {
    return value.substring(1, value.length() - 1);
  }

}