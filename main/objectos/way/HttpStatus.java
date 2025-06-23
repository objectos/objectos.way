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

  public static final HttpStatus OK = BUILDER.create(200, "OK");

  public static final HttpStatus CREATED = BUILDER.create(201, "Created");

  public static final HttpStatus NO_CONTENT = BUILDER.create(204, "No Content");

  // 3.x.x

  public static final HttpStatus MOVED_PERMANENTLY = BUILDER.create(301, "Moved Permanently");

  public static final HttpStatus FOUND = BUILDER.create(302, "Found");

  public static final HttpStatus SEE_OTHER = BUILDER.create(303, "See Other");

  public static final HttpStatus NOT_MODIFIED = BUILDER.create(304, "Not Modified");

  // 4.x.x

  public static final HttpStatus BAD_REQUEST = BUILDER.create(400, "Bad Request");

  public static final HttpStatus FORBIDDEN = BUILDER.create(403, "Forbidden");

  public static final HttpStatus NOT_FOUND = BUILDER.create(404, "Not Found");

  public static final HttpStatus METHOD_NOT_ALLOWED = BUILDER.create(405, "Method Not Allowed");

  public static final HttpStatus LENGTH_REQUIRED = BUILDER.create(411, "Length Required");

  public static final HttpStatus CONTENT_TOO_LARGE = BUILDER.create(413, "Content Too Large");

  public static final HttpStatus URI_TOO_LONG = BUILDER.create(414, "URI Too Long");

  public static final HttpStatus UNSUPPORTED_MEDIA_TYPE = BUILDER.create(415, "Unsupported Media Type");

  public static final HttpStatus UNPROCESSABLE_CONTENT = BUILDER.create(422, "Unprocessable Content");

  public static final HttpStatus REQUEST_HEADER_FIELDS_TOO_LARGE = BUILDER.create(431, "Request Header Fields Too Large");

  // 5.x.x

  public static final HttpStatus INTERNAL_SERVER_ERROR = BUILDER.create(500, "Internal Server Error");

  public static final HttpStatus NOT_IMPLEMENTED = BUILDER.create(501, "Not Implemented");

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

  public static HttpStatus[] values() {
    return VALUES.clone();
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