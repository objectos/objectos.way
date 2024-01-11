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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.http.server.Body;
import objectos.http.server.ServerLoop;
import objectos.http.server.ServerRequestHeaders;
import objectos.http.server.UriPath;
import objectos.http.server.UriQuery;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectox.http.ObjectoxStatus;

public final class ObjectoxServerLoop extends SocketInput implements ServerLoop {

  // new states

  private static final byte _CONFIG = 0;
  private static final byte _PARSE = 1;
  private static final byte _REQUEST = 2;
  private static final byte _RESPONSE = 3;
  private static final byte _COMMITED = 4;

  // new fields / constructors

  private boolean keepAlive;

  @SuppressWarnings("unused")
  private NoteSink noteSink = NoOpNoteSink.of();

  private Body requestBody;

  private ObjectoxServerRequestHeaders requestHeaders;

  private ObjectoxRequestLine requestLine;

  private Object responseBody;

  private Socket socket;

  private byte state;

  public ObjectoxServerLoop(Socket socket, boolean newApi) {
    this.socket = socket;

    state = _CONFIG;
  }

  /**
   * For testing purposes only.
   */
  ObjectoxServerLoop() {}

  // config

  @Override
  public final void bufferSize(int initial, int max) {
    checkConfig();

    super.bufferSize(initial, max);
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
  public final void close() throws IOException {
    socket.close();
  }

  @Override
  public final void parse() throws IOException, IllegalStateException {
    if (state == _CONFIG) {
      // init socket input
      InputStream inputStream;
      inputStream = socket.getInputStream();

      initSocketInput(inputStream);

      // lazily create request line
      requestLine = new ObjectoxRequestLine(this);
    }

    else if (state == _COMMITED) {
      resetSocketInput();

      requestLine.reset();
    }

    else {
      throw new IllegalStateException();
    }

    state = _PARSE;

    // request line

    requestLine.parse();

    if (requestLine.badRequest != null) {
      return;
    }

    // request headers

    if (requestHeaders == null) {
      requestHeaders = new ObjectoxServerRequestHeaders(this);
    } else {
      requestHeaders.reset();
    }

    requestHeaders.parse();

    if (requestHeaders.badRequest != null) {
      return;
    }

    // request body

    Body body;
    body = NoServerRequestBody.INSTANCE;

    if (requestHeaders.containsUnchecked(HeaderName.CONTENT_LENGTH)) {
      throw new UnsupportedOperationException(
          "Implement me :: parse body"
      );
    }

    if (requestHeaders.containsUnchecked(HeaderName.TRANSFER_ENCODING)) {
      throw new UnsupportedOperationException(
          "Implement me :: maybe chunked?"
      );
    }

    requestBody = body;

    // handle keep alive

    keepAlive = false;

    if (requestLine.versionMajor == 1 && requestLine.versionMinor == 1) {
      keepAlive = true;
    }

    ObjectoxHeader connection;
    connection = requestHeaders.getUnchecked(HeaderName.CONNECTION);

    if (connection != null) {
      if (connection.contentEquals(Bytes.KEEP_ALIVE)) {
        keepAlive = true;
      }

      else if (connection.contentEquals(Bytes.CLOSE)) {
        keepAlive = false;
      }
    }

    state = _REQUEST;
  }

  @Override
  public final boolean badRequest() {
    Check.state(state == _REQUEST, "Method can only be invoked after a parse() operation");

    return requestLine.badRequest != null
        || requestHeaders.badRequest != null;
  }

  // request

  @Override
  public final objectos.http.Method method() {
    checkRequest();

    return requestLine.method;
  }

  @Override
  public final UriPath path() {
    checkRequest();

    return requestLine.path;
  }

  @Override
  public final UriQuery query() {
    checkRequest();

    return requestLine.query;
  }

  @Override
  public final ServerRequestHeaders headers() {
    checkRequest();

    return requestHeaders;
  }

  @Override
  public final Body body() {
    checkRequest();

    return requestBody;
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
    size = ObjectoxStatus.size();

    byte[][] map;
    map = new byte[size][];

    for (int index = 0; index < size; index++) {
      ObjectoxStatus status;
      status = ObjectoxStatus.get(index);

      String response;
      response = Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      map[index] = Bytes.utf8(response);
    }

    STATUS_LINES = map;
  }

  @Override
  public final void status(Status status) {
    checkResponse();

    Version version;
    version = Version.HTTP_1_1;

    writeBytes(version.responseBytes);

    ObjectoxStatus internal;
    internal = (ObjectoxStatus) status;

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

  private void header0(HeaderName name, String value) { // write our the name
    int index;
    index = name.index();

    byte[] nameBytes;

    if (index >= 0) {
      nameBytes = ObjectoxServerRequestHeaders.STD_HEADER_NAME_BYTES[index];
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
      keepAlive = false;
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
    writeBytes(Bytes.CRLF);

    responseBody = body;
  }

  private void checkResponse() {
    if (state == _REQUEST) {
      cursor = 0;

      responseBody = null;

      state = _RESPONSE;

      return;
    }

    if (state == _RESPONSE) {
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

    int remaining;
    remaining = buffer.length - cursor;

    if (length > remaining) {
      throw new UnsupportedOperationException("Implement me");
    }

    System.arraycopy(bytes, 0, buffer, cursor, length);

    cursor += length;
  }

  @Override
  public final void commit() throws IOException, IllegalStateException {
    Check.state(state == _RESPONSE, "Cannot commit as we are not in the response phase");
    Check.state(responseBody != null, "Cannot commit: missing ServerExchange::send method invocation");

    OutputStream outputStream;
    outputStream = socket.getOutputStream();

    // send headers
    outputStream.write(buffer, 0, cursor);

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

    state = _COMMITED;
  }

  @Override
  public final boolean keepAlive() {
    return keepAlive;
  }

}