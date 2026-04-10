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
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.http.HttpRequestParserException.Kind;

final class HttpRequestParser8BodyData {

  private final HttpRequestBodySupport bodySupport;

  private final HttpRequestParser0Input input;

  private final HttpRequestBodyMeta.Data meta;

  HttpRequestParser8BodyData(HttpRequestBodySupport bodySupport, HttpRequestParser0Input input, HttpRequestBodyMeta.Data meta) {
    this.bodySupport = bodySupport;

    this.input = input;

    this.meta = meta;
  }

  public final HttpRequestBodyData parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof e) {
      final String msg;
      msg = "EOF while reading request body";

      throw new HttpRequestParserException(msg, Kind.INCOMPLETE_REQUEST_BODY);
    }
  }

  private HttpRequestBodyData parse0() throws IOException {
    return switch (meta) {
      case HttpRequestBodyMeta.DataKind.EMPTY -> HttpRequestBodyData.ofNull();

      case HttpRequestBodyMeta.Fixed(long length) -> parseFixed(length);
    };
  }

  private HttpRequestBodyData parseFixed(long length) throws IOException {
    final long sizeMax;
    sizeMax = bodySupport.sizeMax();

    if (length > sizeMax) {
      final String msg;
      msg = "The request message body exceeds the server's maximum allowed limit: %d > %d".formatted(length, sizeMax);

      throw new HttpRequestParserException(msg, Kind.CONTENT_TOO_LARGE);
    }

    else if (length > bodySupport.memoryMax()) {
      return parseFixedFile(length);
    }

    else {
      return parseFixedMemory(length);
    }
  }

  private HttpRequestBodyData parseFixedFile(long length) throws IOException {
    final Path file;
    file = bodySupport.file();

    try (OutputStream output = Files.newOutputStream(file)) {
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
