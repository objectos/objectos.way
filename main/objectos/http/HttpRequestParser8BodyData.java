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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

final class HttpRequestParser8BodyData {

  private static final byte[] MESSAGE = "Invalid request headers.\n".getBytes(StandardCharsets.US_ASCII);

  enum Invalid implements HttpClientException.Kind {
    // EOF while reading body
    EOF(HttpStatus.BAD_REQUEST),

    // 413 Content Too Large
    CONTENT_TOO_LARGE(HttpStatus.CONTENT_TOO_LARGE);

    private final HttpStatus status;

    private Invalid(HttpStatus status) {
      this.status = status;
    }

    @Override
    public final byte[] message() {
      return MESSAGE;
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

  private final HttpRequestBodyOptions bodyOptions;

  private final long id;

  private final HttpRequestParser0Input input;

  private final HttpRequestBodyMeta.Data meta;

  HttpRequestParser8BodyData(HttpRequestBodyOptions bodyOptions, long id, HttpRequestParser0Input input, HttpRequestBodyMeta.Data meta) {
    this.bodyOptions = bodyOptions;

    this.id = id;

    this.input = input;

    this.meta = meta;
  }

  public final HttpRequestBodyData parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof e) {
      throw HttpClientException.of(Invalid.EOF, e);
    }
  }

  private HttpRequestBodyData parse0() throws IOException {
    return switch (meta) {
      case HttpRequestBodyMeta.DataKind.EMPTY -> HttpRequestBodyData.ofNull();

      case HttpRequestBodyMeta.Fixed(long length) -> parseFixed(length);
    };
  }

  private HttpRequestBodyData parseFixed(long length) throws IOException {
    if (length > bodyOptions.sizeMax()) {
      throw HttpClientException.of(Invalid.CONTENT_TOO_LARGE);
    }

    else if (length > bodyOptions.memoryMax()) {
      return parseFixedFile(length);
    }

    else {
      return parseFixedMemory(length);
    }
  }

  private HttpRequestBodyData parseFixedFile(long length) throws IOException {
    final Path file;
    file = bodyOptions.file(id);

    try (OutputStream output = bodyOptions.newOutputStream(file)) {
      copy(length, output);
    }

    return HttpRequestBodyData.of(file);
  }

  private HttpRequestBodyData parseFixedMemory(long length) throws IOException {
    final ByteArrayOutputStream output;
    output = new ByteArrayOutputStream();

    copy(length, output);

    final byte[] bytes;
    bytes = output.toByteArray();

    return HttpRequestBodyData.of(bytes);
  }

  private void copy(long length, OutputStream output) throws IOException {
    long remaining;
    remaining = length;

    while (remaining > 0) {
      final int buffered;
      buffered = input.readForBody();

      final long bytes;
      bytes = Math.min(remaining, buffered);

      // bytes is guaranteed to fit in an int
      // in any case... let's toIntExact
      final int len;
      len = Math.toIntExact(bytes);

      input.bufferTo(output, len);

      remaining -= len;
    }

    assert remaining == 0;
  }

}
