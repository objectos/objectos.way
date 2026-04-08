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
import objectos.internal.Ascii;

final class HttpRequestParser7BodyMeta {

  enum Invalid implements HttpClientException.Kind {
    // invalid value, e.g., 'Content-Length: two hundred bytes'
    INVALID_CONTENT_LENGTH("Invalid request headers.\n", HttpStatus.BAD_REQUEST),

    // request include both Content-Length and Transfer-Enconding.
    BOTH_CL_TE("Invalid request headers.\n", HttpStatus.BAD_REQUEST),

    // 411 Length Required
    LENGTH_REQUIRED("Invalid request headers.\n", HttpStatus.LENGTH_REQUIRED),

    // 413 Content Too Large
    CONTENT_TOO_LARGE("The request message body exceeds the server's maximum allowed limit.\n", HttpStatus.CONTENT_TOO_LARGE),

    // 501 Not Implemented
    NOT_IMPLEMENTED("Invalid request headers.\n", HttpStatus.NOT_IMPLEMENTED);

    private final String message;

    private final HttpStatus status;

    private Invalid(String message, HttpStatus status) {
      this.message = message;

      this.status = status;
    }

    @Override
    public final String message() {
      return message;
    }

    @Override
    public final HttpStatus status() {
      return status;
    }
  }

  private final HttpRequestHeaders headers;

  HttpRequestParser7BodyMeta(HttpRequestHeaders headers) {
    this.headers = headers;
  }

  public final HttpRequestBodyMeta parse() throws IOException {
    final String contentLength;
    contentLength = headers.header(HttpHeaderName.CONTENT_LENGTH);

    final String contentType;
    contentType = headers.header(HttpHeaderName0.CONTENT_TYPE);

    final String transferEncoding;
    transferEncoding = headers.header(HttpHeaderName.TRANSFER_ENCODING);

    if (contentLength != null) {

      if (transferEncoding != null) {
        throw HttpClientException.of(Invalid.BOTH_CL_TE);
      }

      final long len;
      len = parseContentLength(contentLength);

      if (len == 0) {
        return HttpRequestBodyMeta.ofEmpty();
      }

      final HttpRequestBodyMeta.Type type;
      type = parseContentType(contentType);

      return HttpRequestBodyMeta.of(len, type);

    }

    if (contentType != null) {
      throw HttpClientException.of(Invalid.LENGTH_REQUIRED);
    }

    if (transferEncoding != null) {
      throw HttpClientException.of(Invalid.NOT_IMPLEMENTED);
    }

    return HttpRequestBodyMeta.ofEmpty();
  }

  private long parseContentLength(String contentLength) throws HttpClientException {
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

  private HttpRequestBodyMeta.Type parseContentType(String contentType) {
    if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
      return HttpRequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED;
    }

    return HttpRequestBodyMeta.TypeKind.NONE;
  }

}
