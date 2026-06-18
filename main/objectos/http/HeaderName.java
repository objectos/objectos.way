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
import objectox.http.HeaderNamePojo;

/// An HTTP header name.
public sealed interface HeaderName permits HeaderNamePojo {

  /// Creates a new `HeaderName` instance with the specified name.
  ///
  /// @param name the name of the HTTP header field
  /// 
  /// @return a newly created `HeaderName` instance
  static HeaderName of(String name) {
    Objects.requireNonNull(name, "name == null");

    return HeaderNamePojo.of(name);
  }

  /// The `Accept-Encoding` header name.
  HeaderName ACCEPT_ENCODING = HeaderNamePojo.ACCEPT_ENCODING;

  /// The `Allow` header name.
  HeaderName ALLOW = HeaderNamePojo.ALLOW;

  /// The `Connection` header name.
  HeaderName CONNECTION = HeaderNamePojo.CONNECTION;

  /// The `Content-Disposition` header name.
  HeaderName CONTENT_DISPOSITION = HeaderNamePojo.CONTENT_DISPOSITION;

  /// The `Content-Length` header name.
  HeaderName CONTENT_LENGTH = HeaderNamePojo.CONTENT_LENGTH;

  /// The `Content-Type` header name.
  HeaderName CONTENT_TYPE = HeaderNamePojo.CONTENT_TYPE;

  /// The `Cookie` header name.
  HeaderName COOKIE = HeaderNamePojo.COOKIE;

  /// The `Date` header name.
  HeaderName DATE = HeaderNamePojo.DATE;

  /// The `ETag` header name.
  HeaderName ETAG = HeaderNamePojo.ETAG;

  /// The `From` header name.
  HeaderName FROM = HeaderNamePojo.FROM;

  /// The `Host` header name.
  HeaderName HOST = HeaderNamePojo.HOST;

  /// The `If-None-Match` header name.
  HeaderName IF_NONE_MATCH = HeaderNamePojo.IF_NONE_MATCH;

  /// The `Location` header name.
  HeaderName LOCATION = HeaderNamePojo.LOCATION;

  /// The `Referer` header name.
  HeaderName REFERER = HeaderNamePojo.REFERER;

  /// The `Set-Cookie` header name.
  HeaderName SET_COOKIE = HeaderNamePojo.SET_COOKIE;

  /// The `Transfer-Encoding` header name.
  HeaderName TRANSFER_ENCODING = HeaderNamePojo.TRANSFER_ENCODING;

  /// The `User-Agent` header name.
  HeaderName USER_AGENT = HeaderNamePojo.USER_AGENT;

  /// The `Way-CSRF-Token` header name.
  HeaderName WAY_CSRF_TOKEN = HeaderNamePojo.WAY_CSRF_TOKEN;

  /// The `Way-Request` header name.
  HeaderName WAY_REQUEST = HeaderNamePojo.WAY_REQUEST;

  /// Returns this name in header case, i.e., first letter of each word
  /// capitalized.
  ///
  /// @return this name in header case.
  String headerCase();

  /// Returns this name in lower case.
  ///
  /// @return this name in lower case.
  String lowerCase();

}