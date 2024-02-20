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
package objectos.http;

import java.io.IOException;
import java.io.InputStream;
import objectos.util.map.GrowableMap;
import objectos.util.map.UnmodifiableMap;

final class WayFormUrlEncoded implements FormUrlEncoded {

  private static final String EMPTY = "";

  private final UnmodifiableMap<String, String> map;

  private WayFormUrlEncoded(UnmodifiableMap<String, String> map) {
    this.map = map;
  }

  static WayFormUrlEncoded parse(Body body) throws IOException {
    try (InputStream in = body.openStream()) {
      return parse0(in);
    }
  }

  static WayFormUrlEncoded parse(ServerExchange http) throws UnsupportedMediaTypeException, IOException {
    ServerRequestHeaders headers;
    headers = http.headers();

    String contentType;
    contentType = headers.first(HeaderName.CONTENT_TYPE);

    if (!contentType.equals("application/x-www-form-urlencoded")) {
      throw new UnsupportedMediaTypeException(contentType);
    }

    Body body;
    body = http.body();

    return parse(body);
  }

  private static WayFormUrlEncoded parse0(InputStream in) throws IOException {
    GrowableMap<String, String> map;
    map = new GrowableMap<>();

    StringBuilder sb;
    sb = new StringBuilder();

    String key;
    key = null;

    int c;

    while ((c = in.read()) != -1) {
      switch (c) {
        case '=' -> {
          key = sb.toString();

          sb.setLength(0);

          String oldValue;
          oldValue = map.put(key, EMPTY);

          if (oldValue != null) {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case '&' -> {
          if (key == null) {
            throw new UnsupportedOperationException("Implement me");
          }

          String value;
          value = sb.toString();

          sb.setLength(0);

          String oldValue;
          oldValue = map.put(key, value);

          if (oldValue != EMPTY) {
            throw new UnsupportedOperationException("Implement me");
          }

          key = null;
        }

        case '%' -> {
          c = in.read();

          if (c == -1) {
            throw new IllegalArgumentException(
                "Invalid % escape sequence: no data"
            );
          }

          int high;
          high = Bytes.parseHexDigit(c);

          c = in.read();

          if (c == -1) {
            throw new IllegalArgumentException(
                "Invalid % escape sequence: not enough data"
            );
          }

          int low;
          low = Bytes.parseHexDigit(c);

          int value;
          value = high << 4 | low;

          sb.append((char) value);
        }

        default -> sb.append((char) c);
      }
    }

    String value;
    value = sb.toString();

    if (key != null) {
      map.put(key, value);
    }

    return new WayFormUrlEncoded(
        map.toUnmodifiableMap()
    );
  }

  @Override
  public final String get(String key) {
    return map.get(key);
  }

  @Override
  public final String getOrDefault(String key, String defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public final int size() {
    return map.size();
  }

}