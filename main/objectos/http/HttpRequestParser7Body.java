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
import objectos.http.HttpRequestParser.Invalid;
import objectos.internal.Ascii;

final class HttpRequestParser7Body {

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

  private final int bodyMemoryMax;

  private final long bodySizeMax;

  private final Map<HttpHeaderName, Object> headers;

  private final long id;

  private final HttpRequestParser0Input input;

  HttpRequestParser7Body(HttpExchangeBodyFiles bodyFiles, int bodyMemoryMax, long bodySizeMax, Map<HttpHeaderName, Object> headers, long id, HttpRequestParser0Input input) {
    this.bodyFiles = bodyFiles;

    this.bodyMemoryMax = bodyMemoryMax;

    this.bodySizeMax = bodySizeMax;

    this.headers = headers;

    this.id = id;

    this.input = input;
  }

  public final HttpRequestBody parse() throws IOException {
    try {
      return parse0();
    } catch (HttpRequestParser0Input.Eof e) {
      throw HttpClientException.of(Invalid.EOF, e);
    }
  }

  private HttpRequestBody parse0() throws IOException {
    final String contentLength;
    contentLength = Http.queryParamsGet(headers, HttpHeaderName.CONTENT_LENGTH);

    if (contentLength != null) {
      return parseBodyFixed(headers, contentLength);
    }

    final String transferEncoding;
    transferEncoding = Http.queryParamsGet(headers, HttpHeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      // TODO 501 Not Implemented
      throw new UnsupportedOperationException("Implement me");
    }

    final String contentType;
    contentType = Http.queryParamsGet(headers, HttpHeaderNameImpl.CONTENT_TYPE);

    if (contentType != null) {
      throw HttpClientException.of(Invalid.LENGTH_REQUIRED);
    }

    return HttpRequestBodyImpl.ofNull();
  }

  private HttpRequestBody parseBodyFixed(Map<HttpHeaderName, Object> headers, String contentLength) throws IOException {
    final String transferEncoding;
    transferEncoding = Http.queryParamsGet(headers, HttpHeaderNameImpl.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      throw HttpClientException.of(Invalid.BOTH_CL_TE);
    }

    final long length;
    length = parseBodyFixedLength(contentLength);

    if (length == 0) {
      return HttpRequestBodyImpl.ofNull();
    }

    if (length > bodySizeMax) {
      throw HttpClientException.of(Invalid.CONTENT_TOO_LARGE);
    }

    final String contentType;
    contentType = Http.queryParamsGet(headers, HttpHeaderNameImpl.CONTENT_TYPE);

    final boolean parseForm;
    parseForm = contentType != null && contentType.equalsIgnoreCase("application/x-www-form-urlencoded");

    if (length <= bodyMemoryMax) {
      return parseBodyFixedMemory(length, parseForm);
    }

    else {
      return parseBodyFixedFile(length, parseForm);
    }
  }

  private long parseBodyFixedLength(String contentLength) throws HttpClientException {
    long length;
    length = 0;

    final long hardLimit;
    hardLimit = Long.MAX_VALUE;

    final long multLimit;
    multLimit = hardLimit / 10;

    boolean overflow;
    overflow = false;

    for (int i = 0, len = contentLength.length(); i < len; i++) {
      final char d;
      d = contentLength.charAt(i);

      if (!Ascii.isDigit(d)) {
        throw HttpClientException.of(Invalid.INVALID_CONTENT_LENGTH);
      }

      if (overflow) {
        // already invalid...
        // just check if content-length is numeric
        continue;
      }

      if (length > multLimit) {
        overflow = true;

        continue;
      }

      length *= 10;

      final long digit;
      digit = (long) d & 0xF;

      if (length > hardLimit - digit) {
        overflow = true;

        continue;
      }

      length += digit;
    }

    if (overflow) {
      throw HttpClientException.of(Invalid.CONTENT_TOO_LARGE);
    }

    return length;
  }

  private HttpRequestBody parseBodyFixedMemory(long length, boolean parseForm) throws IOException {
    // length is guaranteed to fit in an int
    // in any case we throw if length overflows...

    final int len;
    len = Math.toIntExact(length);

    final ByteArrayOutputStream outputStream;
    outputStream = new ByteArrayOutputStream(len);

    input.transferTo(outputStream, len);

    final byte[] bytes;
    bytes = outputStream.toByteArray();

    final Map<String, Object> formParams;
    formParams = parseForm ? parseForm(bytes) : null;

    return HttpRequestBodyImpl.of(bytes, formParams);
  }

  private HttpRequestBody parseBodyFixedFile(long length, boolean parseForm) throws IOException {
    final Path file;
    file = bodyFiles.file(id);

    try (OutputStream outputStream = bodyFiles.newOutputStream(file)) {
      input.transferTo(outputStream, length);
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
