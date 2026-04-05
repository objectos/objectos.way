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

import java.util.Objects;

/**
 * An HTTP header name.
 */
public sealed interface HttpHeaderName permits HttpHeaderName0 {

  /**
   * Creates a new {@code HeaderName} instance with the specified name.
   *
   * @param name
   *        the name of the HTTP header field
   *
   * @return a newly created {@code HeaderName} instance
   */
  static HttpHeaderName of(String name) {
    Objects.requireNonNull(name, "name == null");

    return HttpHeaderName0.of(name);
  }

  /**
   * The {@code Accept-Encoding} header name.
   */
  HttpHeaderName ACCEPT_ENCODING = HttpHeaderName0.ACCEPT_ENCODING;

  /**
   * The {@code Allow} header name.
   */
  HttpHeaderName ALLOW = HttpHeaderName0.ALLOW;

  /**
   * The {@code Connection} header name.
   */
  HttpHeaderName CONNECTION = HttpHeaderName0.CONNECTION;

  /**
   * The {@code Content-Disposition} header name.
   */
  HttpHeaderName CONTENT_DISPOSITION = HttpHeaderName0.CONTENT_DISPOSITION;

  /**
   * The {@code Content-Length} header name.
   */
  HttpHeaderName CONTENT_LENGTH = HttpHeaderName0.CONTENT_LENGTH;

  /**
   * The {@code Content-Type} header name.
   */
  HttpHeaderName CONTENT_TYPE = HttpHeaderName0.CONTENT_TYPE;

  /**
   * The {@code Cookie} header name.
   */
  HttpHeaderName COOKIE = HttpHeaderName0.COOKIE;

  /**
   * The {@code Date} header name.
   */
  HttpHeaderName DATE = HttpHeaderName0.DATE;

  /**
   * The {@code ETag} header name.
   */
  HttpHeaderName ETAG = HttpHeaderName0.ETAG;

  /**
   * The {@code From} header name.
   */
  HttpHeaderName FROM = HttpHeaderName0.FROM;

  /**
   * The {@code Host} header name.
   */
  HttpHeaderName HOST = HttpHeaderName0.HOST;

  /**
   * The {@code If-None-Match} header name.
   */
  HttpHeaderName IF_NONE_MATCH = HttpHeaderName0.IF_NONE_MATCH;

  /**
   * The {@code Location} header name.
   */
  HttpHeaderName LOCATION = HttpHeaderName0.LOCATION;

  /**
   * The {@code Referer} header name.
   */
  HttpHeaderName REFERER = HttpHeaderName0.REFERER;

  /**
   * The {@code Set-Cookie} header name.
   */
  HttpHeaderName SET_COOKIE = HttpHeaderName0.SET_COOKIE;

  /**
   * The {@code Transfer-Encoding} header name.
   */
  HttpHeaderName TRANSFER_ENCODING = HttpHeaderName0.TRANSFER_ENCODING;

  /**
   * The {@code User-Agent} header name.
   */
  HttpHeaderName USER_AGENT = HttpHeaderName0.USER_AGENT;

  /**
   * The {@code Way-CSRF-Token} header name.
   */
  HttpHeaderName WAY_CSRF_TOKEN = HttpHeaderName0.WAY_CSRF_TOKEN;

  /**
   * The {@code Way-Request} header name.
   */
  HttpHeaderName WAY_REQUEST = HttpHeaderName0.WAY_REQUEST;

  /**
   * Returns this name in header case, i.e., first letter of each word
   * capitalized.
   *
   * @return this name in header case.
   */
  String headerCase();

  /**
   * Returns this name in lower case.
   *
   * @return this name in lower case.
   */
  String lowerCase();

}