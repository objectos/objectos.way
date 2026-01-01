/*
 * Copyright (C) 2026 Objectos Software LTDA.
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

enum HttpStatus implements Http.Status {

  // 2.x.x

  OK(200, "OK"),

  CREATED(201, "Created"),

  NO_CONTENT(204, "No Content"),

  // 3.x.x

  MOVED_PERMANENTLY(301, "Moved Permanently"),

  FOUND(302, "Found"),

  SEE_OTHER(303, "See Other"),

  NOT_MODIFIED(304, "Not Modified"),

  // 4.x.x

  BAD_REQUEST(400, "Bad Request"),

  FORBIDDEN(403, "Forbidden"),

  NOT_FOUND(404, "Not Found"),

  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  LENGTH_REQUIRED(411, "Length Required"),

  CONTENT_TOO_LARGE(413, "Content Too Large"),

  URI_TOO_LONG(414, "URI Too Long"),

  UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

  UNPROCESSABLE_CONTENT(422, "Unprocessable Content"),

  REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),

  // 5.x.x

  INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

  NOT_IMPLEMENTED(501, "Not Implemented"),

  HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

  public final int code;

  public final String reasonPhrase;

  private HttpStatus(int code, String reasonPhrase) {
    this.code = code;

    this.reasonPhrase = reasonPhrase;
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
  public final String toString() {
    return "HttpStatus[" + code + "=" + reasonPhrase + "]";
  }

}