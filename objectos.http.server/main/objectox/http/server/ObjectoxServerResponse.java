/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import objectos.http.HeaderName;
import objectos.http.Http;
import objectos.http.Status;
import objectos.http.server.ServerResponse;
import objectos.http.server.ServerResponseResult;
import objectos.lang.object.Check;
import objectox.http.ObjectoxStatus;

public class ObjectoxServerResponse implements ServerResponse {

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

  private final byte[] buffer;

  private Clock clock;

  private int cursor;

  private ServerResponseResult result = ObjectoxServerResponseResult.DEFAULT;

  private Version version = Version.HTTP_1_1;

  Object body;

  public ObjectoxServerResponse(byte[] buffer) {
    this.buffer = buffer;
  }

  public final void clock(Clock clock) {
    this.clock = clock;
  }

  public final void version(Version version) {
    this.version = version;
  }

  public final void reset() {
    cursor = 0;

    body = null;

    result = ObjectoxServerResponseResult.DEFAULT;
  }

  @Override
  public final ServerResponse ok() {
    return status(Status.OK);
  }

  @Override
  public final ServerResponse notModified() {
    return status(Status.NOT_MODIFIED);
  }

  @Override
  public final ServerResponse notFound() {
    return status(Status.NOT_FOUND);
  }

  private ServerResponse status(Status status) {
    writeBytes(version.responseBytes);

    ObjectoxStatus internal;
    internal = (ObjectoxStatus) status;

    byte[] statusBytes;
    statusBytes = STATUS_LINES[internal.index];

    writeBytes(statusBytes);

    return this;
  }

  @Override
  public final ServerResponse contentLength(long value) {
    String s;
    s = Long.toString(value);

    return header(HeaderName.CONTENT_LENGTH, s);
  }

  @Override
  public final ServerResponse contentType(String value) {
    return header(HeaderName.CONTENT_TYPE, value);
  }

  @Override
  public final ServerResponse dateNow() {
    ZonedDateTime now;
    now = ZonedDateTime.now(clock);

    String formatted;
    formatted = Http.formatDate(now);

    return header(HeaderName.DATE, formatted);
  }

  @Override
  public final ServerResponse header(HeaderName name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    // write our the name
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
      result = ObjectoxServerResponseResult.CLOSE;
    }

    return this;
  }

  @Override
  public final ServerResponseResult send() {
    return body(NoResponseBody.INSTANCE);
  }

  @Override
  public final ServerResponseResult send(byte[] body) {
    return body(body);
  }

  @Override
  public final ServerResponseResult send(Path file) {
    Check.argument(Files.isRegularFile(file), "Path must be of an existing regular file");

    return body(file);
  }

  private ServerResponseResult body(Object body) {
    writeBytes(Bytes.CRLF);

    this.body = body;

    return result;
  }

  @Override
  public final String toString() {
    return new String(buffer, 0, cursor, StandardCharsets.UTF_8);
  }

  final void commit(OutputStream outputStream) throws IOException {
    Check.state(body != null, "Cannot commit: missing ServerResponse::send method invocation");

    // send headers
    outputStream.write(buffer, 0, cursor);

    switch (body) {
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

}