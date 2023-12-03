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
package objectox.http;

import objectos.http.HeaderName;

public enum StandardHeaderName implements HeaderName {

  ACCEPT_ENCODING("Accept-Encoding", HeaderType.REQUEST),

  CONNECTION("Connection", HeaderType.BOTH),

  CONTENT_LENGTH("Content-Length", HeaderType.BOTH),

  CONTENT_TYPE("Content-Type", HeaderType.BOTH),

  DATE("Date", HeaderType.BOTH),

  HOST("Host", HeaderType.REQUEST),

  LOCATION("Location", HeaderType.RESPONSE),

  SET_COOKIE("Set-Cookie", HeaderType.RESPONSE),

  TRANSFER_ENCODING("Transfer-Encoding", HeaderType.BOTH),

  USER_AGENT("User-Agent", HeaderType.REQUEST);

  public final String name;

  public final HeaderType type;

  private StandardHeaderName(String name, HeaderType type) {
    this.name = name;

    this.type = type;
  }

  @Override
  public final String capitalized() {
    return name;
  }

}