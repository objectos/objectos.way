/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import objectos.internal.Bytes;

final class HttpResponse2WriteStart {

  private static final byte[][] STATUS_LINES;

  static {
    final HttpStatusImpl[] values;
    values = HttpStatusImpl.values();

    final int size;
    size = values.length;

    final byte[][] map;
    map = new byte[size][];

    for (HttpStatusImpl status : values) {
      final int index;
      index = status.ordinal();

      final String response;
      response = Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      map[index] = Http.utf8(response);
    }

    STATUS_LINES = map;
  }

  private final byte[] buffer;

  private int bufferIndex;

  private final List<HttpResponse1Header> headers;

  private final OutputStream outputStream;

  private final HttpStatus status;

  private final HttpVersion version;

  HttpResponse2WriteStart(byte[] buffer, List<HttpResponse1Header> headers, OutputStream outputStream, HttpStatus status, HttpVersion version) {
    this.buffer = buffer;

    this.headers = headers;

    this.outputStream = outputStream;

    this.status = status;

    this.version = version;
  }

  public final void write() throws IOException {
    // version
    writeBytes(version.responseBytes);

    // status
    HttpStatusImpl internal;
    internal = (HttpStatusImpl) status;

    byte[] statusBytes;
    statusBytes = STATUS_LINES[internal.ordinal()];

    writeBytes(statusBytes);

    // headers
    for (var header : headers) {
      writeHeader(header);
    }
  }

  private void writeHeader(HttpResponse1Header header) throws IOException {
    final HttpHeaderName name;
    name = header.name();

    final HttpHeaderNameImpl nameImpl;
    nameImpl = (HttpHeaderNameImpl) name;

    final byte[] nameBytes;
    nameBytes = nameImpl.getBytes(version);

    writeBytes(nameBytes);

    final String value;
    value = header.value();

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
  }

  private void writeBytes(byte[] bytes) throws IOException {
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

}
