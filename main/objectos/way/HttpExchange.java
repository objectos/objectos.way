/*
 * Copyright (C) 2016-2025 Objectos Software LTDA.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import objectos.way.Http.Status;

@SuppressWarnings("serial")
final class HttpExchange implements Http.Exchange, Closeable {

  private record Notes(
      Note.Long1Ref1<InetAddress> start,

      Note.Long2 readResize,
      Note.Long1Ref1<Http.Exchange> readEof,
      Note.Long1Ref1<Http.Exchange> readMaxBuffer,
      Note.Long1Ref2<Http.Exchange, IOException> readIOException,

      Note.Long1Ref2<ClientError, Http.Exchange> badRequest,
      Note.Long1Ref1<Http.Exchange> uriTooLong,
      Note.Long1Ref1<Http.Exchange> notImplemented,
      Note.Long1Ref1<Http.Exchange> httpVersionNotSupported,

      Note.Ref2<String, String> hexdump,
      Note.Ref2<Integer, String> invalidRequestLine
  ) {

    static Notes get() {
      final Class<?> s;
      s = Http.Exchange.class;

      return new Notes(
          Note.Long1Ref1.create(s, "STA", Note.DEBUG),

          Note.Long2.create(s, "RSZ", Note.INFO),
          Note.Long1Ref1.create(s, "EOF", Note.WARN),
          Note.Long1Ref1.create(s, "MAX", Note.WARN),
          Note.Long1Ref2.create(s, "IOX", Note.ERROR),

          Note.Long1Ref2.create(s, "400", Note.INFO),
          Note.Long1Ref1.create(s, "414", Note.INFO),
          Note.Long1Ref1.create(s, "501", Note.INFO),
          Note.Long1Ref1.create(s, "505", Note.INFO),

          Note.Ref2.create(s, "HEX", Note.ERROR),
          Note.Ref2.create(s, "IRL", Note.ERROR)
      );
    }

  }

  private sealed interface ClientError {
    byte[] message();

    Http.Status status();
  }

  private enum InvalidLineTerminator implements ClientError {
    INSTANCE;

    private static final byte[] MESSAGE = "Invalid line terminator.\n".getBytes(StandardCharsets.US_ASCII);

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final Status status() {
      return Http.Status.BAD_REQUEST;
    }
  }

  private enum InvalidRequestLine implements ClientError {
    // do not reorder, do not rename

    // invalid method
    METHOD,

    // path does not start with solidus
    PATH_FIRST_CHAR,

    // path starts with two consecutive '/'
    PATH_SEGMENT_NZ,

    // path has an invalid character
    PATH_NEXT_CHAR,

    // path has an invalid percent encoded sequence
    PATH_PERCENT,

    // query has an invalid character
    QUERY_CHAR,

    // query has an invalid percent encoded sequence
    QUERY_PERCENT,

    // invalid version
    VERSION_CHAR,

    REQUEST_TARGET_EOF,

    REQUEST_TARGET_FORM;

    private static final byte[] MESSAGE = "Invalid request line.\n".getBytes(StandardCharsets.US_ASCII);

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final Http.Status status() {
      return Http.Status.BAD_REQUEST;
    }

    final ClientErrorException create() {
      return new ClientErrorException(this);
    }
  }

  static final byte $START = 0;

  static final byte $READ = 1;
  static final byte $READ_MAX_BUFFER = 2;
  static final byte $READ_EOF = 3;

  static final byte $PARSE_METHOD = 4;
  static final byte $PARSE_PATH = 5;
  static final byte $PARSE_PATH_CONTENTS0 = 6;
  static final byte $PARSE_PATH_CONTENTS1 = 7;
  static final byte $PARSE_PATH_DECODE = 8;
  static final byte $PARSE_QUERY = 9;
  static final byte $PARSE_QUERY0 = 10;
  static final byte $PARSE_QUERY1 = 11;
  static final byte $PARSE_QUERY1_DECODE = 12;
  static final byte $PARSE_QUERY_VALUE = 13;
  static final byte $PARSE_QUERY_VALUE0 = 14;
  static final byte $PARSE_QUERY_VALUE1 = 15;
  static final byte $PARSE_QUERY_VALUE1_DECODE = 16;
  static final byte $PARSE_VERSION_0_9 = 17;
  static final byte $PARSE_VERSION_1_1 = 18;
  static final byte $PARSE_VERSION_OTHERS = 19;

  static final byte $PARSE_HEADER = 20;

  static final byte $BAD_REQUEST = 21;
  static final byte $URI_TOO_LONG = 22;
  static final byte $NOT_IMPLEMENTED = 23;
  static final byte $HTTP_VERSION_NOT_SUPPORTED = 24;

  static final byte $OK = 25;
  static final byte $ERROR = 26;

  private static final int HARD_MAX_BUFFER_SIZE = 1 << 14;

  private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

  private static final Notes NOTES = Notes.get();

  private byte[] buffer;

  private int bufferIndex = 0;

  private int bufferLimit = 0;

  private final long id = ID_GENERATOR.getAndIncrement();

  private final InputStream inputStream;

  private int mark;

  private final int maxBufferSize;

  private Http.Method method;

  private final Note.Sink noteSink;

  private Object object;

  private InetAddress remoteAddress;

  private String path;

  private Map<String, Object> queryParams;

  private byte state;

  private byte stateNext;

  private StringBuilder stringBuilder;

  private Http.Version version = Http.Version.HTTP_1_1;

  // ##################################################################
  // # BEGIN: HTTP/1.1 state machine
  // ##################################################################

  public final boolean shouldHandle() throws IOException {
    final Thread currentThread;
    currentThread = Thread.currentThread();

    if (currentThread.isInterrupted()) {
      return false;
    }

    while (state < $OK) {
      state = execute(state);
    }

    return state == $OK;
  }

  @Lang.VisibleForTesting
  final byte execute(byte state) {
    return switch (state) {
      case $START -> executeStart();

      case $READ -> executeRead();
      case $READ_EOF -> executeReadEof();
      case $READ_MAX_BUFFER -> executeReadMaxBuffer();

      case $PARSE_METHOD -> executeParseMethod();
      case $PARSE_PATH -> executeParsePath();
      case $PARSE_PATH_CONTENTS0 -> executeParsePathContents0();
      case $PARSE_PATH_CONTENTS1 -> executeParsePathContents1();
      case $PARSE_PATH_DECODE -> executeParsePathDecode();

      case $PARSE_QUERY -> executeParseQuery();
      case $PARSE_QUERY0 -> executeParseQuery0();
      case $PARSE_QUERY1 -> executeParseQuery1();
      case $PARSE_QUERY1_DECODE -> executeParseQuery1Decode();
      case $PARSE_QUERY_VALUE -> executeParseQueryValue();
      case $PARSE_QUERY_VALUE0 -> executeParseQueryValue0();
      case $PARSE_QUERY_VALUE1 -> executeParseQueryValue1();
      case $PARSE_QUERY_VALUE1_DECODE -> executeParseQueryValue1Decode();

      case $PARSE_VERSION_0_9 -> executeParseVersion_0_9();
      case $PARSE_VERSION_1_1 -> executeParseVersion_1_1();
      case $PARSE_VERSION_OTHERS -> executeParseVersionOthers();

      case $PARSE_HEADER -> executeParseHeader();

      case $BAD_REQUEST -> executeBadRequest();
      case $URI_TOO_LONG -> executeUriTooLong();
      case $NOT_IMPLEMENTED -> executeNotImplemented();
      case $HTTP_VERSION_NOT_SUPPORTED -> executeHttpVersionNotSupported();

      default -> throw new AssertionError("Unexpected state=" + state);
    };
  }

  private byte executeStart() {
    noteSink.send(NOTES.start, id, remoteAddress);

    bufferLimit = 0;

    bufferIndex = 0;

    mark = 0;

    method = null;

    object = null;

    path = null;

    stateNext = 0;

    if (stringBuilder != null) {
      stringBuilder.setLength(0);
    }

    if (queryParams != null) {
      queryParams.clear();
    }

    version = Http.Version.HTTP_1_1;

    return $PARSE_METHOD;
  }

  // ##################################################################
  // # END: HTTP/1.1 state machine
  // ##################################################################

  // ##################################################################
  // # BEGIN: Read
  // ##################################################################

  private byte executeRead() {
    try {
      int writableLength;
      writableLength = buffer.length - bufferLimit;

      if (writableLength == 0) {
        // buffer is full, try to increase

        if (buffer.length == maxBufferSize) {
          return $READ_MAX_BUFFER;
        }

        int newLength;
        newLength = buffer.length << 1;

        buffer = Arrays.copyOf(buffer, newLength);

        writableLength = buffer.length - bufferLimit;

        noteSink.send(NOTES.readResize, id, newLength);
      }

      int bytesRead;
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);

      if (bytesRead < 0) {
        return $READ_EOF;
      }

      bufferLimit += bytesRead;

      return stateNext;
    } catch (IOException e) {
      noteSink.send(NOTES.readIOException, id, this, e);

      return $ERROR;
    }
  }

  private byte executeReadMaxBuffer() {
    return switch (stateNext) {
      case $PARSE_PATH, $PARSE_PATH_CONTENTS0, $PARSE_PATH_CONTENTS1, $PARSE_PATH_DECODE,
           $PARSE_QUERY, $PARSE_QUERY0, $PARSE_QUERY1,
           $PARSE_QUERY_VALUE, $PARSE_QUERY_VALUE0, $PARSE_QUERY_VALUE1 -> executeUriTooLong();

      default -> { note(NOTES.readMaxBuffer); yield $ERROR; }
    };
  }

  private byte executeReadEof() {
    return switch (stateNext) {
      case $PARSE_METHOD -> bufferLimit == 0 ? $OK : toBadRequest(InvalidRequestLine.METHOD);

      default -> { note(NOTES.readEof); yield $ERROR; }
    };
  }

  private boolean bufferMatches(byte[] bytes) {
    final int length;
    length = bytes.length;

    final int toIndex;
    toIndex = bufferIndex + length;

    final boolean matches;
    matches = Arrays.equals(
        buffer, bufferIndex, toIndex,
        bytes, 0, length
    );

    if (matches) {
      bufferIndex += length;

      return true;
    } else {
      return false;
    }
  }

  @Lang.VisibleForTesting
  final String bufferToAscii() {
    return new String(buffer, StandardCharsets.US_ASCII);
  }

  private boolean canRead(int count) {
    final int readable;
    readable = bufferLimit - bufferIndex;

    return count <= readable;
  }

  @Lang.VisibleForTesting
  final byte toRead(byte next) {
    stateNext = next;

    return $READ;
  }

  // ##################################################################
  // # END: Read
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Method
  // ##################################################################

  private enum $Method {
    CONNECT,
    DELETE,
    GET,
    HEAD,
    OPTIONS,
    PATCH,
    POST,
    PUT,
    TRACE;

    final byte[] ascii;

    final Http.Method external;

    private $Method() {
      final String name;
      name = name();

      ascii = (name + ' ').getBytes(StandardCharsets.US_ASCII);

      final Http.Method value;
      value = Http.Method.valueOf(name);

      external = value.implemented ? value : null;
    }
  }

  private byte executeParseMethod() {
    if (bufferIndex < bufferLimit) {

      final byte first;
      first = buffer[bufferIndex];

      // based on the first char, we select out method candidate

      return switch (first) {
        case 'C' -> executeParseMethod($Method.CONNECT);

        case 'D' -> executeParseMethod($Method.DELETE);

        case 'G' -> executeParseMethod($Method.GET);

        case 'H' -> executeParseMethod($Method.HEAD);

        case 'O' -> executeParseMethod($Method.OPTIONS);

        case 'P' -> executeParseMethodP();

        case 'T' -> executeParseMethod($Method.TRACE);

        default -> toBadRequest(InvalidRequestLine.METHOD);
      };

    } else {
      return toRead($PARSE_METHOD);
    }
  }

  private byte executeParseMethod($Method candidate) {
    final byte[] ascii;
    ascii = candidate.ascii;

    if (canRead(ascii.length)) {

      if (bufferMatches(ascii)) {

        method = candidate.external;

        if (method == null) {
          return $NOT_IMPLEMENTED;
        } else {
          return $PARSE_PATH;
        }

      } else {
        return toBadRequest(InvalidRequestLine.METHOD);
      }

    } else {
      return toRead($PARSE_METHOD);
    }
  }

  private byte executeParseMethodP() {
    final int secondIndex;
    secondIndex = bufferIndex + 1;

    if (secondIndex < bufferLimit) {
      final byte second;
      second = buffer[secondIndex];

      return switch (second) {
        case 'O' -> executeParseMethod($Method.POST);

        case 'U' -> executeParseMethod($Method.PUT);

        case 'A' -> executeParseMethod($Method.PATCH);

        default -> toBadRequest(InvalidRequestLine.METHOD);
      };
    } else {
      return toRead($PARSE_METHOD);
    }
  }

  private $Method parseMethod($Method candidate) {
    final byte[] ascii;
    ascii = candidate.ascii;

    return bufferMatches(ascii) ? candidate : null;
  }

  // ##################################################################
  // # END: Parse: Method
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Path
  // ##################################################################

  private static final byte[] PARSE_PATH_TABLE;

  private static final byte SOLIDUS = '/';

  private static final byte PATH_VALID = 1;
  private static final byte PATH_PERCENT = 2;
  private static final byte PATH_SPACE = 3;
  private static final byte PATH_QUESTION = 4;
  private static final byte PATH_CRLF = 5;

  static {
    final byte[] table;
    table = new byte[128];

    // 0 = invalid
    // 1 = valid
    // 2 = %xx
    // 3 = ' ' -> version
    // 4 = '?' -> stop
    // 5 = '\r' -> 0.9
    // 5 = '\n' -> 0.9

    Http.fillTable(table, Http.unreserved(), PATH_VALID);

    Http.fillTable(table, Http.subDelims(), PATH_VALID);

    table[':'] = PATH_VALID;

    table['@'] = PATH_VALID;

    // solidus acts as segment separator
    table[SOLIDUS] = PATH_VALID;

    table['%'] = PATH_PERCENT;

    table[' '] = PATH_SPACE;

    table['?'] = PATH_QUESTION;

    table['\r'] = PATH_CRLF;

    table['\n'] = PATH_CRLF;

    PARSE_PATH_TABLE = table;
  }

  private byte executeParsePath() {
    if (canRead(2)) {
      // marks path start
      mark = bufferIndex;

      final byte first;
      first = buffer[bufferIndex++];

      if (first != SOLIDUS) {
        return toBadRequest(InvalidRequestLine.PATH_FIRST_CHAR);
      }

      final byte second;
      second = buffer[bufferIndex]; // do not advance, we still need to do more checks

      if (second == SOLIDUS) {
        return toBadRequest(InvalidRequestLine.PATH_SEGMENT_NZ);
      }

      return executeParsePathContents0();
    } else {
      return toRead($PARSE_PATH);
    }
  }

  private byte executeParsePathContents0() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR);
      }

      final byte code;
      code = PARSE_PATH_TABLE[b];

      switch (code) {
        case PATH_VALID -> { bufferIndex += 1; }

        case PATH_PERCENT -> { appendInit(); return $PARSE_PATH_CONTENTS1; }

        case PATH_SPACE -> { path = markToString(); bufferIndex += 1; return $PARSE_VERSION_1_1; }

        case PATH_QUESTION -> { path = markToString(); bufferIndex += 1; return $PARSE_QUERY; }

        case PATH_CRLF -> { path = markToString(); bufferIndex += 1; return $PARSE_VERSION_0_9; }

        default -> { return toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR); }
      }
    }

    return toRead($PARSE_PATH_CONTENTS0);
  }

  private byte executeParsePathContents1() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR);
      }

      final byte code;
      code = PARSE_PATH_TABLE[b];

      switch (code) {
        case PATH_VALID -> { appendChar(b); bufferIndex += 1; }

        case PATH_PERCENT -> { byte next = executeParsePathDecode(); if (state != next) { return next; } }

        case PATH_SPACE -> { path = appendToString(); bufferIndex += 1; return $PARSE_VERSION_1_1; }

        case PATH_QUESTION -> { path = appendToString(); bufferIndex += 1; return $PARSE_QUERY; }

        case PATH_CRLF -> { path = appendToString(); bufferIndex += 1; return $PARSE_VERSION_0_9; }

        default -> { return toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR); }
      }
    }

    return toRead($PARSE_PATH_CONTENTS1);
  }

  private byte executeParsePathDecode() {
    return decodePercent($PARSE_PATH_CONTENTS1, $PARSE_PATH_DECODE, InvalidRequestLine.PATH_PERCENT);
  }

  // ##################################################################
  // # END: Parse: Path
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Query
  // ##################################################################

  private static final byte[] PARSE_QUERY_TABLE;

  private static final byte QUERY_VALID = 1;
  private static final byte QUERY_PERCENT = 2;
  private static final byte QUERY_PLUS = 3;
  private static final byte QUERY_EQUALS = 4;
  private static final byte QUERY_AMPERSAND = 5;
  private static final byte QUERY_SPACE = 6;
  private static final byte QUERY_CRLF = 7;

  static {
    final byte[] table;
    table = new byte[128];

    // 0 = invalid
    // 1 = valid
    // 2 = %xx
    // 3 = '+' -> SPACE
    // 4 = '=' -> key/value separator
    // 5 = '&' -> next key
    // 6 = ' ' -> space
    // 7 = '\r' -> 0.9
    // 7 = '\n' -> 0.9

    Http.fillTable(table, Http.unreserved(), QUERY_VALID);

    Http.fillTable(table, Http.subDelims(), QUERY_VALID);

    table[':'] = QUERY_VALID;

    table['@'] = QUERY_VALID;

    table['/'] = QUERY_VALID;

    table['?'] = QUERY_VALID;

    table['%'] = QUERY_PERCENT;

    table['+'] = QUERY_PLUS;

    table['='] = QUERY_EQUALS;

    table['&'] = QUERY_AMPERSAND;

    table[' '] = QUERY_SPACE;

    table['\r'] = QUERY_CRLF;

    table['\n'] = QUERY_CRLF;

    PARSE_QUERY_TABLE = table;
  }

  private byte executeParseQuery() {
    // where the current key begins
    mark = bufferIndex;

    return executeParseQuery0();
  }

  private byte executeParseQuery0() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestLine.QUERY_CHAR);
      }

      final byte code;
      code = PARSE_QUERY_TABLE[b];

      switch (code) {
        case QUERY_VALID -> { bufferIndex += 1; }

        case QUERY_PERCENT, QUERY_PLUS -> { appendInit(); return $PARSE_QUERY1; }

        case QUERY_EQUALS -> { object = markToString(); bufferIndex += 1; return $PARSE_QUERY_VALUE; }

        case QUERY_AMPERSAND -> { object = markToString(); bufferIndex += 1; makeQueryParam(""); return $PARSE_QUERY; }

        case QUERY_SPACE -> { object = markToString(); bufferIndex += 1; makeQueryParam(""); return $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { object = markToString(); bufferIndex += 1; makeQueryParam(""); return $PARSE_VERSION_0_9; }

        default -> { return toBadRequest(InvalidRequestLine.QUERY_CHAR); }
      }
    }

    return toRead($PARSE_QUERY0);
  }

  private byte executeParseQuery1() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestLine.QUERY_PERCENT);
      }

      final byte code;
      code = PARSE_QUERY_TABLE[b];

      switch (code) {
        case QUERY_VALID -> { appendChar(b); bufferIndex += 1; }

        case QUERY_PERCENT -> { byte next = executeParseQuery1Decode(); if (state != next) { return next; } }

        case QUERY_PLUS -> { appendChar(' '); bufferIndex += 1; }

        case QUERY_EQUALS -> { object = appendToString(); bufferIndex += 1; return $PARSE_QUERY_VALUE; }

        case QUERY_AMPERSAND -> { object = appendToString(); bufferIndex += 1; makeQueryParam(""); return $PARSE_QUERY; }

        case QUERY_SPACE -> { object = appendToString(); bufferIndex += 1; makeQueryParam(""); return $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { object = appendToString(); bufferIndex += 1; makeQueryParam(""); return $PARSE_VERSION_0_9; }

        default -> { return toBadRequest(InvalidRequestLine.QUERY_PERCENT); }
      }
    }

    return toRead($PARSE_QUERY1);
  }

  private byte executeParseQuery1Decode() {
    return decodePercent($PARSE_QUERY1, $PARSE_QUERY1_DECODE, InvalidRequestLine.QUERY_PERCENT);
  }

  private byte executeParseQueryValue() {
    // where the current value begins
    mark = bufferIndex;

    return executeParseQueryValue0();
  }

  private byte executeParseQueryValue0() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestLine.QUERY_CHAR);
      }

      final byte code;
      code = PARSE_QUERY_TABLE[b];

      switch (code) {
        case QUERY_VALID, QUERY_EQUALS -> { bufferIndex += 1; }

        case QUERY_PERCENT, QUERY_PLUS -> { appendInit(); return $PARSE_QUERY_VALUE1; }

        case QUERY_AMPERSAND -> { final String v = markToString(); bufferIndex += 1; makeQueryParam(v); return $PARSE_QUERY; }

        case QUERY_SPACE -> { final String v = markToString(); bufferIndex += 1; makeQueryParam(v); return $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { final String v = markToString(); bufferIndex += 1; makeQueryParam(v); return $PARSE_VERSION_0_9; }

        default -> { return toBadRequest(InvalidRequestLine.QUERY_CHAR); }
      }
    }

    return toRead($PARSE_QUERY_VALUE0);
  }

  private byte executeParseQueryValue1() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestLine.QUERY_CHAR);
      }

      final byte code;
      code = PARSE_QUERY_TABLE[b];

      switch (code) {
        case QUERY_VALID, QUERY_EQUALS -> { appendChar(b); bufferIndex += 1; }

        case QUERY_PERCENT -> { byte next = executeParseQueryValue1Decode(); if (state != next) { return next; } }

        case QUERY_PLUS -> { appendChar(' '); bufferIndex += 1; }

        case QUERY_AMPERSAND -> { final String v = appendToString(); bufferIndex += 1; makeQueryParam(v); return $PARSE_QUERY; }

        case QUERY_SPACE -> { final String v = appendToString(); bufferIndex += 1; makeQueryParam(v); return $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { final String v = appendToString(); bufferIndex += 1; makeQueryParam(v); return $PARSE_VERSION_0_9; }

        default -> { return toBadRequest(InvalidRequestLine.QUERY_PERCENT); }
      }
    }

    return toRead($PARSE_QUERY_VALUE0);
  }

  private byte executeParseQueryValue1Decode() {
    return decodePercent($PARSE_QUERY_VALUE1, $PARSE_QUERY_VALUE1_DECODE, InvalidRequestLine.QUERY_PERCENT);
  }

  @SuppressWarnings("unchecked")
  private void makeQueryParam(String value) {
    if (queryParams == null) {
      queryParams = Util.createMap();
    }

    final String key;
    key = (String) object;

    final Object maybeExisting;
    maybeExisting = queryParams.put(key, value);

    if (maybeExisting == null) {
      return;
    }

    else if (maybeExisting instanceof String s) {

      List<String> list;
      list = Util.createList();

      list.add(s);

      list.add(value);

      queryParams.put(key, list);

    }

    else {
      List<String> list;
      list = (List<String>) maybeExisting;

      list.add(value);

      queryParams.put(key, list);
    }
  }

  // ##################################################################
  // # END: Parse: Query
  // ##################################################################

  // ##################################################################
  // # BEGIN: Decode Percent
  // ############################################################

  private byte decodePercent(byte success, byte read, InvalidRequestLine badRequest) {
    final int firstDigitIndex;
    firstDigitIndex = bufferIndex + 1;

    if (firstDigitIndex < bufferLimit) {
      final byte first;
      first = buffer[firstDigitIndex];

      final byte high;
      high = Bytes.fromHexDigit(first);

      if (high < 0) {
        return toBadRequest(badRequest);
      }

      return switch (high) {
        // 0yyyzzzz
        case 0b0000, 0b0001,
             0b0010, 0b0011,
             0b0100, 0b0101, 0b0110, 0b0111 -> decodePercent1(success, read, badRequest);

        // 110xxxyy 10yyzzzz
        case 0b1100, 0b1101 -> decodePercent2(success, read, badRequest);

        // 1110wwww 10xxxxyy 10yyzzzz
        case 0b1110 -> decodePercent3(success, read, badRequest);

        // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
        case 0b1111 -> decodePercent4(success, read, badRequest);

        default -> toBadRequest(badRequest);
      };
    } else {
      return toRead(read);
    }
  }

  private byte decodePercent1(byte success, byte read, InvalidRequestLine badRequest) {
    if (canRead(3)) {
      final int byte1;
      byte1 = decodePercent();

      if (byte1 < 0) {
        return toBadRequest(badRequest);
      }

      appendChar(byte1);

      return success;
    } else {
      return toRead(read);
    }
  }

  private byte decodePercent2(byte success, byte read, InvalidRequestLine badRequest) {
    if (canRead(6)) {
      final int byte1;
      byte1 = decodePercent();

      if (byte1 < 0) {
        return toBadRequest(badRequest);
      }

      final int byte2;
      byte2 = decodePercent();

      if (!utf8Byte(byte2)) {
        return toBadRequest(badRequest);
      }

      final int c;
      c = (byte1 & 0b1_1111) << 6 | (byte2 & 0b11_1111);

      appendChar(c);

      return success;
    } else {
      return toRead(read);
    }
  }

  private byte decodePercent3(byte success, byte read, InvalidRequestLine badRequest) {
    if (canRead(9)) {
      final int byte1;
      byte1 = decodePercent();

      if (byte1 < 0) {
        return toBadRequest(badRequest);
      }

      final int byte2;
      byte2 = decodePercent();

      if (!utf8Byte(byte2)) {
        return toBadRequest(badRequest);
      }

      final int byte3;
      byte3 = decodePercent();

      if (!utf8Byte(byte3)) {
        return toBadRequest(badRequest);
      }

      final int c;
      c = (byte1 & 0b1111) << 12 | (byte2 & 0b11_1111) << 6 | (byte3 & 0b11_1111);

      appendChar(c);

      return success;
    } else {
      return toRead(read);
    }
  }

  private byte decodePercent4(byte success, byte read, InvalidRequestLine badRequest) {
    if (canRead(12)) {
      final int byte1;
      byte1 = decodePercent();

      if (byte1 < 0) {
        return toBadRequest(badRequest);
      }

      final int byte2;
      byte2 = decodePercent();

      if (!utf8Byte(byte2)) {
        return toBadRequest(badRequest);
      }

      final int byte3;
      byte3 = decodePercent();

      if (!utf8Byte(byte3)) {
        return toBadRequest(badRequest);
      }

      final int byte4;
      byte4 = decodePercent();

      if (!utf8Byte(byte4)) {
        return toBadRequest(badRequest);
      }

      final int c;
      c = (byte1 & 0b111) << 18 | (byte2 & 0b11_1111) << 12 | (byte3 & 0b11_1111) << 6 | (byte4 & 0b11_1111);

      appendCodePoint(c);

      return success;
    } else {
      return toRead(read);
    }
  }

  private boolean utf8Byte(int utf8) {
    final int topTwoBits;
    topTwoBits = utf8 & 0b1100_0000;

    return topTwoBits == 0b1000_0000;
  }

  private int decodePercent() {
    final byte percent;
    percent = buffer[bufferIndex++];

    if (percent != '%') {
      return -1;
    }

    final byte first;
    first = buffer[bufferIndex++];

    final byte high;
    high = Bytes.fromHexDigit(first);

    if (high < 0) {
      return -1;
    }

    final byte second;
    second = buffer[bufferIndex++];

    final byte low;
    low = Bytes.fromHexDigit(second);

    if (low < 0) {
      return -1;
    }

    return (high << 4) | low;
  }

  // ##################################################################
  // # END: Decode Percent
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Version
  // ##################################################################

  private byte executeParseVersion_0_9() {
    return $HTTP_VERSION_NOT_SUPPORTED;
  }

  private static final byte[] HTTP_1_1_CRLF = {'H', 'T', 'T', 'P', '/', '1', '.', '1', '\r', '\n'};

  private static final byte[] HTTP_1_1_LF = {'H', 'T', 'T', 'P', '/', '1', '.', '1', '\n'};

  private byte executeParseVersion_1_1() {
    if (canRead(HTTP_1_1_CRLF.length)) {

      if (bufferMatches(HTTP_1_1_CRLF)) {
        version = Http.Version.HTTP_1_1;

        return $PARSE_HEADER;
      }

      if (bufferMatches(HTTP_1_1_LF)) {
        return toBadRequest(InvalidLineTerminator.INSTANCE);
      }

      return $PARSE_VERSION_OTHERS;

    } else {
      return toRead($PARSE_VERSION_1_1);
    }
  }

  private static final byte[] HTTP_OTHERS = {'H', 'T', 'T', 'P', '/'};

  private byte executeParseVersionOthers() {
    if (bufferMatches(HTTP_OTHERS)) {
      // we'll rely solely on what's buffered.
      // this is already a bad request.

      enum State {
        START,
        MAJOR,
        DOT,
        MINOR,
        CR;
      }

      State state;
      state = State.START;

      while (bufferIndex < bufferLimit) {
        final byte b;
        b = buffer[bufferIndex++];

        if (b == '\n') {
          switch (state) {
            case CR -> { return $HTTP_VERSION_NOT_SUPPORTED; }

            default -> { return toBadRequest(InvalidRequestLine.VERSION_CHAR); }
          }
        }

        else if (b == '\r') {
          switch (state) {
            case MAJOR, MINOR -> state = State.CR;

            default -> { return toBadRequest(InvalidRequestLine.VERSION_CHAR); }
          }
        }

        else if (Http.isDigit(b)) {
          switch (state) {
            case START, MAJOR -> state = State.MAJOR;

            case DOT, MINOR -> state = State.MINOR;

            default -> { return toBadRequest(InvalidRequestLine.VERSION_CHAR); }
          }
        }

        else if (b == '.') {
          switch (state) {
            case MAJOR -> state = State.DOT;

            default -> { return toBadRequest(InvalidRequestLine.VERSION_CHAR); }
          }
        }
      }

      return toBadRequest(InvalidRequestLine.VERSION_CHAR);
    } else {
      return toBadRequest(InvalidRequestLine.VERSION_CHAR);
    }
  }

  // ##################################################################
  // # END: Parse: Version
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Headers
  // ##################################################################

  private byte executeParseHeader() {
    return $OK;
  }

  // ##################################################################
  // # END: Parse: Headers
  // ##################################################################

  // ##################################################################
  // # BEGIN: Response: Early (internal)
  // ##################################################################

  private byte toBadRequest(ClientError error) {
    object = error;

    return $BAD_REQUEST;
  }

  private byte executeBadRequest() {
    final ClientError clientError;
    clientError = (ClientError) object;

    noteSink.send(NOTES.badRequest, id, clientError, this);

    bufferIndex = 0;

    final byte[] message;
    message = clientError.message();

    status1(Http.Status.BAD_REQUEST);

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

    header0(Http.HeaderName.CONTENT_LENGTH, message.length);

    header0(Http.HeaderName.CONNECTION, "close");

    send0(message);

    return $ERROR;
  }

  private byte executeUriTooLong() {
    noteSink.send(NOTES.uriTooLong, id, this);

    bufferIndex = 0;

    status1(Http.Status.URI_TOO_LONG);

    dateNow();

    header0(Http.HeaderName.CONTENT_LENGTH, "0");

    header0(Http.HeaderName.CONNECTION, "close");

    send0();

    return $ERROR;
  }

  private byte executeNotImplemented() {
    noteSink.send(NOTES.notImplemented, id, this);

    bufferIndex = 0;

    status1(Http.Status.NOT_IMPLEMENTED);

    dateNow();

    header0(Http.HeaderName.CONTENT_LENGTH, "0");

    header0(Http.HeaderName.CONNECTION, "close");

    send0();

    return $ERROR;
  }

  private static final byte[] HTTP_VERSION_NOT_SUPPORTED_MSG = "Supported versions: HTTP/1.1\n".getBytes(StandardCharsets.US_ASCII);

  private byte executeHttpVersionNotSupported() {
    noteSink.send(NOTES.httpVersionNotSupported, id, this);

    bufferIndex = 0;

    final byte[] message;
    message = HTTP_VERSION_NOT_SUPPORTED_MSG;

    status1(Http.Status.HTTP_VERSION_NOT_SUPPORTED);

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

    header0(Http.HeaderName.CONTENT_LENGTH, message.length);

    header0(Http.HeaderName.CONNECTION, "close");

    send0(message);

    return $ERROR;
  }

  // ##################################################################
  // # END: Response: Early (internal)
  // ##################################################################

  // ##################################################################
  // # BEGIN: Response
  // ##################################################################

  private void status1(Http.Status status) {
    writeBytes(version.responseBytes);

    HttpStatus internal;
    internal = (HttpStatus) status;

    byte[] statusBytes;
    statusBytes = STATUS_LINES[internal.index];

    writeBytes(statusBytes);
  }

  // ##################################################################
  // # END: Response
  // ##################################################################

  // ##################################################################
  // # BEGIN: Utils
  // ##################################################################

  private void appendInit() {
    if (stringBuilder == null) {
      stringBuilder = new StringBuilder();
    } else {
      stringBuilder.setLength(0);
    }

    for (int idx = mark; idx < bufferIndex; idx++) {
      final byte b;
      b = buffer[idx];

      stringBuilder.append((char) b);
    }
  }

  private void appendChar(byte b) {
    stringBuilder.append((char) b);
  }

  private void appendChar(int c) {
    stringBuilder.append((char) c);
  }

  private void appendCodePoint(int c) {
    stringBuilder.appendCodePoint(c);
  }

  private String appendToString() {
    final String result;
    result = stringBuilder.toString();

    stringBuilder.setLength(0);

    return result;
  }

  private String markToString() {
    return new String(buffer, mark, bufferIndex - mark, StandardCharsets.US_ASCII);
  }

  private void note(Note.Long1Ref1<Http.Exchange> note) {
    noteSink.send(note, id, this);
  }

  // ##################################################################
  // # END: Utils
  // ##################################################################

  private sealed abstract static class InternalException extends RuntimeException {
    InternalException() {}
  }

  private static final class ClientErrorException extends InternalException implements Media.Bytes {
    private final ClientError kind;

    ClientErrorException(ClientError kind) {
      this.kind = kind;
    }

    @Override
    public final String contentType() {
      return "text/plain; charset=utf-8";
    }

    public final Http.Status status() {
      return kind.status();
    }

    @Override
    public final byte[] toByteArray() {
      return kind.message();
    }
  }

  private static final class MaxBufferSizeException extends InternalException {}

  private static final class RemoteClosedException extends InternalException {}

  private static final class UnexpectedEofException extends InternalException {}

  public enum ParseStatus {
    // keep going
    NORMAL,

    // SocketInput statuses
    EOF,

    // 400 bad request
    INVALID_TARGET,

    INVALID_PROTOCOL,

    INVALID_REQUEST_LINE_TERMINATOR,

    INVALID_HEADER,

    INVALID_CONTENT_LENGTH,

    // 414 actually
    URI_TOO_LONG;

    public final boolean isError() {
      return this != NORMAL;
    }

    final boolean isNormal() {
      return this == NORMAL;
    }

    final boolean isBadRequest() {
      return compareTo(INVALID_TARGET) >= 0;
    }
  }

  static final class SendException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SendException(IOException cause) {
      super(cause);
    }

    @Override
    public final IOException getCause() {
      return (IOException) super.getCause();
    }
  }

  private enum RequestBodyKind {
    EMPTY,

    IN_BUFFER,

    FILE;
  }

  private static final int _START = 0;
  private static final int _PARSE = 1;
  private static final int _REQUEST = 2;
  private static final int _RESPONSE = 3;
  private static final int _PROCESSED = 4;
  private static final int _DONE = 5;

  private static final int STATE_MASK = 0xF;
  private static final int BITS_MASK = ~STATE_MASK;

  private static final int KEEP_ALIVE = 1 << 4;
  private static final int CONTENT_LENGTH = 1 << 5;
  private static final int CHUNKED = 1 << 6;

  private Map<String, Object> attributes;

  private int bitset = 0;

  private final Clock clock;

  ParseStatus parseStatus = ParseStatus.NORMAL;

  private final Closeable socket;

  // RequestBody

  private RequestBodyKind requestBodyKind = RequestBodyKind.EMPTY;

  private Path requestBodyDirectory;

  private Path requestBodyFile;

  // RequestHeaders

  HttpHeaderName headerName;

  Map<HttpHeaderName, HttpHeader> headers;

  // RequestLine

  int pathIndex;

  private int pathLimit;

  Map<String, String> pathParams;

  private boolean queryParamsReady;

  private int queryStart;

  private String rawValue;

  // SocketInput

  int lineLimit = 0;

  private final OutputStream outputStream;

  public HttpExchange(Socket socket, int bufferSizeInitial, int bufferSizeMax, Clock clock, Note.Sink noteSink) throws IOException {
    this(socket, socket.getInputStream(), socket.getOutputStream(), bufferSizeInitial, bufferSizeMax, clock, noteSink);
  }

  private HttpExchange(
      Closeable socket,
      InputStream inputStream,
      OutputStream outputStream,
      int bufferSizeInitial,
      int bufferSizeMax,
      Clock clock,
      Note.Sink noteSink
  ) {

    final int initialSize;
    initialSize = powerOfTwo(bufferSizeInitial);

    this.buffer = new byte[initialSize];

    this.clock = clock;

    this.inputStream = inputStream;

    this.maxBufferSize = powerOfTwo(bufferSizeMax);

    this.noteSink = noteSink;

    this.outputStream = outputStream;

    this.socket = socket;

  }

  private HttpExchange(HttpExchangeConfig config) {

    attributes = config.attributes;

    final int initialSize;
    initialSize = powerOfTwo(config.bufferSizeInitial);

    buffer = new byte[initialSize];

    clock = config.clock;

    inputStream = config.inputStream();

    maxBufferSize = config.bufferSizeMax;

    noteSink = config.noteSink;

    outputStream = new ByteArrayOutputStream();

    socket = () -> {}; // noop closeable

  }

  /**
   * Parses the specified string into a new request-target instance.
   *
   * @param target
   *        the raw (undecoded) request-target value
   *
   * @return a new request target instance
   *
   * @throws IllegalArgumentException
   *         if the string represents an invalid request-target value
   */
  public static HttpExchange parseRequestTarget(String target) {
    Objects.requireNonNull(target, "target == null");

    // append a line terminator
    target = target + " \r\n";

    byte[] bytes;
    bytes = target.getBytes(StandardCharsets.UTF_8);

    // in-memory stream... no closing needed...
    InputStream inputStream;
    inputStream = new ByteArrayInputStream(bytes);

    HttpExchange requestLine;
    requestLine = new HttpExchange(null, inputStream, null, bytes.length, bytes.length, null, null);

    try {
      requestLine.parseLine();

      requestLine.parseRequestTarget();
    } catch (IOException e) {
      throw new AssertionError("In-memory stream does not throw IOException", e);
    } catch (MaxBufferSizeException e) {
      throw new AssertionError("Buffer is always large enough for input", e);
    }

    ParseStatus parseStatus;
    parseStatus = requestLine.parseStatus;

    if (parseStatus.isError()) {
      throw new IllegalArgumentException(parseStatus.name());
    }

    return requestLine;
  }

  static HttpExchange build(HttpExchangeConfig config) {
    try {
      final HttpExchange http;
      http = new HttpExchange(config);

      final ParseStatus status;
      status = http.parse();

      if (status != ParseStatus.NORMAL) {
        throw new IllegalArgumentException("Invalid request");
      }

      return http;
    } catch (IOException e) {
      throw new AssertionError("ByteArrayInputStream does not throw IOException", e);
    } catch (MaxBufferSizeException e) {
      throw new IllegalArgumentException("Insufficient buffer size");
    }
  }

  static final int powerOfTwo(int size) {
    // maybe size is already power of 2
    int x;
    x = size - 1;

    int leading;
    leading = Integer.numberOfLeadingZeros(x);

    int n;
    n = -1 >>> leading;

    if (n < 0) {
      // should not happen as minimal buffer size is 128
      throw new IllegalArgumentException("Buffer size is too small");
    }

    if (n >= HARD_MAX_BUFFER_SIZE) {
      return HARD_MAX_BUFFER_SIZE;
    }

    return n + 1;
  }

  @Override
  public final void close() throws IOException {
    try {
      requestBodyClose();
    } finally {
      socket.close();
    }
  }

  @Override
  public final String toString() {
    final int state;
    state = state();

    return switch (state) {
      case _START -> "HttpExchange[START]";

      case _PARSE -> "HttpExchange[PARSE]";

      case _REQUEST -> new String(buffer, 0, bufferLimit, StandardCharsets.UTF_8);

      case _PROCESSED -> toStringOutput("PROCESSED");

      case _DONE -> toStringOutput("DONE");

      default -> "HttpExchange[" + state + "]";
    };
  }

  private String toStringOutput(String state) {
    if (outputStream instanceof ByteArrayOutputStream impl) {
      final byte[] bytes;
      bytes = impl.toByteArray();

      return new String(bytes, StandardCharsets.UTF_8);
    } else {
      return "HttpExchange[" + state + "]";
    }
  }

  private int state() {
    return bitset & STATE_MASK;
  }

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing
  // ##################################################################

  private boolean parse0() throws IOException {
    enum Parse {
      STATUS_LINE,

      HEADERS,

      BODY;
    }

    Parse parse;
    parse = null;

    try {
      setState(_PARSE);

      parse = Parse.STATUS_LINE;

      parseRequestLine();

      parse = Parse.HEADERS;

      parseHeaders();

      parse = Parse.BODY;

      parseRequestBody();

      parseRequestEnd();

      setState(_REQUEST);

      return true;
    } catch (InternalException e) {
      switch (e) {
        case ClientErrorException ex -> {
          switch (ex.kind) {
            case InvalidRequestLine irl -> noteSink.send(NOTES.invalidRequestLine, irl.ordinal(), bufferHex());
          }

          setState(_REQUEST);

          respond(ex.status(), ex);
        }

        case MaxBufferSizeException ex -> {

          switch (parse) {
            case STATUS_LINE -> throw new UnsupportedOperationException("Implement me :: 414");

            case HEADERS -> throw new UnsupportedOperationException("Implement me :: 431");

            case BODY -> throw new UnsupportedOperationException("Implement me :: 413");
          }

        }

        case RemoteClosedException ex -> {
          // noop, we assume remote closed the connection gracefully
        }

        case UnexpectedEofException ex -> {
          // TODO log?
        }
      }

      setState(_DONE);

      return false;
    }
  }

  public final ParseStatus parse() throws IOException, MaxBufferSizeException {
    if (testState(_START)) {
      // noop
    }

    else if (testState(_PROCESSED)) {
      resetSocketInput();

      resetRequestLine();

      resetHeaders();

      resetRequestBody();

      resetServerLoop();
    }

    else {
      throw new IllegalStateException("""
      The parse() metod must only be called after:
      1) loop creation; or
      2) a successful commit operation
      """);
    }

    setState(_PARSE);

    // request line

    parseRequestLine();

    if (parseStatus.isError()) {
      return parseStatus;
    }

    // request headers

    parseHeaders();

    if (parseStatus.isError()) {
      return parseStatus;
    }

    // request body

    parseRequestBody();

    if (parseStatus.isError()) {
      return parseStatus;
    }

    parseRequestEnd();

    return parseStatus;
  }

  private void resetRequestLine() {
    method = null;

    pathLimit = 0;

    if (pathParams != null) {
      pathParams.clear();
    }

    path = null;

    if (queryParams != null) {
      queryParams.clear();
    }

    queryParamsReady = false;

    queryStart = 0;

    rawValue = null;

    // set the version so we send bad request messages
    version = Http.Version.HTTP_1_1;
  }

  private void resetHeaders() {
    headerName = null;

    if (headers != null) {
      headers.clear();
    }
  }

  private void resetRequestBody() {
    requestBodyKind = RequestBodyKind.EMPTY;

    requestBodyFile = null;
  }

  private void resetServerLoop() {
    if (attributes != null) {
      attributes.clear();
    }
  }

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing || request line
  // ##################################################################

  final void parseRequestLine() throws IOException {
    parseLine();

    parseMethod();

    if (method == null) {
      throw InvalidRequestLine.METHOD.create();
    }

    parseRequestTarget();

    parseVersion();

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }

    if (!consumeIfEndOfLine()) {
      parseStatus = ParseStatus.INVALID_REQUEST_LINE_TERMINATOR;

      return;
    }
  }

  private static final byte[] _CONNECT = "CONNECT ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _DELETE = "DELETE ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _GET = "GET ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _HEAD = "HEAD ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _OPTIONS = "OPTIONS ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _POST = "POST ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _PUT = "PUT ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _PATCH = "PATCH ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _TRACE = "TRACE ".getBytes(StandardCharsets.UTF_8);

  private void parseMethod() throws IOException {
    if (bufferIndex >= lineLimit) {
      // empty line... nothing to do
      return;
    }

    byte first;
    first = buffer[bufferIndex];

    // based on the first char, we select out method candidate

    switch (first) {
      case 'C' -> parseMethod0(Http.Method.CONNECT, _CONNECT);

      case 'D' -> parseMethod0(Http.Method.DELETE, _DELETE);

      case 'G' -> parseMethod0(Http.Method.GET, _GET);

      case 'H' -> parseMethod0(Http.Method.HEAD, _HEAD);

      case 'O' -> parseMethod0(Http.Method.OPTIONS, _OPTIONS);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod0(Http.Method.TRACE, _TRACE);
    }
  }

  private void parseMethod0(Http.Method candidate, byte[] candidateBytes) throws IOException {
    if (matches(candidateBytes)) {
      method = candidate;
    }
  }

  private void parseMethodP() throws IOException {
    // method starts with a P. It might be:
    // - POST
    // - PUT
    // - PATCH

    // we'll try them in sequence

    parseMethod0(Http.Method.POST, _POST);

    if (method != null) {
      return;
    }

    parseMethod0(Http.Method.PUT, _PUT);

    if (method != null) {
      return;
    }

    parseMethod0(Http.Method.PATCH, _PATCH);

    if (method != null) {
      return;
    }
  }

  final void parseRequestTarget() throws IOException {
    int startIndex;
    startIndex = parsePathStart();

    parsePathRest(startIndex);

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }
  }

  private int parsePathStart() throws IOException {
    // we will check if the request target starts with a '/' char

    int targetStart;
    targetStart = bufferIndex;

    if (bufferIndex >= lineLimit) {
      throw InvalidRequestLine.REQUEST_TARGET_EOF.create();
    }

    byte b;
    b = buffer[bufferIndex++];

    if (b != Bytes.SOLIDUS) {
      throw InvalidRequestLine.REQUEST_TARGET_FORM.create();
    }

    // mark request path start

    return targetStart;
  }

  private void parsePathRest(int startIndex) throws IOException {
    // we will look for the first:
    // - ? char
    // - SP char
    int index;
    index = indexOf(Bytes.QUESTION_MARK, Bytes.SP);

    if (index < 0) {
      // trailing char was not found
      parseStatus = ParseStatus.URI_TOO_LONG;

      return;
    }

    // index where path ends
    int pathEndIndex;
    pathEndIndex = index;

    // as of now target ends at the path
    int targetEndIndex;
    targetEndIndex = pathEndIndex;

    // as of now query starts and ends at path i.e. len = 0
    int queryStartIndex;
    queryStartIndex = pathEndIndex;

    // we'll continue at the '?' or SP char
    bufferIndex = index;

    byte b;
    b = buffer[bufferIndex++];

    if (b == Bytes.QUESTION_MARK) {
      queryStartIndex = bufferIndex;

      targetEndIndex = indexOf(Bytes.SP);

      if (targetEndIndex < 0) {
        // trailing char was not found
        parseStatus = ParseStatus.URI_TOO_LONG;

        return;
      }

      // we'll continue immediately after the SP
      bufferIndex = targetEndIndex + 1;
    }

    rawValue = bufferToString(startIndex, targetEndIndex);

    pathLimit = pathEndIndex - startIndex;

    queryStart = queryStartIndex - startIndex;
  }

  static final byte[] HTTP_VERSION_PREFIX = {'H', 'T', 'T', 'P', '/'};

  private void parseVersion() {
    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' = 8 bytes

    if (!matches(HTTP_VERSION_PREFIX)) {
      // buffer does not start with 'HTTP/'
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    // check if we  have '1' '.' '1' = 3 bytes

    int requiredIndex;
    requiredIndex = bufferIndex + 3 - 1;

    if (requiredIndex >= lineLimit) {
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeMajor;
    maybeMajor = buffer[bufferIndex++];

    if (!Http.isDigit(maybeMajor)) {
      // major version is not a digit => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeDot;
    maybeDot = buffer[bufferIndex++];

    if (maybeDot != '.') {
      // major version not followed by a DOT => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeMinor;
    maybeMinor = buffer[bufferIndex++];

    if (!Http.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    final int major;
    major = maybeMajor - 0x30;

    final int minor;
    minor = maybeMinor - 0x30;

    if (major == 1 && minor == 1) {
      version = Http.Version.HTTP_1_1;
    } else {
      version = Http.Version.HTTP_1_0;
    }
  }

  // ##################################################################
  // # END: HTTP/1.1 request parsing || request line
  // ##################################################################

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing || headers
  // ##################################################################

  final void parseHeaders() throws IOException, MaxBufferSizeException {
    parseLine();

    while (parseStatus.isNormal() && !consumeIfEmptyLine()) {
      parseHeaderName();

      if (parseStatus.isError()) {
        break;
      }

      parseHeaderValue();

      if (parseStatus.isError()) {
        break;
      }

      parseLine();
    }

    // clear last header name just in case
    headerName = null;
  }

  private void parseHeaderName() {
    // we reset any previous found header name

    headerName = null;

    // possible header name starts here
    int startIndex;
    startIndex = bufferIndex;

    int colonIndex;
    colonIndex = indexOf(Bytes.COLON);

    if (colonIndex < 0) {
      // no colon found
      parseStatus = ParseStatus.INVALID_HEADER;

      return;
    }

    final int length;
    length = colonIndex - startIndex;

    if (length == 0) {
      // empty header name
      parseStatus = ParseStatus.INVALID_HEADER;

      return;
    }

    // let's validate and lowercase the header name
    final byte[] bytes;
    bytes = new byte[length];

    for (int idx = 0; idx < length; idx++) {
      final int offset;
      offset = startIndex + idx;

      final byte b;
      b = buffer[offset];

      final byte mapped;
      mapped = HttpHeaderName.map(b);

      if (mapped < 0) {
        // header name contains an invalid character
        parseStatus = ParseStatus.INVALID_HEADER;

        return;
      }

      bytes[idx] = mapped;
    }

    final String lowerCase;
    lowerCase = new String(bytes, StandardCharsets.US_ASCII);

    final HttpHeaderName standard;
    standard = HttpHeaderName.byLowerCase(lowerCase);

    if (standard != null) {
      headerName = standard;
    } else {
      headerName = HttpHeaderName.ofLowerCase(lowerCase);
    }

    // resume immediately after the colon
    bufferIndex = colonIndex + 1;
  }

  private void parseHeaderValue() {
    int startIndex;
    startIndex = parseHeaderValueStart();

    int endIndex;
    endIndex = parseHeaderValueEnd(startIndex);

    final HttpHeader header;
    header = HttpHeader.createIfValid(buffer, startIndex, endIndex);

    if (header == null) {
      parseStatus = ParseStatus.INVALID_HEADER;

      return;
    }

    if (headers == null) {
      headers = Util.createMap();
    }

    final HttpHeader existing;
    existing = headers.put(headerName, header);

    if (existing != null) {
      existing.add(header);

      headers.put(headerName, existing);
    }
  }

  final void hexDump() {
    String bufferDump;
    bufferDump = bufferHex();

    String args;
    args = "bufferIndex=" + bufferIndex + ";lineLimit=" + lineLimit;

    noteSink.send(NOTES.hexdump, bufferDump, args);
  }

  private String bufferHex() {
    HexFormat format;
    format = HexFormat.of();

    return format.formatHex(buffer, 0, bufferLimit);
  }

  private int parseHeaderValueStart() {
    // consumes and discard a single leading OWS if present
    byte maybeOws;
    maybeOws = buffer[bufferIndex];

    if (Bytes.isOptionalWhitespace(maybeOws)) {
      // consume and discard leading OWS
      bufferIndex++;
    }

    return bufferIndex;
  }

  private int parseHeaderValueEnd(int startIndex) {
    int end;
    end = lineLimit;

    byte maybeCR;
    maybeCR = buffer[end - 1];

    if (maybeCR == Bytes.CR) {
      // value ends at the CR of the line end CRLF
      end = end - 1;
    }

    // resume immediately after lineLimit
    bufferIndex = lineLimit + 1;

    return end;
  }

  // ##################################################################
  // # END: HTTP/1.1 request parsing || headers
  // ##################################################################

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing || body
  // ##################################################################

  final void parseRequestBody() throws IOException {
    final HttpHeader contentLength;
    contentLength = headerUnchecked(HttpHeaderName.CONTENT_LENGTH);

    if (contentLength != null) {
      final long length;
      length = contentLength.unsignedLongValue(buffer);

      if (length < 0) {
        // TODO 413 Payload Too Large
        parseStatus = ParseStatus.INVALID_HEADER;
      }

      else if (canBuffer(length)) {
        int read;
        read = read(length);

        if (read < 0) {
          throw new EOFException();
        }

        requestBodyKind = RequestBodyKind.IN_BUFFER;
      }

      else {
        if (requestBodyDirectory == null) {
          requestBodyFile = Files.createTempFile("objectos-way-request-body-", ".tmp");
        } else {
          requestBodyFile = Files.createTempFile(requestBodyDirectory, "objectos-way-request-body-", ".tmp");
        }

        long read;
        read = read(requestBodyFile, length);

        if (read < 0) {
          parseStatus = ParseStatus.EOF;
        } else {
          requestBodyKind = RequestBodyKind.FILE;
        }
      }

      return;
    }

    final HttpHeader transferEncoding;
    transferEncoding = headerUnchecked(HttpHeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      // TODO 501 Not Implemented
      throw new UnsupportedOperationException("Implement me");
    }

    // TODO 411 Length Required
  }

  // ##################################################################
  // # END: HTTP/1.1 request parsing || body
  // ##################################################################

  // ##################################################################
  // # BEGIN: HTTP/1.1 request parsing || keep alive
  // ##################################################################

  final void parseRequestEnd() {
    // handle keep alive

    clearBit(KEEP_ALIVE);

    if (version == Http.Version.HTTP_1_1) {
      setBit(KEEP_ALIVE);
    }

    HttpHeader connection;
    connection = headerUnchecked(HttpHeaderName.CONNECTION);

    if (connection != null) {
      final String value;
      value = connection.get(buffer);

      if (value != null) {

        if (value.equalsIgnoreCase("keep-alive")) {
          setBit(KEEP_ALIVE);
        }

        else if (value.equalsIgnoreCase("close")) {
          clearBit(KEEP_ALIVE);
        }

      }

    }

    setState(_REQUEST);
  }

  // ##################################################################
  // # END: HTTP/1.1 request parsing || keep alive
  // ##################################################################

  private void checkRequest() {
    Check.state(
        !badRequest(),

        """
        This request method can only be invoked:
        - after a successful parse() operation; and
        - before any response related method invocation.
        """
    );
  }

  private boolean badRequest() {
    Check.state(testState(_REQUEST), "Http.Request methods can only be invoked after a parse() operation");

    return parseStatus.isBadRequest();
  }

  // ##################################################################
  // # BEGIN: Http.Exchange API || request line
  // ##################################################################

  @Override
  public final Http.Method method() {
    return method;
  }

  @Override
  public final Http.Version version() {
    return version;
  }

  // ##################################################################
  // # END: Http.Exchange API || request line
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request target
  // ##################################################################

  @Override
  public final String path() {
    if (path == null) {
      String raw;
      raw = rawPath();

      path = decode(raw);
    }

    return path;
  }

  @Override
  public final String pathParam(String name) {
    Objects.requireNonNull(name, "name == null");

    String result;
    result = null;

    if (pathParams != null) {
      result = pathParams.get(name);
    }

    return result;
  }

  final void pathReset() {
    pathIndex = 0;

    if (pathParams != null) {
      pathParams.clear();
    }
  }

  final void pathParams(Map<String, String> value) {
    pathParams = value;
  }

  final boolean testPathExact(String exact) {
    final String path;
    path = path();

    final int thisLength;
    thisLength = path.length() - pathIndex;

    final int thatLength;
    thatLength = exact.length();

    if (thisLength == thatLength && path.regionMatches(pathIndex, exact, 0, thatLength)) {
      pathIndex += thatLength;

      return true;
    } else {
      return false;
    }
  }

  final boolean testPathParam(String name, char terminator) {
    final String path;
    path = path();

    final int terminatorIndex;
    terminatorIndex = path.indexOf(terminator, pathIndex);

    if (terminatorIndex < 0) {
      return false;
    }

    final String varValue;
    varValue = path.substring(pathIndex, terminatorIndex);

    // immediately after the terminator
    pathIndex = terminatorIndex + 1;

    if (pathParams == null) {
      pathParams = Util.createMap();
    }

    pathParams.put(name, varValue);

    return true;
  }

  final boolean testPathParamLast(String name) {
    final String path;
    path = path();

    final int solidus;
    solidus = path.indexOf('/', pathIndex);

    if (solidus < 0) {

      final String varValue;
      varValue = path.substring(pathIndex);

      pathIndex += varValue.length();

      if (pathParams == null) {
        pathParams = Util.createMap();
      }

      pathParams.put(name, varValue);

      return true;

    } else {

      return false;

    }
  }

  final boolean testPathRegion(String region) {
    final String path;
    path = path();

    if (path.regionMatches(pathIndex, region, 0, region.length())) {
      pathIndex += region.length();

      return true;
    } else {
      return false;
    }
  }

  final boolean testPathEnd() {
    final String path;
    path = path();

    return pathIndex == path.length();
  }

  @Override
  public final String queryParam(String name) {
    Objects.requireNonNull(name, "name == null");

    if (queryParams == null) {
      return null;
    } else {
      return Http.queryParamsGet(queryParams, name);
    }
  }

  @Override
  public final List<String> queryParamAll(String name) {
    Objects.requireNonNull(name, "name == null");

    if (queryParams == null) {
      return List.of();
    } else {
      return Http.queryParamsGetAll(queryParams, name);
    }
  }

  @Override
  public final Set<String> queryParamNames() {
    if (queryParams == null) {
      return Set.of();
    } else {
      return queryParams.keySet();
    }
  }

  @Override
  public final String rawPath() {
    return rawValue.substring(0, pathLimit);
  }

  @Override
  public final String rawQuery() {
    return queryStart == pathLimit ? null : rawValue.substring(queryStart);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final String rawQueryWith(String name, String value) {
    if (name.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }

    Objects.requireNonNull(value, "value == null");

    String encodedKey;
    encodedKey = encode(name);

    String encodedValue;
    encodedValue = encode(value);

    int queryLength;
    queryLength = rawValue.length() - queryStart;

    if (queryLength < 2) {
      return encodedKey + "=" + encodedValue;
    }

    Map<String, Object> params;
    params = Util.createSequencedMap();

    makeQueryParams(params, Function.identity());

    params.put(encodedKey, encodedValue);

    return Http.queryParamsToString(params, Function.identity());
  }

  public final String rawValue() {
    return rawValue;
  }

  private void makeQueryParams(Map<String, Object> map, Function<String, String> decoder) {
    int queryLength;
    queryLength = rawValue.length() - queryStart;

    if (queryLength < 2) {
      // query is empty: either "" or "?"
      return;
    }

    String source;
    source = rawQuery();

    StringBuilder sb;
    sb = new StringBuilder();

    String key;
    key = null;

    for (int i = 0, len = source.length(); i < len; i++) {
      char c;
      c = source.charAt(i);

      switch (c) {
        case '=' -> {
          if (key != null) {
            Http.queryParamsAdd(map, decoder, key, "");
          }

          key = sb.toString();

          sb.setLength(0);
        }

        case '&' -> {
          String value;
          value = sb.toString();

          sb.setLength(0);

          if (key == null) {
            key = value;

            continue;
          }

          Http.queryParamsAdd(map, decoder, key, value);

          key = null;
        }

        default -> sb.append(c);
      }
    }

    String value;
    value = sb.toString();

    if (key != null) {
      Http.queryParamsAdd(map, decoder, key, value);
    } else {
      Http.queryParamsAdd(map, decoder, value, "");
    }
  }

  // ##################################################################
  // # END: Http.Exchange API || request target
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request headers
  // ##################################################################

  @Override
  public final String header(Http.HeaderName name) {
    Objects.requireNonNull(name, "name == null");

    final HttpHeader maybe;
    maybe = headerUnchecked(name);

    if (maybe == null) {
      return null;
    }

    return maybe.get(buffer);
  }

  public final int size() {
    return headers != null ? headers.size() : 0;
  }

  private HttpHeader headerUnchecked(Http.HeaderName name) {
    if (headers == null) {
      return null;
    } else {
      return headers.get(name);
    }
  }

  // ##################################################################
  // # END: Http.Exchange API || request headers
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request body
  // ##################################################################

  public void requestBodyDirectory(java.nio.file.Path directory) {
    requestBodyDirectory = directory;
  }

  private void requestBodyClose() throws IOException {
    if (requestBodyFile != null) {
      Files.delete(requestBodyFile);
    }
  }

  @Override
  public final InputStream bodyInputStream() throws IOException {
    checkRequest();

    return switch (requestBodyKind) {
      case EMPTY -> InputStream.nullInputStream();

      case IN_BUFFER -> openStreamImpl();

      case FILE -> Files.newInputStream(requestBodyFile);
    };
  }

  private InputStream openStreamImpl() {
    int length;
    length = bufferLimit - bufferIndex;

    return new ByteArrayInputStream(buffer, bufferIndex, length);
  }

  // ##################################################################
  // # END: Http.Exchange API || request body
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request attributes
  // ##################################################################

  @Override
  public final <T> void set(Class<T> key, T value) {
    String name;
    name = key.getName(); // implicit null check

    Check.notNull(value, "value == null");

    Map<String, Object> map;
    map = attributes();

    map.put(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T get(Class<T> key) {
    String name;
    name = key.getName(); // implicit null check

    Map<String, Object> map;
    map = attributes();

    return (T) map.get(name);
  }

  private Map<String, Object> attributes() {
    if (attributes == null) {
      attributes = Util.createMap();
    }

    return attributes;
  }

  // ##################################################################
  // # END: Http.Exchange API || request attributes
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || response
  // ##################################################################

  // 2xx responses

  @Override
  public final void ok(Media.Bytes media) {
    respond(Http.Status.OK, media);
  }

  // 4xx responses

  @Override
  public final void badRequest(Media.Bytes media) {
    respond(Http.Status.BAD_REQUEST, media);
  }

  @Override
  public final void notFound(Media.Bytes media) {
    respond(Http.Status.NOT_FOUND, media);
  }

  // generic responses

  @Override
  public final void respond(Http.ResponseMessage message) {
    HttpResponseMessage impl;
    impl = (HttpResponseMessage) message;

    impl.accept(this);
  }

  public final void respond(Http.Status status, Media.Bytes media) {
    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new NullPointerException("The specified Media.Bytes provided a null content-type");
    }

    final byte[] bytes;
    bytes = media.toByteArray();

    if (bytes == null) {
      throw new NullPointerException("The specified Media.Bytes provided a null byte array");
    }

    status0(status);

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, contentType);

    header0(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    switch (status.code()) {
      case 400 -> header0(Http.HeaderName.CONNECTION, "close");
    }

    body0(media, bytes);
  }

  @Override
  public final void respond(Lang.MediaWriter writer) {
    respond(Http.Status.OK, writer);
  }

  @Override
  public final void header(Http.HeaderName name, long value) {
    Objects.requireNonNull(name, "name == null");

    header0(name, value);
  }

  @Override
  public final void respond(Http.Status status, Media.Bytes object, Consumer<Http.ResponseHeaders> headers) {
    final byte[] bytes;
    bytes = respond0(status, object);

    headers.accept(this);

    send0(bytes);
  }

  private byte[] respond0(Http.Status status, Media.Bytes object) {
    Objects.requireNonNull(status, "status == null");

    // early object validation
    String contentType;
    contentType = object.contentType();

    if (contentType == null) {
      throw new NullPointerException("The specified Lang.MediaObject provided a null content-type");
    }

    byte[] bytes;
    bytes = object.toByteArray();

    if (bytes == null) {
      throw new NullPointerException("The specified Lang.MediaObject provided a null byte array");
    }

    status0(status);

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, contentType);

    header0(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    return bytes;
  }

  private static final byte[] CHUNKED_TRAILER = "0\r\n\r\n".getBytes(StandardCharsets.UTF_8);

  @Override
  public final void respond(Http.Status status, Lang.MediaWriter writer) {
    final Charset charset;
    charset = respond0(status, writer);

    send0(writer, charset);
  }

  @Override
  public final void respond(Http.Status status, Lang.MediaWriter writer, Consumer<Http.ResponseHeaders> headers) {
    final Charset charset;
    charset = respond0(status, writer);

    headers.accept(this);

    if (testBit(CONTENT_LENGTH)) {
      throw new IllegalStateException(
          "Content-Length must not be set with a Lang.MediaWriter response"
      );
    }

    if (!testBit(CHUNKED)) {
      throw new IllegalStateException(
          "Transfer-Encoding: chunked must be set with a Lang.MediaWriter response"
      );
    }

    send0(writer, charset);
  }

  private Charset respond0(Http.Status status, Lang.MediaWriter writer) {
    Objects.requireNonNull(status, "status == null");

    // early writer validation
    final String contentType;
    contentType = writer.contentType();

    if (contentType == null) {
      throw new NullPointerException("The specified Lang.MediaWriter provided a null content-type");
    }

    final Charset charset;
    charset = writer.mediaCharset();

    if (charset == null) {
      throw new NullPointerException("The specified Lang.MediaWriter provided a null charset");
    }

    status0(status);

    dateNow();

    header0(Http.HeaderName.CONTENT_TYPE, contentType);

    header0(Http.HeaderName.TRANSFER_ENCODING, "chunked");

    return charset;
  }

  @Override
  public final void header(Http.HeaderName name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    header0(name, value);
  }

  @Override
  public final void dateNow() {
    Clock theClock;
    theClock = clock;

    if (theClock == null) {
      theClock = Clock.systemUTC();
    }

    ZonedDateTime now;
    now = ZonedDateTime.now(theClock);

    String value;
    value = Http.formatDate(now);

    header0(Http.HeaderName.DATE, value);
  }

  final void header0(Http.HeaderName name, long value) {
    final String s;
    s = Long.toString(value);

    header0(name, s);
  }

  private void send0(Lang.MediaWriter writer, final Charset charset) {
    if (method == Http.Method.HEAD) {

      send0();

    } else {

      try {
        sendStart();

        bufferIndex = 0;

        CharWritableAppendable out;
        out = new CharWritableAppendable(charset);

        writer.mediaTo(out);

        out.flush();

        outputStream.write(CHUNKED_TRAILER);
      } catch (IOException e) {
        throw new SendException(e);
      } finally {
        setState(_PROCESSED);
      }

    }
  }

  public final void iseIfPossible(Throwable t) {
    if (testState(_RESPONSE)) {
      return;
    }

    if (testState(_PROCESSED)) {
      return;
    }

    StringWriter sw;
    sw = new StringWriter();

    PrintWriter pw;
    pw = new PrintWriter(sw);

    t.printStackTrace(pw);

    String msg;
    msg = sw.toString();

    byte[] bytes;
    bytes = msg.getBytes();

    status0(Http.Status.INTERNAL_SERVER_ERROR);

    dateNow();

    header0(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    header0(Http.HeaderName.CONTENT_TYPE, "text/plain");

    header0(Http.HeaderName.CONNECTION, "close");

    send0(bytes);
  }

  private void checkResponse() {
    if (testState(_REQUEST)) {
      bufferIndex = 0;

      setState(_RESPONSE);

      return;
    }

    throw new IllegalStateException(
        """
        Response methods can only be invoked:
        - after a successful parse() operation; and
        - before the commit() method invocation.
        """
    );
  }

  static final byte[][] STATUS_LINES;

  static {
    int size;
    size = HttpStatus.size();

    byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      HttpStatus status;
      status = HttpStatus.get(index);

      String response;
      response = Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      map[index] = Http.utf8(response);
    }

    STATUS_LINES = map;
  }

  final void status0(Http.Status status) {
    checkResponse();

    Http.Version version;
    version = Http.Version.HTTP_1_1;

    writeBytes(version.responseBytes);

    HttpStatus internal;
    internal = (HttpStatus) status;

    byte[] statusBytes;
    statusBytes = STATUS_LINES[internal.index];

    writeBytes(statusBytes);
  }

  final void header0(Http.HeaderName name, String value) {
    // write our the name
    final HttpHeaderName impl;
    impl = (HttpHeaderName) name;

    final byte[] nameBytes;
    nameBytes = impl.getBytes(version);

    writeBytes(nameBytes);

    // write out the separator
    writeBytes(Bytes.COLONSP);

    // write out the value
    byte[] valueBytes;
    valueBytes = value.getBytes(StandardCharsets.US_ASCII);

    writeBytes(valueBytes);

    writeBytes(Bytes.CRLF);

    // handle connection: close if necessary
    if (name == Http.HeaderName.CONNECTION && value.equalsIgnoreCase("close")) {
      clearBit(KEEP_ALIVE);
    }

    else if (name == Http.HeaderName.CONTENT_LENGTH) {
      setBit(CONTENT_LENGTH);
    }

    else if (name == Http.HeaderName.TRANSFER_ENCODING) {
      if (value.equalsIgnoreCase("chunked")) {
        setBit(CHUNKED);
      } else {
        clearBit(CHUNKED);
      }
    }
  }

  final void body0(Object original, byte[] bytes) {
    send0(bytes);
  }

  final void send0() {
    try {
      sendStart();
    } catch (IOException e) {
      throw new SendException(e);
    } finally {
      setState(_PROCESSED);
    }
  }

  final void send0(byte[] body) {
    if (method == Http.Method.HEAD) {

      send0();

    } else {

      try {
        sendStart();

        outputStream.write(body, 0, body.length); // implicity body null-check
      } catch (IOException e) {
        throw new SendException(e);
      } finally {
        setState(_PROCESSED);
      }

    }
  }

  final void send0(java.nio.file.Path file) {
    Objects.requireNonNull(file, "file == null");

    if (method == Http.Method.HEAD) {

      send0();

    } else {

      try {
        sendStart();

        try (InputStream in = Files.newInputStream(file)) {
          in.transferTo(outputStream);
        }
      } catch (IOException e) {
        throw new SendException(e);
      } finally {
        setState(_PROCESSED);
      }

    }
  }

  final void endResponse() {
    if (testState(_PROCESSED)) {
      // expected, no action
    } else if (testState(_RESPONSE)) {
      // unfinished response?
      send0();
    }
  }

  private void sendStart() throws IOException {
    writeBytes(Bytes.CRLF);

    outputStream.write(buffer, 0, bufferIndex);
  }

  private class CharWritableAppendable implements Appendable {
    private final Charset charset;

    public CharWritableAppendable(Charset charset) {
      this.charset = charset;
    }

    @Override
    public Appendable append(char c) throws IOException {
      String s;
      s = Character.toString(c);

      return append(s);
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
      String s;
      s = csq.toString();

      byte[] bytes;
      bytes = s.getBytes(charset);

      buffer(bytes);

      return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
      CharSequence sub;
      sub = csq.subSequence(start, end);

      return append(sub);
    }

    private void buffer(byte[] bytes) throws IOException {
      int bytesIndex;
      bytesIndex = 0;

      int remaining;
      remaining = bytes.length - bytesIndex;

      while (remaining > 0) {
        int maxAvailable;
        maxAvailable = maxBufferSize - bufferIndex;

        if (maxAvailable == 0) {
          flush();

          maxAvailable = maxBufferSize - bufferIndex;
        }

        int bytesToCopy;
        bytesToCopy = Math.min(remaining, maxAvailable);

        int requiredIndex;
        requiredIndex = bufferIndex + bytesToCopy - 1;

        if (requiredIndex >= buffer.length) {
          int minSize;
          minSize = requiredIndex + 1;

          int newSize;
          newSize = powerOfTwo(minSize);

          if (newSize > maxBufferSize) {
            throw new UnsupportedOperationException("Implement me");
          }

          buffer = Arrays.copyOf(buffer, newSize);
        }

        System.arraycopy(bytes, bytesIndex, buffer, bufferIndex, bytesToCopy);

        bufferIndex += bytesToCopy;

        bytesIndex += bytesToCopy;

        remaining -= bytesToCopy;
      }
    }

    final void flush() throws IOException {
      int chunkLength;
      chunkLength = bufferIndex;

      String lengthDigits;
      lengthDigits = Integer.toHexString(chunkLength);

      byte[] lengthBytes;
      lengthBytes = (lengthDigits + "\r\n").getBytes(StandardCharsets.UTF_8);

      outputStream.write(lengthBytes, 0, lengthBytes.length);

      int bufferRemaining;
      bufferRemaining = buffer.length - bufferIndex;

      if (bufferRemaining >= 2) {
        buffer[bufferIndex++] = Bytes.CR;
        buffer[bufferIndex++] = Bytes.LF;

        outputStream.write(buffer, 0, bufferIndex);
      } else {
        outputStream.write(buffer, 0, bufferIndex);

        outputStream.write(Bytes.CRLF);
      }

      bufferIndex = 0;
    }
  }

  private void writeBytes(byte[] bytes) {
    int length;
    length = bytes.length;

    int requiredIndex;
    requiredIndex = bufferIndex + length - 1;

    if (requiredIndex >= buffer.length) {
      int minSize;
      minSize = requiredIndex + 1;

      int newSize;
      newSize = powerOfTwo(minSize);

      if (newSize > maxBufferSize) {
        throw new UnsupportedOperationException("Implement me");
      }

      buffer = Arrays.copyOf(buffer, newSize);
    }

    System.arraycopy(bytes, 0, buffer, bufferIndex, length);

    bufferIndex += length;
  }

  // ##################################################################
  // # END: Http.Exchange API || response
  // ##################################################################

  public final boolean keepAlive() {
    return testBit(KEEP_ALIVE);
  }

  @Override
  public final boolean processed() {
    return testState(_PROCESSED);
  }

  private void clearBit(int mask) {
    bitset &= ~mask;
  }

  private void setBit(int mask) {
    bitset |= mask;
  }

  private void setState(int value) {
    int bits;
    bits = bitset & BITS_MASK;

    bitset = bits | value;
  }

  private boolean testBit(int mask) {
    return (bitset & mask) != 0;
  }

  private boolean testState(int value) {
    int result;
    result = bitset & STATE_MASK;

    return result == value;
  }

  private String decode(String raw) {
    return URLDecoder.decode(raw, StandardCharsets.UTF_8);
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  // SocketInput

  final void parseLine() throws IOException, MaxBufferSizeException {
    int startIndex;
    startIndex = bufferIndex;

    byte needle;
    needle = Bytes.LF;

    while (true) {
      for (int i = startIndex; i < bufferLimit; i++) {
        byte maybe;
        maybe = buffer[i];

        if (maybe == needle) {
          lineLimit = i;

          return;
        }
      }

      // not inside buffer
      // let's try to read more data
      startIndex = bufferLimit;

      int writableLength;
      writableLength = buffer.length - bufferLimit;

      if (writableLength == 0) {
        // buffer is full, try to increase

        if (buffer.length == maxBufferSize) {
          throw new MaxBufferSizeException();
        }

        int newLength;
        newLength = buffer.length << 1;

        buffer = Arrays.copyOf(buffer, newLength);

        writableLength = buffer.length - bufferLimit;
      }

      int bytesRead;
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);

      if (bytesRead < 0) {
        // we assume that, if bufferLimit == 0, we're at the start of a new regular request.
        // in this case, we assume the remote closed the connection gracefully.
        // Otherwise, we assume the client disconnected in middle of a request.
        throw bufferLimit == 0
            ? new RemoteClosedException()
            : new UnexpectedEofException();
      }

      bufferLimit += bytesRead;
    }
  }

  final void resetSocketInput() {
    bufferLimit = 0;

    bufferIndex = 0;

    lineLimit = 0;

    parseStatus = ParseStatus.NORMAL;
  }

  final boolean matches(byte[] bytes) {
    int length;
    length = bytes.length;

    int toIndex;
    toIndex = bufferIndex + length;

    if (toIndex >= lineLimit) {
      // outside of line...
      return false;
    }

    boolean matches;
    matches = Arrays.equals(
        buffer, bufferIndex, toIndex,
        bytes, 0, length
    );

    if (matches) {
      bufferIndex += length;

      return true;
    } else {
      return false;
    }
  }

  final int indexOf(byte needle) {
    for (int i = bufferIndex; i < bufferLimit; i++) {
      byte maybe;
      maybe = buffer[i];

      if (maybe == needle) {
        return i;
      }
    }

    return -1;
  }

  final int indexOf(byte needleA, byte needleB) {
    for (int i = bufferIndex; i < bufferLimit; i++) {
      byte maybe;
      maybe = buffer[i];

      if (maybe == needleA) {
        return i;
      }

      if (maybe == needleB) {
        return i;
      }
    }

    return -1;
  }

  final boolean consumeIfEndOfLine() {
    if (bufferIndex < lineLimit) {
      byte next;
      next = buffer[bufferIndex++];

      if (next != Bytes.CR) {
        return false;
      }
    }

    if (bufferIndex != lineLimit) {
      return false;
    }

    // index immediately after LF
    bufferIndex++;

    return true;
  }

  final String bufferToString(int start, int end) {
    int length;
    length = end - start;

    return new String(buffer, start, length, StandardCharsets.UTF_8);
  }

  final boolean consumeIfEmptyLine() {
    int length;
    length = lineLimit - bufferIndex;

    if (length == 0) {
      bufferIndex++;

      return true;
    }

    if (length == 1) {
      byte cr;
      cr = buffer[bufferIndex];

      if (cr == Bytes.CR) {
        bufferIndex += 2;

        return true;
      }
    }

    return false;
  }

  final byte get(int index) {
    return buffer[index];
  }

  final boolean canBuffer(long contentLength) {
    int maxAvailable;
    maxAvailable = maxBufferSize - bufferIndex;

    return maxAvailable >= contentLength;
  }

  final int read(long contentLength) throws IOException {
    // unread bytes in buffer
    int unread;
    unread = bufferLimit - bufferIndex;

    if (unread >= contentLength) {
      // everything is in the buffer already -> do not read

      return 0;
    }

    // we assume canBuffer was invoked before this method...
    // i.e. max buffer size can hold everything
    int length;
    length = (int) contentLength;

    int requiredBufferLength;
    requiredBufferLength = bufferIndex + length;

    // must we increase our buffer?

    if (requiredBufferLength > buffer.length) {
      int newLength;
      newLength = powerOfTwo(requiredBufferLength);

      buffer = Arrays.copyOf(buffer, newLength);
    }

    // how many bytes must we read
    int mustReadCount;
    mustReadCount = length - unread;

    while (mustReadCount > 0) {
      int read;
      read = inputStream.read(buffer, bufferLimit, mustReadCount);

      if (read < 0) {
        return -1;
      }

      bufferLimit += read;

      mustReadCount -= read;
    }

    return length;
  }

  final long read(Path file, long contentLength) throws IOException {
    // max out buffer if necessary
    if (buffer.length < maxBufferSize) {
      buffer = Arrays.copyOf(buffer, maxBufferSize);
    }

    // unread bytes in buffer
    int unread;
    unread = bufferLimit - bufferIndex;

    // how many bytes must we read
    long mustReadCount;
    mustReadCount = contentLength - unread;

    try (OutputStream out = Files.newOutputStream(file)) {
      while (mustReadCount > 0) {
        // this is guaranteed to be an int value
        long available;
        available = buffer.length - bufferLimit;

        // this is guaranteed to be an int value
        long iteration;
        iteration = Math.min(available, mustReadCount);

        int read;
        read = inputStream.read(buffer, bufferLimit, (int) iteration);

        if (read < 0) {
          return -1;
        }

        bufferLimit += read;

        out.write(buffer, bufferIndex, bufferLimit - bufferIndex);

        bufferLimit = bufferIndex;

        mustReadCount -= read;
      }
    }

    return contentLength;
  }

}