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
import objectos.http.HttpRequestParserException.Kind;
import objectos.internal.Ascii;

final class HttpRequestParser7BodyMeta {

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
        final String msg;
        msg = "Content-Length and Transfer-Encoding in the same request message";

        throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_HEADERS);
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
      final String msg;
      msg = "Invalid request headers: expected Content-Length";

      throw new HttpRequestParserException(msg, Kind.LENGTH_REQUIRED);
    }

    if (transferEncoding != null) {
      final String msg;
      msg = "Support for the request Transfer-Encoding header is not implemented";

      throw new HttpRequestParserException(msg, Kind.NOT_IMPLEMENTED);
    }

    return HttpRequestBodyMeta.ofEmpty();
  }

  private long parseContentLength(String contentLength) throws HttpRequestParserException {
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
        final String msg;
        msg = "Invalid Content-Length: char '%c' is not a digit".formatted(d);

        throw new HttpRequestParserException(msg, Kind.INVALID_REQUEST_HEADERS);
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
      final String msg;
      msg = "Invalid Content-Length: value is larger than Long.MAX_VALUE";

      throw new HttpRequestParserException(msg, Kind.CONTENT_TOO_LARGE);
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
