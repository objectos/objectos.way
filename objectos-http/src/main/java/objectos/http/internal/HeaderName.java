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

public enum HeaderName {

  ACCEPT_ENCODING("Accept-Encoding"),

  CONNECTION("Connection"),

  CONTENT_LENGTH("Content-Length"),

  CONTENT_TYPE("Content-Type"),

  DATE("Date"),

  HOST("Host"),

  USER_AGENT("User-Agent");

  final byte[] bytes;

  public final String name;

  private HeaderName(String name) {
    this.name = name;

    bytes = Bytes.utf8(name);
  }

}