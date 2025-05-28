/*
 * Copyright (C) 2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.ArrayList;
import java.util.List;

final class HttpStatus implements Http.Status {

  private static class Builder {

    private final List<HttpStatus> standardValues = new ArrayList<>();

    private int index;

    public final HttpStatus create(int code, String reasonPhrase) {
      HttpStatus result;
      result = new HttpStatus(index++, code, reasonPhrase);

      standardValues.add(result);

      return result;
    }

    public final HttpStatus[] buildValues() {
      return standardValues.toArray(HttpStatus[]::new);
    }

  }

  private static Builder BUILDER = new Builder();

  // 2.x.x

  /**
   * The {@code 200 OK} status.
   */
  public static final HttpStatus OK = BUILDER.create(200, "OK");

  // 3.x.x

  /**
   * The {@code 301 MOVED PERMANENTLY} status.
   */
  public static final HttpStatus MOVED_PERMANENTLY = BUILDER.create(301, "Moved Permanently");

  /**
   * The {@code 302 FOUND} status.
   */
  public static final HttpStatus FOUND = BUILDER.create(302, "Found");

  /**
   * The {@code 303 SEE OTHER} status.
   */
  public static final HttpStatus SEE_OTHER = BUILDER.create(303, "See Other");

  /**
   * The {@code 304 NOT MODIFIED} status.
   */
  public static final HttpStatus NOT_MODIFIED = BUILDER.create(304, "Not Modified");

  // 4.x.x

  public static final HttpStatus BAD_REQUEST = BUILDER.create(400, "Bad Request");

  public static final HttpStatus FORBIDDEN = BUILDER.create(403, "Forbidden");

  public static final HttpStatus NOT_FOUND = BUILDER.create(404, "Not Found");

  /**
   * The {@code 405 METHOD NOT ALLOWED} status.
   */
  public static final HttpStatus METHOD_NOT_ALLOWED = BUILDER.create(405, "Method Not Allowed");

  /**
   * The {@code 413 Content Too Large} status.
   */
  public static final HttpStatus CONTENT_TOO_LARGE = BUILDER.create(413, "Content Too Large");

  /**
   * The {@code 414 URI TOO LONG} status.
   */
  public static final HttpStatus URI_TOO_LONG = BUILDER.create(414, "URI Too Long");

  /**
   * The {@code 415 UNSUPPORTED MEDIA TYPE} status.
   */
  public static final HttpStatus UNSUPPORTED_MEDIA_TYPE = BUILDER.create(415, "UNSUPPORTED MEDIA TYPE");

  /**
   * The {@code 422 UNPROCESSABLE CONTENT} status.
   */
  public static final HttpStatus UNPROCESSABLE_CONTENT = BUILDER.create(422, "UNPROCESSABLE CONTENT");

  /**
   * The {@code 431 Request Header Fields Too Large} status.
   */
  public static final HttpStatus REQUEST_HEADER_FIELDS_TOO_LARGE = BUILDER.create(431, "Request Header Fields Too Large");

  // 5.x.x

  /**
   * The {@code 500 INTERNAL SERVER ERROR} status.
   */
  public static final HttpStatus INTERNAL_SERVER_ERROR = BUILDER.create(500, "Internal Server Error");

  /**
   * The {@code 501 NOT IMPLEMENTED} status.
   */
  public static final HttpStatus NOT_IMPLEMENTED = BUILDER.create(501, "Not Implemented");

  /**
   * The {@code 505 HTTP VERSION NOT SUPPORTED} status.
   */
  public static final HttpStatus HTTP_VERSION_NOT_SUPPORTED = BUILDER.create(505, "HTTP Version Not Supported");

  private static final HttpStatus[] VALUES;

  static {
    VALUES = BUILDER.buildValues();

    BUILDER = null;
  }

  public final int index;

  public final int code;

  public final String reasonPhrase;

  public HttpStatus(int index, int code, String reasonPhrase) {
    this.index = index;

    this.code = code;

    this.reasonPhrase = reasonPhrase;
  }

  public static HttpStatus get(int index) {
    return VALUES[index];
  }

  public static int size() {
    return VALUES.length;
  }

  public final int index() {
    return index;
  }

  @Override
  public final int code() {
    return code;
  }

  @Override
  public final String reasonPhrase() {
    return reasonPhrase;
  }

  @Override
  public final int hashCode() {
    return code;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof HttpStatus that
        && code == that.code;
  }

  @Override
  public final String toString() {
    return "HttpStatus[" + code + "=" + reasonPhrase + "]";
  }

}