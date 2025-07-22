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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

final class TomlParser {

  private enum ParseError {

    NAME_CHAR_INVALID,

    LINE_CHAR_INVALID,

    UNEXPECTED_EOF;

  }

  private byte[] buffer;

  private int bufferIndex, bufferLimit;

  private InputStream inputStream;

  private int int0;

  private Object object0;

  @SuppressWarnings("unused")
  private int posCol, posLine;

  private byte state;

  private byte stateNext;

  TomlParser() {}

  // ##################################################################
  // # BEGIN: Public API
  // ##################################################################

  public final TomlName parseName(String name) {
    final byte[] bytes;
    bytes = name.getBytes(StandardCharsets.UTF_8);

    final byte[] originalBuffer;
    originalBuffer = buffer;

    buffer = bytes;

    bufferIndex = 0;

    bufferLimit = bytes.length;

    inputStream = InputStream.nullInputStream();

    posCol = 0;
    posLine = 1;

    execute($NAME, $NAME_RESULT);

    buffer = originalBuffer;

    return (TomlName) object0;
  }

  // ##################################################################
  // # END: Public API
  // ##################################################################

  // ##################################################################
  // # BEGIN: State Machine
  // ##################################################################

  static final byte $START = 0;

  static final byte $READ = 1;
  static final byte $READ_LINE = 2;
  static final byte $READ_LINE_CR = 3;

  static final byte $NAME = 4;
  static final byte $NAME_BARE = 5;
  static final byte $NAME_DOUBLE = 6;
  static final byte $NAME_SINGLE = 7;
  static final byte $NAME_HEADER = 8;
  static final byte $NAME_ARRAY = 9;
  static final byte $NAME_ARRAY_CLOSE = 10;
  static final byte $NAME_RESULT = 11;

  static final byte $ERROR = 12;

  final void execute(byte from, byte to) {
    state = from;

    while (state < to) {
      execute();
    }
  }

  private void execute() {
    state = switch (state) {
      case $START -> executeStart();

      case $READ -> executeRead();
      case $READ_LINE -> executeReadLine();

      case $NAME -> executeName();
      case $NAME_BARE -> executeNameBare();
      case $NAME_HEADER -> executeNameHeader();
      case $NAME_RESULT -> executeNameResult();

      default -> throw new AssertionError("Unexpected state=" + state);
    };
  }

  // ##################################################################
  // # END: State Machine
  // ##################################################################

  private byte executeStart() {
    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # BEGIN: READ
  // ##################################################################

  private byte executeRead() {
    try {
      final int read;
      read = inputStream.read(buffer, 0, buffer.length);

      if (read != -1) {
        bufferIndex = 0;

        bufferLimit = read;

        return stateNext;
      }

      // EOF reached

      return switch (stateNext) {
        case $READ_LINE -> (byte) int0;

        default -> to(ParseError.UNEXPECTED_EOF);
      };
    } catch (IOException e) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private byte executeReadLine() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex++];

      return switch (b) {
        case Bytes.SP, Bytes.HTAB -> state;

        case Bytes.CR -> $READ_LINE_CR;

        case Bytes.LF -> { posLine++; posCol = 1; yield (byte) int0; }

        default -> to(ParseError.LINE_CHAR_INVALID);
      };
    } else {
      return toRead($READ_LINE);
    }
  }

  private int adv() {
    posCol++;

    return bufferIndex++;
  }

  private String bufferToString() {
    return new String(buffer, int0, bufferIndex, StandardCharsets.UTF_8);
  }

  private byte toRead(byte next) {
    stateNext = next;

    return $READ;
  }

  private byte toReadLine(byte next) {
    int0 = next;

    return $READ_LINE;
  }

  // ##################################################################
  // # BEGIN: END
  // ##################################################################

  // ##################################################################
  // # BEGIN: Name Parsing
  // ##################################################################

  private static final byte[] NAME_TABLE;

  private static final byte NAME_WS = 1;
  private static final byte NAME_BARE = 2;
  private static final byte NAME_BRACKET = 3;
  private static final byte NAME_CLOSE = 4;

  static {
    final byte[] table;
    table = new byte[128];

    table[' '] = NAME_WS;
    table['\t'] = NAME_WS;

    Http.fillTable(table, Ascii.alphaLower(), NAME_BARE);
    Http.fillTable(table, Ascii.alphaUpper(), NAME_BARE);
    Http.fillTable(table, Ascii.digit(), NAME_BARE);

    table['_'] = NAME_BARE;
    table['-'] = NAME_BARE;

    table['['] = NAME_BRACKET;

    table[']'] = NAME_CLOSE;

    NAME_TABLE = table;
  }

  private byte executeName() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return to(ParseError.NAME_CHAR_INVALID);
      }

      final byte code;
      code = NAME_TABLE[b];

      return switch (code) {
        case NAME_WS -> { adv(); yield state; }

        case NAME_BARE -> { int0 = adv(); object0 = TomlName.bare(); yield $NAME_BARE; }

        case NAME_BRACKET -> { adv(); yield $NAME_HEADER; }

        default -> to(ParseError.NAME_CHAR_INVALID);
      };
    } else {
      return toRead($NAME);
    }
  }

  private byte executeNameBare() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return to(ParseError.NAME_CHAR_INVALID);
      }

      final byte code;
      code = NAME_TABLE[b];

      return switch (code) {
        case NAME_WS -> { throw new UnsupportedOperationException("Implement me"); }

        case NAME_BARE -> { adv(); yield $NAME_BARE; }

        case NAME_CLOSE -> {
          final TomlName name;
          name = (TomlName) object0;

          if (name.isKey()) {
            yield to(ParseError.NAME_CHAR_INVALID);
          }

          final String value;
          value = bufferToString();

          name.append(value);

          adv();

          yield name.isHeader() ? toReadLine($NAME_RESULT) : $NAME_ARRAY_CLOSE;
        }

        default -> to(ParseError.NAME_CHAR_INVALID);
      };
    } else {
      return toRead($NAME);
    }
  }

  private byte executeNameHeader() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return to(ParseError.NAME_CHAR_INVALID);
      }

      final byte code;
      code = NAME_TABLE[b];

      return switch (code) {
        case NAME_WS -> { adv(); yield state; }

        case NAME_BARE -> { int0 = adv(); object0 = TomlName.bareHeader(); yield $NAME_BARE; }

        case NAME_BRACKET -> { adv(); yield $NAME_ARRAY; }

        default -> to(ParseError.NAME_CHAR_INVALID);
      };
    } else {
      return toRead($NAME_HEADER);
    }
  }

  private byte executeNameResult() {
    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # END: Name Parsing
  // ##################################################################

  // ##################################################################
  // # BEGIN: Error
  // ##################################################################

  private byte to(ParseError error) {
    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # END: Error
  // ##################################################################

}