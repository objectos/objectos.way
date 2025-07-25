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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import objectos.way.Lang.Key;

final class HttpExchange implements Http.Exchange, Closeable {

  // ##################################################################
  // # BEGIN: Private Types
  // ##################################################################

  private record Notes(
      Note.Long1Ref1<InetAddress> start,

      Note.Long2 readResize,
      Note.Long1Ref1<Http.Exchange> readEof,
      Note.Long1Ref1<Http.Exchange> readMaxBuffer,
      Note.Long1Ref2<Http.Exchange, IOException> readIOException,

      Note.Long1Ref2<Path, Http.Exchange> bodyFile,

      Note.Long1Ref1<IOException> writeIOException,

      Note.Long1Ref2<ClientError, Http.Exchange> badRequest,
      Note.Long1Ref1<Http.Exchange> lengthRequired,
      Note.Long1Ref1<Http.Exchange> contentTooLarge,
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

          Note.Long1Ref1.create(s, "WEX", Note.ERROR),

          Note.Long1Ref2.create(s, "400", Note.INFO),
          Note.Long1Ref1.create(s, "411", Note.INFO),
          Note.Long1Ref1.create(s, "413", Note.INFO),
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
    public final Http.Status status() {
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
    TERMINATOR,

    // invalid value, e.g., 'Content-Length: two hundred bytes'
    INVALID_CONTENT_LENGTH,

    // request include both Content-Length and Transfer-Enconding.
    BOTH_CL_TE;

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

  private enum InvalidApplicationForm implements ClientError {
    // do not reorder, do not rename

    CHAR,

    PERCENT;

    private static final byte[] MESSAGE = "Invalid application/x-www-form-urlencoded content in request body.\n".getBytes(StandardCharsets.US_ASCII);

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final Http.Status status() {
      return Http.Status.BAD_REQUEST;
    }
  }

  // ##################################################################
  // # END: Private Types
  // ##################################################################

  // ##################################################################
  // # BEGIN: States
  // ##################################################################

  static final byte $START = 0;

  static final byte $READ = 1;
  static final byte $READ_MAX_BUFFER = 2;
  static final byte $READ_EOF = 3;

  static final byte $PARSE_METHOD = 4;
  static final byte $PARSE_PATH = 5;
  static final byte $PARSE_PATH_CONTENTS0 = 6;
  static final byte $PARSE_PATH_CONTENTS1 = 7;
  static final byte $PARSE_QUERY = 8;
  static final byte $PARSE_QUERY0 = 9;
  static final byte $PARSE_QUERY1 = 10;
  static final byte $PARSE_QUERY_VALUE = 11;
  static final byte $PARSE_QUERY_VALUE0 = 12;
  static final byte $PARSE_QUERY_VALUE1 = 13;
  static final byte $PARSE_VERSION_0_9 = 14;
  static final byte $PARSE_VERSION_1_1 = 15;
  static final byte $PARSE_VERSION_OTHERS = 16;

  static final byte $PARSE_HEADER = 17;
  static final byte $PARSE_HEADER_NAME = 18;
  static final byte $PARSE_HEADER_VALUE = 19;
  static final byte $PARSE_HEADER_VALUE_CONTENTS = 20;
  static final byte $PARSE_HEADER_VALUE_CR = 21;
  static final byte $PARSE_HEADER_CR = 22;

  static final byte $PARSE_BODY = 23;
  static final byte $PARSE_BODY_FIXED = 24;
  static final byte $PARSE_BODY_FIXED_ZERO = 25;
  static final byte $PARSE_BODY_FIXED_BUFFER = 26;
  static final byte $PARSE_BODY_FIXED_BUFFER_READ = 27;
  static final byte $PARSE_BODY_FIXED_BUFFER_SUCCESS = 28;
  static final byte $PARSE_BODY_FIXED_FILE = 29;
  static final byte $PARSE_BODY_FIXED_FILE_BUFFER = 30;
  static final byte $PARSE_BODY_FIXED_FILE_READ = 31;
  static final byte $PARSE_BODY_FIXED_FILE_CLOSE = 32;

  static final byte $PARSE_APP_FORM = 33;
  static final byte $PARSE_APP_FORM0 = 34;
  static final byte $PARSE_APP_FORM1 = 35;
  static final byte $PARSE_APP_FORM_VALUE = 36;
  static final byte $PARSE_APP_FORM_VALUE0 = 37;
  static final byte $PARSE_APP_FORM_VALUE1 = 38;

  static final byte $DECODE_PERC = 39;
  static final byte $DECODE_PERC1_LOW = 40;
  static final byte $DECODE_PERC2_1_LOW = 41;
  static final byte $DECODE_PERC2_2 = 42;
  static final byte $DECODE_PERC2_2_HIGH = 43;
  static final byte $DECODE_PERC2_2_LOW = 44;
  static final byte $DECODE_PERC3_1_LOW = 45;
  static final byte $DECODE_PERC3_2 = 46;
  static final byte $DECODE_PERC3_2_HIGH = 47;
  static final byte $DECODE_PERC3_2_LOW = 48;
  static final byte $DECODE_PERC3_3 = 49;
  static final byte $DECODE_PERC3_3_HIGH = 50;
  static final byte $DECODE_PERC3_3_LOW = 51;
  static final byte $DECODE_PERC4_1_LOW = 52;
  static final byte $DECODE_PERC4_2 = 53;
  static final byte $DECODE_PERC4_2_HIGH = 54;
  static final byte $DECODE_PERC4_2_LOW = 55;
  static final byte $DECODE_PERC4_3 = 56;
  static final byte $DECODE_PERC4_3_HIGH = 57;
  static final byte $DECODE_PERC4_3_LOW = 58;
  static final byte $DECODE_PERC4_4 = 59;
  static final byte $DECODE_PERC4_4_HIGH = 60;
  static final byte $DECODE_PERC4_4_LOW = 61;

  static final byte $BAD_REQUEST = 62;
  static final byte $LENGTH_REQUIRED = 63;
  static final byte $CONTENT_TOO_LARGE = 64;
  static final byte $URI_TOO_LONG = 65;
  static final byte $REQUEST_HEADER_FIELDS_TOO_LARGE = 66;
  static final byte $NOT_IMPLEMENTED = 67;
  static final byte $HTTP_VERSION_NOT_SUPPORTED = 68;

  static final byte $COMMIT = 69;
  static final byte $WRITE = 70;

  static final byte $REQUEST = 71;

  static final byte $RESPONSE_HEADERS = 72;

  static final byte $ERROR = 73;

  // ##################################################################
  // # END: States
  // ##################################################################

  // ##################################################################
  // # BEGIN: Constants
  // ##################################################################

  private static final int HARD_MAX_BUFFER_SIZE = 1 << 14;

  private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

  private static final Notes NOTES = Notes.get();

  private static final int BIT_KEEP_ALIVE = 1 << 0;
  private static final int BIT_CONTENT_LENGTH = 1 << 1;
  private static final int BIT_CHUNKED = 1 << 2;
  private static final int BIT_QUERY_STRING = 1 << 3;
  private static final int BIT_WRITE_ERROR = 1 << 4;

  // ##################################################################
  // # END: Constants
  // ##################################################################

  private Map<String, Object> attributes;

  private int bitset;

  @SuppressWarnings("unused")
  private final HttpExchangeBodyFiles bodyFiles;

  private BodyKind bodyKind = BodyKind.EMPTY;

  private byte[] buffer;

  private int bufferIndex;

  private int bufferLimit;

  private final Clock clock;

  private byte[] decodePerc;

  private Object formParams;

  private HttpHeaderName headerName;

  private Map<HttpHeaderName, HttpHeader> headers;

  private final long id = ID_GENERATOR.getAndIncrement();

  private final InputStream inputStream;

  private long long0;

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

  private final long requestBodySizeMax;

  private final Http.ResponseListener responseListener;

  private HttpSession session;

  private final Closeable socket;

  private byte state;

  private byte stateNext;

  private StringBuilder stringBuilder;

  private Http.Version version = Http.Version.HTTP_1_1;

  HttpExchange(
      Socket socket,
      int bufferSizeInitial,
      int bufferSizeMax,
      Clock clock,
      Note.Sink noteSink,
      long requestBodySizeMax
  ) throws IOException {
    this(
        socket,
        HttpExchangeBodyFiles.standard(),
        bufferSizeInitial,
        bufferSizeMax,
        clock,
        noteSink,
        requestBodySizeMax
    );
  }

  HttpExchange(
      Socket socket,
      HttpExchangeBodyFiles bodyFiles,
      int bufferSizeInitial,
      int bufferSizeMax,
      Clock clock,
      Note.Sink noteSink,
      long requestBodySizeMax
  ) throws IOException {
    this(
        socket,
        bodyFiles,
        bufferSizeInitial,
        bufferSizeMax,
        clock,
        noteSink,
        requestBodySizeMax,
        Http.NoopResponseListener.INSTANCE
    );
  }

  HttpExchange(
      Socket socket,
      HttpExchangeBodyFiles bodyFiles,
      int bufferSizeInitial,
      int bufferSizeMax,
      Clock clock,
      Note.Sink noteSink,
      long requestBodySizeMax,
      Http.ResponseListener responseListener
  ) throws IOException {
    this.bodyFiles = bodyFiles;

    final int initialSize;
    initialSize = powerOfTwo(bufferSizeInitial);

    this.buffer = new byte[initialSize];

    this.clock = clock;

    this.inputStream = socket.getInputStream();

    this.maxBufferSize = powerOfTwo(bufferSizeMax);

    this.noteSink = noteSink;

    this.outputStream = socket.getOutputStream();

    remoteAddress = socket.getInetAddress();

    this.requestBodySizeMax = requestBodySizeMax;

    this.responseListener = responseListener;

    this.socket = socket;
  }

  private HttpExchange(HttpExchangeBuilder builder) {
    bodyFiles = builder.bodyFiles();

    final int initialSize;
    initialSize = powerOfTwo(builder.bufferSizeInitial);

    buffer = new byte[initialSize];

    clock = builder.clock;

    inputStream = builder.inputStream();

    maxBufferSize = builder.bufferSizeMax;

    noteSink = builder.noteSink;

    outputStream = new ByteArrayOutputStream();

    requestBodySizeMax = builder.requestBodySizeMax;

    responseListener = builder.responseListener;

    socket = () -> {}; // noop closeable
  }

  static HttpExchange create0(Consumer<? super Http.Exchange.Options> options) {
    final HttpExchangeBuilder builder;
    builder = new HttpExchangeBuilder();

    options.accept(builder);

    final HttpExchange impl;
    impl = new HttpExchange(builder);

    if (!impl.shouldHandle()) {
      throw new IllegalArgumentException("Invalid request");
    }

    impl.attributes = builder.attributes;

    impl.session = builder.session();

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
      state = internalServerError(t);
    }
  }

  @Override
  public final String toString() {
    return switch (state) {
      case $REQUEST -> "HttpExchange[id=" + id + ",method=" + method + ",path=" + path + "]";

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
      return "HttpExchange[state=" + state + "]";
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

      case $PARSE_QUERY -> executeParseQuery();
      case $PARSE_QUERY0 -> executeParseQuery0();
      case $PARSE_QUERY1 -> executeParseQuery1();
      case $PARSE_QUERY_VALUE -> executeParseQueryValue();
      case $PARSE_QUERY_VALUE0 -> executeParseQueryValue0();
      case $PARSE_QUERY_VALUE1 -> executeParseQueryValue1();

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
      case $PARSE_BODY_FIXED -> executeParseBodyFixed();
      case $PARSE_BODY_FIXED_ZERO -> executeParseBodyFixedZero();
      case $PARSE_BODY_FIXED_BUFFER -> executeParseBodyFixedBuffer();
      case $PARSE_BODY_FIXED_BUFFER_READ -> executeParseBodyFixedBufferRead();
      case $PARSE_BODY_FIXED_BUFFER_SUCCESS -> executeParseBodyFixedBufferSuccess();
      case $PARSE_BODY_FIXED_FILE -> executeParseBodyFixedFile();
      case $PARSE_BODY_FIXED_FILE_BUFFER -> executeParseBodyFixedFileBuffer();
      case $PARSE_BODY_FIXED_FILE_READ -> executeParseBodyFixedFileRead();
      case $PARSE_BODY_FIXED_FILE_CLOSE -> executeParseBodyFixedFileClose();

      case $PARSE_APP_FORM -> executeParseAppForm();
      case $PARSE_APP_FORM0 -> executeParseAppForm0();
      case $PARSE_APP_FORM1 -> executeParseAppForm1();
      case $PARSE_APP_FORM_VALUE -> executeParseAppFormValue();
      case $PARSE_APP_FORM_VALUE0 -> executeParseAppFormValue0();
      case $PARSE_APP_FORM_VALUE1 -> executeParseAppFormValue1();

      case $DECODE_PERC -> executeDecodePerc();
      case $DECODE_PERC1_LOW -> executeDecodePerc1Low();
      case $DECODE_PERC2_1_LOW -> executeDecodePerc(PERC1_LOW, $DECODE_PERC2_2);
      case $DECODE_PERC2_2 -> executeDecodePerc($DECODE_PERC2_2_HIGH);
      case $DECODE_PERC2_2_HIGH -> executeDecodePerc(PERC2_HIGH, $DECODE_PERC2_2_LOW);
      case $DECODE_PERC2_2_LOW -> executeDecodePerc2_2_Low();
      case $DECODE_PERC3_1_LOW -> executeDecodePerc(PERC1_LOW, $DECODE_PERC3_2);
      case $DECODE_PERC3_2 -> executeDecodePerc($DECODE_PERC3_2_HIGH);
      case $DECODE_PERC3_2_HIGH -> executeDecodePerc(PERC2_HIGH, $DECODE_PERC3_2_LOW);
      case $DECODE_PERC3_2_LOW -> executeDecodePerc(PERC2_LOW, $DECODE_PERC3_3);
      case $DECODE_PERC3_3 -> executeDecodePerc($DECODE_PERC3_3_HIGH);
      case $DECODE_PERC3_3_HIGH -> executeDecodePerc(PERC3_HIGH, $DECODE_PERC3_3_LOW);
      case $DECODE_PERC3_3_LOW -> executeDecodePerc3_3_Low();
      case $DECODE_PERC4_1_LOW -> executeDecodePerc(PERC1_LOW, $DECODE_PERC4_2);
      case $DECODE_PERC4_2 -> executeDecodePerc($DECODE_PERC4_2_HIGH);
      case $DECODE_PERC4_2_HIGH -> executeDecodePerc(PERC2_HIGH, $DECODE_PERC4_2_LOW);
      case $DECODE_PERC4_2_LOW -> executeDecodePerc(PERC2_LOW, $DECODE_PERC4_3);
      case $DECODE_PERC4_3 -> executeDecodePerc($DECODE_PERC4_3_HIGH);
      case $DECODE_PERC4_3_HIGH -> executeDecodePerc(PERC3_HIGH, $DECODE_PERC4_3_LOW);
      case $DECODE_PERC4_3_LOW -> executeDecodePerc(PERC3_LOW, $DECODE_PERC4_4);
      case $DECODE_PERC4_4 -> executeDecodePerc($DECODE_PERC4_4_HIGH);
      case $DECODE_PERC4_4_HIGH -> executeDecodePerc(PERC4_HIGH, $DECODE_PERC4_4_LOW);
      case $DECODE_PERC4_4_LOW -> executeDecodePerc4_4_Low();

      case $BAD_REQUEST -> executeBadRequest();
      case $LENGTH_REQUIRED -> executeLengthRequired();
      case $CONTENT_TOO_LARGE -> executeContentTooLarge();
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

    if (decodePerc != null) {
      Arrays.fill(decodePerc, (byte) 0);
    }

    if (formParams != null) {
      formParams = null;
    }

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

    session = null;

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
           $PARSE_QUERY,
           $PARSE_QUERY0,
           $PARSE_QUERY1,
           $PARSE_QUERY_VALUE,
           $PARSE_QUERY_VALUE0,
           $PARSE_QUERY_VALUE1 -> executeUriTooLong();

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
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR);
      }

      final byte code;
      code = PARSE_PATH_TABLE[b];

      return switch (code) {
        case PATH_VALID -> { bufferIndex += 1; yield $PARSE_PATH_CONTENTS0; }

        case PATH_PERCENT -> { appendInit(); bufferIndex += 1; yield toDecodePerc($PARSE_PATH_CONTENTS1); }

        case PATH_SPACE -> { path = markToString(); bufferIndex += 1; yield pv($PARSE_VERSION_1_1); }

        case PATH_QUESTION -> { path = markToString(); bufferIndex += 1; yield pv($PARSE_QUERY); }

        case PATH_CRLF -> { path = markToString(); bufferIndex += 1; yield pv($PARSE_VERSION_0_9); }

        default -> toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR);
      };
    } else {
      return toRead($PARSE_PATH_CONTENTS0);
    }
  }

  private byte executeParsePathContents1() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR);
      }

      final byte code;
      code = PARSE_PATH_TABLE[b];

      return switch (code) {
        case PATH_VALID -> { appendChar(b); bufferIndex += 1; yield $PARSE_PATH_CONTENTS1; }

        case PATH_PERCENT -> { bufferIndex += 1; yield toDecodePerc($PARSE_PATH_CONTENTS1); }

        case PATH_SPACE -> { path = appendToString(); bufferIndex += 1; yield pv($PARSE_VERSION_1_1); }

        case PATH_QUESTION -> { path = appendToString(); bufferIndex += 1; yield pv($PARSE_QUERY); }

        case PATH_CRLF -> { path = appendToString(); bufferIndex += 1; yield pv($PARSE_VERSION_0_9); }

        default -> toBadRequest(InvalidRequestLine.PATH_NEXT_CHAR);
      };
    } else {
      return toRead($PARSE_PATH_CONTENTS1);
    }
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
  private static final byte QUERY_BAD = 8;

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

  private byte parseQueryTable(byte b) {
    if (b < 0) {
      return QUERY_BAD;
    } else {
      return PARSE_QUERY_TABLE[b];
    }
  }

  private byte executeParseQuery() {
    // where the current key begins
    mark = bufferIndex;

    bitSet(BIT_QUERY_STRING);

    state = $PARSE_QUERY0;

    return executeParseQuery0();
  }

  private byte executeParseQuery0() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte code;
      code = parseQueryTable(b);

      return switch (code) {
        case QUERY_VALID -> { bufferIndex += 1; yield $PARSE_QUERY0; }

        case QUERY_PERCENT -> { appendInit(); bufferIndex += 1; yield toDecodePerc($PARSE_QUERY1); }

        case QUERY_PLUS -> { appendInit(); appendChar(' '); bufferIndex += 1; yield $PARSE_QUERY1; }

        case QUERY_EQUALS -> { object = markToString(); bufferIndex += 1; yield $PARSE_QUERY_VALUE; }

        case QUERY_AMPERSAND -> { object = markToString(); bufferIndex += 1; makeQueryParam(""); yield $PARSE_QUERY; }

        case QUERY_SPACE -> { makeIfNecessary(); bufferIndex += 1; yield $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { makeIfNecessary(); bufferIndex += 1; yield $PARSE_VERSION_0_9; }

        default -> toBadRequest(InvalidRequestLine.QUERY_CHAR);
      };
    } else {
      return toRead($PARSE_QUERY0);
    }
  }

  private byte executeParseQuery1() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte code;
      code = parseQueryTable(b);

      return switch (code) {
        case QUERY_VALID -> { appendChar(b); bufferIndex += 1; yield $PARSE_QUERY1; }

        case QUERY_PERCENT -> { bufferIndex += 1; yield toDecodePerc($PARSE_QUERY1); }

        case QUERY_PLUS -> { appendChar(' '); bufferIndex += 1; yield $PARSE_QUERY1; }

        case QUERY_EQUALS -> { object = appendToString(); bufferIndex += 1; yield $PARSE_QUERY_VALUE; }

        case QUERY_AMPERSAND -> { object = appendToString(); bufferIndex += 1; makeQueryParam(""); yield $PARSE_QUERY; }

        case QUERY_SPACE -> { object = appendToString(); bufferIndex += 1; makeQueryParam(""); yield $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { object = appendToString(); bufferIndex += 1; makeQueryParam(""); yield $PARSE_VERSION_0_9; }

        default -> toBadRequest(InvalidRequestLine.QUERY_CHAR);
      };
    } else {
      return toRead($PARSE_QUERY1);
    }
  }

  private byte executeParseQueryValue() {
    // where the current value begins
    mark = bufferIndex;

    state = $PARSE_QUERY_VALUE0;

    return executeParseQueryValue0();
  }

  private byte executeParseQueryValue0() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte code;
      code = parseQueryTable(b);

      return switch (code) {
        case QUERY_VALID, QUERY_EQUALS -> { bufferIndex += 1; yield $PARSE_QUERY_VALUE0; }

        case QUERY_PERCENT -> { appendInit(); bufferIndex += 1; yield toDecodePerc($PARSE_QUERY_VALUE1); }

        case QUERY_PLUS -> { appendInit(); appendChar(' '); bufferIndex += 1; yield $PARSE_QUERY_VALUE1; }

        case QUERY_AMPERSAND -> { final String v = markToString(); bufferIndex += 1; makeQueryParam(v); yield $PARSE_QUERY; }

        case QUERY_SPACE -> { final String v = markToString(); bufferIndex += 1; makeQueryParam(v); yield $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { final String v = markToString(); bufferIndex += 1; makeQueryParam(v); yield $PARSE_VERSION_0_9; }

        default -> toBadRequest(InvalidRequestLine.QUERY_CHAR);
      };
    } else {
      return toRead($PARSE_QUERY_VALUE0);
    }
  }

  private byte executeParseQueryValue1() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte code;
      code = parseQueryTable(b);

      return switch (code) {
        case QUERY_VALID, QUERY_EQUALS -> { appendChar(b); bufferIndex += 1; yield $PARSE_QUERY_VALUE1; }

        case QUERY_PERCENT -> { bufferIndex += 1; yield toDecodePerc($PARSE_QUERY_VALUE1); }

        case QUERY_PLUS -> { appendChar(' '); bufferIndex += 1; yield $PARSE_QUERY_VALUE1; }

        case QUERY_AMPERSAND -> { final String v = appendToString(); bufferIndex += 1; makeQueryParam(v); yield $PARSE_QUERY; }

        case QUERY_SPACE -> { final String v = appendToString(); bufferIndex += 1; makeQueryParam(v); yield $PARSE_VERSION_1_1; }

        case QUERY_CRLF -> { final String v = appendToString(); bufferIndex += 1; makeQueryParam(v); yield $PARSE_VERSION_0_9; }

        default -> toBadRequest(InvalidRequestLine.QUERY_CHAR);
      };
    } else {
      return toRead($PARSE_QUERY_VALUE1);
    }
  }

  private Map<String, Object> copyQueryParams() {
    if (queryParams == null) {
      return null;
    } else {
      final Map<String, Object> copy;
      copy = Util.createMap();

      copy.putAll(queryParams);

      queryParams.clear();

      return copy;
    }
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
  // # BEGIN: URL Decode
  // ############################################################

  private void makeIfNecessary() {
    if (mark != bufferIndex || (queryParams != null && !queryParams.isEmpty())) {
      // make when
      // 1) key is not empty
      // 2) not the first key/value
      object = markToString();

      makeQueryParam("");
    }
  }

  private static final int PERC1_HIGH = 0;
  private static final int PERC1_LOW = 1;
  private static final int PERC2_HIGH = 2;
  private static final int PERC2_LOW = 3;
  private static final int PERC3_HIGH = 4;
  private static final int PERC3_LOW = 5;
  private static final int PERC4_HIGH = 6;
  private static final int PERC4_LOW = 7;

  private byte executeDecodePerc() {
    if (decodePerc == null) {
      decodePerc = new byte[PERC4_LOW];
    }

    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex++];

      final byte high;
      high = Bytes.fromHexDigit(b);

      if (high < 0) {
        return toDecodePercBadRequest();
      }

      decodePerc[PERC1_HIGH] = high;

      return switch (high) {
        // 0yyyzzzz
        case 0b0000, 0b0001,
             0b0010, 0b0011,
             0b0100, 0b0101, 0b0110, 0b0111 -> $DECODE_PERC1_LOW;

        // 110xxxyy 10yyzzzz
        case 0b1100, 0b1101 -> $DECODE_PERC2_1_LOW;

        // 1110wwww 10xxxxyy 10yyzzzz
        case 0b1110 -> $DECODE_PERC3_1_LOW;

        // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
        case 0b1111 -> $DECODE_PERC4_1_LOW;

        default -> toDecodePercBadRequest();
      };
    } else {
      return toDecodePercExhausted();
    }
  }

  private byte executeDecodePerc1Low() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex++];

      final byte low;
      low = Bytes.fromHexDigit(b);

      if (low < 0) {
        return toDecodePercBadRequest();
      }

      final byte high;
      high = decodePerc[PERC1_HIGH];

      final int perc1;
      perc1 = decodePerc(high, low);

      appendChar(perc1);

      return (byte) markEnd;
    } else {
      return toDecodePercExhausted();
    }
  }

  private byte executeDecodePerc2_2_Low() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex++];

      final byte low;
      low = Bytes.fromHexDigit(b);

      if (low < 0) {
        return toDecodePercBadRequest();
      }

      final int perc1;
      perc1 = decodePerc(decodePerc[PERC1_HIGH], decodePerc[PERC1_LOW]);

      final int perc2;
      perc2 = decodePerc(decodePerc[PERC2_HIGH], low);

      if (!utf8Byte(perc2)) {
        return toDecodePercBadRequest();
      }

      final int c;
      c = (perc1 & 0b1_1111) << 6 | (perc2 & 0b11_1111);

      if (c < 0x80 || c > 0x7FF) {
        return toDecodePercBadRequest();
      }

      appendChar(c);

      return (byte) markEnd;
    } else {
      return toDecodePercExhausted();
    }
  }

  private byte executeDecodePerc3_3_Low() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex++];

      final byte low;
      low = Bytes.fromHexDigit(b);

      if (low < 0) {
        return toDecodePercBadRequest();
      }

      final int perc1;
      perc1 = decodePerc(decodePerc[PERC1_HIGH], decodePerc[PERC1_LOW]);

      final int perc2;
      perc2 = decodePerc(decodePerc[PERC2_HIGH], decodePerc[PERC2_LOW]);

      if (!utf8Byte(perc2)) {
        return toDecodePercBadRequest();
      }

      final int perc3;
      perc3 = decodePerc(decodePerc[PERC3_HIGH], low);

      if (!utf8Byte(perc3)) {
        return toDecodePercBadRequest();
      }

      final int c;
      c = (perc1 & 0b1111) << 12 | (perc2 & 0b11_1111) << 6 | (perc3 & 0b11_1111);

      if (c < 0x800 || c > 0xFFFF || Character.isSurrogate((char) c)) {
        return toDecodePercBadRequest();
      }

      appendChar(c);

      return (byte) markEnd;
    } else {
      return toDecodePercExhausted();
    }
  }

  private byte executeDecodePerc4_4_Low() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex++];

      final byte low;
      low = Bytes.fromHexDigit(b);

      if (low < 0) {
        return toDecodePercBadRequest();
      }

      final int perc1;
      perc1 = decodePerc(decodePerc[PERC1_HIGH], decodePerc[PERC1_LOW]);

      final int perc2;
      perc2 = decodePerc(decodePerc[PERC2_HIGH], decodePerc[PERC2_LOW]);

      if (!utf8Byte(perc2)) {
        return toDecodePercBadRequest();
      }

      final int perc3;
      perc3 = decodePerc(decodePerc[PERC3_HIGH], decodePerc[PERC3_LOW]);

      if (!utf8Byte(perc3)) {
        return toDecodePercBadRequest();
      }

      final int perc4;
      perc4 = decodePerc(decodePerc[PERC4_HIGH], low);

      if (!utf8Byte(perc4)) {
        return toDecodePercBadRequest();
      }

      final int c;
      c = (perc1 & 0b111) << 18 | (perc2 & 0b11_1111) << 12 | (perc3 & 0b11_1111) << 6 | (perc4 & 0b11_1111);

      if (c < 0x1_0000 || !Character.isValidCodePoint(c)) {
        return toDecodePercBadRequest();
      }

      appendCodePoint(c);

      return (byte) markEnd;
    } else {
      return toDecodePercExhausted();
    }
  }

  private int decodePerc(byte high, byte low) {
    return (high << 4) | low;
  }

  private byte executeDecodePerc(byte next) {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex++];

      if (b != '%') {
        return toDecodePercBadRequest();
      }

      return next;
    } else {
      return toDecodePercExhausted();
    }
  }

  private byte executeDecodePerc(int index, byte next) {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex++];

      final byte value;
      value = Bytes.fromHexDigit(b);

      if (value < 0) {
        return toDecodePercBadRequest();
      }

      decodePerc[index] = value;

      return next;
    } else {
      return toDecodePercExhausted();
    }
  }

  private byte toDecodePerc(byte next) {
    markEnd = next;

    return $DECODE_PERC;
  }

  private byte toDecodePercBadRequest() {
    final ClientError error;
    error = switch (markEnd) {
      case $PARSE_PATH_CONTENTS1 -> InvalidRequestLine.PATH_PERCENT;

      case $PARSE_QUERY1, $PARSE_QUERY_VALUE1 -> InvalidRequestLine.QUERY_PERCENT;

      case $PARSE_APP_FORM1, $PARSE_APP_FORM_VALUE1 -> InvalidApplicationForm.PERCENT;

      default -> throw new AssertionError("Unexpected markEnd=" + markEnd);
    };

    return toBadRequest(error);
  }

  private byte toDecodePercExhausted() {
    return switch (markEnd) {
      case $PARSE_PATH_CONTENTS1 -> toRead(state);

      case $PARSE_QUERY1, $PARSE_QUERY_VALUE1 -> toRead(state);

      case $PARSE_APP_FORM1, $PARSE_APP_FORM_VALUE1 -> switch (formParams) {
        case AppFormBufferSupport support -> toBadRequest(InvalidApplicationForm.PERCENT);

        case BodyFileSupport support -> switch (support.state) {
          case $PARSE_BODY_FIXED_FILE_CLOSE -> toBadRequest(InvalidApplicationForm.PERCENT);

          default -> toParseAppFormExhausted(support);
        };

        default -> throw new AssertionError("Unexpected formParams=" + formParams);
      };

      default -> throw new AssertionError("Unexpected markEnd=" + markEnd);
    };
  }

  private boolean utf8Byte(int utf8) {
    final int topTwoBits;
    topTwoBits = utf8 & 0b1100_0000;

    return topTwoBits == 0b1000_0000;
  }

  // ##################################################################
  // # END: URL Decode
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

  private static final byte[] HEADER_VALUE_TABLE;

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

    HEADER_VALUE_TABLE = table;
  }

  private byte executeParseHeaderValueContents() {
    while (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      if (b < 0) {
        return toBadRequest(InvalidRequestHeaders.VALUE_CHAR);
      }

      final byte test;
      test = HEADER_VALUE_TABLE[b];

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
      object = contentLength;

      return $PARSE_BODY_FIXED;
    }

    final HttpHeader transferEncoding;
    transferEncoding = headerUnchecked(HttpHeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      // TODO 501 Not Implemented
      throw new UnsupportedOperationException("Implement me");
    }

    final HttpHeader contentType;
    contentType = headerUnchecked(HttpHeaderName.CONTENT_TYPE);

    if (contentType != null) {
      return $LENGTH_REQUIRED;
    }

    return $REQUEST;
  }

  private byte executeParseBodyFixed() {
    final HttpHeader contentLength;
    contentLength = (HttpHeader) object;

    object = null;

    final HttpHeader transferEncoding;
    transferEncoding = headerUnchecked(HttpHeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      return toBadRequest(InvalidRequestHeaders.BOTH_CL_TE);
    }

    final long length;
    length = contentLength.unsignedLongValue(buffer);

    if (length == HttpHeader.LONG_INVALID) {
      return toBadRequest(InvalidRequestHeaders.INVALID_CONTENT_LENGTH);
    }

    else if (length == HttpHeader.LONG_OVERFLOW) {
      // valid length... but too large -> we do not handle
      return $CONTENT_TOO_LARGE;
    }

    else if (length > requestBodySizeMax) {
      return $CONTENT_TOO_LARGE;
    }

    else if (length == 0) {
      // empty body
      return $PARSE_BODY_FIXED_ZERO;
    }

    else if (canBuffer(length)) {
      // fits in buffer

      // length is guaranteed to fit in an int
      // in any case we throw if length overflows...
      mark = Math.toIntExact(length);

      return $PARSE_BODY_FIXED_BUFFER;
    }

    else {
      // does not fit buffer

      long0 = length;

      return $PARSE_BODY_FIXED_FILE;
    }
  }

  private byte executeParseBodyFixedZero() {
    bodyKind = BodyKind.EMPTY;

    return $REQUEST;
  }

  private byte executeParseBodyFixedBuffer() {
    // restore content length
    final int contentLength;
    contentLength = mark;

    final int requiredBufferLength;
    requiredBufferLength = bufferIndex + contentLength;

    // must we increase our buffer?
    if (requiredBufferLength > buffer.length) {
      final int newLength;
      newLength = powerOfTwo(requiredBufferLength);

      buffer = Arrays.copyOf(buffer, newLength);

      noteSink.send(NOTES.readResize, id, newLength);
    }

    // unread bytes in buffer
    final int unread;
    unread = bufferLimit - bufferIndex;

    if (unread < 0) {
      return internalServerError(
          new AssertionError("unread < 0")
      );
    }

    // mark = remaining bytes to read
    mark = contentLength - unread;

    return $PARSE_BODY_FIXED_BUFFER_READ;
  }

  private record AppFormBufferSupport(int bufferIndex, Map<String, Object> queryParams) {}

  private byte executeParseBodyFixedBufferRead() {
    // restore remaining bytes to read
    final int remaining;
    remaining = mark;

    if (remaining < 0) {
      return internalServerError(
          new AssertionError("remaining < 0")
      );
    }

    else if (remaining == 0) {
      // read successful

      if (shouldParseAppForm()) {
        formParams = new AppFormBufferSupport(bufferIndex, copyQueryParams());

        return $PARSE_APP_FORM;
      } else {
        return $PARSE_BODY_FIXED_BUFFER_SUCCESS;
      }
    }

    try {
      final int read;
      read = inputStream.read(buffer, bufferLimit, remaining);

      if (read < 0) {
        noteSink.send(NOTES.readEof, id, this);

        return $ERROR;
      }

      bufferLimit += read;

      mark -= read;

      return $PARSE_BODY_FIXED_BUFFER_READ;
    } catch (IOException e) {
      return clientReadIOException(e);
    }
  }

  private byte executeParseBodyFixedBufferSuccess() {
    bodyKind = BodyKind.IN_BUFFER;

    return $REQUEST;
  }

  private static final class BodyFileSupport {

    byte[] buffer;

    int bufferIndex;

    int bufferLimit;

    final Path file;

    Object object;

    final OutputStream outputStream;

    Map<String, Object> queryParams;

    long remaining;

    final boolean shouldParseAppForm;

    byte state;

    final byte[] work;

    BodyFileSupport(Path file, OutputStream outputStream, long remaining, boolean shouldParseAppForm, byte[] work) {
      this.file = file;
      this.outputStream = outputStream;
      this.remaining = remaining;
      this.shouldParseAppForm = shouldParseAppForm;
      this.work = work;
    }

    final void copy(int length) throws IOException {
      outputStream.write(work, 0, length);

      remaining -= length;
    }

    final void write(byte[] bytes, int offset, int length) throws IOException {
      outputStream.write(bytes, offset, length);

      remaining -= length;
    }

    final void close() throws IOException {
      outputStream.close();
    }

  }

  private byte executeParseBodyFixedFile() {
    final BodyFileSupport support;

    try {
      final Path file;
      file = bodyFiles.file(id);

      final OutputStream outputStream;
      outputStream = bodyFiles.newOutputStream(file);

      final long remaining;
      remaining = long0;

      final boolean shouldParseAppForm;
      shouldParseAppForm = shouldParseAppForm();

      final byte[] work;
      work = new byte[maxBufferSize];

      support = new BodyFileSupport(file, outputStream, remaining, shouldParseAppForm, work);
    } catch (IOException e) {
      return internalServerError(e);
    }

    object = support;

    return $PARSE_BODY_FIXED_FILE_BUFFER;
  }

  private byte executeParseBodyFixedFileBuffer() {
    final BodyFileSupport support;
    support = (BodyFileSupport) object;

    // part of the body might be in the buffer
    final int buffered;
    buffered = bufferLimit - bufferIndex;

    if (buffered > 0) {
      try {
        support.write(buffer, bufferIndex, buffered);
      } catch (IOException e) {
        return internalServerError(e);
      }
    }

    if (support.shouldParseAppForm) {
      formParams = support;

      support.bufferIndex = bufferIndex;

      support.queryParams = copyQueryParams();

      support.state = state;

      return $PARSE_APP_FORM;
    } else {
      return $PARSE_BODY_FIXED_FILE_READ;
    }
  }

  private byte executeParseBodyFixedFileRead() {
    final BodyFileSupport support;
    support = (BodyFileSupport) object;

    final long remaining;
    remaining = support.remaining;

    if (remaining < 0) {
      return internalServerError(
          new AssertionError("support.remaining < 0")
      );
    }

    else if (remaining == 0) {
      // all done

      if (support.shouldParseAppForm) {
        // we should go back to the form parsing state
        final byte nextState;
        nextState = support.state;

        // let's push all the state we're going to alter
        support.buffer = buffer;
        support.bufferIndex = bufferIndex;
        support.bufferLimit = bufferLimit;

        // let's update the state for the parsing
        buffer = support.work;
        bufferIndex = 0;
        bufferLimit = 0;
        object = support.object;

        // when parsing ends, we should get back to this state
        support.state = $PARSE_BODY_FIXED_FILE_CLOSE;

        return nextState;
      } else {
        return $PARSE_BODY_FIXED_FILE_CLOSE;
      }
    }

    // work buffer
    final byte[] work;
    work = support.work;

    // this is guaranteed to be an int value
    final long iteration;
    iteration = Math.min(remaining, work.length);

    final int read;

    try {
      read = inputStream.read(work, 0, (int) iteration);
    } catch (IOException e) {
      return clientReadIOException(e);
    }

    if (read < 0) {
      noteSink.send(NOTES.readEof, id, this);

      return $ERROR;
    }

    try {
      support.copy(read);
    } catch (IOException e) {
      return internalServerError(e);
    }

    if (support.shouldParseAppForm) {
      // we should go back to the form parsing state
      final byte nextState;
      nextState = support.state;

      // let's push all the state we're going to alter
      support.buffer = buffer;
      support.bufferIndex = bufferIndex;
      support.bufferLimit = bufferLimit;

      // let's update the state for the parsing
      buffer = work;
      bufferIndex = 0;
      bufferLimit = read;
      object = support.object;

      // when parsing ends, we should get back to this state
      support.state = state;

      return nextState;
    } else {
      return $PARSE_BODY_FIXED_FILE_READ;
    }
  }

  private byte executeParseBodyFixedFileClose() {
    try {
      final BodyFileSupport support;
      support = (BodyFileSupport) object;

      support.close();

      bodyKind = BodyKind.FILE;

      object = support.file;

      return $REQUEST;
    } catch (IOException e) {
      return internalServerError(e);
    }
  }

  // ##################################################################
  // # END: Parse: Body
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse: application/x-www-form-urlencoded
  // ##################################################################

  @SuppressWarnings("unused")
  private static final class AppFormSupport {

    final byte[] buffer;

    int bufferIndex;

    int bufferLimit;

    @SuppressWarnings("unused")
    final Map<String, Object> queryParams;

    AppFormSupport(byte[] buffer, int bufferIndex, int bufferLimit, Map<String, Object> queryParams) {
      this.buffer = buffer;

      this.bufferIndex = bufferIndex;

      this.bufferLimit = bufferLimit;

      this.queryParams = queryParams;
    }

    final void advance() {
      bufferIndex += 1;
    }

    final byte current() {
      return buffer[bufferIndex];
    }

    final boolean hasNext() {
      return bufferIndex < bufferLimit;
    }

  }

  private byte executeParseAppForm() {
    // where the current key begins
    mark = bufferIndex;

    state = $PARSE_APP_FORM0;

    return executeParseAppForm0();
  }

  private byte executeParseAppForm0() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte code;
      code = parseQueryTable(b);

      return switch (code) {
        case QUERY_VALID -> { bufferIndex += 1; yield $PARSE_APP_FORM0; }

        case QUERY_PERCENT -> { appendInit(); bufferIndex += 1; yield toDecodePerc($PARSE_APP_FORM1); }

        case QUERY_PLUS -> { appendInit(); appendChar(' '); bufferIndex += 1; yield $PARSE_APP_FORM1; }

        case QUERY_EQUALS -> { object = markToString(); bufferIndex += 1; yield $PARSE_APP_FORM_VALUE; }

        case QUERY_AMPERSAND -> executeParseAppFormName0End($PARSE_APP_FORM);

        default -> toBadRequest(InvalidApplicationForm.CHAR);
      };
    } else {
      return toParseAppFormExhausted();
    }
  }

  private byte executeParseAppFormName0End(byte next) {
    object = markToString();

    bufferIndex += 1;

    makeQueryParam("");

    return next;
  }

  private byte executeParseAppForm1() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte code;
      code = parseQueryTable(b);

      return switch (code) {
        case QUERY_VALID -> { appendChar(b); bufferIndex += 1; yield $PARSE_APP_FORM1; }

        case QUERY_PERCENT -> { bufferIndex += 1; yield toDecodePerc($PARSE_APP_FORM1); }

        case QUERY_PLUS -> { appendChar(' '); bufferIndex += 1; yield $PARSE_APP_FORM1; }

        case QUERY_EQUALS -> { object = appendToString(); bufferIndex += 1; yield $PARSE_APP_FORM_VALUE; }

        case QUERY_AMPERSAND -> executeParseAppFormName1End($PARSE_APP_FORM);

        default -> toBadRequest(InvalidApplicationForm.CHAR);
      };
    } else {
      return toParseAppFormExhausted();
    }
  }

  private byte executeParseAppFormName1End(byte next) {
    object = appendToString();

    bufferIndex += 1;

    makeQueryParam("");

    return next;
  }

  private byte executeParseAppFormValue() {
    // where the current value begins
    mark = bufferIndex;

    state = $PARSE_APP_FORM_VALUE0;

    return executeParseAppFormValue0();
  }

  private byte executeParseAppFormValue0() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte code;
      code = parseQueryTable(b);

      return switch (code) {
        case QUERY_VALID, QUERY_EQUALS -> { bufferIndex += 1; yield $PARSE_APP_FORM_VALUE0; }

        case QUERY_PERCENT -> { appendInit(); bufferIndex += 1; yield toDecodePerc($PARSE_APP_FORM_VALUE1); }

        case QUERY_PLUS -> { appendInit(); appendChar(' '); bufferIndex += 1; yield $PARSE_APP_FORM_VALUE1; }

        case QUERY_AMPERSAND -> executeParseAppFormValue0End($PARSE_APP_FORM);

        default -> toBadRequest(InvalidApplicationForm.CHAR);
      };
    } else {
      return toParseAppFormExhausted();
    }
  }

  private byte executeParseAppFormValue0End(byte next) {
    final String v;
    v = markToString();

    bufferIndex += 1;

    makeQueryParam(v);

    return next;
  }

  private byte executeParseAppFormValue1() {
    if (bufferIndex < bufferLimit) {
      final byte b;
      b = buffer[bufferIndex];

      final byte code;
      code = parseQueryTable(b);

      return switch (code) {
        case QUERY_VALID, QUERY_EQUALS -> { appendChar(b); bufferIndex += 1; yield $PARSE_APP_FORM_VALUE1; }

        case QUERY_PERCENT -> { bufferIndex += 1; yield toDecodePerc($PARSE_APP_FORM_VALUE1); }

        case QUERY_PLUS -> { appendChar(' '); bufferIndex += 1; yield $PARSE_APP_FORM_VALUE1; }

        case QUERY_AMPERSAND -> executeParseAppFormValue1End($PARSE_APP_FORM);

        default -> toBadRequest(InvalidApplicationForm.CHAR);
      };
    } else {
      return toParseAppFormExhausted();
    }
  }

  private byte executeParseAppFormValue1End(byte next) {
    final String v;
    v = appendToString();

    bufferIndex += 1;

    makeQueryParam(v);

    return next;
  }

  private boolean shouldParseAppForm() {
    final HttpHeader contentType;
    contentType = headerUnchecked(HttpHeaderName.CONTENT_TYPE);

    if (contentType != null) {
      final String value;
      value = contentType.get(buffer);

      if (value != null && value.equalsIgnoreCase("application/x-www-form-urlencoded")) {
        return true;
      }
    }

    return false;
  }

  private byte toParseAppFormExhausted() {
    return switch (formParams) {
      case AppFormBufferSupport support -> toParseAppFormExhausted(support);

      case BodyFileSupport support -> toParseAppFormExhausted(support);

      default -> throw new AssertionError("Unexpected formParams=" + formParams);
    };
  }

  private byte toParseAppFormExhausted(AppFormBufferSupport support) {
    final byte next;
    next = toParseAppFormSuccess($PARSE_BODY_FIXED_BUFFER_SUCCESS);

    bufferIndex = support.bufferIndex;

    formParams = queryParams;

    queryParams = support.queryParams;

    return next;
  }

  private byte toParseAppFormExhausted(BodyFileSupport support) {
    return switch (support.state) {
      case $PARSE_BODY_FIXED_FILE_BUFFER -> {
        toParseBodyFixedFileReadInit(support);

        // restore previous state
        bufferIndex = support.bufferIndex;

        yield toParseBodyFixedFileRead(support);
      }

      case $PARSE_BODY_FIXED_FILE_READ -> {
        toParseBodyFixedFileReadInit(support);

        // restore previous state
        buffer = support.buffer;
        bufferIndex = support.bufferIndex;
        bufferLimit = support.bufferLimit;

        yield toParseBodyFixedFileRead(support);
      }

      case $PARSE_BODY_FIXED_FILE_CLOSE -> {
        // make last param if necessary
        switch (state) {
          case $PARSE_APP_FORM1 -> executeParseAppFormName1End($PARSE_BODY_FIXED_FILE_CLOSE);

          case $PARSE_APP_FORM_VALUE1 -> executeParseAppFormValue1End($PARSE_BODY_FIXED_FILE_CLOSE);

          default -> throw new AssertionError("Unexpected state=" + state);
        }

        formParams = queryParams;

        object = support;

        queryParams = support.queryParams;

        yield $PARSE_BODY_FIXED_FILE_CLOSE;
      }

      default -> throw new AssertionError("Unexpected support.state=" + support.state);
    };
  }

  private void toParseBodyFixedFileReadInit(BodyFileSupport support) {
    // we'll return to this state
    support.state = switch (state) {
      case $PARSE_APP_FORM0 -> {
        appendInit();

        yield $PARSE_APP_FORM1;
      }

      case $PARSE_APP_FORM_VALUE0 -> {
        appendInit();

        yield $PARSE_APP_FORM_VALUE1;
      }

      default -> state;
    };
  }

  private byte toParseBodyFixedFileRead(BodyFileSupport support) {
    // store eventual state
    support.object = object;

    // restore original object
    object = support;

    return $PARSE_BODY_FIXED_FILE_READ;
  }

  private byte toParseAppFormSuccess(byte next) {
    return switch (state) {
      case $PARSE_APP_FORM -> next;

      case $PARSE_APP_FORM0 -> executeParseAppFormName0End(next);

      case $PARSE_APP_FORM1 -> executeParseAppFormName1End(next);

      case $PARSE_APP_FORM_VALUE0 -> executeParseAppFormValue0End(next);

      case $PARSE_APP_FORM_VALUE1 -> executeParseAppFormValue1End(next);

      default -> throw new AssertionError("EOF unexpected state=" + state);
    };
  }

  // ##################################################################
  // # END: Parse: application/x-www-form-urlencoded
  // ##################################################################

  // ##################################################################
  // # BEGIN: Session Support
  // ##################################################################

  public final void session(HttpSession value) {
    session = value;
  }

  @Override
  public final boolean sessionAbsent() {
    return session == null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Class<T> key) {
    checkSession();

    final String name;
    name = key.getName();

    return (T) session.get0(name);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Lang.Key<T> key) {
    checkSession();

    Objects.requireNonNull(key, "key == null");

    return (T) session.get0(key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Class<T> key, T value) {
    checkSession();

    final String name;
    name = key.getName();

    Objects.requireNonNull(value, "value == null");

    return (T) session.set0(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttr(Key<T> key, T value) {
    checkSession();

    Objects.requireNonNull(key, "key == null");
    Objects.requireNonNull(value, "value == null");

    return (T) session.set0(key, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttrIfAbsent(Class<T> key, Supplier<? extends T> supplier) {
    checkSession();

    final String name;
    name = key.getName();

    Objects.requireNonNull(supplier, "supplier == null");

    return (T) session.setIfAbsent0(name, supplier);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T sessionAttrIfAbsent(Lang.Key<T> key, Supplier<? extends T> supplier) {
    checkSession();

    Objects.requireNonNull(key, "key == null");
    Objects.requireNonNull(supplier, "supplier == null");

    return (T) session.setIfAbsent0(key, supplier);
  }

  @Override
  public final boolean sessionPresent() {
    return session != null;
  }

  @Override
  public final void sessionInvalidate() {
    checkSession();

    session.invalidate();
  }

  private void checkSession() {
    checkRequest();

    if (session == null) {
      throw new IllegalStateException("No session associated to this exchange");
    }
  }

  // ##################################################################
  // # END: Session Support
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

  private byte executeLengthRequired() {
    noteSink.send(NOTES.lengthRequired, id, this);

    final String formatted;
    formatted = "The Content-Length header is required.\n";

    final byte[] message;
    message = formatted.getBytes(StandardCharsets.US_ASCII);

    statusUnchecked(Http.Status.LENGTH_REQUIRED);

    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, message.length);

    headerUnchecked(Http.HeaderName.CONNECTION, "close");

    sendUnchecked(message);

    return toWrite($ERROR);
  }

  private byte executeContentTooLarge() {
    noteSink.send(NOTES.contentTooLarge, id, this);

    final String formatted;
    formatted = "The request message body exceeds the server's maximum allowed limit.\n";

    final byte[] message;
    message = formatted.getBytes(StandardCharsets.US_ASCII);

    statusUnchecked(Http.Status.CONTENT_TOO_LARGE);

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

      case Media.Stream stream -> executeWriteStream(stream);

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
  private static final class ThisChunkedIOException extends RuntimeException {
    public ThisChunkedIOException(IOException cause) {
      super(cause);
    }

    @Override
    public final IOException getCause() {
      return (IOException) super.getCause();
    }
  }

  private class ChunkedOutputStream extends OutputStream {

    private boolean closed;

    public ChunkedOutputStream() {
      writeChunkBegin();
    }

    @Override
    public final void close() {
      if (!closed) {
        writeChunkClose();

        closed = true;
      }
    }

    @Override
    public final void flush() {
      // noop
    }

    @Override
    public final void write(int b) {
      int available;
      available = writeChunkAvailable();

      if (available <= 0) {
        writeChunkEnd();

        writeChunkFlush();

        writeChunkReset();

        writeChunkBegin();
      }

      buffer[bufferIndex++] = (byte) b;
    }

    @Override
    public final void write(byte[] bytes) {
      write(bytes, 0, bytes.length);
    }

    @Override
    public final void write(byte[] bytes, int offset, int length) {
      writeChunkBytes(bytes, offset, length);
    }

  }

  private byte executeWriteStream(Media.Stream stream) {
    try (ChunkedOutputStream out = new ChunkedOutputStream()) {
      stream.writeTo(out);

      return stateNext;
    } catch (ThisChunkedIOException e) {
      final IOException cause;
      cause = e.getCause();

      return clientWriteIOException(cause);
    } catch (Throwable t) {
      return internalServerError(t);
    }
  }

  private class ChunkedAppendable implements Appendable, Closeable {
    private final Charset charset;

    public ChunkedAppendable(Charset charset) {
      this.charset = charset;

      writeChunkBegin();
    }

    @Override
    public final void close() {
      writeChunkClose();
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

      writeChunkBytes(bytes, 0, bytes.length);

      return this;
    }

  }

  private byte executeWriteText(Media.Text text) {
    final Charset charset;
    charset = text.charset();

    try (ChunkedAppendable out = new ChunkedAppendable(charset)) {
      text.writeTo(out);

      return stateNext;
    } catch (ThisChunkedIOException e) {
      final IOException cause;
      cause = e.getCause();

      return clientWriteIOException(cause);
    } catch (Throwable t) {
      return internalServerError(t);
    }
  }

  private int writeChunkAvailable() {
    //return buffer.length - (bufferIndex + CHUNKED_TRAILER.length + 2);

    // it should be large enough to hold the CRLF
    return buffer.length - (bufferIndex + 2);
  }

  private void writeChunkBegin() {
    // chunkSizeLength = bytes required to store the chunk-size + CRLF
    int chunkSizeLength;
    chunkSizeLength = 0;

    // save space for (max) chunk-size
    chunkSizeLength += writeChunkMaxHexDigits();

    // save space for CRLF
    chunkSizeLength += 2;

    final int available;
    available = buffer.length - bufferIndex;

    if (available <= chunkSizeLength) {
      // remaining buffer is not enough for the chunk size
      // => flush

      writeChunkFlush();

      writeChunkReset();
    }

    // mark = where the chunk begins
    // -> the index of the first digit of the chunk size
    mark = bufferIndex;

    // markEnd = where the chunk data begins
    markEnd = mark + chunkSizeLength;

    bufferIndex = markEnd;
  }

  private int writeChunkMaxHexDigits() {
    // buffer will not increase its size during writes
    int maxDataLength;
    maxDataLength = buffer.length;

    // buffer must hold the last zero chunk
    // maxDataLength -= CHUNKED_TRAILER.length;

    // must hold the CRLF after data
    maxDataLength -= 2;

    // must hold the CRLF after chunk-size
    maxDataLength -= 2;

    // must hold at least 1 digit of the chunk-size
    maxDataLength -= 1;

    return Http.requiredHexDigits(maxDataLength);
  }

  private void writeChunkBytes(byte[] bytes, int offset, int length) {
    int bytesIndex;
    bytesIndex = offset;

    int remaining;
    remaining = length;

    while (remaining > 0) {
      int available;
      available = writeChunkAvailable();

      if (available <= 0) {
        writeChunkEnd();

        writeChunkFlush();

        writeChunkReset();

        writeChunkBegin();

        available = writeChunkAvailable();
      }

      final int bytesToCopy;
      bytesToCopy = Math.min(remaining, available);

      System.arraycopy(bytes, bytesIndex, buffer, bufferIndex, bytesToCopy);

      bufferIndex += bytesToCopy;

      bytesIndex += bytesToCopy;

      remaining -= bytesToCopy;
    }
  }

  private void writeChunkEnd() {
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

  private void writeChunkFlush() {
    try {
      outputStream.write(buffer, 0, bufferIndex);
    } catch (IOException e) {
      throw new ThisChunkedIOException(e);
    }
  }

  private void writeChunkClose() {
    writeChunkEnd();

    final int available;
    available = buffer.length - bufferIndex;

    final int trailerLength;
    trailerLength = CHUNKED_TRAILER.length;

    if (available < trailerLength) {
      // trailer won't fit in buffer
      // => flush

      writeChunkFlush();

      writeChunkReset();
    }

    System.arraycopy(CHUNKED_TRAILER, 0, buffer, bufferIndex, trailerLength);

    bufferIndex += trailerLength;

    writeChunkFlush();
  }

  private void writeChunkReset() {
    mark = markEnd = bufferIndex = 0;
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

      case FILE -> bodyFiles.newInputStream((Path) object);
    };
  }

  @Override
  public final String formParam(String name) {
    checkRequest();
    Objects.requireNonNull(name, "name == null");

    if (formParams == null) {
      return null;
    } else {
      final Map<String, Object> map;
      map = formParamsMap();

      return Http.queryParamsGet(map, name);
    }
  }

  @Override
  public final int formParamAsInt(String name, int defaultValue) {
    String maybe;
    maybe = formParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  @Override
  public final long formParamAsLong(String name, long defaultValue) {
    String maybe;
    maybe = formParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Long.parseLong(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  @Override
  public final List<String> formParamAll(String name) {
    checkRequest();
    Objects.requireNonNull(name, "name == null");

    if (formParams == null) {
      return List.of();
    } else {
      final Map<String, Object> map;
      map = formParamsMap();

      return Http.queryParamsGetAll(map, name);
    }
  }

  @Override
  public final IntStream formParamAllAsInt(String name, int defaultValue) {
    return formParamAll(name).stream().mapToInt(s -> {
      try {
        return Integer.parseInt(s);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    });
  }

  @Override
  public final LongStream formParamAllAsLong(String name, long defaultValue) {
    return formParamAll(name).stream().mapToLong(s -> {
      try {
        return Long.parseLong(s);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    });
  }

  @Override
  public final Set<String> formParamNames() {
    checkRequest();

    if (formParams == null) {
      return Set.of();
    } else {
      final Map<String, Object> map;
      map = formParamsMap();

      return map.keySet();
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> formParamsMap() {
    return (Map<String, Object>) formParams;
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
  public final void ok(Media.Stream media) {
    respond(Http.Status.OK, media);
  }

  @Override
  public final void ok(Media.Text media) {
    respond(Http.Status.OK, media);
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
  public final void forbidden(Media media) {
    respond(Http.Status.FORBIDDEN, media);
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
    public final void header(Http.HeaderName name, long value) {
      HttpExchange.this.header(name, value);
    }

    @Override
    public final void header(Http.HeaderName name, String value) {
      HttpExchange.this.header(name, value);
    }

    @Override
    public final void header(Http.HeaderName name, Consumer<? super Http.HeaderValueBuilder> builder) {
      HttpExchange.this.header(name, builder);
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

    @Override
    public final void media(Media media) {
      switch (media) {
        case Media.Bytes bytes -> {
          final String contentType;
          contentType = bytes.contentType();

          final byte[] array;
          array = bytes.toByteArray();

          checkMediaBytes(contentType, array);

          sendMediaBytes(contentType, array);
        }

        case Media.Text text -> {
          final String contentType;
          contentType = text.contentType();

          final Charset charset;
          charset = text.charset();

          checkMediaText(contentType, charset);

          sendMediaText(contentType, text);
        }

        case Media.Stream stream -> {
          final String contentType;
          contentType = stream.contentType();

          checkMediaStream(contentType);

          sendMediaStream(contentType, stream);
        }
      }
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

    final byte[] bytes;
    bytes = media.toByteArray();

    checkMediaBytes(contentType, bytes);

    statusUnchecked(status);

    sendMediaBytes(contentType, bytes);
  }

  private void checkMediaBytes(String contentType, byte[] bytes) {
    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Bytes provided a null content-type");
    }

    if (bytes == null) {
      throw new IllegalArgumentException("The specified Media.Bytes provided a null byte array");
    }
  }

  private void sendMediaBytes(String contentType, byte[] bytes) {
    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_TYPE, contentType);

    headerUnchecked(Http.HeaderName.CONTENT_LENGTH, bytes.length);

    sendUnchecked(bytes);
  }

  private void respond(Http.Status status, Media.Stream media) {
    checkRequest();

    // early media validation
    final String contentType;
    contentType = media.contentType();

    checkMediaStream(contentType);

    statusUnchecked(status);

    sendMediaStream(contentType, media);
  }

  private void checkMediaStream(String contentType) {
    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null content-type");
    }
  }

  private void sendMediaStream(String contentType, Media.Stream media) {
    headerUnchecked(Http.HeaderName.DATE, now());

    headerUnchecked(Http.HeaderName.CONTENT_TYPE, contentType);

    headerUnchecked(Http.HeaderName.TRANSFER_ENCODING, "chunked");

    sendUnchecked(media);
  }

  private void respond(Http.Status status, Media.Text media) {
    checkRequest();

    // early media validation
    final String contentType;
    contentType = media.contentType();

    final Charset charset;
    charset = media.charset();

    checkMediaText(contentType, charset);

    statusUnchecked(status);

    sendMediaText(contentType, media);
  }

  private void checkMediaText(String contentType, Charset charset) {
    if (contentType == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null content-type");
    }

    if (charset == null) {
      throw new IllegalArgumentException("The specified Media.Text provided a null charset");
    }
  }

  private void sendMediaText(String contentType, Media.Text media) {
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
    checkHeaderValue(value);

    headerUnchecked(name, value);
  }

  final void header(Http.HeaderName name, Consumer<? super Http.HeaderValueBuilder> builder) {
    checkResponseHeaders();
    Objects.requireNonNull(name, "name == null");

    stringBuilder.setLength(0);

    final HttpHeaderValueBuilder valueBuilder;
    valueBuilder = new HttpHeaderValueBuilder();

    builder.accept(valueBuilder);

    final String value;
    value = stringBuilder.toString();

    checkHeaderValue(value);

    headerUnchecked(name, value);
  }

  private void checkHeaderValue(String value) {
    enum Parser {

      START,

      NORMAL,

      WS,

      INVALID;

    }

    final int len;
    len = value.length(); // early implicit null-check

    Parser parser;
    parser = Parser.START;

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      if (c >= 128) {
        throw invalidFieldContent(idx, c);
      }

      final byte flag;
      flag = HEADER_VALUE_TABLE[c];

      switch (parser) {
        case START -> {
          if (flag == HEADER_VALUE_VALID) {
            parser = Parser.NORMAL;
          }

          else if (flag == HEADER_VALUE_WS) {
            throw new IllegalArgumentException("Leading SPACE or HTAB characters are not allowed");
          }

          else {
            throw invalidFieldContent(idx, c);
          }
        }

        case NORMAL, WS -> {
          if (flag == HEADER_VALUE_VALID) {
            parser = Parser.NORMAL;
          }

          else if (flag == HEADER_VALUE_WS) {
            parser = Parser.WS;
          }

          else {
            throw invalidFieldContent(idx, c);
          }
        }

        case INVALID -> {
          throw invalidFieldContent(idx, c);
        }
      }
    }

    switch (parser) {
      case START, NORMAL -> {
        // valid - noop
      }

      case WS -> {
        throw new IllegalArgumentException("Trailing SPACE or HTAB characters are not allowed");
      }

      case INVALID -> {
        throw new IllegalStateException("Unexpected INVALID state");
      }
    }
  }

  private IllegalArgumentException invalidFieldContent(int idx, char c) {
    return new IllegalArgumentException("Invalid character at index " + idx + ": " + c);
  }

  private static final byte[] HEADER_PARAM_NAME;

  private static final byte HEADER_PARAM_NAME_INVALID = 0;

  private static final byte HEADER_PARAM_NAME_VALID = 1;

  static {
    final byte[] table;
    table = new byte[128];

    Http.fillTable(table, Http.tchar(), HEADER_PARAM_NAME_VALID);

    HEADER_PARAM_NAME = table;
  }

  private static final byte[] HEADER_PARAM_VALUE;

  private static final byte HEADER_PARAM_VALUE_INVALID = 0;

  private static final byte HEADER_PARAM_VALUE_UNQUOTED = 1;

  private static final byte HEADER_PARAM_VALUE_QUOTED = 2;

  private static final byte HEADER_PARAM_VALUE_ESCAPE = 3;

  static {
    final byte[] table;
    table = new byte[128];

    // parameter-value = ( token / quoted-string )

    final String tchar;
    tchar = Http.tchar();

    for (int idx = 0, len = tchar.length(); idx < len; idx++) {
      final char c;
      c = tchar.charAt(idx);

      table[c] = HEADER_PARAM_VALUE_UNQUOTED;
    }

    final String vchar;
    vchar = Http.vchar();

    for (int idx = 0, len = vchar.length(); idx < len; idx++) {
      final char c;
      c = vchar.charAt(idx);

      if (table[c] == HEADER_PARAM_VALUE_INVALID) {
        table[c] = HEADER_PARAM_VALUE_QUOTED;
      }
    }

    table[' '] = HEADER_PARAM_VALUE_QUOTED;
    table['\t'] = HEADER_PARAM_VALUE_QUOTED;

    table['"'] = HEADER_PARAM_VALUE_ESCAPE;
    table['\\'] = HEADER_PARAM_VALUE_ESCAPE;

    HEADER_PARAM_VALUE = table;
  }

  final class HttpHeaderValueBuilder implements Http.HeaderValueBuilder {

    @Override
    public final void value(String value) {
      Objects.requireNonNull(value);

      if (!stringBuilder.isEmpty()) {
        stringBuilder.append(", ");
      }

      stringBuilder.append(value);
    }

    @Override
    public final void param(String name, String value) {
      checkParameterName(name);

      final int len; // early implicit null-check
      len = value.length();

      if (stringBuilder.isEmpty()) {
        throw new IllegalStateException("Cannot add a parameter: there's no current value");
      }

      stringBuilder.append(';');
      stringBuilder.append(' ');
      stringBuilder.append(name);
      stringBuilder.append('=');

      if (len == 0) {
        stringBuilder.append("\"\"");

        return;
      }

      // we assume value will be unquoted

      int quotesIndex;
      quotesIndex = stringBuilder.length();

      int valueIndex = 0;

      while (valueIndex < len) {
        final char c;
        c = value.charAt(valueIndex);

        if (c >= 128) {
          throw invalidFieldContent(valueIndex, c);
        }

        final byte flag;
        flag = HEADER_PARAM_VALUE[c];

        if (flag == HEADER_PARAM_VALUE_INVALID) {
          throw invalidFieldContent(valueIndex, c);
        }

        // we're safe so far, char is valid

        valueIndex++;

        if (flag == HEADER_PARAM_VALUE_UNQUOTED) {
          stringBuilder.append(c);

          continue;
        }

        // dquotes needed

        if (quotesIndex > 0) {
          stringBuilder.insert(quotesIndex, '"');

          quotesIndex = -1;
        }

        if (flag == HEADER_PARAM_VALUE_ESCAPE) {
          stringBuilder.append('\\');
        }

        stringBuilder.append(c);
      }

      if (quotesIndex < 0) {
        stringBuilder.append('"');
      }
    }

    @Override
    public final void param(String name, Charset charset, String value) {
      checkParameterName(name);

      if (charset != StandardCharsets.UTF_8) {
        throw new IllegalArgumentException("The UTF-8 charset MUST be used.");
      }

      Objects.requireNonNull(value, "value == null");

      stringBuilder.append(';');
      stringBuilder.append(' ');
      stringBuilder.append(name);
      stringBuilder.append('=');

      final String encoded;
      encoded = Http.rfc8187(value);

      stringBuilder.append(encoded);
    }

    private void checkParameterName(String name) {
      final int len;
      len = name.length();

      for (int idx = 0; idx < len; idx++) {
        final char c;
        c = name.charAt(idx);

        if (c >= 128) {
          throw invalidParameterName(idx, c);
        }

        final byte flag;
        flag = HEADER_PARAM_NAME[c];

        if (flag == HEADER_PARAM_NAME_INVALID) {
          throw invalidParameterName(idx, c);
        }
      }
    }

    private IllegalArgumentException invalidParameterName(int idx, char c) {
      return new IllegalArgumentException(
          "Parameter name contains an invalid character at index " + idx + ": '" + c + "'"
      );
    }

  }

  final void status(Http.Status status) {
    checkRequest();
    Objects.requireNonNull(status, "status == null");

    statusUnchecked(status);
  }

  private static final byte[][] STATUS_LINES;

  static {
    final HttpStatus[] values;
    values = HttpStatus.values();

    final int size;
    size = values.length;

    final byte[][] map;
    map = new byte[size][];

    for (HttpStatus status : values) {
      final int index;
      index = status.ordinal();

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
    statusBytes = STATUS_LINES[internal.ordinal()];

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

    if (value.isEmpty()) {
      writeBytes(Bytes.COLON_BYTES);
    } else {
      // write out the separator
      writeBytes(Bytes.COLONSP);

      // write out the value
      byte[] valueBytes;
      valueBytes = value.getBytes(StandardCharsets.US_ASCII);

      writeBytes(valueBytes);
    }

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

  private void sendUnchecked(Media.Stream stream) {
    terminate();

    if (method == Http.Method.HEAD) {

      body(null);

    } else {

      body(stream);

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
    if (bodyKind == BodyKind.FILE) {

      try {
        final Path file;
        file = (Path) object;

        Files.delete(file);
      } catch (Throwable e) {
        // TODO log exception
      }

    }

    object = value;

    responseListener.body(value);
  }

  private void terminate() {
    if (session != null) {

      final String setCookie;
      setCookie = session.consumeSetCookie();

      if (setCookie != null) {
        headerUnchecked(Http.HeaderName.SET_COOKIE, setCookie);
      }

    }

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