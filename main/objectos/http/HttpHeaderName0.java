/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import objectos.internal.Util;

final class HttpHeaderName0 implements HttpHeaderName {

  private static Builder B = new Builder();

  static final HttpHeaderName0 ACCEPT_ENCODING = B.std("Accept-Encoding", HttpHeaderType.REQUEST);
  static final HttpHeaderName0 ALLOW = B.std("Allow", HttpHeaderType.RESPONSE);
  static final HttpHeaderName0 CONNECTION = B.std("Connection", HttpHeaderType.BOTH);
  static final HttpHeaderName0 CONTENT_DISPOSITION = B.std("Content-Disposition", HttpHeaderType.RESPONSE);
  static final HttpHeaderName0 CONTENT_LENGTH = B.std("Content-Length", HttpHeaderType.BOTH);
  static final HttpHeaderName0 CONTENT_TYPE = B.std("Content-Type", HttpHeaderType.BOTH);
  static final HttpHeaderName0 COOKIE = B.std("Cookie", HttpHeaderType.REQUEST);
  static final HttpHeaderName0 DATE = B.std("Date", HttpHeaderType.BOTH);
  static final HttpHeaderName0 ETAG = B.std("ETag", HttpHeaderType.RESPONSE);
  static final HttpHeaderName0 FROM = B.std("From", HttpHeaderType.REQUEST);
  static final HttpHeaderName0 HOST = B.std("Host", HttpHeaderType.REQUEST);
  static final HttpHeaderName0 IF_NONE_MATCH = B.std("If-None-Match", HttpHeaderType.REQUEST);
  static final HttpHeaderName0 LOCATION = B.std("Location", HttpHeaderType.RESPONSE);
  static final HttpHeaderName0 REFERER = B.std("Referer", HttpHeaderType.REQUEST);
  static final HttpHeaderName0 SET_COOKIE = B.std("Set-Cookie", HttpHeaderType.RESPONSE);
  static final HttpHeaderName0 TRANSFER_ENCODING = B.std("Transfer-Encoding", HttpHeaderType.BOTH);
  static final HttpHeaderName0 USER_AGENT = B.std("User-Agent", HttpHeaderType.REQUEST);
  static final HttpHeaderName0 WAY_CSRF_TOKEN = B.std("Way-CSRF-Token", HttpHeaderType.REQUEST);
  static final HttpHeaderName0 WAY_REQUEST = B.std("Way-Request", HttpHeaderType.REQUEST);

  private static final class Builder {

    private int index;

    private final Set<HttpHeaderName0> values = new LinkedHashSet<>();

    final HttpHeaderName0 std(String headerCase, HttpHeaderType type) {
      final String lowerCase;
      lowerCase = headerCase.toLowerCase(Locale.US);

      final HttpHeaderName0 name;
      name = new HttpHeaderName0(index++, headerCase, lowerCase, type);

      if (values.add(name)) {
        return name;
      } else {
        throw new IllegalArgumentException("Duplicate header name " + name);
      }
    }

    final HttpHeaderName0[] values() {
      return values.toArray(HttpHeaderName0[]::new);
    }

  }

  static final HttpHeaderName0[] VALUES;

  private static final Map<String, HttpHeaderName0> BY_LOWER_CASE;

  private static final byte[] TABLE;

  static final byte INVALID = -1;

  static final byte COLON = -2;

  static {
    VALUES = B.values();

    final Map<String, HttpHeaderName0> byLowerCase;
    byLowerCase = Util.createMap();

    for (HttpHeaderName0 value : VALUES) {
      byLowerCase.put(value.lowerCase, value);
    }

    BY_LOWER_CASE = Util.toUnmodifiableMap(byLowerCase);

    B = null;

    final String tokenChars;
    tokenChars = "!#$%&'*+-.^`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    final String lowerChars;
    lowerChars = "!#$%&'*+-.^`|~abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789";

    final byte[] table;
    table = new byte[128];

    Arrays.fill(table, INVALID);

    for (int i = 0, len = tokenChars.length(); i < len; i++) {
      final char token;
      token = tokenChars.charAt(i);

      final char lower;
      lower = lowerChars.charAt(i);

      table[token] = (byte) lower;
    }

    table[':'] = COLON;

    TABLE = table;
  }

  private final int index;

  private String headerCase;

  private byte[] headerCaseBytes;

  private final String lowerCase;

  private final HttpHeaderType type;

  private HttpHeaderName0(int index, String headerCase, String lowerCase, HttpHeaderType type) {
    this.index = index;
    this.headerCase = headerCase;
    this.lowerCase = lowerCase;
    this.type = type;
  }

  public static HttpHeaderName0 byLowerCase(String name) {
    return BY_LOWER_CASE.get(name);
  }

  public static byte map(byte b) {
    if (b < 0) {
      return -1;
    }

    return TABLE[b];
  }

  public static byte map(char b) {
    if (b < 0) {
      return -1;
    }

    if (b > 127) {
      return -1;
    }

    return TABLE[b];
  }

  public static HttpHeaderName0 of(String name) {
    // let's be optimistic
    final HttpHeaderName0 first;
    first = BY_LOWER_CASE.get(name);

    if (first != null) {
      return first;
    }

    final String lowerCase;
    lowerCase = name.toLowerCase(Locale.US);

    final HttpHeaderName0 second;
    second = BY_LOWER_CASE.get(lowerCase);

    if (second != null) {
      return second;
    }

    StringBuilder sb;
    sb = null;

    for (int idx = 0, len = name.length(); idx < len; idx++) {
      final char original;
      original = name.charAt(idx);

      final byte mapped;
      mapped = map(original);

      if (mapped < 0) {
        throw new IllegalArgumentException("Invalid header name character '" + original + "' at index " + idx);
      }

      if (sb != null) {
        sb.append((char) mapped);
      } else if (mapped != original) {
        sb = new StringBuilder();

        sb.append(name, 0, idx);

        sb.append((char) mapped);
      }
    }

    final String lowerCaseName;

    if (sb != null) {
      lowerCaseName = sb.toString();
    } else {
      lowerCaseName = name;
    }

    return ofLowerCase(lowerCaseName);
  }

  static HttpHeaderName0 ofLowerCase(String lowerCase) {
    return new HttpHeaderName0(-1, null, lowerCase, HttpHeaderType.BOTH);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof HttpHeaderName0 that
        && lowerCase.contentEquals(that.lowerCase);
  }

  @Override
  public final int hashCode() {
    return lowerCase.hashCode();
  }

  @Override
  public final String headerCase() {
    // benign data race

    String result;
    result = headerCase;

    if (result == null) {
      final StringBuilder sb;
      sb = new StringBuilder();

      boolean capitalizeNext;
      capitalizeNext = true;

      for (int i = 0, len = lowerCase.length(); i < len; i++) {
        final char lower;
        lower = lowerCase.charAt(i);

        if (lower == '-') {
          sb.append('-');

          capitalizeNext = true;
        } else if (capitalizeNext) {
          final char upper;
          upper = Character.toUpperCase(lower);

          sb.append(upper);

          capitalizeNext = false;
        } else {
          sb.append(lower);
        }
      }

      result = sb.toString();

      headerCase = result;
    }

    return result;
  }

  public final int index() {
    return index;
  }

  public final boolean isResponseOnly() {
    return type == HttpHeaderType.RESPONSE;
  }

  @Override
  public final String lowerCase() {
    return lowerCase;
  }

  @Override
  public final String toString() {
    return "Http.Header.Name[" + lowerCase + "]";
  }

  public final HttpHeaderType type() {
    return type;
  }

  final byte[] headerCaseBytes() {
    // benign data race

    byte[] result;
    result = headerCaseBytes;

    if (result == null) {
      result = headerCase().getBytes(StandardCharsets.US_ASCII);

      headerCaseBytes = result;
    }

    return result;
  }

}
