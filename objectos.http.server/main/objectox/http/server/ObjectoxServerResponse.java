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
import java.util.EnumMap;
import java.util.Map;
import objectos.http.HeaderName;
import objectos.http.Http;
import objectos.http.server.ServerResponse;
import objectos.http.server.ServerResponseResult;
import objectos.lang.object.Check;
import objectox.http.HttpStatus;

public class ObjectoxServerResponse implements ServerResponse, ServerResponseResult {

  static final Map<HttpStatus, byte[]> HTTP_STATUS_RESPONSE_BYTES;

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

  private final byte[] buffer;

  private Clock clock;

  private int cursor;

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
  }

  @Override
  public final ServerResponse ok() {
    return status(HttpStatus.OK);
  }

  @Override
  public final ServerResponse notModified() {
    return status(HttpStatus.NOT_MODIFIED);
  }

  private ServerResponse status(HttpStatus status) {
    writeBytes(version.responseBytes);

    byte[] statusBytes;
    statusBytes = HTTP_STATUS_RESPONSE_BYTES.get(status);

    if (statusBytes == null) {
      throw new UnsupportedOperationException("Implement me");
    }

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

    writeBytes(Bytes.COLONSP);

    byte[] valueBytes;
    valueBytes = value.getBytes(StandardCharsets.UTF_8);

    writeBytes(valueBytes);

    writeBytes(Bytes.CRLF);

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

    return this;
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