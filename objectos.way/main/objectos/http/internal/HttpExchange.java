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
package objectos.http.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import objectos.http.Http;
import objectos.http.Http.Method;
import objectos.http.server.Exchange;
import objectos.http.server.Handler;
import objectos.http.server.Request;
import objectos.http.server.Response;
import objectos.http.server.Segments;
import objectos.lang.Check;
import objectos.lang.Note1;
import objectos.lang.NoteSink;
import objectos.util.GrowableList;

public final class HttpExchange implements Exchange, Runnable, objectos.http.HttpExchange {

  public static final Note1<IOException> EIO_READ_ERROR = Note1.error();

  // Setup phase

  static final byte _SETUP = 1;

  // Input phase

  static final byte _INPUT = 2;
  static final byte _INPUT_READ = 3;

  // Input / Request Line phase

  static final byte _REQUEST_LINE = 4;
  static final byte _REQUEST_LINE_METHOD = 5;
  static final byte _REQUEST_LINE_METHOD_P = 6;
  static final byte _REQUEST_LINE_TARGET = 7;
  static final byte _REQUEST_LINE_PATH = 8;
  static final byte _REQUEST_LINE_VERSION = 9;

  // Input / Parse header phase

  static final byte _PARSE_HEADER = 10;
  static final byte _PARSE_HEADER_NAME = 11;
  static final byte _PARSE_HEADER_NAME_CASE_INSENSITIVE = 12;
  static final byte _PARSE_HEADER_VALUE = 13;

  // Input / Request Body

  static final byte _REQUEST_BODY = 14;

  // Handle phase

  static final byte _HANDLE = 15;
  static final byte _HANDLE_INVOKE = 16;

  // Output phase

  static final byte _OUTPUT = 17;
  static final byte _OUTPUT_BODY = 18;
  static final byte _OUTPUT_BUFFER = 19;
  static final byte _OUTPUT_HEADER = 20;
  static final byte _OUTPUT_TERMINATOR = 21;
  static final byte _OUTPUT_STATUS = 22;
  static final byte _CLIENT_ERROR = 23;

  // Result phase

  static final byte _RESULT = 24;
  static final byte _RESULT_CLOSE = 25;
  static final byte _RESULT_ERROR_WRITE = 26;

  static final byte _STOP = 100;

  byte[] buffer;

  int bufferIndex;

  int bufferLimit;

  Throwable error;

  Supplier<Handler> handlerSupplier;

  boolean keepAlive;

  HttpMethod method;

  byte nextAction;

  NoteSink noteSink;

  Request request;

  HttpRequestBody requestBody;

  HeaderName requestHeaderName;

  Map<HeaderName, HeaderValue> requestHeaders;

  HttpRequestTarget requestTarget;

  int requestTargetStart;

  private HttpResponse response;

  Object responseBody;

  List<HttpResponseHeader> responseHeaders;

  int responseHeadersIndex;

  Segments segments;

  Socket socket;

  byte state;

  Http.Status status;

  byte versionMajor;

  byte versionMinor;

  public HttpExchange(int bufferSize,
                      Supplier<Handler> handlerSupplier,
                      NoteSink noteSink,
                      Socket socket) {
    // there's a small chance we won't use the buffer
    // but, as it is used in many places in this class, we create it eagerly
    this.buffer = new byte[bufferSize];

    this.handlerSupplier = handlerSupplier;

    this.noteSink = noteSink;

    this.socket = socket;

    // keepAlive() must return true on the first call
    keepAlive = true;

    state = _SETUP;
  }

  /**
   * For testing purposes only.
   */
  HttpExchange() {}

  @Override
  public final void close() throws IOException {
    socket.close();
  }

  @Override
  public final boolean keepAlive() {
    return keepAlive;
  }

  @Override
  public final void executeRequestPhase() throws IOException {
    if (state != _SETUP) {
      // invalid initial state
      return;
    }

    while (state < _HANDLE_INVOKE) {
      stepOne();
    }

    throwErrorIfNecessary();
  }

  private void throwErrorIfNecessary() throws IOException {
    if (error == null) {
      return;
    }

    if (error instanceof Error e) {
      throw e;
    }

    if (error instanceof RuntimeException e) {
      throw e;
    }

    if (error instanceof IOException e) {
      throw e;
    }

    throw new IOException(error);
  }

  @Override
  public final Method method() {
    checkStateHandle();

    return method;
  }

  @Override
  public final Segments segments() {
    checkStateHandle();

    return segments;
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
  public final void header(Http.Header.Name name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    checkStateHandle();

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
  public final void executeResponsePhase() throws IOException {
    checkStateHandle();

    method = null;

    if (requestHeaders != null) {
      requestHeaders.clear();
    }

    requestTarget = null;

    segments = null;

    state = _OUTPUT;

    while (state < _RESULT) {
      stepOne();
    }

    throwErrorIfNecessary();
  }

  private void checkStateHandle() {
    if (state != _HANDLE_INVOKE) {
      throw new IllegalStateException(
        "Request has not been parsed yet or response has already been sent."
      );
    }
  }

  @Override
  public final Request request() {
    // TODO check state

    if (request == null) {
      request = new HttpRequest(this);
    }

    return request;
  }

  @Override
  public final Response response() {
    // TODO check state

    if (response == null) {
      response = new HttpResponse(this);
    }

    return response;
  }

  @Override
  public final void run() {
    while (isActive()) {
      stepOne();
    }
  }

  final boolean isActive() {
    return state != _STOP;
  }

  final void stepOne() {
    state = switch (state) {
      // Setup phase

      case _SETUP -> setup();

      // Input phase

      case _INPUT -> input();
      case _INPUT_READ -> inputRead();

      // Input / Request Line phase

      case _REQUEST_LINE -> requestLine();
      case _REQUEST_LINE_METHOD -> requestLineMethod();
      case _REQUEST_LINE_METHOD_P -> requestLineMethodP();
      case _REQUEST_LINE_TARGET -> requestLineTarget();
      case _REQUEST_LINE_PATH -> requestLinePath();
      case _REQUEST_LINE_VERSION -> requestLineVersion();

      // Input / Parse Header phase

      case _PARSE_HEADER -> parseHeader();
      case _PARSE_HEADER_NAME -> parseHeaderName();
      case _PARSE_HEADER_VALUE -> parseHeaderValue();

      // Input / Request Body

      case _REQUEST_BODY -> requestBody();

      // Handle phase

      case _HANDLE -> handle();
      case _HANDLE_INVOKE -> handleInvoke();

      // Output phase

      case _OUTPUT -> output();
      case _OUTPUT_BODY -> outputBody();
      case _OUTPUT_BUFFER -> outputBuffer();
      case _OUTPUT_HEADER -> outputHeader();
      case _OUTPUT_TERMINATOR -> outputTerminator();
      case _OUTPUT_STATUS -> outputStatus();

      // Result phase

      case _RESULT -> result();
      case _RESULT_CLOSE -> resultClose();

      case _CLIENT_ERROR -> {
        throw new UnsupportedOperationException(
          "status=" + status, error
        );
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
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

    responseBody = null;

    if (responseHeaders == null) {
      responseHeaders = new GrowableList<>();
    } else {
      responseHeaders.clear();
    }

    return _HANDLE_INVOKE;
  }

  private boolean handle0KeepAlive() {
    HeaderValue connection;
    connection = requestHeaders.getOrDefault(HeaderName.CONNECTION, HeaderValue.EMPTY);

    if (connection.contentEquals(Bytes.KEEP_ALIVE)) {
      return true;
    }

    if (connection.contentEquals(Bytes.CLOSE)) {
      return false;
    }

    return versionMajor == 1 && versionMinor >= 1;
  }

  private byte handleInvoke() {
    try {
      Handler handler;
      handler = handlerSupplier.get();

      handler.handle(this);

      return _OUTPUT;
    } catch (Throwable t) {
      error = t;

      return toClientError(HttpStatus.INTERNAL_SERVER_ERROR);
    } finally {
      // TODO log handle phase

      method = null;

      if (requestHeaders != null) {
        requestHeaders.clear();
      }

      requestTarget = null;

      segments = null;
    }
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
      return toInputReadError(e);
    }

    int writableLength;
    writableLength = buffer.length - bufferLimit;

    int bytesRead;

    try {
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);
    } catch (IOException e) {
      return toInputReadError(e);
    }

    if (bytesRead < 0) {
      return _RESULT_CLOSE;
    }

    bufferLimit += bytesRead;

    return nextAction;
  }

  private byte output() {
    bufferIndex = bufferLimit = responseHeadersIndex = 0;

    return _OUTPUT_STATUS;
  }

  private byte outputBody() {
    try {
      return outputBody0();
    } catch (IOException e) {
      error = e;

      return _RESULT_ERROR_WRITE;
    } finally {
      bufferIndex = bufferLimit = -1;

      responseBody = null;

      if (responseHeaders != null) {
        responseHeaders.clear();
      }

      responseHeadersIndex = -1;

      status = null;

      versionMajor = versionMinor = -1;
    }
  }

  private byte outputBody0() throws IOException {
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
  }

  private byte outputBuffer() {
    try {
      OutputStream outputStream;
      outputStream = socket.getOutputStream();

      outputStream.write(buffer, 0, bufferLimit);

      bufferLimit = 0;

      return nextAction;
    } catch (IOException e) {
      error = e;

      return _RESULT_ERROR_WRITE;
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
      statusBytes = internal.responseBytes;
    } else {
      statusBytes = HttpStatus.responseBytes(status);
    }

    requiredLength += statusBytes.length;

    if (buffer.length < requiredLength) {
      // we could send the response unbuffered.
      // Instead this should be considered a bug in the library

      // TODO log irrecoverable error

      return _RESULT_CLOSE;
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
        HeaderName.ACCEPT_ENCODING
      );

      case 'C' -> parseHeaderName0(nameStart,
        HeaderName.CONNECTION,
        HeaderName.CONTENT_LENGTH,
        HeaderName.CONTENT_TYPE
      );

      case 'D' -> parseHeaderName0(nameStart,
        HeaderName.DATE
      );

      case 'H' -> parseHeaderName0(nameStart,
        HeaderName.HOST
      );

      case 'T' -> parseHeaderName0(nameStart,
        HeaderName.TRANSFER_ENCODING
      );

      case 'U' -> parseHeaderName0(nameStart,
        HeaderName.USER_AGENT
      );

      default -> _PARSE_HEADER_VALUE;
    };
  }

  private byte parseHeaderName0(int nameStart, HeaderName candidate) {
    final byte[] candidateBytes;
    candidateBytes = candidate.bytes;

    if (bufferEquals(candidateBytes, nameStart)) {
      requestHeaderName = candidate;
    }

    return _PARSE_HEADER_VALUE;
  }

  private byte parseHeaderName0(int nameStart, HeaderName c0, HeaderName c1, HeaderName c2) {
    byte result;
    result = parseHeaderName0(nameStart, c0);

    if (requestHeaderName != null) {
      return result;
    }

    result = parseHeaderName0(nameStart, c1);

    if (requestHeaderName != null) {
      return result;
    }

    return parseHeaderName0(nameStart, c2);
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

    if (requestHeaderName != null) {
      HeaderValue headerValue;
      headerValue = new HeaderValue(buffer, valueStart, valueEnd);

      if (requestHeaders == null) {
        requestHeaders = new EnumMap<>(HeaderName.class);
      }

      HeaderValue previousValue;
      previousValue = requestHeaders.put(requestHeaderName, headerValue);

      if (previousValue != null) {
        throw new UnsupportedOperationException("Implement me");
      }

      // reset header name just in case

      requestHeaderName = null;
    }

    // we have found the value.
    // bufferIndex should point to the position immediately after the LF char

    bufferIndex = lfIndex + 1;

    return _PARSE_HEADER;
  }

  private byte requestBody() {
    // reset our state

    requestBody = null;

    // Let's check if this is a fixed length or a chunked transfer

    HeaderValue contentLength;
    contentLength = requestHeaders.get(HeaderName.CONTENT_LENGTH);

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
      return toClientError(HttpStatus.BAD_REQUEST);
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
      case 'C' -> toRequestLineMethod(HttpMethod.CONNECT);

      case 'D' -> toRequestLineMethod(HttpMethod.DELETE);

      case 'G' -> toRequestLineMethod(HttpMethod.GET);

      case 'H' -> toRequestLineMethod(HttpMethod.HEAD);

      case 'O' -> toRequestLineMethod(HttpMethod.OPTIONS);

      case 'P' -> _REQUEST_LINE_METHOD_P;

      case 'T' -> toRequestLineMethod(HttpMethod.TRACE);

      // first char does not match any candidate
      // we are sure this is a bad request

      default -> toClientError(HttpStatus.BAD_REQUEST);
    };
  }

  private byte requestLineMethod() {
    // method candidate @ start of the buffer

    int candidateStart;
    candidateStart = bufferIndex;

    // we'll check if the buffer contents matches 'METHOD SP'

    byte[] candidateBytes;
    candidateBytes = method.nameAndSpace;

    int requiredIndex;
    requiredIndex = candidateStart + candidateBytes.length;

    if (!bufferHasIndex(requiredIndex)) {
      // we don't have enough bytes in the buffer...
      // assuming the client is slow on sending data

      // clear method candidate just in case...
      method = null;

      return toInputRead(state);
    }

    if (!bufferEquals(candidateBytes, candidateStart)) {
      // we have enough bytes and they don't match our 'method name + SP'
      // respond with bad request

      // clear method candidate just in case...
      method = null;

      return toClientError(HttpStatus.BAD_REQUEST);
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
      case 'A' -> toRequestLineMethod(HttpMethod.PATCH);

      case 'O' -> toRequestLineMethod(HttpMethod.POST);

      case 'U' -> toRequestLineMethod(HttpMethod.PUT);

      // it does not match any candidate
      // we are sure this is a bad request

      default -> toClientError(HttpStatus.BAD_REQUEST);
    };
  }

  private byte requestLinePath() {
    // we will look for the first SP char

    int pathStart;
    pathStart = bufferIndex;

    for (int index = pathStart; bufferHasIndex(index); index++) {
      byte b;
      b = bufferGet(index);

      switch (b) {
        case Bytes.SOLIDUS -> requestLinePathSegment(index);

        case Bytes.SP -> {

          // SP found, store the indices

          requestTarget = new HttpRequestTarget(buffer, requestTargetStart, index);

          requestLinePathSegment(index);

          return _REQUEST_LINE_VERSION;
        }
      }
    }

    // SP char was not found.
    // Read more data if possible

    return toInputReadIfPossible(state, HttpStatus.URI_TOO_LONG);
  }

  private void requestLinePathSegment(int index) {
    String value;
    value = new String(buffer, bufferIndex, index - bufferIndex, StandardCharsets.UTF_8);

    if (segments == null) {
      segments = new Segments.Segments1(value);
    } else {
      segments = segments.append(value);
    }

    // bufferIndex immediately after the last read char

    bufferIndex = index + 1;
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
      return toClientError(HttpStatus.BAD_REQUEST);
    }

    requestTargetStart = targetStart;

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

      return toClientError(HttpStatus.BAD_REQUEST);
    }

    byte maybeMajor;
    maybeMajor = buffer[index++];

    if (!Bytes.isDigit(maybeMajor)) {
      // major version is not a digit => bad request

      return toClientError(HttpStatus.BAD_REQUEST);
    }

    if (buffer[index++] != '.') {
      // major version not followed by a DOT => bad request

      return toClientError(HttpStatus.BAD_REQUEST);
    }

    versionMajor = (byte) (maybeMajor - 0x30);

    byte maybeMinor;
    maybeMinor = buffer[index++];

    if (!Bytes.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request

      return toClientError(HttpStatus.BAD_REQUEST);
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

    return toClientError(HttpStatus.BAD_REQUEST);
  }

  private byte result() {
    if (keepAlive) {
      return _SETUP;
    }

    return resultClose();
  }

  private byte resultClose() {
    try {
      socket.close();
    } catch (IOException e) {
      throw new UnsupportedOperationException(
        "We should log this"
      );
    }

    return _STOP;
  }

  private byte setup() {
    // TODO set timeout

    // we ensure the buffer is reset

    bufferIndex = bufferLimit = 0;

    return _INPUT;
  }

  private byte toClientError(HttpStatus error) {
    status = error;

    return _CLIENT_ERROR;
  }

  private byte toHandleOrRequestBody() {
    if (requestHeaders == null) {
      return _HANDLE;
    }

    if (requestHeaders.containsKey(HeaderName.CONTENT_LENGTH)) {
      return _REQUEST_BODY;
    }

    if (requestHeaders.containsKey(HeaderName.TRANSFER_ENCODING)) {
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

  private byte toInputReadError(IOException e) {
    error = e;

    noteSink.send(EIO_READ_ERROR, e);

    return _RESULT_CLOSE;
  }

  private byte toInputReadIfPossible(byte onRead, HttpStatus onBufferFull) {
    if (bufferLimit < buffer.length) {
      return toInputRead(onRead);
    }

    if (bufferLimit == buffer.length) {
      return toClientError(onBufferFull);
    }

    // buffer limit overflow!!!
    // programming error

    throw new UnsupportedOperationException(
      "Implement me :: Internal Server Error"
    );
  }

  private byte toRequestLineMethod(HttpMethod maybe) {
    method = maybe;

    return _REQUEST_LINE_METHOD;
  }

}