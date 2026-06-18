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

import java.io.IOException;
import objectos.http.HeaderName;
import objectos.internal.Ascii;
import objectox.http.HttpClientException;
import objectox.http.HeaderNamePojo;
import objectox.http.HttpServerException;
import objectox.http.HttpClientException.Kind;

final class RequestParser7BodyMeta {

  private final RequestHeaders headers;

  RequestParser7BodyMeta(RequestHeaders headers) {
    this.headers = headers;
  }

  public final RequestBodyMeta parse() throws IOException {
    final String contentLength;
    contentLength = headers.header(HeaderName.CONTENT_LENGTH);

    final String contentType;
    contentType = headers.header(HeaderNamePojo.CONTENT_TYPE);

    final String transferEncoding;
    transferEncoding = headers.header(HeaderName.TRANSFER_ENCODING);

    if (contentLength != null) {

      if (transferEncoding != null) {
        final String msg;
        msg = "Content-Length and Transfer-Encoding in the same request message";

        throw new HttpClientException(msg, Kind.INVALID_REQUEST_HEADERS);
      }

      final long len;
      len = parseContentLength(contentLength);

      if (len == 0) {
        return RequestBodyMeta.ofEmpty();
      }

      final RequestBodyMeta.Type type;
      type = parseContentType(contentType);

      return RequestBodyMeta.of(len, type);

    }

    if (contentType != null) {
      final String msg;
      msg = "Invalid request headers: expected Content-Length";

      throw new HttpClientException(msg, Kind.LENGTH_REQUIRED);
    }

    if (transferEncoding != null) {
      throw new HttpServerException(HttpServerException.Kind.TRANSFER_ENCODING);
    }

    return RequestBodyMeta.ofEmpty();
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
        final String msg;
        msg = "Invalid Content-Length: char '%c' is not a digit".formatted(d);

        throw new HttpClientException(msg, Kind.INVALID_REQUEST_HEADERS);
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

      throw new HttpClientException(msg, Kind.CONTENT_TOO_LARGE);
    }

    return length;
  }

  private RequestBodyMeta.Type parseContentType(String contentType) {
    if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
      return RequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED;
    }

    return RequestBodyMeta.TypeKind.NONE;
  }

}
