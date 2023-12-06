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
package objectox.http.server;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import objectos.http.server.UriQuery;
import objectos.lang.object.Check;
import objectos.util.map.GrowableMap;

public final class ObjectoxUriQuery implements UriQuery {

  private final byte[] buffer;

  final int startIndex;

  private int length;

  private String value;

  private Map<String, Object> params;

  public ObjectoxUriQuery(byte[] buffer, int startIndex) {
    this.buffer = buffer;

    this.startIndex = startIndex;
  }

  @Override
  public final String get(String name) {
    Check.notNull(name, "name == null");

    Map<String, Object> params;
    params = params();

    Object maybe;
    maybe = params.get(name);

    if (maybe == null) {
      return null;
    }

    if (maybe instanceof String s) {
      return s;
    }

    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final String value() {
    if (value == null) {
      Charset charset;
      charset = StandardCharsets.UTF_8;

      String raw;
      raw = new String(buffer, startIndex, length, charset);

      value = URLDecoder.decode(raw, charset);
    }

    return value;
  }

  @Override
  public final boolean isEmpty() {
    return params().isEmpty();
  }

  final void end(int endIndex) {
    this.length = endIndex - startIndex;
  }

  private Map<String, Object> params() {
    if (params == null) {
      params = makeParams();
    }

    return params;
  }

  private Map<String, Object> makeParams() {
    String source;
    source = value();

    if (source.isBlank()) {
      return Map.of();
    }

    GrowableMap<String, Object> map;
    map = new GrowableMap<>();

    StringBuilder sb;
    sb = new StringBuilder();

    String key;
    key = null;

    for (int i = 0, len = source.length(); i < len; i++) {
      char c;
      c = source.charAt(i);

      switch (c) {
        case '=' -> {
          key = sb.toString();

          sb.setLength(0);

          Object oldValue;
          oldValue = map.put(key, "");

          if (oldValue != null) {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case '&' -> {
          String value;
          value = sb.toString();

          sb.setLength(0);

          if (key == null) {
            map.put(value, "");

            continue;
          }

          Object oldValue;
          oldValue = map.put(key, value);

          if (oldValue != "") {
            throw new UnsupportedOperationException("Implement me");
          }

          key = null;
        }

        default -> sb.append(c);
      }
    }

    String value;
    value = sb.toString();

    if (key != null) {
      map.put(key, value);
    } else {
      map.put(value, "");
    }

    return map;
  }

}