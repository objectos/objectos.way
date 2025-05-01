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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.Socket;
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
import java.util.stream.Collectors;
import objectos.way.Http.HeaderName;
import objectos.way.Http.Status;

final class HttpExchange implements Http.Exchange, Closeable {

  private record Notes(
      Note.Long1Ref1<InetAddress> start,

      Note.Long2 readResize,
      Note.Long1Ref1<Http.Exchange> readEof,
      Note.Long1Ref1<Http.Exchange> readMaxBuffer,
      Note.Long1Ref2<Http.Exchange, IOException> readIOException,

      Note.Long1Ref2<Path, Http.Exchange> bodyFile,

      Note.Long2 writeResize,
      Note.Long1Ref1<IOException> writeIOException,

      Note.Long1Ref2<ClientError, Http.Exchange> badRequest,
      Note.Long1Ref1<Http.Exchange> uriTooLong,
      Note.Long1Ref1<Http.Exchange> requestHeaderFieldsTooLarge,
      Note.Long1Ref2<Http.Exchange, Throwable> internalServerError,
      Note.Long1Ref1<Http.Exchange> notImplemented,
      Note.Long1Ref1<Http.Exchange> httpVersionNotSupported,

      Note.Long1Ref1<Throwable> appInternalServerError
  ) {

    static Notes get() {
      final Class<?> s;
      s = Http.Exchange.class;

      return new Notes(
          Note.Long1Ref1.create(s, "STA", Note.DEBUG),

          Note.Long2.create(s, "RSZ", Note.INFO),
          Note.Long1Ref1.create(s, "EOF", Note.WARN),
          Note.Long1Ref1.create(s, "MAX", Note.WARN),
          Note.Long1Ref2.create(s, "REX", Note.ERROR),

          Note.Long1Ref2.create(s, "RBF", Note.INFO),

          Note.Long2.create(s, "WSZ", Note.INFO),
          Note.Long1Ref1.create(s, "WEX", Note.ERROR),

          Note.Long1Ref2.create(s, "400", Note.INFO),
          Note.Long1Ref1.create(s, "414", Note.INFO),
          Note.Long1Ref1.create(s, "431", Note.INFO),
          Note.Long1Ref2.create(s, "500", Note.ERROR),
          Note.Long1Ref1.create(s, "501", Note.INFO),
          Note.Long1Ref1.create(s, "505", Note.INFO),

          Note.Long1Ref1.create(s, "5o0", Note.ERROR)
      );
    }

  }

  private enum BodyKind {
    EMPTY,

    IN_BUFFER,

    FILE;
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
    VERSION_CHAR;

    private static final byte[] MESSAGE = "Invalid request line.\n".getBytes(StandardCharsets.US_ASCII);

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final Http.Status status() {
      return Http.Status.BAD_REQUEST;
    }
  }

  private enum InvalidRequestHeaders implements ClientError {
    // do not reorder, do not rename

    // header name has an invalid character
    NAME_CHAR,

    // header value has an invalid character
    VALUE_CHAR,

    // invalid header terminator, i.e., the last '\r\n'
    TERMINATOR;

    private static final byte[] MESSAGE = "Invalid request headers.\n".getBytes(StandardCharsets.US_ASCII);

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final Http.Status status() {
      return Http.Status.BAD_REQUEST;
    }
  }

  private enum MissingHostHeader implements ClientError {
    // do not reorder, do not rename

    INSTANCE;

    private static final byte[] MESSAGE = "Missing Host header.\n".getBytes(StandardCharsets.US_ASCII);

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final Http.Status status() {
      return Http.Status.BAD_REQUEST;
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
  static final byte $PARSE_HEADER_NAME = 21;
  static final byte $PARSE_HEADER_VALUE = 22;
  static final byte $PARSE_HEADER_VALUE_CONTENTS = 23;
  static final byte $PARSE_HEADER_VALUE_CR = 24;
  static final byte $PARSE_HEADER_CR = 25;

  static final byte $PARSE_BODY = 26;

  static final byte $BAD_REQUEST = 27;
  static final byte $URI_TOO_LONG = 28;
  static final byte $REQUEST_HEADER_FIELDS_TOO_LARGE = 29;
  static final byte $NOT_IMPLEMENTED = 30;
  static final byte $HTTP_VERSION_NOT_SUPPORTED = 31;

  static final byte $COMMIT = 32;
  static final byte $WRITE = 33;

  static final byte $REQUEST = 34;

  static final byte $RESPONSE_HEADERS = 35;

  static final byte $ERROR = 36;

  private static final int HARD_MAX_BUFFER_SIZE = 1 << 14;

  private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

  private static final Notes NOTES = Notes.get();

  private static final int BIT_KEEP_ALIVE = 1 << 0;
  private static final int BIT_CONTENT_LENGTH = 1 << 1;
  private static final int BIT_CHUNKED = 1 << 2;
  private static final int BIT_QUERY_STRING = 1 << 3;
  private static final int BIT_WRITE_ERROR = 1 << 4;

  private Map<String, Object> attributes;

  private int bitset;

  private BodyKind bodyKind = BodyKind.EMPTY;

  private byte[] buffer;

  private int bufferIndex;

  private int bufferLimit;

  private final Clock clock;

  private HttpHeaderName headerName;

  private Map<HttpHeaderName, HttpHeader> headers;

  private final long id = ID_GENERATOR.getAndIncrement();

  private final InputStream inputStream;

  private int mark;

  private int markEnd;

  private final int maxBufferSize;

  private Http.Method method;

  private final Note.Sink noteSink;

  private Object object;

  private final OutputStream outputStream;

  private String path;

  private Map<String, String> pathParams;

  private Map<String, Object> queryParams;

  private InetAddress remoteAddress;

  private final Http.ResponseListener responseListener;

  private final Closeable socket;

  private byte state;

  private byte stateNext;

  private StringBuilder stringBuilder;

  private Http.Version version = Http.Version.HTTP_1_1;

  public HttpExchange(Socket socket, int bufferSizeInitial, int bufferSizeMax, Clock clock, Note.Sink noteSink) throws IOException {
    final int initialSize;
    initialSize = powerOfTwo(bufferSizeInitial);

    this.buffer = new byte[initialSize];

    this.clock = clock;

    this.inputStream = socket.getInputStream();

    this.maxBufferSize = powerOfTwo(bufferSizeMax);

    this.noteSink = noteSink;

    this.outputStream = socket.getOutputStream();

    remoteAddress = socket.getInetAddress();

    responseListener = Http.NoopResponseListener.INSTANCE;

    this.socket = socket;
  }

  private HttpExchange(HttpExchangeConfig config) {

    final int initialSize;
    initialSize = powerOfTwo(config.bufferSizeInitial);

    buffer = new byte[initialSize];

    clock = config.clock;

    inputStream = config.inputStream();

    maxBufferSize = config.bufferSizeMax;

    noteSink = config.noteSink;

    outputStream = new ByteArrayOutputStream();

    responseListener = config.responseListener;

    socket = () -> {}; // noop closeable

  }

  static HttpExchange create0(Consumer<? super Http.Exchange.Options> options) {
    final HttpExchangeConfig builder;
    builder = new HttpExchangeConfig();

    options.accept(builder);

    final HttpExchange impl;
    impl = new HttpExchange(builder);

    if (!impl.shouldHandle()) {
      throw new IllegalArgumentException("Invalid request");
    }

    impl.attributes = builder.attributes;

    return impl;
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
    Throwable rethrow = null;

    try {
      socket.close();
    } catch (Throwable e) {
      rethrow = e;
    }

    if (object instanceof AutoCloseable c) {
      try {
        c.close();
      } catch (Throwable e) {
        if (rethrow == null) {
          rethrow = e;
        } else {
          rethrow.addSuppressed(e);
        }
      }
    }

    if (rethrow == null) {
      return;
    }

    if (rethrow instanceof Error err) {
      throw err;
    }

    if (rethrow instanceof RuntimeException runtime) {
      throw runtime;
    }

    if (rethrow instanceof IOException io) {
      throw io;
    }

    throw new IOException(rethrow);
  }

  public final void handle(Http.Handler handler) {
    try {
      handler.handle(this);
    } catch (Http.AbstractHandlerException ex) {
      ex.handle(this);
    } catch (Throwable t) {
      internalServerError(t);

      state = $ERROR;
    }
  }

  @Override
  public final String toString() {
    return switch (state) {
      case $COMMIT -> toStringCommit();

      case $ERROR -> toStringOutput("ERROR");

      default -> "HttpExchange[id=" + id + ",state=" + state + ",hexdump=" + bufferHex() + "]";
    };
  }

  private String bufferHex() {
    final HexFormat format;
    format = HexFormat.of();

    return format.formatHex(buffer, 0, bufferLimit);
  }

  private String toStringCommit() {
    final String message;
    message = new String(buffer, 0, bufferIndex, StandardCharsets.US_ASCII);

    final StringBuilder sb;
    sb = new StringBuilder(message);

    switch (object) {
      case byte[] bytes -> sb.append(new String(bytes, StandardCharsets.UTF_8));

      case Media.Text text -> {
        try {
          text.writeTo(sb);
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      }

      case null, default -> {}
    }

    return sb.toString();
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

  @Lang.VisibleForTesting
  final void setObject(Object value) {
    object = value;
  }

  @Lang.VisibleForTesting
  final void setState(byte value) {
    state = value;
  }

  // ##################################################################
  // # BEGIN: HTTP/1.1 state machine
  // ##################################################################

  public final boolean shouldHandle() {
    final Thread currentThread;
    currentThread = Thread.currentThread();

    if (currentThread.isInterrupted()) {
      return false;
    }

    while (state < $REQUEST) {
      state = execute(state);
    }

    mark = 0;

    return state == $REQUEST;
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
      case $PARSE_HEADER_NAME -> executeParseHeaderName();
      case $PARSE_HEADER_VALUE -> executeParseHeaderValue();
      case $PARSE_HEADER_VALUE_CONTENTS -> executeParseHeaderValueContents();
      case $PARSE_HEADER_VALUE_CR -> executeParseHeaderValueCR();
      case $PARSE_HEADER_CR -> executeParseHeaderCR();

      case $PARSE_BODY -> executeParseBody();

      case $BAD_REQUEST -> executeBadRequest();
      case $URI_TOO_LONG -> executeUriTooLong();
      case $REQUEST_HEADER_FIELDS_TOO_LARGE -> executeRequestHeaderFieldsTooLarge();
      case $NOT_IMPLEMENTED -> executeNotImplemented();
      case $HTTP_VERSION_NOT_SUPPORTED -> executeHttpVersionNotSupported();

      case $COMMIT -> executeCommit();

      case $WRITE -> executeWrite();

      default -> throw new AssertionError("Unexpected state=" + state);
    };
  }

  // ##################################################################
  // # END: HTTP/1.1 state machine
  // ##################################################################

  // ##################################################################
  // # BEGIN: Start
  // ##################################################################

  private byte executeStart() {
    noteSink.send(NOTES.start, id, remoteAddress);

    if (attributes != null) {
      attributes.clear();
    }

    bitset = 0;

    bodyKind = BodyKind.EMPTY;

    bufferLimit = 0;

    bufferIndex = 0;

    headerName = null;

    if (headers != null) {
      headers.clear();
    }

    mark = 0;

    method = null;

    if (object instanceof AutoCloseable c) {
      try {
        c.close();
      } catch (Exception e) {
        return internalServerError(e);
      }
    }

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
  // # END: Start
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
      return clientReadIOException(e);
    }
  }

  private byte executeReadMaxBuffer() {
    return switch (stateNext) {
      case $PARSE_PATH,
           $PARSE_PATH_CONTENTS0,
           $PARSE_PATH_CONTENTS1,
           $PARSE_PATH_DECODE,
           $PARSE_QUERY,
           $PARSE_QUERY0,
           $PARSE_QUERY1,
           $PARSE_QUERY1_DECODE,
           $PARSE_QUERY_VALUE,
           $PARSE_QUERY_VALUE0,
           $PARSE_QUERY_VALUE1,
           $PARSE_QUERY_VALUE1_DECODE -> executeUriTooLong();

      case $PARSE_HEADER,
           $PARSE_HEADER_NAME,
           $PARSE_HEADER_VALUE,
           $PARSE_HEADER_VALUE_CONTENTS,
           $PARSE_HEADER_VALUE_CR,
           $PARSE_HEADER_CR -> executeRequestHeaderFieldsTooLarge();

      default -> { note(NOTES.readMaxBuffer); yield $ERROR; }
    };
  }

  private byte executeReadEof() {
    return switch (stateNext) {
      case $PARSE_METHOD -> bufferLimit == 0 ? $ERROR : toBadRequest(InvalidRequestLine.METHOD);

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
    if (bufferIndex < bufferLimit) {
      // marks path start
      mark = bufferIndex;

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

        case PATH_SPACE -> { path = markToString(); bufferIndex += 1; return pv($PARSE_VERSION_1_1); }

        case PATH_QUESTION -> { path = markToString(); bufferIndex += 1; return pv($PARSE_QUERY); }

        case PATH_CRLF -> { path = markToString(); bufferIndex += 1; return pv($PARSE_VERSION_0_9); }

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

        case PATH_SPACE -> { path = appendToString(); bufferIndex += 1; return pv($PARSE_VERSION_1_1); }

        case PATH_QUESTION -> { path = appendToString(); bufferIndex += 1; return pv($PARSE_QUERY); }

        case PATH_CRLF -> { path = appendToString(); bufferIndex += 1; return pv($PARSE_VERSION_0_9); }

        default -> { return toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR); }
      }
    }

    return toRead($PARSE_PATH_CONTENTS1);
  }

  private byte executeParsePathDecode() {
    return decodePercent($PARSE_PATH_CONTENTS1, $PARSE_PATH_DECODE, InvalidRequestLine.PATH_PERCENT);
  }

  private byte pv(byte next) {
    final int length;
    length = path.length();

    if (length == 0) {
      return toBadRequest(InvalidRequestLine.PATH_FIRST_CHAR);
    }

    final char first;
    first = path.charAt(0);

    if (first != '/') {
      return toBadRequest(InvalidRequestLine.PATH_FIRST_CHAR);
    }

    if (length == 1) {
      return next;
    }

    final char second;
    second = path.charAt(1);

    if (second == '/') {
      return toBadRequest(InvalidRequestLine.PATH_SEGMENT_NZ);
    }

    return next;
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

    bitSet(BIT_QUERY_STRING);

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

        case QUERY_SPACE -> { makeIfNecessary(); bufferIndex += 1; return $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { makeIfNecessary(); bufferIndex += 1; return $PARSE_VERSION_0_9; }

        default -> { return toBadRequest(InvalidRequestLine.QUERY_CHAR); }
      }
    }

    return toRead($PARSE_QUERY0);
  }

  private void makeIfNecessary() {
    if (mark != bufferIndex || (queryParams != null && !queryParams.isEmpty())) {
      // make when
      // 1) key is not empty
      // 2) not the first key/value
      object = markToString();

      makeQueryParam("");
    }
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

      if (c < 0x80 || c > 0x7FF) {
        return toBadRequest(badRequest);
      }

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

      if (c < 0x800 || c > 0xFFFF || Character.isSurrogate((char) c)) {
        return toBadRequest(badRequest);
      }

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

      if (c < 0x1_0000 || !Character.isValidCodePoint(c)) {
        return toBadRequest(badRequest);
      }

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
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      return switch (b) {
        case Bytes.CR -> {
          bufferIndex++;
          yield executeParseHeaderCR();

        }

        case Bytes.LF -> toBadRequest(InvalidLineTerminator.INSTANCE);

        case Bytes.SP -> throw new UnsupportedOperationException("obs-fold not supported");

        case Bytes.HTAB -> throw new UnsupportedOperationException("obs-fold not supported");

        default -> {
          stringBuilderInit();

          yield executeParseHeaderName();

        }
      };
    } else {
      return toRead($PARSE_HEADER);
    }
  }

  private byte executeParseHeaderName() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte mapped;
      mapped = HttpHeaderName.map(b);

      switch (mapped) {
        case HttpHeaderName.INVALID -> {
          return toBadRequest(InvalidRequestHeaders.NAME_CHAR);
        }

        case HttpHeaderName.COLON -> {
          final String lowerCase;
          lowerCase = appendToString();

          final HttpHeaderName standard;
          standard = HttpHeaderName.byLowerCase(lowerCase);

          if (standard != null) {
            headerName = standard;
          } else {
            headerName = HttpHeaderName.ofLowerCase(lowerCase);
          }

          bufferIndex += 1;

          return $PARSE_HEADER_VALUE;
        }

        default -> { appendChar(mapped); bufferIndex++; }
      }
    }

    return toRead($PARSE_HEADER_NAME);
  }

  private byte executeParseHeaderValue() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b == Bytes.SP || b == Bytes.HTAB) {
        // we allow OWS after the ':'
        bufferIndex++;

        continue;
      }

      // we mark where the header value starts
      mark = markEnd = bufferIndex;

      return executeParseHeaderValueContents();
    }

    return toRead($PARSE_HEADER_VALUE);
  }

  private static final byte[] PARSE_HEADER_VALUE_TABLE;

  private static final byte HEADER_VALUE_VALID = 1;

  private static final byte HEADER_VALUE_WS = 2;

  private static final byte HEADER_VALUE_CR = 3;

  private static final byte HEADER_VALUE_LF = 4;

  static {
    final byte[] table;
    table = new byte[128];

    for (int b = 0x21; b < 0x7F; b++) {
      // VCHAR are valid
      table[b] = HEADER_VALUE_VALID;
    }

    // valid under certain circustances
    table[' '] = HEADER_VALUE_WS;

    table['\t'] = HEADER_VALUE_WS;

    table['\r'] = HEADER_VALUE_CR;

    table['\n'] = HEADER_VALUE_LF;

    PARSE_HEADER_VALUE_TABLE = table;
  }

  private byte executeParseHeaderValueContents() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestHeaders.VALUE_CHAR);
      }

      final byte test;
      test = PARSE_HEADER_VALUE_TABLE[b];

      switch (test) {
        default -> { return toBadRequest(InvalidRequestHeaders.VALUE_CHAR); }

        case HEADER_VALUE_VALID -> { bufferIndex += 1; markEnd = bufferIndex; }

        case HEADER_VALUE_WS -> { bufferIndex += 1; }

        case HEADER_VALUE_CR -> { bufferIndex += 1; return executeParseHeaderValueCR(); }

        case HEADER_VALUE_LF -> { return toBadRequest(InvalidLineTerminator.INSTANCE); }
      }
    }

    return toRead($PARSE_HEADER_VALUE_CONTENTS);
  }

  private byte executeParseHeaderValueCR() {
    if (bufferIndex < bufferLimit) {
      final byte lf;
      lf = buffer[bufferIndex++];

      if (lf != Bytes.LF) {
        return toBadRequest(InvalidRequestHeaders.VALUE_CHAR);
      }

      if (mark < 0 || markEnd < 0) {
        return $ERROR;
      }

      if (markEnd - mark < 0) {
        return $ERROR;
      }

      final HttpHeader header;
      header = HttpHeader.create(mark, markEnd);

      if (headers == null) {
        headers = Util.createMap();
      }

      final HttpHeader existing;
      existing = headers.put(headerName, header);

      if (existing != null) {
        existing.add(header);

        headers.put(headerName, existing);
      }

      return $PARSE_HEADER;
    } else {
      return toRead($PARSE_HEADER_VALUE_CR);
    }
  }

  private byte executeParseHeaderCR() {
    if (bufferIndex < bufferLimit) {
      final byte lf;
      lf = buffer[bufferIndex++];

      if (lf != Bytes.LF) {
        return toBadRequest(InvalidRequestHeaders.TERMINATOR);
      }

      final HttpHeader host;
      host = headerUnchecked(HttpHeaderName.HOST);

      if (host == null) {
        return toBadRequest(MissingHostHeader.INSTANCE);
      }

      final String hostValue;
      hostValue = host.get(buffer);

      if (hostValue.isBlank()) {
        return toBadRequest(MissingHostHeader.INSTANCE);
      }

      // we only support HTTP/1.1
      // so we begin with keep-alive = true
      boolean keepAlive;
      keepAlive = true;

      final HttpHeader connection;
      connection = headerUnchecked(HttpHeaderName.CONNECTION);

      if (connection != null) {
        final String value;
        value = connection.get(buffer);

        if (value != null && value.equalsIgnoreCase("close")) {
          keepAlive = false;
        }
      }

      if (keepAlive) {
        bitSet(BIT_KEEP_ALIVE);
      } else {
        bitClear(BIT_KEEP_ALIVE);
      }

      return $PARSE_BODY;
    } else {
      return toRead($PARSE_HEADER_CR);
    }
  }

  // ##################################################################
  // # END: Parse: Headers
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: Body
  // ##################################################################

  private byte executeParseBody() {
    final HttpHeader contentLength;
    contentLength = headerUnchecked(HttpHeaderName.CONTENT_LENGTH);

    if (contentLength != null) {
      return executeParseBodyFixed(contentLength);
    }

    final HttpHeader transferEncoding;
    transferEncoding = headerUnchecked(HttpHeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      // TODO 501 Not Implemented
      throw new UnsupportedOperationException("Implement me");
    }

    // TODO 411 Length Required
    return $REQUEST;
  }

  private byte executeParseBodyFixed(HttpHeader contentLength) {
    final long length;
    length = contentLength.unsignedLongValue(buffer);

    if (length < 0) {
      // length overflow: we do not handle
      // TODO 413 Payload Too Large
      throw new UnsupportedOperationException("Implement me");
    }

    else if (length == 0) {
      // empty body
      return executeParseBodyFixedZero();
    }

    else if (canBuffer(length)) {
      // fits in buffer
      return executeParseBodyFixedBuffer(length);
    }

    else {
      // does not fit buffer
      return executeParseBodyFixedFile(length);
    }
  }

  private byte executeParseBodyFixedZero() {
    bodyKind = BodyKind.EMPTY;

    return $REQUEST;
  }

  private byte executeParseBodyFixedBuffer(long contentLength) {
    try {
      // unread bytes in buffer
      final int unread;
      unread = bufferLimit - bufferIndex;

      if (unread < contentLength) {

        // we assume canBuffer was invoked before this method...
        // i.e. max buffer size can hold everything
        final int length;
        length = (int) contentLength;

        final int requiredBufferLength;
        requiredBufferLength = bufferIndex + length;

        // must we increase our buffer?
        if (requiredBufferLength > buffer.length) {
          final int newLength;
          newLength = powerOfTwo(requiredBufferLength);

          buffer = Arrays.copyOf(buffer, newLength);

          noteSink.send(NOTES.readResize, id, newLength);
        }

        // how many bytes must we read
        int mustReadCount;
        mustReadCount = length - unread;

        while (mustReadCount > 0) {
          final int read;
          read = inputStream.read(buffer, bufferLimit, mustReadCount);

          if (read < 0) {
            noteSink.send(NOTES.readEof, id, this);

            return $ERROR;
          }

          bufferLimit += read;

          mustReadCount -= read;
        }

      }

      bodyKind = BodyKind.IN_BUFFER;

      return $REQUEST;
    } catch (IOException e) {
      return clientReadIOException(e);
    }
  }

  private byte executeParseBodyFixedFile(long contentLength) {
    HttpExchangeTmp tmp;

    try {
      if (object == null) {
        tmp = HttpExchangeTmp.ofFile();

        object = tmp;
      } else {
        // support testing
        tmp = (HttpExchangeTmp) object;
      }
    } catch (IOException e) {
      return internalServerError(e);
    }

    // max out buffer if necessary
    if (buffer.length < maxBufferSize) {
      buffer = Arrays.copyOf(buffer, maxBufferSize);

      noteSink.send(NOTES.readResize, id, maxBufferSize);
    }

    // prepare OutputStream

    final OutputStream out;

    try {
      out = tmp.output();
    } catch (IOException e) {
      return internalServerError(e);
    }

    // unread bytes in buffer
    final int unread;
    unread = bufferLimit - bufferIndex;

    // how many bytes must we read
    long mustReadCount;
    mustReadCount = contentLength - unread;

    try (out) {

      while (mustReadCount > 0) {
        // this is guaranteed to be an int value
        final long available;
        available = buffer.length - bufferLimit;

        // this is guaranteed to be an int value
        final long iteration;
        iteration = Math.min(available, mustReadCount);

        final int read;

        try {
          read = inputStream.read(buffer, bufferLimit, (int) iteration);
        } catch (IOException e) {
          return clientReadIOException(e);
        }

        if (read < 0) {
          return -1;
        }

        bufferLimit += read;

        try {
          out.write(buffer, bufferIndex, bufferLimit - bufferIndex);
        } catch (IOException e) {
          return internalServerError(e);
        }

        bufferLimit = bufferIndex;

        mustReadCount -= read;
      }

    } catch (IOException e) {
      return internalServerError(e);
    }

    bodyKind = BodyKind.FILE;

    return $REQUEST;
  }

  // ##################################################################
  // # END: Parse: Body
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

    final byte[] message;
    message = clientError.message();

    statusUnchecked(Http.Status.BAD_REQUEST);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, message.length);

    headerUnchecked(Http.HeaderName.CONNECTION, "close");

    sendUnchecked(message);

    return toWrite($ERROR);
  }

  private byte executeUriTooLong() {
    noteSink.send(NOTES.uriTooLong, id, this);

    statusUnchecked(Http.Status.URI_TOO_LONG);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, "0");

    headerUnchecked(Http.HeaderName.CONNECTION, "close");

    send();

    return toWrite($ERROR);
  }

  private byte executeRequestHeaderFieldsTooLarge() {
    noteSink.send(NOTES.requestHeaderFieldsTooLarge, id, this);

    statusUnchecked(Http.Status.REQUEST_HEADER_FIELDS_TOO_LARGE);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, "0");

    headerUnchecked(Http.HeaderName.CONNECTION, "close");

    send();

    return toWrite($ERROR);
  }

  private byte executeNotImplemented() {
    noteSink.send(NOTES.notImplemented, id, this);

    statusUnchecked(Http.Status.NOT_IMPLEMENTED);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, "0");

    headerUnchecked(Http.HeaderName.CONNECTION, "close");

    send();

    return toWrite($ERROR);
  }

  private static final byte[] HTTP_VERSION_NOT_SUPPORTED_MSG = "Supported versions: HTTP/1.1\n".getBytes(StandardCharsets.US_ASCII);

  private byte executeHttpVersionNotSupported() {
    noteSink.send(NOTES.httpVersionNotSupported, id, this);

    final byte[] message;
    message = HTTP_VERSION_NOT_SUPPORTED_MSG;

    statusUnchecked(Http.Status.HTTP_VERSION_NOT_SUPPORTED);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, message.length);

    headerUnchecked(Http.HeaderName.CONNECTION, "close");

    sendUnchecked(message);

    return toWrite($ERROR);
  }

  private static final byte[] INTERNAL_SERVER_ERROR_MSG = "The server encountered an internal error and was unable to complete your request.\n".getBytes(StandardCharsets.US_ASCII);

  final byte internalServerError(Throwable ise) {
    noteSink.send(NOTES.internalServerError, id, this, ise);

    final byte[] message;
    message = INTERNAL_SERVER_ERROR_MSG;

    statusUnchecked(Http.Status.INTERNAL_SERVER_ERROR);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, message.length);

    headerUnchecked(Http.HeaderName.CONNECTION, "close");

    sendUnchecked(message);

    return toWrite($ERROR);
  }

  // ##################################################################
  // # END: Response: Early (internal)
  // ##################################################################

  // ##################################################################
  // # BEGIN: Commit
  // ##################################################################

  private byte executeCommit() {
    if (!bitTest(BIT_WRITE_ERROR)) {
      stateNext = bitTest(BIT_KEEP_ALIVE) ? $START : $ERROR;

      return executeWrite();
    } else {
      return $ERROR;
    }
  }

  // ##################################################################
  // # END: Commit
  // ##################################################################

  // ##################################################################
  // # BEGIN: Write
  // ##################################################################

  private byte executeWrite() {
    return switch (object) {
      case null -> executeWriteEmpty();

      case byte[] bytes -> executeWriteBytes(bytes);

      case Path file -> executeWriteFile(file);

      case Media.Text text -> executeWriteText(text);

      case Object o -> executeWriteUnknown(o);
    };
  }

  private byte executeWriteEmpty() {
    try {
      writeBuffer();

      return stateNext;
    } catch (IOException e) {
      return clientWriteIOException(e);
    }
  }

  private byte executeWriteBytes(byte[] bytes) {
    try {
      writeBuffer();

      outputStream.write(bytes);

      return stateNext;
    } catch (IOException e) {
      return clientWriteIOException(e);
    }
  }

  private byte executeWriteFile(Path file) {
    try (InputStream in = Files.newInputStream(file)) {
      writeBuffer();

      in.transferTo(outputStream);

      return stateNext;
    } catch (IOException e) {
      return clientWriteIOException(e);
    }
  }

  private static final byte[] CHUNKED_TRAILER = "0\r\n\r\n".getBytes(StandardCharsets.UTF_8);

  /**
   * Differentiates between a Socket OutputStream writing error and other
   * errors.
   */
  @SuppressWarnings("serial")
  private static final class ThisAppendableIOException extends RuntimeException {
    public ThisAppendableIOException(IOException cause) {
      super(cause);
    }

    @Override
    public final IOException getCause() {
      return (IOException) super.getCause();
    }
  }

  private class Chunked implements Appendable, Closeable {
    private final Charset charset;

    public Chunked(Charset charset) {
      this.charset = charset;

      beginChunk();
    }

    @Override
    public final void close() {
      endChunk();

      final int available;
      available = buffer.length - bufferIndex;

      final int trailerLength;
      trailerLength = CHUNKED_TRAILER.length;

      if (available < trailerLength) {
        throw new UnsupportedOperationException("Implement me");
      }

      System.arraycopy(CHUNKED_TRAILER, 0, buffer, bufferIndex, trailerLength);

      bufferIndex += trailerLength;

      flush();
    }

    @Override
    public final Appendable append(char c) {
      final String s;
      s = Character.toString(c);

      return append(s);
    }

    @Override
    public final Appendable append(CharSequence csq, int start, int end) {
      final CharSequence sub;
      sub = csq.subSequence(start, end);

      return append(sub);
    }

    @Override
    public final Appendable append(CharSequence csq) {
      final String s;
      s = csq.toString();

      final byte[] bytes;
      bytes = s.getBytes(charset);

      writeBytes(bytes);

      return this;
    }

    private void writeBytes(byte[] bytes) {
      int bytesIndex;
      bytesIndex = 0;

      int remaining;
      remaining = bytes.length;

      // max index to write all data
      final int requiredIndex;
      requiredIndex = bufferIndex + remaining;

      // max index to write all data + CRLF + trailer
      final int maxRequiredIndex;
      maxRequiredIndex = requiredIndex + CHUNKED_TRAILER.length + 2;

      if (maxRequiredIndex >= buffer.length && buffer.length < maxBufferSize) {

        // buffer resize

        final int length;
        length = powerOfTwo(maxRequiredIndex + 1);

        final int newLength;
        newLength = Math.min(length, maxBufferSize);

        buffer = Arrays.copyOf(buffer, newLength);

        noteSink.send(NOTES.writeResize, id, newLength);

      }

      while (remaining > 0) {
        int available;
        available = available();

        if (available <= 0) {
          endChunk();

          flush();

          resetChunk();

          beginChunk();

          available = available();
        }

        final int bytesToCopy;
        bytesToCopy = Math.min(remaining, available);

        System.arraycopy(bytes, bytesIndex, buffer, bufferIndex, bytesToCopy);

        bufferIndex += bytesToCopy;

        bytesIndex += bytesToCopy;

        remaining -= bytesToCopy;
      }
    }

    private int available() {
      return buffer.length - (bufferIndex + CHUNKED_TRAILER.length + 2);
    }

    private void flush() {
      try {
        outputStream.write(buffer, 0, bufferIndex);
      } catch (IOException e) {
        throw new ThisAppendableIOException(e);
      }
    }

    private void beginChunk() {
      // mark where the chunk begins
      mark = markEnd = bufferIndex;

      // save space for (max) chunk-size
      markEnd += maxHexDigits();

      // save space for CRLF
      markEnd += 2;

      // marks where chunk data begins
      bufferIndex = markEnd;

      final int available;
      available = buffer.length - markEnd;

      if (available <= 0) {
        throw new UnsupportedOperationException("Implement me");
      }
    }

    private int maxHexDigits() {
      // buffer may get to this max size
      int maxDataLength;
      maxDataLength = maxBufferSize;

      // buffer must hold the last zero chunk
      maxDataLength -= CHUNKED_TRAILER.length;

      // must hold the CRLF after data
      maxDataLength -= 2;

      // must hold the CRLF after chunk-size
      maxDataLength -= 2;

      // must hold at least 1 digit of the chunk-size
      maxDataLength -= 1;

      return Http.requiredHexDigits(maxDataLength);
    }

    private void endChunk() {
      // writes out the chunk size
      int sizeIndex;
      sizeIndex = markEnd - 1;

      buffer[sizeIndex--] = Bytes.LF;
      buffer[sizeIndex--] = Bytes.CR;

      int chunkLength;
      chunkLength = bufferIndex - markEnd;

      final int hexDigits;
      hexDigits = Http.requiredHexDigits(chunkLength);

      for (int i = 0; i < hexDigits; i++) {
        final int nibble;
        nibble = chunkLength & 0xF;

        final byte digit;
        digit = Http.hexDigit(nibble);

        buffer[sizeIndex--] = digit;

        chunkLength >>= 4;
      }

      // left pad the size with '0' digits

      while (sizeIndex >= mark) {
        buffer[sizeIndex--] = '0';
      }

      // buffer the data CRLF

      buffer[bufferIndex++] = Bytes.CR;
      buffer[bufferIndex++] = Bytes.LF;
    }

    private void resetChunk() {
      mark = markEnd = bufferIndex = 0;
    }

  }

  private byte executeWriteText(Media.Text text) {
    final Charset charset;
    charset = text.charset();

    try (Chunked out = new Chunked(charset)) {
      text.writeTo(out);

      return stateNext;
    } catch (ThisAppendableIOException e) {
      final IOException cause;
      cause = e.getCause();

      return clientWriteIOException(cause);
    } catch (Throwable t) {
      return internalServerError(t);
    }
  }

  private byte executeWriteUnknown(Object o) {
    // most likely a bug!
    // TODO log

    return $ERROR;
  }

  private byte clientWriteIOException(IOException e) {
    noteSink.send(NOTES.writeIOException, id, e);

    return $ERROR;
  }

  private byte toWrite(byte next) {
    stateNext = next;

    return $WRITE;
  }

  private void writeBuffer() throws IOException {
    outputStream.write(buffer, 0, bufferIndex);
  }

  // ##################################################################
  // # END: Write
  // ##################################################################

  // ##################################################################
  // # BEGIN: Error Conditions
  // ##################################################################

  private byte clientReadIOException(IOException e) {
    noteSink.send(NOTES.readIOException, id, this, e);

    return $ERROR;
  }

  // ##################################################################
  // # END: Error Conditions
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request attributes
  // ##################################################################

  @Override
  public final <T> void set(Class<T> key, T value) {
    final String name;
    name = key.getName(); // implicit null check

    Objects.requireNonNull(value, "value == null");

    final Map<String, Object> map;
    map = attributes();

    map.put(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T get(Class<T> key) {
    final String name;
    name = key.getName(); // implicit null check

    final Map<String, Object> map;
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
  // # BEGIN: Http.Exchange API || request line
  // ##################################################################

  @Override
  public final Http.Method method() {
    checkRequest();

    return method;
  }

  @Override
  public final String path() {
    checkRequest();

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

  final int pathIndex() {
    return mark;
  }

  final void pathIndex(int value) {
    mark = value;
  }

  final void pathIndexAdd(int value) {
    mark += value;
  }

  @Lang.VisibleForTesting
  final Map<String, String> pathParams() {
    return pathParams;
  }

  final void pathParamsPut(String name, String value) {
    if (pathParams == null) {
      pathParams = Util.createMap();
    }

    pathParams.put(name, value);
  }

  final void pathReset() {
    mark = 0;

    if (pathParams != null) {
      pathParams.clear();
    }
  }

  final String pathUnchecked() {
    return path;
  }

  @Override
  public final String queryParam(String name) {
    checkRequest();
    Objects.requireNonNull(name, "name == null");

    if (queryParams == null) {
      return null;
    } else {
      return Http.queryParamsGet(queryParams, name);
    }
  }

  @Override
  public final List<String> queryParamAll(String name) {
    checkRequest();
    Objects.requireNonNull(name, "name == null");

    if (queryParams == null) {
      return List.of();
    } else {
      return Http.queryParamsGetAll(queryParams, name);
    }
  }

  @Override
  public final Set<String> queryParamNames() {
    checkRequest();

    if (queryParams == null) {
      return Set.of();
    } else {
      return queryParams.keySet();
    }
  }

  @Override
  public final Http.Version version() {
    checkRequest();

    return version;
  }

  @Override
  public final String rawPath() {
    checkRequest();

    return raw(path, PARSE_PATH_TABLE);
  }

  @Override
  public final String rawQuery() {
    checkRequest();

    if (!bitTest(BIT_QUERY_STRING)) {
      return null;
    }

    else if (queryParams == null) {
      return "";
    }

    else {
      return Http.queryParamsToString(queryParams, this::rawQuery0);
    }
  }

  @Override
  public final String rawQueryWith(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    Map<String, Object> params;
    params = Util.createSequencedMap();

    if (queryParams != null) {
      params.putAll(queryParams);
    }

    params.put(name, value);

    return Http.queryParamsToString(params, this::rawQuery0);
  }

  private String rawQuery0(String s) {
    return raw(s, PARSE_QUERY_TABLE);
  }

  private String raw(String input, byte[] table) {
    final int len;
    len = input.length();

    if (len == 0) {
      return input;
    }

    int firstToEncode;
    firstToEncode = -1;

    for (int i = 0; i < len; i++) {
      final char c;
      c = input.charAt(i);

      if (c > 0x7F) {
        firstToEncode = i;

        break;
      }

      final byte code;
      code = table[c];

      if (code != 1) {
        firstToEncode = i;

        break;
      }
    }

    if (firstToEncode == -1) {
      return input;
    }

    final int worstCaseChars;
    worstCaseChars = len - firstToEncode;

    final int initialBytesLength;
    initialBytesLength = (firstToEncode + 1) + (worstCaseChars * 3);

    byte[] bytes;
    bytes = new byte[initialBytesLength];

    int bytesIndex;
    bytesIndex = 0;

    for (int i = 0; i < firstToEncode; i++) {
      bytes[bytesIndex++] = (byte) input.charAt(i);
    }

    char highSurrogate;
    highSurrogate = 0;

    for (int i = firstToEncode; i < input.length(); i++) {
      final char c;
      c = input.charAt(i);

      if (c <= 0x7F) {
        highSurrogate = ensureZero(highSurrogate);

        final byte code;
        code = table[c];

        if (code == 1) {
          bytes = ensureBytes(bytes, bytesIndex, 1);

          bytes[bytesIndex++] = (byte) c;
        } else {
          bytes = ensureBytes(bytes, bytesIndex, 3);

          bytesIndex = raw(bytes, bytesIndex, c);
        }
      }

      else if (c <= 0x7FF) {
        highSurrogate = ensureZero(highSurrogate);

        // 110xxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 6);

        final int byte0;
        byte0 = 0b1100_0000 | (c >> 6); // c <= 0x7FF, no higher bits set.

        final int byte1;
        byte1 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);
      }

      else if (Character.isHighSurrogate(c)) {
        ensureZero(highSurrogate);

        highSurrogate = c;
      }

      else if (Character.isLowSurrogate(c)) {
        if (highSurrogate == 0) {
          throw new IllegalArgumentException("Low surrogate \\u" + Integer.toHexString(c) + " must be preceeded by a high surrogate.");
        }

        int codePoint;
        codePoint = Character.toCodePoint(highSurrogate, c);

        highSurrogate = 0;

        // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 12);

        final int byte0;
        byte0 = 0b1111_0000 | (codePoint >> 18);

        final int byte1;
        byte1 = 0b1000_0000 | ((codePoint >> 12) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | ((codePoint >> 6) & 0b0011_1111);

        final int byte3;
        byte3 = 0b1000_0000 | (codePoint & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);

        bytesIndex = raw(bytes, bytesIndex, byte3);
      }

      else if (c <= 0xFFFF) {
        highSurrogate = ensureZero(highSurrogate);

        // 1110wwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 9);

        final int byte0;
        byte0 = 0b1110_0000 | (c >> 12);

        final int byte1;
        byte1 = 0b1000_0000 | ((c >> 6) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);
      }
    }

    if (highSurrogate != 0) {
      throw new IllegalArgumentException("Unmatched high surrogate at end of string");
    }

    return new String(bytes, 0, bytesIndex, StandardCharsets.US_ASCII);
  }

  private char ensureZero(char highSurrogate) {
    if (highSurrogate != 0) {
      throw new IllegalArgumentException("High surrogate \\u" + Integer.toHexString(highSurrogate) + " must be followed by a low surrogate.");
    }

    return 0;
  }

  private byte[] ensureBytes(byte[] bytes, int bytesIndex, int requiredLength) {
    final int requiredIndex;
    requiredIndex = bytesIndex + requiredLength - 1;

    return Util.growIfNecessary(bytes, requiredIndex);
  }

  private int raw(byte[] bytes, int bytesIndex, int value) {
    // value is < 256
    bytes[bytesIndex++] = '%';
    bytes[bytesIndex++] = hexDigit(value >> 4);
    bytes[bytesIndex++] = hexDigit(value & 0b1111);

    return bytesIndex;
  }

  private byte hexDigit(int nibble) {
    return (byte) (nibble < 10 ? '0' + nibble : 'A' + (nibble - 10));
  }

  // ##################################################################
  // # END: Http.Exchange API || request line
  // ##################################################################

  // ##################################################################
  // # BEGIN: Http.Exchange API || request headers
  // ##################################################################

  @Override
  public final String header(Http.HeaderName name) {
    checkRequest();
    Objects.requireNonNull(name, "name == null");

    final HttpHeader maybe;
    maybe = headerUnchecked(name);

    if (maybe == null) {
      return null;
    }

    return maybe.get(buffer);
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

  @Override
  public final InputStream bodyInputStream() throws IOException {
    checkRequest();

    return switch (bodyKind) {
      case EMPTY -> InputStream.nullInputStream();

      case IN_BUFFER -> new ByteArrayInputStream(buffer, bufferIndex, bufferLimit - bufferIndex);

      case FILE -> {
        if (object instanceof HttpExchangeTmp tmp) {
          yield tmp.input();
        } else {
          throw new IOException("");
        }
      }
    };
  }

  // ##################################################################
  // # END: Http.Exchange API || request body
  // ##################################################################

  private void checkRequest() {
    Check.state(
        state == $REQUEST,

        """
        This request method can only be invoked:
        - after a successful shouldHandle() operation; and
        - before a response has been sent.
        """
    );
  }

  // ##################################################################
  // # BEGIN: Http.Exchange API || Response
  // ##################################################################

  // 2xx responses

  @Override
  public final void ok(Media.Bytes media) {
    respond(Http.Status.OK, media);
  }

  @Override
  public final void ok(Media.Text media) {
    respond(Http.Status.OK, media);
  }

  public final void ok(Media.Stream media) {
    throw new UnsupportedOperationException();
  }

  // 3xx responses

  @Override
  public final void movedPermanently(String location) {
    final String nonNull;
    nonNull = Objects.requireNonNull(location, "location == null");

    location(Http.Status.MOVED_PERMANENTLY, nonNull);
  }

  @Override
  public final void found(String location) {
    final String nonNull;
    nonNull = Objects.requireNonNull(location, "location == null");

    location(Http.Status.FOUND, nonNull);
  }

  @Override
  public final void seeOther(String location) {
    final String nonNull;
    nonNull = Objects.requireNonNull(location, "location == null");

    location(Http.Status.SEE_OTHER, nonNull);
  }

  private void location(Http.Status status, String location) {
    final String raw;
    raw = Http.raw(location);

    statusUnchecked(status);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, 0L);

    headerUnchecked(Http.HeaderName.LOCATION, raw);

    send();
  }

  // 4xx responses

  @Override
  public final void badRequest(Media media) {
    respond(Http.Status.BAD_REQUEST, media);
  }

  @Override
  public final void notFound(Media media) {
    respond(Http.Status.NOT_FOUND, media);
  }

  @Override
  public final void allow(Http.Method... methods) {
    Objects.requireNonNull(methods, "methods == null");

    final String allow;
    allow = Arrays.stream(methods).map(Http.Method::name).collect(Collectors.joining(", "));

    statusUnchecked(Http.Status.METHOD_NOT_ALLOWED);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.ALLOW, allow);

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, 0L);

    send();
  }

  // 5xx responses

  @Override
  public final void internalServerError(Media media, Throwable error) {
    noteSink.send(NOTES.appInternalServerError, id, error);

    respond(Http.Status.INTERNAL_SERVER_ERROR, media);
  }

  // response builder

  final class ResponseHandle implements Http.Response {

    @Override
    public final void status(Http.Status value) {
      HttpExchange.this.status(value);
    }

    @Override
    public final void header(HeaderName name, long value) {
      HttpExchange.this.header(name, value);
    }

    @Override
    public final void header(HeaderName name, String value) {
      HttpExchange.this.header(name, value);
    }

    @Override
    public final String now() {
      return HttpExchange.this.now();
    }

    @Override
    public final void body() {
      checkBody();

      HttpExchange.this.send();
    }

    @Override
    public final void body(byte[] bytes, int offset, int length) {
      checkBody();

      final byte[] copy;
      copy = new byte[length];

      System.arraycopy(bytes, offset, copy, 0, length);

      HttpExchange.this.sendUnchecked(copy);
    }

    private void checkBody() {
      if (state != $RESPONSE_HEADERS) {
        throw new IllegalStateException(
            "Body must be the last part of a response message."
        );
      }
    }

  }

  @Override
  public final void respond(Consumer<? super Http.Response> response) {
    final ResponseHandle handle;
    handle = new ResponseHandle();

    response.accept(handle);
  }

  // ##################################################################
  // # END: Http.Exchange API || Response
  // ##################################################################

  // ##################################################################
  // # BEGIN: Internal Response API
  // ##################################################################

  private void checkResponseHeaders() {
    if (state != $RESPONSE_HEADERS) {
      throw new IllegalStateException(
          "Response header can only be set after the status line and before the response body."
      );
    }
  }

  private void respond(Http.Status status, Media media) {
    Objects.requireNonNull(media, "media == null");

    switch (media) {
      case Media.Bytes bytes -> respond(status, bytes);

      case Media.Text text -> respond(status, text);

      default -> throw new IllegalArgumentException("Unexpected value: " + media);
    }
  }

  private void respond(Http.Status status, Media.Bytes media) {
    checkRequest();

    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Bytes provided a null content-type");
    }

    final byte[] bytes;
    bytes = media.toByteArray();

    if (bytes == null) {
      throw new IllegalArgumentException("The specified Media.Bytes provided a null byte array");
    }

    statusUnchecked(status);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_TYPE, contentType);

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    sendUnchecked(bytes);
  }

  private void respond(Http.Status status, Media.Text media) {
    checkRequest();

    // early media validation
    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null content-type");
    }

    final Charset charset;
    charset = media.charset();

    if (charset == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null charset");
    }

    statusUnchecked(status);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_TYPE, contentType);

    headerUnchecked(Http.HeaderName.TRANSFER_ENCODING, "chunked");

    sendUnchecked(media);
  }

  final void header(Http.HeaderName name, long value) {
    checkResponseHeaders();
    Objects.requireNonNull(name, "name == null");

    headerUnchecked(name, value);
  }

  final void header(Http.HeaderName name, String value) {
    checkResponseHeaders();
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    headerUnchecked(name, value);
  }

  final void status(Http.Status status) {
    checkRequest();
    Objects.requireNonNull(status, "status == null");

    statusUnchecked(status);
  }

  private static final byte[][] STATUS_LINES;

  static {
    final int size;
    size = HttpStatus.size();

    final byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      final HttpStatus status;
      status = HttpStatus.get(index);

      final String response;
      response = Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      map[index] = Http.utf8(response);
    }

    STATUS_LINES = map;
  }

  private void statusUnchecked(Http.Status value) {
    bufferIndex = 0;

    writeBytes(version.responseBytes);

    HttpStatus internal;
    internal = (HttpStatus) value;

    byte[] statusBytes;
    statusBytes = STATUS_LINES[internal.index];

    writeBytes(statusBytes);

    state = $RESPONSE_HEADERS;

    responseListener.status(version, value);
  }

  private void headerUnchecked(Http.HeaderName name, long value) {
    headerUnchecked(name, Long.toString(value));
  }

  private void headerUnchecked(Http.HeaderName name, String value) {
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
      bitClear(BIT_KEEP_ALIVE);
    }

    else if (name == Http.HeaderName.CONTENT_LENGTH) {
      bitSet(BIT_CONTENT_LENGTH);
    }

    else if (name == Http.HeaderName.TRANSFER_ENCODING) {
      if (value.equalsIgnoreCase("chunked")) {
        bitSet(BIT_CHUNKED);
      } else {
        bitClear(BIT_CHUNKED);
      }
    }

    responseListener.header(name, value);
  }

  final void send() {
    terminate();

    body(null);

    state = $COMMIT;
  }

  final void send(byte[] bytes) {
    Objects.requireNonNull(bytes, "bytes == null");

    sendUnchecked(bytes);
  }

  final void send(Path file) {
    Objects.requireNonNull(file, "file == null");

    sendUnchecked(file);
  }

  private void sendUnchecked(byte[] bytes) {
    terminate();

    if (method == Http.Method.HEAD) {

      body(null);

    } else {

      if (canBuffer(bytes.length)) {
        body(null);

        writeBytes(bytes);
      } else {
        body(bytes);
      }

    }

    state = $COMMIT;
  }

  private void sendUnchecked(Media.Text text) {
    terminate();

    if (method == Http.Method.HEAD) {

      body(null);

    } else {

      body(text);

    }

    state = $COMMIT;
  }

  private void sendUnchecked(Path file) {
    terminate();

    if (method == Http.Method.HEAD) {

      body(null);

    } else {

      body(file);

    }

    state = $COMMIT;
  }

  private void body(Object value) {
    if (object instanceof AutoCloseable c) {
      try {
        c.close();
      } catch (Throwable e) {
        // TODO log exception
      }
    }

    object = value;

    responseListener.body(value);
  }

  private void terminate() {
    writeBytes(Bytes.CRLF);
  }

  // ##################################################################
  // # END: Internal Response API
  // ##################################################################

  @Override
  public final boolean processed() {
    return switch (state) {
      case $COMMIT -> true;

      case $REQUEST -> false;

      default -> throw new IllegalStateException(
          "Unexpected state=" + state
      );
    };
  }

  // ##################################################################
  // # BEGIN: Utils
  // ##################################################################

  private void appendInit() {
    stringBuilderInit();

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

  private void bitClear(int mask) {
    bitset &= ~mask;
  }

  private void bitSet(int mask) {
    bitset |= mask;
  }

  private boolean bitTest(int mask) {
    return (bitset & mask) != 0;
  }

  private boolean canBuffer(long contentLength) {
    int maxAvailable;
    maxAvailable = maxBufferSize - bufferIndex;

    return maxAvailable >= contentLength;
  }

  private String markToString() {
    return new String(buffer, mark, bufferIndex - mark, StandardCharsets.US_ASCII);
  }

  private void note(Note.Long1Ref1<Http.Exchange> note) {
    noteSink.send(note, id, this);
  }

  final String now() {
    Clock theClock;
    theClock = clock;

    if (theClock == null) {
      theClock = Clock.systemUTC();
    }

    final ZonedDateTime now;
    now = ZonedDateTime.now(theClock);

    return Http.formatDate(now);
  }

  private void stringBuilderInit() {
    if (stringBuilder == null) {
      stringBuilder = new StringBuilder();
    } else {
      stringBuilder.setLength(0);
    }
  }

  private void writeBytes(byte[] bytes) {
    try {
      if (!bitTest(BIT_WRITE_ERROR)) {
        writeBytes0(bytes);
      }
    } catch (IOException e) {
      bitSet(BIT_WRITE_ERROR);

      noteSink.send(NOTES.writeIOException, id, e);
    }
  }

  private void writeBytes0(byte[] bytes) throws IOException {
    int bytesIndex;
    bytesIndex = 0;

    int remaining;
    remaining = bytes.length;

    // max index to write all data
    final int requiredIndex;
    requiredIndex = bufferIndex + remaining;

    if (requiredIndex >= buffer.length && buffer.length < maxBufferSize) {

      // buffer resize

      final int length;
      length = powerOfTwo(requiredIndex + 1);

      final int newLength;
      newLength = Math.min(length, maxBufferSize);

      buffer = Arrays.copyOf(buffer, newLength);

      noteSink.send(NOTES.writeResize, id, newLength);

    }

    while (remaining > 0) {
      int available;
      available = buffer.length - bufferIndex;

      if (available <= 0) {
        outputStream.write(buffer, 0, bufferIndex);

        bufferIndex = 0;

        available = buffer.length - bufferIndex;
      }

      final int bytesToCopy;
      bytesToCopy = Math.min(remaining, available);

      System.arraycopy(bytes, bytesIndex, buffer, bufferIndex, bytesToCopy);

      bufferIndex += bytesToCopy;

      bytesIndex += bytesToCopy;

      remaining -= bytesToCopy;
    }
  }

  // ##################################################################
  // # END: Utils
  // ##################################################################

}