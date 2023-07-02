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
  URI_TOO_LONG(414),

  // 5.x.x
  INTERNAL_SERVER_ERROR(500),
  NOT_IMPLEMENTED(501),
  HTTP_VERSION_NOT_SUPPORTED(505);

  private final int code;

  private HttpStatus(int code) {
    this.code = code;
  }

  public final int code() {
    return code;
  }

}