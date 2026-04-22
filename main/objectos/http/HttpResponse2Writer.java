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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.internal.Bytes;
import objectos.lang.Testable;
import objectos.way.Media;

final class HttpResponse2Writer {

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

  private static final byte[] HTTP_1_1 = "HTTP/1.1 ".getBytes(StandardCharsets.US_ASCII);

  private final byte[] buffer;

  private int bufferIndex;

  private boolean chunked;

  private final boolean head;

  private final OutputStream outputStream;

  private final boolean testable;

  HttpResponse2Writer(byte[] buffer, boolean head, OutputStream outputStream, boolean testable) {
    this.buffer = buffer;

    this.head = head;

    this.outputStream = outputStream;

    this.testable = testable;
  }

  public final void status(HttpStatus status) throws IOException {
    writeBytes(HTTP_1_1);

    final HttpStatusImpl impl;
    impl = (HttpStatusImpl) status;

    final byte[] statusBytes;
    statusBytes = STATUS_LINES[impl.ordinal()];

    writeBytes(statusBytes);
  }

  public final void header(HttpHeaderName name, String value) throws IOException {
    final HttpHeaderName0 nameImpl;
    nameImpl = (HttpHeaderName0) name;

    final byte[] nameBytes;
    nameBytes = nameImpl.headerCaseBytes();

    writeBytes(nameBytes);

    if (value.isEmpty()) {
      writeBytes(Bytes.COLON_BYTES);
    } else {
      // write out the separator
      writeBytes(Bytes.COLONSP);

      // write out the value
      byte[] valueBytes;
      valueBytes = value.getBytes(StandardCharsets.US_ASCII);

      writeBytes(valueBytes);

      if (name == HttpHeaderName.TRANSFER_ENCODING) {
        chunked = "chunked".equalsIgnoreCase(value);
      }
    }

    writeBytes(Bytes.CRLF);
  }

  public final void lineSeparator() throws IOException {
    writeBytes(Bytes.CRLF);
  }

  public final void send() throws IOException {
    flush();
  }

  public final void send(byte[] bytes, int off, int len) throws IOException {
    if (head) {
      flush();

      return;
    }

    if (chunked) {
      throw new UnsupportedOperationException("Implement me");
    }

    else {
      flush();

      outputStream.write(bytes, off, len);
    }
  }

  public final void send(Media.Stream media) throws IOException {
    if (head) {
      flush();

      return;
    }

    if (chunked) {
      try (HttpResponse3Chunked out = new HttpResponse3Chunked(buffer, bufferIndex, outputStream)) {
        media.writeTo(out);
      }

      bufferIndex = 0;
    }

    else {
      flush();

      media.writeTo(outputStream);

      outputStream.flush();
    }
  }

  public final void send(Media.Text media) throws IOException {
    if (head) {
      flush();

      return;
    }

    final Charset charset;
    charset = media.charset();

    if (testable && media instanceof Testable t) {
      final String text;
      text = t.toTestableText();

      byte[] bytes;
      bytes = text.getBytes(charset);

      flush();

      outputStream.write(bytes);

      return;
    }

    if (chunked) {
      try (HttpResponse3Chunked output = new HttpResponse3Chunked(buffer, bufferIndex, outputStream)) {
        final HttpResponse4Appendable out;
        out = new HttpResponse4Appendable(charset, output);

        media.writeTo(out);
      }

      bufferIndex = 0;
    }

    else {
      flush();

      // do not close the writer!
      final OutputStreamWriter writer;
      writer = new OutputStreamWriter(outputStream, charset);

      media.writeTo(writer);

      writer.flush();
    }
  }

  public final void send(Path file) throws IOException {
    if (head) {
      flush();

      return;
    }

    if (chunked) {
      try (
          InputStream input = Files.newInputStream(file);
          HttpResponse3Chunked output = new HttpResponse3Chunked(buffer, bufferIndex, outputStream);
      ) {
        input.transferTo(output);
      }

      bufferIndex = 0;
    }

    else {
      flush();

      try (InputStream input = Files.newInputStream(file)) {
        input.transferTo(outputStream);
      }
    }
  }

  private void flush() throws IOException {
    outputStream.write(buffer, 0, bufferIndex);

    bufferIndex = 0;
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

}
