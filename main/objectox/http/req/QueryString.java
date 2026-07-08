/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.req;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import objectos.http.Request;

public final class QueryString {

  private final Map<String, Object> params;

  public QueryString(Map<String, Object> params) {
    this.params = params;
  }

  public static QueryString copyOf(Request request) {
    final RequestPojo pojo;
    pojo = (RequestPojo) request;

    final Map<String, Object> original;
    original = pojo.queryParams();

    final LinkedHashMap<String, Object> copy;
    copy = new LinkedHashMap<>(original);

    return new QueryString(copy);
  }

  public final Object put(String name, String value) {
    return params.put(name, value);
  }

  @Override
  public final String toString() {
    if (params.isEmpty()) {
      return "";
    }

    final StringBuilder sb;
    sb = new StringBuilder();

    for (String key : params.keySet()) {
      if (!sb.isEmpty()) {
        sb.append('&');
      }

      final Object value;
      value = params.get(key);

      if (value instanceof List<?> list) {
        final Iterator<?> iter;
        iter = list.iterator();

        if (iter.hasNext()) {
          item(sb, key, iter.next());

          while (iter.hasNext()) {
            sb.append('&');

            item(sb, key, iter.next());
          }
        }
      } else {
        item(sb, key, value);
      }
    }

    return sb.toString();
  }

  private void item(StringBuilder sb, String key, Object value) {
    sb.append(key);

    sb.append('=');

    sb.append(value);
  }

}
