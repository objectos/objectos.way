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
package objectos.http;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import objectos.lang.object.Check;

final class WayUriQuery implements UriQuery {

  private final Map<String, Object> decoded = new HashMap<>();

  private String value = "";

  public WayUriQuery() {
  }

  final void set(String rawValue) {
    value = URLDecoder.decode(rawValue, StandardCharsets.UTF_8);

    decoded.clear();

    makeDecoded();
  }

  @Override
  public final Set<String> names() {
    return decoded.keySet();
  }

  @Override
  public final String get(String name) {
    Check.notNull(name, "name == null");

    Map<String, Object> params;
    params = decoded;

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

  public final UriQuery set(String name, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(value, "value == null");

    Map<String, Object> params;
    params = decoded;

    params.merge(name, value, (oldValue, newValue) -> newValue);

    return this;
  }

  @Override
  public final String value() {
    return value;
  }

  @Override
  public final boolean isEmpty() {
    return decoded.isEmpty();
  }

  private void makeDecoded() {
    String source;
    source = value();

    if (source.isBlank()) {
      return;
    }

    Map<String, Object> map;
    map = decoded;

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
  }

  @Override
  public final String encodedValue() {
    int count;
    count = 0;

    StringBuilder builder;
    builder = new StringBuilder();

    for (String key : decoded.keySet()) {
      if (count++ > 0) {
        builder.append('&');
      }

      builder.append(encode(key));

      builder.append('=');

      Object value;
      value = decoded.get(key);

      if (value instanceof String s) {
        builder.append(encode(s));
      }

      else {
        throw new UnsupportedOperationException("Implement me");
      }
    }

    return builder.toString();
  }

  private String encode(String key) {
    return URLEncoder.encode(key, StandardCharsets.UTF_8);
  }

}