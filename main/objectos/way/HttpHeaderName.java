/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import objectos.way.Http.HeaderName;

final class HttpHeaderName implements Http.HeaderName {

  private static class Builder {

    private final List<HttpHeaderName> standardNames = new ArrayList<>();

    private int index;

    public final Http.HeaderName create(String name, HttpHeaderType type) {
      HttpHeaderName result;
      result = new HttpHeaderName(index++, name);

      standardNames.add(result);

      return result;
    }

    public final HttpHeaderName[] buildNames() {
      return standardNames.toArray(HttpHeaderName[]::new);
    }

  }

  record HeaderNameType(HttpHeaderName name, HttpHeaderType type) {}

  private static Builder BUILDER = new Builder();

  /**
   * The {@code Accept-Encoding} header name.
   */
  public static final HeaderName ACCEPT_ENCODING = BUILDER.create("Accept-Encoding", HttpHeaderType.REQUEST);

  /**
   * The {@code Connection} header name.
   */
  public static final HeaderName CONNECTION = BUILDER.create("Connection", HttpHeaderType.BOTH);

  /**
   * The {@code Content-Length} header name.
   */
  public static final HeaderName CONTENT_LENGTH = BUILDER.create("Content-Length", HttpHeaderType.BOTH);

  /**
   * The {@code Content-Type} header name.
   */
  public static final HeaderName CONTENT_TYPE = BUILDER.create("Content-Type", HttpHeaderType.BOTH);

  /**
   * The {@code Cookie} header name.
   */
  public static final HeaderName COOKIE = BUILDER.create("Cookie", HttpHeaderType.REQUEST);

  /**
   * The {@code Date} header name.
   */
  public static final HeaderName DATE = BUILDER.create("Date", HttpHeaderType.BOTH);

  /**
   * The {@code ETag} header name.
   */
  public static final HeaderName ETAG = BUILDER.create("ETag", HttpHeaderType.RESPONSE);

  /**
   * The {@code From} header name.
   */
  public static final HeaderName FROM = BUILDER.create("From", HttpHeaderType.REQUEST);

  /**
   * The {@code Host} header name.
   */
  public static final HeaderName HOST = BUILDER.create("Host", HttpHeaderType.REQUEST);

  /**
   * The {@code If-None-Match} header name.
   */
  public static final HeaderName IF_NONE_MATCH = BUILDER.create("If-None-Match", HttpHeaderType.REQUEST);

  /**
   * The {@code Location} header name.
   */
  public static final HeaderName LOCATION = BUILDER.create("Location", HttpHeaderType.RESPONSE);

  /**
   * The {@code Set-Cookie} header name.
   */
  public static final HeaderName SET_COOKIE = BUILDER.create("Set-Cookie", HttpHeaderType.RESPONSE);

  /**
   * The {@code Transfer-Encoding} header name.
   */
  public static final HeaderName TRANSFER_ENCODING = BUILDER.create("Transfer-Encoding", HttpHeaderType.BOTH);

  /**
   * The {@code User-Agent} header name.
   */
  public static final HeaderName USER_AGENT = BUILDER.create("User-Agent", HttpHeaderType.REQUEST);

  private static final HttpHeaderName[] STANDARD_NAMES;

  private static final Map<String, HttpHeaderName> FIND_BY_NAME;

  static {
    STANDARD_NAMES = BUILDER.buildNames();

    Map<String, HttpHeaderName> findByName;
    findByName = Util.createMap();

    for (HttpHeaderName value : STANDARD_NAMES) {
      findByName.put(value.capitalized, value);
    }

    FIND_BY_NAME = Util.toUnmodifiableMap(findByName);

    BUILDER = null;
  }

  private final int index;

  private final String capitalized;

  @SuppressWarnings("unused")
  private final String lowerCase;

  public HttpHeaderName(int index, String capitalized) {
    this.index = index;

    this.capitalized = capitalized;

    this.lowerCase = capitalized.toLowerCase(Locale.US);
  }

  public HttpHeaderName(String name) {
    this(-1, name);
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
  public final String capitalized() {
    return capitalized;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof HttpHeaderName that
        && capitalized.equals(that.capitalized);
  }

  @Override
  public final int hashCode() {
    return capitalized.hashCode();
  }

  @Override
  public final int index() {
    return index;
  }

}
