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

/**
 * The status of an HTTP response message.
 */
public sealed interface HttpStatus permits HttpStatusImpl {

  // Response constants

  // 2.x.x

  /**
   * The {@code 200 OK} status.
   */
  HttpStatus OK = HttpStatusImpl.OK;

  /**
   * The {@code 201 Created} status.
   */
  HttpStatus CREATED = HttpStatusImpl.CREATED;

  /**
   * The {@code 204 No Content} status.
   */
  HttpStatus NO_CONTENT = HttpStatusImpl.NO_CONTENT;

  // 3.x.x

  /**
   * The {@code 301 Moved Permanently} status.
   */
  HttpStatus MOVED_PERMANENTLY = HttpStatusImpl.MOVED_PERMANENTLY;

  /**
   * The {@code 302 Found} status.
   */
  HttpStatus FOUND = HttpStatusImpl.FOUND;

  /**
   * The {@code 303 See Other} status.
   */
  HttpStatus SEE_OTHER = HttpStatusImpl.SEE_OTHER;

  /**
   * The {@code 304 Not Modified} status.
   */
  HttpStatus NOT_MODIFIED = HttpStatusImpl.NOT_MODIFIED;

  // 4.x.x

  /**
   * The {@code 400 Bad Request} status.
   */
  HttpStatus BAD_REQUEST = HttpStatusImpl.BAD_REQUEST;

  /**
   * The {@code 403 Forbidden} status.
   */
  HttpStatus FORBIDDEN = HttpStatusImpl.FORBIDDEN;

  /**
   * The {@code 404 Not Found} status.
   */
  HttpStatus NOT_FOUND = HttpStatusImpl.NOT_FOUND;

  /**
   * The {@code 405 Method Not Allowed} status.
   */
  HttpStatus METHOD_NOT_ALLOWED = HttpStatusImpl.METHOD_NOT_ALLOWED;

  /**
   * The {@code 411 Length Required} status.
   */
  HttpStatus LENGTH_REQUIRED = HttpStatusImpl.LENGTH_REQUIRED;

  /**
   * The {@code 413 Content Too Large} status.
   */
  HttpStatus CONTENT_TOO_LARGE = HttpStatusImpl.CONTENT_TOO_LARGE;

  /**
   * The {@code 414 URI Too Long} status.
   */
  HttpStatus URI_TOO_LONG = HttpStatusImpl.URI_TOO_LONG;

  /**
   * The {@code 415 UNSUPPORTED MEDIA TYPE} status.
   */
  HttpStatus UNSUPPORTED_MEDIA_TYPE = HttpStatusImpl.UNSUPPORTED_MEDIA_TYPE;

  /**
   * The {@code 422 UNPROCESSABLE CONTENT} status.
   */
  HttpStatus UNPROCESSABLE_CONTENT = HttpStatusImpl.UNPROCESSABLE_CONTENT;

  /**
   * The {@code 431 Request Header Fields Too Large} status.
   */
  HttpStatus REQUEST_HEADER_FIELDS_TOO_LARGE = HttpStatusImpl.REQUEST_HEADER_FIELDS_TOO_LARGE;

  // 5.x.x

  /**
   * The {@code 500 INTERNAL SERVER ERROR} status.
   */
  HttpStatus INTERNAL_SERVER_ERROR = HttpStatusImpl.INTERNAL_SERVER_ERROR;

  /**
   * The {@code 501 NOT IMPLEMENTED} status.
   */
  HttpStatus NOT_IMPLEMENTED = HttpStatusImpl.NOT_IMPLEMENTED;

  /**
   * The {@code 505 HTTP VERSION NOT SUPPORTED} status.
   */
  HttpStatus HTTP_VERSION_NOT_SUPPORTED = HttpStatusImpl.HTTP_VERSION_NOT_SUPPORTED;

  /**
   * The code of this status.
   *
   * @return the code of this status.
   */
  int code();

  /**
   * The reason-phrase of this status.
   *
   * @return the reason-phrase of this status.
   */
  String reasonPhrase();

}