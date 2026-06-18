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

import objectox.http.resp.StatusEnum;

/// The status of an HTTP response message.
public sealed interface Status extends Result permits StatusEnum {

  // 2.x.x

  /// The `200 OK` status.
  Status OK = StatusEnum.OK;

  /// The `201 Created` status.
  Status CREATED = StatusEnum.CREATED;

  /// The `204 No Content` status.
  Status NO_CONTENT = StatusEnum.NO_CONTENT;

  // 3.x.x

  /// The `301 Moved Permanently` status.
  Status MOVED_PERMANENTLY = StatusEnum.MOVED_PERMANENTLY;

  /// The `302 Found` status.
  Status FOUND = StatusEnum.FOUND;

  /// The `303 See Other` status.
  Status SEE_OTHER = StatusEnum.SEE_OTHER;

  /// The `304 Not Modified` status.
  Status NOT_MODIFIED = StatusEnum.NOT_MODIFIED;

  // 4.x.x

  /// The `400 Bad Request` status.
  Status BAD_REQUEST = StatusEnum.BAD_REQUEST;

  /// The `403 Forbidden` status.
  Status FORBIDDEN = StatusEnum.FORBIDDEN;

  /// The `404 Not Found` status.
  Status NOT_FOUND = StatusEnum.NOT_FOUND;

  /// The `405 Method Not Allowed` status.
  Status METHOD_NOT_ALLOWED = StatusEnum.METHOD_NOT_ALLOWED;

  /// The `411 Length Required` status.
  Status LENGTH_REQUIRED = StatusEnum.LENGTH_REQUIRED;

  /// The `413 Content Too Large` status.
  Status CONTENT_TOO_LARGE = StatusEnum.CONTENT_TOO_LARGE;

  /// The `414 URI Too Long` status.
  Status URI_TOO_LONG = StatusEnum.URI_TOO_LONG;

  /// The `415 UNSUPPORTED MEDIA TYPE` status.
  Status UNSUPPORTED_MEDIA_TYPE = StatusEnum.UNSUPPORTED_MEDIA_TYPE;

  /// The `422 UNPROCESSABLE CONTENT` status.
  Status UNPROCESSABLE_CONTENT = StatusEnum.UNPROCESSABLE_CONTENT;

  /// The `431 Request Header Fields Too Large` status.
  Status REQUEST_HEADER_FIELDS_TOO_LARGE = StatusEnum.REQUEST_HEADER_FIELDS_TOO_LARGE;

  // 5.x.x

  /// The `500 INTERNAL SERVER ERROR` status.
  Status INTERNAL_SERVER_ERROR = StatusEnum.INTERNAL_SERVER_ERROR;

  /// The `501 NOT IMPLEMENTED` status.
  Status NOT_IMPLEMENTED = StatusEnum.NOT_IMPLEMENTED;

  /// The `505 HTTP VERSION NOT SUPPORTED` status.
  Status HTTP_VERSION_NOT_SUPPORTED = StatusEnum.HTTP_VERSION_NOT_SUPPORTED;

  /// The code of this status.
  ///
  /// @return the code of this status.
  int code();

  /// The reason-phrase of this status.
  ///
  /// @return the reason-phrase of this status.
  String reasonPhrase();

}