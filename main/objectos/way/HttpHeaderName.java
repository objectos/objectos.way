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

import java.util.Map;

enum HttpHeaderName implements Http.HeaderName {

  /**
   * The {@code Accept-Encoding} header name.
   */
  ACCEPT_ENCODING("Accept-Encoding", HttpHeaderType.REQUEST),

  /**
   * The {@code Connection} header name.
   */
  CONNECTION("Connection", HttpHeaderType.BOTH),

  /**
   * The {@code Content-Length} header name.
   */
  CONTENT_LENGTH("Content-Length", HttpHeaderType.BOTH),

  /**
   * The {@code Content-Type} header name.
   */
  CONTENT_TYPE("Content-Type", HttpHeaderType.BOTH),

  /**
   * The {@code Cookie} header name.
   */
  COOKIE("Cookie", HttpHeaderType.REQUEST),

  /**
   * The {@code Date} header name.
   */
  DATE("Date", HttpHeaderType.BOTH),

  /**
   * The {@code ETag} header name.
   */
  ETAG("ETag", HttpHeaderType.RESPONSE),

  /**
   * The {@code From} header name.
   */
  FROM("From", HttpHeaderType.REQUEST),

  /**
   * The {@code Host} header name.
   */
  HOST("Host", HttpHeaderType.REQUEST),

  /**
   * The {@code If-None-Match} header name.
   */
  IF_NONE_MATCH("If-None-Match", HttpHeaderType.REQUEST),

  /**
   * The {@code Location} header name.
   */
  LOCATION("Location", HttpHeaderType.RESPONSE),

  /**
   * The {@code Set-Cookie} header name.
   */
  SET_COOKIE("Set-Cookie", HttpHeaderType.RESPONSE),

  /**
   * The {@code Transfer-Encoding} header name.
   */
  TRANSFER_ENCODING("Transfer-Encoding", HttpHeaderType.BOTH),

  /**
   * The {@code User-Agent} header name.
   */
  USER_AGENT("User-Agent", HttpHeaderType.REQUEST),

  WAY_REQUEST("Way-Request", HttpHeaderType.REQUEST);

  private static final HttpHeaderName[] STANDARD_NAMES;

  private static final Map<String, HttpHeaderName> FIND_BY_NAME;

  static {
    STANDARD_NAMES = HttpHeaderName.values();

    Map<String, HttpHeaderName> findByName;
    findByName = Util.createMap();

    for (HttpHeaderName value : STANDARD_NAMES) {
      findByName.put(value.capitalized, value);
    }

    FIND_BY_NAME = Util.toUnmodifiableMap(findByName);
  }

  private final String capitalized;

  private final HttpHeaderType type;

  private HttpHeaderName(String capitalized, HttpHeaderType type) {
    this.capitalized = capitalized;

    this.type = type;
  }

  public static HttpHeaderName findByName(String name) {
    return FIND_BY_NAME.get(name);
  }

  public static HttpHeaderName standardName(int index) {
    return STANDARD_NAMES[index];
  }

  public static int standardNamesSize() {
    return STANDARD_NAMES.length;
  }

  @Override
  public final int index() {
    return ordinal();
  }

  public final boolean isResponseOnly() {
    return type == HttpHeaderType.RESPONSE;
  }

  @Override
  public final String capitalized() {
    return capitalized;
  }

  public final HttpHeaderType type() {
    return type;
  }

}
