/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.http;

import objectos.internal.Ascii;

final class MediaTypeValidator {

  private static final byte[] TABLE;

  private static final byte TFIRST = 1;

  private static final byte TCHARS = 2;

  private static final byte TSLASH = 3;

  private static final byte TSPACE = 4;

  private static final byte TSEMI = 5;

  private static final byte TEQUALS = 6;

  static {
    final byte[] table;
    table = new byte[128];

    Ascii.fill(table, Ascii.alphaLower(), TFIRST);
    Ascii.fill(table, Ascii.alphaUpper(), TFIRST);
    Ascii.fill(table, Ascii.digit(), TFIRST);

    Ascii.fill(table, "!#$&-^_.+", TCHARS);

    table['/'] = TSLASH;
    table[' '] = TSPACE;
    table[';'] = TSEMI;
    table['='] = TEQUALS;

    TABLE = table;
  }

  private enum State {

    START,

    TYPE,

    SLASH,

    SUBTYPE,

    OWS1,

    SEMICOLON,

    OWS2,

    PNAME,

    PEQUALS,

    PVALUE;

  }

  private final String fullType;

  MediaTypeValidator(String fullType) {
    this.fullType = fullType;
  }

  public final void validate() {
    final int length;
    length = validateLength();

    final State state;
    state = validateMain(length);

    validateEnd(state);
  }

  private int validateLength() {
    final int length;
    length = fullType.length();

    if (length == 0) {
      final String msg;
      msg = "Full type must not be empty";

      throw new IllegalArgumentException(msg);
    }

    return length;
  }

  private State validateMain(int length) {
    int index;
    index = 0;

    State state;
    state = State.START;

    while (index < length) {
      final int idx;
      idx = index++;

      final char c;
      c = fullType.charAt(idx);

      if (c >= 128) {
        final String msg;
        msg = "Full type contains the invalid character: 0x%02x".formatted((int) c);

        throw new IllegalArgumentException(msg);
      }

      final byte token;
      token = TABLE[c];

      state = switch (state) {
        case START -> consumeStart(c, token);

        case TYPE -> consumeType(c, token);

        case SLASH -> consumeSlash(c, token);

        case SUBTYPE -> consumeSubtype(c, token);

        case OWS1 -> consumeOws1(c, token);

        case SEMICOLON -> consumeSemicolon(c, token);

        case OWS2 -> consumeOws2(c, token);

        case PNAME -> consumePname(c, token);

        case PEQUALS -> consumePequals(c, token);

        case PVALUE -> consumePvalue(c, token);
      };
    }

    return state;
  }

  private void validateEnd(State state) {
    switch (state) {
      case SUBTYPE, PEQUALS, PVALUE -> {}

      case START, TYPE -> illegal("Full type must contain the '/' character");

      case SLASH -> illegal("Subtype must not be empty");

      case OWS1 -> illegal("Trailing whitespace is not allowed");

      case SEMICOLON, OWS2 -> illegal("Empty parameter declaration");

      case PNAME -> illegal("Missing parameter value declaration");
    }
  }

  private State consumeStart(char c, byte token) {
    return switch (token) {
      case TFIRST -> State.TYPE;

      case TSLASH -> illegal("Type must not be empty");

      default -> illegal("Type must begin with ALPHA or DIGIT, but found '%c'".formatted(c));
    };
  }

  private State consumeType(char c, byte token) {
    return switch (token) {
      case TFIRST, TCHARS -> State.TYPE;

      case TSLASH -> State.SLASH;

      default -> illegal("Type contains the invalid '%c' character".formatted(c));
    };
  }

  private State consumeSlash(char c, byte token) {
    return switch (token) {
      case TFIRST -> State.SUBTYPE;

      default -> illegal("Subtype must begin with ALPHA or DIGIT, but found '%c'".formatted(c));
    };
  }

  private State consumeSubtype(char c, byte token) {
    return switch (token) {
      case TFIRST, TCHARS -> State.SUBTYPE;

      case TSPACE -> State.OWS1;

      case TSEMI -> {
        checkText();

        yield State.SEMICOLON;
      }

      default -> illegal("Subtype contains the invalid '%c' character".formatted(c));
    };
  }

  private State consumeOws1(char c, byte token) {
    return switch (token) {
      case TSPACE -> State.OWS1;

      case TSEMI -> {
        checkText();

        yield State.SEMICOLON;
      }

      default -> illegal("Expected the ';' character but found '%c' instead".formatted(c));
    };
  }

  private State consumeSemicolon(char c, byte token) {
    return switch (token) {
      case TFIRST -> State.PNAME;

      case TSPACE -> State.OWS2;

      default -> illegal("Parameter name must begin with ALPHA or DIGIT, but found '%c'".formatted(c));
    };
  }

  private State consumeOws2(char c, byte token) {
    return switch (token) {
      case TFIRST -> State.PNAME;

      case TSPACE -> State.OWS2;

      default -> illegal("Parameter name must begin with ALPHA or DIGIT, but found '%c'".formatted(c));
    };
  }

  private State consumePname(char c, byte token) {
    return switch (token) {
      case TFIRST, TCHARS -> State.PNAME;

      case TEQUALS -> State.PEQUALS;

      default -> illegal("Parameter name contains the invalid '%c' character".formatted(c));
    };
  }

  private State consumePequals(char c, byte token) {
    return switch (token) {
      case TFIRST, TCHARS -> State.PVALUE;

      case TSPACE -> State.OWS1;

      case TSEMI -> State.SEMICOLON;

      default -> illegal("Parameter value contains the invalid '%c' character".formatted(c));
    };
  }

  private State consumePvalue(char c, byte token) {
    return switch (token) {
      case TFIRST, TCHARS -> State.PVALUE;

      case TSPACE -> State.OWS1;

      case TSEMI -> State.SEMICOLON;

      default -> illegal("Parameter value contains the invalid '%c' character".formatted(c));
    };
  }

  private void checkText() {
    if (!fullType.startsWith("text")) {
      final String msg;
      msg = "Parameters are only allowed on text types";

      throw new IllegalArgumentException(msg);
    }
  }

  private State illegal(String msg) {
    throw new IllegalArgumentException(msg);
  }

}
