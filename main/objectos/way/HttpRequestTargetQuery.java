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

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;

final class HttpRequestTargetQuery implements Http.Request.Target.Query {

  private final Map<String, Object> decoded = new HashMap<>();

  private String value = "";

  public HttpRequestTargetQuery() {
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

  public final Http.Request.Target.Query set(String name, String value) {
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

          put0(map, key, "");
        }

        case '&' -> {
          String value;
          value = sb.toString();

          sb.setLength(0);

          if (key == null) {
            map.put(value, "");

            continue;
          }

          put0(map, key, value);

          key = null;
        }

        default -> sb.append(c);
      }
    }

    String value;
    value = sb.toString();

    if (key != null) {
      put0(map, key, value);
    } else {
      put0(map, value, "");
    }
  }

  @SuppressWarnings("unchecked")
  private void put0(Map<String, Object> map, String key, String value) {
    Object oldValue;
    oldValue = map.put(key, value);

    if (oldValue == null) {
      return;
    }

    if (oldValue.equals("")) {
      return;
    }

    if (oldValue instanceof String s) {

      if (value.equals("")) {
        map.put(key, s);
      } else {
        List<String> list;
        list = new GrowableList<>();

        list.add(s);

        list.add(value);

        map.put(key, list);
      }
      
    }

    else {
      List<String> list;
      list = (List<String>) oldValue;

      list.add(value);

      map.put(key, list);
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