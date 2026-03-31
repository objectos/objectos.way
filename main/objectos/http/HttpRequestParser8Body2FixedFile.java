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
import java.util.Map;

final class HttpRequestParser8Body2FixedFile {

  enum Invalid implements HttpClientException.Kind {
    // Unexpected end of stream
    EOF;

    private static final byte[] MESSAGE = "Invalid request body.\n".getBytes(StandardCharsets.US_ASCII);

    private final HttpStatus status;

    private Invalid() {
      this(HttpStatus.BAD_REQUEST);
    }

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

  private final HttpExchangeBodyFiles bodyFiles;

  @SuppressWarnings("unused")
  private final int bodyMemoryMax;

  @SuppressWarnings("unused")
  private final long bodySizeMax;

  @SuppressWarnings("unused")
  private final Map<HttpHeaderName, Object> headers;

  private final long id;

  @SuppressWarnings("unused")
  private final HttpRequestParser0Input input;

  HttpRequestParser8Body2FixedFile(HttpExchangeBodyFiles bodyFiles, int bodyMemoryMax, long bodySizeMax, Map<HttpHeaderName, Object> headers, long id, HttpRequestParser0Input input) {
    this.bodyFiles = bodyFiles;

    this.bodyMemoryMax = bodyMemoryMax;

    this.bodySizeMax = bodySizeMax;

    this.headers = headers;

    this.id = id;

    this.input = input;
  }

  public final HttpRequestBody parse() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @SuppressWarnings("unused")
  private HttpRequestBody parseBodyFixedMemory(long length, boolean parseForm) throws IOException {
    // length is guaranteed to fit in an int
    // in any case we throw if length overflows...

    final int len;
    len = Math.toIntExact(length);

    final ByteArrayOutputStream outputStream;
    outputStream = new ByteArrayOutputStream(len);

    //input.transferTo(outputStream, len);

    final byte[] bytes;
    bytes = outputStream.toByteArray();

    final Map<String, Object> formParams;
    formParams = parseForm ? parseForm(bytes) : null;

    return HttpRequestBodyImpl.of(bytes, formParams);
  }

  @SuppressWarnings("unused")
  private HttpRequestBody parseBodyFixedFile(long length, boolean parseForm) throws IOException {
    final Path file;
    file = bodyFiles.file(id);

    try (OutputStream outputStream = bodyFiles.newOutputStream(file)) {
      //input.transferTo(outputStream, length);
    }

    final Map<String, Object> formParams;
    formParams = parseForm ? parseForm(file) : null;

    return HttpRequestBodyImpl.of(file, formParams);
  }

  private Map<String, Object> parseForm(byte[] bytes) {
    throw new UnsupportedOperationException("Implement me");
  }

  private Map<String, Object> parseForm(Path file) {
    throw new UnsupportedOperationException("Implement me");
  }

}
