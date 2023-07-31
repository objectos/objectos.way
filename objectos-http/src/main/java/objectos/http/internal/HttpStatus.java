/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.http.internal;

import objectos.http.Http;

public enum HttpStatus implements Http.Status {

  // 2.x.x
  OK(200),

  // 3.x.x
  FOUND(302),

  // 4.x.x
  BAD_REQUEST(400),
  NOT_FOUND(404),
  METHOD_NOT_ALLOWED(405),
  URI_TOO_LONG(414),
  UNSUPPORTED_MEDIA_TYPE(415),
  UNPROCESSABLE_CONTENT(422),

  // 5.x.x
  INTERNAL_SERVER_ERROR(500),
  NOT_IMPLEMENTED(501),
  HTTP_VERSION_NOT_SUPPORTED(505);

  private final int code;

  final byte[] responseBytes;

  private HttpStatus(int code) {
    this.code = code;

    responseBytes = responseBytes(this);
  }

  public static byte[] responseBytes(Http.Status status) {
    String response;
    response = Integer.toString(status.code()) + " " + status.description() + "\r\n";

    return Bytes.utf8(response);
  }

  @Override
  public final int code() {
    return code;
  }

  @Override
  public final String description() {
    return name().replace('_', ' ');
  }

}