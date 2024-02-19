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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Arrays;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;

public final class WayServerLoop extends WayServerRequestBody implements ServerLoop {

  private static final int _CONFIG = 0;
  private static final int _PARSE = 1;
  private static final int _REQUEST = 2;
  private static final int _RESPONSE = 3;
  private static final int _COMMITED = 4;

  private static final int STATE_MASK = 0xF;
  private static final int BITS_MASK = ~STATE_MASK;

  private static final int KEEP_ALIVE = 1 << 4;

  private static final int SESSION_NEW = 1 << 5;

  private int bitset;

  private Clock clock;

  @SuppressWarnings("unused")
  private NoteSink noteSink = NoOpNoteSink.of();

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

    this.noteSink = Check.notNull(noteSink, "noteSink == null");
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
    socket.close();
  }

  @Override
  public final void parse() throws IOException, IllegalStateException {
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

    if (badRequest != null) {
      return;
    }

    // request headers

    parseHeaders();

    if (badRequest != null) {
      return;
    }

    // request body

    parseRequestBody();

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

    setState(_REQUEST);
  }

  @Override
  public final boolean badRequest() {
    Check.state(testState(_REQUEST), "Method can only be invoked after a parse() operation");

    return badRequest != null;
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
  }

  @Override
  public final void send() {
    checkResponse();

    send0(NoResponseBody.INSTANCE);
  }

  @Override
  public final void send(byte[] body) {
    checkResponse();
    Check.notNull(body, "body == null");

    send0(body);
  }

  @Override
  public final void send(Path file) {
    checkResponse();
    Check.notNull(file, "file == null");

    send0(file);
  }

  private void send0(Object body) {
    responseBody = body;
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
    requiredIndex = bufferIndex + length;

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

  @Override
  public final void commit() throws IOException, IllegalStateException {
    Check.state(testState(_RESPONSE), "Cannot commit as we are not in the response phase");
    Check.state(responseBody != null, "Cannot commit: missing ServerExchange::send method invocation");

    if (testBit(SESSION_NEW)) {
      String id;
      id = session.id();

      String setCookie;
      setCookie = sessionStore.setCookie(id);

      header0(HeaderName.SET_COOKIE, setCookie);
    }

    OutputStream outputStream;
    outputStream = socket.getOutputStream();

    // send headers
    writeBytes(Bytes.CRLF);

    outputStream.write(buffer, 0, bufferIndex);

    if (method != Method.HEAD) {
      switch (responseBody) {
        case NoResponseBody no -> {}

        case byte[] bytes -> outputStream.write(bytes, 0, bytes.length);

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

}