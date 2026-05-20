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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import objectos.internal.Bytes;
import objectos.way.Media;

final class ResponseOutput {

  private static final Map<HttpStatus0, byte[]> STATUS_LINES = new EnumMap<>(HttpStatus0.class);

  static {
    final HttpStatus0[] values;
    values = HttpStatus0.values();

    for (HttpStatus0 status : values) {
      final String response;
      response = "HTTP/1.1 " + Integer.toString(status.code()) + " " + status.reasonPhrase() + "\r\n";

      final byte[] bytes;
      bytes = Http.utf8(response);

      STATUS_LINES.put(status, bytes);
    }
  }

  private final byte[] buffer;

  private int bufferIndex;

  private final Clock clock;

  private final boolean head;

  private final OutputStream outputStream;

  ResponseOutput(byte[] buffer, Clock clock, boolean head, OutputStream outputStream) {
    this.buffer = buffer;

    this.clock = clock;

    this.head = head;

    this.outputStream = outputStream;
  }

  private static final byte[] COLONCRLF = ":\r\n".getBytes(StandardCharsets.US_ASCII);

  public final void send(Response0 response) throws IOException {
    // status
    final HttpStatus0 status;
    status = response.status();

    final byte[] statusLine;
    statusLine = STATUS_LINES.get(status);

    write(statusLine);

    // headers
    final List<Header> headers;
    headers = response.headers();

    for (Header header : headers) {
      final HttpHeaderName name;

      final String value;

      if (header == Header.DATE) {
        name = HttpHeaderName.DATE;

        final ZonedDateTime now;
        now = ZonedDateTime.now(clock);

        value = Http.formatDate(now);
      } else {
        name = header.name();

        value = header.value();
      }

      write(name, value);
    }

    // body
    final ResponseBody body;
    body = response.body();

    switch (body) {
      case ResponseBody.OfEmpty.INSTANCE -> write(Bytes.CRLF);

      case ResponseBody.OfBytes _ -> throw new UnsupportedOperationException("Implement me");

      case ResponseBody.OfFile _ -> throw new UnsupportedOperationException("Implement me");

      case ResponseBody.OfMediaStream _ -> throw new UnsupportedOperationException("Implement me");

      case ResponseBody.OfMediaText t -> {
        write(HttpHeaderName.TRANSFER_ENCODING, "chunked");

        write(Bytes.CRLF);

        final Media.Text entity;
        entity = t.entity();

        final Charset charset;
        charset = entity.charset();

        try (OutputStream chunked = new ResponseOutput0Chunked(buffer, bufferIndex, outputStream)) {
          final Appendable out;
          out = new ResponseOutput1Appendable(charset, chunked);

          entity.writeTo(out);
        }
      }
    }
  }

  private void write(byte[] bytes) throws IOException {
    int bytesIndex;
    bytesIndex = 0;

    int remaining;
    remaining = bytes.length;

    while (remaining > 0) {
      int available;
      available = buffer.length - bufferIndex;

      if (available <= 0) {
        flush();

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

  private void write(HttpHeaderName name, String value) throws IOException {
    final HttpHeaderName0 nameImpl;
    nameImpl = (HttpHeaderName0) name;

    final byte[] nameBytes;
    nameBytes = nameImpl.headerCaseBytes();

    write(nameBytes);

    if (value.isEmpty()) {
      write(COLONCRLF);
    }

    else {
      final byte[] valueBytes;
      valueBytes = value.getBytes(StandardCharsets.US_ASCII);

      write(Bytes.COLONSP);

      write(valueBytes);

      write(Bytes.CRLF);
    }
  }

  private void flush() throws IOException {
    outputStream.write(buffer, 0, bufferIndex);

    bufferIndex = 0;
  }

}
