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
package objectos.way;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import objectos.notes.NoteSink;
import objectos.way.Lang.CharWritable;

final class HttpExchangeLoop extends HttpRequestBody implements Http.Exchange, Closeable {

  public enum ParseStatus {
    // keep going
    NORMAL,

    // SocketInput statuses
    EOF,

    UNEXPECTED_EOF,

    OVERFLOW,

    // 400 bad request
    INVALID_METHOD,

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
      return compareTo(INVALID_METHOD) >= 0;
    }
  }

  enum NoResponseBody {

    INSTANCE;

  }

  private static final int _CONFIG = 0;
  private static final int _PARSE = 1;
  private static final int _REQUEST = 2;
  private static final int _RESPONSE = 3;
  private static final int _PROCESSED = 4;
  private static final int _COMMITED = 5;

  private static final int STATE_MASK = 0xF;
  private static final int BITS_MASK = ~STATE_MASK;

  private static final int KEEP_ALIVE = 1 << 4;

  private static final int CONTENT_LENGTH = 1 << 5;

  private static final int CHUNKED = 1 << 6;

  private Map<String, Object> attributes;

  private int bitset;

  private Charset charset;

  private Clock clock;

  private Object responseBody;

  private final Socket socket;

  public HttpExchangeLoop(Socket socket) {
    this.socket = socket;

    setState(_CONFIG);
  }

  @Override
  public final void bufferSize(int initial, int max) {
    checkConfig();

    Check.argument(initial >= 128, "initial size must be >= 128");
    Check.argument(max >= 128, "max size must be >= 128");
    Check.argument(max >= initial, "max size must be >= initial size");

    super.bufferSize(initial, max);
  }

  public final void clock(Clock clock) {
    checkConfig();

    this.clock = Check.notNull(clock, "clock == null");
  }

  @Override
  public final void noteSink(NoteSink noteSink) {
    checkConfig();

    super.noteSink(noteSink);
  }

  private void checkConfig() {
    Check.state(testState(_CONFIG), "This configuration method cannot be called at this moment");
  }

  @Override
  public final void close() throws IOException {
    try {
      super.close();
    } finally {
      socket.close();
    }
  }

  private static final byte[] CLOSE_BYTES = Http.utf8("close");

  private static final byte[] KEEP_ALIVE_BYTES = Http.utf8("keep-alive");

  public final ParseStatus parse() throws IOException, IllegalStateException {
    if (testState(_CONFIG)) {
      // init socket input
      InputStream inputStream;
      inputStream = socket.getInputStream();

      initSocketInput(inputStream);
    }

    else if (testState(_COMMITED)) {
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

    // force bits reset
    bitset = _PARSE;

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

    // handle keep alive

    clearBit(KEEP_ALIVE);

    if (versionMajor == 1 && versionMinor == 1) {
      setBit(KEEP_ALIVE);
    }

    HttpHeader connection;
    connection = headerUnchecked(Http.CONNECTION);

    if (connection != null) {
      if (connection.contentEquals(KEEP_ALIVE_BYTES)) {
        setBit(KEEP_ALIVE);
      }

      else if (connection.contentEquals(CLOSE_BYTES)) {
        clearBit(KEEP_ALIVE);
      }
    }

    setState(_REQUEST);

    return parseStatus;
  }

  private void resetServerLoop() {
    if (attributes != null) {
      attributes.clear();
    }
  }

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

  private boolean badRequest() {
    Check.state(testState(_REQUEST), "Http.Request.Method can only be invoked after a parse() operation");

    return parseStatus.isBadRequest();
  }

  // request

  @Override
  public final byte method() {
    checkRequest();

    return method;
  }

  @Override
  public final Http.Request.Headers headers() {
    checkRequest();

    return this;
  }

  @Override
  public final Http.Request.Body body() {
    checkRequest();

    return this;
  }

  private void checkRequest() {
    Check.state(
        !badRequest(),

        """
        Request methods can only be invoked:
        - after a successful parse() operation; and
        - before the toResponse() method invocation.
        """
    );
  }

  // response

  static final byte[][] STATUS_LINES;

  static {
    int size;
    size = HttpResponseStatus.size();

    byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      HttpResponseStatus status;
      status = HttpResponseStatus.get(index);

      String response;
      response = Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      map[index] = Http.utf8(response);
    }

    STATUS_LINES = map;
  }

  @Override
  public final void status(Http.Response.Status status) {
    checkResponse();

    Http.Version version;
    version = Http.Version.HTTP_1_1;

    writeBytes(version.responseBytes);

    HttpResponseStatus internal;
    internal = (HttpResponseStatus) status;

    byte[] statusBytes;
    statusBytes = STATUS_LINES[internal.index];

    writeBytes(statusBytes);
  }

  @Override
  public final void header(Http.HeaderName name, long value) {
    checkResponse();
    Check.notNull(name, "name == null");

    header0(name, Long.toString(value));
  }

  @Override
  public final void header(Http.HeaderName name, String value) {
    checkResponse();
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    header0(name, value);
  }

  @Override
  public final void dateNow() {
    checkResponse();

    Clock theClock;
    theClock = clock;

    if (theClock == null) {
      theClock = Clock.systemUTC();
    }

    ZonedDateTime now;
    now = ZonedDateTime.now(theClock);

    String value;
    value = Http.formatDate(now);

    header0(Http.DATE, value);
  }

  private void header0(Http.HeaderName name, String value) { // write our the name
    int index;
    index = name.index();

    byte[] nameBytes;

    if (index >= 0) {
      nameBytes = HttpRequestHeaders.STD_HEADER_NAME_BYTES[index];
    } else {
      String capitalized;
      capitalized = name.capitalized();

      nameBytes = capitalized.getBytes(StandardCharsets.UTF_8);
    }

    writeBytes(nameBytes);

    // write out the separator
    writeBytes(Bytes.COLONSP);

    // write out the value
    byte[] valueBytes;
    valueBytes = value.getBytes(StandardCharsets.UTF_8);

    writeBytes(valueBytes);

    writeBytes(Bytes.CRLF);

    // handle connection: close if necessary
    if (name == Http.CONNECTION && value.equalsIgnoreCase("close")) {
      clearBit(KEEP_ALIVE);
    }

    else if (name == Http.CONTENT_LENGTH) {
      setBit(CONTENT_LENGTH);
    }

    else if (name == Http.TRANSFER_ENCODING && value.toLowerCase().contains("chunked")) {
      setBit(CHUNKED);
    }
  }

  @Override
  public final void send() {
    checkResponse();

    responseBody = NoResponseBody.INSTANCE;

    setState(_PROCESSED);
  }

  @Override
  public final void send(byte[] body) {
    checkResponse();

    responseBody = Check.notNull(body, "body == null");

    setState(_PROCESSED);
  }

  @Override
  public final void send(Lang.CharWritable body, Charset charset) {
    checkResponse();

    responseBody = Check.notNull(body, "body == null");
    this.charset = Check.notNull(charset, "charset == null");

    setState(_PROCESSED);
  }

  @Override
  public final void send(java.nio.file.Path file) {
    checkResponse();

    responseBody = Check.notNull(file, "file == null");

    setState(_PROCESSED);
  }

  // 404 NOT FOUND

  @Override
  public final void notFound() {
    status(Http.NOT_FOUND);

    dateNow();

    header0(Http.CONNECTION, "close");

    send();
  }

  // 405 METHOD NOT ALLOWED

  @Override
  public final void methodNotAllowed() {
    status(Http.METHOD_NOT_ALLOWED);

    dateNow();

    header0(Http.CONNECTION, "close");

    send();
  }

  // 500 INTERNAL SERVER ERROR

  @Override
  public final void internalServerError(Throwable t) {
    StringWriter sw;
    sw = new StringWriter();

    PrintWriter pw;
    pw = new PrintWriter(sw);

    t.printStackTrace(pw);

    String msg;
    msg = sw.toString();

    byte[] bytes;
    bytes = msg.getBytes();

    status(Http.INTERNAL_SERVER_ERROR);

    dateNow();

    header(Http.CONTENT_LENGTH, bytes.length);

    header(Http.CONTENT_TYPE, "text/plain");

    header(Http.CONNECTION, "close");

    send(bytes);
  }

  private void checkResponse() {
    if (testState(_REQUEST)) {
      bufferIndex = 0;

      responseBody = null;

      setState(_RESPONSE);

      return;
    }

    if (testState(_RESPONSE)) {
      return;
    }

    throw new IllegalStateException(
        """
        Request methods can only be invoked:
        - after a successful parse() operation; and
        - before the commit() method invocation.
        """
    );
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

  private static final byte[] CHUNKED_TRAILER = "0\r\n\r\n".getBytes(StandardCharsets.UTF_8);

  public final void commit() throws IOException, IllegalStateException {
    Check.state(testState(_PROCESSED), "Cannot commit as we are not in the processed phase");

    writeBytes(Bytes.CRLF);

    OutputStream outputStream;
    outputStream = socket.getOutputStream();

    outputStream.write(buffer, 0, bufferIndex);

    if (method != Http.HEAD) {
      switch (responseBody) {
        case NoResponseBody no -> {}

        case byte[] bytes -> outputStream.write(bytes, 0, bytes.length);

        case CharWritable writable -> {
          if (testBit(CONTENT_LENGTH)) {
            throw new IllegalStateException(
                "Content-Length must not be set with a CharWritable body"
            );
          }

          if (!testBit(CHUNKED)) {
            throw new IllegalStateException(
                "Transfer-Encoding: chunked must be set with a CharWritable body"
            );
          }

          bufferIndex = 0;

          ThisAppendable out;
          out = new ThisAppendable();

          writable.writeTo(out);

          out.flush();

          outputStream.write(CHUNKED_TRAILER);
        }

        case java.nio.file.Path file -> {
          try (InputStream in = Files.newInputStream(file)) {
            in.transferTo(outputStream);
          }
        }

        default -> throw new UnsupportedOperationException("Implement me");
      }
    }

    setState(_COMMITED);
  }

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

  private class ThisAppendable implements Appendable {
    public ThisAppendable() {
      bufferIndex = 0;
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
      OutputStream outputStream;
      outputStream = socket.getOutputStream();

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

}