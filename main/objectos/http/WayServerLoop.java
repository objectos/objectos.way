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
package objectos.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import objectos.lang.CharWritable;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableMap;
import objectos.way.Http;

public final class WayServerLoop extends WayServerRequestBody implements ServerLoop {

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

  private static final int _CONFIG = 0;
  private static final int _PARSE = 1;
  private static final int _REQUEST = 2;
  private static final int _RESPONSE = 3;
  private static final int _PROCESSED = 4;
  private static final int _COMMITED = 5;

  private static final int STATE_MASK = 0xF;
  private static final int BITS_MASK = ~STATE_MASK;

  private static final int KEEP_ALIVE = 1 << 4;

  private static final int SESSION_NEW = 1 << 5;

  private static final int CONTENT_LENGTH = 1 << 6;

  private static final int CHUNKED = 1 << 7;

  private Map<String, Object> attributes;

  private int bitset;

  private Charset charset;

  private Clock clock;

  private Object responseBody;

  private Session session;

  private SessionStore sessionStore;

  private final Socket socket;

  public WayServerLoop(Socket socket) {
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

  public final void noteSink(NoteSink noteSink) {
    checkConfig();

    super.noteSink(noteSink);
  }

  /**
   * Use the specified {@link SessionStore} for session handling.
   *
   * <p>
   * If the specified value is {@code null} then session handling is disabled.
   *
   * @param sessionStore
   *        the session store to use or {@code null} to disable session
   *        handling
   */
  public final void sessionStore(SessionStore sessionStore) {
    checkConfig();

    this.sessionStore = sessionStore;
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

  @Override
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

    WayHeader connection;
    connection = headerUnchecked(HeaderName.CONNECTION);

    if (connection != null) {
      if (connection.contentEquals(Bytes.KEEP_ALIVE)) {
        setBit(KEEP_ALIVE);
      }

      else if (connection.contentEquals(Bytes.CLOSE)) {
        clearBit(KEEP_ALIVE);
      }
    }

    // handle session
    acceptSessionStore0();

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
      attributes = new GrowableMap<>();
    }

    return attributes;
  }

  @Override
  public final void acceptSessionStore(SessionStore sessionStore) {
    checkRequest();
    this.sessionStore = Check.notNull(sessionStore, "sessionStore == null");

    acceptSessionStore0();
  }

  private void acceptSessionStore0() {
    session = null;

    clearBit(SESSION_NEW);

    if (sessionStore != null) {

      WayHeader cookie;
      cookie = headerUnchecked(HeaderName.COOKIE);

      if (cookie != null) {
        String cookieHeader;
        cookieHeader = cookie.get();

        Cookies cookies;
        cookies = Cookies.parse(cookieHeader);

        session = sessionStore.get(cookies);
      }

      if (session == null) {
        session = sessionStore.nextSession();

        setBit(SESSION_NEW);
      }

    }
  }

  private boolean badRequest() {
    Check.state(testState(_REQUEST), "Method can only be invoked after a parse() operation");

    return parseStatus.isBadRequest();
  }

  // request

  @Override
  public final Method method() {
    checkRequest();

    return method;
  }

  @Override
  public final UriPath path() {
    checkRequest();

    return path;
  }

  @Override
  public final UriQuery query() {
    checkRequest();

    return query;
  }

  @Override
  public final ServerRequestHeaders headers() {
    checkRequest();

    return this;
  }

  @Override
  public final Body body() {
    checkRequest();

    return this;
  }

  @Override
  public final Session session() {
    checkRequest();

    return session;
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
    size = WayStatus.size();

    byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      WayStatus status;
      status = WayStatus.get(index);

      String response;
      response = Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      map[index] = Bytes.utf8(response);
    }

    STATUS_LINES = map;
  }

  @Override
  public final void methodMatrix(Method method, Handler handler) {
    Check.notNull(method, "method == null");
    Check.notNull(handler, "handler == null");

    Method actual;
    actual = method();

    if (handles(method, actual)) {
      handler.handle(this);
    } else {
      methodNotAllowed();
    }
  }

  private boolean handles(Method method, Method actual) {
    if (method.is(Method.GET)) {
      return actual.is(Method.GET, Method.HEAD);
    } else {
      return actual.is(method);
    }
  }

  @Override
  public final void status(Status status) {
    checkResponse();

    Version version;
    version = Version.HTTP_1_1;

    writeBytes(version.responseBytes);

    WayStatus internal;
    internal = (WayStatus) status;

    byte[] statusBytes;
    statusBytes = STATUS_LINES[internal.index];

    writeBytes(statusBytes);
  }

  @Override
  public final void header(HeaderName name, long value) {
    checkResponse();
    Check.notNull(name, "name == null");

    header0(name, Long.toString(value));
  }

  @Override
  public final void header(HeaderName name, String value) {
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

    header0(HeaderName.DATE, value);
  }

  private void header0(HeaderName name, String value) { // write our the name
    int index;
    index = name.index();

    byte[] nameBytes;

    if (index >= 0) {
      nameBytes = WayServerRequestHeaders.STD_HEADER_NAME_BYTES[index];
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
    if (name == HeaderName.CONNECTION && value.equalsIgnoreCase("close")) {
      clearBit(KEEP_ALIVE);
    }

    else if (name == HeaderName.CONTENT_LENGTH) {
      setBit(CONTENT_LENGTH);
    }

    else if (name == HeaderName.TRANSFER_ENCODING && value.toLowerCase().contains("chunked")) {
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
  public final void send(CharWritable body, Charset charset) {
    checkResponse();

    responseBody = Check.notNull(body, "body == null");
    this.charset = Check.notNull(charset, "charset == null");

    setState(_PROCESSED);
  }

  @Override
  public final void send(Path file) {
    checkResponse();

    responseBody = Check.notNull(file, "file == null");

    setState(_PROCESSED);
  }

  // 404 NOT FOUND

  @Override
  public final void notFound() {
    status(Status.NOT_FOUND);

    dateNow();

    header0(HeaderName.CONNECTION, "close");

    send();
  }

  // 405 METHOD NOT ALLOWED

  @Override
  public final void methodNotAllowed() {
    status(Status.METHOD_NOT_ALLOWED);

    dateNow();

    header0(HeaderName.CONNECTION, "close");

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

    status(Status.INTERNAL_SERVER_ERROR);

    dateNow();

    header(HeaderName.CONTENT_LENGTH, bytes.length);

    header(HeaderName.CONTENT_TYPE, "text/plain");

    header(HeaderName.CONNECTION, "close");

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

  @Override
  public final void commit() throws IOException, IllegalStateException {
    Check.state(testState(_PROCESSED), "Cannot commit as we are not in the processed phase");

    if (testBit(SESSION_NEW)) {
      String id;
      id = session.id();

      String setCookie;
      setCookie = sessionStore.setCookie(id);

      header0(HeaderName.SET_COOKIE, setCookie);
    }

    writeBytes(Bytes.CRLF);

    OutputStream outputStream;
    outputStream = socket.getOutputStream();

    outputStream.write(buffer, 0, bufferIndex);

    if (method != Method.HEAD) {
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

        case Path file -> {
          try (InputStream in = Files.newInputStream(file)) {
            in.transferTo(outputStream);
          }
        }

        default -> throw new UnsupportedOperationException("Implement me");
      }
    }

    setState(_COMMITED);
  }

  @Override
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