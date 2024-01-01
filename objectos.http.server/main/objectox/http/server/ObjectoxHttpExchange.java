/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectox.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import objectos.http.HeaderName;
import objectos.http.HeaderValue;
import objectos.http.Http;
import objectos.http.Http.Method;
import objectos.http.Http.Status;
import objectos.http.server.Body;
import objectos.http.server.CharWritable;
import objectos.http.server.HttpExchange;
import objectos.http.server.Segment;
import objectos.http.server.ServerExchangeResult;
import objectos.http.server.UriQuery;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.util.list.GrowableList;
import objectox.http.HttpStatus;
import objectox.http.StandardHeaderName;

public final class ObjectoxHttpExchange implements HttpExchange {

  public non-sealed static abstract class OptionValue implements objectos.http.server.HttpExchange.Option {

    OptionValue() {}

    public static OptionValue bufferSize(int size) {
      return new OptionValue() {
        @Override
        public final void accept(ObjectoxHttpExchange builder) {
          builder.buffer = new byte[size];
        }
      };
    }

    public static OptionValue noteSink(NoteSink noteSink) {
      return new OptionValue() {
        @Override
        public final void accept(ObjectoxHttpExchange builder) {
          builder.noteSink = noteSink;
        }
      };
    }

    public abstract void accept(ObjectoxHttpExchange builder);

  }

  public record ProcessedRecord(SocketAddress remoteAddress,
                                Method method,
                                String target,
                                Status status,
                                long processingTime)
      implements objectos.http.server.HttpExchange.Processed {}

  private static final Map<Method, byte[]> METHOD_NAME_AND_SPACE;

  static {
    Map<Method, byte[]> map;
    map = new EnumMap<>(Method.class);

    for (var method : Method.values()) {
      String name;
      name = method.name();

      byte[] value;
      value = (name + " ").getBytes(StandardCharsets.UTF_8);

      map.put(method, value);
    }

    METHOD_NAME_AND_SPACE = map;
  }

  // Config phase

  static final byte _CONFIG = 0;

  // Setup phase

  static final byte _SETUP = 1;

  // Input phase

  static final byte _INPUT = 2;
  static final byte _INPUT_READ = 3;
  static final byte _INPUT_READ_EOF = 4;
  static final byte _INPUT_READ_ERROR = 5;

  // Input / Request Line phase

  static final byte _REQUEST_LINE = 6;
  static final byte _REQUEST_LINE_METHOD = 7;
  static final byte _REQUEST_LINE_METHOD_P = 8;
  static final byte _REQUEST_LINE_TARGET = 9;
  static final byte _REQUEST_LINE_PATH = 10;
  static final byte _REQUEST_LINE_QUERY = 11;
  static final byte _REQUEST_LINE_VERSION = 12;

  // Input / Parse header phase

  static final byte _PARSE_HEADER = 13;
  static final byte _PARSE_HEADER_NAME = 14;
  static final byte _PARSE_HEADER_NAME_CASE_INSENSITIVE = 15;
  static final byte _PARSE_HEADER_VALUE = 16;

  // Input / Request Body

  static final byte _REQUEST_BODY = 17;

  // Handle phase

  static final byte _HANDLE = 18;

  // Output phase

  static final byte _OUTPUT = 19;
  static final byte _OUTPUT_BODY = 20;
  static final byte _OUTPUT_BUFFER = 21;
  static final byte _OUTPUT_HEADER = 22;
  static final byte _OUTPUT_TERMINATOR = 23;
  static final byte _OUTPUT_STATUS = 24;

  // Result phase

  static final byte _RESULT = 25;

  // Non-executable states

  static final byte _KEEP_ALIVE = 26;
  static final byte _HANDLE_INVOKE = 27;
  static final byte _REQUEST_ERROR = 28;
  static final byte _STOP = 29;

  byte[] buffer;

  int bufferIndex;

  int bufferLimit;

  int bufferSize = 1024;

  IOException error;

  boolean keepAlive;

  Http.Method method;

  byte nextAction;

  NoteSink noteSink = NoOpNoteSink.of();

  ObjectoxUriQuery query;

  HttpRequestBody requestBody;

  Object requestHeaderName;

  Map<StandardHeaderName, BufferHeaderValue> requestHeaders;

  Map<String, BufferHeaderValue> requestHeadersUnknown;

  HttpRequestPath requestPath;

  Object responseBody;

  List<HttpResponseHeader> responseHeaders;

  int responseHeadersIndex;

  Socket socket;

  long startTime;

  byte state;

  Http.Status status;

  byte versionMajor;

  byte versionMinor;

  public ObjectoxHttpExchange(Socket socket) {
    this.socket = socket;

    keepAlive = true;

    state = _SETUP;
  }

  public ObjectoxHttpExchange(Socket socket, boolean newApi) {
    this.socket = socket;

    state = _CONFIG;
  }

  /**
   * For testing purposes only.
   */
  ObjectoxHttpExchange() {}

  // new api

  @Override
  public final void bufferSize(int size) {
    checkConfig();

    Check.argument(size >= 128, "buffer size must be >= 128");

    bufferSize = size;
  }

  @Override
  public final void noteSink(NoteSink noteSink) {
    checkConfig();

    this.noteSink = Check.notNull(noteSink, "noteSink == null");
  }

  private void checkConfig() {
    Check.state(state == _CONFIG, "This configuration method cannot be called at this moment");
  }

  @Override
  public final ServerExchangeResult get() throws IOException {
    throw new UnsupportedOperationException("Implement me :: state=" + state);
  }

  // old api

  public final ObjectoxHttpExchange init() {
    if (buffer == null) {
      buffer = new byte[1024];
    }

    return this;
  }

  @Override
  public final boolean active() {
    return switch (state) {
      case _SETUP -> executeRequestPhase();

      case _HANDLE_INVOKE -> executeResponsePhase();

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    };
  }

  private boolean executeRequestPhase() {
    while (state <= _HANDLE) {
      stepOne();
    }

    return state != _STOP;
  }

  private boolean executeResponsePhase() {
    state = _OUTPUT;

    while (state <= _RESULT) {
      stepOne();
    }

    if (state == _KEEP_ALIVE) {
      state = _SETUP;

      return executeRequestPhase();
    } else {
      return state != _STOP;
    }
  }

  @Override
  public final void close() throws IOException {
    socket.close();
  }

  @Override
  public final Body body() {
    checkStateHandle();

    return requestBody;
  }

  @Override
  public final HeaderValue header(HeaderName name) {
    Check.notNull(name, "name == null");

    checkStateHandle();

    HeaderValue value;
    value = requestHeaders.get(name);

    if (value == null) {
      value = HeaderValue.NULL;
    }

    return value;
  }

  @Override
  public final boolean matches(Segment pat) {
    checkStateHandle();

    return requestPath.matches(pat);
  }

  @Override
  public final boolean matches(Segment pat1, Segment pat2) {
    checkStateHandle();

    return requestPath.matches(pat1, pat2);
  }

  @Override
  public final Method method() {
    checkStateHandle();

    return method;
  }

  @Override
  public final boolean methodIn(Method m1, Method m2) {
    checkStateHandle();

    return method.equals(m1) || method.equals(m2);
  }

  @Override
  public final String path() {
    checkStateHandle();

    return requestPath.toString();
  }

  @Override
  public final boolean pathEquals(String s) {
    Check.notNull(s, "s == null");

    checkStateHandle();

    return requestPath.pathEquals(s);
  }

  @Override
  public final boolean pathStartsWith(String prefix) {
    Check.notNull(prefix, "prefix == null");

    checkStateHandle();

    return requestPath.pathStartsWith(prefix);
  }

  @Override
  public final UriQuery query() {
    checkStateHandle();

    if (query != null) {
      return query;
    }

    return EmptyUriQuery.INSTANCE;
  }

  @Override
  public final String segment(int index) {
    checkStateHandle();

    return requestPath.segment(index);
  }

  @Override
  public final Path segmentsAsPath() {
    checkStateHandle();

    return requestPath.toPath();
  }

  @Override
  public final Status status() {
    checkStateHandle();

    return status;
  }

  @Override
  public final boolean statusPresent() {
    checkStateHandle();

    return status != null;
  }

  @Override
  public final boolean hasResponse() {
    checkStateHandle();

    return status != null;
  }

  @Override
  public final void status(Http.Status status) {
    Check.notNull(status, "status == null");

    checkStateHandle();

    this.status = status;
  }

  @Override
  public final void header(HeaderName name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    checkStateHandle();

    if (name == StandardHeaderName.CONNECTION && value.equalsIgnoreCase("close")) {
      keepAlive = false;
    }

    HttpResponseHeader header;
    header = new HttpResponseHeader(name, value);

    responseHeaders.add(header);
  }

  @Override
  public final void body(byte[] data) {
    Check.notNull(data, "data == null");

    checkStateHandle();

    responseBody = data;
  }

  @Override
  public final void body(CharWritable entity, Charset charset) {
    Check.notNull(entity, "entity == null");
    Check.notNull(charset, "charset == null");

    checkStateHandle();

    responseBody = new HttpChunkedChars(this, entity, charset);
  }

  @Override
  public final void bodyClear() {
    checkStateHandle();

    responseBody = null;
  }

  private void checkStateHandle() {
    if (state != _HANDLE_INVOKE) {
      throw new IllegalStateException(
          "Request has not been parsed yet or response has already been sent."
      );
    }
  }

  final void stepOne() {
    state = switch (state) {
      // Config phase

      case _CONFIG -> config();

      // Setup phase

      case _SETUP -> setup();

      // Input phase

      case _INPUT -> input();
      case _INPUT_READ -> inputRead();
      case _INPUT_READ_EOF -> inputReadEof();
      case _INPUT_READ_ERROR -> inputReadError();

      // Input / Request Line phase

      case _REQUEST_LINE -> requestLine();
      case _REQUEST_LINE_METHOD -> requestLineMethod();
      case _REQUEST_LINE_METHOD_P -> requestLineMethodP();
      case _REQUEST_LINE_TARGET -> requestLineTarget();
      case _REQUEST_LINE_PATH -> requestLinePath();
      case _REQUEST_LINE_QUERY -> requestLineQuery();
      case _REQUEST_LINE_VERSION -> requestLineVersion();

      // Input / Parse Header phase

      case _PARSE_HEADER -> parseHeader();
      case _PARSE_HEADER_NAME -> parseHeaderName();
      case _PARSE_HEADER_VALUE -> parseHeaderValue();

      // Input / Request Body

      case _REQUEST_BODY -> requestBody();

      // Handle phase

      case _HANDLE -> handle();

      // Output phase

      case _OUTPUT -> output();
      case _OUTPUT_BODY -> outputBody();
      case _OUTPUT_BUFFER -> outputBuffer();
      case _OUTPUT_HEADER -> outputHeader();
      case _OUTPUT_TERMINATOR -> outputTerminator();
      case _OUTPUT_STATUS -> outputStatus();

      // Result phase

      case _RESULT -> result();

      case _REQUEST_ERROR -> {
        throw new UnsupportedOperationException(
            "status=" + status, error
        );
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
      );
    };
  }

  private byte config() {
    if (buffer == null) {
      buffer = new byte[bufferSize];
    }

    if (noteSink == null) {
      noteSink = NoOpNoteSink.of();
    }

    return _SETUP;
  }

  private boolean bufferEquals(byte[] target, int start) {
    int requiredIndex;
    requiredIndex = start + target.length;

    if (!bufferHasIndex(requiredIndex)) {
      return false;
    }

    return Arrays.equals(
        buffer, start, requiredIndex,
        target, 0, target.length);
  }

  private byte bufferGet(int index) {
    return buffer[index];
  }

  private boolean bufferHasIndex(int index) {
    return index < bufferLimit;
  }

  private byte handle() {
    keepAlive = handle0KeepAlive();

    if (requestHeadersUnknown != null && !requestHeadersUnknown.isEmpty()) {
      Set<String> names;
      names = requestHeadersUnknown.keySet();

      noteSink.send(UKNOWN_HEADER_NAMES, names);
    }

    responseBody = null;

    if (responseHeaders == null) {
      responseHeaders = new GrowableList<>();
    } else {
      responseHeaders.clear();
    }

    return _HANDLE_INVOKE;
  }

  private boolean handle0KeepAlive() {
    BufferHeaderValue connection;
    connection = requestHeaders.getOrDefault(StandardHeaderName.CONNECTION, BufferHeaderValue.EMPTY);

    if (connection.contentEquals(Bytes.KEEP_ALIVE)) {
      return true;
    }

    if (connection.contentEquals(Bytes.CLOSE)) {
      return false;
    }

    return versionMajor == 1 && versionMinor >= 1;
  }

  private byte input() {
    nextAction = _REQUEST_LINE;

    return inputRead();
  }

  private byte inputRead() {
    InputStream inputStream;

    try {
      inputStream = socket.getInputStream();
    } catch (IOException e) {
      error = e;

      return _INPUT_READ_ERROR;
    }

    int writableLength;
    writableLength = buffer.length - bufferLimit;

    int bytesRead;

    try {
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);
    } catch (IOException e) {
      error = e;

      return _INPUT_READ_ERROR;
    }

    if (bytesRead < 0) {
      return _INPUT_READ_EOF;
    }

    bufferLimit += bytesRead;

    return nextAction;
  }

  private byte inputReadEof() {
    reset();

    keepAlive = false;

    return _STOP;
  }

  private byte inputReadError() {
    noteSink.send(IO_READ_ERROR, error);

    error = null;

    return inputReadEof();
  }

  private byte output() {
    bufferIndex = bufferLimit = responseHeadersIndex = 0;

    return _OUTPUT_STATUS;
  }

  private byte outputBody() {
    try {
      OutputStream outputStream;
      outputStream = socket.getOutputStream();

      // write headers + terminator
      outputStream.write(buffer, 0, bufferLimit);

      if (responseBody == null) {
        return _RESULT;
      }

      if (responseBody instanceof byte[] bytes) {
        outputStream.write(bytes, 0, bytes.length);

        return _RESULT;
      }

      if (responseBody instanceof HttpChunkedChars entity) {
        bufferLimit = 0;

        entity.write();

        return _RESULT;
      }

      throw new UnsupportedOperationException(
          "Implement me :: type=" + responseBody.getClass()
      );
    } catch (IOException e) {
      return toResult(e);
    }
  }

  private byte toResult(IOException e) {
    error = e;

    return _RESULT;
  }

  private byte outputBuffer() {
    try {
      OutputStream outputStream;
      outputStream = socket.getOutputStream();

      outputStream.write(buffer, 0, bufferLimit);

      bufferLimit = 0;

      return nextAction;
    } catch (IOException e) {
      return toResult(e);
    }
  }

  private byte outputHeader() {
    if (responseHeadersIndex == responseHeaders.size()) {
      return _OUTPUT_TERMINATOR;
    }

    HttpResponseHeader header;
    header = responseHeaders.get(responseHeadersIndex);

    byte[] headerBytes;
    headerBytes = header.bytes();

    int headerLength;
    headerLength = headerBytes.length;

    int requiredLength;
    requiredLength = bufferLimit + headerLength;

    if (buffer.length < requiredLength) {
      nextAction = _OUTPUT_HEADER;

      return _OUTPUT_BUFFER;
    }

    System.arraycopy(headerBytes, 0, buffer, bufferLimit, headerLength);

    bufferLimit += headerLength;

    responseHeadersIndex++;

    return _OUTPUT_HEADER;
  }

  private static final Map<HttpStatus, byte[]> HTTP_STATUS_RESPONSE_BYTES;

  static {
    Map<HttpStatus, byte[]> map;
    map = new EnumMap<>(HttpStatus.class);

    for (var status : HttpStatus.values()) {
      String response;
      response = Integer.toString(status.code()) + " " + status.description() + "\r\n";

      byte[] responseBytes;
      responseBytes = Bytes.utf8(response);

      map.put(status, responseBytes);
    }

    HTTP_STATUS_RESPONSE_BYTES = map;
  }

  private byte outputStatus() {
    // Buffer will be large enough for status line.
    // Enforced during server creation (in theory).
    // In any case, let's be sure...

    int requiredLength;

    Version version;
    version = Version.HTTP_1_1;

    requiredLength = version.responseBytes.length;

    byte[] statusBytes;

    if (status instanceof HttpStatus internal) {
      statusBytes = HTTP_STATUS_RESPONSE_BYTES.get(internal);
    } else {
      String response;
      response = Integer.toString(status.code()) + " " + status.description() + "\r\n";

      statusBytes = Bytes.utf8(response);
    }

    requiredLength += statusBytes.length;

    if (buffer.length < requiredLength) {
      // we could send the response unbuffered.
      // Instead this should be considered a bug in the library

      // TODO log irrecoverable error

      error = new IOException("""
      Buffer is not large enough to write out status line.

      buffer.length=%d
      requiredLength=%d

      Please increase the internal buffer size
      """.formatted(buffer.length, requiredLength));

      return _RESULT;
    }

    byte[] bytes;
    bytes = version.responseBytes;

    System.arraycopy(bytes, 0, buffer, bufferLimit, bytes.length);

    bufferLimit += bytes.length;

    bytes = statusBytes;

    System.arraycopy(bytes, 0, buffer, bufferLimit, bytes.length);

    bufferLimit += bytes.length;

    return _OUTPUT_HEADER;
  }

  private byte outputTerminator() {
    // buffer must be large enough to hold CR + LF

    int requiredLength;
    requiredLength = bufferLimit + 2;

    if (buffer.length < requiredLength) {
      // buffer is not large enough
      // flush buffer and try again

      nextAction = _OUTPUT_TERMINATOR;

      return _OUTPUT_BUFFER;
    }

    buffer[bufferLimit++] = Bytes.CR;

    buffer[bufferLimit++] = Bytes.LF;

    return _OUTPUT_BODY;
  }

  private byte parseHeader() {
    // if the buffer matches CRLF or LF then header has ended
    // otherwise, we will try to parse the header field name

    int index;
    index = bufferIndex;

    if (!bufferHasIndex(index)) {
      // ask for more data

      return toInputReadIfPossible(state, HttpStatus.BAD_REQUEST);
    }

    // we'll check if buffer is CRLF

    final byte first;
    first = bufferGet(index++);

    if (first == Bytes.CR) {
      // ok, first is CR

      if (!bufferHasIndex(index)) {
        // ask for more data

        return toInputReadIfPossible(state, HttpStatus.BAD_REQUEST);
      }

      final byte second;
      second = bufferGet(index++);

      if (second == Bytes.LF) {
        bufferIndex = index;

        return toHandleOrRequestBody();
      }

      // not sure the best way to handle this case

      return _PARSE_HEADER_NAME;
    }

    if (first == Bytes.LF) {
      bufferIndex = index;

      return toHandleOrRequestBody();
    }

    return _PARSE_HEADER_NAME;
  }

  private byte parseHeaderName() {
    // we reset any previous found header name

    requestHeaderName = null;

    // we will search the buffer for a ':' char

    final int nameStart;
    nameStart = bufferIndex;

    int colonIndex;
    colonIndex = nameStart;

    boolean found;
    found = false;

    for (; bufferHasIndex(colonIndex); colonIndex++) {
      byte b;
      b = bufferGet(colonIndex);

      if (b == Bytes.COLON) {
        found = true;

        break;
      }
    }

    if (!found) {
      // ':' was not found
      // read more data if possible

      return toInputReadIfPossible(state, HttpStatus.BAD_REQUEST);
    }

    // we will use the first char as hash code

    final byte first;
    first = bufferGet(nameStart);

    // bufferIndex will resume immediately after colon

    bufferIndex = colonIndex + 1;

    // ad hoc hash map

    return switch (first) {
      case 'A' -> parseHeaderName0(nameStart,
          StandardHeaderName.ACCEPT_ENCODING
      );

      case 'C' -> parseHeaderName0(nameStart,
          StandardHeaderName.CONNECTION,
          StandardHeaderName.CONTENT_LENGTH,
          StandardHeaderName.CONTENT_TYPE,
          StandardHeaderName.COOKIE
      );

      case 'D' -> parseHeaderName0(nameStart,
          StandardHeaderName.DATE
      );

      case 'H' -> parseHeaderName0(nameStart,
          StandardHeaderName.HOST
      );

      case 'T' -> parseHeaderName0(nameStart,
          StandardHeaderName.TRANSFER_ENCODING
      );

      case 'U' -> parseHeaderName0(nameStart,
          StandardHeaderName.USER_AGENT
      );

      default -> {
        int length;
        length = colonIndex - nameStart;

        requestHeaderName = new String(buffer, nameStart, length);

        yield _PARSE_HEADER_VALUE;
      }
    };
  }

  private static final Map<StandardHeaderName, byte[]> STD_HEADER_NAME_BYTES;

  static {
    Map<StandardHeaderName, byte[]> map;
    map = new EnumMap<>(StandardHeaderName.class);

    for (var headerName : StandardHeaderName.values()) {
      String name;
      name = headerName.name;

      byte[] bytes;
      bytes = name.getBytes(StandardCharsets.UTF_8);

      map.put(headerName, bytes);
    }

    STD_HEADER_NAME_BYTES = map;
  }

  private byte parseHeaderName0(int nameStart, StandardHeaderName candidate) {
    final byte[] candidateBytes;
    candidateBytes = STD_HEADER_NAME_BYTES.get(candidate);

    if (bufferEquals(candidateBytes, nameStart)) {
      requestHeaderName = candidate;
    }

    return _PARSE_HEADER_VALUE;
  }

  private byte parseHeaderName0(int nameStart,
                                StandardHeaderName c0, StandardHeaderName c1, StandardHeaderName c2,
                                StandardHeaderName c3) {
    byte result;
    result = parseHeaderName0(nameStart, c0);

    if (requestHeaderName != null) {
      return result;
    }

    result = parseHeaderName0(nameStart, c1);

    if (requestHeaderName != null) {
      return result;
    }

    result = parseHeaderName0(nameStart, c2);

    if (requestHeaderName != null) {
      return result;
    }

    return parseHeaderName0(nameStart, c3);
  }

  private byte parseHeaderValue() {
    int valueStart;
    valueStart = bufferIndex;

    // we search for the LF char that marks the end-of-line

    int lfIndex = valueStart;

    boolean found;
    found = false;

    for (; bufferHasIndex(lfIndex); lfIndex++) {
      byte b;
      b = bufferGet(lfIndex);

      if (b == Bytes.LF) {
        found = true;

        break;
      }
    }

    if (!found) {
      // LF was not found

      return toInputReadIfPossible(state, HttpStatus.BAD_REQUEST);
    }

    // we'll trim any SP, HTAB or CR from the end of the value

    int valueEnd;
    valueEnd = lfIndex;

    loop: for (; valueEnd > valueStart; valueEnd--) {
      byte b;
      b = bufferGet(valueEnd - 1);

      switch (b) {
        case Bytes.SP, Bytes.HTAB, Bytes.CR -> {
          continue loop;
        }

        default -> {
          break loop;
        }
      }
    }

    // we'll trim any OWS, if found, from the start of the value

    for (; valueStart < valueEnd; valueStart++) {
      byte b;
      b = bufferGet(valueStart);

      if (!Bytes.isOptionalWhitespace(b)) {
        break;
      }
    }

    if (requestHeaderName instanceof StandardHeaderName headerName) {
      BufferHeaderValue headerValue;
      headerValue = new BufferHeaderValue(buffer, valueStart, valueEnd);

      if (requestHeaders == null) {
        requestHeaders = new EnumMap<>(StandardHeaderName.class);
      }

      BufferHeaderValue previousValue;
      previousValue = requestHeaders.put(headerName, headerValue);

      if (previousValue != null) {
        throw new UnsupportedOperationException("Implement me");
      }
    }

    else if (requestHeaderName instanceof String name) {
      BufferHeaderValue headerValue;
      headerValue = new BufferHeaderValue(buffer, valueStart, valueEnd);

      if (requestHeadersUnknown == null) {
        requestHeadersUnknown = new HashMap<>();
      }

      HeaderValue previousValue;
      previousValue = requestHeadersUnknown.put(name, headerValue);

      if (previousValue != null) {
        throw new UnsupportedOperationException("Implement me");
      }
    }

    // reset header name just in case

    requestHeaderName = null;

    // we have found the value.
    // bufferIndex should point to the position immediately after the LF char

    bufferIndex = lfIndex + 1;

    return _PARSE_HEADER;
  }

  private byte requestBody() {
    // reset our state

    requestBody = null;

    // Let's check if this is a fixed length or a chunked transfer

    BufferHeaderValue contentLength;
    contentLength = requestHeaders.get(StandardHeaderName.CONTENT_LENGTH);

    if (contentLength == null) {
      // TODO multipart/form-data?

      throw new UnsupportedOperationException(
          "Implement me :: probably chunked transfer encoding"
      );
    }

    // this is a fixed length body, let's see if the length is valid

    long length;
    length = contentLength.unsignedLongValue();

    if (length < 0) {
      return toRequestError(HttpStatus.BAD_REQUEST);
    }

    // maybe we already have the body in our buffer...

    int bufferRemaining;
    bufferRemaining = bufferLimit - bufferIndex;

    if (bufferRemaining == length) {
      // the body has already been read into our buffer

      requestBody = HttpRequestBody.inBuffer(buffer, bufferIndex, bufferLimit);

      bufferIndex = bufferLimit;

      return _HANDLE;
    }

    throw new UnsupportedOperationException(
        "Implement me :: read more body"
    );
  }

  private byte requestLine() {
    int methodStart;
    methodStart = bufferIndex;

    // the next call is safe.
    //
    // if we got here, InputStream::read returned at least 1 byte
    // InputStream::read only returns 0 when len == 0 in read(array, off, len)
    // otherwise it returns > 0, or -1 when EOF or throws IOException

    byte first;
    first = bufferGet(methodStart);

    // based on the first char, we select out method candidate

    return switch (first) {
      case 'C' -> toRequestLineMethod(Http.Method.CONNECT);

      case 'D' -> toRequestLineMethod(Http.Method.DELETE);

      case 'G' -> toRequestLineMethod(Http.Method.GET);

      case 'H' -> toRequestLineMethod(Http.Method.HEAD);

      case 'O' -> toRequestLineMethod(Http.Method.OPTIONS);

      case 'P' -> _REQUEST_LINE_METHOD_P;

      case 'T' -> toRequestLineMethod(Http.Method.TRACE);

      // first char does not match any candidate
      // we are sure this is a bad request

      default -> toRequestError(HttpStatus.BAD_REQUEST);
    };
  }

  private byte requestLineMethod() {
    // method candidate @ start of the buffer

    int candidateStart;
    candidateStart = bufferIndex;

    // we'll check if the buffer contents matches 'METHOD SP'

    byte[] candidateBytes;
    candidateBytes = METHOD_NAME_AND_SPACE.get(method);

    int requiredIndex;
    requiredIndex = candidateStart + candidateBytes.length;

    if (!bufferHasIndex(requiredIndex)) {
      // we don't have enough bytes in the buffer...
      // assuming the client is slow on sending data

      // clear method candidate just in case...
      // TODO 2023-12-17 this is probably wrong...
      method = null;

      return toInputRead(state);
    }

    if (!bufferEquals(candidateBytes, candidateStart)) {
      // we have enough bytes and they don't match our 'method name + SP'
      // respond with bad request

      // clear method candidate just in case...
      method = null;

      return toRequestError(HttpStatus.BAD_REQUEST);
    }

    // request OK so far...
    // update the bufferIndex

    bufferIndex = requiredIndex;

    // continue to request target

    return _REQUEST_LINE_TARGET;
  }

  private byte requestLineMethodP() {
    // method starts with a P. It might be:
    // - PATCH
    // - POST
    // - PUT
    //
    // so we'll peek at the second character

    int secondCharIndex;
    secondCharIndex = bufferIndex + 1;

    if (!bufferHasIndex(secondCharIndex)) {
      // we don't have enough bytes in the buffer...
      // assuming the client is slow on sending data

      return toInputRead(state);
    }

    byte secondChar;
    secondChar = bufferGet(secondCharIndex);

    // based on the second char, we select out method candidate

    return switch (secondChar) {
      case 'A' -> toRequestLineMethod(Http.Method.PATCH);

      case 'O' -> toRequestLineMethod(Http.Method.POST);

      case 'U' -> toRequestLineMethod(Http.Method.PUT);

      // it does not match any candidate
      // we are sure this is a bad request

      default -> toRequestError(HttpStatus.BAD_REQUEST);
    };
  }

  private byte requestLinePath() {
    // we will look for the first SP char

    for (; bufferHasIndex(bufferIndex); bufferIndex++) {
      byte b;
      b = bufferGet(bufferIndex);

      switch (b) {
        case Bytes.QUESTION_MARK -> {
          requestPath.end(bufferIndex);

          // bufferIndex immediately after the ? char

          bufferIndex = bufferIndex + 1;

          query = new ObjectoxUriQuery(buffer, bufferIndex);

          return _REQUEST_LINE_QUERY;
        }

        case Bytes.SOLIDUS -> requestPath.slash(bufferIndex);

        case Bytes.SP -> {
          requestPath.end(bufferIndex);

          // bufferIndex immediately after the SP char

          bufferIndex = bufferIndex + 1;

          return _REQUEST_LINE_VERSION;
        }
      }
    }

    // SP char was not found
    // -> read more data if possible
    // -> fail with uri too long if buffer is full

    return toInputReadIfPossible(state, HttpStatus.URI_TOO_LONG);
  }

  private byte requestLineQuery() {
    // we will look for the first SP char

    for (; bufferHasIndex(bufferIndex); bufferIndex++) {
      byte b;
      b = bufferGet(bufferIndex);

      if (b == Bytes.SP) {
        query.end(bufferIndex);

        // bufferIndex immediately after the SP char

        bufferIndex = bufferIndex + 1;

        return _REQUEST_LINE_VERSION;
      }
    }

    // SP char was not found
    // -> read more data if possible
    // -> fail with uri too long if buffer is full

    return toInputReadIfPossible(state, HttpStatus.URI_TOO_LONG);
  }

  private byte requestLineTarget() {
    // we will check if the request target starts with a '/' char

    int targetStart;
    targetStart = bufferIndex;

    if (!bufferHasIndex(targetStart)) {
      return toInputRead(state);
    }

    byte b;
    b = bufferGet(targetStart);

    if (b != Bytes.SOLIDUS) {
      // first char IS NOT '/' => BAD_REQUEST
      return toRequestError(HttpStatus.BAD_REQUEST);
    }

    // mark request path start

    requestPath = new HttpRequestPath(buffer, targetStart);

    requestPath.slash(targetStart);

    // bufferIndex immediately after the '/' char

    bufferIndex = targetStart + 1;

    return _REQUEST_LINE_PATH;
  }

  private byte requestLineVersion() {
    int versionStart;
    versionStart = bufferIndex;

    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' = 8 bytes

    int versionLength;
    versionLength = 8;

    int versionEnd;
    versionEnd = versionStart + versionLength - 1;

    // versionEnd is @ CR
    // lineEnd is @ LF

    int lineEnd;
    lineEnd = versionEnd + 1;

    if (!bufferHasIndex(lineEnd)) {
      return toInputReadIfPossible(state, HttpStatus.URI_TOO_LONG);
    }

    int index;
    index = versionStart;

    if (buffer[index++] != 'H' ||
        buffer[index++] != 'T' ||
        buffer[index++] != 'T' ||
        buffer[index++] != 'P' ||
        buffer[index++] != '/') {

      // buffer does not start with 'HTTP/' => bad request

      return toRequestError(HttpStatus.BAD_REQUEST);
    }

    byte maybeMajor;
    maybeMajor = buffer[index++];

    if (!Bytes.isDigit(maybeMajor)) {
      // major version is not a digit => bad request

      return toRequestError(HttpStatus.BAD_REQUEST);
    }

    if (buffer[index++] != '.') {
      // major version not followed by a DOT => bad request

      return toRequestError(HttpStatus.BAD_REQUEST);
    }

    versionMajor = (byte) (maybeMajor - 0x30);

    byte maybeMinor;
    maybeMinor = buffer[index++];

    if (!Bytes.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request

      return toRequestError(HttpStatus.BAD_REQUEST);
    }

    versionMinor = (byte) (maybeMinor - 0x30);

    byte crOrLf;
    crOrLf = buffer[index++];

    if (crOrLf == Bytes.CR && buffer[index++] == Bytes.LF) {
      // bufferIndex resumes immediately after CRLF

      bufferIndex = index;

      return _PARSE_HEADER;
    }

    if (crOrLf == Bytes.LF) {
      // bufferIndex resumes immediately after LF

      bufferIndex = index;

      return _PARSE_HEADER;
    }

    // no line terminator after version => bad request

    return toRequestError(HttpStatus.BAD_REQUEST);
  }

  private void reset() {
    bufferIndex = bufferLimit = -1;

    method = null;

    if (requestHeaders != null) {
      requestHeaders.clear();
    }

    if (requestHeadersUnknown != null) {
      requestHeadersUnknown.clear();
    }

    requestPath = null;

    responseBody = null;

    if (responseHeaders != null) {
      responseHeaders.clear();
    }

    responseHeadersIndex = -1;

    status = null;

    versionMajor = versionMinor = -1;
  }

  private byte result() {
    if (noteSink.isEnabled(PROCESSED)) {
      SocketAddress remoteAddress;
      remoteAddress = socket.getRemoteSocketAddress();

      String target;

      if (requestPath != null) {
        target = requestPath.targetOr("-");
      } else {
        target = "-";
      }

      long processingTime;
      processingTime = System.currentTimeMillis() - startTime;

      ProcessedRecord log;
      log = new ProcessedRecord(remoteAddress, method, target, status, processingTime);

      noteSink.send(PROCESSED, log);
    }

    reset();

    if (error != null) {
      keepAlive = false;
    }

    return keepAlive ? _KEEP_ALIVE : _STOP;
  }

  private byte setup() {
    startTime = System.currentTimeMillis();

    // TODO set timeout

    bufferIndex = bufferLimit = 0;

    error = null;

    return _INPUT;
  }

  private byte toRequestError(HttpStatus error) {
    status = error;

    return _REQUEST_ERROR;
  }

  private byte toHandleOrRequestBody() {
    if (requestHeaders == null) {
      return _HANDLE;
    }

    if (requestHeaders.containsKey(StandardHeaderName.CONTENT_LENGTH)) {
      return _REQUEST_BODY;
    }

    if (requestHeaders.containsKey(StandardHeaderName.TRANSFER_ENCODING)) {
      throw new UnsupportedOperationException(
          "Implement me :: maybe chunked?"
      );
    }

    return _HANDLE;
  }

  private byte toInputRead(byte onRead) {
    nextAction = onRead;

    return _INPUT_READ;
  }

  private byte toInputReadIfPossible(byte onRead, HttpStatus onBufferFull) {
    if (bufferLimit < buffer.length) {
      return toInputRead(onRead);
    }

    if (bufferLimit == buffer.length) {
      return toRequestError(onBufferFull);
    }

    // buffer limit overflow!!!
    // programming error

    throw new UnsupportedOperationException(
        "Implement me :: Internal Server Error"
    );
  }

  private byte toRequestLineMethod(Http.Method maybe) {
    method = maybe;

    return _REQUEST_LINE_METHOD;
  }

}