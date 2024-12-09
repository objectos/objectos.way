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

  LENGTH_PX,

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

    MAYBE_LENGTH_PX,

    LENGTH_PX,

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

          else if (c == 'p') {
            parser = Parser.MAYBE_LENGTH_PX;
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

          else if (c == 'p') {
            parser = Parser.MAYBE_LENGTH_PX;
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

          else if (c == 'p') {
            parser = Parser.MAYBE_LENGTH_PX;
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

        case MAYBE_LENGTH_PX -> {
          if (c == 'x') {
            parser = Parser.LENGTH_PX;
          }

          else {
            parser = Parser.DIMENSION;
          }
        }

        case LENGTH_PX -> {
          parser = Parser.DIMENSION;
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

      case MAYBE_LENGTH_PX -> CssValueType.DIMENSION;

      case LENGTH_PX -> CssValueType.LENGTH_PX;

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

  public final String get(String value) {
    return switch (this) {
      case STRING -> value.replace('_', ' ');

      case LENGTH_PX -> {
        // we assume parse was invoked prior to invoking this method...

        // accumulate all of the digits here
        long digits;
        digits = 0L;

        // let's keep track of now many digits before the '.' we have found
        int integerCount = 0;

        // we need to know if we are before or after the '.'
        boolean dot;
        dot = false;

        // let's keep track of how many digits after the '.' we have found
        int decimalCount;
        decimalCount = 0;

        // we process all digits and stop before the trailing 'px'
        int maxLen;
        maxLen = value.length() - 2;

        for (int idx = 0; idx < maxLen; idx++) {
          char charValue;
          charValue = value.charAt(idx);

          if (!dot && charValue == '.') {
            dot = true;

            continue;
          }

          if (dot) {
            decimalCount++;
          } else {
            integerCount++;
          }

          long longValue;
          longValue = charValue - '0';

          digits *= 10;

          digits += longValue;
        }

        // don't convert 0px
        if (digits == 0) {
          yield value;
        }

        int scale;
        scale = Math.max(decimalCount, 5);

        double dividend;
        dividend = digits;

        if (scale > decimalCount) {
          dividend *= Math.pow(10D, scale - decimalCount);
        }

        // round up if necessary...
        dividend += 5;

        double multiplier;
        multiplier = Math.pow(10D, scale);

        double divisor;
        divisor = 16L * multiplier;

        double result;
        result = dividend / divisor;

        long unscaled;
        unscaled = (long) (result * multiplier);

        StringBuilder out;
        out = new StringBuilder();

        // print the decimal digits

        boolean append;
        append = false;

        for (int idx = 0; idx < scale; idx++) {
          int thisDigit;
          thisDigit = (int) (unscaled % 10L);

          unscaled /= 10L;

          if (thisDigit != 0) {
            append = true;
          }

          if (!append) {
            continue;
          }

          out.append(thisDigit);
        }

        if (append) {
          out.append('.');
        }

        // print the integer digits

        int nonZeroCount;
        nonZeroCount = 0;

        int zeroCount;
        zeroCount = 0;

        for (int idx = 0, max = integerCount; idx < max; idx++) {
          int thisDigit;
          thisDigit = (int) (unscaled % 10L);

          if (thisDigit != 0) {
            nonZeroCount++;

            zeroCount = 0;
          } else {
            zeroCount++;
          }

          unscaled /= 10L;

          out.append(thisDigit);
        }

        if (zeroCount > 1 && nonZeroCount == 0) {
          int length;
          length = out.length();

          out.setLength(length - zeroCount + 1);
        }

        else if (zeroCount > 0 && nonZeroCount > 0) {
          int length;
          length = out.length();

          out.setLength(length - zeroCount);
        }

        out.reverse();

        out.append("rem");

        yield out.toString();
      }

      case ZERO,

           DIMENSION,

           PERCENTAGE,

           INTEGER,

           DECIMAL -> value;
    };
  }

  public final boolean isLength() {
    return switch (this) {
      case LENGTH_PX, DIMENSION -> true;

      default -> false;
    };
  }

}