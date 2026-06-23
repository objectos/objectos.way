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
package objectox.http.req;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import objectox.http.HttpClientException;
import objectox.http.HttpClientException.Kind;

final class RequestBodyDateParser {

  private final RequestBodySupport bodySupport;

  private final RequestInputStream input;

  private final RequestBodyMeta.Data meta;

  RequestBodyDateParser(RequestBodySupport bodySupport, RequestInputStream input, RequestBodyMeta.Data meta) {
    this.bodySupport = bodySupport;

    this.input = input;

    this.meta = meta;
  }

  public final RequestBodyData parse() throws IOException {
    try {
      return parse0();
    } catch (RequestInputStream.Eof e) {
      final String msg;
      msg = "EOF while reading request body";

      throw new HttpClientException(msg, Kind.INCOMPLETE_REQUEST_BODY);
    }
  }

  private RequestBodyData parse0() throws IOException {
    return switch (meta) {
      case RequestBodyMeta.DataKind.EMPTY -> RequestBodyData.ofNull();

      case RequestBodyMeta.Fixed(long length) -> parseFixed(length);
    };
  }

  private RequestBodyData parseFixed(long length) throws IOException {
    final long sizeMax;
    sizeMax = bodySupport.sizeMax();

    if (length > sizeMax) {
      final String msg;
      msg = "The request message body exceeds the server's maximum allowed limit: %d > %d".formatted(length, sizeMax);

      throw new HttpClientException(msg, Kind.CONTENT_TOO_LARGE);
    }

    else if (length > bodySupport.memoryMax()) {
      return parseFixedFile(length);
    }

    else {
      return parseFixedMemory(length);
    }
  }

  private RequestBodyData parseFixedFile(long length) throws IOException {
    final Path file;
    file = bodySupport.file();

    try (OutputStream output = Files.newOutputStream(file, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
      copy(length, output);
    }

    return RequestBodyData.of(file);
  }

  private RequestBodyData parseFixedMemory(long length) throws IOException {
    final ByteArrayOutputStream output;
    output = new ByteArrayOutputStream();

    copy(length, output);

    final byte[] bytes;
    bytes = output.toByteArray();

    return RequestBodyData.of(bytes);
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
